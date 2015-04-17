<#import "/app/macros/commonBase.ftl" as b><@b.html title="库存">
<style>
.t-simple-no input{ width: 160px}
.t-simple-no select{ width:160px}
</style>
  <div class="top" id="__top"> </div>
<div class="body">
    <!-- condition -->
    <div id="conditionDiv" class="h forList">
	<form action="${uri}" method="get" id="searchForm">
	<table border="0" class="grid" width="800" id="queryForm"><colgroup>
	<col style="width:100px"/>
	<col /><col style="width:100px"/>
	<col/>
	</colgroup>
          <tr class="title">
            <td colspan="4">查询条件</td></tr>
		 <tr> 
		<td>礼品编号</td>
		<td><!-- use suggest -->
		<div id="sGift"></div>
		</td>
		
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
		</td>
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
<link rel="stylesheet" type="text/css" href="${base}/static/app/css/t-style.css"></link>
<script type="text/javascript" src="${base}/static/library/puerta/t-table.js"></script> 
<script type="text/javascript" src="${base}/static/app/javascript/giftstock.js"></script>
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
	name:'名称',no:'编号',domain:'礼品库存',giftNo:'礼品编号',giftName:'礼品名称',warehouse:'仓库',space:'区域',quantity:'数量'
,exportTo:'导出',all:'(全部)'
}


</script>
</@b.html>