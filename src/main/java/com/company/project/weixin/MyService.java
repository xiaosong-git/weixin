package com.company.project.weixin;

import com.soecode.wxtools.api.IService;
import com.soecode.wxtools.bean.TemplateSender;
import com.soecode.wxtools.bean.result.TemplateSenderResult;
import com.soecode.wxtools.bean.result.WxOAuth2AccessTokenResult;
import com.soecode.wxtools.exception.WxErrorException;

import java.io.IOException;

/**
 * 我的Iservice
 * @author cwf
 * @date 2020/4/28 9:53
 */
public interface MyService extends IService {

    /**
     * 第三方公众号获取粉丝信息接口
     *
     * @param code	 微信传来的code
     * @param appId	公众号id
     * @param appSecret	 公众号秘钥
     * @return com.soecode.wxtools.bean.result.WxOAuth2AccessTokenResult
     * @throws WxErrorException   微信错误信息
     * @author cwf
     * @date 2020/4/28 10:03
     */
    WxOAuth2AccessTokenResult otherAuth2ToGetAccessToken(  String appId,String appSecret, String code) throws WxErrorException;

    /**
     * 其他公众号跳转页面
     * @param appId	 其他公众号的appId
     * @param redirectUri	其他公众号的
     * @param scope
     * @param state
     * @return java.lang.String
     * @throws Exception
     * @author cwf
     * @date 2020/4/28 11:41
     */
    String OtheroAuth2buildAuthorizationUrl(String appId, String redirectUri, String scope, String state) throws WxErrorException;

    /**
     * 获取其他微信号得accessToken
     * @param appId	 其他微信号得appId
     * @param appSecret	 其他微信号的appSecret
     * @return java.lang.String
     * @author cwf
     * @date 2020/4/29 11:26
     */
    String getOtherAccessToken(String appId, String appSecret) throws WxErrorException, IOException;
    /**
     *
     * @param accessToken	其他微信的token
     * @param sender 模板消息
     * @return com.soecode.wxtools.bean.result.TemplateSenderResult
     * @throws WxErrorException  微信模板错误
     * @author cwf
     * @date 2020/4/29 11:27
     */

    TemplateSenderResult otherTemplateSend(String accessToken, TemplateSender sender) throws WxErrorException, IOException;
}
