package com.company.project.web;


import com.company.project.annotation.AuthCheckAnnotation;
import com.company.project.weixin.MenuKey;
import com.company.project.weixin.WxController;
import com.soecode.wxtools.api.IService;
import com.soecode.wxtools.api.WxConfig;
import com.soecode.wxtools.api.WxService;
import com.soecode.wxtools.bean.WxJsapiConfig;
import com.soecode.wxtools.bean.WxUserList;
import com.soecode.wxtools.bean.result.WxOAuth2AccessTokenResult;
import com.soecode.wxtools.exception.WxErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;


/**
 * @program: weixin
 * @description: 动态网页
 * @author: cwf
 * @create: 2019-09-21 09:24
 **/
    @Controller
    @RequestMapping("/")
    public class ThymeleafController {
    Logger logger = LoggerFactory.getLogger(WxController.class);
    private IService iService = new WxService();
        @AuthCheckAnnotation(checkLogin = false,checkVerify = false)
        @RequestMapping(value = "/test")
        public String test(@RequestParam(name = "name", required = false, defaultValue = "world") String name,
                               Model model) {
            model.addAttribute("xname", name);
            return "index";
        }
        @AuthCheckAnnotation(checkLogin = false,checkVerify = false)
        @RequestMapping(value = "/login",method= RequestMethod.GET)
        public String login(@RequestParam(name="code",required=false)String code,
                            @RequestParam(name="state",defaultValue = "0" )String state,Model model) throws WxErrorException {

            System.out.println("-------微信传来请求数据为："+code+"-----------------------"+state);
            model.addAttribute("state", state);
            WxOAuth2AccessTokenResult wxOAuth2AccessTokenResult = iService.oauth2ToGetAccessToken(code);
            System.out.println("-------Access_token: "+wxOAuth2AccessTokenResult.getAccess_token());
//            WxOAuth2AccessTokenResult accessToken = iService.oauth2ToGetRefreshAccessToken(wxOAuth2AccessTokenResult.getRefresh_token());
//            System.out.println("-------accessToken: "+accessToken);
//            WxError wxError = iService.oauth2CheckAccessToken(accessToken.getAccess_token(), wxOAuth2AccessTokenResult.getOpenid());
//            System.out.println("-------wxError: "+wxError);
            //获取微信登入的openid
            WxUserList.WxUser.WxUserGet wxUser=new WxUserList.WxUser.WxUserGet();
            wxUser.setOpenid(wxOAuth2AccessTokenResult.getOpenid());
            wxUser.setLang("zh_CN");
            WxUserList.WxUser wxUser1 = iService.oauth2ToGetUserInfo(wxOAuth2AccessTokenResult.getAccess_token(), wxUser);
            System.out.println("-------wxUser1: "+wxUser1);
            model.addAttribute("openId",wxUser1.getOpenid());
//            model.addAttribute("openId","test");
            return "login";
        }
        @AuthCheckAnnotation(checkLogin = false,checkVerify = false)
        @RequestMapping(value = "/visit",method= RequestMethod.GET)
        public String visit(@RequestParam(name="code",required=false)String code,
                            @RequestParam(name="state",defaultValue = "0" )String state,
                            Model model) throws WxErrorException {
            List<String> jsApiList = new ArrayList<>();

           //需要用到哪些JS SDK API 就设置哪些
           jsApiList.add("chooseImage");//拍照或从手机相册中选图接口
           jsApiList.add("onMenuShareQZone");//获取“分享到QQ空间”按钮点击状态及自定义分享内容接口
           jsApiList.add("previewImage");//预览图片接口
           jsApiList.add("uploadImage");//上传图片接口
           jsApiList.add("downloadImage");//下载图片接口
           try {
                 //把config返回到前端进行js调用即可。
                 WxJsapiConfig config = iService.createJsapiConfig(MenuKey.URL+"visit", jsApiList);
                 config.setAppid(WxConfig.getInstance().getAppId());
//                 System.out.println(config.getAppid());
//                 System.out.println(config.getSignature());
//                 System.out.println(config.getTimestamp());
//                 System.out.println(config.getUrl());
//                 System.out.println(config.getJsApiList());
                 System.out.println(config.getNoncestr());
//                 System.out.println(config.toJson());
               model.addAttribute("config", config);
               logger.info("进入设置权限");
           } catch (WxErrorException e) {
               e.printStackTrace();
           }
            return "visit";
        }
//        @AuthCheckAnnotation(checkLogin = false,checkVerify = false)
//        @RequestMapping(value = "/form12")
//        public String room(@RequestParam(name = "name", required = false, defaultValue = "world") String name,
//                        Model model) {
//        return "form12";
//        }

//        @AuthCheckAnnotation(checkLogin = false,checkVerify = false)
//        @RequestMapping({"static/MP_verify_I4XWI1ZSKeFojwT6.txt"})
//        private String returnConfigFile(HttpServletResponse response) {
//           return "I4XWI1ZSKeFojwT6";
//        }


        @AuthCheckAnnotation(checkLogin = false,checkVerify = false)
        @RequestMapping(value = "/auth",method= RequestMethod.GET)
        public String auth(Model model) {
            List<String> jsApiList = new ArrayList<>();

           //需要用到哪些JS SDK API 就设置哪些
           jsApiList.add("chooseImage");//拍照或从手机相册中选图接口
           jsApiList.add("onMenuShareQZone");//获取“分享到QQ空间”按钮点击状态及自定义分享内容接口
           jsApiList.add("previewImage");//预览图片接口
           jsApiList.add("uploadImage");//上传图片接口
           jsApiList.add("downloadImage");//下载图片接口
            logger.info("已进入auth界面");
           try {
                 //把config返回到前端进行js调用即可。
                 WxJsapiConfig config = iService.createJsapiConfig(MenuKey.URL+"auth", jsApiList);
                 config.setAppid(WxConfig.getInstance().getAppId());
//                 System.out.println(config.getAppid());
//                 System.out.println(config.getSignature());
//                 System.out.println(config.getTimestamp());
//                 System.out.println(config.getUrl());
//                 System.out.println(config.getJsApiList());
                 System.out.println(config.getNoncestr());
//                 System.out.println(config.toJson());
               model.addAttribute("config", config);
               logger.info("进入设置权限");
           } catch (WxErrorException e) {
               e.printStackTrace();
           }

            return "auth";
        }

    }