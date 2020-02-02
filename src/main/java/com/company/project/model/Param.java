package com.company.project.model;

import javax.persistence.*;

@Table(name = "`tbl_params`")
public class Param {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "paramName")
    private String paramname;

    @Column(name = "paramText")
    private String paramtext;

    /**
     * ˵
     */
    private String remark;

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
     * @return paramName
     */
    public String getParamname() {
        return paramname;
    }

    /**
     * @param paramname
     */
    public void setParamname(String paramname) {
        this.paramname = paramname;
    }

    /**
     * @return paramText
     */
    public String getParamtext() {
        return paramtext;
    }

    /**
     * @param paramtext
     */
    public void setParamtext(String paramtext) {
        this.paramtext = paramtext;
    }

    /**
     * 获取˵
     *
     * @return remark - ˵
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置˵
     *
     * @param remark ˵
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }
}