package com.company.project.annotation;

import java.lang.annotation.*;

/**
 * @Author linyb
 * @Date 2017/4/3 22:08
 */
@Documented
@Target(ElementType.METHOD)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthCheckAnnotation {
    boolean checkLogin()  default false;    //是否验证登录
    boolean checkVerify() default false;    //是否验证实人
//    boolean checkRequestLegal() default false; //检查请求合法性
//    boolean checkOtherLegal() default false; //检查请求合法性
}
