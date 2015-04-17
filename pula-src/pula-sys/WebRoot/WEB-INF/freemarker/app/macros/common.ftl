<#setting url_escaping_charset='utf-8'/>
<#include "taglibs.ftl" />
<#macro html title><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>PULA SYSTEM</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"><meta http-equiv="x-ua-compatible" content="ie=7" />
<link rel="stylesheet" type="text/css" href="${base}/static/app/css/index.css">
<link rel="stylesheet" type="text/css" href="${base}/static/app/css/yui/menu/menu.css">
<link rel="stylesheet" type="text/css" href="${base}/static/library/yui/yui_2.8.1/build/container/assets/skins/sam/container.css">
<script type="text/javascript" src="${base}/static/library/mootools/mootools-core-1.3-full-compat-yc.js"></script>  
<script type="text/javascript" src="${base}/static/laputa/javascript/nilvar-utils.js"></script>
<script type="text/javascript" src="${base}/static/library/yui/yui_2.8.1/build/yahoo-dom-event/yahoo-dom-event.js"></script>
<script type="text/javascript" src="${base}/static/library/yui/yui_2.8.1/build/utilities/utilities.js"></script>
<script type="text/javascript" src="${base}/static/library/yui/yui_2.8.1/build/container/container_core.js"></script>
<script type="text/javascript" src="${base}/static/library/yui/yui_2.8.1/build/menu/menu-tiyi.js"></script>
<script type="text/javascript" src="${base}/static/library/mootools/modules/mbox/mt.mbox.js"></script>
<script type="text/javascript" src="${base}/static/library/puerta/t-tabframe.js"></script>  
</head>
<SCRIPT LANGUAGE="JavaScript">
<!--

	function goBack(){
		<#if backURL?exists>
		goURL('${backURL?if_exists?js_string}');
		<#else>
		history.back();
		</#if>

	}

	function logout(){
		if(confirm('<@b.text key="common.logoutConfirm"/>')){
			setUnload(false);
			goURL2("${base}/app/my/logout",top);
		}
	}
	var _inject_after_login = null ;

	function reLogin(){
		var a =  $F('loginPassword');
		if(a=="") return ;
		var url = "${base}/app/my/login" ;
		//alert(url);
		var myXHR = new Request({url:url,method:'post'});
		myXHR.addEvent( 'onSuccess', function (e ) { 
			var obj = JSON.decode(e);
			$("loginBtn").disabled= false ;			
			if(obj.error){
				alert(obj.message);
			}else{
				//window.status = (obj.message);
				showSuccess();
				$('loginDialog').style.display = "none";	
				$('loginPassword').value = "";
				if(_inject_after_login){
					_inject_after_login();
					_inject_after_login = null;
				}
			}
		} );
		$("loginBtn").disabled= true ;
		myXHR.send('_json=1&loginId=${sessionUser.loginId?if_exists?js_string}&password='+a);
		
	}

	function showLogin(){
		if($('loginDialog').style.display == "block"){
			$('loginDialog').style.display = "none";		
		}else{
			$('loginDialog').style.display = "block";
			centerIt($('loginDialog'));
			$('loginPassword').focus();
		}
	}

	var theTimer = null ;
	function showSuccess(){
		if(theTimer!=null)clearTimeout(theTimer);
		
		
		var myEffect = new Fx.Morph('successDiv', {duration: 'long', transition: Fx.Transitions.Sine.easeOut});
		myEffect.start({
			'opacity':[0,0.9]
		});
		$('successDiv').setStyle('display','block');
		centerIt($('successDiv'));
		theTimer = window.setTimeout( function () {
			$('successDiv').morph({
				'opacity': '0'
			});
		},1500);
	}

	function hideSuccess(){
		if(theTimer!=null)clearTimeout(theTimer);
		$('successDiv').setStyle('display','none');
	}

	function centerIt(obj){
		
		var w = window.getScrollWidth() - obj.getSize().x;
		var h = window.getScrollHeight() - obj.getSize().y;
		obj.setStyle('left',(w /2 )) ;
		obj.setStyle('top',(h /2 )) ;
	}

	function cPwd(){
		
		openDialog({
				title: '<@text key="platform.insider.editPassword"/>',
				url: '${base}/app/my/changePassword',
				width: 620,
				height: 200
		});
	}

	function closeDialog(){
		Mbox.close();
	}
	var dialogCaller = null ;
	function openDialog(conf){
		top.Mbox.open({
			title: conf.title,
			url: conf.url,
			width: conf.width,
			height: conf.height,
			type: "iframe",
			ajax: true,
			onClose: conf.onClose
			
		});

		dialogCaller = conf.caller;
	}

function unloadMsg(){
    msg = "确认离开当前页面？"
    return msg;
}
function setUnload(on){
    window.onbeforeunload = (on) ? unloadMsg : null;
}
setUnload(true);
	-->
</SCRIPT>	
<body class="yui-skin-sam">
<div id="indexPanel">
	<div id="logoPanel" ondblclick="showLogin()"/></div>
	<div id="rightWrapper">
	
	</div>
	<div id="rightPanel">
		<a >${sessionUser.loginId?if_exists?html} (${sessionUser.name?if_exists?html}) <#if sessionUser.props.BRANCH??> ${sessionUser.props.BRANCH.name?if_exists?html} </#if></a>
		<A HREF="#" onclick="goMenu('${base}/app/my/welcome','HOME','首页')" id="lnkHome">首页</A>
		<A HREF="#" onclick="javascript:cPwd()" >修改密码</A> <A HREF="#" onclick="javascript:logout();">注销</A></div>
	</div>
</div>
<div id="midPanel">
<div id="menuPanel" class="tab-left"  onselectstart="return false;" style="-moz-user-select:none;"></div>
<div class="tabs-tool tab-left">
	<div class="tab-prev" id="prevTabLnk" ><A href="#" ><span></span></A></div>
	<div class="tab-next" id="nextTabLnk"><A href="#" ><span></span></A></div>
</div>
<div id="tabsWrapper">

	<div id="tabsPanel">
		<div class="c"></div>
	</div>

</div>

<div class="c"></div>
</div>
        
<div id="t-body"><#nested/></div>
<div id="loginDialog">
<@b.text key="common.password"/><input type="password" id="loginPassword" onkeydown="if(event.keyCode==13){reLogin();}"> <input type="button" id="loginBtn" onclick="reLogin()" value="<@b.text key="common.login"/>"/>
</div>
<div id="successDiv">

</div>
<div id="realMode" class="h" >
生产环境
</div>
<!-- Page-specific script -->
<script type="text/javascript">

	function goMenu(link,no,name){
		if(oTabFrame.length()>=8 && ! oTabFrame.hasTab(no) ){
			alert("已达到页面数量上限；请关闭不用的页面");
			return ;
		}
		oTabFrame.closeTab('HOME') ;//always close home
		oTabFrame.addTab ( name ,no ,link ) ;
	}

	function onMenuItemClick(p_sType, p_aArgs, p_oValue) {

		goMenu('${base}/app/'+p_oValue[0],p_oValue[1],p_oValue[2]);
	
	}

            

            // "load" event handler for the window
			var oMenuBar ;
			var oTabFrame;
            YAHOO.example.onWindowLoad = function(p_oEvent) {
                var aItemData = ${sessionUser.menu?if_exists} ;
                oMenuBar = new YAHOO.widget.MenuBar("myMenuBar", { itemdata:aItemData,lazyload:true });
                // Render the MenuBar instance and corresponding submenus
                oMenuBar.render($('menuPanel'));
				

				oTabFrame = new TTabFrame({container:'tabsPanel',wrapper:'tabsWrapper',prevTab:'prevTabLnk',nextTab:'nextTabLnk'});

				//resizeFrame();
				goMenu('${base}/app/my/welcome','HOME','首页');
				window.addEvent('resize', oTabFrame.resize.bind(oTabFrame));
            }


            // Add a "load" handler for the window

            YAHOO.util.Event.addListener(window, "load", YAHOO.example.onWindowLoad);

	function resizeFrame(){
		
		var frame = oTabFrame.activedFrame ; 
		if(frame==null){
			return ; 
		}
		var h = ($("indexPanel").clientHeight) + ($("menuPanel").clientHeight) ;
		//window.top.document.title= document.documentElement.clientHeight;
		if(document.documentElement.clientHeight>(h+20)){
			frame.setStyle('height',document.documentElement.clientHeight - h -10);
		}
		frame.style.visibility='visible';
		frame = null ;
	}
	
	window.addEvent('resize', resizeFrame);	
	
	var log = function(v){
		if(this.logger){			
		}else{
			this.logger = new Element('div.logger');
			this.logger.inject(document.body);
		}
		this.logger.innerHTML += "<BR/>" + JSON.encode(v) ;
	}
	<#if sessionUser.loginId??&&sessionUser.loginId=="tiyi">
	window.addEvent('domready',function(){
		if (window.location.host.indexOf ('localhost')!=0 )
		{
			$('realMode').removeClass('h');
		}
	});
	</#if>
        </script>




</body>
</html>
</#macro>