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
	background:url("${base}/static/app/images/remove.png") no-repeat right top;
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
</style>
  <div class="top" id="__top"> </div>
<div class="body">
    <!-- condition -->
	<!-- edit form -->
	
	<form action="${uri}" method="post" id="addForm">
	<table border="0" class="grid" width="800">
<colgroup>
        <col style="width:80px;"></col>
        <col ></col> 
        <col style="width:80px;"></col>
        <col ></col> 
        <col style="width:140px;"></col>
</colgroup>
  <tr class="title"> 
    <td colspan="5">请填写下列信息</td>
  </tr>
    <tr> 
    
    <td>姓名<font color=red>*</font></td>
    <td >
	<input type="text" name="student.name" value="" id="student.name"/>
	</td>
	<td>性别 <font color=red>*</font></td>
	<td>
		<#list genders as tp>
		<label><input type="radio" name="student.gender" value="${tp.id?if_exists?html}" class="rGender"/>${tp.name?if_exists?html}</label>
		</#list>
	</td>
	<td rowspan="5" valign="attop" class="photo">
	<A href="#" id="uploadPic"><img src="${base}/static/app/images/nophoto.jpg"/></A>
	
	</td>
	</tr>
   <tr>
	<td>生日</td>
		<td ><input type="text" value="${birthday?if_exists}" name="student.birthdayText" id="student.birthdayText" maxlength="10" size="12" class="dateField"/></td>
		
	
		
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
	<tr style="background:#fffbec">	
		<td>关联卡号</td>
		<td colspan="4"><div id="sCard"></div></td>
	</tr>
	<#if updateMode>
	<tr class="title"><td colspan="5">如无需修改密码，下述密码框请留空</td></tr>
	<tr>
		<td>登录密码</td>
		<td><input type="password" name="student.password" maxlength="40" id="student.password"></td>
	
		<td>密码确认</td>
		<td colspan="2"><input type="password" maxlength="40" id="student.password2" > <span class="tips">请再输入一次密码</span></td>
	</tr>
	<#else>
	<tr>
		<td>登录密码<font color=red>*</font></td>
		<td><input type="password" name="student.password" maxlength="40" id="student.password"></td>
	
		<td>密码确认<font color=red>*</font></td>
		<td colspan="2"><input type="password" maxlength="40" id="student.password2" > <span class="tips">请再输入一次密码</span></td>
	</tr>
	</#if>
	<tr>
		<td>备注</td>
		<td colspan="4"><input type="text" name="student.comments" id="student.comments" size="60" maxlength="100"></td>
	</tr>

	
	<tr class="title"><td colspan="5">附件 数量:<span id="spanQty"></span> <span class="vbutton" id="uploadFile"><IMG src="${base}/static/laputa/images/16X16/upload.png" width="16" height="16" border="0" alt="" align="absbottom"><span>上传文件</span></span></td></tr>
	<tr>
	<td colspan="5">
		
		<DIV ID="attachmentDiv"></div>
	</td>
	</tr>
	  <tr> 
		<td colspan="5">
		
		<div class="l">
			<input type="button" value="保存" id="saveBtn" accesskey="s"/>
		<input type="submit" value="<@b.text key="submitBtn"/>" id="submitBtn" class="h"/>
		<input type="hidden" name="jsonAttachment" id="jsonAttachment" value="[]" />
		<input type="hidden" name="student.id" id="student.id" value="0"/>
		<input type="hidden" name="student.changePassword" id="student.changePassword" value="false"/>
		</div>
		<div class="r">
			ALT+S 保存
		</div>
		
		</td>
	  </tr> 
	</table>
	</form>
	

<!-- others -->
<#include "/calendar.ftl"/>
<link rel="stylesheet" type="text/css" href="${base}/static/app/css/t-style.css"></link>
<script type="text/javascript" src="${base}/static/library/puerta/t-table.js"></script> 
<script type="text/javascript" src="${base}/static/app/javascript/student_create.js"></script>
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
	

		
		if(isEmpty($F("student.name"))){
			alert("请填写姓名");
			$("student.name").focus();
			return false; 
		}
		

		var len = $$(".rGender:checked").length ;
		if(len==0){
			alert('请选取性别');
			$$(".rGender")[0].focus();
			return false;
		}
		
		

		len = $F("student.comments").length ;
		if(len>200){
			alert("备注信息过长，请填写200字以内信息");
			$("student.comments").focus();
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

		var v = $F("student.password");
		var v2 = $F("student.password2");
		$('student.changePassword').value = 'false';
		if(pageVars.updateMode){
			//student.changePassword
			if(isEmpty(v) && isEmpty(v2)){
			}else if(v!=v2){
				alert("确认密码和密码不一致，请重新输入");
				$('student.password2').focus();
				return false;
			}else{
				$('student.changePassword').value = 'true';
			}



		}else{
			if(isEmpty($F("student.password"))){
				alert("请填写登录密码");
				$('student.password').focus();
				return false;
			}


			if(v!=v2){
				alert("确认密码和密码不一致，请重新输入");
				$('student.password2').focus();
				return false;
			}
		}

		
		return true ;
	}

	var pes = null ;
	window.addEvent('domready',function(){
		pes = new PPage({"id":"pes","pageMode":"pageMode","searchForm":"searchForm","addForm":"addForm"});

	<#if updateMode>
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
		$('student.id').value = "${student.id}";
		pes.edtCard.setValue('${student.barcode!?js_string}','${student.barcode!?js_string}');

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
		
		

		var div= new Element("div",{'class':'text l',html:'${student.no!?js_string} <#if student.updater??>最后修改人:${student.updater.loginId!?html}(${student.updater.name!?html}) ${student.updatedTime.time?datetime}</#if>'});
		var obj = $$("#__top .t-head .t-title");
		div.inject(obj[0],'after');
	<#else>
		pes.mock();
	</#if>
	pes.updateAttachments();

	

});



</script>
</@b.html>