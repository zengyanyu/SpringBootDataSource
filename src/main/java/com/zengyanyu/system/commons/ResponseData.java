package com.zengyanyu.system.commons;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author zengyanyu
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("响应数据")
public class ResponseData<T> {

    public static final String ERROR_CODE = "500";
    public static final String SUCCEED = "200";

    // 状态码:默认为200
    @ApiModelProperty("状态编码")
    private String code = SUCCEED;

    @ApiModelProperty("提示信息")
    private String msg;

    @ApiModelProperty("数据")
    private T data;

    public ResponseData(String msg) {
        this.msg = msg;
    }

    public ResponseData(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResponseData(String msg, T data) {
        this.msg = msg;
        this.data = data;
    }
}
