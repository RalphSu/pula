<#import "/app/macros/commonBase.ftl" as b><@b.html title="classroom.title">

<style>
#workerList A:hover{
	text-decoration:line-through;
}
</style>
  <div class="top" id="__top"> </div>
<div class="body">
    <!-- condition -->
    <div id="conditionDiv" class="h forList">
	<#include "condition.ftl"/>
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
	<div class="l"><table border="0" class="grid" width="700">
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
		<td ><input type="text" name="classroom.no" id="classroom.no" size="20" maxlength="40"/> </td>
	  
		<td >名称<span class="redStar">*</span></td>
		<td ><input type="text" name="classroom.name" id="classroom.name" size="20" maxlength="40"/> </td>
	  </tr> <tr> 
		<td >分支机构<span class="redStar">*</span></td>
		<td colspan="3"><select name="classroom.branchId" id="classroom.branchId">
		<option value="">(请选择...)</option>
		<#list branches as tp>
		<option value="${tp.id?if_exists?html}">${tp.no!?html} ${tp.name?if_exists?html}</option>
		</#list>
		</select>
		</tr>
	   <tr> 
		<td >备注</td>
		<td colspan="3"><input type="text" name="classroom.comments" id="classroom.comments" size="60" maxlength="80"/> </td>
	  </tr> 
	  <tr> 
		<td colspan="4">
		<input type="submit" value="<@b.text key="submitBtn"/>" id="submitBtn"/>
		<input type="hidden" name="classroom.id" id="classroom.id"/>
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
<script type="text/javascript" src="${base}/static/app/javascript/classroom.js"></script>
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
	name:'名称',no:'编号',domain:'教室',
	branchName:'分支机构',enabled:'状态'
}


	function check(){
		
		if(isEmpty($F("classroom.no"))){
			alert("请填写编号");
			$("classroom.no").focus();
			return false; 
		}
		if(isEmpty($F("classroom.name"))){
			alert("请填写名称");
			$("classroom.name").focus();
			return false; 
		}
		if(isEmpty($F("classroom.branchId"))){
			alert("请选择分支机构");
			$("classroom.branchId").focus();
			return false; 
		}

		

		len = $F("classroom.comments").length ;
		if(len>200){
			alert("备注信息过长，请填写200字以内信息");
			$("classroom.comments").focus();
			return false;
		}

		
		
		return true ;
	}
</script>
</@b.html>