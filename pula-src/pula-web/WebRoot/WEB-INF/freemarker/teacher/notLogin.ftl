<#import "/app/macros/commonFront.ftl" as b>
<@b.html title="错误"><BR><div align="center">
<table border="0" class="grid" width="500" align="center">
  <tr class="title"> 
    <td colspan="2"><FONT COLOR="#FF0000">错误</FONT></td>
  </tr>
  <tr> 
    <td colspan="2"><IMG SRC="${base}/static/laputa/images/icons/warning.gif" WIDTH="16" HEIGHT="16" BORDER="0" ALT="" align="absbottom">尚未登录或登录已超时</td>
  </tr>
  <tr> 
   
    <td colspan="2" align="center">
	<input type="button" value="返回" onclick="goLogin();"/>
	</td>
  </tr> 
</table></div>
	
<SCRIPT LANGUAGE="JavaScript">
<!--
	function goLogin(){
		try{
			top.window.setUnload(false);
		}catch(e){

		}
		top.window.location.href = "${base}/app/my/entry?"+(new Date());
	}

	window.addEvent ( 'domready', function (){
		if(top&& top.showLogin){
			top._inject_after_login = function(){
				window.location.reload();
			}
			top.showLogin();
		}

	});
//-->
</SCRIPT>
</@b.html>