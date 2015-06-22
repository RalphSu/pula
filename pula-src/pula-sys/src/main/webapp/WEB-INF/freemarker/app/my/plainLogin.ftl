<#import "/app/macros/taglibs.ftl" as b>
<html>
<head>
<title>普拉星球-教学管理系统</title>
<link rel="Shortcut Icon" href="${base}/static/app/images/icon/logo-icon.ico" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="${base}/static/app/css/index.css">
  <script type="text/javascript" src="${base}/static/library/mootools/mootools-core-1.3-full-compat-yc.js"></script>
  <script type="text/javascript" src="${base}/static/laputa/javascript/helper.js"></script>
</head>
<style>

</style>
<body bgcolor="#c0c0c0" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<div align="center" style="padding-top:40px">

<form method="post" id="loginForm" action="${base}/app/my/login" onsubmit="return login()">
<table border="0" class="grid" width="300" align="center">
<colgroup><col style="width:100px"/><col/></colgroup>
  <tr class="title"> 
    <td colspan="2">普拉星球-教学管理系统
	</td>
  </tr>
<tr>
	<td >登录帐号</td>
	<td ><input name="loginId" type="text" id="loginId"/></td>
</tr>
<tr>
	<td>登录密码</td>
	<td ><input name="password" type="password" id="userPwd"/></td>
</tr>
<tr>
	<td></td>
	<td ><input type="submit" value="确定">
	<input type="reset" value="重填">
	<input type="hidden" value="1" name="_json">
	</td>
</tr>
</table></div></form>
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