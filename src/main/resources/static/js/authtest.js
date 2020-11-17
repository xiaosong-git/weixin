


function login() {
    $.login({
        title: '朋悦比邻app用户登入',
        text: '使用朋悦比邻app注册过，并实人认证的用户登入',

        onOK: function (username, password) {
            //点击确认
            password = $.md5(password);
            console.log(password);
            pwdLogin(username, password);
        },
        onCancel: function () {
            //点击取消
        }
    });
}

function pwdLogin(username, password) {
    $.showLoading();
    //做发送信息
    $.ajax({
        type: 'POST',
        contentType: "application/x-www-form-urlencoded",
        url: "user/login",
        data: {
            phone: username,
            password: password,
            openId: getCookie("openId")
        },
        success: function (result) {
            $.hideLoading();
            console.log(result);
            if (result.code == 200) {
                // setCookie('token',result.data.user.token,365);
                setCookie('phone', username, 365);
                setCookie('userId', result.data.id, 365);
                setCookie('isAuth', result.data.isauth, 365);
                setCookie('myName', result.data.realname, 365);
                // setCookie('imageServerUrl',result.data.imageServerUrl,365);
                $.toptip("登入成功", 'success');
                if (result.data.isauth !== "T") {
                    $.toptip("请先前往“我的”进行“实人认证”");
                    setTimeout(function () {
                        window.location.href = verifyUrl;
                    }, 1500);
                    return;
                }
                window.history.go(-1);


                //判断用户
                // console.log("该用户不存在")
            } else if (result.code == 400) {
                $.toptip(result.message);
            } else {
                $.toptip("登录失败，请输入正确的账号或密码");
            }
        },
        //请求失败，包含具体的错误信息
        error: function (e) {
            $.hideLoading();
            $.toptip(e.message);
        }
    });
}

function removeimg(obj) {
    $.confirm('您确定要删除吗?', '确认删除?', function () {
        $(obj).remove();
        $("#choose").val('');
        $("#photoDiv").show();
    });

}

//调用图片
function jssdkimg(obj) {
    wx.chooseImage({
        count: 1, // 默认9
        sizeType: ['original', 'compressed'], // 可以指定是原图还是压缩图，默认二者都有
        sourceType: ['album', 'camera'], // 可以指定来源是相册还是相机，默认二者都有
        success: function (res) {
            var localIds = res.localIds; //本地图片id
            $(obj).val(localIds);
            wx.uploadImage({
                localId: '' + $("#choose").val(), // 需要上传的图片的本地ID，由chooseImage接口获得
                isShowProgressTips: 1, // 默认为1，显示进度提示
                success: function (res) {

                    var serverId = res.serverId; // 返回图片的服务器端ID
                    $.toptip("人像识别中", "success");
                    $.ajax({
                        //请求方式
                        type: "POST",
                        //请求的媒体类型
                        contentType: "application/x-www-form-urlencoded",
                        //请求地址
                        url: "user/uploadVerify",
                        //数据，json字符串
                        data: {
                            openId: getCookie("openId"),
                            // token:getCookie(("token")),
                            mediaId: serverId,
                            type: 3
                            // imgname:rst.base64
                        },
                        //请求成功
                        success: function (result) {
                            if (result.code == 400) {
                                $.toptip("请靠近一点");
                            } else {
                                $.toptip("照片可用", "success");
                                //图片服务器中的图片地址
                                // $("#uploaderFiles").val
                                $("#photoDiv").val(result.data.img);
                                $("#uploaderFiles").val(result.data.imageFileName);
                                $("#photoDiv").hide();
                                if (window.__wxjs_is_wkwebview) {
                                    // $.alert("1");
                                    wx.getLocalImgData({
                                        localId: localIds[0],
                                        success: function (res) {
                                            // $.alert("2");
                                            var localData = res.localData; // localData是图片的base64数据，可以用img标签显示
                                            var replace = localData.replace('jgp', 'jpeg');//iOS 系统里面得到的数据，类型为 image/jgp,因此需要替换一下

                                            $("#uploaderFiles").append('<li onclick="removeimg(this)" class="weui-uploader__file" style="background-image:url(' + replace + ')"><input value=""  type="hidden"  name="file" /></li>');
                                        }
                                    })
                                } else {
                                    $("#uploaderFiles").append('<li onclick="removeimg(this)" class="weui-uploader__file" style="background-image:url(' + localIds + ')"><input value=""  type="hidden"  name="file" /></li>');
                                }
                            }
                        }
                    });
                }
            });

        }
    });
}

//退出
function quit() {
    $.toptip( document.referrer==='', 'success');
    wx.closeWindow();
}
function goback() {
    window.history.go(-1);
}
//实人认证
function verify() {
    if (isEmpty($("#realName").val())) {
        $.toptip("请输入真实姓名");
    }
    else if (isEmpty($("#photoDiv").val())) {
        $.toptip("请上传人像照片");
    } else if (isEmpty($("#bindp").val())) {
        $.toptip("请输入手机号码");
    } else if (isEmpty($("#code").val())) {
        $.toptip("请输入验证码");
    }else if (isEmpty($("#code").val())) {
        $.toptip("请输入验证码");
    }else if (isEmpty($("#idNo").val())){
        $.toptip("请输入身份证");
    }
    else {
        $.showLoading();//15280023431
        $.ajax({
            //请求方式
            type: "POST",
            //请求的媒体类型
            contentType: "application/x-www-form-urlencoded",
            //请求地址
            url: "user/verify",
            //数据，json字符串
            data: {
                openId: getCookie("openId"),
                wxId:getCookie(("wxId")),
                otherOpenId:getCookie(("otherOpenId")),
                idHandleImgUrl: $("#uploaderFiles").val(),
                idNO: $("#idNo").val(),
                realName: $("#realName").val(),
                localImgUrl: $("#photoDiv").val(),
                phone: $("#bindp").val(),
                code: $("#code").val(),
                // imgname:rst.base64
            },
            //请求成功
            success: function (result) {
                if (result.code == 400) {
                    $.toptip("认证失败，请确认输入信息是否有误");
                    $.toptip(result.message);

                    $.hideLoading();
                } else {
                    $.hideLoading();
                    setCookie("userId", result.data.userId);
                    setCookie("isAuth", "T");
                    $.toptip("认证成功！", 'success');

                    //点击确认后的回调函数

                    setTimeout(function () {
                        $.toptip( document.referrer==='', 'success');
                        $.toast("认证成功，页面即将关闭");
                        if (document.referrer==='') {
                            $.toast("认证成功，页面即将关闭");
                        }
                        wx.closeWindow();
                    }, 1500);
                    //点击确认后的回调函数


                }
            },
            error: function (e) {
                $.toptip("当前网络异常");
            }
        });
    }

}
//实人认证
function halfVerify() {
    if (isEmpty($("#realName").val())) {
        $.toptip("请输入真实姓名");
    }
    else if (isEmpty($("#photoDiv").val())) {
        $.toptip("请上传人像照片");
    } else if (isEmpty($("#bindp").val())) {
        $.toptip("请输入手机号码");
    } else if (isEmpty($("#code").val())) {
        $.toptip("请输入验证码");
    }
    else {
        $.showLoading();//15280023431
        $.ajax({
            //请求方式
            type: "POST",
            //请求的媒体类型
            contentType: "application/x-www-form-urlencoded",
            //请求地址
            url: "user/halfVerify",
            //数据，json字符串
            data: {
                openId: getCookie("openId"),
                wxId:getCookie(("wxId")),
                otherOpenId:getCookie(("otherOpenId")),
                idHandleImgUrl: $("#uploaderFiles").val(),
                // idNO: $("#idNo").val(),
                realName: $("#realName").val(),
                localImgUrl: $("#photoDiv").val(),
                phone: $("#bindp").val(),
                code: $("#code").val(),
                // imgname:rst.base64
            },
            //请求成功
            success: function (result) {
                if (result.code == 400) {
                    $.toptip("认证失败，请确认输入信息是否有误");
                    $.toptip(result.message);

                    $.hideLoading();
                } else {
                    $.hideLoading();
                    setCookie("userId", result.data.userId);
                    setCookie("isAuth", "H");
                    $.toptip("认证成功！", 'success');
                    $.toast("认证成功，页面即将关闭");
                    //点击确认后的回调函数
                    alert(document.referrer==='');
                    document.referrer===''? wx.closeWindow():window.history.go(-1);

                }
            },
            error: function (e) {
                $.toptip("当前网络异常");
            }
        });
    }

}

var countdown = 60;

//获取手机验证码
function getCode(ojb) {
    var code = $(ojb);
    if ($("#bindp").val() == "") {
        $.toptip("手机号不能为空");
        return;
    }
    if (countdown == 60) {
        $.ajax({
            type: 'POST',
            contentType: "application/x-www-form-urlencoded",
            url: "code/sendCode/" + $("#bindp").val() + "/1",
            success: function (result) {
                // $.hideLoading();
                console.log(result);
                var data = result.data;
                if (result.code == 200) {
                    $.toptip("发送成功", "success");
                    // console.log("该用户不存在")
                } else if (result.code == 400) {
                    if (result.message == "请求参数格式错误") {
                        $.toptip("发送短信失败，手机号码错误");
                    } else {
                        $.toptip(result.message);
                    }
                    countdown = 0;
                }
            }
        });
        code.text("重新发送(" + countdown + ")");
        code.attr('disabled', true);
        countdown--;
    } else if (countdown == 0) {
        code.removeAttr('disabled');
        code.text("获取验证码");
        countdown = 60;

        return;
    } else {
        code.text("重新发送(" + countdown + ")");
        code.attr('disabled', true);
        countdown--;
    }

    setTimeout(function () {
        getCode(ojb)
    }, 1000);
}
