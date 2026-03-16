/*
 * Copyright (c) 2026, 曾衍育 All rights reserved.
 * 自定义License声明
 * ZENGYANYU PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.zengyanyu.system.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author zengyanyu
 */
@Getter
@Setter
@ToString
@ApiModel("查询对象")
public class QueryObject {

    @ApiModelProperty(value = "页面数", required = true)
    private Integer pageNum = 1;

    @ApiModelProperty(value = "页面大小", required = true)
    private Integer pageSize = 20;
}
