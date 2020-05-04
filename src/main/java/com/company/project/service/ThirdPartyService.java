package com.company.project.service;

import com.soecode.wxtools.exception.WxErrorException;

public interface ThirdPartyService {
    /**
     * 获取微信发来的 verrifyTicket 并保持redis
     * @param timestamp
     * @param nonce
     * @param msgSignature
     * @param postData
     * @throws Exception
     */
    void getComponentVerifyTicket(String timestamp, String nonce, String msgSignature, String postData) throws Exception;

    void setComponentAccessToken() throws WxErrorException;
}
