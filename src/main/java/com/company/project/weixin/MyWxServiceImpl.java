package com.company.project.weixin;

import com.company.project.util.RedisUtil;
import com.soecode.wxtools.api.WxConfig;
import com.soecode.wxtools.api.WxService;
import com.soecode.wxtools.bean.TemplateSender;
import com.soecode.wxtools.bean.WxAccessToken;
import com.soecode.wxtools.bean.result.TemplateSenderResult;
import com.soecode.wxtools.bean.result.WxOAuth2AccessTokenResult;
import com.soecode.wxtools.exception.WxErrorException;

import java.io.IOException;
import java.util.Map;

/**
 * @author cwf
 * @date 2020/4/28 9:57
 */
public class MyWxServiceImpl extends WxService implements MyService {

    @Override
    public String getAccessToken(boolean forceRefresh) throws WxErrorException {
        if (forceRefresh) {
            WxConfig.getInstance().expireAccessToken();
        }

        if (WxConfig.getInstance().isAccessTokenExpired()) {
            synchronized(globalAccessTokenRefreshLock) {
                if (WxConfig.getInstance().isAccessTokenExpired()) {
                    String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET".replace("APPID", WxConfig.getInstance().getAppId()).replace("APPSECRET", WxConfig.getInstance().getAppSecret());

                    try {
                        String resultContent = this.get(url, (Map)null);
                        WxAccessToken accessToken = WxAccessToken.fromJson(resultContent);
                        WxConfig.getInstance().updateAccessToken(accessToken.getAccess_token(), accessToken.getExpires_in());
                        System.out.println("[wx-tools]update accessToken success. accessToken:" + accessToken.getAccess_token());
                        System.out.println("setRedis index2 success ");
                        RedisUtil.setStr("accessToken",accessToken.getAccess_token(),2,7000);
                    } catch (IOException var7) {
                        throw new WxErrorException("[wx-tools]refresh accessToken failure.");
                    }
                }
            }
        }

        return WxConfig.getInstance().getAccessToken();
    }

    @Override
    public WxOAuth2AccessTokenResult otherAuth2ToGetAccessToken(String appId, String appSecret, String code) throws WxErrorException {
        WxOAuth2AccessTokenResult result = null;
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code".replace("APPID", appId).replace("SECRET", appSecret).replace("CODE", code);
        String getResult = this.get(url, (Map)null);

        try {
            result = WxOAuth2AccessTokenResult.fromJson(getResult);
            return result;
        } catch (IOException var6) {
            throw new WxErrorException("[wx-tools]oauth2ToGetAccessToken failure.");
        }
    }

    @Override
    public String OtheroAuth2buildAuthorizationUrl(String appId, String redirectUri, String scope, String state) throws WxErrorException {
//        redirectUri = URIUtil.encodeURIComponent(redirectUri);
        String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect".replace("APPID", appId).replace("REDIRECT_URI", redirectUri).replace("SCOPE", scope).replace("STATE", state);
        return url;
    }

    @Override
    public String getOtherAccessToken(String appId,String appSecret) throws WxErrorException {
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET".replace("APPID", appId).replace("APPSECRET",appSecret);
        String resultContent = this.get(url, (Map)null);
        try {
            WxAccessToken accessToken = WxAccessToken.fromJson(resultContent);
            String access_token = accessToken.getAccess_token();
            return access_token;
        }  catch (IOException var7) {
            throw new WxErrorException("[wx-tools]refresh accessToken failure.");
        }
    }
    @Override
    public TemplateSenderResult otherTemplateSend(String accessToken, TemplateSender sender) throws WxErrorException {
        TemplateSenderResult result = null;
        String postResult = null;
        String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN".replace("ACCESS_TOKEN", accessToken);

        try {
            postResult = this.post(url, sender.toJson());
            result = TemplateSenderResult.fromJson(postResult);
            return result;
        } catch (IOException var6) {
           return null;
        }
    }
}