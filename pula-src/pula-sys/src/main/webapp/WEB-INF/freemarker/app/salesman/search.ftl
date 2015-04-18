<#import "/app/macros/commonBase.ftl" as b><@b.html title="salesman.title">

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
            <td colspan="4"><div class="l">销售信息 <span id="pageMode" style="color:blue" class="h"></span></div>
              <div class="r">
              <div class="backToList " id="backToList"><A href="javascript:pes.backToList()">返回至列表</A></div></td>
	  </tr> <tr> 
		<td >编号</td>
		<td ><span id="span.no"></span> </td>
	  
		<td >名称</td>
		<td ><span id="span.name"></span></td>
	  </tr> <tr> 
		<td >分支机构</td>
		<td><span id="span.branchNo"></span> <span id="span.branchName"></span></td>
		<td>性别</td>
	<td>
		<#list genders as tp>
		<label data="${tp.id?if_exists?html}" class="rGender">${tp.name?if_exists?html}</label>
		</#list></td>
		</tr><tr>
		<td>电话</td>
		<td><span id="span.phone"></span></td>
	
		<td>手机</td>
		<td><span id="span.mobile"></span></td>
	</tr>  <tr> 
		<td >赠分上限</td>
		<td colspan="3"><span id="span.giftPoints"></span> </td>
	  </tr> 
	   <tr> 
		<td >备注</td>
		<td colspan="3"><span id="span.comments"></span> </td>
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
<script type="text/javascript" src="${base}/static/app/javascript/salesman_search.js"></script>
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
	name:'名称',no:'编号',domain:'销售人员查询',
	branchName:'分支机构',enabled:'状态',mobile:'手机',gender:'性别',giftPoints:'赠分上限'
}


	function check(){
		
		if(isEmpty($F("salesman.no"))){
			alert("请填写编号");
			$("salesman.no").focus();
			return false; 
		}
		if(isEmpty($F("salesman.name"))){
			alert("请填写名称");
			$("salesman.name").focus();
			return false; 
		}
		if(isEmpty($F("salesman.branchId"))){
			alert("请选择分支机构");
			$("salesman.branchId").focus();
			return false; 
		}

		
		var len = $$(".rGender:checked").length ;
		if(len==0){
			alert('请选取性别');
			$$(".rGender")[0].focus();
			return false;
		}
		
		var b = true;
		$$(".numberEdit").each ( function (el){
			b = b && isReal( el.value ) ;
		});

		if(!b){
			alert("请正确填写数值");
		}
		

		len = $F("salesman.comments").length ;
		if(len>200){
			alert("备注信息过长，请填写200字以内信息");
			$("salesman.comments").focus();
			return false;
		}

		
		
		return true ;
	}
</script>
</@b.html>