package com.company.project.web;

import com.company.project.annotation.AuthCheckAnnotation;
import com.company.project.core.Result;
import com.company.project.core.ResultGenerator;
import com.company.project.model.Company;
import com.company.project.model.User;
import com.company.project.service.CompanyService;
import com.company.project.service.CompanyUserService;
import com.company.project.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* Created by CodeGenerator on 2019/10/09.
*/
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;
    @Resource
    private CompanyUserService companyUserService;
    @Resource
    private CompanyService companyService;

    Logger logger = LoggerFactory.getLogger(UserController.class);
    @AuthCheckAnnotation(checkLogin = false,checkVerify = false)
    @PostMapping("/namePhone")
    public Result findByNamePhone(@RequestParam(defaultValue = "0") String realName, @RequestParam(defaultValue = "0") String phone) {

        List<User> userList = userService.findByNamePhone( realName, phone);
        if (userList==null||userList.isEmpty()) {
            return ResultGenerator.genFailResult("用户不存在，请填写正确姓名与手机号！");
        }
        Long userId = userList.get(0).getId();
        List<Company> companyUserList = companyService.findByUserId(userId);
        /*if (companyUserList==null||companyUserList.isEmpty()){
            companyUserList= companyService.findNotCompanyUser(userId);
        }*/
        if (companyUserList==null||companyUserList.isEmpty()){
            return ResultGenerator.genFailResult("该用户没有绑定公司");
        }
        return ResultGenerator.genSuccessResult(userList);
    }

    @AuthCheckAnnotation(checkLogin = false,checkVerify = false)
    @PostMapping("/namePhone2")
    public Result findByNamePhone2(@RequestParam(defaultValue = "0") String realName, @RequestParam(defaultValue = "0") String phone) {

        List<User> userList = userService.findByNamePhone( realName, phone);
        if (userList==null||userList.isEmpty()) {
            return ResultGenerator.genFailResult("用户不存在，请填写正确姓名与手机号！");
        }
        return ResultGenerator.genSuccessResult(userList);
    }
    @AuthCheckAnnotation(checkLogin = false,checkVerify = false)
    @PostMapping("/nameCompany")
    public Result nameCompany(@RequestParam() Long companyId, @RequestParam(defaultValue = "0") String name) {

        User userList = userService.nameCompany(companyId,name);
        if (userList==null) {
            return ResultGenerator.genFailResult("该公司用户不存在，请填写正确姓名");
        }
        return ResultGenerator.genSuccessResult(userList);
    }
    /**
     * 账号，密码登录
     * @param phone 手机号
     * @param code 验证码
     * @return
     */
    @AuthCheckAnnotation(checkLogin = false,checkVerify = false)
    @RequestMapping("/login")
    @ResponseBody
    public Result login(@RequestParam(defaultValue = "0") String phone, @RequestParam(defaultValue = "0") String code,
                        @RequestParam String openId,@RequestParam(defaultValue = "0") String password ){
        try {
            if ("0".equals(code)){
                return userService.login(phone, password, openId);
            }else {
                return userService.loginByVerifyCode(phone, code, openId);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return ResultGenerator.genFailResult("系统错误，请重试","");
    }

    /**
     * 实人认证
     * @param openId 微信对应公众号openId
     * @param idNO 验证码
     * @param realName 真实姓名
     * @param idHandleImgUrl 图片地址
     * @param addr 住址
     * @return
     */
    @AuthCheckAnnotation(checkLogin = false,checkVerify = false)
    @RequestMapping("/verify")
    @ResponseBody
    public Result verify(@RequestParam String openId,@RequestParam String idNO,
                         @RequestParam String realName,@RequestParam String idHandleImgUrl,
                         @RequestParam(defaultValue = "无") String addr,
                         @RequestParam String phone,
                         @RequestParam String code,
                         @RequestParam String wxId,
                         @RequestParam String otherOpenId,
    @RequestParam String localImgUrl){
        try {
                return userService.verify(openId, idNO, realName, idHandleImgUrl, addr,localImgUrl, phone, code,wxId,otherOpenId);
        }catch (Exception e){
            e.printStackTrace();
        }
        return ResultGenerator.genFailResult("系统错误，请重试","");
    }

    @AuthCheckAnnotation(checkLogin = false,checkVerify = false)
    @RequestMapping("/halfVerify")
    @ResponseBody
    public Result halfVerify(@RequestParam String openId,
                         @RequestParam String realName,
                         @RequestParam String idHandleImgUrl,
                         @RequestParam String phone,
                         @RequestParam String code,
                         @RequestParam String wxId,
                         @RequestParam String otherOpenId
                         ){
        try {
            return userService.halfVerify(openId, realName, idHandleImgUrl, phone, code,wxId,otherOpenId);
        }catch (Exception e){
            e.printStackTrace();
        }
        return ResultGenerator.genFailResult("系统错误，请重试","");
    }
    /**
     * 实人认证

     * @return
     */
    @AuthCheckAnnotation(checkLogin = false,checkVerify = false)
    @RequestMapping("/authAfter")
    @ResponseBody
    public Result authAfter(@RequestParam Long userId,@RequestParam String idNO,@RequestParam String realName){
        try {
            return userService.authAfter(userId,idNO,realName);
        }catch (Exception e){
            e.printStackTrace();
        }
        return ResultGenerator.genFailResult("系统错误，请重试","");
    }
    /**
     * 实人认证图片上传
     * @param openId 用户id
     * @param mediaId 微信临时图片
     * @param type 状态
     * @return
     */
    @AuthCheckAnnotation(checkLogin = false,checkVerify = false)
    @RequestMapping("/uploadVerify")
    @ResponseBody
    public Result uploadPhoto(@RequestParam String openId, @RequestParam() String  mediaId,@RequestParam() String  type)   {

        try {
            System.out.println(mediaId);
            System.out.println(type);
            return userService.uploadPhoto(openId, mediaId, type);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResultGenerator.genFailResult("系统错误，请重试","");
    }

    /**
     * 查看常用联系人
     * @param userId 登入用户id
     * @return 根据访客时间倒序输出带图片的用户
     */
    @AuthCheckAnnotation(checkLogin = false,checkVerify = false)
    @RequestMapping("/frequentContacts")
    @ResponseBody
    public Result frequentContacts(@RequestParam String userId)   {

        try {
            return userService.frequentContacts(userId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResultGenerator.genFailResult("系统错误，请重试","");
    }
    /**
     * 通过手机号绑定微信账号
     */
    @RequestMapping("/bindWxPhone")
    @ResponseBody
    public Result bindWxPhone(Long userId,String phone,String openId,String code){

        try {
            return userService.bindWxPhone(userId,phone,openId,code);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultGenerator.genFailResult("系统错误，请重试","");
    }
    /**
     * 通过手机号绑定微信账号
     */
    @RequestMapping("/verifyTest")
    @ResponseBody
    public Result verifyTest(Long userId){

        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("isAuth", "T");
        resultMap.put("userId", userId);
        return ResultGenerator.genSuccessResult(resultMap);
    }


    @RequestMapping("/getUserById")
    @ResponseBody
    public Result getUserById(Long userId){

        User u = userService.findById(userId);
        if(null == u){
            return ResultGenerator.genFailResult("数据错误");
        }
        return ResultGenerator.genSuccessResult(u);
    }
    @PostMapping("/bindOther")
    public Result bindOther(@RequestParam (defaultValue = "")String wxId,@RequestParam(defaultValue = "") String userId,@RequestParam (defaultValue = "") String otherOpenId ){
        if("".equals(wxId)||"".equals(otherOpenId)||"".equals(userId)){
            return  ResultGenerator.genSuccessResult(false);
        }
        boolean bind = userService.otherWxVerify(wxId, Long.valueOf(userId), otherOpenId);
        logger.info("bind{}", bind);
        return  ResultGenerator.genSuccessResult(bind);
    }

}
