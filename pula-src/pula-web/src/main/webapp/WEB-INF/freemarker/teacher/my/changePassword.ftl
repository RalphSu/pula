<#import "/teacher/macros/common.ftl" as b>
<@b.html title="修改密码">
    
<form action="_changePassword" method="post" id="cpForm">
<table border="0" class="grid" width="600"><colgroup>
<col style="width:100px"/>
<col/>
</colgroup>
  <tr class="title"> 
    <td colspan="2">请填写密码信息</td>
  </tr>
<tr>
	<td width="100">当前密码</td>
	<td ><input name="oldPassword" type="password" id="oldPassword"/></td>
</tr>
<tr>
	<td width="100">新密码</td>
	<td ><input name="newPassword" type="password" id="newPassword"/></td>
</tr>
<tr>
	<td width="100">确认新密码</td>
	<td ><input name="newPassword2" type="password" id="newPassword2" />请将新密码再填写一次</td>
</tr>
<tr>
	<td></td>
	<td ><input type="submit" value="<@b.text key="submitBtn"/>">
	<input type="hidden" name="_json" value="1">
	</td>
</tr>
</table>
</form>

<script language="javascript">
<!--
	window.addEvent('domready',function(){
		focusDelay('oldPassword');
	});
	

	function check(){
		if(isEmpty($("oldPassword").value)){
			alert("<@b.text key="platform.insider.inputOldPassword"/>");
			$("oldPassword").focus();
			return false; 
		}

		if(isEmpty($("newPassword").value)){
			alert("<@b.text key="platform.insider.inputNewPassword"/>");
			$("newPassword").focus();
			return false; 
		}

		if(($("newPassword").value)!=($("newPassword2").value)){
			alert("<@b.text key="platform.insider.confirmPassword"/>");
			$("newPassword").focus();
			return false; 
		}

		return true ;
	}

		$('cpForm').addEvent('submit',function (e){
			e.stop();
			if(!check()) return ;
			var aj = new Request.JSON({
						url:$('cpForm').get('action'),
						onSuccess:function (e){
							if(e.error){
								alert(e.message);
							}else{
							}
						}
					}).send($('cpForm').toQueryString());

		});
//-->
</SCRIPT>
</@b.html>
