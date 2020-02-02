package com.company.project.service.impl;

import com.company.project.core.AbstractService;
import com.company.project.dao.CompanyMapper;
import com.company.project.model.Company;
import com.company.project.service.CompanyService;
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
}
