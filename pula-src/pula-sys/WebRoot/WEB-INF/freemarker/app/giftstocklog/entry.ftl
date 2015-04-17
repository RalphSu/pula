<#import "/app/macros/commonBase.ftl" as b><@b.html title="stocklog">
  <div class="top" id="__top"> </div>
<div class="body">
    <!-- condition -->
    <div id="conditionDiv" class="h forList">
	<form action="${uri}" method="get" id="searchForm">
	<table border="0" class="grid" width="800" id="queryForm"><colgroup>
		<col style="width:100px"/>
		<col />
		<col style="width:100px"/>
		<col style="width:180px"/>
	</colgroup>
          <tr class="title">
            <td colspan="4">查询条件</td></tr>
		 <tr> 
		<td>礼品编号</td>
		<td colspan="3"><!-- use suggest -->
		<div id="sGift"></div>
		</td>
		</tr>
		<tr>
		<td>类型</td>
		<td ><select name="condition.type" id="condition.type">
		<option value="0">(全部)</option>
		<#list typeList as tp>
		<option value="${tp.id?if_exists?html}">${tp.name?if_exists?html}</option>
		</#list>
		</select></td>
		<td>分支机构</td>
		<td>
		<#if headquarter><select name="condition.branchId" id="branchList">
		<option value="0">(全部)</option>
		<#list branchList as tp>
		<option value="${tp.id!?html}">${tp.no!?html} ${tp.name!?html}</option>
		</#list>
		</select>
		<#else>
			${branch.name!?html}
		</#if>
		</td></tr>
		<tr>
		<td>日期</td>
	 <td colspan="1"><input type="text" value="${condition.beginDate?if_exists?html}" name="condition.beginDate" id="beginDate" maxlength="10" size="12" class="dateField"/>
		 <img src='${base}/static/laputa/images/icons/arrow_blue.gif' border=0 align="absmiddle">
		 <input type="text" value="${(condition.endDate?if_exists?html)}" name="condition.endDate" id="endDate" maxlength="10" size="12" class="dateField"/> </td>
		
		 <td>外单号</td><td><input type="text" name="condition.outNo"/></td>
	  </tr>

	  <tr> 
		<td colspan="4">
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
	
	</div>
  </div>
<!-- others -->
<#include "/calendar.ftl"/>
<link rel="stylesheet" type="text/css" href="${base}/static/app/css/t-style.css"></link>
<script type="text/javascript" src="${base}/static/library/puerta/t-table.js"></script> 
<script type="text/javascript" src="${base}/static/app/javascript/giftstocklog.js"></script>
<script type="text/javascript" src="${base}/static/library/mootools/modules/mbox/mt.mbox.js"></script>
<script type="text/javascript" src="${base}/static/library/puerta/t-simple-no.js"></script>
<script type="text/javascript" src="${base}/static/app/javascript/modules/autoSuggest.js"></script>
<script type="text/javascript" src="${base}/static/library/puerta/t-suggest.js"></script>
<script type="text/javascript">

var pageVars = {
	queryString:'',
	action : '_create',
	id :'',
	base :'${base}'
}

var lang = {
	name:'名称',no:'编号',domain:'礼品库存日志',giftNo:'礼品编号',giftName:'礼品名称',warehouse:'仓库',space:'区域',quantity:'数量',no:'内单号',outNo:'外单号'
,exportTo:'导出',typeName:'类型',eventTime:'日期',all:'全部',
	noTypes :["入库","出库","盘点","转仓"]
}


	function check(){
		
		if(isEmpty($F("material.no"))){
			alert("请填写编号");
			$("material.no").focus();
			return false; 
		}
		if(isEmpty($F("material.name"))){
			alert("请填写名称");
			$("material.name").focus();
			return false; 
		}
		if(isEmpty($F("material.typeId"))){
			alert("请选择类型");
			$("material.typeId").focus();
			return false; 
		}

		var b = true;
		$$(".numberEdit").each ( function (el){
			b = b && isReal( el.value ) ;
		});

		if(!b){
			alert("请正确填写数值");
		}
		
		return true ;
	}
</script>
</@b.html>