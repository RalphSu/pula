<#import "/app/macros/commonBase.ftl" as b><@b.html title="shop.title">
<#include "/calendar.ftl"/>
<div class="top" id="__top"> </div>
<div class="body">
    <!-- condition -->
    <div id="conditionDiv" class="force-h forList">
	<form action="${uri}" method="get" id="searchForm">
	<table border="0" class="grid" width="800" id="queryForm"><colgroup>
	<col style="width:100px"/>
	<col />	<col style="width:100px"/>
	<col />
	</colgroup>
            <tr class="title">
            <td colspan="4">查询条件</td></tr>
		 <tr> 
		<td>学号</td>
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
	<div id="inputPanel" class="l" style="margin-left:10px;padding-left:10px;width:400px;border-left:1px solid #ccc;height:340px">
	<form action="${uri}" method="post" id="addForm" class="h"> 
	<table border="0" class="grid" width="400">
	<colgroup><col style="width:80px"/>
	<col />
	</colgroup>
	  <tr class="title"> 
            <td colspan="2"><div class="l">手动调整积分 <span id="pageMode" style="color:blue" class="h"></span></div>
              </td>
	  </tr><tr>
		<td>学号</td>
		<td ><div id="sUser"></div></td>
	</tr>
    <tr> 
    <td width="100">积分<font color=red>*</font></td>
    <td >
	<input type="text" name="points" id="points" value="0" class="numberEdit"/> 负值为扣分
	</td></tr>
	<tr>
		<td>备注<font color=red>*</font></td>
		<td ><textarea name="comments" id="comments" cols="35" rows="3"></textarea></td>
	</tr><tr> 
		<td colspan="2">
		<input type="submit" value="<@b.text key="submitBtn"/>" id="submitBtn"/>
		</td>
	  </tr> 
	</table>
	</form>

	<form action="${uri}" method="post" id="costForm">
	<table border="0" class="grid" width="400">
	<colgroup><col style="width:80px"/>
	<col />
	</colgroup>
	  <tr class="title"> 
            <td colspan="2"><div class="l">积分兑换礼品</div>
              </td>
	  </tr><tr>
		<td>学号<font color=red>*</font></td>
		<td ><div id="sUser2"></div></td>
	</tr>
	<tr><td>礼品编号<font color=red>*</font></td>
		<td><!-- use suggest -->
		<div id="sGift"></div>
		</td></tr>
    <tr> 
    <td width="100">数量<font color=red>*</font></td>
    <td >
	<input type="text" name="quantity" id="cost_quantity" value="0" class="numberEdit"/>
	</td></tr>
	<tr>
		<td>备注<font color=red>*</font></td>
		<td ><textarea name="comments" id="cost_comments" cols="35" rows="3"></textarea></td>
	</tr><tr> 
		<td colspan="2">
		<div class="l"><input type="submit" value="<@b.text key="submitBtn"/>" id="submitBtn2"/></div>
		<div class="r" id="tips"></div>
		<div class="c"></div>
			
		</td>
	  </tr> 
	</table>
	</form>
	</div>
  </div>
<!-- others -->
<link rel="stylesheet" type="text/css" href="${base}/static/app/css/t-style.css"></link>
<script type="text/javascript" src="${base}/static/library/puerta/t-table.js"></script> 
<script type="text/javascript" src="${base}/static/app/javascript/studentpoints.js"></script>
<script type="text/javascript" src="${base}/static/library/puerta/t-simple-no.js"></script>
<script type="text/javascript" src="${base}/static/app/javascript/modules/autoSuggest.js"></script>
<script type="text/javascript" src="${base}/static/library/puerta/t-suggest.js"></script>
<script type="text/javascript">

var pageVars = {
	queryString:'',
	action : '_create',
	id :'',
	base :'${base}'
}

var lang = {
	no:'编号',name:'名称',createdTime:'创建时间',comments:'备注',status:'状态',points:'积分',domain:'学生积分',
	admin:'用户',type:'类型',owner:'学号'
}

	function checkCost(){
		
		if(isEmpty($F("studentId_cost"))){
			alert("请填写学号");
			$("studentId_cost").focus();
			return false;
		}

		if(isEmpty($F("cGiftNo"))){
			alert("请填写礼品编号");
			$("cGiftNo").focus();
			return false;
		}

		if(!isInteger($F("cost_quantity"))){
			alert("数量必须为整数");
			$("cost_quantity").focus();
			return false;
		}		
		if(isEmpty($F("cost_comments"))){
			alert("请填写备注");
			$("cost_comments").focus();
			return false;
		}
		
		len = $F("cost_comments").length ;
		if(len>200){
			alert("备注信息过长，请填写200字以内信息");
			$("cost_comments").focus();
			return false;
		}
		return true ;
	}

	function check(){
		
		if(isEmpty($F("studentId"))){
			alert("请填写学号");
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