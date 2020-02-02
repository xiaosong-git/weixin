package com.company.project.service;


import com.company.project.core.Result;

/**
 * @Author linyb
 * @Date 2017/4/3 15:55
 */
public interface CodeService {
    /**
     * 验证验证码
     * @Author linyb
     * @Date 2017/4/3 15:57
     */
    Boolean verifyCode(String phone, String code);
    /**
     * 发送短信验证码
     * @Author linyb
     * @Date 2017/4/3 16:09
     */
    Result sendMsg(String phone, Integer type, String visitorResult, String visitorBy, String visitorDateTime, String visitor);

    void YMNotification(String deviceToken, String deviceType, String notification_title, String msg_content) throws Exception;
}
