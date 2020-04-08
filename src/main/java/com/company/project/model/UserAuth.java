package com.company.project.model;

import javax.persistence.*;

@Table(name = "tbl_user_auth")
public class UserAuth {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户id
     */
    @Column(name = "userId")
    private Long userid;

    /**
     * 用户真实姓名
     */
    @Column(name = "realName")
    private String realname;

    /**
     * 身份证号
     */
    @Column(name = "idNo")
    private String idno;

    /**
     * 手持证件照
     */
    @Column(name = "idHandleImgUrl")
    private String idhandleimgurl;

    /**
     * 实人日期时间 yyyy-MM-dd hhmmss
     */
    @Column(name = "authDate")
    private String authdate;

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
     * 获取用户id
     *
     * @return userId - 用户id
     */
    public Long getUserid() {
        return userid;
    }

    /**
     * 设置用户id
     *
     * @param userid 用户id
     */
    public void setUserid(Long userid) {
        this.userid = userid;
    }

    /**
     * 获取用户真实姓名
     *
     * @return realName - 用户真实姓名
     */
    public String getRealname() {
        return realname;
    }

    /**
     * 设置用户真实姓名
     *
     * @param realname 用户真实姓名
     */
    public void setRealname(String realname) {
        this.realname = realname;
    }

    /**
     * 获取身份证号
     *
     * @return idNo - 身份证号
     */
    public String getIdno() {
        return idno;
    }

    /**
     * 设置身份证号
     *
     * @param idno 身份证号
     */
    public void setIdno(String idno) {
        this.idno = idno;
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
     * 获取实人日期时间 yyyy-MM-dd hhmmss
     *
     * @return authDate - 实人日期时间 yyyy-MM-dd hhmmss
     */
    public String getAuthdate() {
        return authdate;
    }

    /**
     * 设置实人日期时间 yyyy-MM-dd hhmmss
     *
     * @param authdate 实人日期时间 yyyy-MM-dd hhmmss
     */
    public void setAuthdate(String authdate) {
        this.authdate = authdate;
    }
}