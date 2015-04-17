<#import "/app/macros/commonBase.ftl" as b><@b.html title="course.title">

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
	
	</div>
  </div>

<div id="divDeploy" class="h" style="background:white">
	<form action="${uri}" method="post" id="frmDeploy">
	<table border="0" class="grid" width="400">
	<colgroup><col style="width:120px"/>
	<col />
	</colgroup>
	  <tr> 
		<td >选中课程(<span id="spnCourseCount"></span>)</td>
		<td ><div id="divAllCourse"></div></td>
	  </tr>
	 <tr> 
		<td >分支机构-教室<span class="redStar">*</span></td>
		<td  ><select name="branchId" id="branchId" class="c-branchId">
			<option value="">请选取...</option>
			<#list branches as branch>
			<option value="${branch.id}">${branch.no!?html} ${branch.name!?html}</option>
			</#list>
			</select>
			<select name="classroomId" id="classroomId" class="_branchId">
			</select>
		</td>	 
	  </tr>
	  <tr> 
		<td colspan="2">
		<input type="submit" value="<@b.text key="submitBtn"/>" id="submitBtn"/>
		</td>
	  </tr> 
	</table>
	</form>
</div>

<!-- others -->
<#include "/calendar.ftl"/>
<link rel="stylesheet" type="text/css" href="${base}/static/app/css/t-style.css"></link>
<script type="text/javascript" src="${base}/static/library/puerta/t-table.js"></script> 
<script type="text/javascript" src="${base}/static/app/javascript/course_deploy.js"></script>
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
	name:'名称',no:'编号',domain:'课程部署',expiredTime:'失效日期',publishTime:'发布日期',showInWeb:'显示在网站上',indexNo:'序号',
	categoryName:'分类',status:'状态',deployTo:'部署'
}


	function checkDeploy(){
		
		if(isEmpty($F("branchId"))){
			alert("请选取分支机构");
			$("branchId").focus();
			return false;
		}
		if(isEmpty($F("classroomId"))){
			alert("请选取教室");
			$("classroomId").focus();
			return false;
		}

		return true ;
	}
</script>
</@b.html>