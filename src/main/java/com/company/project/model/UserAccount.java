package com.company.project.model;

import javax.persistence.*;

@Table(name = "tbl_user_account")
public class UserAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户
     */
    @Column(name = "userId")
    private Long userid;

    /**
     * 系统密码 密文
     */
    @Column(name = "sysPwd")
    private String syspwd;

    /**
     * 账户交易密码 密文
     */
    @Column(name = "payPwd")
    private String paypwd;

    /**
     * 状态 normal:正常 freeze:冻结 del:删除
     */
    private String cstatus;

    /**
     * 手势密码
     */
    @Column(name = "gesturePwd")
    private String gesturepwd;

    /**
     * 事由
     */
    @Column(name = "handleCause")
    private String handlecause;

    /**
     * 操作时间
     */
    @Column(name = "handleTime")
    private String handletime;

    /**
     * 操作人Id
     */
    @Column(name = "operId")
    private Long operid;

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
     * 获取用户
     *
     * @return userId - 用户
     */
    public Long getUserid() {
        return userid;
    }

    /**
     * 设置用户
     *
     * @param userid 用户
     */
    public void setUserid(Long userid) {
        this.userid = userid;
    }

    /**
     * 获取系统密码 密文
     *
     * @return sysPwd - 系统密码 密文
     */
    public String getSyspwd() {
        return syspwd;
    }

    /**
     * 设置系统密码 密文
     *
     * @param syspwd 系统密码 密文
     */
    public void setSyspwd(String syspwd) {
        this.syspwd = syspwd;
    }

    /**
     * 获取账户交易密码 密文
     *
     * @return payPwd - 账户交易密码 密文
     */
    public String getPaypwd() {
        return paypwd;
    }

    /**
     * 设置账户交易密码 密文
     *
     * @param paypwd 账户交易密码 密文
     */
    public void setPaypwd(String paypwd) {
        this.paypwd = paypwd;
    }

    /**
     * 获取状态 normal:正常 freeze:冻结 del:删除
     *
     * @return cstatus - 状态 normal:正常 freeze:冻结 del:删除
     */
    public String getCstatus() {
        return cstatus;
    }

    /**
     * 设置状态 normal:正常 freeze:冻结 del:删除
     *
     * @param cstatus 状态 normal:正常 freeze:冻结 del:删除
     */
    public void setCstatus(String cstatus) {
        this.cstatus = cstatus;
    }

    /**
     * 获取手势密码
     *
     * @return gesturePwd - 手势密码
     */
    public String getGesturepwd() {
        return gesturepwd;
    }

    /**
     * 设置手势密码
     *
     * @param gesturepwd 手势密码
     */
    public void setGesturepwd(String gesturepwd) {
        this.gesturepwd = gesturepwd;
    }

    /**
     * 获取事由
     *
     * @return handleCause - 事由
     */
    public String getHandlecause() {
        return handlecause;
    }

    /**
     * 设置事由
     *
     * @param handlecause 事由
     */
    public void setHandlecause(String handlecause) {
        this.handlecause = handlecause;
    }

    /**
     * 获取操作时间
     *
     * @return handleTime - 操作时间
     */
    public String getHandletime() {
        return handletime;
    }

    /**
     * 设置操作时间
     *
     * @param handletime 操作时间
     */
    public void setHandletime(String handletime) {
        this.handletime = handletime;
    }

    /**
     * 获取操作人Id
     *
     * @return operId - 操作人Id
     */
    public Long getOperid() {
        return operid;
    }

    /**
     * 设置操作人Id
     *
     * @param operid 操作人Id
     */
    public void setOperid(Long operid) {
        this.operid = operid;
    }
}