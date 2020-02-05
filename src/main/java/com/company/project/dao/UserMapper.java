package com.company.project.dao;

import com.company.project.compose.TableList;
import com.company.project.core.Mapper;
import com.company.project.model.User;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserMapper extends Mapper<User> {

    @Select(  "select id,realName,idHandleImgUrl from tbl_user where id in (\n" +
            "select visitorId from "+TableList.VISITOR_RECORD+" where userId=#{userId}\n" +
            "union select userId from "+TableList.VISITOR_RECORD+" where visitorId=#{userId})")
    List<User> frequentContacts(Object userId);

}