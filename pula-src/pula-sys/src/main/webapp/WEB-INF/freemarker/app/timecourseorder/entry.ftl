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
		<td >订单编号<span class="redStar">*</span></td>
		<td ><input type="text" name="course.no" id="course.no" size="20" maxlength="40"/> </td>
		<td >课程编号<span class="redStar">*</span></td>
		<td ><input type="text" name="course.courseNo" id="course.courseNo" size="20" maxlength="40"/> </td>
	  </tr>
	  
	  <tr> 
		<td >学生编号<span class="redStar">*</span></td>
		<td ><input type="text" name="course.studentNo" id="course.studentNo" size="20" maxlength="40"/>
		</td>
		<!-- <td >购买类型(0/1)</td>
		<td ><input type="text" name="course.buyType" id="course.buyType" size="20" maxlength="10"  class="numberEdit" /></td> -->
	  </tr>
	  
	  <tr> 
		<td >支付金额</td>
		<td colspan="1"><input type="text" name="course.paied" id="course.paied" size="20" maxlength="40"  class="numberEdit"/> </td>
		<td >是否已付款(0-未付/1-已付)</td>
		<td ><input type="text" name="course.orderStatus" id="course.orderStatus" size="20" maxlength="40" class="numberEdit" /></td>
	  </tr>
	  
	  <tr> 
		<td >上课时间</td>
		<td colspan="1"><input type="text" name="course.courseTime" id="course.courseTime" size="20" maxlength="40" /> </td>
		<!-- <td >课程有效时间到</td>
		<td ><input type="text" name="course.effectTime" id="course.effectTime" size="20" maxlength="40" /></td> -->
	  </tr>
	  
	  <tr>
		  <td >课程次数</td>
		  <td colspan="1"><input type="text" name="course.paiedCount" id="course.paiedCount" size="20" maxlength="40" class="numberEdit"/> </td>
		  <td >课程已使用次数</td>
		  <td colspan="1"><input type="text" name="course.usedCount" id="course.usedCount" size="20" maxlength="40" class="numberEdit"/> </td>
	  </tr>
	  
	  <tr> 
		<td >工坊次数</td>
		<td colspan="1"><input type="text" name="course.gongfangCount" id="course.gongfangCount" size="20" maxlength="40" class="numberEdit"/> </td>
		<td >已使用工坊次数</td>
		<td colspan="1"><input type="text" name="course.usedGongFangCount" id="course.usedGongFangCount" size="20" maxlength="40" class="numberEdit"/> </td>
	  </tr>
	  <tr> 
		<td >活动次数</td>
		<td colspan="1"><input type="text" name="course.huodongCount" id="course.huodongCount" size="20" maxlength="40" class="numberEdit"/> </td>
		<td >已使用活动次数</td>
		<td colspan="1"><input type="text" name="course.usedHuodongCount" id="course.usedHuodongCount" size="20" maxlength="40" class="numberEdit"/> </td>
	  </tr>
	  <tr> 
		<td >特殊课程次数</td>
		<td colspan="1"><input type="text" name="course.specialCourseCount" id="course.specialCourseCount" size="20" maxlength="40" class="numberEdit"/> </td>
		<td >已使用特殊课程次数</td>
		<td colspan="1"><input type="text" name="course.usedSpecialCourseCount" id="course.usedSpecialCourseCount" size="20" maxlength="40" class="numberEdit"/> </td>
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
<script type="text/javascript" src="${base}/static/app/javascript/time_course_order.js"></script>
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
	status:'状态', branchName: '分部', classRoomName: '教室',
	buyType: '购买类型', paied:'购买付款', paiedCount:'购买次数', courseNo:'课程编号', studentNo:'学生编号', usedCount:'已使用次数', remainCost:'剩余款',
	updateTime:'最后更新', updator:'操作人'
}


	function check(){
		if(isEmpty($F("course.no"))){
			alert("请填写编号");
			$("course.no").focus();
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