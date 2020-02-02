package com.company.project.dao;

import com.company.project.compose.TableList;
import com.company.project.core.Mapper;
import com.company.project.model.VisitRecord;
import org.apache.ibatis.annotations.Select;

public interface VisitRecordMapper extends Mapper<VisitRecord> {

     // update by cwf  2019/10/10 17:08 Reason: 根据userId visitorId cstatus查看是否有访问
     @Select(" select * from " + TableList.VISITOR_RECORD + " where userId = #{userId} and visitorId =#{visitorId}" +
             "  and cstatus =#{cstatus} order by id desc limit 1  ")
     VisitRecord findByRepeat(long userId, long visitorId, String cstatus);
}