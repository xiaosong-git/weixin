package com.company.project.service.impl;

import com.company.project.dao.UserAccountMapper;
import com.company.project.model.UserAccount;
import com.company.project.service.UserAccountService;
import com.company.project.core.AbstractService;
import com.company.project.util.MD5Util;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by CodeGenerator on 2019/10/14.
 */
@Service
@Transactional
public class UserAccountServiceImpl extends AbstractService<UserAccount> implements UserAccountService {
    @Resource
    private UserAccountMapper tblUserAccountMapper;

    //自动生成账号
    @Override
    public void preCreateAcount(Long userid) throws Exception {
        UserAccount userAccount=new UserAccount();
        userAccount.setUserid(userid);
        String sysPwd = MD5Util.MD5("000000");
        userAccount.setSyspwd(sysPwd);
        userAccount.setCstatus("normal");
        int save = save(userAccount);
    }

    @Override
    public UserAccount findByUserId(long userId) {
        return tblUserAccountMapper.findByUserId(userId);
    }

}
