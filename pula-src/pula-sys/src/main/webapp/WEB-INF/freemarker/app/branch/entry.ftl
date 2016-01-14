<#import "/app/macros/commonBase.ftl" as b><@b.html title="branch.title">
<style>
.headquarter{ background:red;color:white;padding:2px}
</style>

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
	<table border="0" class="grid" width="700">
	<colgroup><col style="width:100px"/>
	<col /><col style="width:100px"/>
	<col />
	</colgroup>
	  <tr class="title"> 
            <td colspan="4"><div class="l">请填写下列信息 <span id="pageMode" style="color:blue"></span></div>
              <div class="r">
              <div class="backToList " id="backToList"><A href="javascript:pes.backToList()">返回至列表</A></div></td>
	  </tr> <tr> 
		<td >编号<span class="redStar">*</span></td>
		<td ><input type="text" name="branch.no" id="form.no" size="20" maxlength="40"/> <span class="headquarter" id="spnHeadquarter">总部</span></td>
	  
		<td >名称<span class="redStar">*</span></td>
		<td ><input type="text" name="branch.name" id="form.name" size="20" maxlength="40"/> 
		
		
		</td>
	  </tr> <tr> 
		<td >学号前缀</td>
		<td ><input type="text" name="branch.prefix" id="form.prefix" size="4" maxlength="4"/> <span class="tips">四位字符</span></td>
	  
	
		<td>属性</td><td ><label><input type="checkbox" name="branch.showInWeb" id="showInWeb" value="true"/>显示在网站中</label>
	  
		
	  </td></tr>
	  <tr>
		<td >地址</td>
		<td  colspan="3"><input type="text" name="branch.address" id="form.address" size="70" maxlength="100"/> </td>
	  </tr>
	  <tr> 
		<td >联系人</td>
		<td ><input type="text" name="branch.linkman" id="form.linkman" size="20" maxlength="20"/> </td>
	  
		<td >电话</td>
		<td ><input type="text" name="branch.phone" id="form.phone" size="20" maxlength="40"/> </td>
	  </tr> <tr> 
		<td >传真</td>
		<td ><input type="text" name="branch.fax" id="form.fax" size="20" maxlength="40"/> </td>
	  
		<td >Email</td>
		<td ><input type="text" name="branch.email" id="form.email" size="20" maxlength="40"/> </td>
	  </tr> <tr> 
		<td >备注</td>
		<td colspan="3"><input type="text" name="branch.comments" id="form.comments" size="70" maxlength="100"/> </td></tr>
	  <tr> 
		<td colspan="4">
		<input type="submit" value="<@b.text key="submitBtn"/>" id="submitBtn"/>
		<input type="hidden" name="branch.id" id="form.id"/>		
		</td>
	  </tr> 
	</table>
	</form>
	</div>
  </div>
<!-- others -->
<link rel="stylesheet" type="text/css" href="${base}/static/app/css/t-style.css"></link>
<script type="text/javascript" src="${base}/static/library/puerta/t-table.js"></script> 
<script type="text/javascript" src="${base}/static/app/javascript/branch.js"></script>
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
	name:'名称',no:'编号',domain:'分支机构',linkman:'联系人',phone:'电话',email:'Email',status:'状态',prefix:'学号前缀'
}


	function check(){
		
		if(isEmpty($F("form.no"))){
			alert("请填写编号");
			$("form.no").focus();
			return false; 
		}
		if(isEmpty($F("form.name"))){
			alert("请填写名称");
			$("form.name").focus();
			return false; 
		}
		
		return true ;
	}
</script>
</@b.html>