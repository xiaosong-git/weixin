package com.company.project.dao;

import com.company.project.compose.TableList;
import com.company.project.core.Mapper;
import com.company.project.model.VisitRecord;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

public interface VisitRecordMapper extends Mapper<VisitRecord> {

     // update by cwf  2019/10/10 17:08 Reason: 根据userId visitorId cstatus查看是否有访问
     @Select(" select * from " + TableList.VISITOR_RECORD + " where userId = #{userId} and visitorId =#{visitorId} and " +
             "recordType =#{recordType} and cstatus<>'applyFail' and STR_TO_DATE(startDate,'%Y-%m-%d %H:%i')<STR_TO_DATE(#{endDate},'%Y-%m-%d %H:%i')" +
             " and   STR_TO_DATE(endDate,'%Y-%m-%d %H:%i')>STR_TO_DATE(#{startDate},'%Y-%m-%d %H:%i')")
     VisitRecord check(@Param("userId") Object userId, @Param("visitorId") Object visitorId, @Param("recordType") Object recordType, @Param("startDate") String startDate, @Param("endDate") String endDate);
    @Update("update " + TableList.VISITOR_RECORD + " set dateType=concat('U',userId),userId=#{id}   where userId=#{userId}  ")
    int updateUserId(@Param("userId") Long userId, @Param("id") Long id);
    @Update("update " + TableList.VISITOR_RECORD + " set dateType=concat('V',visitorId),visitorId=#{id}  where visitorId=#{userId}  ")
    int updateVisitorId(@Param("userId") Long userId, @Param("id") Long id);
    //sql放xml中
    List<VisitRecord> record(Long userId);
    //sql放xml中
    List<Map<String,Object>> recordDetail(@Param("visitorId") Long visitorId, @Param("loginId") Long loginId);

    @Select("select * from "+ TableList.VISITOR_RECORD + " where id = #{recordId}")
    VisitRecord findByRecordId(Object recordId);


    @Update("update " + TableList.VISITOR_RECORD + " set cstatus = #{cstatus} , replyDate = #{replyDate} , replyTime = #{replyTime},companyId =#{companyId},orgCode=#{orgCode} where id = #{recordId}")
    int updateCstatus(@Param("recordId") Long recordId, @Param("cstatus") String cstatus, @Param("replyDate") String replyDate, @Param("replyTime") String replyTime, @Param("companyId") String companyId, @Param("orgCode") String orgCode);

    @Select("select * from "+ TableList.VISITOR_RECORD + " where userId = #{userId} and visitorId =#{visitorId} and visitDate =#{visitDate} and visitTime =#{visitTime}")
    VisitRecord findRecord(@Param("userId") Long userId, @Param("visitorId") Long visitorId, @Param("visitDate") String visitDate, @Param("visitTime") String visitTime);

    Map<String,Object> findRecordCompany(Long recordId);
}