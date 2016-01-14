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

<div class="top" id="__top">
	  <div class="t-head">
		<div class="<#if !condition.history>t-tab-over<#else>t-tab</#if> l "><A href="${uri}?condition.branchId=${condition.branchId}">当前市场活动</A></div>
		<div class="<#if condition.history>t-tab-over<#else>t-tab</#if> l"><A href="${uri}?condition.branchId=${condition.branchId}&condition.history=1">历史市场活动</A></div>
		<div class="l tools_container"></div>
		<div class="l text">
		</div>
	</div>
  </div>
<div class="body">
    <!-- condition -->
    <div id="conditionDiv" class="h forList">
	<form action="${uri}" method="get" id="searchForm">
	<table border="0" class="grid" width="800" ><colgroup>
	<col style="width:100px"/>
	<col />
	
	</colgroup>
          <tr class="title">
            <td colspan="2">查询条件</td></tr>
		 <tr> 
		
		<td>分支机构</td>
		<td>
		<select name="condition.branchId" id="condition.branchId">
		<option value="">请选取...</option>
		<#list branches as tp>
		<option value="${tp.id?if_exists?html}" <#if condition.branchId==tp.id>selected</#if>>${tp.no?if_exists?html} ${tp.name?if_exists?html}</option>
		</#list>
		</select>
		</td>
	  </tr>
	  <tr> 
		<td colspan="2">
		<input type="submit" value="查询" id="searchBtn"/>
		<input type="reset" value="重填" id="resetBtn"/>
		<input type="hidden" name="condition.history" value="${condition.history?string("true","false")}"/>
		</td>
	  </tr> 
	</table>
	 </form>
	 </div>
<!-- list -->


<table class="grid" width="1000">
<colgroup>
<col style="width:70px"/><!-- no -->
<col style="width:110px"/><!-- name -->
<col style="width:140px"/><!-- partner -->
<col style="width:70px"/><!-- beginTime-->
<col style="width:70px"/><!-- endTime -->
</colgroup>
<tr>
<td colspan="5"  id="pager1"><@b.pager results/></td>
</tr>
<tr class="title">
<td>活动编号</td>
<td>活动名称</td>
<td>合作单位</td>
<td align="center">开始日期</td>
<td align="center">结束日期</td>
</td>
</tr>
<#list results.items as tt>
<tr>
<td>${tt.no!?html}</td>
<td>${tt.name!?html}</td>
<td>${tt.partner!?html}</td>
<td class="text-c"><#if tt.beginDate??>${tt.beginDate.time?date}<#else><span class="gray">无</span></#if></td>
<td class="text-c"><#if tt.endDate??>${tt.endDate.time?date}<#else><span class="gray">无</span></#if></td>
</tr>
</#list>
<#if results.items?size==0><tr><td colspan="5">暂无记录</td></tr></#if>
</table>



  </div>
<!-- others -->
<#include "/calendar.ftl"/>
<link rel="stylesheet" type="text/css" href="${base}/static/app/css/t-style.css"></link>
<script type="text/javascript" src="${base}/static/library/puerta/t-table.js"></script> 
<script type="text/javascript" src="${base}/static/app/javascript/activity_current.js"></script>
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

	$('searchForm').addEvent('submit',function(e){
		if(!check()){
			e.stop();
			return ;
		}
	});


	function check(){
		
		if(isEmpty($F("condition.branchId"))){
			alert("请选取分支机构");
			$("condition.branchId").focus();
			return false; 
		}
		
		return true ;
	}
</script>
</@b.html>