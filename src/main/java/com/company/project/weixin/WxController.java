package com.company.project.weixin;

import com.company.project.annotation.AuthCheckAnnotation;
import com.company.project.dao.VisitRecordMapper;
import com.company.project.model.VisitRecord;
import com.company.project.service.UserService;
import com.company.project.service.visitRecordService;
import com.company.project.weixin.handler.MyHandler;
import com.company.project.weixin.handler.ShareRoomHandler;
import com.company.project.weixin.handler.VisitHandler;
import com.company.project.weixin.handler.WhoAmIHandler;
import com.company.project.weixin.matcher.WhoAmIMatcher;
import com.company.project.weixin.model.WxTemplateData;
import com.soecode.wxtools.api.IService;
import com.soecode.wxtools.api.WxConsts;
import com.soecode.wxtools.api.WxMessageRouter;
import com.soecode.wxtools.api.WxService;
import com.soecode.wxtools.bean.TemplateSender;
import com.soecode.wxtools.bean.WxXmlMessage;
import com.soecode.wxtools.bean.WxXmlOutMessage;
import com.soecode.wxtools.bean.result.TemplateSenderResult;
import com.soecode.wxtools.exception.WxErrorException;
import com.soecode.wxtools.util.DateUtil;
import com.soecode.wxtools.util.xml.XStreamTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import static com.company.project.weixin.MenuKey.REPLY;
import static com.company.project.weixin.MenuKey.URL;

/**
 * @program: spring-boot-api-project-seed
 * @description: 微信
 * @author: cwf
 * @create: 2019-09-19 16:49
 **/

@RestController
@RequestMapping("/wx")
public class WxController {
    Logger logger = LoggerFactory.getLogger(WxController.class);
    private IService iService = new WxService();
    @Autowired
    private visitRecordService visitRecordService;

    @Resource
    private VisitRecordMapper visitorRecordMapper;

    @AuthCheckAnnotation(checkLogin = false, checkVerify = false)
    @GetMapping
    public String check(String signature, String timestamp, String nonce, String echostr) {
        logger.info("signature: {},timestamp: {},nonce: {},echostr： {}", signature, timestamp, nonce, echostr);
        if (iService.checkSignature(signature, timestamp, nonce, echostr)) {
            logger.info("微信发送成功");
            return echostr;
        }
        return null;
    }

    @AuthCheckAnnotation(checkLogin = false, checkVerify = false)
    @PostMapping
    public void handle(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        // 创建一个路由器
        WxMessageRouter router = new WxMessageRouter(iService);
        try {
            // 微信服务器推送过来的是XML格式。
            WxXmlMessage wx = XStreamTransformer.fromXml(WxXmlMessage.class, request.getInputStream());
            System.out.println("消息：\n " + wx.toString());
            router.rule().msgType(WxConsts.XML_MSG_TEXT).matcher(new WhoAmIMatcher()).handler(new WhoAmIHandler()).end().
                    rule().event(WxConsts.EVT_CLICK).eventKey(MenuKey.VISIT).handler(new VisitHandler()).next().
                    rule().event(WxConsts.EVT_CLICK).eventKey(MenuKey.INVITE).handler(new VisitHandler()).end().
                    rule().event(WxConsts.EVT_CLICK).eventKey(MenuKey.MEETING).handler(new ShareRoomHandler()).next().
                    rule().event(WxConsts.EVT_CLICK).eventKey(MenuKey.TEA).handler(new ShareRoomHandler()).end().
                    rule().event(WxConsts.EVT_CLICK).eventKey(MenuKey.FIRST_RECORD).handler(new MyHandler()).next().
                    rule().event(WxConsts.EVT_CLICK).eventKey(MenuKey.SHARE_RECORD).handler(new MyHandler()).end();
            // 把消息传递给路由器进行处理
            WxXmlOutMessage xmlOutMsg = router.route(wx);
            if (xmlOutMsg != null)
                // 因为是明文，所以不用加密，直接返回给用户
                out.print(xmlOutMsg.toXml());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.close();
        }

    }

    @AuthCheckAnnotation(checkLogin = false, checkVerify = false)
    @PostMapping("/sendTempMsg")
    public void sendTempMsg(@RequestParam(defaultValue = "0") String wxOpenId,@RequestParam(defaultValue = "0") Long userId) throws WxErrorException {
        /*TemplateSender sender = new TemplateSender();
        //公众号模板id
        //朋客联盟
        // sender.setTemplate_id("xtGAH74BuXa6qQD6t8GXjwMwYlLun_OSLxf-DhllTA0");
        //朋悦比邻
        sender.setTemplate_id("2UBJNiTiPPQTlwu2PHxtbCKhqao3Ix1I8mjGPBIWnUU");
        sender.setTouser(wxOpenId);
        logger.info("访客微信openId为：" + wxOpenId);
        Map<String, WxTemplateData> dataMap = new HashMap<>();
       *//* dataMap.put("test",new WxTemplateData("你好", "#173177"));
        dataMap.put("companyAddr",new WxTemplateData("你好", "#173177"));*//*
        dataMap.put("first",new WxTemplateData("访问申请", "#173177"));
        dataMap.put("keyword1",new WxTemplateData("林", "#173177"));
        dataMap.put("keyword2",new WxTemplateData("18065988666", "#173177"));
        dataMap.put("keyword3",new WxTemplateData("2015-05-30 12:50", "#173177"));
        dataMap.put("keyword4",new WxTemplateData("其他", "#173177"));
        dataMap.put("remark",new WxTemplateData("点击查看详情信息↓", "#173177"));
        sender.setUrl(URL+"replyVisit?recordId=1731&name=%u53D1&phone=123&cstatus=applying&visitDate=2020-03-06");
        sender.setData(dataMap);
        TemplateSenderResult result = iService.templateSend(sender);
        System.out.println(result);*/
    }
}
