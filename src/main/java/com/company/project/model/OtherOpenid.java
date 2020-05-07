package com.company.project.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "tbl_other_openid")
public class OtherOpenid {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户id
     */
    @Column(name = "user_id")
    private Long userId;

    /**
     * wx_id
     */
    @Column(name = "wx_id")
    private Long wxId;

    /**
     * openId
     */
    @Column(name = "open_id")
    private String openId;

    /**
     * 生成时间
     */
    @Column(name = "creat_time")
    private Date creatTime;

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

    /**
     * 获取wx_id
     *
     * @return wx_id - wx_id
     */
    public Long getWxId() {
        return wxId;
    }

    /**
     * 设置wx_id
     *
     * @param wxId wx_id
     */
    public void setWxId(Long wxId) {
        this.wxId = wxId;
    }

    /**
     * 获取openId
     *
     * @return open_id - openId
     */
    public String getOpenId() {
        return openId;
    }

    /**
     * 设置openId
     *
     * @param openId openId
     */
    public void setOpenId(String openId) {
        this.openId = openId;
    }

    /**
     * 获取生成时间
     *
     * @return creat_time - 生成时间
     */
    public Date getCreatTime() {
        return creatTime;
    }

    /**
     * 设置生成时间
     *
     * @param creatTime 生成时间
     */
    public void setCreatTime(Date creatTime) {
        this.creatTime = creatTime;
    }
}