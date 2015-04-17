<#import "/app/macros/commonBase.ftl" as b><@b.html title="授课记录管理">
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
	
	<form  method="post" id="addForm">
	<table border="0" class="grid" width="800">
<colgroup>
        <col style="width:100px;"></col>
        <col ></col> 
        <col style="width:100px;"></col>
        <col ></col> 
</colgroup>
  <tr class="title"> 
    <td colspan="4">培训信息</td>
  </tr>
    <tr> 
    <td width="100">开始时间 <font color=red>*</font></td>
    <td width="336" ><input name="form.startTimeText" type="text" id="form.startTimeText" value="${startDate}" size="10" maxlength="10" class="dateField"/>
     
      <select name="form.startTimeHour" id="form.startTimeHour">
	  <#list hours as hrs>
        <option value="${hrs.id}" <#if startHour??&&startHour?string==hrs.id>selected</#if>>${hrs.name!?html}</option>
	  </#list>
      </select>
      时
      <select name="form.startTimeMinute" id="form.startTimeMinute">
	  <#list minutes as hrs>
        <option value="${hrs.id}" <#if startMinute??&&startMinute?string==hrs.id>selected</#if>>${hrs.name!?html}</option>
	  </#list>
      </select>
      分</td>
    <td >结束时间 <font color=red>*</font></td>
    <td width="300" ><input name="form.endTimeText" type="text" id="form.endTimeText" value="${endDate}" size="10" maxlength="10" class="dateField"/>
     
      <select name="form.endTimeHour" id="form.endTimeHour">
	  <#list hours as hrs>
        <option value="${hrs.id}" <#if endHour??&&endHour?string==hrs.id>selected</#if>>${hrs.name!?html}</option>
	  </#list>
      </select>
      时
      <select name="form.endTimeMinute" id="form.endTimeMinute">
	  <#list minutes as hrs>
        <option value="${hrs.id}" <#if endMinute??&&endMinute?string==hrs.id>selected</#if>>${hrs.name!?html}</option>
	  </#list>
      </select>
      分</td>
	</tr>
   <tr> 
	<td>地点 <font color=red>*</font></td>
	<td><#if headquarter><select name="form.branchId" id="cBranchId" >
		<option value="">请选择...</option>
		<#list branch as tp>
		<option value="${tp.id?if_exists?html}" <#if courseTaskResult??&&courseTaskResult.branchId==tp.id>selected</#if>>${tp.name?if_exists?html}</option>
		</#list>
		</select>
		</#if>
		<select name="form.classroomId" id="form.classroomId" class="_cBranchId"><option value="0">请选择...</option>
		<#if !headquarter><#list classroomList as tp>
		<option value="${tp.id?if_exists?html}" <#if courseTaskResult??&&courseTaskResult.classroomId==tp.id>selected</#if>>${tp.name?if_exists?html}</option>
		</#list></#if>
		</select></td>
	<td>课程 <font color=red>*</font></td>
	<td>
		<div class="tips" id="courseTips" style="width:100px"> 请先选择教室 </div>
		<div id="courseDiv" class="h">
		<select name="form.courseCategoryId" id="categoryId">
		<option value="">请选择...</option>
		<#list courseCategories as tp>
		<option value="${tp.id?if_exists?html}" <#if courseTaskResult??&&courseTaskResult.courseCategoryId==tp.id>selected</#if>>${tp.name?if_exists?html}</option>
		</#list>
		</select>
		<select name="form.courseId" id="form.courseId" class="_categoryId"><option value="0">请选择...</option>
		</select>
		</div>
	</td>
	</tr>

	
	
	  <tr>
	    <td>教师 <font color=red>*</font></td>
	    <td colspan="3"><div id="sMasterNo"></div></td>
      </tr>
	  <tr>
	    <td>助教1 <font color=red>*</font></td>
	    <td colspan="3"><div id="sAssistant1No"></div></td>
      </tr>
	  <tr>
	    <td>助教2</td>
	    <td colspan="3"><div id="sAssistant2No"></div></td>
      </tr>
	  <tr >
	    <td colspan="4">
	      <input type="button" value="保存" id="saveBtn" accesskey="s"/>
			<input type="submit" class="h" id="submitBtn"/>
			<input type="hidden" name="form.id" value="0" id="form.id"/>
	   </td>
      </tr>
	  </table>
		</form>
		
		<form id="saveForm">
	<table border="0" class="grid h" width="910" id="tblStudent">
	  <tr class="title">
	    <td >相关学员</td>
      </tr>
	  <tr>
	    <td >
		<div id="dt"></div>
		</td>
        </tr><tr class="title">
	    <td >新增学员</td>
      </tr>
      <tr>	        
	        <td ><div id="sStudentNo"></div></td>
	        </tr>
	      <tr>
	        <td><span class="l">
	          <input type="submit" value="保存" id="saveBtn2" accesskey="s"/>
	        </span></td>
	        </tr>
        </table></td>
      </tr>
	  </table>
	  </form>
</div>	

<!-- others -->
<#include "/calendar.ftl"/>
<link rel="stylesheet" type="text/css" href="${base}/static/app/css/t-style.css"></link>
<script type="text/javascript" src="${base}/static/library/puerta/t-table.js"></script> 
<script type="text/javascript" src="${base}/static/app/javascript/coursetaskresult_create.js"></script>
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
	updateMode:${updateMode?string("true","false")},
	noPhoto:'${base}/static/app/images/nophoto.jpg'
	<#if courseTaskResult??>
	,courseId: ${courseTaskResult.courseId}
	</#if>
}

var lang = {
	name:'名称',no:'编号',domain:'<#if !updateMode>新增<#else>修改</#if>授课记录',
	materialNo:'材料编号',
	materialName:'材料名称',space:'区域',picNo:'图号',msgSelectFirst:'请选择...',uploadPic:'上传照片',uploadFile:'上传作品',
	quantity:'数量',lineMsg:"第{index}行:{message}\n",needQty:'需要填写数量',needSpace:'需要选取区域',
	needDetail:"至少需要填写一行明细数据",cancel:'取消',relationShip:'关系',phone:'电话',address:'地址',
	name:'姓名',attachmentItem:'<span class="fileName"><a href="#" data="{fileId}" dataId="{id}" class="download" title="下载">{fileName}</a></span>',
	noneAttachment:"<span class='gray'>暂无附件</span>",tipsOfTSN:'输入名称加载'
}

	function checkStudent(){
		if(isEmpty($F("cStudentNo"))){
			alert( "请填写学员编号");
			$('cStudentNo').focus();
			return false;
		}
		
		return true;
	}

	function check(){
		var chk = true ;
		var matchEl = null ;
		$$(".dateField").each (function(el,idx){
			if(chk && !isDate( el.value )){
				chk = false;
				matchEl = el ;
			}
		});

		if(!chk){
			alert("请正确填写日期");
			matchEl.focus();
			return false;
		}
		var v = $F("form.classroomId");
		if(isEmpty(v)|| v == "0"){
			alert('请选取教室');
			$('form.classroomId').focus();
			return false;
		}
		v = $F("form.courseId");
		if(isEmpty(v)|| v == "0"){
			alert('请选取课程');
			$('form.courseId').focus();
			return false;
		}
		if(isEmpty($F("cMasterNo"))){
			alert("请填写教师编号");
			$('cMasterNo').focus();
			return false;
		}

		if(isEmpty($F("cAssistant1No"))){
			alert("请填写助教1编号");
			$('cAssistant1No').focus();
			return false;
		}

		return true; 
	}



	var pes = null ;
	window.addEvent('domready',function(){
		pes = new PPage({"id":"pes","pageMode":"pageMode","searchForm":"searchForm","addForm":"addForm"});

		<#if courseTaskResult??>
			pes.edtMasterNo.setValue('${courseTaskResult.masterNo!?js_string}','${courseTaskResult.masterName!?js_string}');		
			pes.edtAssistant1No.setValue('${courseTaskResult.assistant1No!?js_string}','${courseTaskResult.assistant1Name!?js_string}');
			<#if courseTaskResult.assistant2No??>
			pes.edtAssistant2No.setValue('${courseTaskResult.assistant2No!?js_string}','${courseTaskResult.assistant2Name!?js_string}');		
			</#if>
			$('form.id').value = '${courseTaskResult.id}';
		</#if>
	});

</script>
</@b.html>