<#import "/app/macros/commonBase.ftl" as b><@b.html title="employee.title">
  <div class="top" id="__top"> </div>
<div class="body">
    <!-- condition -->
    <div id="conditionDiv" class="h forList">
	<form action="${uri}" method="get" id="searchForm">
	<table border="0" class="grid" width="1000" id="queryForm"><colgroup>
	<col style="width:70px"/>
	<col /><col style="width:60px"/>
	<col /><col style="width:60px"/>
	<col style="width:80px"/><col style="width:60px"/>
	<col style="width:80px"/><col style="width:60px"/>
	<col style="width:80px"/>
	</colgroup>
          <tr class="title">
            <td colspan="10">查询条件</td></tr>
		 <tr> 
		<td>编号</td>
		<td>
		<input type="text" name="condition.no" value="${condition.no?if_exists?html}"/>
		</td>
		<td>名称</td>
		<td>
		<input type="text" name="condition.name" value="${condition.name?if_exists?html}"/>
		</td>
		<td>性别</td>
		<td>
		<select name="condition.gender" id="condition.gender">
		<option value="0">(全部)</option>
		<#list genders as tp>
		<option value="${tp.id?if_exists?html}">${tp.name?if_exists?html}</option>
		</#list>
		</select>
		</td>
		<td>状态</td>
		<td>
		<select name="condition.status" id="condition.status">
		<option value="0">(全部)</option>
		<#list statusList as tp>
		<option value="${tp.id?if_exists?html}">${tp.name?if_exists?html}</option>
		</#list>
		</select>
		</td>
		<td>级别</td>
		<td>
		<select name="condition.level" id="condition.level">
		<option value="0">(全部)</option>
		<#list levels as tp>
		<option value="${tp.id?if_exists?html}">${tp.name?if_exists?html}</option>
		</#list>
		</select>
		</td></tr>
		<tr>
		<#if headquarter><td>分支机构</td>
		<td>
		<select name="condition.branchId" id="condition.branchId">
		<option value="0">(全部)</option>
		<#list branches as tp>
		<option value="${tp.id?if_exists?html}">${tp.no!?html} ${tp.name?if_exists?html}</option>
		</#list>
		</select>
		</td></#if><td>卡号</td>
		<td colspan="<#if headquarter>5<#else>7</#if>">
		<input type="text" name="condition.barcode" id="condition.barcode" size="40" maxlength="40" class="barcode"/>
		</td>
		<td>启用</td>
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
		<td colspan="10">
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
		<td ><input type="text" name="employee.no" id="employee.no" size="20" maxlength="40"/> </td>
	  
		<td >名称<span class="redStar">*</span></td>
		<td ><input type="text" name="employee.name" id="employee.name" size="20" maxlength="40"/> </td>
	  </tr>
	
	  <tr> 
		<td colspan="4">
		<input type="submit" value="<@b.text key="submitBtn"/>" id="submitBtn"/>
		<input type="hidden" name="employee.id" id="employee.id"/>
		</td>
	  </tr> 
	</table>
	</form>
	</div>
  </div>


<div id="divAssign" class="h" style="background:white">
	<form action="${uri}" method="post" id="frmAssign">
	<table border="0" class="grid" width="400">
	<colgroup><col style="width:100px"/>
	<col />
	</colgroup>
	  <tr> 
		<td >教师信息</td>
		<td ><span id="spnTeacherNo"></span> <span id="spnTeacherName"></span></td>  
		
	  </tr>
	 <tr> 
		<td >分支机构<span class="redStar">*</span></td>
		<td  ><select name="branchId" id="branchId">
			<option value="">请选取...</option>
			<#list branches as branch>
			<option value="${branch.id}">${branch.no!?html} ${branch.name!?html}</option>
			</#list>
			</select>
		</td>	 
	  </tr>
	  <tr> 
		<td colspan="2">
		<input type="submit" value="<@b.text key="submitBtn"/>" id="submitBtn"/>
		<input type="hidden" name="id" id="assign.id"/>
		</td>
	  </tr> 
	</table>
	</form>
</div>

<!-- others -->
<link rel="stylesheet" type="text/css" href="${base}/static/app/css/t-style.css"></link>
<script type="text/javascript" src="${base}/static/library/puerta/t-table.js"></script> 
<script type="text/javascript" src="${base}/static/app/javascript/teacher_search.js"></script>
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
	name:'名称',no:'编号',domain:'教师信息查询',gender:'性别',status:'状态',print:'打印条码',printOK:'选中项已成功输出打印',file:'档案'
	,view:'查看',birthday:'生日',branchName:'所在分支机构',assign:'指派',levelName:'级别',barcode:'卡号'
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
		

		
		return true ;
	}
</script>
</@b.html>