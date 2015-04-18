<#import "/app/macros/commonBase.ftl" as b><@b.html title="学生">
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
	height:16px;
	/*background:url("${base}/static/app/images/remove.png") no-repeat right top;*/
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
<#include "studentTop.inc.ftl"/>
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
    <td colspan="5">学生信息 <#if student.updater??>最后修改人:${student.updater.loginId!?html}(${student.updater.name!?html}) ${student.updatedTime.time?datetime}</#if></td>
  </tr>
    <tr> 
    
    <td>姓名<font color=red>*</font></td>
    <td >
	<input type="text" name="student.name" value="" id="student.name"/>
	</td>
	<td>性别 <font color=red>*</font></td>
	<td>
		${student.genderName}
	</td>
	<td rowspan="5" valign="attop" class="photo">
	<A href="#" id="uploadPic"><img src="${base}/static/app/images/nophoto.jpg"/></A>
	
	</td>
	</tr>
   <tr>
	<td>生日</td>
		<td ><input type="text" value="${birthday?if_exists}" name="student.birthdayText" id="student.birthdayText" maxlength="10" size="12" class="dateField"/></td>
		
	<td>卡号</td>
		<td><input type="text" name="student.barcode" maxlength="20" id="student.barcode" style="width:180px" class="barcode"></td>
		
	</tr>
	<tr class="title"><td colspan="4">家长信息</td></tr>
	<tr>
	<td>家长姓名</td>
    <td >
	<input type="text" name="student.parentName" value="" id="student.parentName" maxlength="20"/>
	</td>
	<td>称呼</td>
	 <td >
	<input type="text" name="student.parentCaption" value="" id="student.parentCaption" maxlength="20"/>
	</td>
	</tr>
	<tr>
		<td>电话</td>
		<td><input type="text" name="student.phone" maxlength="20" id="student.phone"></td>
	
		<td>手机</td>
		<td><input type="text" name="student.mobile" maxlength="20" id="student.mobile"></td>
	</tr>
	<tr>
		<td>现住地址</td>
		<td colspan="4"><input type="text" name="student.address" size="60" maxlength="100" id="student.address"></td>
	</tr>
	<tr>
		<td>Email</td>
		<td><input type="text" name="student.email" maxlength="40" id="student.email"></td>
	
		<td>邮编</td>
		<td colspan="2"><input type="text" name="student.zip" maxlength="12" id="student.zip" ></td>
	</tr>
	
	
	<tr>
		<td>备注</td>
		<td colspan="4"><input type="text" name="student.comments" id="student.comments" size="60" maxlength="100"></td>
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
<script type="text/javascript" src="${base}/static/app/javascript/student_view.js"></script>
<script type="text/javascript" src="${base}/static/library/mootools/modules/mbox/mt.mbox.js"></script>
<script type="text/javascript" src="${base}/static/library/puerta/t-simple-no.js"></script>
<script type="text/javascript" src="${base}/static/library/puerta/t-datagrid.js"></script>
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
	name:'名称',no:'编号',domain:'<#if updateMode>修改<#else>新增</#if>学生',
	materialNo:'材料编号',
	materialName:'材料名称',space:'区域',picNo:'图号',msgSelectFirst:'请选择...',uploadPic:'上传照片',uploadFile:'上传附件',
	quantity:'数量',lineMsg:"第{index}行:{message}\n",needQty:'需要填写数量',needSpace:'需要选取区域',
	needDetail:"至少需要填写一行明细数据",cancel:'取消',relationShip:'关系',phone:'电话',address:'地址',
	name:'姓名',attachmentItem:'<span class="fileName"><a href="#" data="{fileId}" dataId="{id}" class="download" title="下载">{fileName}</a><a href="#" data="{fileId}" dataId="{id}" class="remove" title="删除"></a></span>',
	noneAttachment:"<span class='gray'>暂无附件</span>"
}







	function check(){
	

		return true ;
	}

	var pes = null ;
	window.addEvent('domready',function(){
		pes = new PPage({"id":"pes","pageMode":"pageMode","searchForm":"searchForm","addForm":"addForm"});

		//set value 
		
		$('student.name').value = "${student.name!?js_string}";
		$$('.rGender').each ( function(el ) {
			if( el.value == ${student.gender} ) {
				el.checked = true ;
			}
		});
		
		<#if student.birthday??>$('student.birthdayText').value = "${student.birthday.time?date}";</#if>

		$('student.parentName').value = "${student.parentName!?js_string}";
		$('student.parentCaption').value = "${student.parentCaption!?js_string}";
		$('student.phone').value = "${student.phone!?js_string}";
		$('student.mobile').value = "${student.mobile!?js_string}";
		$('student.address').value = "${student.address!?js_string}";
		$('student.zip').value = "${student.zip!?js_string}";
		$('student.email').value = "${student.email!?js_string}";
		$('student.comments').value = "${student.comments!?js_string}";
		$('student.barcode').value = ('${student.barcode!?js_string}');

		//icon
		<#if icon??>
			pes.showPic( '${icon.fileId!?js_string}','${icon.id}') ;
		</#if>
		

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