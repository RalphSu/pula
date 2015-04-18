
<#import "/app/macros/commonDialog.ftl" as b>
<@b.html title="platform.insider.editPassword">
    
<form action="_changePassword" method="post" id="cpForm">
<table border="0" class="grid" width="600"><colgroup>
<col style="width:100px"/>
<col/>
</colgroup>
  <tr class="title"> 
    <td colspan="2"><@b.text key="platform.insider.inputPassword"/></td>
  </tr>
<tr>
	<td width="100"><@b.text key="platform.insider.currentPassword"/></td>
	<td ><input name="oldPassword" type="password" id="oldPassword"/></td>
</tr>
<tr>
	<td width="100"><@b.text key="platform.insider.newPassword"/></td>
	<td ><input name="newPassword" type="password" id="newPassword"/></td>
</tr>
<tr>
	<td width="100"><@b.text key="platform.insider.confirmNewPassword"/></td>
	<td ><input name="newPassword2" type="password" id="newPassword2" /><@b.text key="platform.insider.inputNewPasswordAgain"/></td>
</tr>
<tr>
	<td></td>
	<td ><input type="submit" value="<@b.text key="submitBtn"/>">
	<input type="reset" value="<@b.text key="resetBtn"/>">
	<input type="button" value="<@b.text key="GoBack"/>" onclick="goBack()">
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
								topSuccess();
								top.closeDialog();
							}
						}
					}).send($('cpForm').toQueryString());

		});
//-->
</SCRIPT>
</@b.html>
