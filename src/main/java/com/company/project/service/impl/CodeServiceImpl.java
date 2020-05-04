package com.company.project.service.impl;

import com.company.project.core.Result;
import com.company.project.core.ResultGenerator;
import com.company.project.service.ParamService;
import com.company.project.service.CodeService;
import com.company.project.util.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 手机验证码
 * @Author linyb
 * @Date 2017/4/3 15:55
 */
@Service
@Transactional
public class CodeServiceImpl implements CodeService {
    @Autowired
    private ParamService paramService;
    @Override
    public Boolean verifyCode(String phone, String code) {
       //判断参数完整性
        if ((!StringUtils.isEmpty(phone)) && (!StringUtils.isEmpty(code))) {
            //从Redis中取出正确验证码
            Object obj = RedisUtil.getObject(phone.getBytes(),31);
            if(obj == null){
                return false;
            }
            String redisCode = (String)obj;
            //                RedisUtil.delObject(phone.getBytes(), 31);
            //比对
            return (code.equals(redisCode));
        }
        return false;
    }

    @Override
    public Result sendMsg(String phone, Integer type, String visitorResult, String visitorBy, String visitorDateTime, String visitor) {
        String code = NumberUtil.getRandomCode(6);
        String limit = paramService.findValueByName("maxErrorInputSyspwdLimit");
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String state = YunPainSmsUtil.sendSmsCode(code, phone, type, date, null, visitorResult, visitorBy, visitorDateTime, visitor);
        if("0000".equals(state)){
            boolean flag = RedisUtil.setObject(phone.getBytes(), code,60*30, 31) != null;
            return flag ? ResultGenerator.genSuccessResult() : ResultGenerator.genFailResult("发送短信失败");
        }else {
            return ResultGenerator.genFailResult(state);
        }
    }
    @Override
    public void YMNotification(String deviceToken,String deviceType,String notification_title,String msg_content) throws Exception{
            if (Constant.DEVICE_IOS.equals(deviceType)) {
                YMNotification.httpToYMIOS(deviceToken,notification_title , msg_content);
            } else if (Constant.DEVICE_ANDRIOD.equals(deviceType)) {
              YMNotification.httpToYMAndroid(deviceToken, notification_title, msg_content);
            }else {//没有Type时都发送
                 YMNotification.httpToYMIOS(deviceToken, notification_title, msg_content);
                 YMNotification.httpToYMAndroid(deviceToken, notification_title, msg_content);
            }
        }

    }
