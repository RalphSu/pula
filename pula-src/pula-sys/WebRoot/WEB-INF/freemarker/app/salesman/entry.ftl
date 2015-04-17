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
            <td colspan="4"><div class="l">请填写下列信息 <span id="pageMode" style="color:blue"></span></div>
              <div class="r">
              <div class="backToList " id="backToList"><A href="javascript:pes.backToList()">返回至列表</A></div></td>
	  </tr> <tr> 
		<td >编号<span class="redStar">*</span></td>
		<td ><input type="text" name="salesman.no" id="salesman.no" size="20" maxlength="40"/> </td>
	  
		<td >名称<span class="redStar">*</span></td>
		<td ><input type="text" name="salesman.name" id="salesman.name" size="20" maxlength="40"/> </td>
	  </tr> <tr> 
		<td >分支机构<span class="redStar">*</span></td>
		<td><select name="salesman.branchId" id="salesman.branchId">
		<option value="">(请选择...)</option>
		<#list branches as tp>
		<option value="${tp.id?if_exists?html}">${tp.no!?html} ${tp.name?if_exists?html}</option>
		</#list>
		</select></td>
		<td>性别<span class="redStar">*</span></td>
	<td>
		<#list genders as tp>
		<label><input type="radio" name="salesman.gender" value="${tp.id?if_exists?html}" class="rGender"/>${tp.name?if_exists?html}</label>
		</#list></td>
		</tr><tr>
		<td>电话</td>
		<td><input type="text" name="salesman.phone" maxlength="20" id="salesman.phone"></td>
	
		<td>手机</td>
		<td><input type="text" name="salesman.mobile" maxlength="20" id="salesman.mobile"></td>
	</tr>  <tr> 
		<td >赠分上限</td>
		<td colspan="3"><input type="text" name="salesman.giftPoints" id="salesman.giftPoints" size="10" class="numberEdit"/> </td>
	  </tr> 
	   <tr> 
		<td >备注</td>
		<td colspan="3"><input type="text" name="salesman.comments" id="salesman.comments" size="60" maxlength="80"/> </td>
	  </tr> 
	  <tr> 
		<td colspan="4">
		<input type="submit" value="<@b.text key="submitBtn"/>" id="submitBtn"/>
		<input type="hidden" name="salesman.id" id="salesman.id"/>
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
<script type="text/javascript" src="${base}/static/app/javascript/salesman.js"></script>
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
	name:'名称',no:'编号',domain:'销售人员管理',
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
		var ael = null ;
		$$(".numberEdit").each ( function (el){
			if( b ){
				b =  isReal( el.value ) ;
				if(!b){
					ael = el ;
				}
			}
		});

		if(!b){
			alert("请正确填写数值");
			ael.focus();
			return false;
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