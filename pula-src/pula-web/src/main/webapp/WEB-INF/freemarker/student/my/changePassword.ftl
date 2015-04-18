<#import "/student/macros/common.ftl" as b>
<@b.html title="修改密码" pageId="container_changePassword">
    
<form action="_changePassword" method="post" id="cpForm">
<input name="oldPassword" type="password" id="oldPassword"/>
<input name="newPassword" type="password" id="newPassword"/>
<input name="newPassword2" type="password" id="newPassword2" placeholder="请把密码再填一次"/>
<input type="submit" id="btnSubmit" style="position:absolute;left:380px;top:500px;width:80px;height:40px;text-indent:-30em;background:transparent;border:1px solid blue">
	<input type="hidden" name="_json" value="1">

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
								alert('OK');
							}
						}
					}).send($('cpForm').toQueryString());

		});
//-->
</SCRIPT>
</@b.html>
