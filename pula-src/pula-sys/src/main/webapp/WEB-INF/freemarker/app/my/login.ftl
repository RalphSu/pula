<#import "/app/macros/taglibs.ftl" as b>
<html>
<head>
<title>普拉星球-教学管理系统</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="${base}/static/app/css/index.css">
  <script type="text/javascript" src="${base}/static/library/mootools/mootools-core-1.3-full-compat-yc.js"></script>
  <script type="text/javascript" src="${base}/static/laputa/javascript/helper.js"></script>
</head>
<style>

input{margin-left:20px}
body{background:url("${base}/static/app/images/index/login_bg.jpg" ) no-repeat top center #c5e5e4}

</style>
<body bgcolor="#c0c0c0" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">

<div style="width:400px;margin:0px auto;margin-top:560px;padding-left:40px;">

<form method="post" id="loginForm" action="${base}/app/my/login" onsubmit="return login()" >
<table border="0"  width="300">
<colgroup><col style="width:100px"/><col/></colgroup>
  
<tr>
	<td align="right">登录帐号</td>
	<td ><input name="loginId" type="text" id="loginId"/></td>
</tr>
<tr>
	<td align="right">登录密码</td>
	<td ><input name="password" type="password" id="userPwd"/></td>
</tr>
<tr>
	<td></td>
	<td ><input type="submit" value="登录">
	<input type="hidden" value="1" name="_json">
	</td>
</tr>
</table>
</form>
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