package com.company.project.service;
import com.company.project.core.Result;
import com.company.project.model.VisitRecord;
import com.company.project.core.Service;
import com.soecode.wxtools.exception.WxErrorException;

import java.util.List;
import java.util.Map;


/**
 * Created by CodeGenerator on 2019/09/22.
 */
public interface visitRecordService extends Service<VisitRecord> {



    Result visitRequest(VisitRecord visitRecord, String hour) throws Exception;

    Result inviteRequest(VisitRecord visitRecord, String hour);

    Result record(Long userId, int pages, int sizes);


    Result recordDetail( Long visitorId, Long loginId, int pages, int sizes);

    Result recordReply(VisitRecord visitRecord, Long loginId);

    Result recordComfire(Long recordId,String cstatus,String companyId);

    void sendTemplate(String wxOpenId, String templateId, String accessType, String visitResult,
                      String visitorBy, String startDate, String endDate, String qrcodeUrl, String companyFloor,
                      String orgName, String companyName) throws WxErrorException;
    Map<String,Object> findRecordCompany(Long recordId);
}
