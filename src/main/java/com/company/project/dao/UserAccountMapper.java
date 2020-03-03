package com.company.project.dao;

import com.company.project.compose.TableList;
import com.company.project.core.Mapper;
import com.company.project.model.UserAccount;
import org.apache.ibatis.annotations.Select;

public interface UserAccountMapper extends Mapper<UserAccount> {

    @Select("select * from "+ TableList.USER_ACCOUNT+" where  userId=#{userId}")
    UserAccount findByUserId(long userId);
}