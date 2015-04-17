<#import "/app/macros/commonBase.ftl" as b>
<@b.html title="导入成功"><BR><div align="center">
<table border="0" class="grid" width="500" align="center">
  <tr class="title"> 
    <td colspan="2">导入完成</td>
  </tr>
  <tr> 
    <td colspan="2">
	导入总数: ${result.count?string("#")}
	</td>
  </tr>
  <tr> 
   
    <td colspan="2" align="center">
	<input type="button" value="返回" onclick="goBack();"/>
	</td>
  </tr> 
</table></div>
	
<SCRIPT LANGUAGE="JavaScript">
<!--
	function goBack(){
		
		history.back();
	}
//-->
</SCRIPT>
</@b.html>