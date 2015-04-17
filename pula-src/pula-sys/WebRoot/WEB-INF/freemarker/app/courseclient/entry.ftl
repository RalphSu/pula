<#import "/app/macros/commonBase.ftl" as b><@b.html title="employee.title">
  <div class="top" id="__top"> </div>
<div class="body">
    <!-- condition -->
    <div id="conditionDiv" class="h forList">
	<form action="${uri}" method="get" id="searchForm">
	<table border="0" class="grid" width="800" id="queryForm"><colgroup>
	<col style="width:70px"/>
	<col style="width:170px"/><col style="width:80px"/>
	<col style="width:100px" /><col style="width:80px"/>
	<col />
	</colgroup>
          <tr class="title">
            <td colspan="6">查询条件</td></tr>
		 <tr> 
		<td>机器码</td>
		<td>
		<input type="text" name="condition.machineNo" value="${condition.machineNo?if_exists?html}"/>
		</td><td>状态</td><td colspan="3">
		<label><input type="radio" name="condition.status" value="0" class="crStatus"/>全部</label>
		<#list statusList as sl>
		<label><input type="radio" name="condition.status" value="${sl.id}" class="crStatus"/>${sl.name!?html}</label>
		</#list></td>
		
		
		</tr>
		<tr>
		<td>受理日期</td>
		<td colspan="2">
		<input type="text" value="${condition.beginDate?if_exists?html}" name="condition.beginDate" id="beginDate" class='dateField' maxlength="10" size="12"/>
		 <img src='${base}/static/laputa/images/icons/arrow_blue.gif' border=0 align="absmiddle">
		 <input type="text" value="${(condition.endDate?if_exists?html)}" name="condition.endDate" id="endDate" class='dateField' maxlength="10" size="12"/> 
		</td>
		<td>分支机构-教室</td><td colspan="2">
		<select name="condition.branchId" id="cbranchId">
			<option value="">请选取...</option>
			<#list branchList as branch>
			<option value="${branch.id}">${branch.no!?html} ${branch.name!?html}</option>
			</#list>
			</select>
			<select name="condition.classroomId" id="cclassroomId" class="_cbranchId">
			</select>

		</td>
		</tr>
		
	  <tr> 
		<td colspan="6">
	<input type="submit" value="查询" id="searchBtn"/>

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
	<colgroup><col style="width:80px"/>
	<col  style="width:170px"/><col style="width:100px"/>
	<col />
	</colgroup>
	  <tr class="title"> 
            <td colspan="4"><div class="l">申请信息 <span id="pageMode" style="color:blue" class="h"></span></div>
              <div class="r">
              <div class="backToList " id="backToList"><A href="javascript:pes.backToList()">返回至列表</A></div></td>
	  </tr> <tr> 
		<td >ID</td>
		<td ><span id="span.id"></span></td>

		<td >机器码</td>
		<td ><span id="span.machineNo"></span></td>
		
	  </tr>
	 <tr> <td >IP</td>
		<td ><span id="span.ip"></span></td>		
		<td >申请时间</td>
		<td ><span id="createdTimeText"></span></td>
	  </tr>
	  <tr> 
		<td >申请备注</td>
		<td colspan="3">
		<span id="span.comments"></span></td>
	  </tr>
	  
	  <tr class="title"> 
            <td colspan="4">审核信息</td>
	  </tr>
	   <tr> 
		<td >当前状态<span class="redStar">*</span></td>
		<td colspan="3">
		<#list statusList as sl>
		<label><input type="radio" name="form.status" value="${sl.id}" class="rStatus"/>${sl.name!?html}</label>
		</#list>

</td>
	  </tr>
	   <tr><td >教室 <span class="redStar normalInputIn">*</span></td>
		<td colspan="3"><select name="form.branchId" id="branchId" class="normalInput">
			<option value="">请选取...</option>
			<#list branchList as branch>
			<option value="${branch.id}">${branch.no!?html} ${branch.name!?html}</option>
			</#list>
			</select>
			<select name="form.classroomId" id="classroomId" class="_branchId normalInput">
		</select>
			
			</td>		
		
	  </tr>
	  <tr><td >名称 <span class="redStar normalInputIn">*</span></td>
		<td >
		<input type="text" value="" name="form.name" id="form.name" maxlength="40" size="20"/>
		</td><td >失效日期</td>
		<td >
		<input type="text" value="" name="form.expiredTimeText" id="expiredTimeText" class='dateField normalInput' maxlength="10" size="12"/>
		</td>		
		
	  </tr>
	  <tr> 
		<td >受理备注</td>
		<td colspan="3">
		<textarea name="form.applyComments" id="form.applyComments" style="width:440px;height:100px;"></textarea>
</td>
	  </tr>
	  <tr> 
		<td colspan="4">
		<input type="submit" value="<@b.text key="submitBtn"/>" id="submitBtn"/>
		<input type="hidden" name="form.id" id="form.id"/>
		</td>
	  </tr> 
	</table>
	</form>
	</div>
  </div>



<!-- others -->
<#include "/calendar.ftl"/>
<link rel="stylesheet" type="text/css" href="${base}/static/app/css/t-style.css"></link>
<script type="text/javascript" src="${base}/static/library/puerta/t-table.js"></script> 
<script type="text/javascript" src="${base}/static/app/javascript/courseclient.js"></script>
<script type="text/javascript" src="${base}/static/library/mootools/modules/mbox/mt.mbox.js"></script>
<script type="text/javascript" src="${base}/static/library/puerta/t-simple-no.js"></script>
<script type="text/javascript">

var pageVars = {
	queryString:'',
	action : '_create',
	id :'',
	base :'${base}',const_normal:2
}

var lang = {
	name:'名称',no:'编号',domain:'开课客户端管理',gender:'性别',status:'状态',print:'打印条码',printOK:'选中项已成功输出打印',file:'档案'
	,view:'查看',birthday:'生日',branchName:'所在分支机构',assign:'指派',levelName:'级别'
}

	function checkAssign(){
		if(isEmpty($F("branchId"))){
			alert("请选取分支机构");
			$("branchId").focus();
			return false;
		}

		return true ;
	}

	function check(){

		var len = $$(".rStatus:checked").length ;
		if(len==0){
			alert('请选取状态');
			$$(".rStatus")[0].focus();
			return false;
		}

		var cked = $$(".rStatus:checked")[0].value;
		if(cked == pageVars.const_normal){
			
			//check classroom;
			if( isEmpty($F("branchId"))){
				alert("请选取分支机构");
				$('branchId').focus();
				return;
			}
			if( isEmpty($F("classroomId"))){
				alert("请选取教室");
				$('classroomId').focus();
				return;
			}
			if( isEmpty($F("form.name"))){
				alert("请填写名称");
				$('form.name').focus();
				return;
			}
			var d = $F("expiredTimeText");
			if( ! isEmpty( d ) &&   !isDate( d) ){
				alert("请正确填写失效日期");
				$("expiredTimeText").focus();
				return false;
			}	
		}


		var len = $F("form.applyComments").length ;
		if(len>200){
			alert("备注信息过长，请填写200字以内信息");
			$("form.applyComments").focus();
			return false;
		}		

		
		return true ;
	}
</script>
</@b.html>