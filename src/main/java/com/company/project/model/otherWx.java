package com.company.project.model;

import lombok.Data;

import java.util.Date;
import javax.persistence.*;

@Table(name = "tbl_other_wx")
@Data
public class otherWx {
    /**
     * id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 微信号
     */
    @Column(name = "wx_value")
    private String wxValue;

    /**
     * 微信名
     */
    @Column(name = "wx_name")
    private String wxName;

    /**
     * 接口地址
     */
    @Column(name = "template")
    private String template;

    /**
     * 创建时间
     */
    @Column(name = "creat_time")
    private Date creatTime;

    /**
     * appid
     */
    private String appid;

    /**
     * 秘钥
     */
    private String secret;
    /**
     * 临时数据
     */
    private String ext1;
    private String ext2;
    private String ext3;



}