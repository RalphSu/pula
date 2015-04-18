
<#import "/app/macros/commonFront.ftl" as b>
<@b.html title="platform.admin.logout">

<BR><div align="center">
<table border="0" class="grid" width="500" align="center">
  <tr class="title"> 
    <td colspan="2"><@b.text key="Information"/></td>
  </tr>
  <tr> 
    <td colspan="2"><@b.text key="platform.insider.exit"/></td>
  </tr>
  <tr> 
   
    <td colspan="2" align="center">
	<input type="button" value="<@b.text key="platform.insider.backToLogin"/>" onclick="goURL('entry')"/>
	</td>
  </tr> 
</table></div>
	
<SCRIPT LANGUAGE="JavaScript">
<!--
	function goBack(){
		<#if backURL?exists>
		//goURL("${backURL?if_exists}");
		<#else/>
		//history.back();
		</#if>
		
		history.back();
	}
//-->
</SCRIPT></@b.html>
