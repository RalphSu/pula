<#setting url_escaping_charset='utf-8'/>
<#include "taglibs.ftl" />
<#macro html title><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>PULA SYSTEM</title>
<link rel="Shortcut Icon" href="${base}/static/app/images/icon/logo-icon.ico" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"><meta http-equiv="x-ua-compatible" content="ie=7" />
<link rel="stylesheet" type="text/css" href="${base}/static/app/css/index.css">
<link rel="stylesheet" type="text/css" href="${base}/static/app/css/yui/menu/menu.css">
<link rel="stylesheet" type="text/css" href="${base}/static/library/yui/yui_2.8.1/build/container/assets/skins/sam/container.css">
<script type="text/javascript" src="${base}/static/library/mootools/mootools-core-1.3-full-compat-yc.js"></script>  
<script type="text/javascript" src="${base}/static/laputa/javascript/nilvar-utils.js"></script>
<script type="text/javascript" src="${base}/static/library/yui/yui_2.8.1/build/yahoo-dom-event/yahoo-dom-event.js"></script>
<script type="text/javascript" src="${base}/static/library/yui/yui_2.8.1/build/utilities/utilities.js"></script>
<script type="text/javascript" src="${base}/static/library/yui/yui_2.8.1/build/container/container_core.js"></script>
<script type="text/javascript" src="${base}/static/library/yui/yui_2.8.1/build/menu/menu-min.js"></script>
<script type="text/javascript" src="${base}/static/library/mootools/modules/mbox/mt.mbox.js"></script>      
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

	-->
</SCRIPT>	
<body class="yui-skin-sam">

        
<div><#nested/></div>

<div id="successDiv">

</div>
<!-- Page-specific script -->
<script type="text/javascript">

	
</script>


</body>
</html>
</#macro>