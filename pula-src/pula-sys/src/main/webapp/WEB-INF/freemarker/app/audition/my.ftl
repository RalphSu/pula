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
	
	<table border="0" class="grid">
	


		
		<tr>
		<td><div id="detailTable"></div></td>
		</tr><tr> 
		<td >
		
		<div class="l">
			<input type="submit" value="保存" id="saveBtn" accesskey="s"/>
		<input type="hidden" name="json" id="jsonDetail" />
		</div>
		<div class="r">
			ALT+S 保存
		</div>
		
		</td>
	  </tr> 
	</table></form>
</div>	

<!-- others -->
<#include "/calendar.ftl"/>
<link rel="stylesheet" type="text/css" href="${base}/static/app/css/t-style.css"></link>
<script type="text/javascript" src="${base}/static/library/puerta/t-table.js"></script> 
<script type="text/javascript" src="${base}/static/app/javascript/audition_my.js"></script>
<script type="text/javascript" src="${base}/static/library/mootools/modules/mbox/mt.mbox.js"></script>
<script type="text/javascript" src="${base}/static/library/puerta/t-simple-no.js"></script>
<script type="text/javascript" src="${base}/static/library/puerta/t-datagrid.js"></script>
<script type="text/javascript" src="${base}/static/library/mootools/modules/UUID.js"></script>
<script type="text/javascript">

var pageVars = {
	queryString:'',
	action : '_update',
	id :'',
	base :'${base}',
	imagePath:'${base}/static/laputa/images/icons/',
	noPhoto:'${base}/static/app/images/nophoto.jpg'
}

var lang = {
	name:'名称',no:'编号',domain:'试听登记',
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

		pageVars.results = [
			{"value":"","text":"尚未结束"}
			<#list categories as cate>,{"value":"${cate.id!?js_string}","text":"${cate.name!?js_string}"}</#list>
		];


	pes = new PPage({"id":"pes","pageMode":"pageMode","searchForm":"searchForm","addForm":"addForm"});

	
		var fillItems = function(){
			var items = ${items_json!} ;
			pes.grid.fill(items);
			//pes.grid.fixLine();
		};


		
		fillItems();

		pes.grid.addRow();

	

});



</script>
</@b.html>