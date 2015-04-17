<#import "/app/macros/commonBase.ftl" as b><@b.html title="教师">
<style>
.top .text{
	line-height:25px;
	height:25px;
	margin-top:5px;
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
    <td width="100">编号<font color=red>*</font></td>
    <td >
	<input type="text" name="teacher.no" value="" id="teacher.no"/>
	</td>
    <td>姓名<font color=red>*</font></td>
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
		<#list genders as tp>
		<label><input type="radio" name="teacher.gender" value="${tp.id?if_exists?html}" class="rGender"/>${tp.name?if_exists?html}</label>
		</#list>
		<span class="tips">
		<B>状态</B>
			<#list statusList as tp>
		<label><input type="radio" name="teacher.status" value="${tp.id?if_exists?html}" class="rStatus"/>${tp.name?if_exists?html}</label>
		</#list>
		</span>

	</td>
	<td>级别</td>
	<td>
	<#list levels as tp>
		<label><input type="radio" name="teacher.level" value="${tp.id?if_exists?html}" class="rLevel"/>${tp.name?if_exists?html}</label>
		</#list>
	</td>
   </tr><tr>
		<td>电话</td>
		<td><input type="text" name="teacher.phone" maxlength="20" id="teacher.phone"></td>
	
		<td>手机</td>
		<td><input type="text" name="teacher.mobile" maxlength="20" id="teacher.mobile"></td>
	</tr><tr>
		<td>Email</td>
		<td colspan="3"><input type="text" name="teacher.email" maxlength="40" id="teacher.email"></td>
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
		<td colspan="2"><input type="text" name="teacher.identity" maxlength="30" id="teacher.identity" style="width:180px"></td>
	</tr>
	<tr>
		<td>户籍地址</td>
		<td colspan="4"><input type="text" name="teacher.hjAddress" size="60" maxlength="100" id="teacher.hjAddress"></td>
	</tr>
	<tr><td>入职日期</td>
		<td ><input type="text" value="${joinDate?if_exists}" name="teacher.joinTimeText" id="teacher.joinTimeText" maxlength="10" size="12" class="dateField"/>
		<!-- 转正日期<input type="text" value="${joinDate?if_exists}" name="teacher.confirmationTimeText" id="teacher.confirmationTimeText" maxlength="10" size="12" class="dateField"/> -->
		</td><td>离职日期</td><td colspan="2">
		<input type="text" value="${joinDate?if_exists}" name="teacher.leaveTimeText" id="teacher.leaveTimeText" maxlength="10" size="12"  class="dateField"/></td>
	</tr>
	<tr style="background:#fffbec">	
		<td>关联卡号</td>
		<td colspan="4"><div id="sCard"></div></td>
	</tr>	
	<#if updateMode>
	<tr class="title"><td colspan="5">如无需修改密码，下述密码框请留空</td></tr>
	<tr>
		<td>登录密码</td>
		<td><input type="password" name="teacher.password" maxlength="40" id="teacher.password"></td>
	
		<td>密码确认</td>
		<td colspan="2"><input type="password" maxlength="40" id="teacher.password2" > <span class="tips">请再输入一次密码</span></td>
	</tr>
	<#else>
	<tr>
		<td>登录密码<font color=red>*</font></td>
		<td><input type="password" name="teacher.password" maxlength="40" id="teacher.password"></td>
	
		<td>密码确认<font color=red>*</font></td>
		<td colspan="2"><input type="password" maxlength="40" id="teacher.password2" > <span class="tips">请再输入一次密码</span></td>
	</tr>
	</#if>
	<tr>
		<td>备注</td>
		<td colspan="4"><input type="text" name="teacher.comments" id="teacher.comments" size="60" maxlength="100"></td>
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
		<input type="hidden" name="teacher.id" id="teacher.id" value="0"/>
		<input type="hidden" name="teacher.changePassword" id="teacher.changePassword" value="false"/>
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
<script type="text/javascript" src="${base}/static/app/javascript/teacher_create.js"></script>
<script type="text/javascript" src="${base}/static/library/mootools/modules/mbox/mt.mbox.js"></script>
<script type="text/javascript" src="${base}/static/library/puerta/t-simple-no.js"></script>
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
	name:'名称',no:'编号',domain:'<#if updateMode>修改<#else>新增</#if>教师',
	materialNo:'材料编号',
	materialName:'材料名称',space:'区域',picNo:'图号',msgSelectFirst:'请选择...',uploadPic:'上传照片',uploadFile:'上传附件',
	quantity:'数量',lineMsg:"第{index}行:{message}\n",needQty:'需要填写数量',needSpace:'需要选取区域',
	needDetail:"至少需要填写一行明细数据",cancel:'取消',relationShip:'关系',phone:'电话',address:'地址',
	name:'姓名',attachmentItem:'<span class="fileName"><a href="#" data="{fileId}" dataId="{id}" class="download" title="下载">{fileName}</a><a href="#" data="{fileId}" dataId="{id}" class="remove" title="删除"></a></span>',
	noneAttachment:"<span class='gray'>暂无附件</span>"
}







	function check(){
	

		if(isEmpty($F("teacher.no"))){
			alert("请填写编号");
			$("teacher.no").focus();
			return false; 
		}

		if(isEmpty($F("teacher.name"))){
			alert("请填写姓名");
			$("teacher.name").focus();
			return false; 
		}
		

		var len = $$(".rGender:checked").length ;
		if(len==0){
			alert('请选取性别');
			$$(".rGender")[0].focus();
			return false;
		}
		
		len = $$(".rStatus:checked").length ;
		if(len==0){
			alert('请选取状态');
			$$(".rStatus")[0].focus();
			return false;
		}

		len = $$(".rLevel:checked").length ;
		if(len==0){
			alert('请选取级别');
			$$(".rLevel")[0].focus();
			return false;
		}

		var v = $F("teacher.password");
		var v2 = $F("teacher.password2");
		$('teacher.changePassword').value = 'false';
		if(pageVars.updateMode){
			//teacher.changePassword
			if(isEmpty(v) && isEmpty(v2)){
				
			}else if(v!=v2){
				alert("确认密码和密码不一致，请重新输入");
				$('teacher.password2').focus();
				return false;
			}else{
				$('teacher.changePassword').value = 'true';
			}
			
			


		}else{
			if(isEmpty($F("teacher.password"))){
				alert("请填写登录密码");
				$('teacher.password').focus();
				return false;
			}


			if(v!=v2){
				alert("确认密码和密码不一致，请重新输入");
				$('teacher.password2').focus();
				return false;
			}
		}


		len = $F("teacher.comments").length ;
		if(len>200){
			alert("备注信息过长，请填写200字以内信息");
			$("teacher.comments").focus();
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

	var pes = null ;
	window.addEvent('domready',function(){
		pes = new PPage({"id":"pes","pageMode":"pageMode","searchForm":"searchForm","addForm":"addForm"});

	<#if updateMode>
		//set value 
		$('teacher.no').value = "${teacher.no!?js_string}";
		$('teacher.name').value = "${teacher.name!?js_string}";
		$$('.rGender').each ( function(el ) {
			if( el.value == ${teacher.gender} ) {
				el.checked = true ;
			}
		});
		$$('.rStatus').each ( function(el ) {
			if( el.value == ${teacher.status} ) {
				el.checked = true ;
			}
		});
		$$('.rLevel').each ( function(el ) {
			if( el.value == ${teacher.level} ) {
				el.checked = true ;
			}
		});
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
		//$('teacher.barcode').value = "${teacher.barcode!?js_string}";
		pes.edtCard.setValue('${teacher.barcode!?js_string}','${teacher.barcode!?js_string}');
		<#if teacher.joinTime??>$('teacher.joinTimeText').value = "${teacher.joinTime.time?date}";</#if>
		<#if teacher.leaveTime??>$('teacher.leaveTimeText').value = "${teacher.leaveTime.time?date}";</#if>
	
		$('teacher.comments').value = "${teacher.comments!?js_string}";
		$('teacher.id').value = "${teacher.id}";

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
		
		

		var div= new Element("div",{'class':'text l',html:'${teacher.no!?js_string} <#if teacher.updater??>最后修改人:${teacher.updater.loginId!?html}(${teacher.updater.name!?html}) ${teacher.updatedTime.time?datetime}</#if>'});
		var obj = $$("#__top .t-head .t-title");
		div.inject(obj[0],'after');
	<#else>
		pes.mock();
	</#if>
	pes.updateAttachments();

	

});



</script>
</@b.html>