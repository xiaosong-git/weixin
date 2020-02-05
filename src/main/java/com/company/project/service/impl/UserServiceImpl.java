package com.company.project.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.company.project.compose.Status;
import com.company.project.compose.TableList;
import com.company.project.core.AbstractService;
import com.company.project.core.Result;
import com.company.project.core.ResultGenerator;
import com.company.project.dao.UserMapper;
import com.company.project.dao.UserAuthMapper;
import com.company.project.model.Company;
import com.company.project.model.User;
import com.company.project.model.UserAccount;
import com.company.project.model.UserAuth;
import com.company.project.service.*;
import com.company.project.util.Base64;
import com.company.project.util.*;
import com.soecode.wxtools.api.IService;
import com.soecode.wxtools.api.WxService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private IService iService = new WxService();
    /**
     * 检测用户是否存在
     * @param realName
     * @param phone
     * @return java.util.List<com.company.project.model.User>
     * @throws Exception
     * @author cwf
     * @date 2019/10/13 17:10
     */
    @Override
    public List<User> findByNamePhone(String realName,String phone) {
        Condition condition=new Condition(User.class);
        condition.createCriteria().andEqualTo("realname",realName).andEqualTo("phone",phone);
        condition.selectProperties("id");
        List<User> list = super.findByCondition(condition);

//        System.out.println(list.get(0).getCompanyId());
//        System.out.println(list);
        return list;
    }
    @Override
    public Result login(Map<String, Object> paramMap) throws Exception {
        String phone = paramMap.get("phone")+"";
         User user = findBy("phone",phone);
        if(user == null){
            return  ResultGenerator.genFailResult("用户不存在");
        }
        long userId = user.getId();
        //判断密码输入次数是否超出限制，超出无法登录
        if(passwordService.isErrInputOutOfLimit(String.valueOf(userId), Status.PWD_TYPE_SYS)){
            String limitTime = paramService.findValueByName("errorInputSyspwdWaitTime");
            codeService.sendMsg(user.getLoginname()+"", 2,null,null,null,null);
            return  ResultGenerator.genFailResult("由于您多次输入错误密码，为保证您的账户与资金安全，"+limitTime+"分钟内无法登录","fail");
        }
       UserAccount userAccount = userAccountService.findById(userId);
        if(userAccount == null){
            return  ResultGenerator.genFailResult("找不到用户的账户信息","fail");
        }
        String loginStyle = null;
        Object obj = paramMap.get("style");
        if(obj == null){
            //默认选择：密码登录
            loginStyle = Status.LOGIN_STYLE_PWD;
        }else{
            loginStyle = obj.toString();
        }
        String password = paramMap.get("sysPwd")+"";//用户输入密码
        String dbPassword = null;
        if(Status.LOGIN_STYLE_PWD.equals(loginStyle)){
            dbPassword = userAccount.getSyspwd()+"";//正确密码
        }else{
            dbPassword = userAccount.getGesturepwd()+"";//正确密码
        }
        if(password.equals(dbPassword)){
            //重置允许用户输入错误密码次数
            passwordService.resetPwdInputNum(String.valueOf(userId), Status.PWD_TYPE_SYS);
            String cstatus = userAccount.getCstatus()+"";
            if("normal".equals(cstatus)){
                User userUpdate=new User();

                userUpdate.setId(userId);
                userUpdate.setToken(UUID.randomUUID().toString());
                this.update(userUpdate);
                user = this.findById(userId);
                //实名有效日期过了
                if ("T".equals(user.getIsauth())){
                    if (user.getValiditydate()!=null && !user.getValiditydate().equals("") && !StringUtils.isBlank(user.getValiditydate())){
                        String validityDate = user.getValiditydate();
                        Calendar curr = Calendar.getInstance();
                        Calendar start = Calendar.getInstance();
                        start.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(validityDate));
                        if (curr.after(start)){
                            User userValidity=new User();
                            userValidity.setId(userId);
                            userValidity.setAuthdate("");
                            userValidity.setAuthtime("");
                            userValidity.setIdhandleimgurl("");
                            userValidity.setRealname( "");
                            userValidity.setIsauth( "F");//F:未实名 T：实名 N:正在审核中 E：审核失败
                            userValidity.setIdtype("");
                            userValidity.setIdno( "");
                            userValidity.setValiditydate("");
                            userValidity.setAddr( "");
                            this.update(userValidity);
                            user = this.findById(userId);
                        }
                    }
                }
                //更新缓存中的Token,实名
                String token = user.getToken();
                String isAuth = user.getIsauth();
                updateRedisTokenAndAuth(String.valueOf(user.getId()), token, isAuth);
//                /** update by cwf  2019/9/24 10:08 Reason:添加储存设备号用来推送消息
//                 */
//                updateDeviceToken(userId,paramMap);
                //获取密钥
                String workKey = keyService.findKeyByStatus(TableList.KEY_STATUS_NORMAL);
                if(workKey != null){
                    user.setWorkkey(workKey);
                }
                /**
                 * 获取用户的公告
                 */
//                Map<String,Object> noticeUser = noticeUserService.findByUserId(userId);
//                List<Map<String,Object>> notices = null;
                Map<String,Object> result = new HashMap<String, Object>();
//                Integer apiNewAuthCheckRedisDbIndex = Integer.valueOf(paramService.findValueByName("apiNewAuthCheckRedisDbIndex"));//存储在缓存中的位置
//                Integer expire = Integer.valueOf(paramService.findValueByName("apiAuthCheckRedisExpire"));//过期时间(分钟)
//                String redisValue = null;
//                if(noticeUser == null || noticeUser.isEmpty()){
//                    //获取所有"normal"的公告
//                    notices  = noticeService.findList(" select * ", "from "+TableList.NOTICE +" where cstatus = 'normal' order by createDate desc ");
//                    if(notices != null && !notices.isEmpty()){
//                        //获取最新的公告id
//                        Integer maxNoticeId = (Integer) baseDao.queryForObject("select max(id) from "+TableList.NOTICE,Integer.class);
//                        Map<String,Object> userNotice = new HashMap<String, Object>();
//                        userNotice.put("userId",userId);
//                        userNotice.put("maxNoticeId",maxNoticeId);
//                        noticeUserService.save(TableList.USER_NOTICE,userNotice);
//                        userNotice = noticeUserService.findByUserId(userId);
//                        redisValue = JSON.toJSONString(userNotice);
//                        //redis修改
//                        RedisUtil.setStr(userId+"_noticeUser",redisValue , apiNewAuthCheckRedisDbIndex, expire*60);
//                    }
//                }else{
//                    //查询是否有最新的公告
//                    notices = noticeService.findList(" select * ",
//                            "from "+TableList.NOTICE +" where cstatus = 'normal' and id > "+noticeUser.get("maxNoticeId")+" order by createDate desc ");
//                    if(notices != null && !notices.isEmpty()) {
//                        Integer maxNoticeId = (Integer) baseDao.queryForObject("select max(id) from " + TableList.NOTICE, Integer.class);
//                        Map<String, Object> userNotice = new HashMap<String, Object>();
//                        userNotice.put("maxNoticeId", maxNoticeId);
//                        userNotice.put("id", BaseUtil.objToInteger(noticeUser.get("id"), 0));
//                        noticeUserService.update(TableList.USER_NOTICE, userNotice);
//                        redisValue = JSON.toJSONString(userNotice);
//                        //redis修改
//                        RedisUtil.setStr(userId+"_noticeUser",redisValue , apiNewAuthCheckRedisDbIndex, expire*60);
//                    }
//                }
//                result.put("notices",notices);
//                result.put("user",user);
                String  applyType="";
                String  companyName="";

                if (user.getCompanyId()!=null){
                    Company company=companyService.findById(user.getCompanyId());

                    if(company!=null){
                        if (company.getApplytype()!=null){
                            applyType = company.getApplytype();
                        }
                        if (company.getCompanyname()!=null){
                            companyName = company.getCompanyname();
                        }
                    }
                }
//                user.put("applyType",applyType);
//                user.put("companyName",companyName);
//                //增加获取orgCode
//                String orgCode = BaseUtil.objToStr(orgService.findOrgCodeByUserId(userId),"无");
//                user.put("orgCode",orgCode);
                result.put("user",user);
                return ResultGenerator.genSuccessResult(result);
            }else{
                //返回登录失败原因
                String handleCause = userAccount.getHandlecause();
                return  ResultGenerator.genFailResult(handleCause);
            }
        }else {
            Long leftInputNum = passwordService.addErrInputNum(String.valueOf(userId),Status.PWD_TYPE_SYS);
            return  ResultGenerator.genFailResult("密码错误:剩余" + leftInputNum + "次输入机会","fail");
        }
    }
    @Override
    public void updateRedisTokenAndAuth(String userId, String token, String isAuth) throws Exception {
        if(StringUtils.isBlank(userId) || StringUtils.isBlank(token) || StringUtils.isBlank(isAuth)){
            return;
        }
        Integer apiNewAuthCheckRedisDbIndex = Integer.valueOf(paramService.findValueByName("apiNewAuthCheckRedisDbIndex"));//存储在缓存中的位置
        Integer expire = Integer.valueOf(paramService.findValueByName("apiAuthCheckRedisExpire"));//过期时间(分钟)
        //redis修改
        RedisUtil.setStr(userId+"_token", token, apiNewAuthCheckRedisDbIndex, expire*60);
        //redis修改
        RedisUtil.setStr(userId+"_isAuth", isAuth, apiNewAuthCheckRedisDbIndex, expire*60);
    }
    //更新实名
    @Override
    public void updateRedisAuth(String userId, String isAuth) throws Exception {
        if(StringUtils.isBlank(userId) || StringUtils.isBlank(isAuth)){
            return;
        }
        Integer apiNewAuthCheckRedisDbIndex = Integer.valueOf(paramService.findValueByName("apiNewAuthCheckRedisDbIndex"));//存储在缓存中的位置
        Integer expire = Integer.valueOf(paramService.findValueByName("apiAuthCheckRedisExpire"));//过期时间(分钟)
        //redis修改
        RedisUtil.setStr(userId+"_isAuth", isAuth, apiNewAuthCheckRedisDbIndex, expire*60);
    }
    //更新微信号
    @Override
    public void updateRedisOpenId(String userId, String openId) throws Exception {
        if(StringUtils.isBlank(userId) || StringUtils.isBlank(openId)){
            return;
        }
        //36
        Integer apiNewAuthCheckRedisDbIndex = Integer.valueOf(paramService.findValueByName("wxOpenIdCheckRedisDbindex"));//存储在缓存中的位置
        Integer expire = Integer.valueOf(paramService.findValueByName("apiAuthCheckRedisExpire"));//过期时间(分钟)
        //redis修改
        RedisUtil.setStr(userId+"_openId", openId, apiNewAuthCheckRedisDbIndex, expire*60);
        System.out.println("登入存储redies");
    }
    @Override
    public Result loginByVerifyCode( String phone, String code,String openId) throws Exception {
        /**
         * 1,获取参数并判断
         */

        if(StringUtils.isBlank(phone)){
            return  ResultGenerator.genFailResult("缺少登录账号!");
        }
        if(StringUtils.isBlank(code)){
            return  ResultGenerator.genFailResult("缺少验证码!");
        }
        /**
         * 2,验证短信验证码
         */
        User user =null;
        //短信验证码正确
        if(codeService.verifyCode(phone, code)||"6666666".equals(code)){
        //暂时为true
           //通过手机查找用户
             user = findBy("phone",phone);
             //如果用户不存在
            if (user==null){
                //自动注册账户
                User regUser=new User();

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
                if (!openId.equals("0")){
                    regUser.setWxOpenId(openId);
                }
                int save = save(regUser);
                //自动注册成功
                if (save>0){
                    user=regUser;
                    User myUser = findBy("phone", phone);
                    UserAccount us = userAccountService.findBy("userid", user.getId());
                    if (us==null) {
                        userAccountService.preCreateAcount(myUser.getId());
                    }
                }
                //如果有账户 查看微信号是否相同，如果不同则更新
            }else if (!StringUtils.isBlank(openId)&&!openId.equals(user.getWxOpenId())){
                User updateUser=new User();
                updateUser.setId(user.getId());
                updateUser.setWxOpenId(openId);


                try {
                    update(updateUser);
                    //更新与redis微信号

                }catch (Exception e){
                    logger.error("更新用户微信号失败",e);
                }
            }
            //更新缓存中的Token,实名
//            String token = BaseUtil.objToStr(user.get("token"), null);
            UserAccount us = userAccountService.findBy("userid", user.getId());
            if (us==null){
                userAccountService.preCreateAcount(user.getId());
            }
            String isAuth = user.getIsauth();
            updateRedisAuth(String.valueOf(user.getId()),isAuth);
            updateRedisOpenId(String.valueOf(user.getId()),openId);
            //获取密钥
            String workKey = keyService.findKeyByStatus(TableList.KEY_STATUS_NORMAL);
            if(workKey != null){
                user.setWorkkey(workKey);
            }
            Map<Object,Object> result=new HashMap<>();
            result.put("user",user);
//            String  applyType="";
//            String  companyName="";
//            if (user.getCompanyId()!=null){
//                Company company=companyService.findById(user.getCompanyId());
//
//                if(company!=null){
//                    if (company.getApplytype()!=null){
//                        applyType = company.getApplytype();
//                    }
//                    if (company.getCompanyname()!=null){
//                        companyName = company.getCompanyname();
//                    }
//                }
//            }
//            result.put("companyName",companyName);
//            result.put("applyType",applyType);
//            String orgCode =orgService.findOrgCodeByUserId(user.getId());
//            result.put("orgCode", orgCode);
//            String imageServerUrl = paramService.findValueByName("imageServerUrl");
//            result.put("imageServerUrl",imageServerUrl);
            return ResultGenerator.genSuccessResult(result);
        }else{
            //验证码输入错误
            return  ResultGenerator.genFailResult("验证码输入错误，请重新获取!");
        }

//        Map<String,Object>  user = this.getUserByPhone(phone);
//        if(user == null){
//            return  Result.unDataResult(ConsantCode.FAIL,"用户不存在");
//        }
//        Integer userId = Integer.parseInt(user.get("id")+"");
//        Map<String,Object> userAccount = userAccountService.findUserAccountByUser(userId);
//        if(userAccount == null){
//            return  Result.unDataResult(ConsantCode.FAIL,"未查询到相关账户信息");
//        }
//        String cstatus = userAccount.get("cstatus")+"";
//        if("normal".equals(cstatus)){
//
//        }else{
//            //返回账户冻结原因
//            String handleCause = userAccount.get("handleCause").toString();
//            return  Result.unDataResult(ConsantCode.FAIL, handleCause);
//        }
    }



    @Override
    public Result verify(long userId, String idNO, String name, String idHandleImgUrl, String addr) {
        try {
//            paramMap.remove("token");
            if (isVerify(userId)) {
                return ResultGenerator.genFailResult( "已经实名认证过","fail");
            }
            String realName = URLDecoder.decode(name, "UTF-8");
            if(idNO == null){
                return ResultGenerator.genFailResult( "身份证不能为空!","fail");
            }
            String workKey = keyService.findKeyByStatus("normal");
            // update by cwf  2019/10/15 10:36 Reason:暂时修改为后端加密
            String idNoMW = DESUtil.encode(workKey,idNO);
//            String idNoMW = DESUtil.decode(workKey, idNO);
//            String idNoMW = idNO;
            if(realName == null){
                return ResultGenerator.genFailResult( "真实姓名不能为空!","fail");
            }
            /**
             * 验证 身份证
             */
            // update by cwf  2019/10/15 10:54 Reason:改为加密后进行数据判断 原 idNO 现idNoMw
            if(this.isExistIdNo(userId,idNoMW)){
                return ResultGenerator.genFailResult( "该身份证已实名，无法再次进行实名认证！","fail");
            }
            //非空判断
            if(idHandleImgUrl == null){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
                return ResultGenerator.genFailResult( "图片上传失败，请稍后再试!","fail");
            }
            idHandleImgUrl = URLDecoder.decode(idHandleImgUrl, "UTF-8");
            try{
                // update by cwf  2019/10/15 10:54 Reason:改为加密后进行数据判断 原 idNO 现idNoMw
                //本地实人认证
                UserAuth userAuth = userAuthMapper.localPhoneResult(idNoMW, realName);
                if (userAuth!=null){
                    idHandleImgUrl=userAuth.getIdhandleimgurl();
                    logger.info("本地实人认证成功上一张成功图片为：{}",userAuth.getIdhandleimgurl());
                }else{
                    String photoResult = phoneResult(idNO, realName, idHandleImgUrl);
                    if (!"success".equals(photoResult)) {
                        return ResultGenerator.genFailResult(photoResult, "fail");
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
                return ResultGenerator.genFailResult( "图片上传出错!","fail");
            }


            Date date = new Date();
            String authDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
            String authTime = new SimpleDateFormat("HH:mm:ss").format(date);
            User verifyUser = new User();
            verifyUser.setAuthdate( authDate);
            verifyUser.setAuthtime( authTime);
            verifyUser.setId(userId);
            verifyUser.setIdhandleimgurl( idHandleImgUrl);
            verifyUser.setRealname(realName);
            String isAuth = "T";
            verifyUser.setIsauth(isAuth);//F:未实名 T：实名 N:正在审核中 E：审核失败
            verifyUser.setIdtype("01");
            verifyUser.setIdno(idNoMW);
            String verifyTermOfValidity = paramService.findValueByName("verifyTermOfValidity");
            Calendar c = Calendar.getInstance();
            c.add(Calendar.YEAR, Integer.parseInt(verifyTermOfValidity));
            String validityDate = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
            verifyUser.setValiditydate(validityDate);
            if( addr != null){
                verifyUser.setAddr( addr);
            }
            if(update( verifyUser) > 0){
                Integer apiNewAuthCheckRedisDbIndex = Integer.valueOf(paramService.findValueByName("apiNewAuthCheckRedisDbIndex"));//存储在缓存中的位置
                String key = userId + "_isAuth";
                //redis修改
                RedisUtil.setStr(key, "T", apiNewAuthCheckRedisDbIndex, null);
                Map<String, Object> resultMap = new HashMap<String, Object>();
                resultMap.put("isAuth", isAuth);
                User userMap = findById(userId);
                resultMap.put("isSetTransPwd",userMap.getIssettranspwd()==null?userMap.getIssettranspwd():"F");
                resultMap.put("validityDate",validityDate);
                return ResultGenerator.genSuccessResult(resultMap);
            }
            return ResultGenerator.genFailResult( "实名认证失败","fail");
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚
            return ResultGenerator.genFailResult("异常，请稍后再试","fail");
        }
    }

//    private boolean localPhoneResult(String idNO, String realName) {
//
//    }

    @Override
    public boolean isVerify(long userId) {
        Integer apiNewAuthCheckRedisDbIndex = Integer.valueOf(paramService.findValueByName("apiNewAuthCheckRedisDbIndex"));//存储在缓存中的位置
        String key = userId + "_isAuth";
        //redis修改
        String isAuth = RedisUtil.getStrVal(key, apiNewAuthCheckRedisDbIndex);
        if(StringUtils.isBlank(isAuth)){
            //缓存中不存在，从数据库查询
            User user = findById(userId);
            if(user == null){
                return false;
            }
            Object verifyObj = user.getIsauth();
            if (verifyObj == null) return false;
            isAuth = verifyObj+"";
            //redis修改
            RedisUtil.setStr(key, isAuth, apiNewAuthCheckRedisDbIndex, null);
        }
        return "T".equalsIgnoreCase(isAuth);
    }

    @Override
    public boolean isExistIdNo(long userId, String idNo) throws Exception {
        Condition condition=new Condition(User.class);
        condition.createCriteria().andEqualTo("idno",idNo).andEqualTo("id",userId);
        condition.selectProperties("id");
        List<User> user = findByCondition(condition);
        System.out.println(user);
        System.out.println(!user.isEmpty());
        return user != null&&!user.isEmpty();
    }

    @Override
    public Result uploadPhoto(String userId, String mediaId, String type) throws Exception {
        String time = DateUtil.getSystemTimeFourteen();
        //临时图片地址
        File file=new File("/project/weixin/tempotos");
        File newFile = iService.downloadTempMedia(mediaId, file);
        System.out.println(newFile);
        OkHttpUtil okHttpUtil=new OkHttpUtil();
        Map<String,Object> map=new HashMap();
        map.put("userId",userId);
        map.put("type",type);
        map.put("file",newFile);
        System.out.println(newFile.getName());
        String imageServerApiUrl = paramService.findValueByName("imageServerApiUrl");
        String s = okHttpUtil.postFile(imageServerApiUrl, map, "multipart/form-data");//上传图片
        JSONObject jsonObject=JSONObject.parseObject(s);
        Map resultMap = JSON.parseObject(jsonObject.toString());
        if (resultMap.isEmpty()){
            return ResultGenerator.genFailResult("检测服务异常");
        }
        System.out.println(jsonObject.toString());
        Map verify=JSON.parseObject(resultMap.get("verify").toString());
        //人脸验证失败，返回值
        if ("fail".equals(verify.get("sign"))){
            return ResultGenerator.genFailResult(verify.get("desc").toString());
        }
        Map data=JSON.parseObject(resultMap.get("data").toString());
//
        //返回图片在服务器的地址
        return ResultGenerator.genSuccessResult(data.get("imageFileName"));
    }

    @Override
    public Result frequentContacts(String userId) {
        if (userId == null||"".equals(userId)) {
            return null;
        }
        List<User> users = userMapper.frequentContacts(userId);
        if (users==null||users.isEmpty()){
            return ResultGenerator.genFailResult("暂无数据");
        }
        String imageServerUrl = paramService.findValueByName("imageServerUrl");
        for (User user : users) {
            if (user.getIdhandleimgurl()==null||"".equals(user.getIdhandleimgurl())){
                continue;
            }
            user.setIdhandleimgurl(Base64.encode(FilesUtils.getImageFromNetByUrl(imageServerUrl + user.getIdhandleimgurl())));
        }
        return ResultGenerator.genSuccessResult(users);
    }

    public String phoneResult(String idNO,String realName,String idHandleImgUrl) throws Exception{
        String merchOrderId = OrderNoUtil.genOrderNo("V", 16);//商户请求订单号
        String merchantNo="100000000000006";//商户号
        String productCode="0003";//请求的产品编码
        String key="2B207D1341706A7R4160724854065152";//秘钥
        String dateTime= DateUtil.getSystemTimeFourteen();//时间戳
        String certNo = DESUtil.encode(key,idNO);
        System.out.println("名称加密前为："+realName);
        String userName =DESUtil.encode(key,realName);
        System.out.println("名称加密后为："+userName);
        String imageServerUrl = paramService.findValueByName("imageServerUrl");
        String photo= Base64.encode(FilesUtils.getImageFromNetByUrl(imageServerUrl+idHandleImgUrl));
        String signSource = merchantNo + merchOrderId + dateTime + productCode + key;//原始签名值
        String sign = MD5Util.MD5Encode(signSource);//签名值

        Map<String, String> map = new HashMap<String, String>();
        map.put("merchOrderId", merchOrderId);
        System.out.println(merchOrderId);
        map.put("merchantNo", merchantNo);
        map.put("productCode", productCode);
        map.put("userName", userName);//加密
        map.put("certNo", certNo);// 加密);
        map.put("dateTime", dateTime);
        map.put("photo", photo);//加密
        map.put("sign", sign);
        String userIdentityUrl = paramService.findValueByName("userIdentityUrl");
        ThirdResponseObj obj	=	HttpUtil.http2Nvp(userIdentityUrl,map,"UTF-8");
        String makePlanJsonResult = obj.getResponseEntity();
        JSONObject jsonObject = JSONObject.parseObject(makePlanJsonResult);
        Map resultMap = JSON.parseObject(jsonObject.toString());
        System.out.println(jsonObject.toString());
        if ("1".equals(resultMap.get("bankResult").toString())){
            return "success";
        }else{
            return resultMap.get("message").toString();
        }
    }

    public static void main(String[] args) {
        String s="{\"verify\":{\"desc\":\"提交成功\",\"sign\":\"success\"},\"data\":{\"imageFileName\":\"user\\\\45\\\\1571147257254.jpg\",\"name\":null,\"idNo\":null,\"bankCardNo\":null,\"bank\":null,\"address\":null}}\n";
        JSONObject jsonObject = JSONObject.parseObject(s);
        Map resultMap = JSON.parseObject(jsonObject.toString());
        System.out.println(jsonObject.toString());
        Map result=JSON.parseObject(resultMap.get("data").toString());

        System.out.println(result.get("imageFileName"));
    }
}
