package com.company.project.weixin.model;

/**
 * @program: weixin
 * @description: 微信模板消息
 * @author: cwf
 * @create: 2019-10-21 09:31
 **/
public class WxTemplateData {
    private String value;
    private String color;
    public WxTemplateData() {

    }

    public WxTemplateData(String value, String color) {
        this.value = value;
        this.color = color;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }


    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
