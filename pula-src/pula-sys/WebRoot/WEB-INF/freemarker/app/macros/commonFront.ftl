<#setting url_escaping_charset='utf-8'/>
<#include "taglibs.ftl" />
<#macro html title>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN"><html>
<head>
<title><@text key="${title?html}"/></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" type="text/css" href="${base}/static/laputa/css/front.css">
<link rel="stylesheet" type="text/css" href="${base}/static/laputa/css/table.css">
<script type="text/javascript" src="${base}/static/library/mootools/mootools-core-1.3-full-compat-yc.js"></script>
<script type="text/javascript" src="${base}/static/laputa/javascript/nilvar-utils.js"></script>
</head>

<SCRIPT LANGUAGE="JavaScript">
<!--

	var webTemplatePath = '';

	window.addEvent('domready',function(){
		
		if(window.parent!=window){
			$('topBar').style.display = "none" ;
			//$('menuBar').style.display = "none" ;
		}else{
			document.body.className="front";
		}	
	});
	-->
</SCRIPT>	
<body bgcolor="#FFFFFF" >



<div id="topBar">
	<div id="leftBar">
		<div id="logo">
			PULA SYSTEM
		</div>
	</div>
	<div id="rightBar">
		<div id="userBar"></div>
		<div class="topBtn"><A HREF="${base}/app/my/entry" target="_top"><@text key="common.login"/></A></div>
	</div>


</div>

<div >
	<#nested/>
</div>



</body>
</html>
</#macro>
