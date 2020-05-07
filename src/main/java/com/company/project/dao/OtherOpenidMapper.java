package com.company.project.dao;

import com.company.project.compose.TableList;
import com.company.project.core.Mapper;
import com.company.project.model.OtherOpenid;
import org.apache.ibatis.annotations.Select;

public interface OtherOpenidMapper extends Mapper<OtherOpenid> {
    @Select("select o.* from "+ TableList.OTHER_WX+" wx "+
            " left join "+TableList.OTHER_OPENID+" o on o.wx_id=wx.id " +
            "where wx_value=#{wxId} and open_id=#{otherOpenId} and user_id=#{userId} ")
    OtherOpenid findbyRlat(String wxId, Long userId, String otherOpenId);
}