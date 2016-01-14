<#import "/app/macros/commonBase.ftl" as b><@b.html title="activity.title">
<style>
.branchUL{
	list-style:none;
	margin:0px;
	padding:0px;
}

.branchUL li{
	float:left;
	width:200px;
	height:20px;
	line-height:20px;
	overflow:hidden;
}

</style>

  <div class="top" id="__top"> </div>
<div class="body">
    <!-- condition -->
    <div id="conditionDiv" class="h forList">
	<form action="${uri}" method="get" id="searchForm">
	<table border="0" class="grid" width="800" id="queryForm"><colgroup>
	<col style="width:100px"/>
	<col /><col style="width:100px"/>
	<col /><col style="width:100px"/>
	<col />
	</colgroup>
          <tr class="title">
            <td colspan="6">查询条件</td></tr>
		 <tr> 
		<td>编号</td>
		<td>
		<input type="text" name="condition.no" value="${condition.no?if_exists?html}"/>
		</td> <td>名称</td>
		<td>
		<input type="text" name="condition.name" value="${condition.name?if_exists?html}"/>
		</td>
		<td>分支机构</td>
		<td>
		<select name="condition.branchId" id="condition.branchId">
		<option value="0">(全部)</option>
		<#list branches as tp>
		<option value="${tp.id?if_exists?html}">${tp.no?if_exists?html} ${tp.name?if_exists?html}</option>
		</#list>
		</select>
		</td>
	  </tr>
	  <tr> 
		<td colspan="6">
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
		<td ><input type="text" name="activity.no" id="form.no" size="20" maxlength="40"/> </td>
	  
		<td >名称<span class="redStar">*</span></td>
		<td ><input type="text" name="activity.name" id="form.name" size="20" maxlength="40"/> 
		
		
		</td>
	  </tr>
	  <tr>
		<td >合作单位</td>
		<td  colspan="3"><input type="text" name="activity.partner" id="form.partner" size="70" maxlength="100"/> </td>
	  </tr>
	  <tr> 
		<td >开始日期</td>
		<td ><input type="text" name="activity.beginDateText" id="form.beginDate" size="10" maxlength="10" class="dateField form-to-date"/> </td>
	  
		<td >结束日期</td>
		<td ><input type="text" name="activity.endDateText" id="form.endDate" size="10" maxlength="10" class="dateField form-to-date"/> </td>
	  </tr> <tr> 
		<td >备注</td>
		<td colspan="3"><input type="text" name="activity.comments" id="form.comments" size="70" maxlength="100"/> </td></tr>
		<tr class="title"> 
            <td colspan="4">相关分支机构</td>
		</tr>
		<tr>
			<td colspan="4">
			<ul class="branchUL">
				<#list branches as br>
				<li><label><input type="checkbox" name="activity.branchId" value="${br.id}" class="chkBranchId"/> ${br.no!?html} ${br.name!?html}</label></li>
				</#list>
			</ul>

</td>

	  </tr>
	  <tr> 
		<td colspan="4">
		<input type="submit" value="<@b.text key="submitBtn"/>" id="submitBtn"/>
		<input type="hidden" name="activity.id" id="form.id"/>		
		</td>
	  </tr> 
	</table>
	</form>
	</div>
  </div>
<!-- others -->
<#include "/calendar.ftl"/>
<link rel="stylesheet" type="text/css" href="${base}/static/app/css/t-style.css"></link>
<script type="text/javascript" src="${base}/static/library/puerta/t-table.js"></script> 
<script type="text/javascript" src="${base}/static/app/javascript/activity.js"></script>
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
	name:'名称',no:'编号',domain:'市场活动管理',linkman:'联系人',phone:'电话',email:'Email',status:'状态',prefix:'学号前缀'
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
		var pass = true ;
		var target = null ;
		$$(".dateField").each ( function(el){
			var val = el.value ;
			if( pass && !isEmpty( val ) && !isDate( val )){
				pass = false;
				target = el ;
			}
		});

		if(!pass){
			alert('日期格式错误，请重新填写');
			target.focus();
			return false;
		}

		var bd = $F("form.beginDate");
		var ed = $F("form.endDate");

		var days = betweenDays(bd,ed);
		if(days>0){
			
			alert("开始日期大于结束日期，请修改");
			$("form.endDate").focus();
			return false; 
		}
		
		return true ;
	}
</script>
</@b.html>