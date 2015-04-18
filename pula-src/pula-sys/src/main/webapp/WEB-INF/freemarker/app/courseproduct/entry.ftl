<#import "/app/macros/commonBase.ftl" as b><@b.html title="courseProduct.title">

<style>
#workerList A:hover{
	text-decoration:line-through;
}
</style>
  <div class="top" id="__top"> </div>
<div class="body">
    <!-- condition -->
   <div id="conditionDiv" class="h forList">
	<form action="${uri}" method="get" id="searchForm">
	<table border="0" class="grid" width="800" id="queryForm"><colgroup>
	<col style="width:60px"/>
	<col /><col style="width:60px"/>
	<col /><col style="width:80px"/>
	<col />
	</colgroup>
          <tr class="title">
            <td colspan="6">查询条件</td></tr>
		 <tr> 
		<td>编号</td>
		<td>
		<input type="text" name="condition.no" value="${condition.no?if_exists?html}"/>
		</td>
		<td>名称</td>
		<td>
		<input type="text" name="condition.name" value="${condition.name?if_exists?html}"/>
		</td>
		<td>分支机构</td>
		<td>
		<select name="condition.branchId" id="condition.branchId">
		<option value="0">(全部)</option>
		<#list branches as tp>
		<option value="${tp.id?if_exists?html}">${tp.no!?html} ${tp.name?if_exists?html}</option>
		</#list>
		</select>
		</td>
	  </tr>
	  <tr> 
		<td colspan="6">
	<input type="submit" value="查询" id="searchBtn"/>

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
	<div class="l"><table border="0" class="grid" width="700">
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
		<td ><input type="text" name="courseProduct.no" id="courseProduct.no" size="20" maxlength="40"/> </td>
	  
		<td >名称<span class="redStar">*</span></td>
		<td ><input type="text" name="courseProduct.name" id="courseProduct.name" size="20" maxlength="40"/> </td>
	  </tr>
	<tr>
		<td >课时数</td>
		<td ><input type="text" name="courseProduct.courseCount" id="courseProduct.courseCount" size="10" maxlength="40" class="numberEdit" value="0"/> </td>
		<td >价格</td>
		<td ><input type="text" name="courseProduct.price" id="courseProduct.price" size="10" maxlength="40" class="numberEdit" value="0"/> </td>
		</tr>
	  <tr> 
		<td >开始日期</td>
		<td ><input type="text" name="courseProduct.beginTimeText" id="courseProduct.beginTimeText" size="12" maxlength="10" class="dateField"/>
		</td>
	  
		<td >结束日期</td>
		<td ><input type="text" name="courseProduct.endTimeText" id="courseProduct.endTimeText" size="12" maxlength="10" class="dateField"/></td>
	  </tr><tr> 
		<td >分支机构<span class="redStar">*</span></td>
		<td colspan="3" ><select name="courseProduct.branchId" id="courseProduct.branchId">
			<option value="">请选取...</option>
			<#list branches as branch>
			<option value="${branch.id}">${branch.no!?html} ${branch.name!?html}</option>
			</#list>
			</select>
		</td>	 
	  </tr>
	   <tr> 
		<td >备注</td>
		<td colspan="3"><input type="text" name="courseProduct.comments" id="courseProduct.comments" size="60" maxlength="80"/> </td>
	  </tr> 
	  <tr> 
		<td colspan="4">
		<input type="submit" value="<@b.text key="submitBtn"/>" id="submitBtn"/>
		<input type="hidden" name="courseProduct.id" id="courseProduct.id"/>
		</td>
	  </tr> 
	</table>

</div>
	
	</form>
	</div>
  </div>
<!-- others -->
<#include "/calendar.ftl"/>
<link rel="stylesheet" type="text/css" href="${base}/static/app/css/t-style.css"></link>
<script type="text/javascript" src="${base}/static/library/puerta/t-table.js"></script> 
<script type="text/javascript" src="${base}/static/app/javascript/courseproduct.js"></script>
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
	name:'名称',no:'编号',domain:'课程产品',beginTime:'开始日期',endTime:'结束日期',courseCount:'课时数',status:'状态',price:'价格',branchName:'分支机构'
}


	function check(){
		
		if(isEmpty($F("courseProduct.no"))){
			alert("请填写编号");
			$("courseProduct.no").focus();
			return false; 
		}
		if(isEmpty($F("courseProduct.name"))){
			alert("请填写名称");
			$("courseProduct.name").focus();
			return false; 
		}
		
		if(isEmpty($F("courseProduct.branchId"))){
			alert("请选取分支机构");
			$("courseProduct.branchId").focus();
			return false;
		}
		var b = true;
		var eel = null ;
		$$(".numberEdit").each ( function (el){
			var v = Number.from( el.value ) ;
			if( b && !v && v!=0  ) {
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

		len = $F("courseProduct.comments").length ;
		if(len>200){
			alert("备注信息过长，请填写200字以内信息");
			$("courseProduct.comments").focus();
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
</script>
</@b.html>