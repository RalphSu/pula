<#import "/app/macros/commonBase.ftl" as b><@b.html title="material.title">
  <div class="top" id="__top"> </div>
<div class="body">
    <!-- condition -->
    <div id="conditionDiv" class="h forList">
	<form action="${uri}" method="get" id="searchForm">
	<table border="0" class="grid" width="600" id="queryForm"><colgroup>
	<col style="width:60px"/>
	<col /><col style="width:60px"/>
	<col style="width:120px"/><col style="width:60px"/>
	<col style="width:80px"/>
	</colgroup>
          <tr class="title">
            <td colspan="6">查询条件</td></tr>
		 <tr> 
		<td>关键词</td>
		<td>
		<input type="text" name="condition.keywords" value="${condition.keywords?if_exists?html}"/>
		</td><td>类型</td>
		<td>
		<select name="condition.categoryId" id="condition.categoryId">
		<option value="">(全部)</option>
		<#list types as tp>
		<option value="${tp.id?if_exists?html}">${tp.name?if_exists?html}</option>
		</#list>
		</select>
		</td>
		<td>状态</td>
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
		<td >编号<span class="redStar">*</span></td>
		<td ><input type="text" name="material.no" id="material.no" size="20" maxlength="40"/> </td>
	  
		<td >名称<span class="redStar">*</span></td>
		<td ><input type="text" name="material.name" id="material.name" size="20" maxlength="40"/> </td>
	  </tr><tr> 
		<td >拼音码</td>
		<td colspan="3"><input type="text" name="material.pinyin" id="material.pinyin" size="40" maxlength="40" /> </td>  
		
	  </tr>  <tr> 
		<td >类型<span class="redStar">*</span></td>
		<td ><select name="material.categoryId" id="material.categoryId">
		<option value="">(请选择...)</option>
		<#list types as tp>
		<option value="${tp.id?if_exists?html}">${tp.name?if_exists?html}</option>
		</#list>
		</select> </td>
		<td >品牌</td>
		<td ><input type="text" name="material.brand" id="material.brand" size="20" maxlength="100"/> </td></tr>
	  <tr> 
		<td >单位</td>
		<td colspan="3"><input type="text" name="material.unit" id="material.unit" size="20" maxlength="40"/> </td>
	  </tr>
	  <tr>
		<td >备注</td>
		<td colspan="3"><input type="text" name="material.comments" id="material.comments" size="60" maxlength="100"/> </td>
	  </tr> <!-- <tr> 
		<td >重量</td>
		<td ><input type="text" name="material.weight" id="material.weight" size="10" maxlength="40" class="numberEdit" value="0"/> </td>
	  
		<td >表面积</td>
		<td ><input type="text" name="material.superficialArea" id="material.superficialArea"  size="10" maxlength="40" class="numberEdit" value="0"/> </td>
	  </tr>  --> <!-- <tr> 
		<td >最大库存</td>
		<td ><input type="text" name="material.stockMax" id="material.stockMax" size="10" maxlength="40" class="numberEdit" value="0"/> </td>
	  
		<td >最小库存</td>
		<td ><input type="text" name="material.stockMin" id="material.stockMin"  size="10" maxlength="40" class="numberEdit" value="0"/> </td>
	  </tr>  -->
	  <tr> 
		<td colspan="4">
		<input type="submit" value="<@b.text key="submitBtn"/>" id="submitBtn"/>
		<input type="hidden" name="material.id" id="material.id"/>
		</td>
	  </tr> 
	</table>
	</form>
	</div>
  </div>

  <div class="h" id="pnl_upload"><form method="post" id="uploadForm" enctype="multipart/form-data" action="importMaterialCheck">
	<table border="0" class="grid" width="600"><colgroup>
	<col style="width:100px"/>
	<col />
	<col style="width:100px"/>
	<col />
	</colgroup>
	<tr class="title">
            <td colspan="4">导入材料(Excel2003格式)</td></tr>
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
<script type="text/javascript" src="${base}/static/app/javascript/material.js"></script>
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
	name:'名称',no:'编号',domain:'材料',raw:'材料',spec:'规格',weight:'重量',sa:'表面积',status:'状态'
	,materialTypeName:'类型',stockMax:'最大库存',stockMin:'最小库存',importTo:'导入',upload:'导入'
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
		if(isEmpty($F("material.categoryId"))){
			alert("请选择类型");
			$("material.categoryId").focus();
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