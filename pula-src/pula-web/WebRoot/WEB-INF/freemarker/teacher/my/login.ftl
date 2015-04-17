<#import "/teacher/macros/taglibs.ftl" as b>
<html>
<head>
<title>普拉星球-教学管理系统</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="${base}/static/teacher/css/index.css">
  <script type="text/javascript" src="${base}/static/library/mootools/mootools-core-1.3-full-compat-yc.js"></script>
  <script type="text/javascript" src="${base}/static/laputa/javascript/helper.js"></script>
</head>
<style>

</style>
<body bgcolor="#c0c0c0" marginwidth="0" marginheight="0">
<div id="loginDiv">

<form method="post" id="loginForm" action="${base}/teacher/my/login" onsubmit="return login()">
<input name="loginId" type="text" id="loginId"/>
<input name="password" type="password" id="userPwd"/>
	<input type="hidden" value="1" name="_json">
	<input type="submit" value="确定" id="btnSubmit">
</form>


<a id="btnStudentMode" href="${base}/student/my/entry"></a>
<a id="btnTeacherMode" href="${base}/teacher/my/entry"></a>

</div>

</div><SCRIPT type="text/javascript">
<!--
	function login(){
		if($('loginId').value == ""){
			alert("请填写登录账号");
			$('loginId').focus();
			return false;
		}
		if($('userPwd').value == ""){
			alert("请填写登录密码");
			$('userPwd').focus();
			return false;
		}
		gf($('loginForm').action,$('loginForm').toQueryString(),function (e){
			if(e.error){
				alert(e.message);
			}else{
				window.location.reload();
			}

		});
		return false;
	}

	window.addEvent('domready',function(){ $('loginId').focus();
		$$('.text').addEvent('keydown',function (e){
			if(e.key == 'enter'){
				login();
			}
		});
	
	});
//-->
</SCRIPT>
</body>
</html>