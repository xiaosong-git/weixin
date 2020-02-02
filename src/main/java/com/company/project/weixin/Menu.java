package com.company.project.weixin;

import com.soecode.wxtools.api.IService;
import com.soecode.wxtools.api.WxConsts;
import com.soecode.wxtools.api.WxService;
import com.soecode.wxtools.bean.WxMenu;
import com.soecode.wxtools.exception.WxErrorException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.company.project.weixin.MenuKey.URL;

/**
 * @program: weixin
 * @description: 菜单
 * @author: cwf
 * @create: 2019-09-20 10:38
 **/
public class Menu {


    public static void creatMenu(){
        IService iService = new WxService();
        WxMenu menu = new WxMenu();
        List<WxMenu.WxMenuButton> btnList = new ArrayList<>();

        //按钮一
        WxMenu.WxMenuButton btn1 = new WxMenu.WxMenuButton();
        btn1.setName("发起访问申请");
//        List<WxMenu.WxMenuButton> subList1 = new ArrayList<>();
        List<WxMenu.WxMenuButton> subList2 = new ArrayList<>();
        List<WxMenu.WxMenuButton> subList3 = new ArrayList<>();

//        WxMenu.WxMenuButton btn1_1 = new WxMenu.WxMenuButton();
//        btn1_1.setType(WxConsts.MENU_BUTTON_CLICK);
//        btn1_1.setKey(MenuKey.INVITE);
//        btn1_1.setName("发起邀约申请");
//        WxMenu.WxMenuButton btn2 = new WxMenu.WxMenuButton();
//        btn2.setName("共享申请");
//        WxMenu.WxMenuButton btn2_1=new WxMenu.WxMenuButton();
//        btn2_1.setType(WxConsts.MENU_BUTTON_CLICK);
//        btn2_1.setKey(MenuKey.MEETING);
//        btn2_1.setName("会议室申请");
//        WxMenu.WxMenuButton btn2_2=new WxMenu.WxMenuButton();
//        btn2_2.setType(WxConsts.MENU_BUTTON_CLICK);
//        btn2_2.setKey(MenuKey.TEA);
//        btn2_2.setName("茶室申请");
        WxMenu.WxMenuButton btn3 = new WxMenu.WxMenuButton();
        btn3.setName("我的");
        WxMenu.WxMenuButton btn3_1 = new WxMenu.WxMenuButton();
        btn3_1.setType(WxConsts.MENU_BUTTON_CLICK);
        btn3_1.setKey(MenuKey.INVITE_RECORD);
        btn3_1.setName("访客记录");
//        WxMenu.WxMenuButton btn3_2 = new WxMenu.WxMenuButton();
//        btn3_2.setType(WxConsts.MENU_BUTTON_CLICK);
//        btn3_2.setKey(MenuKey.SHARE_RECORD);
//        btn3_2.setName("共享记录");
//        WxMenu.WxMenuButton btn3_3 = new WxMenu.WxMenuButton();
//        btn3_3.setType(WxConsts.MENU_BUTTON_VIEW);
//        btn3_3.setUrl(URL+"auth");
//        btn3_3.setName("实名认证");
        String url=null;

//        WxMenu.WxMenuButton btn1_2 = new WxMenu.WxMenuButton();
        btn1.setType(WxConsts.MENU_BUTTON_VIEW);

//        btn1_2.setName("发起访问申请");
        try {
            btn1.setUrl(URL+MenuKey.VISIT);
            //通过公众号访问地址授权
            url = iService.oauth2buildAuthorizationUrl(URL + "login", "snsapi_userinfo", "233");
        }catch (WxErrorException w){
            w.getStackTrace();
        }
        WxMenu.WxMenuButton btn3_4 = new WxMenu.WxMenuButton();
        btn3_4.setType(WxConsts.MENU_BUTTON_VIEW);
        btn3_4.setUrl(url);
        //授权登入
        btn3_4.setName("登入");
        //子按钮加入列表
//        subList1.addAll(Arrays.asList(btn1_1, btn1_2));
//        subList1.addAll(Arrays.asList( btn1_2));
//        subList2.addAll(Arrays.asList(btn2_1, btn2_2));
        subList3.addAll(Arrays.asList(btn3_1,btn3_4));
        //子按钮绑定父按钮
//        btn1.setSub_button(subList1);
//        btn2.setSub_button(subList2);
        btn3.setSub_button(subList3);

        //将三个按钮设置进btnList
        btnList.add(btn1);
//        btnList.add(btn2);
        btnList.add(btn3);
        //设置进菜单类
        menu.setButton(btnList);
        //调用API即可
        try {
            //参数1--menu  ，参数2--是否是个性化定制。如果是个性化菜单栏，需要设置MenuRule
            iService.createMenu(menu, false);
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        creatMenu();

    }

}
