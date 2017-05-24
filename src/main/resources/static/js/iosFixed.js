/*********
ios不支持position:fixed时用position:absolute来实现固定定位
opt=[{"obj":"header","top":0},{"obj":".cs_inp_layout","top":44}]
**********/
function iosFixed(opt){
	var isIOS = (/iphone|ipad/gi).test(navigator.appVersion);    //ios系统
	var ios5up = navigator.userAgent.match(/OS [5-9]_\d[_\d]*/); //ios5及以上系统
	var android2_3up = navigator.userAgent.match(/Android [2-9].[3-9][.\d]*/);  //安卓2.3及以上系统
	if(!opt) return;
	if (isIOS) { //匹配所有ios系统
		var domArr=[];
		var timer=null;
		for(var i=0;i<opt.length;i++){
			domArr.push(opt[i].obj);
		}
		var $fixed=$(domArr.join(","));
		function handler(event) {
			for(var i=0;i<opt.length;i++){
				$(opt[i].obj).css("top",document.body.scrollTop+opt[i].top);
			}	
		}
		function initPos(){
			timer=setTimeout(function(){
				for(var i=0;i<opt.length;i++){
					$(opt[i].obj).css("top",document.body.scrollTop+opt[i].top);
				}	
			},10)	
		}
		function touchstart(event) {
			$fixed.css("opacity",0);
		}
		function touchend(event) {
			setTimeout(function(){
				$fixed.css("opacity",1);
			},10)
		}
		if(!ios5up){  //匹配ios4及一下默认不支持fixed，直接模拟
			$fixed.css({"position":"absolute","background-image":"url(about:blank)","background-attachment":"fixed"});
			handler();
			document.addEventListener("scroll", handler, false);
			document.addEventListener("touchstart", touchstart, false);
			document.addEventListener("touchend", touchend, false);
		}else{  //匹配ios5及以上，输入框聚焦时才模拟fixed
			$('input').focus(function(){
				$fixed.css({"position":"absolute","background-image":"url(about:blank)","background-attachment":"fixed"});
				initPos();
				document.addEventListener("scroll", handler, false);
				//document.addEventListener("touchstart", touchstart, false);
				//document.addEventListener("touchend", touchend, false);
			}).blur(function(){
				$fixed.attr("style","");
				document.removeEventListener("scroll", handler, false);
				document.removeEventListener("touchstart", touchstart, false);
				document.removeEventListener("touchend", touchend, false);
			});	
		}
	}	
}
