package com.company.project.weixin;

import com.company.project.util.RedisUtil;
import com.soecode.wxtools.api.IService;
import com.soecode.wxtools.api.WxConfig;
import com.soecode.wxtools.api.WxService;
import com.soecode.wxtools.bean.WxAccessToken;
import com.soecode.wxtools.exception.WxErrorException;

import java.io.IOException;
import java.util.Map;

public class MyWxService extends WxService implements IService {
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
}