package com.company.project.weixin.Interceptor;

import com.soecode.wxtools.api.IService;
import com.soecode.wxtools.api.WxMessageInterceptor;
import com.soecode.wxtools.bean.WxXmlMessage;
import com.soecode.wxtools.exception.WxErrorException;

import java.util.Map;

/**
 * @program: weixin
 * @description: test
 * @author: cwf
 * @create: 2019-09-20 15:17
 **/
public class DemoInterceptor implements WxMessageInterceptor {

 public boolean intercept(WxXmlMessage wxMessage, Map context, IService wxService) throws WxErrorException {
     //可以使用wxService的微信API方法
      //可以在Handler和Interceptor传递消息，使用context上下文
      //可以实现自己的业务逻辑
      //这里就不编写验证关注三天以上的用户了
      if(true){
           return true;
       }
      return false;
  }
}

