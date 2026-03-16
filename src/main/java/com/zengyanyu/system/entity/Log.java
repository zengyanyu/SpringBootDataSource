package com.zengyanyu.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

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
    @GenericGenerator(name = "idGenerator", strategy = "com.zengyanyu.system.util.SnowflakeIdUtils")
    private Long id;

    private String name;

}
