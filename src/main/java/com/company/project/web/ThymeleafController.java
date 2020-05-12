package com.company.project.web;


import com.company.project.annotation.AuthCheckAnnotation;
import com.company.project.dao.OtherWxMapper;
import com.company.project.model.User;
import com.company.project.model.otherWx;
import com.company.project.service.UserService;
import com.company.project.weixin.MenuKey;
import com.company.project.weixin.MyService;
import com.company.project.weixin.MyWxServiceImpl;
import com.company.project.weixin.WxController;
import com.soecode.wxtools.api.WxConfig;
import com.soecode.wxtools.bean.WxJsapiConfig;
import com.soecode.wxtools.bean.WxUserList;
import com.soecode.wxtools.bean.result.WxOAuth2AccessTokenResult;
import com.soecode.wxtools.exception.WxErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
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
                        @RequestParam(name = "state", defaultValue = "0") String state, Model model) throws WxErrorException {

        logger.info(state);
//        Map<String, Object> parameter =getParameter(state);
//        Object wxId = parameter.get("wxId");
//        Object openId = parameter.get("openId");
        String[] split = state.split(",");


        //todo 增加一个state后的参数wxId,otherId
        model.addAttribute("state", split[0]);
        WxOAuth2AccessTokenResult oAuth2AccessTokenResult = iService.oauth2ToGetAccessToken(code);
        //获取微信登入的openid,昵称等等
//        WxUserList.WxUser.WxUserGet wxUser = new WxUserList.WxUser.WxUserGet();
//        wxUser.setOpenid(oAuth2AccessTokenResult.getOpenid());
//        wxUser.setLang("zh_CN");
//        WxUserList.WxUser wxUser1 = iService.oauth2ToGetUserInfo(oAuth2AccessTokenResult.getAccess_token(), wxUser);
        model.addAttribute("openId", oAuth2AccessTokenResult.getOpenid());
        User user = userService.getUser(oAuth2AccessTokenResult.getOpenid());
        if (user != null) {
            model.addAttribute("userId", user.getId());
            model.addAttribute("isAuth", user.getIsauth());
            model.addAttribute("myName", user.getRealname());
            model.addAttribute("phone", user.getPhone());
            if (split.length>1) {
                String wxId = split[1];
                String otherOpenId = split[2];
                userService.otherWxVerify(wxId,user.getId(),otherOpenId);
            }
        } else {
            model.addAttribute("userId", 0);
            model.addAttribute("isAuth", "F");
            model.addAttribute("myName", "");
            model.addAttribute("phone", "");
        }
        return "login";
    }

//    public static void main(String[] args) {
//        Map<String, Object> parameter = getParameter("?wxId=212&openId=33");
//        Object wxId = parameter.get("wxId");
//        Object openId = parameter.get("openId");
//        System.out.println(wxId);
//        System.out.println(openId);
//
//        String s="visit";
//        String[] split = s.split("=");
//        System.out.println(split[0]);
//    }
//
//    public static Map<String, Object> getParameter(String url) {
//        Map<String, Object> map = new HashMap<String, Object>();
//        try {
//            final String charset = "utf-8";
//            url = URLDecoder.decode(url, charset);
//            if (url.indexOf('?') != -1) {
//                final String contents = url.substring(url.indexOf('?') + 1);
//                String[] keyValues = contents.split(",");
//                for (int i = 0; i < keyValues.length; i++) {
//                    String key = keyValues[i].substring(0, keyValues[i].indexOf("="));
//                    String value = keyValues[i].substring(keyValues[i].indexOf("=") + 1);
//                    map.put(key, value);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return map;
//    }

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
    public void setAuth(String path,Model model){
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
            WxJsapiConfig config = iService.createJsapiConfig(MenuKey.URL + path, jsApiList);
            config.setAppid(WxConfig.getInstance().getAppId());
            System.out.println(config.getNoncestr());
//                 System.out.println(config.toJson());
            model.addAttribute("config", config);
            logger.info("进入设置权限");
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
    }
}
