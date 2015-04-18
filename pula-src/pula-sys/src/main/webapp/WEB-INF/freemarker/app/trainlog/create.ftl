<#import "/app/macros/commonBase.ftl" as b><@b.html title="学生">
<style>
.top .text{
	line-height:25px;
	height:25px;
	margin-top:3px;
	color:#5f5f5f;
}
.even{
	background:#f7ffff;
}

</style>
  <div class="top" id="__top"> </div>
<div class="body">
    <!-- condition -->
	<!-- edit form -->
	
	<form action="${uri}" method="post" id="addForm">
	<table border="0" class="grid" width="800">
<colgroup>
        <col style="width:80px;"><col>
        <col style="width:80px;"><col >
</colgroup>
  <tr class="title"> 
    <td colspan="4">请填写下列信息</td>
  </tr>
    <tr> 
    <td>培训日期<font color=red>*</font></td>
    <td >
	<input type="text" name="trainLog.trainDateText" id="trainLog.trainDateText" maxlength="10" class="dateField" value="${today!?html}"/>
	</td>
	<td>培训地点<font color=red>*</font></td>
    <td ><input type="text" name="trainLog.location" maxlength="50" id="trainLog.location" style="width:230px"/></td>
	</tr>
   <tr>
	<td>培训内容<font color=red>*</font></td>
	<td><input type="text" name="trainLog.content" maxlength="50" id="trainLog.content" style="width:230px"/></td>
	<td>培训师<font color=red>*</font></td>
	<td><input type="text" name="trainLog.trainer" maxlength="20" id="trainLog.trainer" style="width:230px"/></td>

		
	</tr>
	
	<tr>
		<td>备注</td>
		<td colspan="3"><input type="text" name="trainLog.comments" id="trainLog.comments" size="60" maxlength="100"></td>
	</tr>
	<tr class="title">
	<td colspan="4">教师评分情况</td>
	</tr>
	<tr>
	<td colspan="4">
		<table width="300">
		<colgroup>
        <col style="width:140px;"><col>        
		</colgroup>
		<#list teachers as te>
		<tr <#if te_index%2==0>class="even"</#if>>
		<td>${te.no!?html} ${te.name!?html}</td>
		<td><input type="text" maxlength="4" style="width:100px" class="teacher_points numberEdit" data="${te.id!?html}" value="<#if items??&&items[te.id?string]??>${items[te.id?string].points}<#else>0</#if>"/></td>
		</tr>
		</#list>
		</table>
	</td>
	</tr>
	  <tr> 
		<td colspan="4">
		
		<div class="l">
			<input type="button" value="保存" id="saveBtn" accesskey="s"/>
		<input type="submit" value="<@b.text key="submitBtn"/>" id="submitBtn" class="h"/>
		<input type="hidden" name="json" id="json" value="[]" />
		<input type="hidden" name="trainLog.id" id="trainLog.id" value="0"/>
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
<script type="text/javascript" src="${base}/static/app/javascript/trainlog_create.js"></script>
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
	name:'名称',no:'编号',domain:'<#if updateMode>修改<#else>新增</#if>培训记录',
	materialNo:'材料编号',
	materialName:'材料名称',space:'区域',picNo:'图号',msgSelectFirst:'请选择...',uploadPic:'上传照片',uploadFile:'上传附件',
	quantity:'数量',lineMsg:"第{index}行:{message}\n",needQty:'需要填写数量',needSpace:'需要选取区域',
	needDetail:"至少需要填写一行明细数据",cancel:'取消',relationShip:'关系',phone:'电话',address:'地址',
	name:'姓名',attachmentItem:'<span class="fileName"><a href="#" data="{fileId}" dataId="{id}" class="download" title="下载">{fileName}</a><a href="#" data="{fileId}" dataId="{id}" class="remove" title="删除"></a></span>',
	noneAttachment:"<span class='gray'>暂无附件</span>"
}







	function check(){
	

		
		if(!isDate($F("trainLog.trainDateText"))){
			alert("请正确填写培训日期");
			$("trainLog.trainDateText").focus();
			return false; 
		}

		
		if(isEmpty($F("trainLog.location"))){
			alert("请填写培训地点");
			$("trainLog.location").focus();
			return false; 
		}
		
		
		if(isEmpty($F("trainLog.content"))){
			alert("请填写培训内容");
			$("trainLog.content").focus();
			return false; 
		}
		if(isEmpty($F("trainLog.trainer"))){
			alert("请填写培训师");
			$("trainLog.trainer").focus();
			return false; 
		}

		len = $F("trainLog.comments").length ;
		if(len>200){
			alert("备注信息过长，请填写200字以内信息");
			$("trainLog.comments").focus();
			return false;
		}


		var b = true;
		var eel = null ;

		$$(".numberEdit").each ( function (el){
			var v = Number.from( el.value ) ;
			if( b && !v && v!=0 && v<=0  ) {
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

		

		
		return true ;
	}

	var pes = null ;
	window.addEvent('domready',function(){
		pes = new PPage({"id":"pes","pageMode":"pageMode","searchForm":"searchForm","addForm":"addForm"});

	<#if updateMode>
		//set value 
		
		
		<#if trainLog.trainDate??>$('trainLog.trainDateText').value = "${trainLog.trainDate.time?date}";</#if>

		$('trainLog.location').value = "${trainLog.location!?js_string}";
		$('trainLog.content').value = "${trainLog.content!?js_string}";
		$('trainLog.trainer').value = "${trainLog.trainer!?js_string}";
		$('trainLog.comments').value = "${trainLog.comments!?js_string}";
		$('trainLog.id').value = "${trainLog.id}";
		
		

		

		var div= new Element("div",{'class':'text l',html:'${trainLog.id!?js_string} <#if trainLog.updater??>最后修改人:${trainLog.updater.loginId!?html}(${trainLog.updater.name!?html}) ${trainLog.updatedTime.time?datetime}</#if>'});
		var obj = $$("#__top .t-head .t-title");
		div.inject(obj[0],'after');
	<#else>
		pes.mock();
	</#if>

	

});



</script>
</@b.html>