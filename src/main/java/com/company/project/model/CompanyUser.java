package com.company.project.model;

import javax.persistence.*;
import java.util.List;

@Table(name = "tbl_company_user")
public class CompanyUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 公司id
     */
    @Column(name = "companyId")
    private Long companyid;

    /**
     * 部门id
     */
    @Column(name = "sectionId")
    private Long sectionid;

    /**
     * 用户Id
     */
    @Column(name = "userId")
    private Long userid;

    @Column(name = "postId")
    private Long postid;

    /**
     * 用户姓名
     */
    @Column(name = "userName")
    private String username;

    /**
     * 创建日期yy:MM:dd
     */
    @Column(name = "createDate")
    private String createdate;

    /**
     * 创建时间HH:mm:ss
     */
    @Column(name = "createTime")
    private String createtime;

    /**
     * 角色:(staff:普通员工,manage:管理员,front:前台)
     */
    @Column(name = "roleType")
    private String roletype;

    /**
     * 状态：确认:applySuc/未确认:applying/确认不通过:applyFail
     */
    private String status;

    /**
     * normal为正常，deleted为删除
     */
    @Column(name = "currentStatus")
    private String currentstatus;

    private String sex;

    /**
     * 是否涉密0可以被访问1不可访问
     */
    private String secucode;

    /**
     * 授权类型0为自己授权1不可授权2为本部门授权3全体公司授权
     */
    private String authtype;
    /**
     * 公司
     */
    @Transient
    private Company company;
    /**
     * 大楼
     */
    @Transient
    private Org  org;

    /**
     * 用户
     */
    @Transient
    private User  users;

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
     * 获取公司id
     *
     * @return companyId - 公司id
     */
    public Long getCompanyid() {
        return companyid;
    }

    /**
     * 设置公司id
     *
     * @param companyid 公司id
     */
    public void setCompanyid(Long companyid) {
        this.companyid = companyid;
    }

    /**
     * 获取部门id
     *
     * @return sectionId - 部门id
     */
    public Long getSectionid() {
        return sectionid;
    }

    /**
     * 设置部门id
     *
     * @param sectionid 部门id
     */
    public void setSectionid(Long sectionid) {
        this.sectionid = sectionid;
    }

    /**
     * 获取用户Id
     *
     * @return userId - 用户Id
     */
    public Long getUserid() {
        return userid;
    }

    /**
     * 设置用户Id
     *
     * @param userid 用户Id
     */
    public void setUserid(Long userid) {
        this.userid = userid;
    }

    /**
     * @return postId
     */
    public Long getPostid() {
        return postid;
    }

    /**
     * @param postid
     */
    public void setPostid(Long postid) {
        this.postid = postid;
    }

    /**
     * 获取用户姓名
     *
     * @return userName - 用户姓名
     */
    public String getUsername() {
        return username;
    }

    /**
     * 设置用户姓名
     *
     * @param username 用户姓名
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * 获取创建日期yy:MM:dd
     *
     * @return createDate - 创建日期yy:MM:dd
     */
    public String getCreatedate() {
        return createdate;
    }

    /**
     * 设置创建日期yy:MM:dd
     *
     * @param createdate 创建日期yy:MM:dd
     */
    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }

    /**
     * 获取创建时间HH:mm:ss
     *
     * @return createTime - 创建时间HH:mm:ss
     */
    public String getCreatetime() {
        return createtime;
    }

    /**
     * 设置创建时间HH:mm:ss
     *
     * @param createtime 创建时间HH:mm:ss
     */
    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    /**
     * 获取角色:(staff:普通员工,manage:管理员,front:前台)
     *
     * @return roleType - 角色:(staff:普通员工,manage:管理员,front:前台)
     */
    public String getRoletype() {
        return roletype;
    }

    /**
     * 设置角色:(staff:普通员工,manage:管理员,front:前台)
     *
     * @param roletype 角色:(staff:普通员工,manage:管理员,front:前台)
     */
    public void setRoletype(String roletype) {
        this.roletype = roletype;
    }

    /**
     * 获取状态：确认:applySuc/未确认:applying/确认不通过:applyFail
     *
     * @return status - 状态：确认:applySuc/未确认:applying/确认不通过:applyFail
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置状态：确认:applySuc/未确认:applying/确认不通过:applyFail
     *
     * @param status 状态：确认:applySuc/未确认:applying/确认不通过:applyFail
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 获取normal为正常，deleted为删除
     *
     * @return currentStatus - normal为正常，deleted为删除
     */
    public String getCurrentstatus() {
        return currentstatus;
    }

    /**
     * 设置normal为正常，deleted为删除
     *
     * @param currentstatus normal为正常，deleted为删除
     */
    public void setCurrentstatus(String currentstatus) {
        this.currentstatus = currentstatus;
    }

    /**
     * @return sex
     */
    public String getSex() {
        return sex;
    }

    /**
     * @param sex
     */
    public void setSex(String sex) {
        this.sex = sex;
    }

    /**
     * 获取是否涉密0可以被访问1不可访问
     *
     * @return secucode - 是否涉密0可以被访问1不可访问
     */
    public String getSecucode() {
        return secucode;
    }

    /**
     * 设置是否涉密0可以被访问1不可访问
     *
     * @param secucode 是否涉密0可以被访问1不可访问
     */
    public void setSecucode(String secucode) {
        this.secucode = secucode;
    }
    /**
     * 获取授权类型0为自己授权1不可授权2为本部门授权3全体公司授权
     *
     * @return authtype - 授权类型0为自己授权1不可授权2为本部门授权3全体公司授权
     */
    public String getAuthtype() {
        return authtype;
    }
    /**
     * 设置授权类型0为自己授权1不可授权2为本部门授权3全体公司授权
     *
     * @param authtype 授权类型0为自己授权1不可授权2为本部门授权3全体公司授权
     */
    public void setAuthtype(String authtype) {
        this.authtype = authtype;
    }
    public Company getCompany() {
        return company;
    }
    public void setCompany(Company company) {
        this.company = company;
    }
    public Org getOrg() {
        return org;
    }

    public void setOrg(Org org) {
        this.org = org;
    }


    public User getUsers() {
        return users;
    }

    public void setUsers(User users) {
        this.users = users;
    }
}