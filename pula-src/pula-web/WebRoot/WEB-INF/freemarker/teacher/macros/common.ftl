<#setting url_escaping_charset='utf-8'/>
<#include "taglibs.ftl" />
<#macro html title pageId><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>PULA SYSTEM</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"><meta http-equiv="x-ua-compatible" content="ie=7" />
<link rel="stylesheet" type="text/css" href="${base}/static/teacher/css/index.css">

<script type="text/javascript" src="${base}/static/library/mootools/mootools-core-1.3-full-compat-yc.js"></script>  
<script type="text/javascript" src="${base}/static/laputa/javascript/nilvar-utils.js"></script>
<script type="text/javascript" src="${base}/static/laputa/javascript/nilvar-validation.js"></script>
  <script type="text/javascript" src="${base}/static/laputa/javascript/puerta-mix.js"></script>
  <script type="text/JavaScript" src="${base}/static/teacher/js/lang/${env.lang}.js" ></script>
  <script type="text/javascript" src="${base}/static/laputa/javascript/pager.js"></script>
</head>
<SCRIPT LANGUAGE="JavaScript">
<!--

		function goBack(){
		
		goURL('${RequestParameters.backURL?if_exists?js_string}');

	}


	-->
</SCRIPT>	
<body>
<div id="container_wrapper">
	<div class="container" id="${pageId!?html}">
	<div id="userInfoDiv">@ ${_sess.getProps("S_BRANCH").name!?html}</div>
		<a href="${base}/teacher/score/entry" id="btnScore"/></a>
		<a href="${base}/teacher/calendar/entry" id="btnCalendr"/></a>
		
		<div id="wrapper">
		<#nested/>
		</div>


		<a href="${base}/student/my/changePassword" id="btnChangePassword"></a>
		<a href="${base}/student/my/logout" id="btnLogout"></a>

	</div>
</div>
</body>
</html>
</#macro>