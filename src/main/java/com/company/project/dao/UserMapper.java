package com.company.project.dao;

import com.company.project.compose.TableList;
import com.company.project.core.Mapper;
import com.company.project.model.User;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserMapper extends Mapper<User> {

    @Select(  "select DISTINCT id, realName,idHandleImgUrl,phone from (select  * from (\n" +
            "select u.id,realName,idHandleImgUrl,phone,startDate from "+TableList.VISITOR_RECORD+" vr left join tbl_user u on u.id=vr.visitorId where userId=#{userId}\n" +
            "union select  u.id,realName,idHandleImgUrl,phone,startDate from "+TableList.VISITOR_RECORD+" vr left join tbl_user u on u.id=vr.userId where visitorId=#{userId})x ORDER BY startDate desc)y")
    List<User> frequentContacts(Object userId);
    @Select("select id,realName,phone,isAuth from "+TableList.USER+" where wx_open_id=#{openId} limit 1")
    User getUserFromOpenId(Object openId);
}