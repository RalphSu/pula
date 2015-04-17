<#import "/app/macros/commonBase.ftl" as b><@b.html title="materialRequire.title">

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
	<col /><col style="width:80px"/>
	<col style="width:80px"/><col style="width:80px"/>
	<col style="340px"/>
	</colgroup>
          <tr class="title">
            <td colspan="6">查询条件</td></tr>
		 <tr> 
		<td>材料编号</td>
		<td>
		<div id="scMaterialNo"/>
		</td><td>状态</td>
		<td>
		<select name="condition.status" id="condition.status">
		<option value="0">(全部)</option>
		<#list statusList as tp>
		<option value="${tp.id?if_exists?html}">${tp.name?if_exists?html}</option>
		</#list>
		</select>
		</td>
		<td>填写日期</td>
		
	 <td><input type="text" value="${condition.beginDate?if_exists?html}" name="condition.beginDate" id="beginDate" maxlength="10" size="12" class="dateField"/>
		 <img src='${base}/static/laputa/images/icons/arrow_blue.gif' border=0 align="absmiddle">
		 <input type="text" value="${(condition.endDate?if_exists?html)}" name="condition.endDate" id="endDate" maxlength="10" size="12" class="dateField"/> </td>
	  </tr>
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
	<table border="0" class="grid" width="700">
	<colgroup><col style="width:100px"/>
	<col /><col style="width:100px"/>
	<col />
	</colgroup>
	  <tr class="title"> 
            <td colspan="4"><div class="l">请填写下列信息 <span id="pageMode" style="color:blue"></span></div>
              <div class="r">
              <div class="backToList " id="backToList"><A href="javascript:pes.backToList()">返回至列表</A></div></td>
	  </tr> <tr> 
		<td >材料编号<span class="redStar">*</span></td>
		<td colspan="3"><div id="sMaterialNo"></div></td>
	  </tr><tr>
		<td >数量<span class="redStar">*</span></td>
		<td colspan="3"><input type="text" name="materialRequire.quantity" id="materialRequire.quantity" size="10" maxlength="10" class="numberEdit" value="0"/><span id="spanUnit"></span> </td>
	  </tr> 
	   <tr> 
		<td >备注</td>
		<td colspan="3"><input type="text" name="materialRequire.comments" id="materialRequire.comments" size="60" maxlength="100"/> </td>
	  </tr> 
	  <tr> 
		<td colspan="4">
		<input type="submit" value="<@b.text key="submitBtn"/>" id="submitBtn"/>
		<input type="hidden" name="materialRequire.id" id="materialRequire.id"/>
		</td>
	  </tr> 
	</table>
	</form>

	<table border="0" class="grid" width="850" id="tblView">
	<colgroup><col style="width:100px"/>
	<col /><col style="width:80px"/>
	<col /><col style="width:80px"/>
	<col />
	</colgroup>
	  <tr class="title"> 
            <td colspan="6"><div class="l">材料申请 ID:<span id="span.id"></span> 状态:<span id="span.statusName"></span></div>
              <div class="r">
              <div class="backToList " id="backToList"><A href="javascript:pes.backToList()">返回至列表</A></div></td>
	  </tr> <tr> 
		<td >材料编号</td>
		<td colspan="5"><span id="span.materialNo"></span> <span id="span.materialName"></span></td>
	  </tr><tr>
		<td >数量</td>
		<td colspan="1"><span id="span.quantity"></span> <span id="span.materialUnit"></span></td>
		<td>分支机构</td>
		<td colspan="3"><span id="span.branchNo"></span> <span id="span.branchName"></span></td>
	  </tr> 
	   <tr> 
		<td >备注</td>
		<td colspan="5"><span id="span.comments"></span></td>
	  </tr> 
	   <tr> 
		<td >创建人</td>
		<td><span id="span.creatorName"></span> </td>
		
		<td >创建时间</td>
		<td ><span id="span.createdTime" class="span-to-time"></span>
		
		<td >提交时间</td>
		<td "><span id="span.submitTime" class="span-to-time"></span></td>
	  </tr> 
	  <tr class="title"> 
            <td colspan="6">审核信息</td>
	  </tr>  <tr> 
		<td >审核人</td>
		<td ><span id="span.auditorName"></span> </td>
	  
	    <td >审核数量</td>
		<td ><span id="span.sentQuantity"></span> </td>
		<td >审核时间</td>
		<td ><span id="span.auditTime" class="span-to-time"></span></td>
	  </tr> 
	   <tr> 
		<td >审核备注</td>
		<td colspan="5"><span id="span.auditComments"></span></td>
	  </tr>
	   <tr class="title"> 
            <td colspan="6">发出信息</td>
	  </tr>  <tr> 
		<td >发送人</td>
		<td ><span id="span.senterName"></span> </td>
		<td >发货单号</td>
		<td ><span id="span.outNo"></span> </td>
		<td >发送时间</td>
		<td><span id="span.sentTime" class="span-to-time"></span></td>
	  </tr> <tr class="title"> 
            <td colspan="6">接收信息</td>
	  </tr>  <tr> 
		<td >接收人</td>
		<td ><span id="span.receiverName"></span> </td>
	    <td >接收数量</td>
		<td ><span id="span.arriveQuantity"></span> </td>
		<td >接收时间</td>
		<td ><span id="span.receivedTime" class="span-to-time"></span></td>
	  </tr> 
	</table>
	
	</div>
  </div>
<!-- others -->
<#include "/calendar.ftl"/>
<script type="text/javascript" src="${base}/static/app/javascript/modules/autoSuggest.js"></script>
<link rel="stylesheet" type="text/css" href="${base}/static/app/css/t-style.css"></link>
<script type="text/javascript" src="${base}/static/library/puerta/t-table.js"></script> 
<script type="text/javascript" src="${base}/static/app/javascript/materialrequire.js"></script>
<script type="text/javascript" src="${base}/static/library/mootools/modules/mbox/mt.mbox.js"></script>
<script type="text/javascript" src="${base}/static/library/puerta/t-simple-no.js"></script>
<script type="text/javascript" src="${base}/static/library/puerta/t-suggest.js"></script>
<script type="text/javascript">

var pageVars = {
	queryString:'',
	action : '_create',
	id :'',
	base :'${base}'
}

var lang = {
	name:'名称',no:'编号',domain:'材料申请',
	branchName:'分支机构',enabled:'状态'
}


	function check(){
		
		if(isEmpty($F("materialNo"))){
			alert("请填写材料编号");
			$("materialNo").focus();
			return false; 
		}
		
		var b = true;
		var eel = null ;
		$$(".numberEdit").each ( function (el){
			var v = Number.from( el.value ) ;
			if( b && !v && v!=0  ) {
				eel = el ;
				b= false;
			}
			
		});

		if(!b){
			alert("请正确填写数值");
			eel.select();
			eel.focus();
			return false;
		}


		len = $F("materialRequire.comments").length ;
		if(len>200){
			alert("备注信息过长，请填写200字以内信息");
			$("materialRequire.comments").focus();
			return false;
		}

		
		
		return true ;
	}
</script>
</@b.html>