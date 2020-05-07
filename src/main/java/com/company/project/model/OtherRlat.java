package com.company.project.model;

import javax.persistence.*;

@Table(name = "tbl_other_rlat")
public class OtherRlat {
    /**
     * 公众号与openId关系表
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 微信id
     */
    @Column(name = "wx_id")
    private Long wxId;

    /**
     * 用户id
     */
    @Column(name = "user_id")
    private Long userId;

    /**
     * 获取公众号与openId关系表
     *
     * @return id - 公众号与openId关系表
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置公众号与openId关系表
     *
     * @param id 公众号与openId关系表
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取微信id
     *
     * @return wx_id - 微信id
     */
    public Long getWxId() {
        return wxId;
    }

    /**
     * 设置微信id
     *
     * @param wxId 微信id
     */
    public void setWxId(Long wxId) {
        this.wxId = wxId;
    }

    /**
     * 获取用户id
     *
     * @return user_id - 用户id
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * 设置用户id
     *
     * @param userId 用户id
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }
}