package com.company.project.service;
import com.company.project.core.Result;
import com.company.project.model.User;
import com.company.project.core.Service;
import com.soecode.wxtools.exception.WxErrorException;

import java.util.List;
import java.util.Map;


/**
 * Created by CodeGenerator on 2019/10/09.
 */
public interface UserService extends Service<User> {

    List<User> findByNamePhone(String realName, String phone);

    Result login(Map<String, Object> paramMap) throws Exception;

    void updateRedisTokenAndAuth(String userId, String token, String isAuth) throws Exception;

    void updateRedisAuth(String userId, String isAuth) throws Exception;

    void updateRedisOpenId(String userId, String openId) throws Exception;

    Result loginByVerifyCode(String phone, String code, String openId) throws Exception;

    Result verify(long userId, String idNO, String name, String idHandleImgUrl, String addr);

    boolean isVerify(long userId);

    boolean isExistIdNo(long userId, String idNo) throws Exception;

    Result uploadPhoto(String userId, String mediaId, String type) throws WxErrorException, Exception;


    Result frequentContacts(String userId);
}
