<#import "/app/macros/commonBase.ftl" as b><@b.html title="card.title">
<style>
.headquarter{ background:red;color:white;padding:2px}
</style>

  <div class="top" id="__top"> </div>
<div class="body">
    <!-- condition -->
    <div id="conditionDiv" class="h forList">
	<form action="${uri}" method="get" id="searchForm">
	<table border="0" class="grid" width="800" id="queryForm"><colgroup>
	<col style="width:60px"/>
	<col /><col style="width:70px"/>
	<col /><col style="width:60px"/>
	<col  style="width:80px"/><col style="width:60px"/>
	<col  style="width:80px"/>
	</colgroup>
          <tr class="title">
            <td colspan="8">查询条件</td></tr>
		 <tr> 
		<td>编号</td>
		<td>
		<input type="text" name="condition.no" value="${condition.no?if_exists?html}"/>
		</td> <td>物理号</td>
		<td>
		<input type="text" name="condition.mac" value="${condition.mac?if_exists?html}"/>
		</td><td>状态</td>
		<td>
		<select name="condition.status" id="condition.status">
		<option value="0">(全部)</option>
		<#list statusList as tp>
		<option value="${tp.id?if_exists?html}">${tp.name?if_exists?html}</option>
		</#list>
		</select>
		</td><td>启用</td>
		<td>
		<select name="condition.enabledStatus" id="condition.enabledStatus">
		<option value="0">(全部)</option>
		<#list enabledStatusList as tp>
		<option value="${tp.id?if_exists?html}">${tp.name?if_exists?html}</option>
		</#list>
		</select>
		</td>
	  </tr>
	  <tr> 
		<td colspan="8">
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

   <div class="h" id="pnl_upload">
   <form method="post" id="uploadForm" enctype="multipart/form-data" action="importXls">
	<table border="0" class="grid" width="600"><colgroup>
	<col style="width:100px"/>
	<col />
	<col style="width:100px"/>
	<col />
	</colgroup>
	<tr class="title">
            <td colspan="4">导入(Excel2003格式)</td></tr>
		 <tr> 
		<td>Excel文件</td>
		<td colspan="3">
		<input type="file" style="width:400px" name="file"/></td>
	  </tr>
	  <tr>
	  <td colspan="4">
	  <input type="submit" value="上传" id="uploadBtn"/></td>
	  </tr>
	  </table></form>
	</div>

<!-- others -->
<link rel="stylesheet" type="text/css" href="${base}/static/app/css/t-style.css"></link>
<script type="text/javascript" src="${base}/static/library/puerta/t-table.js"></script> 
<script type="text/javascript" src="${base}/static/app/javascript/card.js"></script>
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
	name:'名称',no:'编号',domain:'卡片',mac:'物理号',status:'状态',enabled:'启用',comments:'备注',importTo:'导入',upload:'导入'
}


	function check(){
		
		if(isEmpty($F("form.no"))){
			alert("请填写编号");
			$("form.no").focus();
			return false; 
		}
		if(isEmpty($F("form.name"))){
			alert("请填写名称");
			$("form.name").focus();
			return false; 
		}
		
		return true ;
	}
</script>
</@b.html>