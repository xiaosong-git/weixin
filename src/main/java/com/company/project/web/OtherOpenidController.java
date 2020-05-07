package com.company.project.web;
import com.company.project.core.Result;
import com.company.project.core.ResultGenerator;
import com.company.project.model.OtherOpenid;
import com.company.project.service.OtherOpenidService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
* Created by CodeGenerator on 2020/04/28.
*/
@RestController
@RequestMapping("/other/openid")
public class OtherOpenidController {
    @Resource
    private OtherOpenidService otherOpenidService;

    @PostMapping("/add")
    public Result add(OtherOpenid otherOpenid) {
        otherOpenidService.save(otherOpenid);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        otherOpenidService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(OtherOpenid otherOpenid) {
        otherOpenidService.update(otherOpenid);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        OtherOpenid otherOpenid = otherOpenidService.findById(id);
        return ResultGenerator.genSuccessResult(otherOpenid);
    }

    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<OtherOpenid> list = otherOpenidService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
}
