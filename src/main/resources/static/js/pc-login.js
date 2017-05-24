/*huanglin*/
function login_check(url){
    (function do_login_check() {
    $.ajax({
        cache: false,
        type: "GET",
        url: url,
        async: false,
        error: function(request,err,data){
            console.log(request,err,data);
        },
        success: function(data){
            console.debug(data);
            var authflag=data.status;
            if(authflag==='success'){
                window.location.reload();
                return;
            }
        },
        complete: function(request) {
            setTimeout(do_login_check,3000);
        }
    });
    return;
    })();
}

