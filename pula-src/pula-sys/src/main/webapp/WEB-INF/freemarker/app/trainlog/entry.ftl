<#import "/app/macros/commonBase.ftl" as b><@b.html title="employee.title">
  <div class="top" id="__top"> </div>
<div class="body">
    <!-- condition -->
    <div id="conditionDiv" class="h forList">
	<form action="${uri}" method="get" id="searchForm">
	<table border="0" class="grid" width="<#if headquarter>900<#else>650</#if>" id="queryForm"><colgroup>
	<col style="width:70px"/>
	<col /><col style="width:80px"/>
	<#if headquarter><#assign total_col=6><col /><col style="width:60px"/><#else><#assign total_col=4></#if>
	<col style="width:280px"/>
	</colgroup>
          <tr class="title">
            <td colspan="${total_col}">查询条件</td></tr>
		 <tr> 
		<td>关键词</td>
		<td>
		<input type="text" name="condition.keywords" value="${condition.keywords?if_exists?html}"/>
		</td>
		<#if headquarter><td>分支机构</td>
		<td>
		<select name="condition.branchId" id="condition.branchId">
		<option value="0">(全部)</option>
		<#list branches as tp>
		<option value="${tp.id?if_exists?html}">${tp.no!?html} ${tp.name?if_exists?html}</option>
		</#list>
		</select>
		</td></#if><td>日期</td>
	 <td ><input type="text" value="${condition.beginDate?if_exists?html}" name="condition.beginDate" id="beginDate" maxlength="10" size="12" class="dateField"/>
		 <img src='${base}/static/laputa/images/icons/arrow_blue.gif' border=0 align="absmiddle">
		 <input type="text" value="${(condition.endDate?if_exists?html)}" name="condition.endDate" id="endDate" maxlength="10" size="12" class="dateField"/> </td>
	  </tr>
	  <tr> 
		<td colspan="${total_col}">
	<input type="submit" value="查询" id="searchBtn"/>

		</td>
	  </tr> 
	</table>
	 </form>
	 </div>


    <!-- listview -->
    <div class="l forList" id="listview">
      <form id="batchForm">
        <div id="dt"> </div>
      </form>
      <!-- left ends --> 
    </div>

	<!-- edit form -->
	<div id="inputPanel" class="l h">
	
	</div>
  </div>


<!-- others -->
<#include "/calendar.ftl"/>
<link rel="stylesheet" type="text/css" href="${base}/static/app/css/t-style.css"></link>
<script type="text/javascript" src="${base}/static/library/puerta/t-table.js"></script> 
<script type="text/javascript" src="${base}/static/app/javascript/trainlog.js"></script>
<script type="text/javascript" src="${base}/static/library/mootools/modules/mbox/mt.mbox.js"></script>
<script type="text/javascript" src="${base}/static/library/puerta/t-simple-no.js"></script>
<script type="text/javascript">

var pageVars = {
	queryString:'',
	action : '_create',
	id :'',
	base :'${base}'
}

var lang = {
	name:'名称',no:'编号',domain:'培训记录登记',gender:'性别',status:'状态',print:'打印条码',printOK:'选中项已成功输出打印',file:'档案'
	,view:'查看',birthday:'生日',branchName:'所在分支机构',assign:'指派',levelName:'级别',barcode:'卡号'
}

	function checkAssign(){
		if(isEmpty($F("branchId"))){
			alert("请选取分支机构");
			$("branchId").focus();
			return false;
		}

		return true ;
	}

	function check(){
		

		
		return true ;
	}
</script>
</@b.html>