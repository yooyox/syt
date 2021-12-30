package com.lyx.yygh.hosp.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lyx.yygh.common.result.Result;
import com.lyx.yygh.common.utils.MD5;
import com.lyx.yygh.model.hosp.HospitalSet;
import com.lyx.yygh.hosp.service.HospitalSetService;
import com.lyx.yygh.vo.hosp.HospitalSetQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;

@Api(tags = "医院设置管理")
@RestController
@RequestMapping("/admin/hosp/hospitalSet")
@CrossOrigin
public class HospitalSetController {

    @Autowired
    private HospitalSetService hospitalSetService;

    //1 查询医院设置表中的所有信息
    @ApiOperation(value = "医院设置-查询所有信息")
    @GetMapping("findAll")
    public Result findAllHospitalSet() {
        //调用service的方法
        List<HospitalSet> list = hospitalSetService.list();
        return Result.ok(list);
    }

    //2 根据id逻辑删除医院设置表中的信息
    @ApiOperation(value = "医院设置-根据id逻辑删除")
    @DeleteMapping("{id}")
    public Result removeHospSet(@PathVariable Long id) {
        boolean flag = hospitalSetService.removeById(id);
        return flag ? Result.ok() : Result.fail();
    }

    //3.分页条件查询医院设置
    @ApiOperation(value = "医院设置-分页条件查询")
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

    //4.添加医院设置
    @ApiOperation(value = "医院设置-添加")
    @PostMapping("addHospitalSet")
    public Result addHospitalSet(@RequestBody HospitalSet hospitalSet) {
        //4.1 设置添加的值
        //状态 1 能使用 0 不能使用
        hospitalSet.setStatus(1);
        //签名秘钥
        Random random = new Random();
        hospitalSet.setSignKey(MD5.encrypt(System.currentTimeMillis()+""+random.nextInt(1000)));
        //4.2 调用添加方法
        boolean isAdd = hospitalSetService.save(hospitalSet);
        return isAdd ? Result.ok() : Result.fail();
    }

    //5.根据id查询医院设置
    @ApiOperation(value = "医院设置-根据id查询")
    @GetMapping("getHospSetById/{id}")
    public Result getHospSetById(@PathVariable Long id) {
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        return Result.ok(hospitalSet);
    }

    //6.修改医院设置
    @ApiOperation(value = "医院设置-修改")
    @PostMapping("updateHospitalSet")
    public Result updateHospitalSet(@RequestBody HospitalSet hospitalSet) {
        boolean isUpdate = hospitalSetService.updateById(hospitalSet);
        return isUpdate ? Result.ok() : Result.fail();
    }

    //7.批量删除医院设置
    @ApiOperation(value = "医院设置-批量删除")
    @DeleteMapping("batchRemoveHospitalSet")
    public Result batchRemoveHospitalSet(@RequestBody List<Long> idList) {
        hospitalSetService.removeByIds(idList);
        return Result.ok();
    }

    //8.医院设置锁定和解锁
    @ApiOperation(value = "医院设置-锁定和解锁")
    @PutMapping("lockHospitalSet/{id}/{status}")
    public Result lockHospitalSet(@PathVariable Long id,
                                  @PathVariable Integer status) {
        //8.1根据id查询医院设置信息
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        //8.2设置status  1是能使用。0是不能使用
        hospitalSet.setStatus(status);
        //8.3调用修改方法
        hospitalSetService.updateById(hospitalSet);
        return Result.ok();
    }

    //9.TODO 发送签名密钥（未完成）
    @ApiOperation(value = "医院设置-发送签名密钥")
    @PutMapping("sendKey/{id}")
    public Result lockHospitalSet(@PathVariable Long id) {
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        String signKey = hospitalSet.getSignKey();
        String hoscode = hospitalSet.getHoscode();
        //TODO 发送短信
        return Result.ok();
    }




}
