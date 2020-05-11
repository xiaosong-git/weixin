var users = [];

function getContacts(searchName) {

    $.ajax({
        type: 'POST',
        contentType: "application/x-www-form-urlencoded",
        url: "user/frequentContacts",
        data: {userId: getCookie("userId")},
        success: function (result) {
            $("#contactPerson").empty();
            var data = result.data;
            $("#contactPerson").append("<div class=\"weui-cells\">")
            for (var i in data) {
                var phone = data[i].phone
                var visitorid = data[i].id
                var realname = data[i].realname;
                var autocompleteddata =  {lable:"",value:"",phone:""};
                autocompleteddata.label = realname;
                autocompleteddata.value = realname;
                autocompleteddata.phone = phone;
                users.push(autocompleteddata);
                if (realname.indexOf(searchName) >= 0) {
                    $("#contactPerson").append(" <div class=\"weui-cell\">\n" +
                        "                      <div class=\"weui-cell__hd\"><img src=\"images\\user.png\" style=\"width: 30px;height: 30px\"></div>\n" +
                        "                        <div class=\"weui-cell__bd\">\n" +
                        "                             <p>" + data[i].realname + "</p>\n" +
                        "                        </div>\n" +
                        "                        <div class=\"weui-cell__ft\">\n" +
                        "                           <a href=\"javascript:void(0);\" style=\"font-size:25px;\" onclick=\"addPersonInfo('" + realname + "','" + phone + "','" + visitorid + "')\">+</a>\n" +
                        "                        </div>\n" +
                        "                    </div>")
                }
            }
            $("#contactPerson").append("</div>");
            $.hideLoading();
        },
        error: function (e) {
            alert("error" + e.message)
        }
    });
}

$(function () {
    if (!isLogin("visit")) {
        if (getCookie("isAuth") != "T") {
            window.location.href = authUrl;
            $.showLoading();
        } else {
            FastClick.attach(document.body);
            $("#start-time").val(formatDateTime(0, 0, 0));
            var searchName = "";
            $.showLoading();
            getContacts(searchName);
            $("#searchInput").bind("input propertychange", function () {
                searchName = $("#searchInput").val();
                console.log(searchName)
                getContacts(searchName);
            });
            $.hideLoading();
        }
    }
});

$("#start-time").datetimePicker();
$("#hour").picker({
    title: "请选择拜访时长",
    cols: [
        {
            textAlign: 'center',
            values: ['0.5', '1', '1.5', '2', '2.5', '3', '3.5', '4', '4.5', '5', '5.5', '6']
        }
    ],
    onChange: function (p, v, dv) {
        console.log(p, v, dv);
    },
    onClose: function (p, v, d) {
        console.log("close");
    }
});

$("#reason").picker({
    title: "请选择拜访事由",
    cols: [
        {
            textAlign: 'center',
            values: ['商务拜访', '配送服务', '面试', '找人', '其他']
        }
    ],
    onChange: function (p, v, dv) {
        console.log(p, v, dv);
    },
    onClose: function (p, v, d) {
        console.log("close");
    }
});

$('.weui-cell_swiped').swipeout('open')


$("#showTooltips").click(function () {
    var isAuth = getCookie("isAuth")
    if (isAuth != "T") {
        $.toptip('请先进行实人认证');
        window.location.href = authUrl;
    } else {
        visit();
    }
});

//输入完姓名与电话查找visitorId
function postNP() {
    var visitorName = $("#visitorName").val();
    var visitorPhone = $("#visitorPhone").val();
    if (visitorName == "") {
        return;
    }
    if (visitorPhone == "") {
        return;
    }
    $.ajax({
        type: 'POST',
        contentType: "application/x-www-form-urlencoded",
        url: "user/namePhone",
        data: {
            realName: visitorName,
            phone: visitorPhone
        },
        success: function (result) {
            console.log(result)
            var data = result.data;
            if (result.code == 400) {
                $.toptip(result.message);
                $("#visitorName").attr("flag", -1);
            } else if (result.code == 200) {
                $("#visitorName").attr("visitorId", data[0].id);
                $("#visitorName").attr("flag", 0);
            } else {
                $.toptip("系统错误");
                $("#visitorName").attr("flag", -2);
            }
        }
    });
}

//发起成功访问
function visit() {
    var visitorName = $("#visitorName").val();
    var visitorPhone = $("#visitorPhone").val();
    var startDate = $("#start-time").val();
    var hour = $("#hour").val();
    var reason = $("#reason").val();
    if (isEmpty(visitorName)) {
        $.toptip("请输入受访人姓名");
        return;
    } else if (isEmpty(visitorPhone)) {
        $.toptip("请输入受访人手机号");
        return;
    } else if (!visitorPhone || !/1[3|4|5|7|8]\d{9}/.test(visitorPhone)) {
        $.toptip('请输入正确的手机号');
        return;
    } else if ($("#visitorName").attr("flag") == -1) {
        $.toptip("受访者无公司归属");
        return;
    } else if (isEmpty(hour)) {
        $.toptip("请选择访问时长");
        return;
    } else if ($("#visitorName").attr("flag") == -2) {
        $.toptip("访问发起失败");
        return;
    } else {
        $.showLoading();
        $.ajax({
            type: 'POST',
            contentType: "application/x-www-form-urlencoded",
            url: "visit/record/visitRequest",
            data: {
                //从跳转页获取用户id
                userId: getCookie('userId'),
                visitorId: $("#visitorName").attr("visitorId"),
                reason: reason,
                startDate: startDate,
                hour: hour,
            },
            success: function (result) {
                $.hideLoading();
                if (result.code == 400) {
                    $.toptip(result.message);
                } else if (result.code == 200) {
                    $.toast("发起访问成功", 'success');
                    alert("访问申请已发送，等待对方审核");
                    location.reload();
                } else {
                    $.toptip("系统错误，请重试");
                }
            }
        });
    }


}

function formatDateTime(year, day, hours) {
    var date = new Date();
    var y = date.getFullYear() + year;
    var m = date.getMonth() + 1;
    m = m < 10 ? ('0' + m) : m;
    var d = date.getDate() - day;
    d = d < 10 ? ('0' + d) : d;
    var h = date.getHours() + hours;
    h = h < 10 ? ('0' + h) : h;
    var minute = date.getMinutes();
    var second = date.getSeconds();
    minute = minute < 10 ? ('0' + minute) : minute;
    // second = second < 10 ? ('0' + second) : second;

    return y + '-' + m + '-' + d + ' ' + h + ':' + minute;
    //2017-12-12 12:23:34
}

function addPersonInfo(name, phone, visitorId) {
    window.scrollTo(0, 0);
    $("#visitorName").val(name);
    $("#visitorPhone").val(phone);
    $.ajax({
        type: 'POST',
        contentType: "application/x-www-form-urlencoded",
        url: "user/namePhone",
        data: {
            realName: name,
            phone: phone
        },
        success: function (result) {
            console.log(result)
            var data = result.data;
            if (result.code == 400) {
                $.toptip(result.message);
                $("#visitorName").attr("flag", -1);
            } else if (result.code == 200) {
                $("#visitorName").attr("visitorId", data[0].id);
                $("#visitorName").attr("flag", 0);
            } else {
                $.toptip("系统异常，请重试");
                $("#visitorName").attr("flag", -2);
            }
        }
    });

    /* $("#visitorName").attr("visitorId", visitorId);
     $("html,body").animate({scrollTop:0},250);*/
}

var pages = 1;
var sizes = 8;
var loading = false;  //状态标记
var hasNextPage = true;
$("#listwrap").infinite().on("infinite", function () {
    if (loading) return;
    if (!hasNextPage) return;
    loading = true;
    pages++; //页数
    console.log(pages);
    $('#loading').show();
    getRecord();

});

//访问记录
function getRecord() {
    $.ajax({
        type: 'POST',
        contentType: "application/x-www-form-urlencoded",
        url: "visit/record/record",
        data: {
            pages: pages,
            sizes: sizes,
            userId: getCookie("userId")
        },//需要改为从cookie获取
        success: function (result) {
            $("#recorddiv").empty();
            $.hideLoading();
            if (result.code == 200) {
                var list = result.data.list;
                for (var i in list) {
                    var username = list[i].user.realname;
                    var date = list[i].startDate.substring(0, 10);
                    var startTime = list[i].startDate.substring(11, 16);
                    var endTime = list[i].endDate.substring(11, 16);
                    var addr = list[i].org.addr;
                    if (isEmpty(addr)) {
                        addr = "未选";
                    }
                    console.log(JSON.stringify(list[i]));

                    $("#recorddiv").append("<a href=\"javascript:void(0);\" class=\"weui-media-box weui-media-box_appmsg\" onclick=\"toSecond('" + username + "','" + list[i].visitorId + "')\">\n" +
                        "\n" +
                        "    <div class=\"weui-media-box__bd\">\n" +
                        "        <p class=\"weui-cell__bd\" ><strong>" + username + "</strong></p><br>\n" +
                        "        <p class=\"weui-cell__bd\">访问时间&nbsp;&nbsp;&nbsp;" + date + " " + startTime + "-" + endTime + "</p>\n" +
                        "        <p class=\"weui-cell__bd\">访问地址&nbsp;&nbsp;&nbsp;" + addr + "</p>\n" +
                        "    </div>\n" +
                        "    <div class=\"weui-panel__ft\">\n" +
                        "        <div class=\"weui-cell__bd\">查看更多》</div>\n" +
                        "    </div>\n" +
                        "</a>")

                }
                $('.weui-loadmore').hide();
                loading = false;
                hasNextPage = result.data.hasNextPage;
                if (!hasNextPage) {
                    $('#over').show();
                }
            }
        },
        error: function (e) {
            $.hideLoading();
            alert("发送错误");
        }
    })
}

function toSecond(username, visitorId) {
    window.localStorage.setItem("rName", username);
    window.localStorage.setItem("rVisitorId", visitorId);
    window.location.href = secondRecordUrl;
}

function autocompletename() {
    $("#visitorName").autocomplete({
        source:users,
        select :function (event,ui) {
            console.log(ui.item)
            document.getElementById("visitorPhone").value = ui.item.phone;

            $.ajax({
                type: 'POST',
                contentType: "application/x-www-form-urlencoded",
                url: "user/namePhone",
                data: {
                    realName: ui.item.value,
                    phone: ui.item.phone
                },
                success: function (result) {
                    console.log(result)
                    var data = result.data;
                    if (result.code == 400) {
                        $.toptip(result.message);
                        $("#visitorName").attr("flag", -1);
                    } else if (result.code == 200) {
                        $("#visitorName").attr("visitorId", data[0].id);
                        $("#visitorName").attr("flag", 0);
                    } else {
                        $.toptip("系统错误");
                        $("#visitorName").attr("flag", -2);
                    }
                }
            });
        }
    })
}
