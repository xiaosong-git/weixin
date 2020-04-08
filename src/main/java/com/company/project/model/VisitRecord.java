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
    private String visitDate;

    /**
     * 发起时间
     */
    @Column(name = "visitTime")
    private String visitTime;

    /**
     * 访客id
     */
    @Column(name = "userId")
    private Long userId;

    /**
     * 被访者id
     */
    @Column(name = "visitorId")
    private Long visitorId;

    /**
     * 访问事由
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
    private String dateType;

    /**
     * 开始日期
     */
    @Column(name = "startDate")
    private String startDate;

    /**
     * 结束日期
     */
    @Column(name = "endDate")
    private String endDate;

    /**
     * 被访者回复
     */
    @Column(name = "answerContent")
    private String answerContent;

    /**
     * 被访者大楼编码
     */
    @Column(name = "orgCode")
    private String orgCode;

    /**
     * 被访者公司Id
     */
    @Column(name = "companyId")
    private Long companyId;

    /**
     * 1--访问，2--邀约
     */
    @Column(name = "recordType")
    private Integer recordType;

    /**
     * 审核日期
     */
    @Column(name = "replyDate")
    private String replyDate;

    /**
     * 审核时间
     */
    @Column(name = "replyTime")
    private String replyTime;

    /**
     * app端还是页端
     */
    private String vitype;

    /**
     * 审核人ID
     */
    @Column(name = "replyUserId")
    private Long replyUserId;

    /**
     * 是否已下发用户 T--是 F--否
     */
    @Column(name = "isReceive")
    private String isReceive;
    @Transient
    private Company company;
    @Transient
    private Org org;
    @Transient
    private String vstatus;
    @Transient
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(String visitDate) {
        this.visitDate = visitDate;
    }

    public String getVisitTime() {
        return visitTime;
    }

    public void setVisitTime(String visitTime) {
        this.visitTime = visitTime;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getVisitorId() {
        return visitorId;
    }

    public void setVisitorId(Long visitorId) {
        this.visitorId = visitorId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getCstatus() {
        return cstatus;
    }

    public void setCstatus(String cstatus) {
        this.cstatus = cstatus;
    }

    public String getDateType() {
        return dateType;
    }

    public void setDateType(String dateType) {
        this.dateType = dateType;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getAnswerContent() {
        return answerContent;
    }

    public void setAnswerContent(String answerContent) {
        this.answerContent = answerContent;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Integer getRecordType() {
        return recordType;
    }

    public void setRecordType(Integer recordType) {
        this.recordType = recordType;
    }

    public String getReplyDate() {
        return replyDate;
    }

    public void setReplyDate(String replyDate) {
        this.replyDate = replyDate;
    }

    public String getReplyTime() {
        return replyTime;
    }

    public void setReplyTime(String replyTime) {
        this.replyTime = replyTime;
    }

    public String getVitype() {
        return vitype;
    }

    public void setVitype(String vitype) {
        this.vitype = vitype;
    }

    public Long getReplyUserId() {
        return replyUserId;
    }

    public void setReplyUserId(Long replyUserId) {
        this.replyUserId = replyUserId;
    }

    public String getIsReceive() {
        return isReceive;
    }

    public void setIsReceive(String isReceive) {
        this.isReceive = isReceive;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public String getVstatus() {
        return vstatus;
    }

    public void setVstatus(String vstatus) {
        this.vstatus = vstatus;
    }

    public Org getOrg() {
        return org;
    }

    public void setOrg(Org org) {
        this.org = org;
    }
}