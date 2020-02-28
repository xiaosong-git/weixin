package com.company.project.web;

import com.company.project.core.Result;
import com.company.project.core.ResultGenerator;
import com.company.project.dao.CompanyMapper;
import com.company.project.model.Company;
import com.company.project.service.CompanyService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
* Created by CodeGenerator on 2019/10/09.
*/
@RestController
@RequestMapping("/company")
public class CompanyController {
    @Resource
    private CompanyService companyService;

    @Resource
    private CompanyMapper companyMapper;
    @PostMapping("/findByPhone")
    public Result findByPhone(@RequestParam String phone) {
        PageHelper.startPage(1,2);
        List<Company> companyList = companyMapper.findByPhone(phone);
        PageInfo<Company> page=new PageInfo<>(companyList);
        return ResultGenerator.genSuccessResult(page);
    }
    @PostMapping("/findByUserId")
    public Result findByUser(@RequestParam Long userId) {
        List<Company> companyList = companyMapper.findByUserId(userId);
        return ResultGenerator.genSuccessResult(companyList);
    }
//    @PostMapping("/delete")
//    public Result delete(@RequestParam Integer id) {
//        companyService.deleteById(id);
//        return ResultGenerator.genSuccessResult();
//    }
//
//    @PostMapping("/update")
//    public Result update(Company company) {
//        companyService.update(company);
//        return ResultGenerator.genSuccessResult();
//    }
//
//    @PostMapping("/detail")
//    public Result detail(@RequestParam Integer id) {
//        Company company = companyService.findById(id);
//        return ResultGenerator.genSuccessResult(company);
//    }
//
//    @PostMapping("/list")
//    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
//        PageHelper.startPage(page, size);
//        List<Company> list = companyService.findAll();
//        PageInfo pageInfo = new PageInfo(list);
//        return ResultGenerator.genSuccessResult(pageInfo);
//    }
}
