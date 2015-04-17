<#import "/app/macros/commonBase.ftl" as b>
<@b.html title="课程计划定制"> <#include "/calendar.ftl"/>

<style>
#calendar{
	width:870px;
	border:1px solid #ccc;
	float:left;
	position:relative;
	padding:10px 0px ;
}
#calendar .calendar-header{
	text-align:center;
	height:30px;
	font-size:14px;
	font-weight:bold;
}	
#calendar .calendar-buttons{
	position:absolute;
	right:20px;
	top:5px;
}

#calendar .calendar-branch{
	position:absolute;
	left:20px;
	top:10px;
	font-size:12px;
}
#calendar ul{
	list-style:none;
	padding:0px;
	margin:0px;
	margin:10px ;
}
.weekday-block{
	width:120px;
	height:25px;
	float:left;
	border-top:1px solid #ccc;
	border-left:1px solid #ccc;
	text-align:center;
	line-height:25px;
	background:#ccffff;
}

.block-f{
	
	border-right:1px solid #ccc;
}

.block-b{
	
	border-bottom:1px solid #ccc;
}
.block-e{
	
	border-right:1px solid #ccc;
	__border-bottom:1px solid #ccc;
}

.none-block{
	background:#f0f0f0;
	 text-indent: -40em;
}


.day-block{
	width:120px;
	float:left;
	border-top:1px solid #ccc;
	border-left:1px solid #ccc;
	position:relative;
}

.day-block .date{
	text-align:center;
	font-size:14px;
	padding-top:4px;
}

.day-block .checkbox{
	position:absolute;
	top:1px;
	left:1px;

}
.day-block .items{
	margin:5px;
}

/*.day-block .items h3{
	margin:0px;
	padding-left:4px;
	font-size:12px;
	background:#e9f9fe;
	height:25px;
	line-height:25px;
	border-bottom:1px dashed #e6e8fb;
	overflow:hidden;
	cursor:pointer;
	position:relative;
	text-align:center;
	font-weight:normal;
	color:#ccc;
	
}
*/


.day-block .items div a{
	display:block;
	cursor:pointer;
	height:20px;

}

.day-block .items div a.empty{
}


.restday {
	color:#358fc6;
	font-weight:bold;
	cursor:pointer;
}
#calendar h3.workday {
	color:#ff0000;
	font-weight:normal;
	text-align:center;
}

.weekend{
	background:#ceecff;
}
.book-hover{
	font-weight:normal;
}

#lblSelect{
	background:#ffffcc;
}

#indicator_add{
	position:absolute;
	right:4px;
	top:2px;
	background:url('${base}/static/laputa/images/icons/add.gif') ;
	height:16px;
	width:16px;	
	display:block;
	cursor:pointer;
}

#spEmployee a {
	padding-right:10px;
	display:block;
	margin:2px;
}

#spEmployee a.hover{
	padding:2px;
}

#spEmployee a.hover:hover{
	text-decoration: line-through;
	border:1px dashed #ccc;
}


#spEmployee a div.plan-text{
	font-weight:bold;
}
#spEmployee a div.plan-tips{
	margin:5px;
	background:#fff4cc;
	border:1px solid #ff9900;
}


.c{
	clear:both;
	width:0px;height:0px;
}

#btnSummary{
	color:#339900;
}

#spDate{
	color:#336600;
}


</style>
  <div class="top" id="__top"> </div>
  <div class="body">
  <form method="post" id="frmPost">
    <!-- condition -->
    <div id="calendar">
		<div class="calendar-header ">
		<div class="calendar-branch">
			分支机构:<#if headquarter>
			<select name="branchId" id="branchId">
		<option value="0">请选择...</option>
		<#list branches as tp>
		<option value="${tp.id?if_exists?html}" <#if branchId==tp.id>selected</#if>>${tp.no!?html} ${tp.name?if_exists?html}</option>
		</#list>
		</select><#else>${branch.name!?html}</#if>
		</div>


		<select id="selYear">
		<#list 2014..nowYear+1 as m >
		<option value="${m}">${m}</option>
		</#list>
		</select>
		年
		<select id="selMonth">
		<#list 1..12 as m >
		<option value="${m}">${m}</option>
		</#list>
		</select> 月<input type="button" value="跳转" id="btnGoto" class="h"/></div>
		<div class="calendar-buttons "><input type="button" value="上一个月" id="btnPrevMonth"><input type="button" value="下一个月" id="btnNextMonth">
		
		<input type="button" value="汇总" style="margin-left:30px" id="btnSummary" class="h"/>
		</div>
		<div class="container"></div>
	</div>
  </form>
  <#if canShow>
	<div class="l" style="width:300px;padding-left:4px">
		<input type="button" value="全部" id="btnAll" class="h"/>
		<input type="button" value="工作日" id="btnWorkDay" class="h"/>
		<input type="button" value="周末" id="btnWeekEnd" class="h"/>
		<input type="button" value="反选" id="btnReserve" class="h"/>
		<input type="button" value="清除" id="btnClear" class="h"/>
		<hr style="height:1px"  class="h">
		<table border="0" class="grid " width="300" id="tblDayInfo">
         <colgroup>
		 <col style="width:100px"/>
		 <col/>
		 </colgroup>
          <tr class="title">
            <td colspan="2" ><div class="l">排班</div>
			<div class="l">(<span id="spEmployeeCount"></span>)</div>
              <div class="r"><span id="spDate"></span></div>
              </td>
          </tr>
         
            <td colspan="2"><span id="spEmployee"></span></td>
          </tr>
		</table>
		<form id="frmSetup" class="h">
		<table border="0" class="grid " width="300" id="">
         <colgroup>
		 <col style="width:100px"/>
		 <col/>
		 </colgroup>
		<tr class="title inputRow">
		<td colspan="2">新增排班</td>
		</tr>
		<tr class="inputRow">
		<td>开始时间<font color="red">*</font></td>
		<td ><SELECT name="form.hour">
			<#list hours as h><OPTION value="${h.id}">${h.name!?html}</option></#list>
		</SELECT>
		<SELECT name="form.minute">
			<#list minutes as h><OPTION value="${h.id}">${h.name!?html}</option></#list>
		</SELECT></td>
		</tr> 
		<tr>
		<tr class="inputRow">
		<td>教师编号<font color="red">*</font></td>
		<td ><div id="sTeacherNo"></div></td>
		</tr> 
		<tr class="inputRow">
		<td>助教1编号<font color="red">*</font></td>
		<td ><div id="sAssistant1No"></div></td>
		</tr> <tr class="inputRow">
		<td>助教2编号</td>
		<td ><div id="sAssistant2No"></div></td>
		</tr> 
		<tr>
		<td>教室<font color="red">*</font></td>
		<td >
		<#if classroomList??>
		<select name="form.classroomId" id="classroomId">
			<option value="">请选取...</option>
			<#list classroomList as cl>
			<option value="${cl.id}">${cl.no!?html} ${cl.name!?html}</option>
			</#list>
			</select>
		</#if></td>
		</tr> 
		<tr>
		<td>课程<font color="red">*</font></td>
		<td >
		
		<span id="frmSelect"><SELECT id="courseCategoryId">
			<option value="">请选取...</option>
			<#list types as t><OPTION value="${t.id!?html}">${t.name!?html}</option></#list>
		</SELECT><SELECT name="form.courseId" id="form.courseId" class="_courseCategoryId">
			<OPTION value="" selected>请选取...</option>
		</SELECT>
		</span>
		<span id="lblSelect">请先选取教室</span>
		</td>
		</tr>
		<tr class="inputRow">
		<td colspan="2" ><textarea name="form.comments" id="fComments" style="width:280px;height:100px"></textarea></td>
		</tr> 
		
		<tr class="inputRow">
            <td colspan="2">
			 <div class="l"> <input type="submit" value="保存" id="sendBtn"/></div>
			 <div class="r"> <input type="button" value="取消" id="cancelBtn"/></div>
			  <input type="hidden" name="form.date" id="fDate" />
			  <input type="hidden" name="form.branchId" id="fBranchId" value="${branchId}"/>
			  </td>
          </tr>
        </table>
		</form>
		
	</div>
	</#if>
	<div class="c"></div>


	<span id="indicator_add">
	</span>

  </div>
  <!-- others -->
<link rel="stylesheet" type="text/css" href="${base}/static/app/css/t-style.css"></link>
<script type="text/javascript" src="${base}/static/library/puerta/t-table.js"></script> 
<script type="text/javascript" src="${base}/static/app/javascript/coursetaskplan.js"></script> 
<script type="text/javascript" src="${base}/static/library/puerta/t-simple-no.js"></script>
<script type="text/javascript" src="${base}/static/library/mootools/modules/mbox/mt.mbox.js"></script>
<script type="text/javascript" src="${base}/static/laputa/m/ajax-upload.js"></script> 
<script type="text/javascript">

var pageVars = {
	queryString:'',
	action : '_create',
	id :'',minOfYear:2014,
	base :'${base}',year:${year},month:${month},maxYear:${nowYear}+1,forceView:${forceView?string("true","false")},
	nowYear :${nowYear},nowMonth:${nowMonth},branchId:${branchId},canSetup:${canSetup?string("true","false")}
}

var lang = {
	
	domain:'课表计划<#if forceView>查询<#else>定制</#if>',
	auditor:'审核人',auditTime:'审核时间'
	,url:'链接',noneURL:'无链接',createdTime:'申请时间',ip:'IP',audit:'审核',auditedList:'已审核列表',requestList:'待审核列表'
	,ownerTemplet:'{ownerName} {ownerId}',comments:'备注'
	,calendarHeader:'{year}年{month}月',minOfYear:'已经到达最前的一个月了',maxOfYear:'已经到达最后一个月了',
	weekDays : new Array("星期日","星期一","星期二","星期三","星期四","星期五","星期六"),opened:'有效时段',sendOk:'发送成功'
}
	function checkSetup(){
		if(isEmpty($F("cTeacherNo"))){
			alert("请填写教师编号");
			$("cTeacherNo").focus();
			return false;
		}
		if(isEmpty($F("cAssistant1No"))){
			alert("请填写助教1编号");
			$("cAssistant1No").focus();
			return false;
		}
		if(isEmpty($F("classroomId"))){
			alert("请选择教室");
			$("cAssistantNo").focus();
			return false;
		}
		
		if(isEmpty($F("classroomId"))){
			alert("请选择教室");
			$("classroomId").focus();
			return false;
		}
		
		if(isEmpty($F("form.courseId"))){
			alert("请选择课程");
			$("form.courseId").focus();
			return false;
		}

		var len = $F("fComments").length ;
		if(len>200){
			alert("备注信息过长，请填写200字以内信息");
			$("fComments").focus();
			return false;
		}
		return true ;
	}

	
</script> 
</@b.html>
