<#import "/app/macros/commonBase.ftl" as b> <@b.html title="学生作品">
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
	background: url("${base}/static/app/images/remove.png") no-repeat right
		top;
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
</style>

<div class="top" id="__top"></div>
<div class="body">
	<!-- condition -->
	<!-- edit form -->

	<!-- edit form -->
	<!-- <div id="inputPanel" class="l h"> -->
	<form action="${uri}" method="post" id="addForm">
		<table border="1" class="grid" width="700">
			<colgroup>
				<col style="width: 100px" />
				<col style="width: 100px" />
				<col />
			</colgroup>
			<tr>
				<td>编号<span class="redStar">*</span></td>
				<td colspan="3"><input type="text" name="notice.no"
					id="notice.no" size="20" maxlength="40" /></td>
			</tr>
			<tr>
				<td>标题<span class="redStar">*</span></td>
				<td colspan="3"><input type="text" name="notice.title"
					id="notice.title" size="60" maxlength="40" /></td>
			</tr>
			<tr>
				<td>格式化标题<span class="redStar"></span></td>
				<td colspan="3"><input type="text" name="notice.formattedTitle"
					id="notice.formattedTitle" size="60" maxlength="40" /></td>
			</tr>

			<tr>
				<td>内容</td>
				<td colspan="3"><textarea name="notice.content"
						id="notice.content" cols="50" rows="10"></textarea></td>
			</tr>

			<tr>
				<td>价格</td>
				<td colspan="1"><input type="text" name="notice.noticePrice"
					id="notice.noticePrice" size="12" class="numberEdit"> </textarea></td>
			</tr>

			<tr>
				<td>备注</td>
				<td colspan="3"><input type="text" name="notice.comment"
					id="notice.comment" size="60" maxlength="80" /></td>
			</tr>

			<tr>
				<td>作品:<span class="redStar">*</span></td>
				<td colspan="3">点击下面图标上传</td>
			</tr>
			<tr>
				<td colspan="4" valign="attop" class="photo"><A href="#"
					id="uploadPic"><img src="${base}/static/app/images/nophoto.jpg" /></A>
					<input type="button" value="新窗口打开" id="openInNew" /></td>
			</tr>
			<tr>
				<td colspan="4">
					<DIV ID="attachmentDiv"></div>
				</td>
			</tr>
			<tr>
				<td colspan="4">
				<input type="button" value="保存" id="saveBtn" accesskey="s" />
				<input type="submit" value="<@b.text key=" submitBtn "/>" id="submitBtn" class="h" /> 
				<input type="hidden" name="notice.id" id="notice.id" /> 
				<input type="hidden" name="jsonAttachment" id="jsonAttachment" value="[]" />
				<div class="r">ALT+S 保存</div></td>
			</tr>
		</table>
	</form>
	<!-- </div> -->
</div>

<!-- others -->
<#include "/calendar.ftl"/>
<link rel="stylesheet" type="text/css"
	href="${base}/static/app/css/t-style.css"></link>
<script type="text/javascript"
	src="${base}/static/library/puerta/t-table.js"></script>
<script type="text/javascript"
	src="${base}/static/app/javascript/notice_create.js"></script>
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
	name:'名称',no:'编号',domain:'<#if updateMode>修改<#else>新增</#if>活动',
	materialNo:'材料编号',
	materialName:'材料名称',space:'区域',picNo:'图号',msgSelectFirst:'请选择...',uploadPic:'上传照片',uploadFile:'上传附件',
	quantity:'数量',lineMsg:"第{index}行:{message}\n",needQty:'需要填写数量',needSpace:'需要选取区域',
	needDetail:"至少需要填写一行明细数据",cancel:'取消',relationShip:'关系',phone:'电话',address:'地址',
	name:'姓名',attachmentItem:'<span class="fileName"><a href="#" data="{fileId}" dataId="{id}" class="download" title="下载">{fileName}</a><a href="#" data="{fileId}" dataId="{id}" class="remove" title="删除"></a></span>',
	noneAttachment:"<span class='gray'>暂无附件</span>"
}

	function check(){

		len = $F("notice.comment").length ;
		if(len>200){
			alert("备注信息过长，请填写200字以内信息");
			$("notice.comment").focus();
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
		$('notice.no').value = "${notice.no!?js_string}";
		$('notice.title').value = "${notice.title!?js_string}";
		$('notice.formattedTitle').value = "${notice.formattedTitle!?js_string}";
		$('notice.content').value = "${notice.content!?js_string}";
		$('notice.noticePrice').value = "${notice.noticePrice!?js_string}";
		$('notice.comment').value = "${notice.comment!?js_string}";
		$('notice.id').value = "${notice.id}";

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
	<#else>
		pes.mock();
	</#if>
	pes.updateAttachments();
});



</script>
</@b.html>
