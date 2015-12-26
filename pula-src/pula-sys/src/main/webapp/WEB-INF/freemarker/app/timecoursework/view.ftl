<#import "/app/macros/commonBase.ftl" as b><@b.html title="学生作品">
<style>
.top .text {
	line-height: 25px;
	height: 25px;
	margin-top: 3px;
	color: #5f5f5f;
}

.photo {
	width: 140px;
}

.photo a img {
	border: 1px solid #ccc;
	margin: 0px auto;
	width: 120px;
	height: 120px;
	display: block;
}

#attachmentDiv span.fileName {
	border: 1px solid #ccc;
	background: #fafafa;
	padding: 0px 16px 0px 4px;
	display: inline-block;
	height: 20px;
	line-height: 20px;
	position: relative;
	margin-right: 10px;
}

#attachmentDiv a.remove {
	display: block;
	position: absolute;
	width: 16px;
	height: 16px;
	/*background:url("${base}/static/app/images/remove.png") no-repeat right top;*/
	cursor: pointer;
	right: 0px;
	top: 0px;
}

#attachmentDiv span.gray {
	color: #ccc;
}

#uploadFile {
	width: 80px;
	height: 20px;
	cursor: pointer;
}

.body input {
	border: 0px;
}
</style>
<div class="body">
	<!-- condition -->
	<!-- edit form -->

	<table border="0" class="grid" width="800">
		<colgroup>
			<col style="width: 80px;"></col>
			<col></col>
			<col style="width: 80px;"></col>
			<col></col>
		</colgroup>
		<tr class="title">
			<td colspan="4">学生作品</td>
		</tr>

		<tr>
			<!-- <td>编号</td>
					<td><input type="hiddern" name="work.no" id="work.no"
						size="20" maxlength="40" /></td> -->

			<td>课程编号<span class="redStar">*</span></td>
			<td>
				<!-- <input type="text" name="work.courseNo" id="work.courseNo"
						size="20" maxlength="40" /> --> <select name="work.courseNo"
				id="work.courseNo"> <#list courses as tp>
					<option value="${tp.no?if_exists?html}">${tp.name?if_exists?html}</option>
					</#list>
			</select>
			</td>

			<td>学号<span class="redStar">*</span></td>
			<td><input type="text" name="work.studentNo"
				id="work.studentNo" size="20" maxlength="40" /></td>
		</tr>

		<tr>
			<td>作品时间<span class="redStar">*</span></td>
			<td><input type="text" name="work.workEffectDate"
				id="work.workEffectDate" size="20" maxlength="40"
				class="dateField" /></td>
			<td colspan="2"></td>
		</tr>

		<tr>
			<td>注释</td>
			<td><input type="text" name="work.comments"
				id="work.comments" size="20" maxlength="80" /></td>
			<td colspan="2"></td>
		</tr>
		
		<tr>
            <td>评分</td>
            <td><input type="text" name="work.rate"
                id="work.rate" size="20" maxlength="80" /></td>
            <td colspan="2">
                <select id="work.select.rate" class="rating">
                    <option value="1">Did not like</option>
                    <option value="2">Ok</option>
                    <option value="3">Liked</option>
                    <option value="4">Loved!</option>
                    <option value="5">Loved!</option>
                </select>
            </td>
        </tr>

		<tr>
			<td>作品:<span class="redStar">*</span></td>
			<td colspan="3"><input type="text" name="work.imagePath"
				id="work.imagePath" size="60" maxlength="120" disabled /></td>
		</tr>
		<tr>
			<td colspan="4"><A href="#" id="uploadPic"><img
					src="${base}/static/app/images/nophoto.jpg" /></A></td>
		</tr>

	<!-- 	<tr>
			<td colspan="4"><input type="submit" value="<@b.text key=" submitBtn "/>"
				id="submitBtn"/> <input type="hidden" name="work.id"
				id="work.id" /></td>
		</tr> -->

		<tr class="title">
			<td colspan="5">附件 数量:<span id="spanQty"></span></td>
		</tr>
		<tr>
			<td colspan="5">

				<DIV ID="attachmentDiv"></div>
			</td>
		</tr>

	</table>


	<!-- others -->
	<#include "/calendar.ftl"/>
	<link rel="stylesheet" type="text/css"
		href="${base}/static/app/css/t-style.css"></link>
	<script type="text/javascript"
		src="${base}/static/library/puerta/t-table.js"></script>
	<script type="text/javascript"
		src="${base}/static/app/javascript/timecoursework_view.js"></script>
	<script type="text/javascript"
		src="${base}/static/library/mootools/modules/mbox/mt.mbox.js"></script>
	<script type="text/javascript"
		src="${base}/static/library/puerta/t-simple-no.js"></script>
	<script type="text/javascript"
		src="${base}/static/library/puerta/t-datagrid.js"></script>
	<script type="text/javascript"
		src="${base}/static/library/mootools/modules/UUID.js"></script>
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
        $(".rating").rating();
		pes = new PPage({"id":"pes","pageMode":"pageMode","searchForm":"searchForm","addForm":"addForm"});

		//set value
		$('work.studentNo').value = "${work.studentNo!?js_string}";
		$('work.courseNo').value = "${work.courseNo!?js_string}";
		$('work.imagePath').value = "${work.imagePath!?js_string}";
		$('work.comments').value = "${work.comments!?js_string}";
		$('work.workEffectDate').value = "${work.workEffectDate!?js_string}";
        $('work.select.rate').value="${work.rate !? js_string}";

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