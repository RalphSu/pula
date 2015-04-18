<#import "/app/macros/commonBase.ftl" as b><@b.html title="course.title">

<style>
.top .text{
	line-height:25px;
	height:25px;
	margin-top:3px;
	color:#5f5f5f;
}
.t-simple-no input{ width: 100px}
.t-simple-no select{ width:100px}
</style>
<#assign wt_target="history"/>
<#include "../student/studentTop.inc.ftl"/>
<div class="body">
    <!-- condition -->
    <div id="conditionDiv" class="h forList">
		<form action="${uri}" method="get" id="searchForm">
	<table border="0" class="grid" width="1000" id="queryForm"><colgroup>
	<col style="width:80px"/>
	<col /><col style="width:80px"/>
	<col />
	</colgroup>
          <tr class="title">
            <td colspan="4">查询条件</td></tr>
		 <tr> 
		<td>课程</td>
		<td>
		<select name="condition.courseCategoryId" id="categoryId">
		<option value="">请选择...</option>
		<#list courseCategories as tp>
		<option value="${tp.id?if_exists?html}">${tp.name?if_exists?html}</option>
		</#list>
		</select>
		<select name="condition.courseId" id="condition.courseId" class="_categoryId"><option value="0">请选择...</option>
		</select>
		</td><td><#if headquarter></#if>教室</td>
		<td>
		<#if headquarter><select name="condition.branchId" id="cBranchId" >
		<option value="">请选择...</option>
		<#list branches as tp>
		<option value="${tp.id?if_exists?html}">${tp.name?if_exists?html}</option>
		</#list>
		</select>
		</#if>
		<select name="condition.classroomId" id="condition.classroomId" class="_cBranchId"><option value="0">请选择...</option>
		<#if !headquarter><#list classroomList as tp>
		<option value="${tp.id?if_exists?html}">${tp.name?if_exists?html}</option>
		</#list></#if>
		</select>
		</td>
		</tr><tr>
		<td>教师编号</td>
		<td>
		<div id="sTeacher"></div>
		</td><td>日期</td>
	 <td colspan="1"><input type="text" value="${condition.beginDate?if_exists?html}" name="condition.beginDate" id="beginDate" class='dateField' maxlength="10" size="12"/>
		 <img src='${base}/static/laputa/images/icons/arrow_blue.gif' border=0 align="absmiddle">
		 <input type="text" value="${(condition.endDate?if_exists?html)}" name="condition.endDate" id="endDate" class='dateField' maxlength="10" size="12"/> </td>
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
<!-- others -->
<#include "/calendar.ftl"/>
<link rel="stylesheet" type="text/css" href="${base}/static/app/css/t-style.css"></link>
<script type="text/javascript" src="${base}/static/library/puerta/t-table.js"></script> 
<script type="text/javascript" src="${base}/static/app/javascript/coursetaskresultstudent_student.js"></script>
<script type="text/javascript" src="${base}/static/library/mootools/modules/mbox/mt.mbox.js"></script>
<script type="text/javascript" src="${base}/static/library/puerta/t-simple-no.js"></script>
<script type="text/javascript" src="${base}/static/library/puerta/t-selectorloader.js"></script>
<script type="text/javascript">

var pageVars = {
	queryString:'',
	action : '_create',
	id :'',
	base :'${base}'
}

var lang = {
	name:'名称',no:'编号',domain:'授课记录明细',expiredTime:'失效日期',publishTime:'发布日期',showInWeb:'显示在网站上',indexNo:'序号',
	categoryName:'分类',status:'状态',tipsOfTSN:'输入名称加载'
}


	
</script>
</@b.html>