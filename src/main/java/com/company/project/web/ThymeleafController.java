package com.company.project.web;


import com.company.project.annotation.AuthCheckAnnotation;
import com.company.project.dao.OtherWxMapper;
import com.company.project.model.User;
import com.company.project.model.otherWx;
import com.company.project.service.UserService;
import com.company.project.weixin.MenuKey;
import com.company.project.weixin.MyService;
import com.company.project.weixin.MyWxServiceImpl;
import com.soecode.wxtools.api.WxConfig;
import com.soecode.wxtools.bean.WxJsapiConfig;
import com.soecode.wxtools.bean.result.WxOAuth2AccessTokenResult;
import com.soecode.wxtools.exception.WxErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
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
    Logger logger = LoggerFactory.getLogger(ThymeleafController.class);
    private MyService iService = new MyWxServiceImpl();
    @Autowired
    private UserService userService;
    @Resource
    private OtherWxMapper otherWxMapper;
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(@RequestParam(name = "code", required = false) String code,
                        @RequestParam(name = "state", defaultValue = "0") String state, Model model, HttpServletResponse response)  {

        logger.info(state+code);
//        Map<String, Object> parameter =getParameter(state);
//        Object wxId = parameter.get("wxId");
//        Object openId = parameter.get("openId");
        String[] split = state.split(",");


        //todo 增加一个state后的参数wxId,otherId
        model.addAttribute("state", split[0]);
        WxOAuth2AccessTokenResult oAuth2AccessTokenResult = null;
        try {
            oAuth2AccessTokenResult = iService.oauth2ToGetAccessToken(code);
        } catch (WxErrorException e) {
            logger.error("获取用户openId报错：{},{}",e.getError().getErrcode(),e.getError().getErrmsg());
        }
        //获取微信登入的openid,昵称等等
//        WxUserList.WxUser.WxUserGet wxUser = new WxUserList.WxUser.WxUserGet();
//        wxUser.setOpenid(oAuth2AccessTokenResult.getOpenid());
//        wxUser.setLang("zh_CN");
//        WxUserList.WxUser wxUser1 = iService.oauth2ToGetUserInfo(oAuth2AccessTokenResult.getAccess_token(), wxUser);
//        model.addAttribute("openId", oAuth2AccessTokenResult.getOpenid());
        logger.info("openId{}",oAuth2AccessTokenResult.getOpenid());
        User user = userService.getUser(oAuth2AccessTokenResult.getOpenid());
        response.addCookie(new Cookie("openId",oAuth2AccessTokenResult.getOpenid()));

        if (user != null) {
            response.addCookie(new Cookie("phone",user.getPhone()));
            response.addCookie(new Cookie("myName",user.getRealname()));
            response.addCookie(new Cookie("userId",user.getId().toString()));
            response.addCookie(new Cookie("isAuth",user.getIsauth()));
            if (split.length>1) {
                String wxId = split[1];
                String otherOpenId = split[2];
                userService.otherWxVerify(wxId,user.getId(),otherOpenId);
            }
        } else {
            response.addCookie(new Cookie("phone",""));
            response.addCookie(new Cookie("myName",""));
            response.addCookie(new Cookie("userId","0"));
            response.addCookie(new Cookie("isAuth","F"));
        }
        //跳转地址
        setAuth( split[0],model);
        return split[0];
    }

    @AuthCheckAnnotation(checkLogin = false, checkVerify = false)
    @RequestMapping(value = "/visit", method = RequestMethod.GET)
    public String visit() {
        return "visit";
    }

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(@RequestParam(name = "wxId", required = false) String wxId,
                        @RequestParam(name = "state", defaultValue = "0") String state,
                        @RequestParam(name = "code", required = false) String code,Model model) {
            logger.info(wxId);
            logger.info(state);
            logger.info(code);
        //获取其他公众号的openid
        try {
            otherWx wx = otherWxMapper.findByWx(wxId);
            WxOAuth2AccessTokenResult result = iService.otherAuth2ToGetAccessToken(wx.getAppid(),wx.getSecret(),code);
            logger.info("otherOpenId,{}",result.getOpenid());
            model.addAttribute("otherOpenId",result.getOpenid());

        } catch (WxErrorException e) {
            e.printStackTrace();
        }
        model.addAttribute("wxId",wxId);
        return "index1";
    }
    @RequestMapping(value = "/index1", method = RequestMethod.GET)
    public String index1() {

        return "index1";
    }

    @AuthCheckAnnotation(checkLogin = false, checkVerify = false)
    @RequestMapping(value = "/clear", method = RequestMethod.GET)
    public String clear() {
        return "clear";
    }
    @RequestMapping(value = "/error", method = RequestMethod.GET)
    public String error() {
        return "error";
    }
    @RequestMapping(value = "/clear1", method = RequestMethod.GET)
    public String clear1() {
        return "clear1";
    }
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String test() {
        return "redirect:https://www.baidu.com";
    }

    @AuthCheckAnnotation(checkLogin = false, checkVerify = false)
    @RequestMapping(value = "/invite", method = RequestMethod.GET)
    public String invite() {
        return "invite";
    }

    @AuthCheckAnnotation(checkLogin = false, checkVerify = false)
    @RequestMapping(value = "/bindphone", method = RequestMethod.GET)
    public String bindphone() {
        return "bindphone";
    }

    @AuthCheckAnnotation(checkLogin = false, checkVerify = false)
    @RequestMapping(value = "/firstrecord", method = RequestMethod.GET)
    public String firstrecord() {
        return "firstrecord";
    }

    @AuthCheckAnnotation(checkLogin = false, checkVerify = false)
    @RequestMapping(value = "/secondrecord", method = RequestMethod.GET)
    public String secondrecord() {
        return "secondrecord";
    }

    @AuthCheckAnnotation(checkLogin = false, checkVerify = false)
    @RequestMapping(value = "/recorddetail", method = RequestMethod.GET)
    public String recorddetail() {
        return "recorddetail";
    }

    //        @AuthCheckAnnotation(checkLogin = false,checkVerify = false)
//        @RequestMapping({"static/MP_verify_I4XWI1ZSKeFojwT6.txt"})
//        private String returnConfigFile(HttpServletResponse response) {
//           return "I4XWI1ZSKeFojwT6";
//        }
    @AuthCheckAnnotation(checkLogin = false, checkVerify = false)
    @RequestMapping(value = "/reply", method = RequestMethod.GET)
    public String reply() {
        return "reply";
    }

    @AuthCheckAnnotation(checkLogin = false, checkVerify = false)
    @RequestMapping(value = "/auth", method = RequestMethod.GET)
    public String auth(Model model) {
        setAuth("auth",model);
        return "auth";
    }

    @AuthCheckAnnotation(checkLogin = false, checkVerify = false)
    @RequestMapping(value = "/auth2", method = RequestMethod.GET)
    public String auth2(Model model) {
        setAuth("auth2",model);

        return "auth2";
    }
    @AuthCheckAnnotation(checkLogin = false, checkVerify = false)
    @RequestMapping(value = "/auth3", method = RequestMethod.GET)
    public String auth3(Model model) {
        logger.info("auth3");
        setAuth( "auth3",model);
        return "auth3";
    }
    @AuthCheckAnnotation(checkLogin = false, checkVerify = false)
    @RequestMapping(value = "/verify", method = RequestMethod.GET)
    public String verify(Model model) {

        logger.info("verify");
        setAuth( "verify",model);
        return "verify";
    }
    @AuthCheckAnnotation(checkLogin = false, checkVerify = false)
    @RequestMapping(value = "/verifytest", method = RequestMethod.GET)
    public String verifytest(Model model) {

        logger.info("verify");
        setAuth( "verifytest",model);
        return "verifytest";
    }
    public void setAuth(String path,Model model){
        List<String> jsApiList = new ArrayList<>();

        //需要用到哪些JS SDK API 就设置哪些
        //拍照或从手机相册中选图接口
        jsApiList.add("chooseImage");
        //获取“分享到QQ空间”按钮点击状态及自定义分享内容接口
        jsApiList.add("onMenuShareQZone");
        //预览图片接口
        jsApiList.add("previewImage");
        //上传图片接口
        jsApiList.add("uploadImage");
        //下载图片接口
        jsApiList.add("downloadImage");
        logger.info("已进入auth界面");
        try {
            //把config返回到前端进行js调用即可。
            WxJsapiConfig config = iService.createJsapiConfig(MenuKey.URL + path, jsApiList);
            config.setAppid(WxConfig.getInstance().getAppId());
            System.out.println(config.getNoncestr());
            model.addAttribute("config", config);
            logger.info("进入设置权限");
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
    }
}
