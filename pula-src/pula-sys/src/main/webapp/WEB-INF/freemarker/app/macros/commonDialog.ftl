<#setting url_escaping_charset='utf-8'/>
<#include "taglibs.ftl" />
<#macro html title><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>${title?if_exists?html}</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"><meta http-equiv="x-ua-compatible" content="ie=7" />
  <link rel="stylesheet" type="text/css" href="${base}/static/app/css/base.css?">
  <script type="text/javascript" src="${base}/static/library/mootools/mootools-core-1.3-full-compat-yc.js"></script>
  <script type="text/javascript" src="${base}/static/laputa/javascript/nilvar-validation.js"></script>
  <script type="text/javascript" src="${base}/static/laputa/javascript/nilvar-utils.js"></script>
  <script type="text/javascript" src="${base}/static/laputa/javascript/nilvar-form.js"></script>
  <script type="text/javascript" src="${base}/static/laputa/javascript/helper.js"></script>
  <script type="text/javascript" src="${base}/static/laputa/javascript/pager.js"></script>
  <script type="text/javascript" src="${base}/static/laputa/javascript/puerta-mix.js"></script>
</head>
<SCRIPT LANGUAGE="JavaScript">
<!--
	function goBack(){		
		top.Mbox.close();
	}
	document.addEvent('keydown',function(e){		
		if(e.key=='esc') {
			top.closeDialog();
		}
	});

	function disableBtn3(b){
		$("submitBtn").disabled = b ;
		$("resetBtn").disabled = b; 
		$("closeBtn").disabled = b; 
	}
	-->
</SCRIPT>	
<body bgcolor="#FFFFFF" class="yui-skin-sam"><#nested/>

<!-- <script type="text/javascript" src="${base}/static/app/javascript/tracker.js"></script> -->
</body>

</html>

</#macro>