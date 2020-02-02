package com.company.project.model;

import javax.persistence.*;
import java.util.List;

@Table(name = "tbl_company")
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 公司编码
     */
    @Column(name = "companyCode")
    private String companycode;

    /**
     * 公司名
     */
    @Column(name = "companyName")
    private String companyname;

    /**
     * 创建日期yyyy-mm-dd
     */
    @Column(name = "createDate")
    private String createdate;

    /**
     * 创建时间HH:mm:ss
     */
    @Column(name = "createTime")
    private String createtime;

    /**
     * 联系人手机号
     */
    private String phone;

    /**
     * 联系人
     */
    private String name;

    /**
     * 审核类型:(manage:管理员审核,front:前台审核,staff:员工自己审核)
     */
    @Column(name = "applyType")
    private String applytype;

    /**
     * 法人身份证
     */
    @Column(name = "corporationID")
    private String corporationid;

    /**
     * 营业执照号
     */
    @Column(name = "licenceNo")
    private String licenceno;

    /**
     * 公司地址
     */
    private String addr;

    /**
     * 大楼id
     */
    @Column(name = "orgId")
    private Long orgid;

    @Column(name = "companyFloor")
    private String companyfloor;

    @Column(name = "companyFloor2")
    private String companyfloor2;
    /**
     * 大楼
     */
    @Transient
    private List<Org> org;

    /**
     * 用户
     */
    @Transient
    private List<User>  users;
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
     * 获取公司编码
     *
     * @return companyCode - 公司编码
     */
    public String getCompanycode() {
        return companycode;
    }

    /**
     * 设置公司编码
     *
     * @param companycode 公司编码
     */
    public void setCompanycode(String companycode) {
        this.companycode = companycode;
    }

    /**
     * 获取公司名
     *
     * @return companyName - 公司名
     */
    public String getCompanyname() {
        return companyname;
    }

    /**
     * 设置公司名
     *
     * @param companyname 公司名
     */
    public void setCompanyname(String companyname) {
        this.companyname = companyname;
    }

    /**
     * 获取创建日期yyyy-mm-dd
     *
     * @return createDate - 创建日期yyyy-mm-dd
     */
    public String getCreatedate() {
        return createdate;
    }

    /**
     * 设置创建日期yyyy-mm-dd
     *
     * @param createdate 创建日期yyyy-mm-dd
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
     * 获取联系人手机号
     *
     * @return phone - 联系人手机号
     */
    public String getPhone() {
        return phone;
    }

    /**
     * 设置联系人手机号
     *
     * @param phone 联系人手机号
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * 获取联系人
     *
     * @return name - 联系人
     */
    public String getName() {
        return name;
    }

    /**
     * 设置联系人
     *
     * @param name 联系人
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取审核类型:(manage:管理员审核,front:前台审核,staff:员工自己审核)
     *
     * @return applyType - 审核类型:(manage:管理员审核,front:前台审核,staff:员工自己审核)
     */
    public String getApplytype() {
        return applytype;
    }

    /**
     * 设置审核类型:(manage:管理员审核,front:前台审核,staff:员工自己审核)
     *
     * @param applytype 审核类型:(manage:管理员审核,front:前台审核,staff:员工自己审核)
     */
    public void setApplytype(String applytype) {
        this.applytype = applytype;
    }

    /**
     * 获取法人身份证
     *
     * @return corporationID - 法人身份证
     */
    public String getCorporationid() {
        return corporationid;
    }

    /**
     * 设置法人身份证
     *
     * @param corporationid 法人身份证
     */
    public void setCorporationid(String corporationid) {
        this.corporationid = corporationid;
    }

    /**
     * 获取营业执照号
     *
     * @return licenceNo - 营业执照号
     */
    public String getLicenceno() {
        return licenceno;
    }

    /**
     * 设置营业执照号
     *
     * @param licenceno 营业执照号
     */
    public void setLicenceno(String licenceno) {
        this.licenceno = licenceno;
    }

    /**
     * 获取公司地址
     *
     * @return addr - 公司地址
     */
    public String getAddr() {
        return addr;
    }

    /**
     * 设置公司地址
     *
     * @param addr 公司地址
     */
    public void setAddr(String addr) {
        this.addr = addr;
    }

    /**
     * 获取大楼id
     *
     * @return orgId - 大楼id
     */
    public Long getOrgid() {
        return orgid;
    }

    /**
     * 设置大楼id
     *
     * @param orgid 大楼id
     */
    public void setOrgid(Long orgid) {
        this.orgid = orgid;
    }

    /**
     * @return companyFloor
     */
    public String getCompanyfloor() {
        return companyfloor;
    }

    /**
     * @param companyfloor
     */
    public void setCompanyfloor(String companyfloor) {
        this.companyfloor = companyfloor;
    }

    /**
     * @return companyFloor2
     */
    public String getCompanyfloor2() {
        return companyfloor2;
    }

    /**
     * @param companyfloor2
     */
    public void setCompanyfloor2(String companyfloor2) {
        this.companyfloor2 = companyfloor2;
    }

    public List<Org> getOrg() {
        return org;
    }

    public void setOrg(List<Org> org) {
        this.org = org;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}