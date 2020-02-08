package com.company.project.dao;

import com.company.project.compose.TableList;
import com.company.project.core.Mapper;
import com.company.project.model.VisitRecord;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface VisitRecordMapper extends Mapper<VisitRecord> {

     // update by cwf  2019/10/10 17:08 Reason: 根据userId visitorId cstatus查看是否有访问
     @Select(" select * from " + TableList.VISITOR_RECORD + " where userId = #{userId} and visitorId =#{visitorId} and " +
             "recordType =#{recordType} and cstatus<>'applyFail' and STR_TO_DATE(startDate,'%Y-%m-%d %H:%i')<STR_TO_DATE(#{endDate},'%Y-%m-%d %H:%i')" +
             " and   STR_TO_DATE(endDate,'%Y-%m-%d %H:%i')>STR_TO_DATE(#{startDate},'%Y-%m-%d %H:%i')")
     VisitRecord check(Object userId, Object visitorId,Object recordType, String startDate,String endDate);
    @Update("update " + TableList.VISITOR_RECORD + " set dateType=concat('U',userId),userId=#{id}   where userId=#{userId}  ")
    int updateUserId(Long userId, Long id);
    @Update("update " + TableList.VISITOR_RECORD + " set dateType=concat('V',visitorId),visitorId=#{id}  where visitorId=#{userId}  ")
    int updateVisitorId(Long userId, Long id);
    //sql放xml中
    List<VisitRecord> record(Long userId);
    //sql放xml中
    List<VisitRecord> recordDetail(Long userId,Long visitorId);
}