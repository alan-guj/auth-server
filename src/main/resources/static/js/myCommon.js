/*********
 * 版本：1.0
 *
 *一般公共js代码
 * 作者：jsir
 * 更新时间：2016-02-05
**********/

//姓名验证
function userNameValidate(myNameObj){
    $("#error").html("");
    var reg = /[^\x00-\xff]/g;
    if( $("#"+ myNameObj).val()=="")
        { 
            $("#"+ myNameObj).focus();
            $("#error").html("<span class='iconfont icon-jinggao'></span>请输入姓名");
            return false;
        }
    else if(!reg.test($("#"+ myNameObj).val()))
        {
            $("#"+ myNameObj).focus();
            $("#error").html("<span class='iconfont icon-jinggao'></span>请输入中文姓名！");
            return false;
        }    
    else{
        return true;
    }
}
//手机号验证
function phoneVaildate(myPhoObj){
    $("#error").html("");
    var phoneNum = $("#" + myPhoObj).val();
    var regular = /^(13[0-9]|14[0-9]|15[0-9]|17[0-9]|18[0-9])\d{8}$/;
    if( phoneNum=="")
    {
        $("#error").html("<span class='iconfont icon-jinggao'></span>手机号码不能为空!");
        $("#" + myPhoObj).focus();
        return false;
    }
    else if(!regular.test(phoneNum))
    {
        $("#error").html("<span class='iconfont icon-jinggao'></span>请输入正确的手机号码！");
        $("#" + myPhoObj).focus();
        return false;
    }
    else
    {
        return true;
    }
    
}
//邮箱验证
function emailValidate(myMailObj){
    $("#error").html("");
    reg=/^\w+[@]\w+((.com)|(.net)|(.cn)|(.org)|(.gmail))$$/;
        if( $("#" + myMailObj).val()=="")
        {
            $("#error").html("<span class='iconfont icon-jinggao'></span>邮箱不能为空!");
            $("#" + myMailObj).focus();
            return false;
        }
        else if(!reg.test($("#" + myMailObj).val()))
        {
            $("#" + myMailObj).focus();
            $("#error").html("<span class='iconfont icon-jinggao'></span>邮箱格式错误！");
            return false;
        }
        else 
        {
            return true;
        }
}

//非默认值下拉框选择验证
function userSelectValidate(myselObj){
    $("#error").html("");
   
    if( $("#" + myselObj + " option:selected").val()=="0")
        { 
            var myWarningStr =  $("#" + myselObj + " option:selected").text();

            $("#error").html("<span class='iconfont icon-jinggao'></span>" + myWarningStr);
            $("#" + myselObj).focus();             
            return false;
        }  
    else{
        return true;
    }
}

function myValidate(myObjId){
  
  $("#error").html("");
  var myObjContext = $("#" + myObjId).val();
    
    if( myObjContext=="")
    {
       var myWarningStr = $("#" + myObjId).attr("placeholder");
        $("#error").html("<span class='iconfont icon-jinggao'></span>" + myWarningStr);
        $("#" + myObjId).focus();
        return false;
    }
    else
    {
        return true;
    } 
         
}


//非空值，数字验证
function myValidateNumber(myObjId){
  $("#error").html("");
  var myObjContext = $("#" + myObjId).val();
    
    if( (myObjContext=="") || (isNaN(myObjContext)) )
    {
       var myWarningStr = $("#" + myObjId).attr("placeholder");
       $("#error").html("<span class='iconfont icon-jinggao'></span>" + myWarningStr);
       $("#" + myObjId).focus();
       return false;
    }
    else
    {
        return true;
    } 
         
}

//复选框非空值验证
function myValidateCheckbox(myObjId){
    $("#error").html("");
    var mytag = 0;
    $("#" + myObjId).find("[name='data-checkbox']").each(function(){     
        if($(this).prop("checked") == true){
            mytag = 1;
            return false;     //相当于使用break结束循环
        }         
    })  
    
    if( mytag == 0) 
    {
        var myWarningStr = "请选择";
        $("#error").html("<span class='iconfont icon-jinggao'></span>" + myWarningStr);
        $("#" + myObjId).focus();
        return false;
    }
    else
    {
        return true;
    } 
         
}


 //mobiscroll插件使用  
function mobiscrollIvoke(myObjId){
    var currYear = (new Date()).getFullYear();  
    var opt={};
      //opt.date = {preset : 'date'};
      opt.datetime = {preset : 'datetime'};
      //opt.time = {preset : 'time'};
      opt.default = {
        theme: 'android-ics light', //皮肤样式
        display: 'modal', //显示方式 
        mode: 'scroller', //日期选择模式
        dateFormat: 'yyyy-mm-dd',
        lang: 'zh',
        showNow: true,
        nowText: "今天",
        startYear: currYear - 10, //开始年份
        endYear: currYear + 10 //结束年份
      };
      
      var optDateTime = $.extend(opt['datetime'], opt['default']);
      //var optTime = $.extend(opt['time'], opt['default']);
          
      
      $("#" + myObjId).mobiscroll(optDateTime).datetime(optDateTime);
}

//兼容方式获取scrolltop以及设置scrolltop
    function getScrollTop() {
        var scrollTop = document.documentElement.scrollTop || window.pageYOffset || document.body.scrollTop;
        return scrollTop;
    }
 
    function setScrollTop(scroll_top) {
        document.documentElement.scrollTop = scroll_top;
        window.pageYOffset = scroll_top;
        document.body.scrollTop = scroll_top;
    }

$(function(){


 /*点击展开/收缩列表*/
    $(".panel").on("click",".J-mytitle",function(e){
      e.stopPropagation();
    
    if ($(this).find(".J-hint-pic").hasClass("myClose")) {
        $(this).find(".J-mytitlePic").removeClass("icon-wenjianguanli").addClass("icon-iconfont90");
       
        $(this).find(".J-hint-pic").removeClass("myClose");
        $(this).find(".J-hint-pic").html('<span class="iconfont icon-iconfont40 font20 text-common"></span>');        
    
        //关闭其他打开的列表   
        $(this).siblings(".J-mytitle").each(function(){
            if($(this).find(".J-hint-pic").hasClass("myClose")){}
            else{
                $(this).find(".J-mytitlePic").removeClass("icon-iconfont90").addClass("icon-wenjianguanli");
                $(this).find(".J-hint-pic").addClass("myClose");       
                $(this).find(".J-hint-pic").html('<span class="iconfont icon-shousuo1 font20 text-common"></span>');
                $(this).next().slideUp();
            }
        });

        $(this).next().slideDown();

        //不在可视区域内(部分显示也上移)，并在下方,上移一个子元素距离
        var childHeight = $(this).next().children().first().height();
        var childOffset = $(this).next().children().first().offset().top;
        var scrollTop = getScrollTop();

        if((scrollTop+$(window).height())<(childOffset + childHeight)){
            
             var scHeight = scrollTop + childHeight;
            $('html,body').animate({scrollTop: scHeight}, 800);
        }
        

        $(this).next().focus();
    }
    else{
        $(this).find(".J-mytitlePic").removeClass("icon-iconfont90").addClass("icon-wenjianguanli");
        $(this).find(".J-hint-pic").addClass("myClose");       
        $(this).find(".J-hint-pic").html('<span class="iconfont icon-shousuo1 font20 text-common"></span>');
        $(this).next().slideUp();
    }
 
      return false;
    });



/*切换 打开关闭事件*/  
$(".J-toggleOpenClose-btn").click(function(e){
      e.stopPropagation();

      if($(this).hasClass("active")){
        $(this).removeClass("active");
        $(this).css({"background":"#F5F5F5","text-align":"left"});
      }else{        
          $(this).addClass("active");
          $(this).css({"background":"#4b0","text-align":"right"});
      }
    })

/*新资料点击变灰，变为已读*/
$(".panel").on("click",".J-data-title",function(e){
    e.stopPropagation();

    if( $(this).find(".cs-notRead") ){
        $(this).find(".cs-notRead").removeClass("cs-notRead").addClass("font_gray666");
    }
    return true;        
})

})

