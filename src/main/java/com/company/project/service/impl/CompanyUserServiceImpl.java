package com.company.project.service.impl;

import com.company.project.core.AbstractService;
import com.company.project.dao.CompanyUserMapper;
import com.company.project.model.CompanyUser;
import com.company.project.service.CompanyUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


/**
 * Created by CodeGenerator on 2019/10/09.
 */
@Service
@Transactional
public class CompanyUserServiceImpl extends AbstractService<CompanyUser> implements CompanyUserService {
    @Resource
    private CompanyUserMapper tblCompanyUserMapper;

    @Override
    public List<CompanyUser> findByUserId(long userId) {
//        Condition condition=new Condition(User.class);
//        condition.createCriteria().andEqualTo("userid",userId);
//        condition.selectProperties("id");
//        List<CompanyUser> list = super.findByCondition(condition);
        List<CompanyUser> list = tblCompanyUserMapper.findByUserId(userId);
        return list;
    }

}
