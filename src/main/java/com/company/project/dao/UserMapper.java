package com.company.project.dao;

import com.company.project.compose.TableList;
import com.company.project.core.Mapper;
import com.company.project.model.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserMapper extends Mapper<User> {

    @Select(  "select  * from (SELECT DISTINCT * from (select  id,realName,idHandleImgUrl,phone,authDate startDate from tbl_user where id=-1 union\n" +
            "            select u.id,realName,idHandleImgUrl,phone,startDate from `tbl_visitor_record`  vr left join tbl_user u on u.id=vr.visitorId where userId=#{userId} and visitorId<>#{userId} and u.id is not null and u.realName is not null \n" +
            "            union select  u.id,realName,idHandleImgUrl,phone,startDate from `tbl_visitor_record` vr left join tbl_user u on u.id=vr.userId where visitorId=#{userId}  and userId<>#{userId} and u.id is not null and u.realName is not null)x ORDER BY  startDate desc  )y\n" +
            "GROUP BY id, realName,idHandleImgUrl,phone order by startDate desc")
    List<User> frequentContacts(Object userId);
    @Select("select id,realName,phone,isAuth from "+TableList.USER+" where wx_open_id=#{openId} limit 1")
    User getUserFromOpenId(Object openId);
    @Select("select id,isAuth from "+TableList.USER+" where phone=#{phone} limit 1")
    User findByPhone(String phone);
    @Select("select * from "+TableList.USER+" where realName=#{name} and idNO=#{idNO} limit 1")
    User findByNameIdNo(@Param("name") String name, @Param("idNO") String idNO);
    @Select("select u.id,u.realName from tbl_user u left join tbl_company_user cu on u.id =cu.userId\n" +
            "where cu.companyId=#{companyId} and u.realName = #{name}  and (u.isAuth='T' or u.isAuth='H') and cu.currentStatus='normal' and status='applySuc' limit 1")
    User nameCompany(@Param("companyId") Long companyId, @Param("name") String name);
    @Select(" select id,realName,idHandleImgUrl from tbl_user where id=#{userId}")
    User findByUserId(Long userId);
}