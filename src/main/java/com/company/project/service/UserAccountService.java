package com.company.project.service;
import com.company.project.model.UserAccount;
import com.company.project.core.Service;


/**
 * Created by CodeGenerator on 2019/10/14.
 */
public interface UserAccountService extends Service<UserAccount> {
    //自动生成账号
    void preCreateAcount(Long userid) throws Exception;

    UserAccount findByUserId(long userId);


    //自动生成账号

}
