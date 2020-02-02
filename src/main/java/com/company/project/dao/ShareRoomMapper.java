package com.company.project.dao;

import com.company.project.compose.TableList;
import com.company.project.core.Mapper;
import com.company.project.model.ShareRoom;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ShareRoomMapper extends Mapper<ShareRoom> {

    @Select("select * from "+ TableList.SHARE_ROOM+" sr " +
            "left join "+TableList.ORG+" o on sr.room_orgcode=o.org_code where room_orgcode=#{orgCode}")
    List<ShareRoom> findByOrg (String orgCode);
}