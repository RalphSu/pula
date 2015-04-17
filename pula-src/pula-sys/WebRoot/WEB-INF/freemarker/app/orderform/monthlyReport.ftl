<#import "/app/macros/commonBase.ftl" as b><@b.html title="salesman.title">

<style>


#ms{
	width:600px;
}
</style>
  <div class="top" id="__top"> </div>
<div class="body">
    <!-- condition -->
    <div id="conditionDiv" class="h forList">
	<form id="searchForm" action="${uri}">
		<table border="0" class="grid" width="640" >
		
	
		<tr class="title">
			<td>
			<div class="l">
			年份 <#if year&gt;2000><a href="#" onclick="pes.goYear(${year-1})">${year-1}</a></#if> <input type="text" name="year" style="width:40px" value="${year}" id="year" maxlength="4"/>  <a href="#" onclick="pes.goYear(${year+1})">${year+1}</a>
			
			<#if thisYear!=year>[<A href="#" onclick="pes.goYear(${thisYear})">本年度</A>]</#if>
			</div>
			
			<div class="r">
			分支机构:<#if headquarter>
			<select name="branchId" id="branchId">
		<option value="0">请选择...</option>
		<#list branches as tp>
		<option value="${tp.id?if_exists?html}" <#if branchId==tp.id>selected</#if>>${tp.no!?html} ${tp.name?if_exists?html}</option>
		</#list>
		</select><#else>${branch.name!?html}</#if></div><div class="c"></div>
			</td></tr>
			
		</table>
		</form>
	 </div>

<#if monthList ??>

	<table border="0" class="grid" width="640">
<colgroup>
        <col style="width:80px;"></col>
</colgroup>
  <tr class="title"> 
    <td rowspan="2"  class="text-c">月份</td>   
	<td colspan="2" class="text-c">订单</td>
	<td colspan="2" class="text-c">退单</td>
    </tr>	
	<tr class="title">
	<#list 1..2 as aeeee >
	<td class="text-c">单数</td>
	<td class="text-c">总额</td>
	</#list>
	</tr>
	<#list monthList as sm> 
	<tr>
	  <td  class="text-c">${sm.id!?html}月</td>
	  <#if table[sm.id]??>
		  <#assign row=table[sm.id]/>
		  <td class="text-c">${row["totalOrders"]?default("<span style=\"color:#ccc\">0</span>")}</td>
		  <td class="text-c"><#if row["totalAmount"]??>${row["totalAmount"]?string("#.##")}<#else><span style="color:#ccc">0</span></#if></td>	  
			<td class="text-c">${row["totalBackOrders"]?default("<span style=\"color:#ccc\">0</span>")}</td>
		  <td class="text-c"><#if row["totalBackAmount"]??>${row["totalBackAmount"]?string("#.##")}<#else><span style="color:#ccc">0</span></#if></td>	
	  <#else>
	  <Td></td>
	  <Td></td>
	  <Td></td>
	  <Td></td>
	  </#if>
	  </tr>
	</#list>
	  </table>

    </#if>
  </div>
<!-- others -->
<#include "/calendar.ftl"/>
<link rel="stylesheet" type="text/css" href="${base}/static/app/css/t-style.css"></link>
<script type="text/javascript" src="${base}/static/library/puerta/t-table.js"></script> 
<script type="text/javascript" src="${base}/static/app/javascript/orderform_monthlyreport.js"></script>
<script type="text/javascript" src="${base}/static/library/mootools/modules/mbox/mt.mbox.js"></script>
<script type="text/javascript" src="${base}/static/library/puerta/t-simple-no.js"></script>
<script type="text/javascript">

var pageVars = {
	queryString:'',
	action : '_create',
	id :'',
	base :'${base}',year:${year}
}

var lang = {
	name:'名称',no:'编号',domain:'销售月表',
	branchName:'分支机构',enabled:'状态',mobile:'手机',gender:'性别',giftPoints:'赠分上限'
}


function check(){
	var v = $("year").value ;
	if(!isInteger(v) || eval(v)<2000){
		alert( '请正确填写年份,必须不小于2000年');
		$('year').focus();
		return false;
	}
	return true ;
}

	
</script>
</@b.html>