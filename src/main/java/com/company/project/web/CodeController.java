package com.company.project.web;

import com.company.project.annotation.AuthCheckAnnotation;
import com.company.project.core.Result;
import com.company.project.service.CodeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @Author linyb
 * @Date 2017/4/10 19:31
 */
@Controller
@RequestMapping("/code")
@Api(tags = "短信管理", value = "短信管理")
public class CodeController  {

    @Autowired
    private CodeService codeService;

    /**
     * 发送验证码
     * @Author linyb
     * @Date 2017/4/3 16:05
     */
    @AuthCheckAnnotation
    @PostMapping("/sendCode/{phone}/{type}")
    @ResponseBody
    @ApiOperation(value = "发送验证码",notes = "发送验证码给用户")
    public Result sendCode(@PathVariable String phone, @PathVariable Integer type) {
        return codeService.sendMsg(phone,type,null,null,null,null);
    }
    @AuthCheckAnnotation
    @PostMapping("/verifyCode")
    @ResponseBody
    @ApiOperation(value = "验证验证码")
    public Boolean verifyCode(@RequestParam String  phone, @RequestParam String code) {

        return codeService.verifyCode(phone,code);
    }


}
