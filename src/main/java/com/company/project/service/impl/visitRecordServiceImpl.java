package com.company.project.service.impl;

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
import com.company.project.weixin.model.WxTemplateData;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.soecode.wxtools.api.IService;
import com.soecode.wxtools.api.WxService;
import com.soecode.wxtools.bean.TemplateSender;
import com.soecode.wxtools.bean.result.TemplateSenderResult;
import com.soecode.wxtools.exception.WxErrorException;
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

import static com.company.project.weixin.MenuKey.REPLY;
import static com.company.project.weixin.MenuKey.URL;


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
    private IService iService = new WxService();
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
        return  visitCommon( visitRecord,  hour ,1);
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
        visitRecord.setVisitorId(visitorId);
        try {
            return visitCommon(visitRecord,hour,2);
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
        return ResultGenerator.genFailResult("系统错误！");
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
        //是回应访问还是回应邀约
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
    public Result recordComfire(Long recordId, String cstatus,String companyId) throws WxErrorException {

        String replyDate =DateUtil.getCurDate();
        String replyTime =DateUtil.getCurTime();

        int resultCode = visitorRecordMapper.updateCstatus(recordId,cstatus,replyDate,replyTime,companyId);
        if(resultCode!=1){
            return ResultGenerator.genFailResult("审核失败");
        }
        VisitRecord visitRecord = visitorRecordMapper.findByRecordId(recordId);
        TemplateSender sender = new TemplateSender();
        Map<String, WxTemplateData> dataMap = new HashMap<>();

        if(visitRecord.getRecordType() == 1){
            sender.setTemplate_id("2UBJNiTiPPQTlwu2PHxtbCKhqao3Ix1I8mjGPBIWnUU");
            User me = userService.findById(visitRecord.getUserId());
            User otherUser = userService.findById(visitRecord.getVisitorId());
            if("".equals(me.getWxOpenId()) || me.getWxOpenId().isEmpty()){
                return ResultGenerator.genSuccessResult("成功");
            }
            sender.setTouser(me.getWxOpenId());
            dataMap.put("first",new WxTemplateData("访问消息通知", "#173177"));
            dataMap.put("keyword1",new WxTemplateData(otherUser.getRealname(), "#173177"));
            dataMap.put("keyword2",new WxTemplateData(otherUser.getPhone(), "#173177"));
            dataMap.put("keyword3",new WxTemplateData(visitRecord.getStartDate(), "#173177"));
            dataMap.put("keyword4",new WxTemplateData(visitRecord.getReason(), "#173177"));
            dataMap.put("remark",new WxTemplateData("您的访问申请信息已被对方审核", "#173177"));
            sender.setData(dataMap);
            TemplateSenderResult result = iService.templateSend(sender);
            System.out.println(result);
        }else{
            sender.setTemplate_id("2UBJNiTiPPQTlwu2PHxtbCKhqao3Ix1I8mjGPBIWnUU");
            User me = userService.findById(visitRecord.getVisitorId());
            User otherUser = userService.findById(visitRecord.getUserId());
            if("".equals(me.getWxOpenId()) || me.getWxOpenId().isEmpty()){
                return ResultGenerator.genSuccessResult("成功");
            }
            sender.setTouser(me.getWxOpenId());
            dataMap.put("first",new WxTemplateData("邀约消息通知", "#173177"));
            dataMap.put("keyword1",new WxTemplateData(otherUser.getRealname(), "#173177"));
            dataMap.put("keyword2",new WxTemplateData(otherUser.getPhone(), "#173177"));
            dataMap.put("keyword3",new WxTemplateData(visitRecord.getStartDate(), "#173177"));
            dataMap.put("keyword4",new WxTemplateData(visitRecord.getReason(), "#173177"));
            dataMap.put("remark",new WxTemplateData("您的邀约信息已被对方审核", "#173177"));
            sender.setData(dataMap);
            TemplateSenderResult result = iService.templateSend(sender);
            System.out.println(result);
        }

        return ResultGenerator.genSuccessResult("成功");
    }

    public Result visitCommon(VisitRecord visitRecord, String hour,int applyTpey) throws WxErrorException {
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
            return ResultGenerator.genFailResult("在" + startDate + "——" + endDate + "内已经有访问信息存在");
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
                if (!single) {
                    if (recordType == 2) {
                        codeService.sendMsg(user.getPhone(), 4, null, visitor.getRealname(), startDate, realName);
                    } else {
                        codeService.sendMsg(visitor.getPhone(), 5, null, null, startDate, realName);
                    }
                }
            }else {
                //发送短信
                //邀约模板没有
                if (recordType == 2) {
                    codeService.sendMsg(user.getPhone(), 4, null, visitor.getRealname(), startDate, realName);
                } else {
                    codeService.sendMsg(visitor.getPhone(), 5, null, null, startDate, realName);
                }

            }

            //公众号消息推送
            TemplateSender sender = new TemplateSender();
            Map<String, WxTemplateData> dataMap = new HashMap<>();
            VisitRecord vt = visitorRecordMapper.findRecord(userId,visitorId,visitRecord.getVisitDate(),visitRecord.getVisitTime());
            if(applyTpey == 1){
                sender.setTemplate_id("2UBJNiTiPPQTlwu2PHxtbCKhqao3Ix1I8mjGPBIWnUU");
                User me = userService.findById(vt.getUserId());
                User otherUser = userService.findById(vt.getVisitorId());
                if("".equals(otherUser.getWxOpenId()) || otherUser.getWxOpenId().isEmpty()){
                    return ResultGenerator.genSuccessResult(visitRecord);
                }
                sender.setTouser(otherUser.getWxOpenId());
                dataMap.put("first",new WxTemplateData("访问申请", "#173177"));
                dataMap.put("keyword1",new WxTemplateData(me.getRealname(), "#173177"));
                dataMap.put("keyword2",new WxTemplateData(me.getPhone(), "#173177"));
                dataMap.put("keyword3",new WxTemplateData(vt.getStartDate(), "#173177"));
                dataMap.put("keyword4",new WxTemplateData(vt.getReason(), "#173177"));
                dataMap.put("remark",new WxTemplateData("点击查看详情信息↓", "#173177"));
                String params = "?recordId="+vt.getId()+"&otherId="+me.getId();
                sender.setUrl(URL+REPLY+params);
                sender.setData(dataMap);
                TemplateSenderResult result = iService.templateSend(sender);
                System.out.println(result);
            }else{
                sender.setTemplate_id("2UBJNiTiPPQTlwu2PHxtbCKhqao3Ix1I8mjGPBIWnUU");
                User me = userService.findById(vt.getVisitorId());
                User otherUser = userService.findById(vt.getUserId());
                if("".equals(otherUser.getWxOpenId()) || otherUser.getWxOpenId().isEmpty()){
                    return ResultGenerator.genSuccessResult(visitRecord);
                }
                sender.setTouser(otherUser.getWxOpenId());
                sender.setTouser(otherUser.getWxOpenId());
                dataMap.put("first",new WxTemplateData("邀约申请", "#173177"));
                dataMap.put("keyword1",new WxTemplateData(me.getRealname(), "#173177"));
                dataMap.put("keyword2",new WxTemplateData(me.getPhone(), "#173177"));
                dataMap.put("keyword3",new WxTemplateData(vt.getStartDate(), "#173177"));
                dataMap.put("keyword4",new WxTemplateData(vt.getReason(), "#173177"));
                dataMap.put("remark",new WxTemplateData("点击查看详情信息↓", "#173177"));
                String params = "?recordId="+vt.getId()+"&otherId="+me.getId();
                sender.setUrl(URL+REPLY+params);
                sender.setData(dataMap);
                TemplateSenderResult result = iService.templateSend(sender);
                System.out.println(result);
            }


            return ResultGenerator.genSuccessResult(visitRecord);
        }else {
            return ResultGenerator.genFailResult("发送访问失败");
        }
    }

    /**
     * 发送公众号模板消息 ：回应访问
     * @param wxOpenId 要发送对象的微信号
     * @param templateId 模板id
     * @param accessType 进出类型
     * @param visitResult 访问结果
     * @param visitorBy 要发送对象姓名
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @param qrcodeUrl 二维码地址（如果有）
     * @param companyFloor 公司楼层
     * @param orgName 大楼名
     * @param companyName 公司名
     * @throws WxErrorException
     */
    @Override
    public void sendTemplate(String wxOpenId, String templateId, String accessType, String visitResult,
                             String visitorBy, String startDate, String endDate, String qrcodeUrl, String companyFloor,
                             String orgName, String companyName) throws WxErrorException {
        TemplateSender sender=new TemplateSender();
        //公众号模板id
        //朋客联盟
//        sender.setTemplate_id("xtGAH74BuXa6qQD6t8GXjwMwYlLun_OSLxf-DhllTA0");
        //朋悦比邻
        sender.setTemplate_id(templateId);
        sender.setTouser(wxOpenId);
        logger.info("访客微信openId为："+wxOpenId);
        Map<String, WxTemplateData> dataMap = new HashMap<>();
        if ("接受访问".equals(visitResult)) {
            dataMap.put("first", new WxTemplateData("恭喜您访问"+visitorBy+"成功！", "red"));
            dataMap.put("keyword3", new WxTemplateData(startDate+"至"+endDate,"#173177"));
            logger.info("进出方式为："+accessType);
            if("1".equals(accessType)){

                dataMap.put("remark", new WxTemplateData("请到指定地点通过下方二维码进出！\n↓点击详情查看访问二维码","red"));
                //添加二维码
                sender.setUrl(qrcodeUrl);
            }else {
                dataMap.put("remark", new WxTemplateData("请到访问地点刷脸进出！","red"));
            }
        }else{
            dataMap.put("first", new WxTemplateData("很遗憾您访问"+visitorBy+"失败！", "red"));
            dataMap.put("keyword3", new WxTemplateData("访问不通过","#173177"));
        }
        if ("无".equals(companyFloor)||"0".equals(companyFloor)){

            dataMap.put("keyword1", new WxTemplateData(orgName,"black"));
        }else {
            dataMap.put("keyword1", new WxTemplateData(orgName+companyFloor+"层","black"));
        }
        dataMap.put("keyword2", new WxTemplateData(companyName,"#173177"));
        dataMap.put("keyword4", new WxTemplateData(com.soecode.wxtools.util.DateUtil.getNowTime(),"#173177"));
//        dataMap.put("visitResult", new WxTemplateData(visitResult,"#black"));
        sender.setData(dataMap);
        TemplateSenderResult result=iService.templateSend(sender);
        System.out.println(result);
    }

    @Override
    public Map<String, Object> findRecordCompany(Long recordId) {
        return visitorRecordMapper.findRecordCompany(recordId);
    }
}
