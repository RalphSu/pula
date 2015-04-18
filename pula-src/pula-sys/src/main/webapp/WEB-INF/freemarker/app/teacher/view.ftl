<#import "/app/macros/commonBase.ftl" as b><@b.html title="教师">
<style>
.top .text{
	line-height:25px;
	height:25px;
	margin-top:3px;
	color:#5f5f5f;
}

.photo  {
	width:140px;
}

.photo a img {
	border:1px solid #ccc;
	margin:0px auto;
	width:120px;
	height:120px;
	display:block;
}
#attachmentDiv span.fileName{
	border:1px solid #ccc;
	background:#fafafa;
	padding:0px 16px 0px 4px;
	display:inline-block;
	
	height:20px;
	line-height:20px;
	position:relative;
	margin-right:10px;
}
#attachmentDiv a.remove{
	display:block;
	position:absolute;
	width:16px;
	height:16px;	/*background:url("${base}/static/app/images/remove.png") no-repeat right top;*/
	cursor:pointer;
	right:0px;
	top:0px;
}

#attachmentDiv span.gray{
	color:#ccc;
}


#uploadFile{
width:80px;
height:20px;
cursor:pointer;
}

.body input{
	border:0px;
}

</style>
<#assign wt_target="main"/>
<#include "teacherTop.inc.ftl"/>
<div class="body">
    <!-- condition -->
	<!-- edit form -->
	
	<table border="0" class="grid" width="800">
<colgroup>
        <col style="width:80px;"></col>
        <col ></col> 
        <col style="width:80px;"></col>
        <col ></col> 
        <col style="width:140px;"></col>
</colgroup>
  <tr class="title"> 
    <td colspan="5">教师信息 <#if teacher.updater??>最后修改人:${teacher.updater.loginId!?html}(${teacher.updater.name!?html}) ${teacher.updatedTime.time?datetime}</#if></td>
  </tr>
    <tr> 
    <td width="100">编号</td>
    <td >
	<input type="text" name="teacher.no" value="" id="teacher.no"/>
	</td>
    <td>姓名</td>
    <td >
	<input type="text" name="teacher.name" value="" id="teacher.name"/>
	</td>
	<td rowspan="5" valign="attop" class="photo">
	<A href="#" id="uploadPic"><img src="${base}/static/app/images/nophoto.jpg"/></A>
	
	</td>
	</tr>
	
   <tr> 
	<td>性别</td>
	<td>
		${teacher.genderName}
	</td>
	<td>状态</td>
	<td>
	${teacher.statusName}
	</td>
   </tr><tr>
		<td>电话</td>
		<td><input type="text" name="teacher.phone" maxlength="20" id="teacher.phone"></td>
	
		<td>手机</td>
		<td><input type="text" name="teacher.mobile" maxlength="20" id="teacher.mobile"></td>
	</tr><tr>
		<td>Email</td>
		<td><input type="text" name="teacher.email" maxlength="40" id="teacher.email"></td>
	
		<td>卡号</td>
		<td><input type="text" name="teacher.barcode" maxlength="20" id="teacher.barcode" style="width:180px" class="barcode"></td>
	</tr>		
	<tr>
		<td>现住地址</td>
		<td colspan="3"><input type="text" name="teacher.liveAddress" size="60" maxlength="100" id="teacher.liveAddress"></td>
	</tr>
	<tr>
		<td>特长</td>
		<td><input type="text" name="teacher.speciality" maxlength="20" id="teacher.speciality"></td>
	
		<td>邮编</td>
		<td colspan="2"><input type="text" name="teacher.zip" maxlength="12" id="teacher.zip" ></td>
	</tr>
	<tr>
		<td>联系人</td>
		<td colspan="4">
		<input type="text" name="teacher.linkman" size="20" maxlength="20" id="teacher.linkman">
		与本人关系：<input type="text" name="teacher.linkmanCaption" size="20" maxlength="20" id="teacher.linkmanCaption">
		电话：<input type="text" name="teacher.linkmanTel" size="20" maxlength="20" id="teacher.linkmanTel">
		
		</td>
	</tr>
	<tr>
		<td>毕业院校</td>
		<td><input type="text" name="teacher.school" id="teacher.school" size="24" maxlength="100"></td>
	
		<td>生日</td>
		<td colspan="2"><input type="text" value="${birthday?if_exists}" name="teacher.birthdayText" id="teacher.birthdayText" maxlength="10" size="12" class="dateField"/></td>
	</tr>
	<tr>
		<td>出生地</td>
		<td><input type="text" name="teacher.homeplace" maxlength="20" id="teacher.homeplace"></td>
	
		<td>身份证号</td>
		<td colspan="2"><input type="text" name="teacher.identity" maxlength="30" id="teacher.identity"></td>
	</tr>
	
	<tr>
		<td>户籍地址</td>
		<td colspan="4"><input type="text" name="teacher.hjAddress" size="60" maxlength="100" id="teacher.hjAddress"></td>
	</tr>
	<tr><td>入职日期</td>
		<td  colspan="4"><input type="text" value="${joinDate?if_exists}" name="teacher.joinTimeText" id="teacher.joinTimeText" maxlength="10" size="12" class="dateField"/>
		
		离职日期
		<input type="text" value="${joinDate?if_exists}" name="teacher.leaveTimeText" id="teacher.leaveTimeText" maxlength="10" size="12"  class="dateField"/>
	</tr><tr>
		<td>备注</td>
		<td colspan="4"><input type="text" name="teacher.comments" id="teacher.comments" size="60" maxlength="100"></td>
	</tr>

	
	<tr class="title"><td colspan="5">附件 数量:<span id="spanQty"></span></td></tr>
	<tr>
	<td colspan="5">
		
		<DIV ID="attachmentDiv"></div>
	</td>
	</tr>
	
	</table>
	

<!-- others -->
<#include "/calendar.ftl"/>
<link rel="stylesheet" type="text/css" href="${base}/static/app/css/t-style.css"></link>
<script type="text/javascript" src="${base}/static/library/puerta/t-table.js"></script> 
<script type="text/javascript" src="${base}/static/app/javascript/teacher_view.js"></script>
<script type="text/javascript" src="${base}/static/library/mootools/modules/mbox/mt.mbox.js"></script>
<script type="text/javascript" src="${base}/static/library/puerta/t-simple-no.js"></script>
<script type="text/javascript" src="${base}/static/library/mootools/modules/UUID.js"></script>
<script type="text/javascript">

var pageVars = {
	queryString:'',
	action : '_create',
	id :'',
	base :'${base}',
	imagePath:'${base}/static/laputa/images/icons/',
	updateMode:${updateMode?string("true","false")},
	noPhoto:'${base}/static/app/images/nophoto.jpg'
}

var lang = {
	name:'名称',no:'编号',domain:'查看教师',
	materialNo:'材料编号',
	materialName:'材料名称',space:'区域',picNo:'图号',msgSelectFirst:'请选择...',uploadPic:'上传照片',uploadFile:'上传附件',
	quantity:'数量',lineMsg:"第{index}行:{message}\n",needQty:'需要填写数量',needSpace:'需要选取区域',
	needDetail:"至少需要填写一行明细数据",cancel:'取消',relationShip:'关系',phone:'电话',address:'地址',
	name:'姓名',attachmentItem:'<span class="fileName"><a href="#" data="{fileId}" dataId="{id}" class="download" title="下载">{fileName}</a></span>',
	noneAttachment:"<span class='gray'>暂无附件</span>"
}






	var pes = null ;
	window.addEvent('domready',function(){
		pes = new PPage({"id":"pes","pageMode":"pageMode","searchForm":"searchForm","addForm":"addForm"});

	
		//set value 
		$('teacher.no').value = "${teacher.no!?js_string}";
		$('teacher.name').value = "${teacher.name!?js_string}";
		
		$('teacher.school').value = "${teacher.school!?js_string}";
		<#if teacher.birthday??>$('teacher.birthdayText').value = "${teacher.birthday.time?date}";</#if>
		

		$('teacher.homeplace').value = "${teacher.homeplace!?js_string}";
		$('teacher.identity').value = "${teacher.identity!?js_string}";
		$('teacher.liveAddress').value = "${teacher.liveAddress!?js_string}";
		$('teacher.hjAddress').value = "${teacher.hjAddress!?js_string}";
		$('teacher.phone').value = "${teacher.phone!?js_string}";
		$('teacher.mobile').value = "${teacher.mobile!?js_string}";
		$('teacher.linkman').value = "${teacher.linkman!?js_string}";
		$('teacher.linkmanCaption').value = "${teacher.linkmanCaption!?js_string}";
		$('teacher.linkmanTel').value = "${teacher.linkmanTel!?js_string}";
		$('teacher.speciality').value = "${teacher.speciality!?js_string}";
		$('teacher.zip').value = "${teacher.zip!?js_string}";
		$('teacher.email').value = "${teacher.email!?js_string}";
		$('teacher.barcode').value = "${teacher.barcode!?js_string}";
		<#if teacher.joinTime??>$('teacher.joinTimeText').value = "${teacher.joinTime.time?date}";</#if>
		<#if teacher.leaveTime??>$('teacher.leaveTimeText').value = "${teacher.leaveTime.time?date}";</#if>
	
		$('teacher.comments').value = "${teacher.comments!?js_string}";
		//icon
		<#if icon??>
			pes.showPic( '${icon.fileId!?js_string}','${icon.id}') ;
		</#if>
		
		//items 

		//attachments
		<#list attachments as af>
		<#if !af.removed>
		pes.vars.attachments.push ( { fileId: '${af.fileId!?js_string}' , fileName : '${af.name!?js_string}', type : ${af.type} ,id :${af.id}}  );
		</#if>
		</#list>
		
		

	
		pes.updateAttachments();

	

});



</script>
</@b.html>