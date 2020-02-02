package com.company.project.weixin.handler;

import com.company.project.weixin.MenuKey;
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
public class ShareRoomHandler implements WxMessageHandler {
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
        StringBuilder result =null;

        result = execute(wxXmlMessage);

        return WxXmlOutMessage.TEXT().content(result.toString()).toUser(wxXmlMessage.getFromUserName()).fromUser(wxXmlMessage.getToUserName()).build();


    }

    private StringBuilder  execute(WxXmlMessage wxMessage) {
        StringBuilder stringBuilder = new StringBuilder();
        switch (wxMessage.getEventKey()) {
            case MenuKey.MEETING:
                stringBuilder.append("会议室申请");
                break;
            case   MenuKey.TEA:
                stringBuilder.append("茶室申请");
                break;
            default:
                stringBuilder.append("暂时无此分类噢！");
                break;
        }
        return stringBuilder;

    }

}
