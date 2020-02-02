package com.company.project.web;

import com.company.project.annotation.AuthCheckAnnotation;
import com.company.project.core.Result;
import com.company.project.core.ResultGenerator;
import com.company.project.model.Company;
import com.company.project.model.User;
import com.company.project.service.CompanyService;
import com.company.project.service.CompanyUserService;
import com.company.project.service.UserService;
import com.soecode.wxtools.api.IService;
import com.soecode.wxtools.api.WxService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

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

    private IService iService = new WxService();

//    @PostMapping("/add")
//    public Result add(User user) {
//        userService.save(user);
//        return ResultGenerator.genSuccessResult();
//    }
//
//    @PostMapping("/delete")
//    public Result delete(@RequestParam Integer id) {
//        userService.deleteById(id);
//        return ResultGenerator.genSuccessResult();
//    }
//
//    @PostMapping("/update")
//    public Result update(User user) {
//        userService.update(user);
//        return ResultGenerator.genSuccessResult();
//    }
//
//    @PostMapping("/detail")
//    public Result detail(@RequestParam Integer id) {
//        User user = userService.findById(id);
//        return ResultGenerator.genSuccessResult(user);
//    }

//    @PostMapping("/list")
//    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
//        PageHelper.startPage(page, size);
//        List<User> list = userService.findAll();
//        PageInfo pageInfo = new PageInfo(list);
//        return ResultGenerator.genSuccessResult(pageInfo);
//    }
    @AuthCheckAnnotation(checkLogin = false,checkVerify = false)
    @PostMapping("/namePhone")
    public Result findByNamePhone(@RequestParam(defaultValue = "0") String realName, @RequestParam(defaultValue = "0") String phone) {

        List<User> userList = userService.findByNamePhone( realName, phone);
        if (userList==null||userList.isEmpty()) {
            return ResultGenerator.genFailResult("用户不存在，请填写正确姓名与手机号！");
        }
        Long userId = userList.get(0).getId();
        List<Company> companyUserList = companyService.findByUserId(userId);
        if (companyUserList==null||companyUserList.isEmpty()){
            companyUserList= companyService.findNotCompanyUser(userId);
        }
        if (companyUserList==null||companyUserList.isEmpty()){
            return ResultGenerator.genFailResult("该用户没有绑定公司");
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
                        @RequestParam String openId ){
        try {
                return userService.loginByVerifyCode(phone,code,openId);
        }catch (Exception e){
            e.printStackTrace();
        }
        return ResultGenerator.genFailResult("系统异常");
    }
    /**
     * 实名认证
     * @param userId 手机号
     * @param idNO 验证码
     * @param realName 真实姓名
     * @param idHandleImgUrl 图片地址
     * @param addr 住址
     * @return
     */
    @AuthCheckAnnotation(checkLogin = true,checkVerify = false)
    @RequestMapping("/verify")
    @ResponseBody
    public Result verify(@RequestParam long userId,@RequestParam String idNO,
                         @RequestParam String realName,@RequestParam String idHandleImgUrl,@RequestParam(defaultValue = "无") String addr){
        try {
                return userService.verify(userId, idNO, realName, idHandleImgUrl, addr);
        }catch (Exception e){
            e.printStackTrace();
        }
        return ResultGenerator.genFailResult("系统异常");
    }
    @AuthCheckAnnotation(checkLogin = true,checkVerify = false)
    @RequestMapping("/uploadVerify")
    @ResponseBody
    public Result uploadPhoto(@RequestParam String userId, @RequestParam() String  mediaId,@RequestParam() String  type)   {

        try {
            System.out.println(userId);
            System.out.println(mediaId);
            System.out.println(type);
            return userService.uploadPhoto(userId, mediaId, type);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResultGenerator.genFailResult("系统异常");
    }


}
