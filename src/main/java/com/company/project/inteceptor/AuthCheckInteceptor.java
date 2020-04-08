package com.company.project.inteceptor;

import com.alibaba.fastjson.JSONObject;
import com.company.project.annotation.AuthCheckAnnotation;
import com.company.project.core.ResultGenerator;
import com.company.project.model.User;
import com.company.project.service.ParamService;
import com.company.project.service.UserService;
import com.company.project.util.RedisUtil;
import com.company.project.util.ResponseUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author linyb
 * @Date 2017/4/3 22:10
 */
@Configurable
public class AuthCheckInteceptor extends HandlerInterceptorAdapter {
    @Autowired
    private ParamService paramService;
    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        HandlerMethod methodHandler=(HandlerMethod) handler;
        AuthCheckAnnotation auth = methodHandler.getMethodAnnotation(AuthCheckAnnotation.class);

        if (auth==null){
           return super.preHandle(request, response, handler);
        }
        if(auth.checkLogin()){  //需要登录验证
            /**
             * 登录验证visitRequest
             */
            String token = request.getParameter("token");
            if(StringUtils.isBlank(token)){
                ResponseUtil.response(response,JSONObject.toJSONString(ResultGenerator.genFailResult("登入已过期，请重新登录。","overTime")));
                return false;
            }
            String userIdStr = request.getParameter("userId");
            if(StringUtils.isNotBlank(token) && StringUtils.isNotBlank(userIdStr)){
                Integer apiNewAuthCheckRedisDbIndex = Integer.valueOf(paramService.findValueByName("apiNewAuthCheckRedisDbIndex"));//存储在缓存中的位置
//                String userToken = null;
//                String tokenKey = userIdStr+"_token";
                //redis修改
//                userToken = RedisUtil.getStrVal(tokenKey, apiNewAuthCheckRedisDbIndex);
                User user = null;
//                if(StringUtils.isBlank(userToken)){
                    //缓存中不存在Token，就从数据库中查询
                     user = userService.findById(Long.valueOf(userIdStr));
                     if (user==null){
                         return false;
                     }
//                    userToken = user.getToken()+"";
//                }
                //token相同
//                if(token.equals(userToken)){
                    /**
                     * 实人验证
                     */
                    if(auth.checkVerify()){
                        String verifyKey = userIdStr+"_isAuth";
                        String isAuth = RedisUtil.getStrVal(verifyKey, apiNewAuthCheckRedisDbIndex);
                        if(StringUtils.isBlank(isAuth)){
                            //缓存中不存在，就从数据库中查询
                            //redis修改
                            isAuth = user.getIsauth()==null?"F":user.getIsauth();
                        }
                        if( !"T".equalsIgnoreCase(isAuth)){
                            ResponseUtil.response(response,JSONObject.toJSONString(ResultGenerator.genFailResult("您还未进行实人验证","verifyFail")));
                            return false;
                        }
                    }
//                    /**
//                     * 验证请求合法性
//                     */
//                    if(auth.checkRequestLegal()){
//                       ;
//                        if(TokenUtil.checkRequestLegal(userIdStr, factor, token, threshold)){
//                            return true;
//                        }else{
//                            ResponseUtil.response(response,JSONObject.toJSONString(ResultGenerator.genFailResult("请求非法!","userFail")));
//                            return false;
//                        }
//
//                    }
                    return true;
//                }else{
//                    ResponseUtil.response(response,JSONObject.toJSONString(ResultGenerator.genFailResult("您的帐号在另一台设备登录，请重新登录。","tokenFail")));
//                    return false;
//                }
            }
            ResponseUtil.response(response,JSONObject.toJSONString(ResultGenerator.genFailResult("用户信息验证失败，请重新登录","userFail")));
            return false;
        }
        return super.preHandle(request, response, handler);
    }
}
