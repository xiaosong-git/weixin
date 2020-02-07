package com.company.project.web;
import com.company.project.core.Result;
import com.company.project.core.ResultGenerator;
import com.company.project.dao.CompanyUserMapper;
import com.company.project.model.Company;
import com.company.project.model.CompanyUser;
import com.company.project.service.CompanyUserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
* Created by CodeGenerator on 2019/10/09.
*/
@RestController
@RequestMapping("/company/user")
public class CompanyUserController {

    @Resource
    private CompanyUserMapper companyUserMapper;

//    @Resource
//    private CompanyUserService companyUserService;
//
    @PostMapping("/test")
    public Result add(CompanyUser companyUser) {
        List<CompanyUser> byUserId = companyUserMapper.findByUserId(125L);
        List<CompanyUser> byPhone = companyUserMapper.findByPhone("18150797748");
        List<Company> companies=new LinkedList<>();
         for (CompanyUser user : byPhone) {
            companies.add(user.getCompany());
        }

        return ResultGenerator.genSuccessResult(companies);
    }
//
//    @PostMapping("/delete")
//    public Result delete(@RequestParam Integer id) {
//        companyUserService.deleteById(id);
//        return ResultGenerator.genSuccessResult();
//    }
//
//    @PostMapping("/update")
//    public Result update(CompanyUser companyUser) {
//        companyUserService.update(companyUser);
//        return ResultGenerator.genSuccessResult();
//    }
//
//    @PostMapping("/detail")
//    public Result detail(@RequestParam Integer id) {
//        CompanyUser companyUser = companyUserService.findById(id);
//        return ResultGenerator.genSuccessResult(companyUser);
//    }
//
//    @PostMapping("/list")
//    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
//        PageHelper.startPage(page, size);
//        List<CompanyUser> list = companyUserService.findAll();
//        PageInfo pageInfo = new PageInfo(list);
//        return ResultGenerator.genSuccessResult(pageInfo);
//    }
}
