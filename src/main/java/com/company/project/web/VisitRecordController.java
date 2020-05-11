package com.company.project.web;

import com.company.project.configurer.MvcConfig;
import com.company.project.core.Result;
import com.company.project.core.ResultGenerator;
import com.company.project.model.VisitRecord;
import com.company.project.service.visitRecordService;
import com.company.project.util.DateUtil;
import com.company.project.util.RedisUtil;
import com.company.project.weixin.MyService;
import com.company.project.weixin.MyWxServiceImpl;
import com.company.project.weixin.model.WxTemplateData;
import com.soecode.wxtools.bean.TemplateSender;
import com.soecode.wxtools.bean.result.TemplateSenderResult;
import com.soecode.wxtools.bean.result.WxError;
import com.soecode.wxtools.exception.WxErrorException;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* Created by CodeGenerator on 2019/09/22.
*/
@RestController
    @RequestMapping("/visit/record")
public class VisitRecordController {
    @Resource
    private visitRecordService visitRecordService;
    private MyService iService = new MyWxServiceImpl();
    private final org.slf4j.Logger logger = LoggerFactory.getLogger(MvcConfig.class);
    /**
     * @param hour 结束时间
     * @return result
     * @throws Exception
     * @author cwf
     * @date 2019/10/8 16:11
     */
     @PostMapping("/visitRequest")
    public Result visitRequest(VisitRecord visitRecord, @RequestParam() String hour)
             throws Exception {
         try {
             return visitRecordService.visitRequest(visitRecord, hour);
         } catch (Exception e) {
             e.printStackTrace();
         }

         return ResultGenerator.genFailResult("系统错误，请重试","");

    }

    @PostMapping("/inviteRequest")
    public Result inviteRequest(VisitRecord visitRecord, @RequestParam() String hour)
            throws Exception {
        try {
            return visitRecordService.inviteRequest( visitRecord, hour);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultGenerator.genFailResult("系统错误，请重试","");
    }

    /**
     *
     * @param userId 用户id
     * @return
     * @throws Exception
     */
    @PostMapping("/record")
    public Result record(@RequestParam() Long userId,@RequestParam() int pages,@RequestParam() int sizes)
            throws Exception {
        try {
            return visitRecordService.record( userId,pages,sizes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultGenerator.genFailResult("系统错误，请重试","");
    }
    /**
     *
     * @param loginId 用户id
     * @return
     * @throws Exception
     */
    @PostMapping("/recordDetail")
    public Result recordDetail(@RequestParam() Long visitorId,@RequestParam() Long loginId,@RequestParam() int pages,@RequestParam() int sizes)
            throws Exception {
        try {
            return visitRecordService.recordDetail(visitorId,loginId,pages,sizes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultGenerator.genFailResult("系统错误，请重试","");

    }

    /**
     * 审核访问或邀约
     * @param visitRecord 访问记录
     * @param loginId 登入人id
     * @return
     * @throws Exception
     */
    @PostMapping("/recordReply")
    public Result recordReply(VisitRecord visitRecord,
                              @RequestParam() Long loginId)
            throws Exception {
        try {
            return visitRecordService.recordReply(visitRecord,loginId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultGenerator.genFailResult("系统错误，请重试","");
    }


    /**
     *  审核访问
     * @param recordId  访问记录ID
     * @param cstatus   状态
     * @return
     */
    @PostMapping("/confireRecord")
    public Result confireRecord(Long recordId,String cstatus,String companyId){
        try {
            return visitRecordService.recordComfire(recordId,cstatus,companyId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultGenerator.genFailResult("系统错误，请重试","");
    }

    @PostMapping("/getRecord")
    public Result getRecord(Long recordId){
        Map<String,Object> visitRecord =  visitRecordService.findRecordCompany(recordId);
        if(null == visitRecord){
           return ResultGenerator.genFailResult("数据错误");
        }
        String startDate = String.valueOf(visitRecord.get("startDate")) ;
        String endDate = String.valueOf(visitRecord.get("endDate")) ;
        String now = DateUtil.getCurDate() +" "+DateUtil.getCurTime();
        String isValue;
        System.out.println(endDate.compareTo(now));
        System.out.println(startDate.compareTo(now));
        if(endDate.compareTo(now) > 0){
            isValue ="T";
        }else{
            isValue ="F";
        }
        visitRecord.put("isValue",isValue);
        return ResultGenerator.genSuccessResult(visitRecord);
    }

    @PostMapping("/test/temp")
    public Result testTemp(String token){
        TemplateSender sender = new TemplateSender();
        Map<String, WxTemplateData> dataMap = new HashMap<>();
        sender.setTemplate_id("I-nNgggadJrZcZmkJzgxMVOptw4tf2MD9NKgYhWnCdM");
        sender.setTouser("oFw0JwGlkNWM9DByJR8C76hSgYuc");
        dataMap.put("first",new WxTemplateData("访问消息通知", "#173177"));
        dataMap.put("keyword1",new WxTemplateData("测试", "#173177"));
        dataMap.put("keyword2",new WxTemplateData("测试", "#173177"));
        dataMap.put("keyword3",new WxTemplateData("测试", "#173177"));
        dataMap.put("keyword4",new WxTemplateData("测试", "#173177"));
        dataMap.put("remark",new WxTemplateData("您的访问申请信息已被对方审核", "#173177"));
        sender.setData(dataMap);
        try {
            TemplateSenderResult templateSenderResult = iService.otherTemplateSend(token, sender);

        } catch (WxErrorException e) {
            WxError error = e.getError();
            logger.error("错误码:{},错误内容:{}",error.getErrcode(),error.getErrmsg());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
