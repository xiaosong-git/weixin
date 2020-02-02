package com.company.project.service;
import com.company.project.core.Result;
import com.company.project.model.VisitRecord;
import com.company.project.core.Service;


/**
 * Created by CodeGenerator on 2019/09/22.
 */
public interface visitRecordService extends Service<VisitRecord> {


    Result visitRequest(long userId, long visitorId, String reason, String startDate, String endDate) throws Exception;

}
