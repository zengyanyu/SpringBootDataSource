package com.zengyanyu.system.util;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.*;

/**
 * Twitter_Snowflake<br>
 * SnowFlake的结构如下(每部分用-分开):<br>
 * 0 - 0000000000 0000000000 0000000000 0000000000 0 - 00000 - 00000 - 000000000000 <br>
 * 1位标识，由于long基本类型在Java中是带符号的，最高位是符号位，正数是0，负数是1，所以id一般是正数，最高位是0<br>
 * 41位时间戳(毫秒级)，注意，41位时间戳不是存储当前时间的时间戳，而是存储时间戳的差值（当前时间戳 - 开始时间戳)
 * 得到的值），这里的开始时间戳，一般是我们的id生成器开始使用的时间，由我们程序来指定的（如下下面程序IdWorker类的STARTIME属性）。41位的时间戳，可以使用69年，年T = (1L << 41) / (1000L * 60 * 60 * 24 * 365) = 69<br>
 * 10位的数据机器位，可以部署在1024个节点，包括5位datacenterId和5位workerId<br>
 * 12位序列，毫秒内的计数，12位的计数顺序号支持每个节点每毫秒(同一机器，同一时间戳)产生4096个ID序号<br>
 * 加起来刚好64位，为一个Long型。<br>
 * SnowFlake的优点是，整体上按照时间自增排序，并且整个分布式系统内不会产生ID碰撞(由数据中心ID和机器ID作区分)，并且效率较高，经测试，SnowFlake每秒能够产生26万ID左右。
 *
 * @Date 2020/8/28 11:27
 * @Author hsx
 * @author zengyanyu
 */

@Slf4j
public class SnowflakeIdUtils implements Configurable, IdentifierGenerator {

    /**
     * 开始时间戳 (2015-01-01)
     */
    private static final long STARTIME = 1420041600000L;

    /**
     * 机器id所占的位数
     */
    private static final long WORKERIDBITS = 5L;

    /**
     * 数据标识id所占的位数
     */
    private static final long DATACENTERIDBITS = 5L;

    /**
     * 支持的最大机器id，结果是31 (这个移位算法可以很快的计算出几位二进制数所能表示的最大十进制数)
     */
    private static final long MAXWORKERID = ~(-1L << WORKERIDBITS);

    /**
     * 支持的最大数据标识id，结果是31
     */
    private static final long MAXDATACENTERID = ~(-1L << DATACENTERIDBITS);

    /**
     * 序列在id中占的位数
     */
    private static final long SEQUENCEBITS = 12L;

    /**
     * 机器ID向左移12位
     */
    private static final long WORKERIDSHIFT = SEQUENCEBITS;

    /**
     * 数据标识id向左移17位(12+5)
     */
    private static final long DATACENTERIDSHIFT = SEQUENCEBITS + WORKERIDBITS;

    /**
     * 时间戳向左移22位(5+5+12)
     */
    private static final long TIMESTAMPLEFTSHIFT = SEQUENCEBITS + WORKERIDBITS + DATACENTERIDBITS;

    /**
     * 生成序列的掩码，这里为4095 (0b111111111111=0xfff=4095)
     */
    private static final long SEQUENCEMASK = ~(-1L << SEQUENCEBITS);

    /**
     * 数据中心ID(0~31)
     */
    private static final long DATACENTERID = getDatacenterId();

    /**
     * 工作机器ID(0~31)
     */
    private static final long WORKERID = getMaxWorkerId();

    /**
     * 毫秒内序列(0~4095)
     */
    private static long sequence = 0L;

    /**
     * 上次生成ID的时间戳
     */
    private static long lastTimestamp = -1L;

    /**
     * 当前机器 MAC 地址列表
     */
    private static List<byte[]> macs = getMacList();

    /**
     * 获得下一个ID (该方法是线程安全的)
     *
     * @return SnowflakeId
     */
    public static synchronized long nextId() {
        long timestamp = timeGen();

        //如果当前时间小于上一次ID生成的时间戳，说明系统时钟回退过这个时候应当抛出异常
        Assert.isTrue(timestamp >= lastTimestamp, String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));

        //如果是同一时间生成的，则进行毫秒内序列
        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & SEQUENCEMASK;
            //毫秒内序列溢出
            if (sequence == 0) {
                //阻塞到下一个毫秒,获得新的时间戳
                timestamp = tilNextMillis(lastTimestamp);
            }
        }
        //时间戳改变，毫秒内序列重置
        else {
            sequence = 0L;
        }

        //上次生成ID的时间戳
        lastTimestamp = timestamp;

        //移位并通过或运算拼到一起组成64位的ID
        return ((timestamp - STARTIME) << TIMESTAMPLEFTSHIFT)
                | (DATACENTERID << DATACENTERIDSHIFT)
                | (WORKERID << WORKERIDSHIFT)
                | sequence;
    }

    /**
     * 阻塞到下一个毫秒，直到获得新的时间戳
     *
     * @param lastTimestamp 上次生成ID的时间戳
     * @return 当前时间戳
     */
    protected static long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    /**
     * 返回以毫秒为单位的当前时间
     *
     * @return 当前时间(毫秒)
     */
    protected static long timeGen() {
        return System.currentTimeMillis();
    }

    /**
     * 获取数据中心ID
     *
     * @return 数据中心ID
     */
    private static long getDatacenterId() {

        long id = 0L;
        try {
            InetAddress ip = InetAddress.getLocalHost();
            NetworkInterface network = NetworkInterface.getByInetAddress(ip);
            if (network == null) {
                id = 1L;
            } else {
                byte[] mac = getSimpleMacAddress();
                id = ((0x000000FF & (long) mac[mac.length - 1])
                        | (0x0000FF00 & (((long) mac[mac.length - 2]) << 8))) >> 6;
                id = id % (SnowflakeIdUtils.MAXDATACENTERID + 1);
            }
        } catch (Exception e) {
            //" getDatacenterId: " + e.getMessage()
            e.printStackTrace();
        }
        return id;
    }

    /**
     * 获取 WORKERID
     *
     * @return WORKERID
     */
    protected static long getMaxWorkerId() {

        if (SnowflakeIdUtils.MAXWORKERID > MAXWORKERID || SnowflakeIdUtils.MAXWORKERID < 0) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", MAXWORKERID));
        }
        if (SnowflakeIdUtils.DATACENTERID > MAXDATACENTERID || SnowflakeIdUtils.DATACENTERID < 0) {
            throw new IllegalArgumentException(String.format("datacenter Id can't be greater than %d or less than 0", MAXDATACENTERID));
        }

        StringBuilder mpid = new StringBuilder();
        mpid.append(SnowflakeIdUtils.DATACENTERID);
        String name = ManagementFactory.getRuntimeMXBean().getName();
        if (!name.isEmpty()) {
            /*
             * GET jvmPid
             */
            mpid.append(name.split("@")[0]);
        }
        /*
         * MAC + PID 的 hashcode 获取16个低位
         */
        return (mpid.toString().hashCode() & 0xffff) % (SnowflakeIdUtils.MAXWORKERID + 1);
    }

    @Override
    public void configure(Type type, Properties params, ServiceRegistry serviceRegistry) {
        //
    }

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) {
        return nextId();
    }


    /**
     * 返回机器上的网卡的 MAC 地址列表
     *
     * @return MAC 地址列表
     */
    public static List<byte[]> getMacList() {

        List<byte[]> macList = new ArrayList<>();
        try {
            Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
            while (en.hasMoreElements()) {
                NetworkInterface networkInterface = en.nextElement();
                if (Objects.isNull(networkInterface)) {
                    return Collections.emptyList();
                }
                List<InterfaceAddress> addresses = networkInterface.getInterfaceAddresses();
                for (InterfaceAddress addr : addresses) {
                    InetAddress ip = addr.getAddress();
                    NetworkInterface network = NetworkInterface.getByInetAddress(ip);
                    if (Objects.isNull(network)) {
                        continue;
                    }
                    byte[] mac = network.getHardwareAddress();
                    if (Objects.isNull(mac)) {
                        continue;
                    }
                    macList.add(mac);
                }
            }
        } catch (SocketException e) {
            log.error("读取 MAC 地址失败，异常信息：{}", e.getMessage());
            Arrays.stream(e.getStackTrace()).forEach(trace -> log.error(trace.toString()));
        }
        // 同一个网卡的ipv4,ipv6得到的mac都是一样的
        return CollectionUtils.isEmpty(macList) ? Collections.emptyList() : distinct(macList);
    }

    private static List<byte[]> distinct(List<byte[]> macs) {
        for (int i = 0; i < macs.size(); i++) {
            for (int j = 0; j < macs.size(); j++) {
                if (i != j && Arrays.equals(macs.get(i), macs.get(j))) {
                    macs.remove(macs.get(j));
                }
            }
        }
        return macs;
    }

    /**
     * 读取 MAC 列表的首个 MAC 地址
     *
     * @return MAC 地址
     */
    public static byte[] getSimpleMacAddress() {
        if (CollectionUtils.isEmpty(macs)) {
            macs = getMacList();
        }
        return macs.stream().findFirst().orElseThrow(() -> new RuntimeException("系统异常，无法读取该机器 MAC 地址"));
    }

    /**
     * 打印 MAC 地址列表
     *
     * @param macs MAC 地址列表
     */
    public static void printMacList(List<byte[]> macs) {
        macs.forEach(mac -> {
            StringBuilder sb = new StringBuilder();
            sb.delete(0, sb.length());
            for (int i = 0; i < mac.length; i++) {
                sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
            }
            log.info(sb.toString());
        });
    }

}
