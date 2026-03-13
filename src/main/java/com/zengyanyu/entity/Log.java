package com.zengyanyu.entity;

import cn.hutool.db.DaoTemplate;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author zengyanyu
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@TableName("log")
public class Log implements Serializable {

    @Id
    @Column(name = "id", columnDefinition = "int8 NOT NULL")
    @GeneratedValue(generator = "idGenerator", strategy = GenerationType.SEQUENCE)
    @GenericGenerator(name = "idGenerator", strategy = "com.zengyanyu.util.SnowflakeIdUtils")
    private Long id;

    private String name;

}
