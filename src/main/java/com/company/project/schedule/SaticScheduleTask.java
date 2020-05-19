package com.company.project.schedule;

import com.company.project.util.RedisUtil;
import com.company.project.weixin.MyService;
import com.company.project.weixin.MyWxServiceImpl;
import com.company.project.weixin.model.WxTemplateData;
import com.soecode.wxtools.bean.TemplateSender;
import com.soecode.wxtools.exception.WxErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.company.project.service.visitRecordService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @program: weixin
 * @description:
 * @author: cwf
 * @create: 2020-05-18 17:41
 **/
@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
public class SaticScheduleTask {

    Logger logger = LoggerFactory.getLogger(SaticScheduleTask.class);
    private MyService iService = new MyWxServiceImpl();

    @Autowired
    private visitRecordService visitRecordService;

    //3.添加定时任务 30分钟
//        @Scheduled(cron = "0 0/30 * * * ? ")
    //或直接指定时间间隔，例如：5秒
    @Scheduled(fixedRate = 3600 * 1000)
    private void configureTasks() throws WxErrorException, IOException {
        RedisUtil.setStr("accessToken", iService.getAccessToken(), 2, 7000);
//            thirdPartyService.setComponentAccessToken();
//            List<otherWx> wxList = otherWxService.findAll();
//            for (com.company.project.model.otherWx otherWx : wxList) {
//                try {
//
//                    logger.info("更新前：redis第三方accessToken：{},", RedisUtil.getStrVal(otherWx.getWxValue(), 2));
//                    String otherAccessToken = iService.getOtherAccessToken(otherWx.getAppid(), otherWx.getSecret());
//                    String s = RedisUtil.setStr(otherWx.getWxValue(), otherAccessToken, 2, 7000);
//                    logger.info("更新第三方accessToken成功！,{},{}", s, otherAccessToken);
//                    logger.info("更新后：redis第三方accessToken：{},", RedisUtil.getStrVal(otherWx.getWxValue(), 2));
//                } catch (Exception e) {
//                    logger.error("获取第三方accessToken报错,{},{}", otherWx.getWxValue(), otherWx.getAppid());
//                }
//            }
//            logger.info("存储acessToken时间: {},acessToken：{}" , LocalDateTime.now(),iService.getAccessToken());
    }

    @Scheduled(fixedRate = 60 * 1000)
    private void pushOverTime() throws WxErrorException, IOException {
        Set<String> delayBucket2 = RedisUtil.getZsetByMaxMinRegion("delayBucket", System.currentTimeMillis() + 3600 * 1000, 0L, 2);
        if (delayBucket2.size() > 0) {
            Map<String, Object> dataMap = new HashMap<>();
            TemplateSender sender = new TemplateSender();
            for (String s : delayBucket2) {
                logger.info("要发送的过期模板：{}", s);
                String[] split = s.split(",");
                if (split.length > 0) {
                    dataMap.put("first", new WxTemplateData("消息过期通知", "#F90000"));
                    dataMap.put("keyword1", new WxTemplateData(split[2], "#173177"));
                    dataMap.put("keyword2", new WxTemplateData(split[3], "#173177"));
                    dataMap.put("keyword3", new WxTemplateData(split[4], "#173177"));
                    dataMap.put("keyword4", new WxTemplateData(split[5], "#173177"));
                    dataMap.put("remark", new WxTemplateData(split[6], "#F90000"));
                    sender.setUrl(split[7]);
                    sender.setData(dataMap);
                    boolean b = visitRecordService.otherTemplateSend(Long.parseLong(split[1]), sender);
                    if (b) {
                        //删除发送的模板消息集合
                        Long delayBucket = RedisUtil.delZset("delayBucket", 2, s);
                    }

                }
            }
        }
    }
}

