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
		<form action="${uri}" method="get" id="searchForm">
	<table border="0" class="grid" width="800" id="queryForm"><colgroup>
	<col style="width:80px"/>
	<col /><col style="width:80px"/>
	<col />
	</colgroup>
          <tr class="title">
            <td colspan="4">查询条件</td></tr>
		 <tr> 
		<td>课程关键词</td>
		<td>
		<input type="text" name="condition.keywords" value="${condition.keywords?if_exists?html}"/>
		</td><td>分支机构</td>
		<td>
		<select name="condition.branchId"  id="cbranchId" class="c-branchId">
		<option value="0">(全部)</option>
		<#list branches as tp>
		<option value="${tp.id?if_exists?html}">${tp.no!?html} ${tp.name?if_exists?html}</option>
		</#list>
		</select>
			<select name="condition.classroomId" id="classroomId" class="_cbranchId">
			</select>
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

<div id="divDeploy" class="h" style="background:white">
	<form action="${uri}" method="post" id="frmDeploy">
	<table border="0" class="grid" width="400">
	<colgroup><col style="width:100px"/>
	<col />
	</colgroup>
	  <tr> 
		<td >选中课程(<span id="spnCourseCount"></span>)</td>
		<td ><div id="divAllCourse"></div></td>
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
		</td>
	  </tr> 
	</table>
	</form>
</div>

<!-- others -->
<#include "/calendar.ftl"/>
<link rel="stylesheet" type="text/css" href="${base}/static/app/css/t-style.css"></link>
<script type="text/javascript" src="${base}/static/library/puerta/t-table.js"></script> 
<script type="text/javascript" src="${base}/static/app/javascript/coursedeployment.js"></script>
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
	courseName:'名称',courseNo:'编号',domain:'课程部署结果',expiredTime:'失效日期',publishTime:'发布日期',createdTime:'部署时间',creatorName:'部署人',
	branchName:'分支机构',categoryName:'课程分类'
}


	function checkDeploy(){
		
		if(isEmpty($F("branchId"))){
			alert("请选取分支机构");
			$("branchId").focus();
			return false;
		}

		return true ;
	}
</script>
</@b.html>