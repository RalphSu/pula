<#import "/app/macros/commonBase.ftl" as b><@b.html title="教师">
<style>
.top .text{
	line-height:25px;
	height:25px;
	margin-top:3px;
	color:#5f5f5f;
}


</style>
<#assign wt_target="personal_logs"/>
<#include "teacherTop.inc.ftl"/>
<div class="body">
    <!-- condition -->
	
<div id="conditionDiv" class="h forList">
		<form action="${uri}" method="get" id="searchForm">
	<table border="0" class="grid" width="700" id="queryForm"><colgroup>
	<col style="width:80px"/>
	<col />
	<#assign total_col=2/>
	<#if headquarter>
	<#assign total_col=4/>
	<col style="width:80px"/>
	<col />
	</#if>
	</colgroup>
          <tr class="title">
            <td colspan="${total_col}">查询条件</td></tr>
		 <tr> 
		<td>日期</td>
	 <td colspan="1"><input type="text" value="${condition.beginDate?if_exists?html}" name="condition.beginDate" id="beginDate" class='dateField' maxlength="10" size="12"/>
		 <img src='${base}/static/laputa/images/icons/arrow_blue.gif' border=0 align="absmiddle">
		 <input type="text" value="${(condition.endDate?if_exists?html)}" name="condition.endDate" id="endDate" class='dateField' maxlength="10" size="12"/> </td>
	 
	  <#if headquarter><td>分校</td>
		<td >
		<select name="condition.branchId" id="condition.branchId">
		<option value="0">(全部)</option>
		<#list branchList as tp>
		<option value="${tp.id?if_exists?html}">${tp.no!?html} ${tp.name?if_exists?html}</option>
		</#list>
		</select>
		
		</td>
	</#if>	
		</tr>
	  <tr> 
		<td colspan="${total_col}">
		<input type="submit" value="查询" id="searchBtn"/>
		<input type="reset" value="重填" id="resetBtn"/>
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
</div>	
	

<!-- others -->
<#include "/calendar.ftl"/>
<link rel="stylesheet" type="text/css" href="${base}/static/app/css/t-style.css"></link>
<script type="text/javascript" src="${base}/static/library/puerta/t-table.js"></script> 
<script type="text/javascript" src="${base}/static/app/javascript/teacher_personallogs.js"></script>
<script type="text/javascript" src="${base}/static/library/mootools/modules/mbox/mt.mbox.js"></script>
<script type="text/javascript" src="${base}/static/library/puerta/t-simple-no.js"></script>
<script type="text/javascript" src="${base}/static/library/puerta/t-datagrid.js"></script>
<script type="text/javascript" src="${base}/static/library/mootools/modules/UUID.js"></script>
<script type="text/javascript">

var pageVars = {
	queryString:'',
	action : '_create',
	id :'',
	base :'${base}',
	imagePath:'${base}/static/laputa/images/icons/',
	noPhoto:'${base}/static/app/images/nophoto.jpg'
}

var lang = {
	name:'名称',no:'编号',domain:'查看教师',
	materialNo:'材料编号',
	materialName:'材料名称',space:'区域',picNo:'图号',msgSelectFirst:'请选择...',uploadPic:'上传照片',uploadFile:'上传附件',
	quantity:'数量',lineMsg:"第{index}行:{message}\n",needQty:'需要填写数量',needSpace:'需要选取区域',
	needDetail:"至少需要填写一行明细数据",cancel:'取消',relationShip:'关系',phone:'电话',address:'地址',
	name:'姓名'
}






</script>
</@b.html>