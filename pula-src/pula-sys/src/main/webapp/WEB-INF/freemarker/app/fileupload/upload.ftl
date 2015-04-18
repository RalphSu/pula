<#import "/app/macros/commonBase.ftl" as b><@b.html title="文件上传">
<style>
.unSelected{-moz-user-select:none;hutia:expression(this.onselectstart=function(){return(false)});}
#pathInfo{
	height:20px;
	background-color:#ecfbff;
	line-height:20px;
	border:1px solid #ccc;
	padding:0px 0px 0px 10px;
	overflow:hidden;
}
body,html{
	overflow:hidden;
}
</style>
<div class="top h " id="__top"></div>
<div class="body">
	<!-- condition -->
	<div id="newFilePanel" style="width:540px;overflow:hidden;">
		<form method="post" id="uploadForm" enctype="multipart/form-data">
		<table border="0" class="grid" width="100%">
		<colgroup><col style="width:60px"/>
		<col />
		</colgroup>
		 
		 <tr> 
			<td >文件<b><font color="red" id="picTips">*</font></b></td>
			<td ><input type="file" name="imgFile" id="picFile" size="50" /></td>
		  </tr> 
		  <tr> 
			<td colspan="2">
			<input type="submit" value="<@b.text key="submitBtn"/>" id="uploadBtn"/>
			<input type="hidden" name="type" value="${type!("0")}" />
			</td>
		  </tr> 
		</table>
		</form>
	</div>


</div>


<!-- others -->
<link rel="stylesheet" type="text/css" href="${base}/static/app/css/t-style.css"></link>
<script type="text/javascript" src="${base}/static/library/puerta/t-table.js"></script> 
<script type="text/javascript" src="${base}/static/library/mootools/modules/mbox/mt.mbox.js"></script>
<script type="text/javascript" src="${base}/static/library/puerta/simpleeditor.js"></script>
<script type="text/javascript" src="${base}/static/laputa/m/ajax-upload.js"></script> 
<script type="text/javascript" src="${base}/static/library/swf/swfobject_source.js"></script> 
<script type="text/javascript">

var pageVars = {
	queryString:'',
	action : '_create',
	id :'',
	base :'${base}'
}

var lang = {
	name:'名称',
	typeName:'类型',
	newFile:'上传文件',
	newFolder:'创建目录',
	remove:'删除选中',
	rename:'更改名称',
	refresh:'刷新',
	back:'返回',
	rootDir:'根目录',
	selectOneItem:'请单击左侧选择一项文件或文件夹',domain:'文件管理',emptyURL:'空白的地址，请填写http://'

}

PA.on( function(){
	function uploadFormShow(){

			 var json = JSON.decode(this.responseText);
			 $('uploadBtn').disabled = false ;
			 $('uploadForm').reset();
			// alert(this.responseText);
			 if(json.error){
				alert(json.message);
			}else{
				window.parent.executeSelect(json.filePath,json.fileName);				
			}
		}

	$('uploadForm').addEvent('submit',function(e){
			$('uploadBtn').disabled = true ;
			
			e.stop();
			$('uploadForm').action = '_upload?_json=1';
			(new AjaxUpload($('uploadForm'))).upload().addEvent('onFinish',uploadFormShow);
	});

	//$('picFile').fireEvent('dblclick');
	//$('picFile').click();
	//alert('hi');
});

</script>
</@b.html>