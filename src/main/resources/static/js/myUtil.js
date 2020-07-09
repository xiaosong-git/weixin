let IS_DEVELOP = false;//是否生产环境
let uri;
let url;
let appId = "wx1ddcdc86c83bc9a1";
if (IS_DEVELOP) {//生产环境
    uri = "f.pyblkj.cn";
    url = "http://" + uri + "/";
} else {
    appId = "wx2a1951f46acc4371";
    uri = "fager.pyblkj.cn";
    url = "http://" + uri + "/";
}

function getLoginUrl(state) {
    console.log(state);
    if (IS_DEVELOP) {
        //重定向地址
        return "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + appId + "&redirect_uri=http%3A%2F%2F" + uri + "%2Fpybl%2Flogin&response_type=code&scope=snsapi_base&state=" + state + "#wechat_redirect";
    } else {
        return "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + appId + "&redirect_uri=http%3A%2F%2F" + uri + "%2Fpybl%2Flogin&response_type=code&scope=snsapi_base&state=" + state + "#wechat_redirect";
    }
}

//朋悦比邻
let suffix="pybl/";
let visitUrl=url+suffix+"visit";
let authUrl=url+suffix+"auth";
let auth2Url=url+suffix+"auth2";
let auth3Url=url+suffix+"auth3";
let invitUrl=url+suffix+"invite";
let firstRecordUrl=url+suffix+"firstrecord";
let secondRecordUrl=url+suffix+"secondrecord";
let recordDetailUrl=url+suffix+"recorddetail";
let bindphoneUrl=url+suffix+"bindphone";
let verifyUrl=url+suffix+"verify";

let replyUrl=url+suffix+"reply";
let indexUrl=url+suffix+"index1";
function setCookie(c_name,value,expiredays)
{
    let exdate=new Date();
    exdate.setDate(exdate.getDate()+expiredays);
    document.cookie=c_name+ "=" +escape(value)+
        ((expiredays==null) ? "" : ";expires="+exdate.toGMTString())
}

//取回cookie
function getCookie(c_name)
{
    if (document.cookie.length>0)
    {
        c_start=document.cookie.indexOf(c_name + "=");
        if (c_start!=-1)
        {
            c_start=c_start + c_name.length+1
            c_end=document.cookie.indexOf(";",c_start);
            if (c_end==-1) c_end=document.cookie.length;
            return unescape(document.cookie.substring(c_start,c_end))
        }
    }
    return ""
}
//控制界面跳转
function isLogin(state) {
    console.log(getCookie('openId'));
if (getCookie('openId')===""){
    window.location.href = getLoginUrl(state+","+getCookie("wxId")+","+getCookie("otherOpenId"));
    return true;
}
return false;
}

window.alert = function(name){
    let iframe = document.createElement("IFRAME");
    iframe.style.display="none";
    iframe.setAttribute("src", 'data:text/plain,');
    document.documentElement.appendChild(iframe);
    window.frames[0].window.alert(name);
    iframe.parentNode.removeChild(iframe);
};
function isEmpty(v) {
    switch (typeof v) {
        case 'undefined':
            return true;
        case 'string':
            if (v.replace(/(^[ \t\n\r]*)|([ \t\n\r]*$)/g, '').length == 0) return true;
            break;
        case 'boolean':
            if (!v) return true;
            break;
        case 'number':
            if (0 === v || isNaN(v)) return true;
            break;
        case 'object':
            if (null === v || v.length === 0) return true;
            for (let i in v) {
                return false;
            }
            return true;
    }
    return false;
}
//获取上个页面传过来的参数
function getQueryString(name) {
    let reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)', 'i');
    let r = window.location.search.substr(1).match(reg);
    if (r != null) {
        return unescape(r[2]);
    }
    return null;
}

function bindOther(){
    if(getCookie("isBind")){
        return;
    }
    $.ajax({
        type: 'POST',
        contentType: "application/x-www-form-urlencoded",
        url: "user/bindOther",
        data: {
            wxId: getCookie("wxId"),
            userId: getCookie("userId"),
            otherOpenId: getCookie("otherOpenId")
        },
        success: function (result) {
           console.log(result);
            if(result.message){
                setCookie("isBind",true,1);
            }
        },
        //请求失败，包含具体的错误信息
        error: function (e) {
            console.log(e.message);
            $.toptip("当前网络异常");
            console.log(e.responseText);
        }
    });
}