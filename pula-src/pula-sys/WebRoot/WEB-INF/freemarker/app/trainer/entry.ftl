<#import "/app/macros/commonBase.ftl" as b><@b.html title="trainer.title">
  <div class="top" id="__top"> </div>
<div class="body">
    <!-- condition -->
    <div id="conditionDiv" class="h forList">
	<form action="${uri}" method="get" id="searchForm">
	<table border="0" class="grid" width="600" id="queryForm"><colgroup>
	<col style="width:100px"/>
	<col /><col style="width:100px"/>
	<col />
	</colgroup>
          <tr class="title">
            <td colspan="4">查询条件</td></tr>
		 <tr> 
		<td>编号</td>
		<td>
		<input type="text" name="condition.no" value="${condition.no?if_exists?html}"/>
		</td> <td>名称</td>
		<td>
		<input type="text" name="condition.name" value="${condition.name?if_exists?html}"/>
		</td>
	  </tr>
	  <tr> 
		<td colspan="4">
		<input type="submit" value="查询" id="searchBtn"/>
		<input type="reset" value="重填" id="resetBtn"/>
		</td>
	  </tr> 
	</table>
	 </form>
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
	<div class="l">
	<table border="0" class="grid" width="400">
	<colgroup><col style="width:100px"/>
	<col />
	</colgroup>
	  <tr class="title"> 
            <td colspan="2"><div class="l">请填写下列信息 <span id="pageMode" style="color:blue"></span></div>
              <div class="r">
              <div class="backToList " id="backToList"><A href="javascript:pes.backToList()">返回至列表</A></div></td>
	  </tr> <tr> 
		<td >编号<span class="redStar">*</span></td>
		<td ><input type="text" name="trainer.no" id="trainer.no" size="20" maxlength="40"/> </td>
	   </tr>  <tr> 
		<td >名称<span class="redStar">*</span></td>
		<td ><input type="text" name="trainer.name" id="trainer.name" size="20" maxlength="40"/> </td>
	  </tr> 
	  <tr> 
		<td colspan="2">
		<input type="submit" value="<@b.text key="submitBtn"/>" id="submitBtn"/>
		<input type="hidden" name="trainer.id" id="trainer.id"/>		
		</td>
	  </tr> 
	</table>
	</div>
	
	</form>
  </div>
<!-- others -->
<link rel="stylesheet" type="text/css" href="${base}/static/app/css/t-style.css"></link>
<script type="text/javascript" src="${base}/static/library/puerta/t-table.js"></script> 
<script type="text/javascript" src="${base}/static/app/javascript/trainer.js"></script>
<script type="text/javascript" src="${base}/static/library/mootools/modules/mbox/mt.mbox.js"></script>
<script type="text/javascript" src="${base}/static/library/puerta/t-simple-no.js"></script>
<script type="text/javascript">

var pageVars = {
	queryString:'',
	action : '_create',
	id :'',
	base :'${base}'
}

var lang = {
	name:'名称',no:'编号',domain:'培训师',status:'状态',noneUser:'暂未设定用户',
	msgInputUserNo:"请填写用户账号，或输入姓名按回车自动加载"
}


	function check(){
		
		if(isEmpty($F("trainer.no"))){
			alert("请填写编号");
			$("trainer.no").focus();
			return false; 
		}
		if(!isKeyStr($F("trainer.no"))){
			alert("请正确填写编号，数字和英文组合");
			$("trainer.no").focus();
			return false; 
		}
		if(isEmpty($F("trainer.name"))){
			alert("请填写名称");
			$("trainer.name").focus();
			return false; 
		}
		
		return true ;
	}
</script>
</@b.html>