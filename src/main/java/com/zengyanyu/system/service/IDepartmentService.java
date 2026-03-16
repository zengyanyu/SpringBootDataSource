package com.zengyanyu.system.service;

import com.zengyanyu.system.entity.Department;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zengyanyu.system.commons.ResponseData;

/**
 * 部门 服务类
 *
 * @author zengyanyu
 * @since 2026-03-16
 */
public interface IDepartmentService extends IService<Department> {

    /**
     * 保存或更新部门
     *
     * @param department 部门
     * @return
     */
    ResponseData saveOrUpdateDepartment(Department department);

}
