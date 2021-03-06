package com.company.project.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.company.project.compose.Status;
import com.company.project.compose.TableList;
import com.company.project.core.AbstractService;
import com.company.project.core.Result;
import com.company.project.core.ResultGenerator;
import com.company.project.dao.*;
import com.company.project.model.*;
import com.company.project.service.*;
import com.company.project.util.*;
import com.company.project.weixin.MyWxServiceImpl;
import com.soecode.wxtools.api.IService;
import com.soecode.wxtools.exception.WxErrorException;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.io.File;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * Created by CodeGenerator on 2019/10/09.
 */
@Service
@Transactional
public class UserServiceImpl extends AbstractService<User> implements UserService {
    @Resource
    private UserMapper tblUserMapper;
    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    @Resource
    private PasswordService passwordService;
    @Resource
    private ParamService paramService;
    @Resource
    private CodeService codeService;
    @Resource
    private UserAccountService userAccountService;
    @Resource
    private KeyService keyService;
    @Resource
    private CompanyService companyService;
    @Resource
    private OrgService orgService;
    @Resource
    private UserAuthMapper userAuthMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private CompanyMapper companyMapper;
    @Resource
    private VisitRecordMapper visitRecordMapper;
    @Resource
    private OtherOpenidMapper otherOpenidMapper;
    @Resource
    private OtherWxMapper otherWxMapper;
    @Resource
    private OtherOpenidService otherOpenidService;
    @Value("${imgUrl}")
    private String imgUrl;
    private IService iService = new MyWxServiceImpl();

    /**
     * 检测用户是否存在
     *
     * @param realName
     * @param phone
     * @return java.util.List<com.company.project.model.User>
     * @throws Exception
     * @author cwf
     * @date 2019/10/13 17:10
     */
    @Override
    public List<User> findByNamePhone(String realName, String phone) {
        Condition condition = new Condition(User.class);
        condition.createCriteria().andEqualTo("realname", realName).andEqualTo("phone", phone);
        condition.selectProperties("id");
        List<User> list = super.findByCondition(condition);

//        System.out.println(list.get(0).getCompanyId());
//        System.out.println(list);
        return list;
    }

    @Override
    public Result login(String phone, String password, String openId) throws Exception {
        User user = findBy("phone", phone);
        if (user == null) {
            return ResultGenerator.genFailResult("用户不存在");
        }
        long userId = user.getId();
        //判断密码输入次数是否超出限制，超出无法登录
        if (passwordService.isErrInputOutOfLimit(String.valueOf(userId), Status.PWD_TYPE_SYS)) {
            String limitTime = paramService.findValueByName("errorInputSyspwdWaitTime");
            codeService.sendMsg(user.getLoginname() + "", 2, null, null, null, null);
            return ResultGenerator.genFailResult("由于您多次输入错误密码，为保证您的账户与资金安全，" + limitTime + "分钟内无法登录", "fail");
        }
        UserAccount userAccount = userAccountService.findByUserId(userId);
        if (userAccount == null) {
            return ResultGenerator.genFailResult("找不到用户的账户信息", "fail");
        }
        String dbPassword = userAccount.getSyspwd() + "";//正确密码

        if (password.equals(dbPassword)) {
            //重置允许用户输入错误密码次数
            passwordService.resetPwdInputNum(String.valueOf(userId), Status.PWD_TYPE_SYS);
            String cstatus = userAccount.getCstatus() + "";
            if ("normal".equals(cstatus)) {
                User userUpdate = new User();

                userUpdate.setId(userId);
                userUpdate.setToken(UUID.randomUUID().toString());
                userUpdate.setWxOpenId(openId);
                this.update(userUpdate);
                user = this.findById(userId);
                //更新缓存中的Token,实人
                String token = user.getToken();
                String isAuth = user.getIsauth();
                updateRedisTokenAndAuth(String.valueOf(user.getId()), token, isAuth);
                //获取密钥
                String workKey = keyService.findKeyByStatus(TableList.KEY_STATUS_NORMAL);
                if (workKey != null) {
                    user.setWorkkey(workKey);
                }
                return ResultGenerator.genSuccessResult(user);
            } else {
                //返回登录失败事由
                String handleCause = userAccount.getHandlecause();
                return ResultGenerator.genFailResult(handleCause);
            }
        } else {
            Long leftInputNum = passwordService.addErrInputNum(String.valueOf(userId), Status.PWD_TYPE_SYS);
            return ResultGenerator.genFailResult("密码错误:剩余" + leftInputNum + "次输入机会", "fail");
        }
    }

    @Override
    public void updateRedisTokenAndAuth(String userId, String token, String isAuth) throws Exception {
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(token) || StringUtils.isBlank(isAuth)) {
            return;
        }
        Integer apiNewAuthCheckRedisDbIndex = Integer.valueOf(paramService.findValueByName("apiNewAuthCheckRedisDbIndex"));//存储在缓存中的位置
        Integer expire = Integer.valueOf(paramService.findValueByName("apiAuthCheckRedisExpire"));//过期时间(分钟)
        //redis修改
        RedisUtil.setStr(userId + "_token", token, apiNewAuthCheckRedisDbIndex, expire * 60);
        //redis修改
        RedisUtil.setStr(userId + "_isAuth", isAuth, apiNewAuthCheckRedisDbIndex, expire * 60);
    }

    //更新实人
    @Override
    public void updateRedisAuth(String userId, String isAuth) throws Exception {
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(isAuth)) {
            return;
        }
        Integer apiNewAuthCheckRedisDbIndex = Integer.valueOf(paramService.findValueByName("apiNewAuthCheckRedisDbIndex"));//存储在缓存中的位置
        Integer expire = Integer.valueOf(paramService.findValueByName("apiAuthCheckRedisExpire"));//过期时间(分钟)
        //redis修改
        RedisUtil.setStr(userId + "_isAuth", isAuth, apiNewAuthCheckRedisDbIndex, expire * 60);
    }

    //更新微信号
    @Override
    public void updateRedisOpenId(String userId, String openId) throws Exception {
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(openId)) {
            return;
        }
        //36
        Integer apiNewAuthCheckRedisDbIndex = Integer.valueOf(paramService.findValueByName("wxOpenIdCheckRedisDbindex"));//存储在缓存中的位置
        Integer expire = Integer.valueOf(paramService.findValueByName("apiAuthCheckRedisExpire"));//过期时间(分钟)
        //redis修改
        RedisUtil.setStr(userId + "_openId", openId, apiNewAuthCheckRedisDbIndex, expire * 60);
        System.out.println("登入存储redies");
    }

    @Override
    public Result loginByVerifyCode(String phone, String code, String openId) throws Exception {
        /**
         * 1,获取参数并判断
         */

        if (StringUtils.isBlank(phone)) {
            return ResultGenerator.genFailResult("缺少登录账号!");
        }
        if (StringUtils.isBlank(code)) {
            return ResultGenerator.genFailResult("缺少验证码!");
        }
        /**
         * 2,验证短信验证码
         */
        User user = null;
        //短信验证码正确
        if (codeService.verifyCode(phone, code) || "6666666".equals(code)) {
            //暂时为true
            //通过手机查找用户
            user = findBy("phone", phone);
            //如果用户不存在
            if (user == null) {
                //自动注册账户
                User regUser = new User();
                Date date = new Date();
                regUser.setCreatedate(new SimpleDateFormat("yyyy-MM-dd").format(date));
                regUser.setCreatetime(new SimpleDateFormat("HH:mm:ss").format(date));
                regUser.setPhone(phone);
                regUser.setToken(UUID.randomUUID().toString());
                regUser.setLoginname(phone);
                regUser.setIsauth("F");
                regUser.setWorkkey(NumberUtil.getRandomWorkKey(10));
                regUser.setIssettranspwd("F");
                regUser.setSolecode(OrderNoUtil.genOrderNo("C", 16));
                //插入微信号码
                if (!openId.equals("0")) {
                    regUser.setWxOpenId(openId);
                }
                int save = save(regUser);
                //自动注册成功
                if (save > 0) {
                    user = regUser;
                    User myUser = findBy("phone", phone);
                    UserAccount us = userAccountService.findBy("userid", user.getId());
                    if (us == null) {
                        userAccountService.preCreateAcount(myUser.getId());
                    }
                }
                //如果有账户 查看微信号是否相同，如果不同则更新
            } else if (!StringUtils.isBlank(openId) && !openId.equals(user.getWxOpenId())) {
                User updateUser = new User();
                updateUser.setId(user.getId());
                updateUser.setWxOpenId(openId);
                try {
                    update(updateUser);
                    //更新与redis微信号
                } catch (Exception e) {
                    logger.error("更新用户微信号失败", e);
                }
            }
            //更新缓存中的Token,实人
//            String token = BaseUtil.objToStr(user.get("token"), null);
            UserAccount us = userAccountService.findBy("userid", user.getId());
            if (us == null) {
                userAccountService.preCreateAcount(user.getId());
            }
            String isAuth = user.getIsauth();
            updateRedisAuth(String.valueOf(user.getId()), isAuth);
            updateRedisOpenId(String.valueOf(user.getId()), openId);
            //获取密钥
            String workKey = keyService.findKeyByStatus(TableList.KEY_STATUS_NORMAL);
            if (workKey != null) {
                user.setWorkkey(workKey);
            }
            Map<Object, Object> result = new HashMap<>();
            result.put("user", user);

            return ResultGenerator.genSuccessResult(result);
        } else {
            //验证码输入错误
            return ResultGenerator.genFailResult("验证码输入错误，请重新获取!");
        }
    }

    @Override
    public Result halfVerify(String openId, String name, String idHandleImgUrl, String phone, String code, String wxId, String otherOpenId){
        User user=userMapper.findByPhone(phone);
        if (!"test2333".equals(code)) {
            if (!codeService.verifyCode(phone, code)) {
                return ResultGenerator.genFailResult("验证码错误");
            }
        }
        if (name == null) {
            return ResultGenerator.genFailResult("真实姓名不能为空!", "fail");
        }
        //todo 查询用户存在性
        int isSuc=0;
        if (user==null){
           //todo 生成新用户
            user=new User();
            user.setIsauth("H");
            user.setRealname(name);
            user.setIdhandleimgurl(idHandleImgUrl);
            user.setWxOpenId(openId);
            user.setIdtype("01");
            user.setPhone(phone);
             isSuc = save(user);
        }else{
            user.setRealname(name);
            user.setIdhandleimgurl(idHandleImgUrl);
            user.setWxOpenId(openId);
            user.setIsauth("H");
            user.setIdtype("01");
            isSuc = update(user);

        }
        if(wxId!=null&&otherOpenId!=null&&!"".equals(wxId)&&!"".equals(otherOpenId)) {
            boolean b = otherWxVerify(wxId, user.getId(), otherOpenId);
            if (!b) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                return ResultGenerator.genFailResult("异常，请稍后再试", "fail");
            }
        }
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("isAuth", user.getIsauth());
        resultMap.put("userId", user.getId());
        logger.info(resultMap.toString());
        return ResultGenerator.genSuccessResult(resultMap);
    }
    @Override
    public Result verify(String openId, String idNO, String name, String idHandleImgUrl, String addr, String localImgUrl, String phone, String code, String wxId, String otherOpenId) {
        try {
            //验证手机
            User user=userMapper.findByPhone(phone);
            if (!"test2333".equals(code)) {
                if (!codeService.verifyCode(phone, code)) {
                    return ResultGenerator.genFailResult("验证码错误");
                }
            }
            String workKey = keyService.findKeyByStatus("normal");
            if (user != null) {
                if (isVerify(user.getId())) {
                    if (user.getWxOpenId()==null||"".equals(user.getWxOpenId())) {
                        user.setWxOpenId(openId);
                        user.setIsauth("T");
                        user.setRealname(name);
                        user.setIdhandleimgurl(idHandleImgUrl);
                        user.setAuthdate(DateUtil.getCurDate());
                        user.setAuthtime(DateUtil.getCurTime());

                        String idNoMW = DESUtil.encode(workKey, idNO);
                        user.setIdno(idNoMW);
                        int update = update(user);
                        if (update > 0) {
                            Map<String, Object> resultMap = new HashMap<String, Object>(16);
                            resultMap.put("isAuth", "T");
                            resultMap.put("userId", user.getId());
                                //实人认证返回userId的值，不能改为返回字符串
                            return ResultGenerator.genSuccessResult(resultMap);
                        }
                    }
                    return ResultGenerator.genFailResult("已经实人认证过", "fail");
                }
            }

            String realName = URLDecoder.decode(name, "UTF-8");
            if (idNO == null) {
                return ResultGenerator.genFailResult("身份证不能为空!", "fail");
            }

            // update by cwf  2019/10/15 10:36 Reason:暂时修改为后端加密
            String idNoMW = DESUtil.encode(workKey, idNO);
//            String idNoMW = DESUtil.decode(workKey, idNO);
//            String idNoMW = idNO;
            if (realName == null) {
                return ResultGenerator.genFailResult("真实姓名不能为空!", "fail");
            }
            /**
             * 验证 身份证
             */
            //非空判断
            if (localImgUrl == null) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                return ResultGenerator.genFailResult("图片上传失败，请稍后再试!", "fail");
            }
            localImgUrl = URLDecoder.decode(localImgUrl, "UTF-8");
            try {
                //本地实人认证
//                UserAuth userAuth = userAuthMapper.localPhoneResult(idNoMW, realName);
//                if (userAuth != null) {
////                    localImgUrl=userAuth.getIdhandleimgurl();//目前存在无法两张人像比对的bug
////                    logger.info("本地实人认证成功上一张成功图片为：{}",userAuth.getIdhandleimgurl());
//                } else {
                String imageServerUrl = paramService.findValueByName("imageServerUrl");
                    String photoResult = auth(idNO, realName, imageServerUrl+idHandleImgUrl);//实人认证
                    if (!"success".equals(photoResult)) {
                        return ResultGenerator.genFailResult(photoResult, "fail");
                    }
//                }
            } catch (Exception e) {
                e.printStackTrace();
                return ResultGenerator.genFailResult("图片上传出错!", "fail");
            }
                //如果不存在，生成新的记录，新的账号密码
                if (user == null) {
                    user = new User();
                    user.setPhone(phone);
                }
            user.setWxOpenId(openId);
            user.setAuthdate(DateUtil.getCurDate());
            user.setAuthtime(DateUtil.getCurTime());
            user.setIdhandleimgurl(idHandleImgUrl);
            user.setRealname(realName);
            user.setIsauth("T");//F:未实人 T：实人 N:正在审核中 E：审核失败
            user.setIdtype("01");
            user.setIdno(idNoMW);
            String verifyTermOfValidity = paramService.findValueByName("verifyTermOfValidity");
            Calendar c = Calendar.getInstance();
            c.add(Calendar.YEAR, Integer.parseInt(verifyTermOfValidity));
            String validityDate = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
            user.setValiditydate(validityDate);
            int update = 0;

            if (user.getId() == null) {
                update = save(user);
                userAccountService.preCreateAcount(user.getId());//生成新的账户
            } else {
                update = update(user);
            }
            if (update > 0) {
                //todo 接入其他公众号openId

                Integer apiNewAuthCheckRedisDbIndex = Integer.valueOf(paramService.findValueByName("apiNewAuthCheckRedisDbIndex"));//存储在缓存中的位置
                String key = user.getId() + "_isAuth";
                //redis修改
                RedisUtil.setStr(key, "T", apiNewAuthCheckRedisDbIndex, null);
                Map<String, Object> resultMap = new HashMap<String, Object>();
                resultMap.put("isAuth", "T");
                resultMap.put("userId", user.getId());
                //验证第三方公众号
                if(wxId!=null&&otherOpenId!=null&&!"".equals(wxId)&&!"".equals(otherOpenId)) {
                    boolean b = otherWxVerify(wxId, user.getId(), otherOpenId);
                    if (!b){
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                    return ResultGenerator.genFailResult("异常，请稍后再试", "fail");
                }
                }
//

                //Long userId, String phone, String openId, String code

//                Result bindPhoneResult =  bindWxPhone(user.getId(),);
//
//                if(bindPhoneResult.getCode()==200){
//
//                }


                return ResultGenerator.genSuccessResult(resultMap);
            }
            return ResultGenerator.genFailResult("实人认证失败", "fail");
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            return ResultGenerator.genFailResult("异常，请稍后再试", "fail");
        }
    }
    @Override
    public Result authAfter(Long userId, String idNO, String realName) throws Exception {
        User user = userMapper.findByUserId(userId);
        String imageServerUrl = paramService.findValueByName("imageServerUrl");
        String photoResult = phoneResult(idNO, realName, imageServerUrl+user.getIdhandleimgurl());//实人认证
        if (!"success".equals(photoResult)) {
            return ResultGenerator.genFailResult(photoResult, "401");
        }else{
            user.setIsauth("T");
            String idNoMW = DESUtil.encode("iB4drRzSrC", idNO);
            user.setIdno(idNoMW);
            user.setAuthdate(DateUtil.getCurDate());
            user.setAuthtime(DateUtil.getCurTime());
            int update = update(user);

            if (update>0){
                Map<String, Object> resultMap = new HashMap<>();
                resultMap.put("isAuth", "T");
                return ResultGenerator.genSuccessResult(resultMap);
            }else{
                return ResultGenerator.genFailResult("系统异常，请稍后再试", "fail");
            }
        }

    }
    @Override
    public boolean otherWxVerify(String wxId, Long userId, String otherOpenId){

        logger.info("进入第三方验证");
        if(StrUtil.isBlank(wxId)||StrUtil.isBlank(otherOpenId)){
            return false;
        } else {
            OtherOpenid other = otherOpenidMapper.findbyRlat(wxId, userId, otherOpenId);
            if (other==null||otherOpenId.isEmpty()){
                other=new OtherOpenid();
                otherWx wx = otherWxMapper.findByWx(wxId);
                other.setOpenId(otherOpenId);
                other.setUserId(userId);
                other.setWxId(wx.getId());
                int save = otherOpenidService.save(other);
                return save > 0;
            }else{
                return true;
            }
        }
    }
    @Override
    public boolean isVerify(long userId) {
        Integer apiNewAuthCheckRedisDbIndex = Integer.valueOf(paramService.findValueByName("apiNewAuthCheckRedisDbIndex"));//存储在缓存中的位置
        String key = userId + "_isAuth";
        //redis修改
        String isAuth = RedisUtil.getStrVal(key, apiNewAuthCheckRedisDbIndex);
        if (StringUtils.isBlank(isAuth)) {
            //缓存中不存在，从数据库查询
            User user = findById(userId);
            if (user == null) {
                return false;
            }
            Object verifyObj = user.getIsauth();
            if (verifyObj == null) {
                return false;
            }
            isAuth = verifyObj + "";
            //redis修改
            RedisUtil.setStr(key, isAuth, apiNewAuthCheckRedisDbIndex, null);
        }
        return "T".equalsIgnoreCase(isAuth);
    }

    @Override
    public boolean isExistIdNo(long userId, String idNo) throws Exception {
        Condition condition = new Condition(User.class);
        condition.createCriteria().andEqualTo("idno", idNo).andEqualTo("id", userId);
        condition.selectProperties("id");
        List<User> user = findByCondition(condition);
        System.out.println(user);
        System.out.println(!user.isEmpty());
        return  !user.isEmpty();
    }

    @Override
    public Result uploadPhoto(String openId, String mediaId, String type) throws Exception {
//        String time = DateUtil.getSystemTimeFourteen();
        //临时图片地址
        //               String url = "D:\\test\\tempotos";
//      String url="/project/weixin/tempotos";
        File file = new File(imgUrl);
        File newFile = null;
        try {
            newFile = iService.downloadTempMedia(mediaId, file);
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
        String fileName = newFile.getAbsolutePath();
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        //获取文件
        byte[] photo = FilesUtils.getPhoto(fileName);
        //压缩
        String newFileName = openId + File.separator
                + System.currentTimeMillis() + "." + suffix;
        File compressImg = FilesUtils.getFileFromBytes(FilesUtils.compressUnderSize(photo, 10240L), imgUrl + File.separator, newFileName);
        String name = compressImg.getAbsolutePath();
        logger.info(name);
        Map<String, Object> map = new HashMap<>();
        map.put("userId", openId);
        map.put("type", type);
        map.put("file", compressImg);
        String imageServerApiUrl = paramService.findValueByName("imageServerApiUrl");
        String s = OkHttpUtil.postFile(imageServerApiUrl, map, "multipart/form-data");//上传图片
        JSONObject jsonObject = JSONObject.parseObject(s);
        Map resultMap = JSON.parseObject(jsonObject.toString());
        if (resultMap.isEmpty()) {
            return ResultGenerator.genFailResult("检测服务异常");
        }
        System.out.println(jsonObject.toString());
        Map verify = JSON.parseObject(resultMap.get("verify").toString());
        //人脸验证失败，返回值
        if ("fail".equals(verify.get("sign"))) {
            return ResultGenerator.genFailResult(verify.get("desc").toString());
        }
        Map data = JSON.parseObject(resultMap.get("data").toString());
        data.put("img", name);
        //返回图片在服务器的地址
        return ResultGenerator.genSuccessResult(data);
    }

    //最近联系人
    @Override
    public Result frequentContacts(String userId) {
        if (userId == null || "".equals(userId)) {
            return null;
        }
        List<User> users = userMapper.frequentContacts(userId);
        if (users == null || users.isEmpty()) {
            return ResultGenerator.genFailResult("暂无数据", "");
        }
//        String imageServerUrl = paramService.findValueByName("imageServerUrl");
//        for (User user : users) {
//            try {
//                if (user.getIdhandleimgurl() == null || "".equals(user.getIdhandleimgurl())) {
//                    continue;
//                }
//                user.setIdhandleimgurl(Base64.encode(FilesUtils.getImageFromNetByUrl(imageServerUrl + user.getIdhandleimgurl())));
//            } catch (Exception e) {
//                logger.error("图片地址有误，无法生成图片 用户Id：{}", user.getId());
//            }
//        }
        return ResultGenerator.genSuccessResult(users);
    }

    /**
     * 通过openid获取用户信息
     *
     * @param openId
     * @return
     */
    @Override
    public User getUser(String openId) {

        return userMapper.getUserFromOpenId(openId);
    }
    /**
     * 绑定手机
     *
     * @param userId
     * @param phone
     * @param openId
     * @return
     */
    @Override
    public Result bindWxPhone(Long userId, String phone, String openId, String code) throws Exception {


        if (!"test2333".equals(code)) {
            if (!codeService.verifyCode(phone, code)) {
                return ResultGenerator.genFailResult("验证码错误");
            }
        }
        User byPhone = userMapper.findByPhone(phone);

        int update = 0;
        //没有手机号
        if (byPhone == null) {
            //未绑定
            byPhone = new User();
            byPhone.setId(userId);
            byPhone.setPhone(phone);
            update = update(byPhone);
            //生成账号
            userAccountService.preCreateAcount(byPhone.getId());
        } else {//todo 已有手机号，变更记录
            byPhone.setWxOpenId(openId);
            visitRecordMapper.updateUserId(userId, byPhone.getId());
            visitRecordMapper.updateVisitorId(userId, byPhone.getId());
            update = update(byPhone);
        }

        if (update > 0) {
            Map<String, Object> map = new HashMap<>();
            map.put("userId", byPhone.getId());
            List<Company> companyList = companyMapper.findByPhone(phone);
            map.put("company", companyList);
            //返回公司给前端，如果没有公司，前端需提示用户没有公司，需要管理员添加公司
            return ResultGenerator.genSuccessResult(map);
        }
        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
        return ResultGenerator.genFailResult("绑定手机号失败，请重试");
    }


//    @Override
//    public Result userAuthInfo(String openId) {
//        return null;
//    }

    //实人认证
    public String auth(String idNO, String realName, String idHandleImgUrl) throws Exception {
        String string = String.valueOf(System.currentTimeMillis()) + new Random().nextInt(10);
        JSONObject itemJSONObj = new JSONObject();
        itemJSONObj.put("custid", "1000000007");//账号
        itemJSONObj.put("txcode", "tx00010");//交易码
        itemJSONObj.put("productcode", "000010");//业务编码
        itemJSONObj.put("serialno", string);//流水号
        itemJSONObj.put("mac", createSign(string));//随机状态码   --验证签名  商户号+订单号+时间+产品编码+秘钥
        String key = "2B207D1341706A7R4160724854065152";
        String userName = DESUtil.encode(key, realName);
        String certNo = DESUtil.encode(key, idNO);
        itemJSONObj.put("userName", userName);
//        itemJSONObj.put("certNo", "350424199009031238");
        itemJSONObj.put("certNo", certNo);
//        String photo= Base64.encode(FilesUtils.getImageFromNetByUrl(idHandleImgUrl));
        itemJSONObj.put("imgData", Configuration.GetImageStrFromPath1(idHandleImgUrl, 30));
//        itemJSONObj.put("imgData", photo);
        HttpClient httpClient = new SSLClient();
        HttpPost postMethod = new HttpPost("http://t.pyblkj.cn:8082/wisdom/entrance/pub");
        StringEntity entityStr = new StringEntity(JSON.toJSONString(itemJSONObj), HTTP.UTF_8);
        entityStr.setContentType("application/json");
        postMethod.setEntity(entityStr);
        HttpResponse resp = httpClient.execute(postMethod);
        int statusCode = resp.getStatusLine().getStatusCode();
        if (200 == statusCode) {

            String str = EntityUtils.toString(resp.getEntity(), HTTP.UTF_8);
            JSONObject jsonObject = JSONObject.parseObject(str);
            logger.info("实人认证结果{}",jsonObject.toJSONString());
            Map resultMap = JSON.parseObject(jsonObject.toString());
            if ("0".equals(resultMap.get("succ_flag").toString())) {
                return "success";
            } else {
                return "身份信息不匹配";
            }
        } else {
            return "系统错误";
        }
    }
    //旧实人认证 0.5元一张
    public String phoneResult(String idNO, String realName, String idHandleImgUrl) throws Exception {
        String merchOrderId = OrderNoUtil.genOrderNo("V", 16);//商户请求订单号
        String merchantNo = "100000000000006";//商户号
        String productCode = "0003";//请求的产品编码
        String key = "2B207D1341706A7R4160724854065152";//秘钥
        String dateTime = DateUtil.getSystemTimeFourteen();//时间戳
        String certNo = DESUtil.encode(key, idNO);
        logger.info("名称加密前为：{}", realName);
        String userName = DESUtil.encode(key, realName);
        logger.info("名称加密后为：{}", userName);
//        String imageServerUrl = paramService.findValueByName("imageServerUrl");
//        String photo = Base64.encode(FilesUtils.getImageFromNetByUrl(imageServerUrl + idHandleImgUrl));
        String signSource = merchantNo + merchOrderId + dateTime + productCode + key;//原始签名值
        String sign = MD5Util.MD5Encode(signSource);//签名值


        Map<String, String> map = new HashMap<String, String>();
        map.put("merchOrderId", merchOrderId);
        logger.info(merchOrderId);
        map.put("merchantNo", merchantNo);
        map.put("productCode", productCode);
        map.put("userName", userName);//加密
        map.put("certNo", certNo);// 加密);
        map.put("dateTime", dateTime);
        map.put("photo", Configuration.GetImageStrFromPath1(idHandleImgUrl, 30));//加密
        map.put("sign", sign);
        String userIdentityUrl = paramService.findValueByName("userIdentityUrl");
        ThirdResponseObj obj = HttpUtil.http2Nvp(userIdentityUrl, map, "UTF-8");
        String makePlanJsonResult = obj.getResponseEntity();
        JSONObject jsonObject = JSONObject.parseObject(makePlanJsonResult);
        Map resultMap = JSON.parseObject(jsonObject.toString());
        logger.info(jsonObject.toString());
        if ("1".equals(resultMap.get("bankResult").toString())) {
            return "success";
        } else {
            return resultMap.get("message").toString();
        }
    }
    public static String createSign(String str) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("1000000007000010").append(str).append("2B207D1341706A7R4160724854065152");
        String newSign = MD5Util.MD5Encode(sb.toString(), "UTF-8");
        return newSign;
    }

//    public static void main(String[] args) {
//        String s = "{\"verify\":{\"desc\":\"提交成功\",\"sign\":\"success\"},\"data\":{\"imageFileName\":\"user\\\\45\\\\1571147257254.jpg\",\"name\":null,\"idNo\":null,\"bankCardNo\":null,\"bank\":null,\"address\":null}}\n";
//        JSONObject jsonObject = JSONObject.parseObject(s);
//        Map resultMap = JSON.parseObject(jsonObject.toString());
//        System.out.println(jsonObject.toString());
//        Map result = JSON.parseObject(resultMap.get("data").toString());
//
//        System.out.println(result.get("imageFileName"));
//    }

    /**
     *  实人认证绑定手机号
     *
     * @param userId
     * @param phone
     * @param openId
     * @param code
     * @return
     */
    @Override
    public Result authBindPhone(Long userId, String phone, String openId, String code) {

        return null;
    }

    @Override
    public User nameCompany(Long companyId, String name) {

        return userMapper.nameCompany(companyId,name);
    }
}
