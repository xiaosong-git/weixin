var _cardNumbers=Array(8).fill('');
;(function($) {
    $.fn.keyboard = function(options) {
        var bodyW = document.documentElement.clientWidth || document.body.clientWidth;
        var itemWidth = (bodyW - 40) / 10;
        var keyBoard = '<div id="keycontent">\n' +
            '<div id="keyboard">\n' +
            '    <div class="keyTitle">\n' +
            '        <div class="keyText"></div>\n' +
            '        <div class="keyHide">收起</div>\n' +
            '    </div>\n' +
            '    <div class="keyContent"></div>\n' +
            ' </div>\n' +
            // ' <div class="keyMask"></div>\n' +
            '</div>';
        if (!($("#keycontent").length > 0)) {
            $('body').append(keyBoard);
        }
        var defaults = {
            //各种参数、各种属性
            defaults: 'English', //键盘显示类型   English 字母  number 数字
            inputClass: 'text', //输入框ID
            caseSwitch: 'toUpperCase', //英文大小写  toLowerCase 小写  toUpperCase 大写
        };

        var endOptions = $.extend(defaults, options);
        this.each(function() {

            var _this = $('#keycontent');

            _this.on('click', 'li', function() { //获取点击的内容
                try {
                    inputVal($(this));
                    keyState($(this));
                } catch (error) {
                    
                }
              
            });

            caseSwitch(defaults.defaults);

            _this.on('click', '.englishKeyboard', function() { //英文键盘
                caseSwitch('English');
                keyState($(this));
            });
            _this.on('click', '.symbolSwitch', function() { //符号键盘
                caseSwitch('symbol');
                keyState($(this));
            });
            _this.on('click', '.keyHide,.keyMask', function() { //收起键盘
                _this.remove();
            });


            function inputVal(_this) {
                let oDiv = $('.' + defaults.inputClass + '').val();
                let val = oDiv += _this.html();
                $('.' + defaults.inputClass + '').val(val);
            }

            function caseSwitch(data) {
                if (data == 'English') {
                    english(defaults.caseSwitch);
                } else if (data == 'number') {
                    number();
                } else if (data == 'symbol') {
                    symbol();
                }
            }

            function english(data) { //英文键盘
                _this.find('.keyContent').html('');
                let englishArray = ['1', '2', '3', '4', '5', '6', '7', '8', '9', '0', 'Q', 'W', 'E', 'R', 'T', 'Y', 'U', 'I', 'O', 'P', 'A', 'S', 'D', 'F', 'G', 'H', 'J', 'K', 'L', 'Z', 'X', 'C', 'V', 'B', 'N', 'M'];
                let english = '';
                english += '<ul class="english">';

                for (let i = 0; i < englishArray.length; i++) {
                    if (i == 20) {
                        english += "<li class='item' style='width: " + itemWidth + "px;margin-left:" + (itemWidth / 2 + 2 * 2) + "px'>" + englishArray[i] + "</li>"

                    } else if (i == 29) {
                        english += "<li class='item' style='width: " + itemWidth + "px;margin-left:" + (itemWidth + itemWidth / 2 + 2 * 4) + "px'>" + englishArray[i] + "</li>"
                    } else {
                        english += "<li class='item' style='width: " + itemWidth + "px'>" + englishArray[i] + "</li>"
                    }
                }
                english += '</ul>';
                english += '<div class="symbolSwitch" style="width: ' + (itemWidth + itemWidth / 2 + 2) + 'px">省份</div>';
                english += '<div class="del" style="width: ' + (itemWidth + itemWidth / 2 + 2) + 'px">删除</div>';
                _this.find('.keyContent').append(english);
            }

            function symbol(data) { //符号键盘
                _this.find('.keyContent').html('');
                let symbolArray = ['京', '津', '渝', '沪', '冀', '晋', '辽', '吉', '黑', '苏', '浙', '皖', '闽', '赣', '鲁', '豫', '鄂', '湘', '粤', '琼', '川', '贵', '云', '陕', '甘', '青', '蒙', '桂', '宁', '新', '藏', '使', '领', '警', '学', '港', '澳'];
                let english = '';
                english += '<ul class="english">';

                for (let i = 0; i < symbolArray.length; i++) {
                    if (i == 30) {
                        english += "<li class='item' style='width: " + itemWidth + "px;margin-left:" + (itemWidth + itemWidth / 2 + 2 * 4) + "px'>" + symbolArray[i] + "</li>"
                    } else {
                        english += "<li class='item' style='width: " + itemWidth + "px'>" + symbolArray[i] + "</li>"
                    }
                }
                english += '</ul>';
                english += '<div class="englishKeyboard" style="width: ' + (itemWidth + itemWidth / 2 + 2) + 'px">ABC</div>';
                english += '<div class="del" style="width: ' + (itemWidth + itemWidth / 2 + 2) + 'px">删除</div>';
                _this.find('.keyContent').append(english);
            }

            function keyState(data) {
                data.css('opacity', '0.3')
                setTimeout(function() {
                    data.css('opacity', '1')
                }, 100);
            }

        });
    };
})(jQuery);
//一般直接写在一个js文件中
layui.use(['layer', 'form'], function() {

    var $ = layui.jquery,
        layer = layui.layer,
        form = layui.form;

    $(document).on('click', '.cardNumbersInput li', function() {
        document.activeElement.blur(); // 阻止弹出系统软键盘
        var _cliss = $(this).attr("class");
        var _sort = $(this).data("sort");

        $(this).addClass("input_zim").siblings().removeClass("input_zim");
        if (!$('#keycontent').is(":visible")) {
            $('#keycontent').show();
        }
        if (_sort == 1) {
            $('body').keyboard({
                defaults: 'symbol', //键盘显示类型   English 字母  number 数字  symbol 符号
                inputClass: _cliss, //输入框Class
            });
        } else {
            $('body').keyboard({
                defaults: 'English', //键盘显示类型   English 字母  number 数字  symbol 符号
                inputClass: _cliss, //输入框Class
            });
        }
    });
    $(document).on("click", '#keyboard .keyContent li', function(event) {
        var _cardnumber = $(this).text()
        var _sortInit = $(".input_zim").data("sort");
        var _newEng = $(".cardNumbersInput").data("newEng");
        var maxIndex=_newEng?7:6;
        if(_sortInit - 1<=maxIndex){
            _cardNumbers[_sortInit - 1] = _cardnumber;
            $(".input_zim span").html(_cardnumber);
        }
        var _sort = _sortInit + 1;
        if (_sort == 2) {
            $('body').keyboard({
                defaults: 'English', //键盘显示类型   English 字母  number 数字  symbol 符号
            });
        }
        $("#cp" + _sort).addClass("input_zim").siblings().removeClass("input_zim");
        $(".cardNumbersInput").data("cardNumbers", _cardNumbers.join(''))
    });

    $(document).on("click", '.del', function(event) {
        $(".input_zim span").text('');
        var _sort = $(".input_zim").data("sort") - 1;
        var _newEng = $(".cardNumbersInput").data("newEng");
        $("#cp" + _sort).addClass("input_zim").siblings().removeClass("input_zim");
        $(".cardNumbersInput").data("temNewCardNumbers", "")

        if (_newEng) {
            var _newCardNumbersDel = $(".cardNumbersInput").data("newCardNumbers")
            if (_newCardNumbersDel) {
                if (_newCardNumbersDel.length > 0) {
                    var _newCardNumbersArr = _newCardNumbersDel.split('')
                    _newCardNumbersArr.splice(_sort, 1)
                    $(".cardNumbersInput").data("newCardNumbers", _newCardNumbersArr.join(''))
                    console.log('newCardNumbers', _newCardNumbersArr)
                }
            }
        } else {
            var _cardNumbersDel = $(".cardNumbersInput").data("cardNumbers")
            if (_cardNumbersDel) {
                if (_cardNumbersDel.length > 0) {
                    var _cardNumbersArr = _cardNumbersDel.split('')
                    _cardNumbersArr.splice(_sort, 1)
                    $(".cardNumbersInput").data("cardNumbers", _cardNumbersArr.join(''))
                    console.log('cardNumbers', _cardNumbersArr)
                }
            }
        }
    });

    $(document).on("click", '.xinneng', function(event) {
        $(".xinneng").remove();
        $("#cp8").show();
        $(".cardNumbersInput").data("newEng", true)
        var _restCardNumber = $(".cardNumbersInput").data("cardNumbers");
        if (_restCardNumber) {
            $("#cp" + _restCardNumber.length).addClass("input_zim").siblings().removeClass("input_zim");
        } else {
            $("#cp1").addClass("input_zim").siblings().removeClass("input_zim");
        }
    });


});

function createCarnumberUI(ele) {
    $('#' + ele).empty()
    $('#' + ele).append(`<div class="weui-cell"  class="cardNumbersInput">
        <div class="car_input">
            <ul class="clearfix ul_input ul_input_spans">
                <li id="cp1" class="input_zim" data-sort="1"><span></span></li>
                <li id="cp2" data-sort="2"><span></span></li>
                <li id="cp3" data-sort="3"><span></span></li>
                <li id="cp4" data-sort="4"><span></span></li>
                <li id="cp5" data-sort="5"><span></span></li>
                <li id="cp6" data-sort="6"><span></span></li>
                <li id="cp7" data-sort="7"><span></span></li>
                <li id="cp8" data-sort="8" style="display:none;"><span></span></li>
                <li class="newBtn xinneng"><span>新能源</span></li>
            </ul>
        </div>
    </div>`)
}