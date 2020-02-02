package com.company.project.web;
import com.company.project.annotation.AuthCheckAnnotation;
import com.company.project.core.Result;
import com.company.project.core.ResultGenerator;
import com.company.project.model.Param;
import com.company.project.service.ParamService;
import com.company.project.weixin.Menu;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
* Created by CodeGenerator on 2019/10/11.
*/
@RestController
@RequestMapping("/param")
public class ParamController {
    @Resource
    private ParamService paramService;

//    @PostMapping("/add")
//    public Result add(Param param) {
//        paramService.save(param);
//        return ResultGenerator.genSuccessResult();
//    }
//
//    @PostMapping("/delete")
//    public Result delete(@RequestParam Integer id) {
//        paramService.deleteById(id);
//        return ResultGenerator.genSuccessResult();
//    }
//
//    @PostMapping("/update")
//    public Result update(Param param) {
//        paramService.update(param);
//        return ResultGenerator.genSuccessResult();
//    }
//
//    @PostMapping("/detail")
//    public Result detail(@RequestParam Integer id) {
//        Param param = paramService.findById(id);
//        return ResultGenerator.genSuccessResult(param);
//    }
//
//    @PostMapping("/list")
//    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
//        PageHelper.startPage(page, size);
//        List<Param> list = paramService.findAll();
//        PageInfo pageInfo = new PageInfo(list);
//        return ResultGenerator.genSuccessResult(pageInfo);
//    }
//    @AuthCheckAnnotation
//    @PostMapping("/menu")
//    public Result menu() {
//        Menu.creatMenu();
//        return ResultGenerator.genSuccessResult("修改菜单");
//    }

}
