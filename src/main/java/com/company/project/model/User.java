package com.company.project.model;

import javax.persistence.*;

@Table(name = "tbl_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 所属机构
     */
    @Column(name = "orgId")
    private Long orgid;

    /**
     * 机构关联 方便查看无限下级
     */
    @Column(name = "relationNo")
    private String relationno;

    /**
     * 真实姓名
     */
    @Column(name = "realName")
    private String realname;

    /**
     * 性别
     */
    private String sex;

    /**
     * 昵称
     */
    @Column(name = "niceName")
    private String nicename;

    /**
     * 登录账号
     */
    @Column(name = "loginName")
    private String loginname;

    /**
     * 证件类型 默认就是身份证 01
     */
    @Column(name = "idType")
    private String idtype;

    /**
     * 证件号 用密钥加密，取出来再解密
     */
    @Column(name = "idNO")
    private String idno;

    /**
     * 联系手机号
     */
    private String phone;

    /**
     * 建立日期 yyyy-MM-dd
     */
    @Column(name = "createDate")
    private String createdate;

    /**
     * 建立时间 HH:mm:ss
     */
    @Column(name = "createTime")
    private String createtime;

    /**
     * 省
     */
    private String province;

    /**
     * 市
     */
    private String city;

    /**
     * 县
     */
    private String area;

    /**
     * 地址
     */
    private String addr;

    /**
     * 是否实名 F:未实名 T：实名;N:审核中
     */
    @Column(name = "isAuth")
    private String isauth;

    /**
     * 实名日期 yyyy-MM-dd
     */
    @Column(name = "authDate")
    private String authdate;

    /**
     * 实名时间 HH:mm:ss
     */
    @Column(name = "authTime")
    private String authtime;

    /**
     * 证件正面照
     */
    @Column(name = "idFrontImgUrl")
    private String idfrontimgurl;

    /**
     * 证件反面照
     */
    @Column(name = "idOppositeImgUrl")
    private String idoppositeimgurl;

    /**
     * 手持证件照
     */
    @Column(name = "idHandleImgUrl")
    private String idhandleimgurl;

    /**
     * 银行卡正面照
     */
    @Column(name = "bankCardImgUrl")
    private String bankcardimgurl;

    @Column(name = "headImgUrl")
    private String headimgurl;

    private String token;

    /**
     * 是否设置支付密码 T：设置 F：未设置
     */
    @Column(name = "isSetTransPwd")
    private String issettranspwd;

    /**
     * 二维码存放地址
     */
    @Column(name = "qrcodeUrl")
    private String qrcodeurl;

    /**
     * 公司id
     */
    @Column(name = "companyId")
    private Long companyId;

    /**
     * staff:普通员工,manage:管理员,front:前台
     */
    private String role;

    /**
     * 密钥 用于加解密身份证号、卡号
     */
    @Column(name = "workKey")
    private String workkey;

    /**
     * 审核失败事由
     */
    @Column(name = "failReason")
    private String failreason;

    /**
     * 用户唯一标识码
     */
    @Column(name = "soleCode")
    private String solecode;

    /**
     * 有效期
     */
    @Column(name = "validityDate")
    private String validitydate;

    /**
     * 友盟设备编号
     */
    @Column(name = "deviceToken")
    private String devicetoken;

    /**
     * 1 ios 2 android
     */
    @Column(name = "deviceType")
    private String devicetype;

    /**
     * T --在线 F--离线
     */
    @Column(name = "isOnlineApp")
    private String isonlineapp;
    /**
     * 微信公众号标识
     */
    @Column(name = "wx_open_id")
    private String wxOpenId;

    /**
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取所属机构
     *
     * @return orgId - 所属机构
     */
    public Long getOrgid() {
        return orgid;
    }

    /**
     * 设置所属机构
     *
     * @param orgid 所属机构
     */
    public void setOrgid(Long orgid) {
        this.orgid = orgid;
    }

    /**
     * 获取机构关联 方便查看无限下级
     *
     * @return relationNo - 机构关联 方便查看无限下级
     */
    public String getRelationno() {
        return relationno;
    }

    /**
     * 设置机构关联 方便查看无限下级
     *
     * @param relationno 机构关联 方便查看无限下级
     */
    public void setRelationno(String relationno) {
        this.relationno = relationno;
    }

    /**
     * 获取真实姓名
     *
     * @return realName - 真实姓名
     */
    public String getRealname() {
        return realname;
    }

    /**
     * 设置真实姓名
     *
     * @param realname 真实姓名
     */
    public void setRealname(String realname) {
        this.realname = realname;
    }

    /**
     * 获取性别
     *
     * @return sex - 性别
     */
    public String getSex() {
        return sex;
    }

    /**
     * 设置性别
     *
     * @param sex 性别
     */
    public void setSex(String sex) {
        this.sex = sex;
    }

    /**
     * 获取昵称
     *
     * @return niceName - 昵称
     */
    public String getNicename() {
        return nicename;
    }

    /**
     * 设置昵称
     *
     * @param nicename 昵称
     */
    public void setNicename(String nicename) {
        this.nicename = nicename;
    }

    /**
     * 获取登录账号
     *
     * @return loginName - 登录账号
     */
    public String getLoginname() {
        return loginname;
    }

    /**
     * 设置登录账号
     *
     * @param loginname 登录账号
     */
    public void setLoginname(String loginname) {
        this.loginname = loginname;
    }

    /**
     * 获取证件类型 默认就是身份证 01
     *
     * @return idType - 证件类型 默认就是身份证 01
     */
    public String getIdtype() {
        return idtype;
    }

    /**
     * 设置证件类型 默认就是身份证 01
     *
     * @param idtype 证件类型 默认就是身份证 01
     */
    public void setIdtype(String idtype) {
        this.idtype = idtype;
    }

    /**
     * 获取证件号 用密钥加密，取出来再解密
     *
     * @return idNO - 证件号 用密钥加密，取出来再解密
     */
    public String getIdno() {
        return idno;
    }

    /**
     * 设置证件号 用密钥加密，取出来再解密
     *
     * @param idno 证件号 用密钥加密，取出来再解密
     */
    public void setIdno(String idno) {
        this.idno = idno;
    }

    /**
     * 获取联系手机号
     *
     * @return phone - 联系手机号
     */
    public String getPhone() {
        return phone;
    }

    /**
     * 设置联系手机号
     *
     * @param phone 联系手机号
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * 获取建立日期 yyyy-MM-dd
     *
     * @return createDate - 建立日期 yyyy-MM-dd
     */
    public String getCreatedate() {
        return createdate;
    }

    /**
     * 设置建立日期 yyyy-MM-dd
     *
     * @param createdate 建立日期 yyyy-MM-dd
     */
    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }

    /**
     * 获取建立时间 HH:mm:ss
     *
     * @return createTime - 建立时间 HH:mm:ss
     */
    public String getCreatetime() {
        return createtime;
    }

    /**
     * 设置建立时间 HH:mm:ss
     *
     * @param createtime 建立时间 HH:mm:ss
     */
    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    /**
     * 获取省
     *
     * @return province - 省
     */
    public String getProvince() {
        return province;
    }

    /**
     * 设置省
     *
     * @param province 省
     */
    public void setProvince(String province) {
        this.province = province;
    }

    /**
     * 获取市
     *
     * @return city - 市
     */
    public String getCity() {
        return city;
    }

    /**
     * 设置市
     *
     * @param city 市
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * 获取县
     *
     * @return area - 县
     */
    public String getArea() {
        return area;
    }

    /**
     * 设置县
     *
     * @param area 县
     */
    public void setArea(String area) {
        this.area = area;
    }

    /**
     * 获取地址
     *
     * @return addr - 地址
     */
    public String getAddr() {
        return addr;
    }

    /**
     * 设置地址
     *
     * @param addr 地址
     */
    public void setAddr(String addr) {
        this.addr = addr;
    }

    /**
     * 获取是否实名 F:未实名 T：实名;N:审核中
     *
     * @return isAuth - 是否实名 F:未实名 T：实名;N:审核中
     */
    public String getIsauth() {
        return isauth;
    }

    /**
     * 设置是否实名 F:未实名 T：实名;N:审核中
     *
     * @param isauth 是否实名 F:未实名 T：实名;N:审核中
     */
    public void setIsauth(String isauth) {
        this.isauth = isauth;
    }

    /**
     * 获取实名日期 yyyy-MM-dd
     *
     * @return authDate - 实名日期 yyyy-MM-dd
     */
    public String getAuthdate() {
        return authdate;
    }

    /**
     * 设置实名日期 yyyy-MM-dd
     *
     * @param authdate 实名日期 yyyy-MM-dd
     */
    public void setAuthdate(String authdate) {
        this.authdate = authdate;
    }

    /**
     * 获取实名时间 HH:mm:ss
     *
     * @return authTime - 实名时间 HH:mm:ss
     */
    public String getAuthtime() {
        return authtime;
    }

    /**
     * 设置实名时间 HH:mm:ss
     *
     * @param authtime 实名时间 HH:mm:ss
     */
    public void setAuthtime(String authtime) {
        this.authtime = authtime;
    }

    /**
     * 获取证件正面照
     *
     * @return idFrontImgUrl - 证件正面照
     */
    public String getIdfrontimgurl() {
        return idfrontimgurl;
    }

    /**
     * 设置证件正面照
     *
     * @param idfrontimgurl 证件正面照
     */
    public void setIdfrontimgurl(String idfrontimgurl) {
        this.idfrontimgurl = idfrontimgurl;
    }

    /**
     * 获取证件反面照
     *
     * @return idOppositeImgUrl - 证件反面照
     */
    public String getIdoppositeimgurl() {
        return idoppositeimgurl;
    }

    /**
     * 设置证件反面照
     *
     * @param idoppositeimgurl 证件反面照
     */
    public void setIdoppositeimgurl(String idoppositeimgurl) {
        this.idoppositeimgurl = idoppositeimgurl;
    }

    /**
     * 获取手持证件照
     *
     * @return idHandleImgUrl - 手持证件照
     */
    public String getIdhandleimgurl() {
        return idhandleimgurl;
    }

    /**
     * 设置手持证件照
     *
     * @param idhandleimgurl 手持证件照
     */
    public void setIdhandleimgurl(String idhandleimgurl) {
        this.idhandleimgurl = idhandleimgurl;
    }

    /**
     * 获取银行卡正面照
     *
     * @return bankCardImgUrl - 银行卡正面照
     */
    public String getBankcardimgurl() {
        return bankcardimgurl;
    }

    /**
     * 设置银行卡正面照
     *
     * @param bankcardimgurl 银行卡正面照
     */
    public void setBankcardimgurl(String bankcardimgurl) {
        this.bankcardimgurl = bankcardimgurl;
    }

    /**
     * @return headImgUrl
     */
    public String getHeadimgurl() {
        return headimgurl;
    }

    /**
     * @param headimgurl
     */
    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }

    /**
     * @return token
     */
    public String getToken() {
        return token;
    }

    /**
     * @param token
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * 获取是否设置支付密码 T：设置 F：未设置
     *
     * @return isSetTransPwd - 是否设置支付密码 T：设置 F：未设置
     */
    public String getIssettranspwd() {
        return issettranspwd;
    }

    /**
     * 设置是否设置支付密码 T：设置 F：未设置
     *
     * @param issettranspwd 是否设置支付密码 T：设置 F：未设置
     */
    public void setIssettranspwd(String issettranspwd) {
        this.issettranspwd = issettranspwd;
    }

    /**
     * 获取二维码存放地址
     *
     * @return qrcodeUrl - 二维码存放地址
     */
    public String getQrcodeurl() {
        return qrcodeurl;
    }

    /**
     * 设置二维码存放地址
     *
     * @param qrcodeurl 二维码存放地址
     */
    public void setQrcodeurl(String qrcodeurl) {
        this.qrcodeurl = qrcodeurl;
    }

    /**
     * 获取公司id
     *
     * @return companyId - 公司id
     */
    public Long getCompanyId() {
        return companyId;
    }

    /**
     * 设置公司id
     *
     * @param companyId 公司id
     */
    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    /**
     * 获取staff:普通员工,manage:管理员,front:前台
     *
     * @return role - staff:普通员工,manage:管理员,front:前台
     */
    public String getRole() {
        return role;
    }

    /**
     * 设置staff:普通员工,manage:管理员,front:前台
     *
     * @param role staff:普通员工,manage:管理员,front:前台
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * 获取密钥 用于加解密身份证号、卡号
     *
     * @return workKey - 密钥 用于加解密身份证号、卡号
     */
    public String getWorkkey() {
        return workkey;
    }

    /**
     * 设置密钥 用于加解密身份证号、卡号
     *
     * @param workkey 密钥 用于加解密身份证号、卡号
     */
    public void setWorkkey(String workkey) {
        this.workkey = workkey;
    }

    /**
     * 获取审核失败事由
     *
     * @return failReason - 审核失败事由
     */
    public String getFailreason() {
        return failreason;
    }

    /**
     * 设置审核失败事由
     *
     * @param failreason 审核失败事由
     */
    public void setFailreason(String failreason) {
        this.failreason = failreason;
    }

    /**
     * 获取用户唯一标识码
     *
     * @return soleCode - 用户唯一标识码
     */
    public String getSolecode() {
        return solecode;
    }

    /**
     * 设置用户唯一标识码
     *
     * @param solecode 用户唯一标识码
     */
    public void setSolecode(String solecode) {
        this.solecode = solecode;
    }

    /**
     * 获取有效期
     *
     * @return validityDate - 有效期
     */
    public String getValiditydate() {
        return validitydate;
    }

    /**
     * 设置有效期
     *
     * @param validitydate 有效期
     */
    public void setValiditydate(String validitydate) {
        this.validitydate = validitydate;
    }

    /**
     * 获取友盟设备编号
     *
     * @return deviceToken - 友盟设备编号
     */
    public String getDevicetoken() {
        return devicetoken;
    }

    /**
     * 设置友盟设备编号
     *
     * @param devicetoken 友盟设备编号
     */
    public void setDevicetoken(String devicetoken) {
        this.devicetoken = devicetoken;
    }

    /**
     * 获取1 ios 2 android
     *
     * @return deviceType - 1 ios 2 android
     */
    public String getDevicetype() {
        return devicetype;
    }

    /**
     * 设置1 ios 2 android
     *
     * @param devicetype 1 ios 2 android
     */
    public void setDevicetype(String devicetype) {
        this.devicetype = devicetype;
    }

    /**
     * 获取T --在线 F--离线
     *
     * @return isOnlineApp - T --在线 F--离线
     */
    public String getIsonlineapp() {
        return isonlineapp;
    }

    /**
     * 设置T --在线 F--离线
     *
     * @param isonlineapp T --在线 F--离线
     */
    public void setIsonlineapp(String isonlineapp) {
        this.isonlineapp = isonlineapp;
    }

    public String getWxOpenId() {
        return wxOpenId;
    }

    public void setWxOpenId(String wxOpenId) {
        this.wxOpenId = wxOpenId;
    }
}