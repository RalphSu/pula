<#import "/app/macros/commonBase.ftl" as b><@b.html title="course.title">

<style>
#workerList A:hover{
	text-decoration:line-through;
}
</style>
  <div class="top" id="__top"> </div>
<div class="body">
 	<!--  search form -->
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
		<td ><input type="text" name="course.no" id="course.no" size="20" maxlength="40"/> </td>
	  
		<td >名称<span class="redStar">*</span></td>
		<td ><input type="text" name="course.name" id="course.name" size="20" maxlength="40"/> </td>
	  </tr> 

	  <tr> 
		<td >开始日期</td>
		<td ><input type="text" name="course.startTimeText" id="course.startTimeText" size="12" maxlength="10" class="dateField"/>
		</td>
		<td >结束日期</td>
		<td ><input type="text" name="course.endTimeText" id="course.endTimeText" size="12" maxlength="10" class="dateField"/></td>
	  </tr><tr> 
		<td >分部名字</td>
		<td colspan="3"><input type="text" name="course.branchName" id="course.branchName" size="60" maxlength="40"/> </td>
	  </tr><tr> 
		<td >教室名字</td>
		<td colspan="3"><input type="text" name="course.classRoomName" id="course.classRoomName" size="60" maxlength="40"/> </td>
	  </tr>
	  <tr> 
		<td >最大学员数量</td>
		<td colspan="3"><input type="text" name="course.maxStudentNum" id="course.maxStudentNum" class="numberEdit" size="12" maxlength="10"/> </td>
	  </tr>
	  <tr> 
		<td >开课时间</td>
		<td colspan="1"><input type="text" name="course.startHour" id="course.startHour" size="12" class="numberEdit" maxlength="10"/>(点) </td>
		<td colspan="2"><input type="text" name="course.startMinute" id="course.startMinute" size="12" class="numberEdit" maxlength="10"/>(分) </td>
	  </tr>
	  <tr> 
		<td >开课天(周一到周日, 1-7)</td>
		<td colspan="3"><input type="text" name="course.startWeekDay" id="course.startWeekDay" size="12" class="numberEdit" maxlength="10"/> </td>
	  </tr>
	  <tr> 
		<td >课长(分钟)</td>
		<td colspan="3"><input type="text" name="course.durationMinute" id="course.durationMinute" class="numberEdit" size="12" maxlength="10"/> </td>
	  <tr> 
		<td >价格</td>
		<td colspan="3"><input type="text" name="course.price" id="course.price" size="12" class="numberEdit" maxlength="10"/> </td>
	  </tr>
	   <tr> 
		<td >备注</td>
		<td colspan="3"><input type="text" name="course.comments" id="course.comments" size="60" maxlength="80"/> </td>
	  </tr> 
	  <tr> 
		<td colspan="4">
		<input type="submit" value="<@b.text key="submitBtn"/>" id="submitBtn"/>
		<input type="hidden" name="course.id" id="course.id"/>
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
<script type="text/javascript" src="${base}/static/app/javascript/time_course.js"></script>
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
	name:'名称',no:'编号',domain:'课程',expiredTime:'结束日期',publishTime:'开始日期',showInWeb:'显示在网站上',indexNo:'序号',
	status:'状态', branchName: '分部', classRoomName: '教室'
}


	function check(){
		if(isEmpty($F("course.no"))){
			alert("请填写编号");
			$("course.no").focus();
			return false; 
		}
		if(isEmpty($F("course.name"))){
			alert("请填写名称");
			$("course.name").focus();
			return false; 
		}

		var b = true;
		var eel = null ;
		$$(".numberEdit").each ( function (el){
			var v = Number.from( el.value ) ;
			if( b && !v && v!=0  ) {
				eel = el ;
				b= false;
			}
			
		});

		if(!b){
			alert("请正确填写数值");
			eel.select();
			eel.focus();
			return false;
		}

		len = $F("course.comments").length ;
		if(len>200){
			alert("备注信息过长，请填写200字以内信息");
			$("course.comments").focus();
			return false;
		}

		var pass = true ;
		$$(".dateField").each ( function(el){
			if( !pass ) return ;
			var dv = $F( el );
			if( !isEmpty( dv ) &&  !isDate( dv )){
				alert("请正确填写日期");
				el.focus();
				pass =false; 
				return ;
			}
		});

		if(!pass ) {
			return false ;
		}
		
		return true ;
	}
</script>
</@b.html>