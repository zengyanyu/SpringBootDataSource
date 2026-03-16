package com.zengyanyu.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author zengyanyu
 * @since 2025-07-18
 */
@Getter
@Setter
@MappedSuperclass
@ApiModel("通用公共实体")
public class BaseEntity implements Serializable {

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty("创建人")
    private String createBy;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.UPDATE)
    @ApiModelProperty("更改人")
    private String updateBy;

    @TableField(fill = FieldFill.UPDATE)
    @ApiModelProperty("更改时间")
    private LocalDateTime updateTime;
}
