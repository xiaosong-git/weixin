var IS_DEVELOP=true;//是否生产环境
var uri;
var url;
var loginUrl;
var appId="wx1ddcdc86c83bc9a1";
if (IS_DEVELOP){//生产环境
    uri="f.pyblkj.cn";
    url= "http://"+uri+"/";
    //重定向地址
    loginUrl="https://open.weixin.qq.com/connect/oauth2/authorize?appid="+appId+"&redirect_uri=http%3A%2F%2F"+uri+"%2Fpybl%2Flogin&response_type=code&scope=snsapi_userinfo&state=233#wechat_redirect";
} else{
    appId="wx2a1951f46acc4371";
    uri="ymms7i.natappfree.cc";
    url= "http://"+uri+"/";
    loginUrl="https://open.weixin.qq.com/connect/oauth2/authorize?appid="+appId+"&redirect_uri=http%3A%2F%2F"+uri+"%2Fpybl%2Flogin&response_type=code&scope=snsapi_userinfo&state=233#wechat_redirect";
}
//朋悦比邻
var suffix="pybl/";
var visitUrl=url+suffix+"visit";
var authUrl=url+suffix+"auth";
var invitUrl=url+suffix+"invit";
var secondRecordUrl=url+suffix+"secondrecord";
var recordDetailUrl=url+suffix+"recorddetail";
var bindphoneUrl=url+suffix+"bindphone";
function setCookie(c_name,value,expiredays)
{
    var exdate=new Date()
    exdate.setDate(exdate.getDate()+expiredays)
    document.cookie=c_name+ "=" +escape(value)+
        ((expiredays==null) ? "" : ";expires="+exdate.toGMTString())
}

//取回cookie
function getCookie(c_name)
{
    if (document.cookie.length>0)
    {
        c_start=document.cookie.indexOf(c_name + "=")
        if (c_start!=-1)
        {
            c_start=c_start + c_name.length+1
            c_end=document.cookie.indexOf(";",c_start)
            if (c_end==-1) c_end=document.cookie.length
            return unescape(document.cookie.substring(c_start,c_end))
        }
    }
    return ""
}
function isLogin() {
    console.log(getCookie('openId'));
if (getCookie('openId')===""){
    window.location.href=loginUrl;
    return true;
}
return false;
}


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
            for (var i in v) {
                return false;
            }
            return true;
    }
    return false;
}