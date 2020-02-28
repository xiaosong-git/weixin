package com.company.project.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.company.project.compose.TableList;
import com.company.project.core.AbstractService;
import com.company.project.core.Result;
import com.company.project.core.ResultGenerator;
import com.company.project.dao.VisitRecordMapper;
import com.company.project.model.User;
import com.company.project.model.VisitRecord;
import com.company.project.service.CodeService;
import com.company.project.service.UserService;
import com.company.project.service.visitRecordService;
import com.company.project.util.DateUtil;
import com.company.project.util.GTNotification;
import com.company.project.weixin.WxController;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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

    Logger logger = LoggerFactory.getLogger(visitRecordServiceImpl.class);
    /**

     * @param hour 结束时间
     * @return result
     * @throws Exception
     * @author cwf
     * @date 2019/10/8 16:11
     */
    @Override
    public Result visitRequest(VisitRecord visitRecord, String hour) throws Exception {
        visitRecord.setRecordType(1);
        return  visitCommon( visitRecord,  hour);
    }

    @Override
    public Result inviteRequest(VisitRecord visitRecord, String hour) {
        if (visitRecord.getCompanyId()==null){
            return ResultGenerator.genFailResult("邀约公司地址未上传成功！");
        }
        visitRecord.setRecordType(2);
        Long visitorId = visitRecord.getUserId();
        Long userId = visitRecord.getVisitorId();
        visitRecord.setUserId(userId);
        visitRecord.setUserId(visitorId);
        return visitCommon(visitRecord,hour);
    }

    @Override
    public Result record(Long userId, int pages, int sizes) {
        PageHelper.startPage(pages,sizes);
        List<VisitRecord> record = visitorRecordMapper.record(userId);
        PageInfo<VisitRecord> page=new PageInfo<>(record);
        return ResultGenerator.genSuccessResult(page);
    }

    @Override
    public Result recordDetail(Long visitorId, Long loginId, int pages, int sizes) {
        PageHelper.startPage(pages,sizes);
        List<Map<String, Object>> list = visitorRecordMapper.recordDetail( visitorId,loginId);
        PageInfo<Map<String, Object>> page=new PageInfo<>(list);
        return ResultGenerator.genSuccessResult(page);
    }
    @Override
    public Result recordReply(VisitRecord visitRecord,Long loginId) {
        String replyDate = DateUtil.getCurDate();
        String replyTime = DateUtil.getCurTime();
        visitRecord.setReplyDate(replyDate);
        visitRecord.setReplyTime(replyTime);
        visitRecord.setIsReceive("F");
        int update = this.update(visitRecord);
        String apply = "同意";
        if ("applyFail".equals(visitRecord.getCstatus())) {
            apply = "拒绝";
        }
        if (update > 0) {
            Long other = visitRecord.getUserId().equals(loginId) ? visitRecord.getUserId() : visitRecord.getVisitorId();
            //发送推送
            User otherUser = userService.findById(other);
                String notification_title = "回应信息提醒";
                String msg_content = "您好，您有一条回应信息，请登入app查收!";
                boolean single = false;
            String devicetoken = otherUser.getDevicetoken();
            String phone = otherUser.getPhone();
            if (devicetoken !=null&&!"".equals(devicetoken)&& phone !=null&&!"".equals(phone)){
                single = GTNotification.Single(devicetoken, phone, notification_title, msg_content, msg_content);
            }
                logger.info("发送个推 推送成功? 设备号{}", single);
//                        if (!isYmSuc) {
//				          codeService.sendMsg(phone, 3, visitorResult, visitorBy, visitorDateTime, null);
//			                }
            // todo 推送微信
            return ResultGenerator.genSuccessResult( apply + "成功！");
        } else {
            return ResultGenerator.genSuccessResult( apply + "失败！");
        }
    }

    @Override
    public Result recordComfire(Long recordId, String cstatus,String companyId) {

        String replyDate =DateUtil.getCurDate();
        String replyTime =DateUtil.getCurTime();

        int resultCode = visitorRecordMapper.updateCstatus(recordId,cstatus,replyDate,replyTime,companyId);
        if(resultCode!=1){
            return ResultGenerator.genFailResult("审核失败");
        }
        return ResultGenerator.genSuccessResult("成功");
    }

    public Result visitCommon(VisitRecord visitRecord, String hour){
        String cstatus = "applyConfirm";
        Long userId = visitRecord.getUserId();
        Long visitorId = visitRecord.getVisitorId();
        if (userId.equals(visitorId)){
            return ResultGenerator.genFailResult("请不要对自己发起访问！");
        }
        long v = (long)(Float.parseFloat(hour)*60);
        String startDate = visitRecord.getStartDate();
        String endDate= DateUtil.addMinute(startDate,v);
        Integer recordType = visitRecord.getRecordType();
        VisitRecord check = visitorRecordMapper.check(userId, visitorId, recordType, startDate,endDate);
        if (check!=null){
            return ResultGenerator.genFailResult("在" + startDate + "——" + endDate + "内已经有邀约信息存在");
        }
        //储存新的来访记录
        Date date = new Date();
        visitRecord.setCstatus(cstatus);
        visitRecord.setVisitDate(new SimpleDateFormat("yyyy-MM-dd").format(date));
        visitRecord.setVisitTime(new SimpleDateFormat("HH:mm:ss").format(date));
        visitRecord.setEndDate(endDate);
        visitRecord.setVitype("C");
        int save = save(visitRecord);
        if (save > 0) {
            //消息推送
            User user = userService.findById(userId);
            User visitor = userService.findById(visitorId);
            String notification_title = recordType==1?"访客-审核通知":"邀约-审核通知";
            String realName = user.getRealname();
            String msg_content = "【朋悦比邻】您好，您有一条预约访客需审核，访问者:" + realName + "，被访者:" + visitor.getRealname() + ",访问时间:"
                    + startDate;
            if ("T".equals(visitor.getIsonlineapp())) {
                //发送个推
                boolean single = GTNotification.Single(visitor.getDevicetoken(), visitor.getPhone(), notification_title, msg_content, msg_content);
                if (!single){
                    codeService.sendMsg(visitor.getPhone(), 5, null, null, startDate, realName);
                }
            }else {
                //发送短信
                codeService.sendMsg(visitor.getPhone(), 5, null, null, startDate, realName);
            }
            return ResultGenerator.genSuccessResult(visitRecord);
        }else {
            return ResultGenerator.genFailResult("发送访问失败");
        }
    }
}
