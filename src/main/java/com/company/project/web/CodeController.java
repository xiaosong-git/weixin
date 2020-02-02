package com.company.project.web;

import com.company.project.annotation.AuthCheckAnnotation;
import com.company.project.core.Result;
import com.company.project.service.CodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author linyb
 * @Date 2017/4/10 19:31
 */
@Controller
@RequestMapping("/code")
public class CodeController  {

    @Autowired
    private CodeService codeService;

    /**
     * 发送验证码
     * @Author linyb
     * @Date 2017/4/3 16:05
     */
    @AuthCheckAnnotation
    @RequestMapping("/sendCode/{phone}/{type}")
    @ResponseBody
    public Result sendCode(@PathVariable String phone, @PathVariable Integer type) {
        return codeService.sendMsg(phone,type,null,null,null,null);
    }
    @AuthCheckAnnotation
    @RequestMapping("/verifyCode")
    @ResponseBody
    public Boolean verifyCode(@RequestParam String  phone, @RequestParam String code) {

        return codeService.verifyCode(phone,code);
    }


}
