package com.lyx.yygh.hosp.controller;

import com.lyx.yygh.model.hosp.HospitalSet;
import com.lyx.yygh.hosp.service.HospitalSetService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "医院设置管理")
@RestController
@RequestMapping("/admin/hosp/hospitalSet")
public class HospitalSetController {

    @Autowired
    private HospitalSetService hospitalSetService;

    //1 查询医院设置表中的所有信息
    @ApiOperation(value = "查询医院设置表中的所有信息")
    @GetMapping("findAll")
    public List<HospitalSet> findAllHospitalSet() {
        //调用service的方法
        List<HospitalSet> list = hospitalSetService.list();
        return list;
    }

    //2 根据id逻辑删除医院设置表中的信息
    @ApiOperation(value = "根据id逻辑删除医院设置表中的信息")
    @DeleteMapping("{id}")
    public boolean removeHospSet(@PathVariable Long id) {
        boolean flag = hospitalSetService.removeById(id);
        return flag;
    }


}
