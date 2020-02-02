package com.company.project.weixin.matcher;

import com.soecode.wxtools.api.WxMessageMatcher;
import com.soecode.wxtools.bean.WxXmlMessage;
import com.soecode.wxtools.util.StringUtils;

/**
 * @program: weixin
 * @description: 匹配器
 * @author: cwf
 * @create: 2019-09-20 14:22
 **/
public class WhoAmIMatcher implements WxMessageMatcher {

    @Override
    public boolean match(WxXmlMessage message) {
      if(StringUtils.isNotEmpty(message.getContent())){
              if(message.getContent().equals("我是谁")){
                      return true;
                  }
          }
       return false;
    }

}
