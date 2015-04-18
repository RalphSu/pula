<#import "/app/macros/commonBase.ftl" as b><@b.html title="shop.title">
<#include "/calendar.ftl"/>
<div class="top" id="__top"> </div>
<div class="body">
    <!-- condition -->
    <div id="conditionDiv" class="h forList">
	<form action="${uri}" method="get" id="searchForm">
	<table border="0" class="grid" width="800" id="queryForm"><colgroup>
	<col style="width:100px"/>
	<col />	<col style="width:100px"/>
	<col />
	</colgroup>
            <tr class="title">
            <td colspan="4">查询条件</td></tr>
		 <tr> 
		<td>学生编号</td>
		<td>
		<div id="conditionUserNo"></div>
		</td>
		<td>类型</td>
		<td><select name="condition.type" id="conditionType"><option value="0">全部</option>
			<#list typeList as col>
			<option value="${col.id?if_exists?html}" <#if col.id==condition.type?string>selected</#if>>${col.name?if_exists?html}</option>
			</#list>
		</select>
		</td></tr><tr>
		<td>日期</td>
	 <td><input type="text" value="${condition.beginDate?if_exists?html}" name="condition.beginDate" id="beginDate" maxlength="10" size="12"/>
		 <img src='${base}/static/laputa/images/icons/arrow_blue.gif' border=0 align="absmiddle">
		 <input type="text" value="${(condition.endDate?if_exists?html)}" name="condition.endDate" id="endDate" maxlength="10" size="12"/> </td>
		 <td>备注</td>
		<td><input type="text" name="condition.keywords" id="conditionKeywords" />
		</td>
	  </tr>
	  <tr> 
		<td colspan="4">
		<div class="l">
		<input type="submit" value="查询" id="searchBtn"/>
		<input type="reset" value="重填" id="resetBtn"/>
		</div>
		<div class="r">
			<input type="button" value="返回" id="backBtn" class="h"/>
		</div>
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
	<div id="inputPanel" class="l">
	
	</div>
  </div>
<!-- others -->
<link rel="stylesheet" type="text/css" href="${base}/static/app/css/t-style.css"></link>
<script type="text/javascript" src="${base}/static/library/puerta/t-table.js"></script> 
<script type="text/javascript" src="${base}/static/app/javascript/studentpoints_search.js"></script>
<script type="text/javascript" src="${base}/static/library/puerta/t-simple-no.js"></script>
<script type="text/javascript">

var pageVars = {
	queryString:'',
	action : '_create',
	id :'',
	base :'${base}'
}

var lang = {
	no:'编号',name:'名称',createdTime:'创建时间',comments:'备注',status:'状态',points:'积分',domain:'学生积分查询',
	admin:'用户',type:'类型',owner:'学生编号'
}

	function checkCost(){
		
		if(isEmpty($F("studentId_cost"))){
			alert("请填写学生编号");
			$("studentId_cost").focus();
			return false;
		}
		if(!isInteger($F("cost_points"))){
			alert("积分必须为整数");
			$("cost_points").focus();
			return false;
		}		
		if(isEmpty($F("cost_comments"))){
			alert("请填写兑换内容");
			$("cost_comments").focus();
			return false;
		}
		
		return true ;
	}

	function check(){
		
		if(isEmpty($F("studentId"))){
			alert("请填写学生编号");
			$("studentId").focus();
			return false;
		}
		if(!isInteger($F("points"))){
			alert("积分必须为整数");
			$("points").focus();
			return false;
		}		
		if(isEmpty($F("comments"))){
			alert("请填写调整原因");
			$("comments").focus();
			return false;
		}
		
		return true ;
	}
</script>
</@b.html>