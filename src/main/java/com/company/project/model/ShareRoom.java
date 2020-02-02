package com.company.project.model;

import javax.persistence.*;
import java.util.List;

@Table(name = "tbl_share_room")
public class ShareRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 房间名
     */
    @Column(name = "room_name")
    private String roomName;

    /**
     * 房间地址--第几层 几号房
     */
    @Column(name = "room_addr")
    private String roomAddr;

    /**
     * 房间大小 1- -小  2- -中  3- -大
     */
    @Column(name = "room_size")
    private String roomSize;

    /**
     * 房间类型：会议室 茶室。。。
     */
    @Column(name = "room_type")
    private Integer roomType;

    /**
     * 房间简介
     */
    @Column(name = "room_short_content")
    private String roomShortContent;

    /**
     * 图片
     */
    @Column(name = "room_image")
    private String roomImage;

    /**
     * 开启预定时间
     */
    @Column(name = "room_open_time")
    private String roomOpenTime;

    /**
     * 关闭预定时间
     */
    @Column(name = "room_close_time")
    private String roomCloseTime;

    /**
     * 价格/小时
     */
    @Column(name = "room_price")
    private Integer roomPrice;

    /**
     * 管理员id
     */
    @Column(name = "room_manager")
    private Long roomManager;

    /**
     * 房间状态  1 ：正常  -1：维护
     */
    @Column(name = "room_status")
    private Integer roomStatus;

    /**
     * 大楼编码
     */
    @Column(name = "room_orgcode")
    private String roomOrgcode;

    /**
     * 提前几小时取消
     */
    @Column(name = "room_cancle_hour")
    private Integer roomCancleHour;

    /**
     * 取消预定扣费百分比
     */
    @Column(name = "room_percent")
    private Integer roomPercent;

    /**
     * 进出方式 1 --刷脸 2--二维码
     */
    private Integer mode;

    /**
     * 扩展字段1
     */
    private String ext1;

    /**
     * 扩展字段2
     */
    private String ext2;

    /**
     * 扩展字段3
     */
    private String ext3;


    @Transient
    private List<Org> orgs;
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
     * 获取房间名
     *
     * @return room_name - 房间名
     */
    public String getRoomName() {
        return roomName;
    }

    /**
     * 设置房间名
     *
     * @param roomName 房间名
     */
    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    /**
     * 获取房间地址--第几层 几号房
     *
     * @return room_addr - 房间地址--第几层 几号房
     */
    public String getRoomAddr() {
        return roomAddr;
    }

    /**
     * 设置房间地址--第几层 几号房
     *
     * @param roomAddr 房间地址--第几层 几号房
     */
    public void setRoomAddr(String roomAddr) {
        this.roomAddr = roomAddr;
    }

    /**
     * 获取房间大小 1- -小  2- -中  3- -大
     *
     * @return room_size - 房间大小 1- -小  2- -中  3- -大
     */
    public String getRoomSize() {
        return roomSize;
    }

    /**
     * 设置房间大小 1- -小  2- -中  3- -大
     *
     * @param roomSize 房间大小 1- -小  2- -中  3- -大
     */
    public void setRoomSize(String roomSize) {
        this.roomSize = roomSize;
    }

    /**
     * 获取房间类型：会议室 茶室。。。
     *
     * @return room_type - 房间类型：会议室 茶室。。。
     */
    public Integer getRoomType() {
        return roomType;
    }

    /**
     * 设置房间类型：会议室 茶室。。。
     *
     * @param roomType 房间类型：会议室 茶室。。。
     */
    public void setRoomType(Integer roomType) {
        this.roomType = roomType;
    }

    /**
     * 获取房间简介
     *
     * @return room_short_content - 房间简介
     */
    public String getRoomShortContent() {
        return roomShortContent;
    }

    /**
     * 设置房间简介
     *
     * @param roomShortContent 房间简介
     */
    public void setRoomShortContent(String roomShortContent) {
        this.roomShortContent = roomShortContent;
    }

    /**
     * 获取图片
     *
     * @return room_image - 图片
     */
    public String getRoomImage() {
        return roomImage;
    }

    /**
     * 设置图片
     *
     * @param roomImage 图片
     */
    public void setRoomImage(String roomImage) {
        this.roomImage = roomImage;
    }

    /**
     * 获取开启预定时间
     *
     * @return room_open_time - 开启预定时间
     */
    public String getRoomOpenTime() {
        return roomOpenTime;
    }

    /**
     * 设置开启预定时间
     *
     * @param roomOpenTime 开启预定时间
     */
    public void setRoomOpenTime(String roomOpenTime) {
        this.roomOpenTime = roomOpenTime;
    }

    /**
     * 获取关闭预定时间
     *
     * @return room_close_time - 关闭预定时间
     */
    public String getRoomCloseTime() {
        return roomCloseTime;
    }

    /**
     * 设置关闭预定时间
     *
     * @param roomCloseTime 关闭预定时间
     */
    public void setRoomCloseTime(String roomCloseTime) {
        this.roomCloseTime = roomCloseTime;
    }

    /**
     * 获取价格/小时
     *
     * @return room_price - 价格/小时
     */
    public Integer getRoomPrice() {
        return roomPrice;
    }

    /**
     * 设置价格/小时
     *
     * @param roomPrice 价格/小时
     */
    public void setRoomPrice(Integer roomPrice) {
        this.roomPrice = roomPrice;
    }

    /**
     * 获取管理员id
     *
     * @return room_manager - 管理员id
     */
    public Long getRoomManager() {
        return roomManager;
    }

    /**
     * 设置管理员id
     *
     * @param roomManager 管理员id
     */
    public void setRoomManager(Long roomManager) {
        this.roomManager = roomManager;
    }

    /**
     * 获取房间状态  1 ：正常  -1：维护
     *
     * @return room_status - 房间状态  1 ：正常  -1：维护
     */
    public Integer getRoomStatus() {
        return roomStatus;
    }

    /**
     * 设置房间状态  1 ：正常  -1：维护
     *
     * @param roomStatus 房间状态  1 ：正常  -1：维护
     */
    public void setRoomStatus(Integer roomStatus) {
        this.roomStatus = roomStatus;
    }

    /**
     * 获取大楼编码
     *
     * @return room_orgcode - 大楼编码
     */
    public String getRoomOrgcode() {
        return roomOrgcode;
    }

    /**
     * 设置大楼编码
     *
     * @param roomOrgcode 大楼编码
     */
    public void setRoomOrgcode(String roomOrgcode) {
        this.roomOrgcode = roomOrgcode;
    }

    /**
     * 获取提前几小时取消
     *
     * @return room_cancle_hour - 提前几小时取消
     */
    public Integer getRoomCancleHour() {
        return roomCancleHour;
    }

    /**
     * 设置提前几小时取消
     *
     * @param roomCancleHour 提前几小时取消
     */
    public void setRoomCancleHour(Integer roomCancleHour) {
        this.roomCancleHour = roomCancleHour;
    }

    /**
     * 获取取消预定扣费百分比
     *
     * @return room_percent - 取消预定扣费百分比
     */
    public Integer getRoomPercent() {
        return roomPercent;
    }

    /**
     * 设置取消预定扣费百分比
     *
     * @param roomPercent 取消预定扣费百分比
     */
    public void setRoomPercent(Integer roomPercent) {
        this.roomPercent = roomPercent;
    }

    /**
     * 获取进出方式 1 --刷脸 2--二维码
     *
     * @return mode - 进出方式 1 --刷脸 2--二维码
     */
    public Integer getMode() {
        return mode;
    }

    /**
     * 设置进出方式 1 --刷脸 2--二维码
     *
     * @param mode 进出方式 1 --刷脸 2--二维码
     */
    public void setMode(Integer mode) {
        this.mode = mode;
    }

    /**
     * 获取扩展字段1
     *
     * @return ext1 - 扩展字段1
     */
    public String getExt1() {
        return ext1;
    }

    /**
     * 设置扩展字段1
     *
     * @param ext1 扩展字段1
     */
    public void setExt1(String ext1) {
        this.ext1 = ext1;
    }

    /**
     * 获取扩展字段2
     *
     * @return ext2 - 扩展字段2
     */
    public String getExt2() {
        return ext2;
    }

    /**
     * 设置扩展字段2
     *
     * @param ext2 扩展字段2
     */
    public void setExt2(String ext2) {
        this.ext2 = ext2;
    }

    /**
     * 获取扩展字段3
     *
     * @return ext3 - 扩展字段3
     */
    public String getExt3() {
        return ext3;
    }

    /**
     * 设置扩展字段3
     *
     * @param ext3 扩展字段3
     */
    public void setExt3(String ext3) {
        this.ext3 = ext3;
    }

    public List<Org> getOrgs() {
        return orgs;
    }

    public void setOrgs(List<Org> orgs) {
        this.orgs = orgs;
    }
}