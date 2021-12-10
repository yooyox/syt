package com.lyx.yygh.hosp.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lyx.yygh.common.result.Result;
import com.lyx.yygh.model.hosp.HospitalSet;
import com.lyx.yygh.hosp.service.HospitalSetService;
import com.lyx.yygh.vo.hosp.HospitalSetQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
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
    public Result findAllHospitalSet() {
        //调用service的方法
        List<HospitalSet> list = hospitalSetService.list();
        return Result.ok(list);
    }

    //2 根据id逻辑删除医院设置表中的信息
    @ApiOperation(value = "根据id逻辑删除医院设置表中的信息")
    @DeleteMapping("{id}")
    public Result removeHospSet(@PathVariable Long id) {
        boolean flag = hospitalSetService.removeById(id);
        return flag ? Result.ok() : Result.fail();
    }

    //3.分页条件查询
    @PostMapping("findHospitalSetByPage/{current}/{limit}")
    public Result findHospitalSetByPage(@PathVariable long current, @PathVariable long limit,
                                        @RequestBody(required = false) HospitalSetQueryVo hospitalSetQueryVo) {
        //3.1 创建page对象，传递当前页，和每页记录数
        Page<HospitalSet> page = new Page<>(current,limit);
        //3.2 分页条件
        QueryWrapper<HospitalSet> wrapper = new QueryWrapper<>();
        String hosname = hospitalSetQueryVo.getHosname();//医院名称
        String hoscode = hospitalSetQueryVo.getHoscode();//医院编号
        if(!StringUtils.isEmpty(hosname)) {
            wrapper.like("hosname",hospitalSetQueryVo.getHosname());
        }
        if(!StringUtils.isEmpty(hoscode)) {
            wrapper.eq("hoscode",hospitalSetQueryVo.getHoscode());
        }

        //3.3 调用分页查询方法
        Page<HospitalSet> pageHospitalSet = hospitalSetService.page(page, wrapper);
        //3.4 得到返回结果
        return Result.ok(pageHospitalSet);


    }


}
