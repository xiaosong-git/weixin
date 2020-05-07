package com.company.project.dao;

import com.company.project.compose.TableList;
import com.company.project.core.Mapper;
import com.company.project.model.otherWx;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface OtherWxMapper extends Mapper<otherWx> {

    @Select("select * from "+ TableList.OTHER_WX+" where wx_value=#{wxId} ")
    otherWx findByWx(String wxId);
    @Select("select wx.id,wx_value,wx_name,template,appid,secret,o.open_id ext1 from "+ TableList.OTHER_WX+" wx left join "+TableList.OTHER_OPENID+" o on o.wx_id=wx.id " +
            " where o.user_id=#{userId} ")
    List<otherWx> findWxByUser(Long userId);

}