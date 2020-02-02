package com.company.project.dao;

import com.company.project.compose.TableList;
import com.company.project.core.Mapper;
import com.company.project.model.UserAuth;
import org.apache.ibatis.annotations.Select;

public interface UserAuthMapper extends Mapper<UserAuth> {
    @Select("select distinct * from "+ TableList.USER_AUTH +" where idNo=#{idNO} and realName=#{realName} order by id desc limit 1")
    UserAuth localPhoneResult(String idNO,String realName);
}