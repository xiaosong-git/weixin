package com.company.project.weixin.handler;

import com.soecode.wxtools.api.IService;
import com.soecode.wxtools.api.WxMessageHandler;
import com.soecode.wxtools.bean.WxXmlMessage;
import com.soecode.wxtools.bean.WxXmlOutMessage;
import com.soecode.wxtools.exception.WxErrorException;

import java.util.Map;

/**
 * @program: weixin
 * @description: 处理器
 * @author: cwf
 * @create: 2019-09-20 11:19
 **/
public class WhoAmIHandler implements WxMessageHandler {
//    private boolean isRun = false;
//    private synchronized  boolean getIsRun() { return isRun; }
//    private synchronized void setRun(boolean run) { isRun = run; }

    /**
     * 处理消息为
     * @param wxXmlMessage
     * @param map
     * @param iService
     * @return
     * @throws WxErrorException
     */
    @Override
    public WxXmlOutMessage handle(WxXmlMessage wxXmlMessage, Map<String, Object> map, IService iService) throws WxErrorException {
        WxXmlOutMessage response = null;
//          if (!getIsRun()) {
//                  setRun(true);
                  response = execute(wxXmlMessage);
//                  setRun(false);
//              }
          return response;

    }

    private WxXmlOutMessage execute(WxXmlMessage wxMessage) {

             return WxXmlOutMessage.TEXT().content(wxMessage.getFromUserName()).toUser(wxMessage.getFromUserName()).fromUser(wxMessage.getToUserName()).build();
         }

}
