package com.company.project.service.impl;

import cn.hutool.core.util.XmlUtil;
import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSONObject;
import com.company.project.service.ThirdPartyService;
import com.company.project.util.RedisUtil;
import com.company.project.weixin.MyWxServiceImpl;
import com.soecode.wxtools.api.IService;
import com.soecode.wxtools.exception.WxErrorException;
import com.soecode.wxtools.util.crypto.WXBizMsgCrypt;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;


/**
 * @program: weixin
 * @description: 第三方平台接口
 * @author: cwf
 * @create: 2020-04-22 14:27
 **/
@Service
public class ThirdPartyServiceImpl implements ThirdPartyService {
    private final Logger logger = LoggerFactory.getLogger(ThirdPartyServiceImpl.class);

    @Value("${component.appId}")
    private String componentAppId;
    @Value("${component.appSecret}")
    private String appSecret;
    private IService iService = new MyWxServiceImpl();
    @Override
    public void getComponentVerifyTicket(String timestamp, String nonce, String msgSignature, String postData) throws Exception {
        // 需要加密的明文
        String encodingAesKey = "LJdrAbC5yibwFk3ZZNl63CUpH4aatzJePGON2ZSex6w";

        WXBizMsgCrypt pc = new WXBizMsgCrypt("weixin123456", encodingAesKey, componentAppId);

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        dbf.setFeature("http://xml.org/sax/features/external-general-entities", false);
        dbf.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
        dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        dbf.setXIncludeAware(false);
        dbf.setExpandEntityReferences(false);
        DocumentBuilder db = dbf.newDocumentBuilder();
        StringReader sr = new StringReader(postData);
        InputSource is = new InputSource(sr);
        Document document = db.parse(is);

        Element root = document.getDocumentElement();
        NodeList nodelist1 = root.getElementsByTagName("Encrypt");
        String encrypt = nodelist1.item(0).getTextContent();

        String format = "<xml><ToUserName><![CDATA[toUser]]></ToUserName><Encrypt><![CDATA[%1$s]]></Encrypt></xml>";
        String fromXML = String.format(format, encrypt);

        String result = pc.decryptMsg(msgSignature, timestamp, nonce, fromXML);
        logger.info("解密后: " + result);

        //获取ComponentVerifyTicket
         Map<String, Object> xmlMap = XmlUtil.xmlToMap(result);
        String componentVerifyTicket = xmlMap.get("ComponentVerifyTicket").toString();
        if(StringUtils.isNotBlank(componentVerifyTicket)) {
            //获取ticket,没有则为authorized事件
            RedisUtil.setStr("ComponentVerifyTicket", componentVerifyTicket,2,7200);
            logger.info("ComponentVerifyTicket: " + componentVerifyTicket);
        }
    }

    @Override
    public void setComponentAccessToken() throws WxErrorException {
        String componentVerifyTicket = RedisUtil.getStrVal("ComponentVerifyTicket", 2);
        String url="https://api.weixin.qq.com/cgi-bin/component/api_component_token";
        JSONObject json = new JSONObject();
        json.put("component_appid",componentAppId);
        json.put("component_appsecret",appSecret);
        json.put("component_verify_ticket",componentVerifyTicket);
        HashMap<String, String> headers = new HashMap<>();
        //存放请求头，可以存放多个请求头
        headers.put("Content-Type", "application/json; encoding=utf-8");
        String result= HttpRequest.post(url).body(json.toJSONString()).execute().body();
        JSONObject jsonObject = JSONObject.parseObject(result);
        Object componentAccessToken = jsonObject.get("component_access_token");
        if (componentAccessToken!=null){
            String value = componentAccessToken.toString();
            logger.info(value);
            RedisUtil.setStr("componentAccessToken", value,2,7200);
        }
        logger.info(result);
    }

    public static void main(String[] args) {
//        String componentVerifyTicket = RedisUtil.getStrVal("ComponentVerifyTicket", 2);
        String url="https://api.weixin.qq.com/cgi-bin/component/api_component_token";
        JSONObject json = new JSONObject();
        json.put("component_appid","wx4aecbb914c5bcf48");
        json.put("component_appsecret","51942c5a17fa44ba11cae98e31a8cc74");
        json.put("component_verify_ticket","ticke1@@@NF9FIXDjyTmJcS0SVz9XJpRviwZBfaEr5NUqiFk_nI1NFf0wkag9BCdTY1tRA9O5XBpqnXjJFlSusP3evHf8SQ");
        HashMap<String, String> headers = new HashMap<>();
        //存放请求头，可以存放多个请求头
        headers.put("Content-Type", "application/json; encoding=utf-8");
        String result= HttpRequest.post(url).body(json.toJSONString()).execute().body();
        JSONObject jsonObject = JSONObject.parseObject(result);
        Object componentCccessToken = jsonObject.get("component_access_token");
        if (componentCccessToken!=null){
            System.out.println(componentCccessToken.toString());
        }
        System.out.println(result);
    }



}
