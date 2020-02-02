package com.company.project.service.impl;

import com.company.project.core.Result;
import com.company.project.core.ResultGenerator;
import com.company.project.dao.VisitRecordMapper;
import com.company.project.model.User;
import com.company.project.model.VisitRecord;
import com.company.project.service.CodeService;
import com.company.project.service.UserService;
import com.company.project.service.visitRecordService;
import com.company.project.core.AbstractService;
import com.company.project.util.GTNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * Created by CodeGenerator on 2019/09/22.
 */
@Service
@Transactional
public class visitRecordServiceImpl extends AbstractService<VisitRecord> implements visitRecordService {
    @Resource
    private VisitRecordMapper visitorRecordMapper;
    @Autowired
    private CodeService codeService;
    @Autowired
    private UserService userService;
    /**
     * @param userId 用户id
     * @param visitorId 访问id
     * @param reason 访问原因
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return result
     * @throws Exception
     * @author cwf
     * @date 2019/10/8 16:11
     */
    @Override
    public Result visitRequest(long userId, long visitorId, String reason, String startDate, String endDate) throws Exception {

        String cstatus = "applyConfirm";

//        Map<String, Object> save = new HashMap<String, Object>();
        VisitRecord repeat = visitorRecordMapper.findByRepeat(userId, visitorId, cstatus);
        if (userId==visitorId){
            return ResultGenerator.genFailResult("请不要对自己发起访问！");
        }
        System.out.println(repeat);
        if (repeat != null) {
            if (repeat.getEnddate() != null || repeat.getEnddate() != "") {
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                Date de = new Date();
                Date d1 = df.parse(df.format(de));
                Date d2 = df.parse(repeat.getEnddate());
                if (d1.getTime() <= d2.getTime()) {
                    return ResultGenerator.genFailResult("已经向此人提交过申请，请勿重复提交!");
                } else {
                    //如果时间过期，则状态改为取消
                    VisitRecord visitRecord = new VisitRecord();
                    visitRecord.setId(repeat.getId());
                    visitRecord.setCstatus("Cancle");
                    Integer num = update(visitRecord);
                    if (num <= 0) {
                        return ResultGenerator.genFailResult("上条访问申请未过期，请联系客服!");
                    }
                }
            }
        }
        //储存新的来访记录
        Date date = new Date();
        VisitRecord visitRecord = new VisitRecord();
        visitRecord.setUserid(userId);
        visitRecord.setVisitorid(visitorId);
        visitRecord.setCstatus(cstatus);
        visitRecord.setVisitdate(new SimpleDateFormat("yyyy-MM-dd").format(date));
        visitRecord.setVisittime(new SimpleDateFormat("HH:mm:ss").format(date));
        visitRecord.setReason(reason);
        visitRecord.setStartdate(startDate);
        visitRecord.setEnddate(endDate);
        visitRecord.setVitype("C");
        visitRecord.setRecordtype(1);

        int save = save(visitRecord);

        if (save > 0) {
                    //消息推送
            User user = userService.findById(userId);
            User visitor = userService.findById(visitorId);
            String notification_title = "访客-审核通知";
                String msg_content = "【朋悦比邻】您好，您有一条预约访客需审核，访问者:" + user.getRealname() + "，被访者:" + visitor.getRealname() + ",访问时间:"
                        + visitRecord.getStartdate();
                if ("T".equals(visitor.getIsonlineapp())) {
                    //发送个推
                   boolean single = GTNotification.Single(visitor.getDevicetoken(), visitor.getPhone(), notification_title, msg_content, msg_content);
                    if (!single){
                        codeService.sendMsg(visitor.getPhone(), 5, null, null, visitRecord.getStartdate(), user.getRealname());
                    }
                }else {
                    //发送短信
                    codeService.sendMsg(visitor.getPhone(), 5, null, null, visitRecord.getStartdate(), user.getRealname());
                }

            return ResultGenerator.genSuccessResult(visitRecord);
        }else {
            return ResultGenerator.genFailResult("发送访问失败");
        }
    }

}
