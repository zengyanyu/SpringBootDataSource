package com.zengyanyu.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@TableName("userinfo")
public class Userinfo implements Serializable {

    @Id
    @Column(name = "id", columnDefinition = "int8 NOT NULL")
    @GeneratedValue(generator = "idGenerator", strategy = GenerationType.SEQUENCE)
    @GenericGenerator(name = "idGenerator", strategy = "com.zengyanyu.system.util.SnowflakeIdUtils")
    private Long id;

    private String name;
}
