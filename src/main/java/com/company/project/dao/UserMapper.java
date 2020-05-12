package com.company.project.dao;

import com.company.project.compose.TableList;
import com.company.project.core.Mapper;
import com.company.project.model.User;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserMapper extends Mapper<User> {

    @Select(  "select DISTINCT id, realName,idHandleImgUrl,phone from (select  * from (\n" +
            "select u.id,realName,idHandleImgUrl,phone,startDate from "+TableList.VISITOR_RECORD+" vr left join tbl_user u on u.id=vr.visitorId where userId=#{userId} and visitorId<>#{userId} and u.id is not null and u.realName is not null\n" +
            "union select  u.id,realName,idHandleImgUrl,phone,startDate from "+TableList.VISITOR_RECORD+" vr left join tbl_user u on u.id=vr.userId where visitorId=#{userId}  and userId<>#{userId} and u.id is not null and u.realName is not null)x ORDER BY startDate desc)y")
    List<User> frequentContacts(Object userId);
    @Select("select id,realName,phone,isAuth from "+TableList.USER+" where wx_open_id=#{openId} limit 1")
    User getUserFromOpenId(Object openId);
    @Select("select id from "+TableList.USER+" where phone=#{phone} limit 1")
    User findByPhone(String phone);
    @Select("select * from "+TableList.USER+" where realName=#{name} and idNO=#{idNO} limit 1")
    User findByNameIdNo(String name, String idNO);
    @Select("select u.id,u.realName from tbl_user u left join tbl_company_user cu on u.id =cu.userId\n" +
            "where cu.companyId=#{companyId} and u.realName = #{name}  where u.isAuth='T' and cu.currentStatus='normal' status='applySuc' limit 1")
    User nameCompany(Long companyId, String name);
}