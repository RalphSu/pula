<#setting url_escaping_charset='utf-8'/>
<#include "taglibs.ftl" />
<#macro html title>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>${title?if_exists?html}</title>
<link rel="Shortcut Icon" href="${base}/static/app/images/icon/logo-icon.ico" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"><meta http-equiv="x-ua-compatible" content="ie=7" />
  <link rel="stylesheet" type="text/css" href="${base}/static/app/css/base.css?">
<script type="text/javascript" src="${base}/static/library/yui/yui_2.8.1/build/yahoo/yahoo.js"></script>
<script type="text/javascript" src="${base}/static/library/yui/yui_2.8.1/build/event/event.js"></script>
<script type="text/javascript" src="${base}/static/library/yui/yui_2.8.1/build/dom/dom.js"></script>
<script type="text/javascript" src="${base}/static/library/mootools/mootools-core-1.4.1-src.js"></script> 
  <script type="text/javascript" src="${base}/static/laputa/javascript/nilvar-validation.js"></script>
  <script type="text/javascript" src="${base}/static/laputa/javascript/nilvar-utils.js"></script>
  <script type="text/javascript" src="${base}/static/laputa/javascript/nilvar-form.js"></script>
  <script type="text/javascript" src="${base}/static/laputa/javascript/pager.js"></script>
  <script type="text/javascript" src="${base}/static/laputa/javascript/helper.js"></script>
  <script type="text/javascript" src="${base}/static/laputa/javascript/puerta-mix.js"></script>
  <script type="text/JavaScript" src="${base}/static/app/javascript/lang/${env.lang}.js" ></script>
  <script type="text/JavaScript" src="${base}/static/app/javascript/globals.js" ></script>
</head>
<SCRIPT LANGUAGE="JavaScript">
<!--
	function goBack(){
		
		goURL('${backURL?if_exists?js_string}');

	}
	
var pa_vars = {
	base :'${base}'
}
	-->
</SCRIPT>	
<body bgcolor="#FFFFFF" class="yui-skin-sam"><#nested/>


<script type="text/javascript" src="${base}/static/laputa/javascript/bottom.js"></script>
<!-- <script type="text/javascript" src="${base}/static/app/javascript/tracker.js"></script> -->
</body>
</html>

</#macro>
