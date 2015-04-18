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
<#include "../teacher/teacherTop.inc.ftl"/>
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
<!-- others -->
<#include "/calendar.ftl"/>
<link rel="stylesheet" type="text/css" href="${base}/static/app/css/t-style.css"></link>
<script type="text/javascript" src="${base}/static/library/puerta/t-table.js"></script> 
<script type="text/javascript" src="${base}/static/app/javascript/coursetaskresultstudent_teacher.js"></script>
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