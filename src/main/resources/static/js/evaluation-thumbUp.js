/*********
 *  * 版本：1.0
 *   *
 *    *评价和点赞的公共js代码
 *     * 作者：jsir
 *      * 更新时间：2016-01-25
 *      **********/
/*
 * huanglin
 * 获取页面标题
 * 返回值:
 * 1 -- ppt播放页面
 * 2 -- video播放页面
 * 0 -- other
 * */
function get_page_title()
{
  pagetitle = $("title").text();
  if(pagetitle == "资料查看")
    return 1;
  else if(pagetitle == "视频播放")
    return 2;
  else
    return 0;
}

/*huanglin*/
/*
 * 获取资源编号
 * 返回值:
 * > 0  -- 资源编号
 * 0    -- error
 * */
function get_material_id(pagetype)
{
  if(pagetype == 1)
    materialid=$("#slider").parent().attr("id");
  else if(pagetype == 2)
    materialid=$("video").attr("id");
  else
    materialid=0;
	
  return materialid;
}

/*huanglin*/
/*点赞处理*/
function lovehandler(lovetype){
  pagetype=get_page_title();
  if(pagetype == 0)
    return;

  materialid=get_material_id(pagetype);
  if(materialid == 0)
    return;
  
  var data = {
      data: JSON.stringify({
                "materialid":materialid,
                "lovetype":lovetype,
              })
    }
   
  $.ajax({
    cache: false,
    type: "POST",
    url: "/material/love",
    data: data,
    async: false,

    error: function(request){
      alert("发送点赞请求失败！");
    },

    success: function(data){
      likeid="like"+materialid;
      $('#'+likeid).html(data);
    }
  });
}

/*huanglin*/
/*评价处理*/
function reviewhandler(){
  context=$(".cs-pingjia-box").find("textarea").val();
  if(context.length <= 0)
    return;
  
  pagetype=get_page_title();
  if(pagetype == 0)
    return;
    
  materialid=get_material_id(pagetype);
  if(materialid == 0)
    return;
  
  var data = {
      data: JSON.stringify({
                "materialid":materialid,
                "context":context,
              })
  }
   
  $.ajax({
    cache: false,
    type: "POST",
    url: "/material/review",
    data: data,
    async: false,

    error: function(request){
        alert("发送评论请求失败！");
    },

    success: function(data){
      var responsedata=jQuery.parseJSON(data);
      likeid="like"+materialid;
      $('#pinglun-list1').prepend("<div class='cs-pinglun-dispaly'><span class='cs-blue'>"+responsedata.username+
      	  "</span>&nbsp;<span class='font_gray999'>"+responsedata.reviewtime+'</span><p class="cs-black">'+
      	  responsedata.context+"</p></div>");
    }
  });
}


$(function(){
    /*点击图标显示点赞，评价*/
    $(".J_discuss_btn").click(function(e){
      e.stopPropagation();
      $(this).next(".cs-discuss-tool").fadeToggle();
    });

    /*点空白区域隐藏点赞评价*/
     $("body").click(function(e){
       e.stopPropagation();
       $(".cs-discuss-tool").hide();
     }) 

    /*点赞事件*/
    $(".J_dianzan_btn").click(function(e){
      e.stopPropagation();
      if($(this).hasClass("active")){
        $(this).removeClass("active");
        $(this).html('<span class="iconfont icon-dianzan"></span> 赞');
        /*huanglin*/
        lovehandler(0);
      }else{
        $(this).addClass("active");
        $(this).html('<span class="iconfont icon-dianzan"></span> 取消');
        /*huanglin*/
        lovehandler(1);
      }
            
    })
    /*评价事件*/
    $(".J_pingjia_btn").click(function(e){
      e.stopPropagation();
      
      $(".cs-pingjia-box").find("textarea").val("");
       $(".cs-pingjia-box").find("textarea").css("height","25px");
      $(".cs-pingjia-box").fadeIn();

      $(".J_ipt").focus();
      $(".cs-discuss-tool").hide();  
    }) 


  $(".J_close_dialog").click(function(e){
      e.stopPropagation();
      
      /*huanglin*/ 
      reviewhandler();
      
      $(this).parents(".cs-pingjia-box").fadeOut();
        
    })

 /* $(".J_cancle_dialog,.J_blockdrop").click(function(e){ */
  $(".J_blockdrop").click(function(e){
      e.stopPropagation();
      $(this).parents(".cs-pingjia-box").fadeOut();      
    })

   iosFixed([{"obj":".cs-wdialog-main","bottom":0}]);
  })


