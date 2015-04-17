<#import "/app/macros/commonBase.ftl" as b><@b.html title="material.title">

<style>
#workerList A:hover{
	text-decoration:line-through;
}
</style>
  <div class="top" id="__top"> </div>
<div class="body">
    <!-- condition -->
    <div id="conditionDiv" class="h forList">
	<form action="${uri}" method="get" id="searchForm">
	<table border="0" class="grid" width="800" id="queryForm"><colgroup>
		<col style="width:80px"/>
		<col style="width:200px"/>
		<col style="width:70px"/>
		<col style="width:100px"/>
		<col style="width:60px"/>
		<col />
	</colgroup>
          <tr class="title">
            <td colspan="6">查询条件</td></tr>
		 <tr> 
		<td>编号</td>
		<td >
		<input type="condition.no" />
		</td>
		<td>数据来源</td>
		<td><select name="condition.dataFrom" id="condition.dataFrom">
		<option value="0">(全部)</option>
		<#list dataFroms as tp>
		<option value="${tp.id?if_exists?html}">${tp.name?if_exists?html}</option>
		</#list>
		</select></td>
		<td>日期</td>
 <td ><input type="text" value="${condition.beginDate?if_exists?html}" name="condition.beginDate" id="beginDate" maxlength="10" size="12" class="dateField"/>
		 <img src='${base}/static/laputa/images/icons/arrow_blue.gif' border=0 align="absmiddle">
		 <input type="text" value="${(condition.endDate?if_exists?html)}" name="condition.endDate" id="endDate" maxlength="10" size="12" class="dateField"/> </td>
		 </tr>
		 <tr>
		<td>职员编号</td>
		<td colspan="5">
		<div id="sEmployeeC"></div>
		</td></tr>
		

	  <tr> 
		<td colspan="6">
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

	<!-- edit form -->
	<div id="inputPanel" class="l h">
	<form action="${uri}" method="post" id="addForm">
	<div class="l"><table border="0" class="grid" width="500">
	<colgroup><col style="width:100px"/>
	<col />
	</colgroup>
	  <tr class="title"> 
            <td colspan="2"><div class="l">请填写下列信息 <span id="pageMode" style="color:blue"></span></div>
              <div class="r">
              <div class="backToList " id="backToList"><A href="javascript:pes.backToList()">返回至列表</A></div></td>
	  </tr> <tr> 
		<td >考勤编号<span class="redStar">*</span></td>
		<td ><input type="text" name="attendanceRecord.no" id="attendanceRecord.no" size="20" maxlength="40"/> </td>
	  
	
	  </tr>  <tr> 
		<td >考勤时间<span class="redStar">*</span></td>
		<td ><input type="text" name="attendanceRecord.checkTimeText" id="attendanceRecord.checkTimeText"
		size="10" maxlength="10" class="dateField"/>
		<SELECT name="attendanceRecord.hour">
						<#list 0..23 as h>
						<OPTION value="${h}">${h?string?left_pad(2,"0")}</OPTION>
						</#list>
					</SELECT>时

					<SELECT name="attendanceRecord.minute">
						<#list 0..59 as h>
						<OPTION value="${h}">${h?string?left_pad(2,"0")}</OPTION>
						</#list>
					</SELECT>分
		</td>
		
	  </tr> 

	  <tr> 
		<td colspan="2">
		<input type="submit" value="<@b.text key="submitBtn"/>" id="submitBtn"/>
		<input type="hidden" name="material.id" id="material.id"/>
		</td>
	  </tr> 
	</table>

</div>
	
	</form>
	</div>
  </div>
<!-- others -->
<#include "/calendar.ftl"/>
<link rel="stylesheet" type="text/css" href="${base}/static/app/css/t-style.css"></link>
<script type="text/javascript" src="${base}/static/library/puerta/t-table.js"></script> 
<script type="text/javascript" src="${base}/static/app/javascript/attendancerecord.js"></script>
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
	name:'名称',no:'编号',domain:'考勤记录',raw:'材料',spec:'规格',weight:'重量',checkTime:'考勤时间'
	,collectTime:'采集时间',employeeName:'职员',ip:'IP',dataFromName:'数据来源',machine:'设备',status:'有效'
}


	function check(){
		
		if(isEmpty($F("attendanceRecord.no"))){
			alert("请填写考勤编号");
			$("attendanceRecord.no").focus();
			return false; 
		}
		if(!isDate($F("attendanceRecord.checkTimeText"))){
			alert("请填写考勤日期");
			$("attendanceRecord.checkTimeText").focus();
			return false; 
		}
		
		return true ;
	}
</script>
</@b.html>