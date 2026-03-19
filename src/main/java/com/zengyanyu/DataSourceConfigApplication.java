package com.zengyanyu;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

import java.net.InetAddress;

/**
 * @author zengyanyu
 */
@Slf4j
@SpringBootApplication
public class DataSourceConfigApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext ioc = SpringApplication.run(DataSourceConfigApplication.class, args);

        ConfigurableEnvironment env = ioc.getEnvironment();
        logApplicationStartup(env);
    }

    private static void logApplicationStartup(ConfigurableEnvironment env) {
        String protocol = "http";
        String serverPort = env.getProperty("server.port");
        String contextPath = env.getProperty("server.servlet.context-path");
        if (contextPath.startsWith("/") && contextPath.length() == 1) {
            contextPath += "doc.html";
        } else {
            contextPath += "/doc.html";
        }

        String hostAddress = "localhost";
        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
        }
        log.info("应用程序“{}”正在运行中......", env.getProperty("spring.application.name"));
        log.info("接口文档访问 URL:");
        log.info("本地: \t\t{}://localhost:{}{}", protocol, serverPort, contextPath);
        log.info("外部: \t{}://{}:{}{}", protocol, hostAddress, serverPort, contextPath);
        log.info("配置文件: \t{}", env.getActiveProfiles());
    }
}
