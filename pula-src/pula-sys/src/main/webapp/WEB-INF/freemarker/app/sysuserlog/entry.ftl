<#import "/app/macros/commonBase.ftl" as b><@b.html title="platform.sysUserlog">
<#include "/calendar.ftl"/>
<div class="top" id="__top"> </div>
  <div class="body">
	<!-- condition -->
	<div id="conditionDiv" class="h ">
	<form action="${uri}" method="get" id="searchForm">
	<table border="0" class="grid" width="700" id="queryForm">
	<colgroup>
	<col style="width:80px"/><col />
	<col style="width:80px"/><col />
	</colgroup>
	   <tr class="title"> 
		<td colspan="4">查询条件</td>
	  </tr>   <tr> 
		<td>用户</td>
		<td>
		<input type="text" name="condition.userId" value="${(condition.userId?html)?if_exists}" size="18"/>
		</td>   
		<td>日期</td>
	 <td><input type="text" value="${condition.beginDate?if_exists?html}" name="condition.beginDate" id="beginDate" maxlength="10" size="12"/>
		 <img src='${base}/static/laputa/images/icons/arrow_blue.gif' border=0 align="absmiddle">
		 <input type="text" value="${(condition.endDate?if_exists?html)}" name="condition.endDate" id="endDate" maxlength="10" size="12"/> </td>
	</tr><tr><td><@b.text key="platform.insiderlog.event"/></td>
		<td>
		<input type="text" name="condition.event" value="${(condition.event?html)?if_exists}" size="18"/>
		</td><td><@b.text key="platform.insiderlog.extendInfo"/></td>
			<td >
		<input type="text" name="condition.extendInfo" value="${(condition.extendInfo?html)?if_exists}"/>
		</td>
	  </tr>
		
	  <tr> 
		<td colspan="4">
		<input type="submit" value="查询" id="searchBtn"/>
		<input type="reset" value="重填" id="resetBtn"/>
		</td>
	  </tr> 
	</table>
	 </form></div>

	<div class="l ">


	<form id="batchForm">
	<div id="dt">
	</div>
	</form>
	<!-- left ends -->
	</div>
	<div class="c"></div>

</div>
<!-- edit form -->

<!-- others -->
<link rel="stylesheet" type="text/css" href="${base}/static/app/css/t-style.css"></link>
<script type="text/javascript" src="${base}/static/library/puerta/t-table.js"></script> 
<script type="text/javascript" src="${base}/static/app/javascript/sysuserlog.js"></script>
<script type="text/javascript" src="${base}/static/library/mootools/modules/mbox/mt.mbox.js"></script>
<script type="text/javascript">

var pageVars = {
	queryString:'',
	action : '_create',
	id :'',
	base :'${base}'
}

var lang = {
	eventTime :'日期时间',
	actor:'用户',
	ip:'IP地址',
	event:'事件',
	extendInfo:'扩展信息',domain:'用户日志'
}

	
</script>
</@b.html>