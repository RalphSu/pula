<#import "/app/macros/commonBase.ftl" as b><@b.html title="syscategory.title">
  <div class="top" id="__top"> </div>
<div class="body">
    <!-- condition -->
   <div id="conditionDiv" class="h forList force-h">
<form action="${uri}" method="get" id="searchForm">
<table border="0" class="grid" width="600" id="queryForm"><colgroup>
<col style="width:100px"/>
<col /><col style="width:100px"/>
<col />
</colgroup> <tr class="title">
            <td colspan="4">查询条件</td></tr>
		 <tr> 
     <tr> 
    <td>名称</td>
    <td >
	<input type="text" name="condition.name" value="${condition.name?if_exists?html}"/>
	</td>
    <td>编号</td>
    <td >
	<input type="text" name="condition.no" value="${condition.no?if_exists?html}"/>
	</td>
  </tr>
  <tr> 
    <td colspan="4">
	<input type="submit" value="查询" id="searchBtn"/>
	<input type="reset" value="重填" id="resetBtn"/>
	<input type="hidden" name="condition.parentId" id="pid"/>
	</td>
  </tr> 
</table>
 </form></div>


    <!-- listview -->
    <div class="l forList" id="listview">
      <form id="batchForm">
        <div id="dt"> </div>
      </form>
      <!-- left ends --> 
    </div>

	<!-- edit form -->
	<div id="inputPanel" class="l h">
	<form action="${uri}" method="post" id="addForm" enctype="multipart/form-data">
	<table border="0" class="grid" width="600">
	<colgroup><col style="width:100px"/>
	<col />
	</colgroup>
	  <tr class="title"> 
            <td colspan="2"><div class="l">请填写下列信息 <span id="pageMode" style="color:blue"></span></div>
              <div class="r">
              <div class="backToList " id="backToList"><A href="javascript:pes.backToList()">返回至列表</A></div></td>
	  </tr>
	   <tr> 
		<td width="100">编号<b><font color=red>*</font></b></td>
		<td ><input type="text" name="sysCategory.no" id="sysCategory.no" size="40" maxlength="40"/> </td>
	  </tr>
	  <tr> 
		<td width="100">名称<b><font color=red>*</font></b></td>
		<td ><input type="text" name="sysCategory.name" id="sysCategory.name" size="60" maxlength="100"/> </td>
	  </tr><tr> 
		<td width="100">排序<b><font color=red>*</font></b></td>
		<td ><input type="text" name="sysCategory.indexNo" id="sysCategory.indexNo" size="10" maxlength="10" value="0" class="numberEdit"/> </td>
	  </tr>
	  <tr> 
		<td colspan="2">
		<input type="submit" value="<@b.text key="submitBtn"/>" id="submitBtn"/>
		<input type="hidden" name="sysCategory.id" id="sysCategory.id"/>
		<input type="hidden" name="sysCategory.parentId" id="sysCategory.parentId"/>
	
		</td>
	  </tr>
	</table>
	</form>
	</div>
  </div>
<!-- others -->
<link rel="stylesheet" type="text/css" href="${base}/static/app/css/t-style.css"></link>
<script type="text/javascript" src="${base}/static/library/puerta/t-table.js"></script> 
<script type="text/javascript" src="${base}/static/app/javascript/syscategory.js"></script>
<script type="text/javascript" src="${base}/static/library/mootools/modules/mbox/mt.mbox.js"></script>
<script type="text/javascript">

var pageVars = {
	queryString:'',
	action : '_create',
	id :'',
	base :'${base}'
}

var lang = {
	name:'名称',no:'编号',domain:'系统分类',status:'状态',
	under:'位于',
	only2Level:'仅支持二层结构',
	enabled:'状态',
	indexNo:'排序',nextLevel:'下一级',backToUpper:'返回上一级',
	title:'系统分类 - {title}'
}


	function check(){
		if(isEmpty($F("sysCategory.no"))){
			alert("<@b.text key="syscategory.inputNo"/>");
			$("sysCategory.no").focus();
			return false; 
		}
		if(isEmpty($F("sysCategory.name"))){
			alert("<@b.text key="syscategory.inputName"/>");
			$("sysCategory.name").focus();
			return false; 
		}

		if(!isInteger($F("sysCategory.indexNo"))){
			alert("请填写排序序号");
			$("sysCategory.indexNo").focus();
			return false; 
		}
		return true ;
	}
</script>
</@b.html>