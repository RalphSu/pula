<#import "/app/macros/commonBase.ftl" as b><@b.html title="授课记录查看">
<style>
.top .text{
	line-height:25px;
	height:25px;
	margin-top:3px;
	color:#5f5f5f;
}



#uploadFile{
width:80px;
height:20px;
cursor:pointer;
}


</style>
  <div class="top" id="__top"> </div>
<div class="body">
    <!-- condition -->
	<!-- edit form -->
	
	<table border="0" class="grid" width="800">
<colgroup>
        <col style="width:100px;"></col>
        <col ></col> 
        <col style="width:100px;"></col>
        <col ></col> 
</colgroup>
  <tr class="title"> 
    <td colspan="4">培训信息  数据来源:${courseTaskResult.submitTypeName!?html}</td>
  </tr>
    <tr> 
    <td width="100">开始时间</td>
    <td width="336" >${courseTaskResult.startTime.time?datetime}</td>
    <td >结束时间</td>
    <td width="300" >${courseTaskResult.endTime.time?datetime}</td>
	</tr>
   <tr> 
	<td>地点</td>
	<td>${courseTaskResult.branchName!?html} ${courseTaskResult.classroomName!?html}</td>
	<td>课程</td>
	<td>
		${courseTaskResult.courseCategoryName!?html} 
		${courseTaskResult.courseName!?html} 
	</td>
	</tr>
	  <tr>
	    <td>教师</td>
	    <td colspan="3">${courseTaskResult.masterNo!?html} ${courseTaskResult.masterName!?html}</td>
      </tr>
	  <tr>
	    <td>助教1</td>
	    <td colspan="3">${courseTaskResult.assistant1No!?html} ${courseTaskResult.assistant1Name!?html}</td>
      </tr>
	  <tr>
	    <td>助教2</td>
	    <td colspan="3">${courseTaskResult.assistant2No!?html} ${courseTaskResult.assistant2Name!?html}</td>
      </tr>
	 
	  </table>
		
		<form id="saveForm">
	<table border="0" class="grid" width="800" id="tblStudent">
	  <tr class="title">
	    <td >相关学员</td>
      </tr>
	  <tr>
	    <td >
		<div id="dt"></div>
		</td>
        </tr>
	  </table>
	  </form>
	</div>

<!-- others -->
<#include "/calendar.ftl"/>
<link rel="stylesheet" type="text/css" href="${base}/static/app/css/t-style.css"></link>
<script type="text/javascript" src="${base}/static/library/puerta/t-table.js"></script> 
<script type="text/javascript" src="${base}/static/app/javascript/coursetaskresult_view.js"></script>
<script type="text/javascript" src="${base}/static/library/mootools/modules/mbox/mt.mbox.js"></script>
<script type="text/javascript" src="${base}/static/library/puerta/t-simple-no.js"></script>
<script type="text/javascript" src="${base}/static/library/mootools/modules/UUID.js"></script>
<script type="text/javascript" src="${base}/static/library/puerta/t-selectorloader.js"></script>
<script type="text/javascript">

var pageVars = {
	queryString:'',
	action : '_create',
	id :'<#if courseTaskResult??>${courseTaskResult.id}</#if>',
	base :'${base}',
	imagePath:'${base}/static/laputa/images/icons/',
	noPhoto:'${base}/static/app/images/nophoto.jpg'
	<#if courseTaskResult??>
	,courseId: ${courseTaskResult.courseId}
	</#if>
}

var lang = {
	name:'名称',no:'编号',domain:'查看授课记录',
	materialNo:'材料编号',
	materialName:'材料名称',space:'区域',picNo:'图号',msgSelectFirst:'请选择...',uploadPic:'上传照片',uploadFile:'上传作品',
	quantity:'数量',lineMsg:"第{index}行:{message}\n",needQty:'需要填写数量',needSpace:'需要选取区域',
	needDetail:"至少需要填写一行明细数据",cancel:'取消',relationShip:'关系',phone:'电话',address:'地址',
	name:'姓名',attachmentItem:'<span class="fileName"><a href="#" data="{fileId}" dataId="{id}" class="download" title="下载">{fileName}</a></span>',
	noneAttachment:"<span class='gray'>暂无附件</span>",tipsOfTSN:'输入名称加载'
}



	



	var pes = null ;
	window.addEvent('domready',function(){
		pes = new PPage({"id":"pes","pageMode":"pageMode","searchForm":"searchForm","addForm":"addForm"});

	
	});

</script>
</@b.html>