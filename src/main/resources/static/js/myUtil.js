var url= "http://f.pyblkj.cn/";
//朋客联盟
// var suffix="weixin/";
//朋悦比邻
var suffix="pybl/";
//朋客联盟
// var loginUrl="https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx960de9db7158a03b&redirect_uri=http%3A%2F%2Ff.pyblkj.cn%2Fweixin%2Flogin&response_type=code&scope=snsapi_userinfo&state=233#wechat_redirect";
//朋悦比邻
var loginUrl="https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx1ddcdc86c83bc9a1&redirect_uri=http%3A%2F%2Ff.pyblkj.cn%2Fpybl%2Flogin&response_type=code&scope=snsapi_userinfo&state=233#wechat_redirect";
//测试
// var loginUrl="https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx2a1951f46acc4371&redirect_uri=http%3A%2F%2Ff.pyblkj.cn%2Fpybl%2Flogin&response_type=code&scope=snsapi_userinfo&state=233#wechat_redirect";
var visitUrl=url+suffix+"visit";
var authUrl=url+suffix+"auth";
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