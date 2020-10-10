var users = [];
var entourageNames = [];
var hasCar = false;
var hasEntourage = false;
var tabInex = 0;
var hasEntourageAddr = false
var hasCarAddr = false;

function initState(index) {
    $("#start-time").datetimePicker();
    $('#keycontent').hide();
    $('#listwrap').empty();
    tabInex = index
    users = [];
    entourageNames = [];
   _cardNumbers=Array(8).fill('');
    
    if (index == 0) { //联系人访问
        $('#visitorName').val('');
        $('#visitorPhone').val('')
        $('#hour').val('')
        $('#reason').val('')
        $('#cardNumbers').val('')
        $('#entourage').val('')
        $('#cardNumbersInput').hide()
        $('#peopleEntourage').hide()
        $('#contactPerson').show()
        $('#contactEntouragePerson').hide()
        hasCar = false;
        hasEntourage = false;
        $('#cardNumbersInputAddr').empty()
        createCarnumberUI("cardNumbersInput")
        getContacts('');
        getEntourageContact('');
        
    } else { //地址访问
        $('#companyName').val('');
        $('#visit2Name').val('')
        $('#visit2Hour').val('')
        $('#visit2Reason').val('')
        $('#cardNumbersAddr').val('')
        $('#entourageAddr').val('')
        $('#cardNumbersInputAddr').hide()
        $('#peopleEntourageAddr').hide()

        hasEntourageAddr = false
        hasCarAddr = false;
        $('#cardNumbersInput').empty()
        createCarnumberUI("cardNumbersInputAddr")
        getEntourageContact('');
    }
}
/**
 * 常用联系人(单选)
 * @param {} searchName 
 */
function getContacts(searchName) {
    $.ajax({
        type: 'POST',
        contentType: "application/x-www-form-urlencoded",
        url: "user/frequentContacts",
        // url: "http://192.168.10.30:3000/users",
        data: { userId: getCookie("userId") },
        success: function(result) {
            $("#contactPerson").empty();
            var data = result.data;
            // var data = JSON.parse(result);
            $("#contactPerson").append("<div class=\"weui-cells\">")

            for (var i in data) {
                var phone = data[i].phone
                var visitorid = data[i].id
                var realname = data[i].realname;
                var autocompleteddata = { lable: "", value: "", phone: "" };
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
                        "                           <a href=\"javascript:void(0);\"  onclick=\"addPersonInfo('" + realname + "','" + phone + "','" + visitorid + "')\"><img src=\"images\\add.png\" style=\"width: 32%;\"></a>\n" +
                        "                        </div>\n" +
                        "                    </div>")
                }
            }
            $("#contactPerson").append("</div>");
            $.hideLoading();
        },
        error: function(e) {
            alert("error" + e.message)
        }
    });
}
/**
 * 常用联系人(复选)
 * @param {*} searchName 
 */
function getEntourageContact(searchName) {
    $.ajax({
        type: 'POST',
        contentType: "application/x-www-form-urlencoded",
        url: "user/frequentContacts",
        // url: "http://192.168.10.20:3000/users",
        data: { userId: getCookie("userId") },
        success: function(result) {
            var $obj = tabInex == 0 ? $("#contactEntouragePerson") : $("#contactEntouragePersonAddr");
            $obj.empty();
            var data = result.data;
            // var data = JSON.parse(result);
            $obj.append("<div class=\"weui-cells\">")
            for (var i in data) {
                var visitorid = data[i].id
                var realname = data[i].realname;
                if (realname.indexOf(searchName) >= 0) {
                    $obj.append(" <div class=\"weui-cell weui-cells_checkbox_cus\" style='position:relative;font-size:17px'>\n" +
                        "                      <div class=\"weui-cell__hd\"><img src=\"images\\user.png\" style=\"width: 30px;height: 30px\"></div>\n" +
                        "                        <div class=\"weui-cell__bd\">\n" +
                        "                             <p>" + realname + "</p>\n" +
                        "                        </div>\n" +
                        "                        <div class=\"weui-cell__ft weui-check__label\">\n" +
                        "                          <input type=\"checkbox\" value='" + realname + "' name=\"checkbox1\" class=\" weui-check-cus\" data-visitorid='" + visitorid + "' />" +
                        "                           <i class=\"weui-icon-checked\"></i>" +
                        "                        </div>\n" +
                        "                    </div>")
                }
            }
            $obj.append("</div>");
            $obj.find('input').each(function() {
                $(this).prop('checked', false);
            })
            $.hideLoading();
        },
        error: function(e) {
            alert("error" + e.message)
        }
    });
}

$(function() {
    if (!isLogin("visit")) {
    // if (true) {
        if (getCookie("isAuth") != "T" && getCookie("isAuth") != "H") {
        // if (false) {
            window.location.href = authUrl;
            $.showLoading();
        } else {
            FastClick.attach(document.body);
            $("#start-time").val(formatDateTime(0, 0, 0));
            var searchName = '';
            $.showLoading();
            if (tabInex == 0) {
                getContacts(searchName);
                getEntourageContact(searchName);
            }
            $("#searchInputAddr,#searchInput").bind("input propertychange", function() {
                if (tabInex == 0) {
                    searchName = $("#searchInput").val()
                    getContacts(searchName);
                    getEntourageContact(searchName);
                } else {
                    searchName = $("#searchInput").val()
                    getEntourageContact(searchName);
                }
            });
            /**
             * 常用联系人复选
             */
            $("#contactEntouragePersonAddr,#contactEntouragePerson").bind('change', function(e) {
                console.log('e.target', e.target)
                var $sel = $(e.target);
                var visitorid = $sel.attr('data-visitorid');
                var realName = $sel.val();

                var _objIndex = entourageNames.findIndex((item) => item.visitorid === visitorid);
                if (_objIndex == -1) {
                    if ($sel.prop('checked')) {
                        entourageNames.push({ visitorid, realName })
                    }
                } else {
                    if (!$sel.prop('checked')) {
                        entourageNames.splice(_objIndex, 1);
                    }
                }
                var _namesArray = [];
                for (item in entourageNames) {
                    _namesArray.push(entourageNames[item].realName);
                }
                if (_namesArray.length) {
                    tabInex == 0 ? $('#entourageNames').val(_namesArray.join('，')) : $('#entourageNamesAddr').val(_namesArray.join('，'));
                } else {
                    tabInex == 0 ? $('#entourageNames').val('') : $('#entourageNamesAddr').val('');
                }
            })
            $("#visitorName").focus(function() {
                $('#keycontent').hide();
                $('#contactEntouragePerson').hide()
                $('#contactPerson').show()
            })
            $("#visitorPhone").focus(function() {
                $('#keycontent').hide();
                $('#contactEntouragePerson').hide()
                $('#contactPerson').show()
            })
            $("#start-time").focus(function() {
                $('#keycontent').hide();
            })
            $("#start-time").datetimePicker();
            $("#hour").picker({
                title: "请选择拜访时长",
                cols: [{
                    textAlign: 'center',
                    values: ['0.5', '1', '1.5', '2', '2.5', '3', '3.5', '4', '4.5', '5', '5.5', '6']
                }],
                onChange: function(p, v, dv) {
                    console.log(p, v, dv);
                    $('#keycontent').hide();
                },
                onClose: function(p, v, d) {
                    console.log("close");
                    $('#keycontent').hide();
                }
            });

            $("#reason").picker({
                title: "请选择拜访事由",
                cols: [{
                    textAlign: 'center',
                    values: ['商务拜访', '配送服务', '面试', '找人', '其他']
                }],
                onChange: function(p, v, dv) {
                    console.log(p, v, dv);
                    $('#keycontent').hide();
                },
                onClose: function(p, v, d) {
                    console.log("close");
                    $('#keycontent').hide();
                }
            });
            $("#cardNumbers").click(function() {
                $('#cardNumbersInput').show()
            })
            $("#cardNumbers").picker({
                title: "请选择是否有车",
                cols: [{
                    textAlign: 'center',
                    values: ['是', '否']
                }],
                onChange: function(p, v, dv) {
                    $('#peopleEntourage').hide()
                    let _ul_input_spans = $('#cardNumbersInput .ul_input_spans').find('span');
                    _ul_input_spans.each(function() {
                        if ($(this).text() !== '新能源') {
                            $(this).text('');
                        }
                    })
                    if (Array.isArray(v)) {
                        if (v[0] === '是') {
                            $('#cardNumbersInput').show()
                            hasCar = true;
                        } else {
                            $('#cardNumbersInput').hide()
                            hasCar = false;
                        }
                    }
                },
                onClose: function(p, v, d) {
                    $('#peopleEntourage').hide();
                    console.log("close");
                }
            });
            $("#entourage").click(function() {
                $('#peopleEntourage').show();
                entourageNames = [];
                $("#contactEntouragePerson").find('input').each(function() {
                    $(this).prop('checked', false);
                })
                $('#entourageNames').val('');
                if (hasEntourage) {
                    $('#contactEntouragePerson').show()
                    $('#contactPerson').hide()
                }
            })
            $("#entourage").picker({
                title: "请选择随行人员",
                cols: [{
                    textAlign: 'center',
                    values: ['是', '否']
                }],
                onChange: function(p, v, dv) {
                    console.log(dv);
                    $('#keycontent').hide()
                    if (Array.isArray(v)) {
                        if (v[0] === '是') {
                            $('#peopleEntourage').show()
                            $('#contactEntouragePerson').show()
                            $('#contactPerson').hide()
                            hasEntourage = true
                        } else {
                            $('#peopleEntourage').hide()
                            $('#contactEntouragePerson').hide()
                            $('#contactPerson').show()
                            hasEntourage = false
                        }
                    }
                },
                onClose: function(p, v, d) {
                    console.log("close");
                    $('#keycontent').hide()

                }
            });
            $('.weui-cell_swiped').swipeout('open')


            $("#showTooltips").click(function() {
                if (getCookie("isAuth") != "T"&&getCookie("isAuth") != "H") {
                    $.toptip('请先进行实人认证');
                    window.location.href = authUrl;
                } else {
                    visit();
                }
                // visit();
            });
            $.hideLoading();
        }
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
        success: function(result) {
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
    var visitorId = $("#visitorName").attr("visitorId");

    var _newEng = $("#cardNumbersInput").data("newEng");
    var _entourageNames = $("#entourageNames").val();
    var _cardNumber = $("#cardNumbersInput").data("cardNumbers");


    var entourageNamesIds = []
    var _entourageNamesIds = '';
    entourageNames.forEach((item) => {
        entourageNamesIds.push(item.visitorid);
    })
    if (entourageNamesIds.length) {
        _entourageNamesIds = entourageNamesIds.join(',');
    }
    if (isEmpty(visitorName)) {
        $.toptip("请输入受访人姓名");
        return;
    } else if (isEmpty(visitorPhone)) {
        $.toptip("请输入受访人手机号");
        return;
    } else if (!visitorPhone || !/1[3|4|5|7|8]\d{9}/.test(visitorPhone)) {
        $.toptip('请输入正确的手机号');
        return;
    }
    else if ($("#visitorName").attr("flag") == -1) {
        $.toptip("受访者无公司归属");
        return;
    }
    else if (isEmpty(hour)) {
        $.toptip("请选择访问时长");
        return;
    } else if ($("#visitorName").attr("flag") == -2) {
        $.toptip("访问发起失败");
        return;
    } else if (hasCar && isEmpty(_cardNumber)) { //车牌号
        $.toptip("请输入车牌号");
        return;
    } 
    else if (hasCar && _cardNumber.length < 7) {
        $.toptip("车牌号格式不对");
        return;
    }
    else if (hasEntourage && isEmpty(_cardNumber)) {
        $.toptip("请选择随行人员");
        return;
    } else {
        visitRequest(visitorId, reason, startDate, hour, _cardNumber, _entourageNamesIds);
    }
}
//调用访问接口
function visitRequest(visitorId, reason, startDate, hour, cardNumber, entourageNamesIds) {
    $.showLoading();
    $.ajax({
        type: 'POST',
        contentType: "application/x-www-form-urlencoded",
        url: "visit/record/visitRequest",
        data: {
            //从跳转页获取用户id
            userId: getCookie('userId'),
            visitorId: visitorId,
            reason: reason,
            startDate: startDate,
            hour: hour,
            carNumber: cardNumber, //车牌号
            userIds: entourageNamesIds //随行人员
        },
        success: function(result) {
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
        success: function(result) {
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
var loading = false; //状态标记
var hasNextPage = true;
$("#listwrap").infinite().on("infinite", function() {
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
        }, //需要改为从cookie获取
        success: function(result) {
            $("#recorddiv").empty();
            $.hideLoading();
            if (result.code == 200) {
                var list = result.data.list;
                console.log(list)
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

                    /* $("#recorddiv").append("<a href=\"javascript:void(0);\" class=\"weui-media-box weui-media-box_appmsg\" onclick=\"toSecond('" + username + "','" + list[i].visitorId + "')\">\n" +
                         "\n" +
                         "    <div class=\"weui-media-box__bd\">\n" +
                         "        <p class=\"weui-cell__bd\" ><strong>" + username + "</strong></p><br>\n" +
                         "        <p class=\"weui-cell__bd\">访问时间&nbsp;&nbsp;&nbsp;" + date + " " + startTime + "-" + endTime + "</p>\n" +
                         "        <p class=\"weui-cell__bd\">访问地址&nbsp;&nbsp;&nbsp;" + addr + "</p>\n" +
                         "    </div>\n" +
                         "    <div class=\"weui-panel__ft\">\n" +
                         "        <div class=\"weui-cell__bd\">查看更多》</div>\n" +
                         "    </div>\n" +
                         "</a>")*/

                    $("#recorddiv").append("<a href=\"javascript:void(0);\" class=\"weui-media-box weui-media-box_appmsg\" onclick=\"toSecond('" + username + "','" + list[i].visitorId + "','" + list[i].visitorId + "')\">\n" +
                        "            <div class=\"weui-media-box__hd\">\n" +
                        "                <font size='3'>" + username + "</font>\n" +
                        "            </div>\n" +
                        "            <div class=\"weui-media-box__bd\">\n" +
                        "                <p >" + date + " " + startTime + "-" + endTime + "</p>\n" +
                        "                <p style=\"padding-top: 5px\">" + addr + "</p>\n" +
                        "            </div>\n" +
                        "            <div class=\"weui-media-box__foot\">\n" +
                        "                <font size=\"5\">></font>\n" +
                        "            </div>\n" +
                        "        </a>")

                }
                $('.weui-loadmore').hide();
                loading = false;
                hasNextPage = result.data.hasNextPage;
                if (!hasNextPage) {
                    $('#over').show();
                }
            }
        },
        error: function(e) {
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
        source: users,
        select: function(event, ui) {
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
                success: function(result) {
                    console.log(result)
                    var data = result.data;
                    if (result.code == 400) {
                        $.toptip(result.message);
                        $("#visitorName").attr("flag", -1);
                        //todo 改成未查到用户
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