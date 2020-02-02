package com.company.project.model;

import javax.persistence.*;

@Table(name = "`tbl_key`")
public class Key {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 密钥的key
     */
    @Column(name = "workKey")
    private String workkey;

    /**
     * 状态，normal正常，disable:禁用
     */
    private String cstatus;

    /**
     * 创建时间
     */
    @Column(name = "createDate")
    private String createdate;

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
     * 获取密钥的key
     *
     * @return workKey - 密钥的key
     */
    public String getWorkkey() {
        return workkey;
    }

    /**
     * 设置密钥的key
     *
     * @param workkey 密钥的key
     */
    public void setWorkkey(String workkey) {
        this.workkey = workkey;
    }

    /**
     * 获取状态，normal正常，disable:禁用
     *
     * @return cstatus - 状态，normal正常，disable:禁用
     */
    public String getCstatus() {
        return cstatus;
    }

    /**
     * 设置状态，normal正常，disable:禁用
     *
     * @param cstatus 状态，normal正常，disable:禁用
     */
    public void setCstatus(String cstatus) {
        this.cstatus = cstatus;
    }

    /**
     * 获取创建时间
     *
     * @return createDate - 创建时间
     */
    public String getCreatedate() {
        return createdate;
    }

    /**
     * 设置创建时间
     *
     * @param createdate 创建时间
     */
    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }
}