package com.company.project.weixin;

import com.soecode.wxtools.api.IService;
import com.soecode.wxtools.api.WxConsts;
import com.soecode.wxtools.api.WxService;
import com.soecode.wxtools.bean.WxMenu;
import com.soecode.wxtools.bean.result.WxUserTagResult;
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


        //按钮一
        WxMenu.WxMenuButton btn1 = new WxMenu.WxMenuButton();
        btn1.setName("访问");
        btn1.setUrl(URL+MenuKey.VISIT);
        btn1.setType(WxConsts.MENU_BUTTON_VIEW);
        List<WxMenu.WxMenuButton> subList3 = new ArrayList<>();
        WxMenu.WxMenuButton btn2=new WxMenu.WxMenuButton();
        btn2.setName("邀约");
        btn2.setUrl(URL+MenuKey.INVITE);
        btn2.setType(WxConsts.MENU_BUTTON_VIEW);
        WxMenu.WxMenuButton btn3 = new WxMenu.WxMenuButton();
        btn3.setName("我的");
        WxMenu.WxMenuButton btn3_1 = new WxMenu.WxMenuButton();
        btn3_1.setType(WxConsts.MENU_BUTTON_VIEW);
        btn3_1.setName("记录");
        btn3_1.setUrl(URL+MenuKey.FIRST_RECORD);
        WxMenu.WxMenuButton btn3_3 = new WxMenu.WxMenuButton();
        btn3_3.setType(WxConsts.MENU_BUTTON_VIEW);
        btn3_3.setUrl(URL+"auth2");
        btn3_3.setName("实名认证");

        WxMenu.WxMenuButton btn3_4 = new WxMenu.WxMenuButton();
        btn3_4.setType(WxConsts.MENU_BUTTON_VIEW);
        btn3_4.setUrl(URL+MenuKey.BINDPHONE);
        btn3_4.setName("绑定手机");

        WxMenu.WxMenuButton btn3_2 = new WxMenu.WxMenuButton();
        btn3_2.setType(WxConsts.MENU_BUTTON_VIEW);
        btn3_2.setName("清除缓存");
        btn3_2.setUrl(URL+"clear");

//        String url=null;
//        try {
//            //通过公众号访问地址授权
//            url = iService.oauth2buildAuthorizationUrl(URL + "login", "snsapi_userinfo", "233");
//        }catch (WxErrorException w){
//            w.getStackTrace();
//        }
//        WxMenu.WxMenuButton btn3_4 = new WxMenu.WxMenuButton();
//        btn3_4.setType(WxConsts.MENU_BUTTON_VIEW);
//        btn3_4.setUrl(url);
//        //授权登入
//        btn3_4.setName("登入");
        //子按钮加入列表
        subList3.addAll(Arrays.asList(btn3_1,btn3_3,btn3_4,btn3_2));
        //子按钮绑定父按钮
        btn3.setSub_button(subList3);
        List<WxMenu.WxMenuButton> btnList = new ArrayList<>();
        //将三个按钮设置进btnList
        btnList.add(btn1);
        btnList.add(btn2);
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
    public static void checkMenu() throws WxErrorException {
        IService iService = new WxService();
        try {
            WxUserTagResult wxUserTagResult = iService.queryAllUserTag();
            List<WxUserTagResult.WxUserTag> tags = wxUserTagResult.getTags();
            for (WxUserTagResult.WxUserTag tag : tags) {
                System.out.println("-----tag_id:"+tag.getId());
            }
            String s = iService.menuTryMatch("otlyluFmKy3-oThjxdBYGQj2hzLI");
            System.out.println(s);
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws WxErrorException {
        creatMenu();
//        checkMenu();

    }

}
