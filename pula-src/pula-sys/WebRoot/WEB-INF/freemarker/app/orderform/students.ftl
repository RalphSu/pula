<#import "/app/macros/commonBase.ftl" as b><@b.html title="学员课程进度查询">
<style>
.t-simple-no input{ width: 100px}
.t-simple-no select{ width:100px}
</style>
  <div class="top" id="__top"> </div>
<div class="body">
    <!-- condition -->
<div id="conditionDiv" class="h forList">
	<form action="${uri}" method="get" id="searchForm">
	<table border="0" class="grid" width="1000" id="queryForm"><colgroup>
	<col style="width:70px"/>
	<col style="width:140px"/><col style="width:70px"/>
	<col style="width:90px"/><col style="width:80px"/>
	<col style="width:90px"/><col style="width:80px"/>
	<col />
	</colgroup>
	<#assign total_col=8>
          <tr class="title">
            <td colspan="${total_col}">查询条件</td></tr>
		 <tr> 
		<td>订单号</td>
		<td>
		<input type="text" name="condition.no" value="${condition.no?if_exists?html}" style="width:100px"/>
		</td>
		<td>订单日期</td>
	 <td colspan="3"><input type="text" value="" name="condition.beginDate" id="beginDate" maxlength="10" size="12" class="dateField"/>
		 <img src='${base}/static/laputa/images/icons/arrow_blue.gif' border=0 align="absmiddle">
		 <input type="text" value="" name="condition.endDate" id="endDate" maxlength="10" size="12" class="dateField"/> </td>

		 <td>学员编号</td>
		 <td><div id="sStudent"></div></td>
	  </tr>
	  <tr> 
<td>课程产品</td>
		<td <#if !headquarter>colspan="7"</#if>>
		<select name="condition.courseProductId" id="condition.courseProductId">
		    <option value="0">(全部)</option>
			<#list courseProducts as cp>
		    <option value="${cp.id}" >${cp.name!?html}</option>
			</#list>
        </select>
		</td>
		<#if headquarter><td>分支机构</td>
		<td colspan="5">
		<select name="condition.branchId" id="condition.branchId">
		<option value="0">(全部)</option>
		<#list branches as tp>
		<option value="${tp.id?if_exists?html}">${tp.no!?html} ${tp.name?if_exists?html}</option>
		</#list>
		</select>
		</td></#if>
				 
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
<script type="text/javascript" src="${base}/static/app/javascript/orderform_students.js"></script>
<script type="text/javascript" src="${base}/static/library/mootools/modules/mbox/mt.mbox.js"></script>
<script type="text/javascript" src="${base}/static/library/puerta/t-simple-no.js"></script>
<script type="text/javascript">

var pageVars = {
	queryString:'',
	action : '_create',
	id :'',
	base :'${base}'
}

var pageConsts= {
	PS_PREPAY : 2 ,
	S_INPUT : 1
}
var lang = {
	name:'名称',no:'编号',domain:'学员课程进度查询',gender:'性别',status:'状态',print:'打印条码',printOK:'选中项已成功输出打印',file:'档案'
	,view:'查看',birthday:'生日',branchName:'所在分支机构',assign:'指派',levelName:'级别',barcode:'卡号',tipsOfTSN:'输入名称加载'
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