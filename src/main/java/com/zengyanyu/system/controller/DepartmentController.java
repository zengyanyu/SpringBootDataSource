package com.zengyanyu.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zengyanyu.system.commons.ResponseData;
import com.zengyanyu.system.config.LogRecord;
import com.zengyanyu.system.entity.Department;
import com.zengyanyu.system.query.QueryObject;
import com.zengyanyu.system.service.IDepartmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author zengyanyu
 * @since 2026-03-16
 */
@Slf4j
@RestController
@Api(tags = "部门控制器")
@RequestMapping("/department")
public class DepartmentController extends BaseController {

    @Resource
    private IDepartmentService departmentService;

    @LogRecord("保存或更新部门")
    @ApiOperation("保存或更新部门")
    @PostMapping("/save")
    public ResponseData save(@RequestBody Department department) {
        return departmentService.saveOrUpdateDepartment(department);
    }

    @LogRecord("删除部门")
    @ApiOperation("删除部门")
    @GetMapping("/{id}")
    public ResponseData delete(@PathVariable String id) {
        departmentService.removeById(id);
        return new ResponseData("删除成功");
    }

    @LogRecord("批量删除部门")
    @ApiOperation("批量删除部门")
    @PostMapping("/del/batch")
    public ResponseData deleteBatch(@RequestBody List<String> ids) {
        departmentService.removeByIds(ids);
        return new ResponseData("批量删除成功");
    }

    @LogRecord("查询所有数据")
    @ApiOperation("查询所有数据")
    @GetMapping("/findAll")
    public ResponseData<List<Department>> findAll() {
        return new ResponseData("查询所有数据", departmentService.list());
    }

    @LogRecord("根据ID查询指定数据")
    @ApiOperation("根据ID查询指定数据")
    @GetMapping("/get/{id}")
    public ResponseData<Department> get(@PathVariable String id) {
        return new ResponseData("根据ID查询指定数据", departmentService.getById(id));
    }

    @LogRecord("部门分页查询数据")
    @ApiOperation("部门分页查询数据")
    @GetMapping("/page")
    public Page<Department> page(QueryObject queryObject) {
        QueryWrapper<Department> wrapper = new QueryWrapper<>();
        return departmentService.page(new Page<>(queryObject.getPageNum(), queryObject.getPageSize()), wrapper);
    }
}
