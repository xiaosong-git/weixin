package com.company.project.model;

import javax.persistence.*;

@Table(name = "tbl_visitor_record")
public class VisitRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 发起日期
     */
    @Column(name = "visitDate")
    private String visitdate;

    /**
     * 发起时间
     */
    @Column(name = "visitTime")
    private String visittime;

    /**
     * 访客id
     */
    @Column(name = "userId")
    private Long userid;

    /**
     * 被访者id
     */
    @Column(name = "visitorId")
    private Long visitorid;

    /**
     * 访问原因
     */
    private String reason;

    /**
     * 状态 applying:申请中，applySuccess:接受访问，applyFail:拒绝访问
     */
    private String cstatus;

    /**
     * 日期类型:无期：Indefinite,有限期:limitPeriod
     */
    @Column(name = "dateType")
    private String datetype;

    /**
     * 开始日期
     */
    @Column(name = "startDate")
    private String startdate;

    /**
     * 结束日期
     */
    @Column(name = "endDate")
    private String enddate;

    /**
     * 被访者回复
     */
    @Column(name = "answerContent")
    private String answercontent;

    /**
     * 被访者大楼编码
     */
    @Column(name = "orgCode")
    private String orgcode;

    /**
     * 被访者公司Id
     */
    @Column(name = "companyId")
    private Long companyid;

    /**
     * 1--访问，2--邀约
     */
    @Column(name = "recordType")
    private Integer recordtype;

    /**
     * 审核日期
     */
    @Column(name = "replyDate")
    private String replydate;

    /**
     * 审核时间
     */
    @Column(name = "replyTime")
    private String replytime;

    /**
     * app端还是页端
     */
    private String vitype;

    /**
     * 审核人ID
     */
    @Column(name = "replyUserId")
    private Long replyuserid;

    /**
     * 是否已下发用户 T--是 F--否
     */
    @Column(name = "isReceive")
    private String isreceive;

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
     * 获取发起日期
     *
     * @return visitDate - 发起日期
     */
    public String getVisitdate() {
        return visitdate;
    }

    /**
     * 设置发起日期
     *
     * @param visitdate 发起日期
     */
    public void setVisitdate(String visitdate) {
        this.visitdate = visitdate;
    }

    /**
     * 获取发起时间
     *
     * @return visitTime - 发起时间
     */
    public String getVisittime() {
        return visittime;
    }

    /**
     * 设置发起时间
     *
     * @param visittime 发起时间
     */
    public void setVisittime(String visittime) {
        this.visittime = visittime;
    }

    /**
     * 获取访客id
     *
     * @return userId - 访客id
     */
    public Long getUserid() {
        return userid;
    }

    /**
     * 设置访客id
     *
     * @param userid 访客id
     */
    public void setUserid(Long userid) {
        this.userid = userid;
    }

    /**
     * 获取被访者id
     *
     * @return visitorId - 被访者id
     */
    public Long getVisitorid() {
        return visitorid;
    }

    /**
     * 设置被访者id
     *
     * @param visitorid 被访者id
     */
    public void setVisitorid(Long visitorid) {
        this.visitorid = visitorid;
    }

    /**
     * 获取访问原因
     *
     * @return reason - 访问原因
     */
    public String getReason() {
        return reason;
    }

    /**
     * 设置访问原因
     *
     * @param reason 访问原因
     */
    public void setReason(String reason) {
        this.reason = reason;
    }

    /**
     * 获取状态 applying:申请中，applySuccess:接受访问，applyFail:拒绝访问
     *
     * @return cstatus - 状态 applying:申请中，applySuccess:接受访问，applyFail:拒绝访问
     */
    public String getCstatus() {
        return cstatus;
    }

    /**
     * 设置状态 applying:申请中，applySuccess:接受访问，applyFail:拒绝访问
     *
     * @param cstatus 状态 applying:申请中，applySuccess:接受访问，applyFail:拒绝访问
     */
    public void setCstatus(String cstatus) {
        this.cstatus = cstatus;
    }

    /**
     * 获取日期类型:无期：Indefinite,有限期:limitPeriod
     *
     * @return dateType - 日期类型:无期：Indefinite,有限期:limitPeriod
     */
    public String getDatetype() {
        return datetype;
    }

    /**
     * 设置日期类型:无期：Indefinite,有限期:limitPeriod
     *
     * @param datetype 日期类型:无期：Indefinite,有限期:limitPeriod
     */
    public void setDatetype(String datetype) {
        this.datetype = datetype;
    }

    /**
     * 获取开始日期
     *
     * @return startDate - 开始日期
     */
    public String getStartdate() {
        return startdate;
    }

    /**
     * 设置开始日期
     *
     * @param startdate 开始日期
     */
    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    /**
     * 获取结束日期
     *
     * @return endDate - 结束日期
     */
    public String getEnddate() {
        return enddate;
    }

    /**
     * 设置结束日期
     *
     * @param enddate 结束日期
     */
    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    /**
     * 获取被访者回复
     *
     * @return answerContent - 被访者回复
     */
    public String getAnswercontent() {
        return answercontent;
    }

    /**
     * 设置被访者回复
     *
     * @param answercontent 被访者回复
     */
    public void setAnswercontent(String answercontent) {
        this.answercontent = answercontent;
    }

    /**
     * 获取被访者大楼编码
     *
     * @return orgCode - 被访者大楼编码
     */
    public String getOrgcode() {
        return orgcode;
    }

    /**
     * 设置被访者大楼编码
     *
     * @param orgcode 被访者大楼编码
     */
    public void setOrgcode(String orgcode) {
        this.orgcode = orgcode;
    }

    /**
     * 获取被访者公司Id
     *
     * @return companyId - 被访者公司Id
     */
    public Long getCompanyid() {
        return companyid;
    }

    /**
     * 设置被访者公司Id
     *
     * @param companyid 被访者公司Id
     */
    public void setCompanyid(Long companyid) {
        this.companyid = companyid;
    }

    /**
     * 获取1--访问，2--邀约
     *
     * @return recordType - 1--访问，2--邀约
     */
    public Integer getRecordtype() {
        return recordtype;
    }

    /**
     * 设置1--访问，2--邀约
     *
     * @param recordtype 1--访问，2--邀约
     */
    public void setRecordtype(Integer recordtype) {
        this.recordtype = recordtype;
    }

    /**
     * 获取审核日期
     *
     * @return replyDate - 审核日期
     */
    public String getReplydate() {
        return replydate;
    }

    /**
     * 设置审核日期
     *
     * @param replydate 审核日期
     */
    public void setReplydate(String replydate) {
        this.replydate = replydate;
    }

    /**
     * 获取审核时间
     *
     * @return replyTime - 审核时间
     */
    public String getReplytime() {
        return replytime;
    }

    /**
     * 设置审核时间
     *
     * @param replytime 审核时间
     */
    public void setReplytime(String replytime) {
        this.replytime = replytime;
    }

    /**
     * 获取app端还是页端
     *
     * @return vitype - app端还是页端
     */
    public String getVitype() {
        return vitype;
    }

    /**
     * 设置app端还是页端
     *
     * @param vitype app端还是页端
     */
    public void setVitype(String vitype) {
        this.vitype = vitype;
    }

    /**
     * 获取审核人ID
     *
     * @return replyUserId - 审核人ID
     */
    public Long getReplyuserid() {
        return replyuserid;
    }

    /**
     * 设置审核人ID
     *
     * @param replyuserid 审核人ID
     */
    public void setReplyuserid(Long replyuserid) {
        this.replyuserid = replyuserid;
    }

    /**
     * 获取是否已下发用户 T--是 F--否
     *
     * @return isReceive - 是否已下发用户 T--是 F--否
     */
    public String getIsreceive() {
        return isreceive;
    }

    /**
     * 设置是否已下发用户 T--是 F--否
     *
     * @param isreceive 是否已下发用户 T--是 F--否
     */
    public void setIsreceive(String isreceive) {
        this.isreceive = isreceive;
    }
}