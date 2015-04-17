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
			月份 [<A href="#" onclick="pes.goMonth(${thisYear},${thisMonth})">前往本月</A>]
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
			<tr> 
			<td><div id="ms"></div>
			<input type="hidden" name="year" id="hidYear"/>
			<input type="hidden" name="month" id="hidMonth"/></td>
		 </tr>
		</table>
		</form>
	 </div>

<#if salesmanList ??>

	<table border="0" class="grid" width="1400">
<colgroup>
        <col style="width:80px;"></col>
</colgroup>
  <tr class="title"> 
    <td rowspan="3">销售人员</td>   
	<td colspan="12" class="text-c">订单</td>
	<td colspan="12" class="text-c">退单</td>
    </tr>		
	<tr class="title">
	
	<td colspan="2" class="text-c">全单</td>
    <td colspan="2" class="text-c">8 单</td>
    <td colspan="2" class="text-c">7 单</td>
    <td colspan="2" class="text-c">5 单</td>
    <td colspan="2" class="text-c">3 单</td>
    <td colspan="2" class="text-c">2 单</td>

	<td colspan="2" class="text-c">全单</td>
    <td colspan="2" class="text-c">8 单</td>
    <td colspan="2" class="text-c">7 单</td>
    <td colspan="2" class="text-c">5 单</td>
    <td colspan="2" class="text-c">3 单</td>
    <td colspan="2" class="text-c">2 单</td>
	</tr>
	<tr class="title">
	<#list 1..12 as aeeee >
	<td class="text-c">单数</td>
	<td class="text-c">总额</td>
	</#list>
	</tr>
	<#list salesmanList as sm> 
	<tr>
	  <td>${sm.name!?html}</td>
	  <#assign row=table[sm.no]/>
	  <#list ["","_r"] as fix>
	  <#list ["100","8","7","5","3","2"] as kkk>
	  <#assign key=kkk+fix/>
	  <#assign key_a=kkk+fix+"_amount"/>
	  <td class="text-c">${row[key]?default("<span style=\"color:#ccc\">0</span>")}</td>
	  <td class="text-c"><#if row[key_a]??>${row[key_a]?string("#.##")}<#else><span style="color:#ccc">0</span></#if></td>
	  </#list>
	  </#list>
	  </tr>
	</#list>
	  </table>

    </#if>
  </div>
<!-- others -->
<#include "/calendar.ftl"/>
<link rel="stylesheet" type="text/css" href="${base}/static/app/css/t-style.css"></link>
<script type="text/javascript" src="${base}/static/library/puerta/t-table.js"></script> 
<script type="text/javascript" src="${base}/static/app/javascript/salesman_performance.js"></script>
<script type="text/javascript" src="${base}/static/library/mootools/modules/mbox/mt.mbox.js"></script>
<script type="text/javascript" src="${base}/static/library/puerta/t-simple-no.js"></script>
<script type="text/javascript" src="/pula-sys/static/library/puerta/t-moonselector.js"></script>
<script type="text/javascript">

var pageVars = {
	queryString:'',
	action : '_create',
	id :'',
	base :'${base}',year:${year},month:${month}
}

var lang = {
	name:'名称',no:'编号',domain:'销售业绩查询',
	branchName:'分支机构',enabled:'状态',mobile:'手机',gender:'性别',giftPoints:'赠分上限'
}


	function check(){
		
		if(isEmpty($F("salesman.no"))){
			alert("请填写编号");
			$("salesman.no").focus();
			return false; 
		}
		if(isEmpty($F("salesman.name"))){
			alert("请填写名称");
			$("salesman.name").focus();
			return false; 
		}
		if(isEmpty($F("salesman.branchId"))){
			alert("请选择分支机构");
			$("salesman.branchId").focus();
			return false; 
		}

		
		var len = $$(".rGender:checked").length ;
		if(len==0){
			alert('请选取性别');
			$$(".rGender")[0].focus();
			return false;
		}
		
		var b = true;
		var ael = null ;
		$$(".numberEdit").each ( function (el){
			if( b ){
				b =  isReal( el.value ) ;
				if(!b){
					ael = el ;
				}
			}
		});

		if(!b){
			alert("请正确填写数值");
			ael.focus();
			return false;
		}
		

		len = $F("salesman.comments").length ;
		if(len>200){
			alert("备注信息过长，请填写200字以内信息");
			$("salesman.comments").focus();
			return false;
		}

		
		
		return true ;
	}
</script>
</@b.html>