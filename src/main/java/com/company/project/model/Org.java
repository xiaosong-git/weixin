package com.company.project.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;

import javax.persistence.*;

@Table(name = "`t_org`")
public class Org {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "org_code")
    private String orgCode;

    @Column(name = "org_name")
    private String orgName;

    /**
     * 父id
     */
    private Long sid;

    private String istop;

    @Column(name = "org_icon")
    private String orgIcon;

    @Column(name = "relation_no")
    private String relationNo;

    /**
     * 0:正常；1:禁止
     */
    private String sstatus;

    /**
     * 大楼:floor,物业:property，运营商:business;0:超级管理员
     */
    @Column(name = "orgType")
    private String orgtype;

    @Column(name = "realName")
    private String realname;

    private String phone;

    private String addr;

    @Column(name = "createDate")
    private String createdate;

    /**
     * 省
     */
    private String province;

    /**
     * 市区
     */
    private String city;

    /**
     * 县
     */
    private String area;

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
     * @return org_code
     */
    public String getOrgCode() {
        return orgCode;
    }

    /**
     * @param orgCode
     */
    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    /**
     * @return org_name
     */
    public String getOrgName() {
        return orgName;
    }

    /**
     * @param orgName
     */
    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    /**
     * 获取父id
     *
     * @return sid - 父id
     */
    public Long getSid() {
        return sid;
    }

    /**
     * 设置父id
     *
     * @param sid 父id
     */
    public void setSid(Long sid) {
        this.sid = sid;
    }

    /**
     * @return istop
     */
    public String getIstop() {
        return istop;
    }

    /**
     * @param istop
     */
    public void setIstop(String istop) {
        this.istop = istop;
    }

    /**
     * @return org_icon
     */
    public String getOrgIcon() {
        return orgIcon;
    }

    /**
     * @param orgIcon
     */
    public void setOrgIcon(String orgIcon) {
        this.orgIcon = orgIcon;
    }

    /**
     * @return relation_no
     */
    public String getRelationNo() {
        return relationNo;
    }

    /**
     * @param relationNo
     */
    public void setRelationNo(String relationNo) {
        this.relationNo = relationNo;
    }

    /**
     * 获取0:正常；1:禁止
     *
     * @return sstatus - 0:正常；1:禁止
     */
    public String getSstatus() {
        return sstatus;
    }

    /**
     * 设置0:正常；1:禁止
     *
     * @param sstatus 0:正常；1:禁止
     */
    public void setSstatus(String sstatus) {
        this.sstatus = sstatus;
    }

    /**
     * 获取大楼:floor,物业:property，运营商:business;0:超级管理员
     *
     * @return orgType - 大楼:floor,物业:property，运营商:business;0:超级管理员
     */
    public String getOrgtype() {
        return orgtype;
    }

    /**
     * 设置大楼:floor,物业:property，运营商:business;0:超级管理员
     *
     * @param orgtype 大楼:floor,物业:property，运营商:business;0:超级管理员
     */
    public void setOrgtype(String orgtype) {
        this.orgtype = orgtype;
    }

    /**
     * @return realName
     */
    public String getRealname() {
        return realname;
    }

    /**
     * @param realname
     */
    public void setRealname(String realname) {
        this.realname = realname;
    }

    /**
     * @return phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * @return addr
     */
    public String getAddr() {
        return addr;
    }

    /**
     * @param addr
     */
    public void setAddr(String addr) {
        this.addr = addr;
    }

    /**
     * @return createDate
     */
    public String getCreatedate() {
        return createdate;
    }

    /**
     * @param createdate
     */
    public void setCreatedate(String createdate) {
        this.createdate = createdate;
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
     * 获取市区
     *
     * @return city - 市区
     */
    public String getCity() {
        return city;
    }

    /**
     * 设置市区
     *
     * @param city 市区
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
}