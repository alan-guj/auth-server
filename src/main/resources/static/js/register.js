
//姓名验证
function userNameValidate(){
    $("#error").html("");
    var reg = /[^\x00-\xff]/g;
    if( $("#username").val()=="")
        {
            $("#username").focus();
            $("#error").html("<span class='iconfont icon-jinggao'></span>请输入姓名");
            return false;
        }
    else if(!reg.test($("#username").val()))
        {
            $("#username").focus();
            $("#error").html("<span class='iconfont icon-jinggao'></span>请输入中文姓名！");
            return false;
        }
    else{
        return true;
    }
}
//手机号验证
function phoneVaildate(){
    $("#error").html("");
    var phoneNum = $("#mobile").val();
    var regular = /^(13[0-9]|14[0-9]|15[0-9]|17[0-9]|18[0-9])\d{8}$/;
    if( phoneNum=="")
    {
        $("#error").html("<span class='iconfont icon-jinggao'></span>手机号码不能为空!");
        $("#phoneNum").focus();
        return false;
    }
    else if(!regular.test(phoneNum))
    {
        $("#error").html("<span class='iconfont icon-jinggao'></span>请输入正确的手机号码！");
        $("#phoneNum").focus();
        return false;
    }
    else
    {
        return true;
    }

}
//邮箱验证
function emailValidate(){
    $("#error").html("");
    reg=/^\w+[@]\w+((.com)|(.net)|(.cn)|(.org)|(.gmail))$$/;
        if( $("#email").val()=="")
        {
            $("#error").html("<span class='iconfont icon-jinggao'></span>邮箱不能为空!");
            $("#email").focus();
            return false;
        }
        else if(!reg.test($("#email").val()))
        {
            $("#email").focus();
            $("#error").html("<span class='iconfont icon-jinggao'></span>邮箱格式错误！");
            return false;
        }
        else
        {
            return true;
        }
}
//提交
function register(){
    if(phoneVaildate()){
//        document.registerForm.action = "register-success.html";
        document.registerForm.submit();
    }else{

    }

}


