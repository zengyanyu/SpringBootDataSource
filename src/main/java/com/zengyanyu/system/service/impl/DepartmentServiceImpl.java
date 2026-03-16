package com.zengyanyu.system.service.impl;

import com.zengyanyu.system.entity.Department;
import com.zengyanyu.system.mapper.DepartmentMapper;
import com.zengyanyu.system.service.IDepartmentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.zengyanyu.system.commons.ResponseData;

/**
 * 部门 服务实现类
 *
 * @author zengyanyu
 * @since 2026-03-16
 */
@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements IDepartmentService {

    /**
     * 保存或更新部门
     *
     * @param department 部门
     * @return
     */
    @Override
    public ResponseData saveOrUpdateDepartment(Department department) {
        this.saveOrUpdate(department);
        return new ResponseData("保存或更新成功");
    }

}
