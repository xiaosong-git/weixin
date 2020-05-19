package com.company.project.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.company.project.core.AbstractService;
import com.company.project.dao.CompanyMapper;
import com.company.project.model.Company;
import com.company.project.service.CompanyService;
import com.company.project.util.RedisUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


/**
 * Created by CodeGenerator on 2019/10/09.
 */
@Service
@Transactional
public class CompanyServiceImpl extends AbstractService<Company> implements CompanyService {
    @Resource
    private CompanyMapper tblCompanyMapper;
    @Override
    public List<Company> findByUserId(long userId) {
//        Condition condition=new Condition(User.class);
//        condition.createCriteria().andEqualTo("userid",userId);
//        condition.selectProperties("id");
//        List<CompanyUser> list = super.findByCondition(condition);
        List<Company> list = tblCompanyMapper.findByUserId(userId);
        return list;
    }
    @Override
    public List<Company> findNotCompanyUser(long userId){
        List<Company> list = tblCompanyMapper.findNotCompanyUser(userId);
        return list;
    }

    @Override
    public List<Company> findCompany(String companyName) {
        List<Company> companyList = null;
        //redis修改
        String json = RedisUtil.getStrVal(companyName, 4);
        if(StrUtil.isNotBlank(json)&&!"null".equals(json)){
            System.out.println("null".equals(json));
            //先从缓存中获取
            companyList = JSON.parseObject(json, List.class);
        }else {
            companyList = tblCompanyMapper.findCompany(companyName);
            json = JSON.toJSONString(companyList);
            System.out.println(json);
            RedisUtil.setStr(companyName, json, 4, 24*3600);
        }
        return companyList;
    }

    @Override
    public List<Company> findCompanyAddr(String addr) {
        return tblCompanyMapper.findCompanyAddr(addr);
    }
}
