<#import "/app/macros/commonBase.ftl" as b><@b.html title="考勤时段规则">
<style>
.top .text{
	line-height:25px;
	height:25px;
	margin-top:3px;
	color:#5f5f5f;
}
#ms{
	width:600px
}

</style>
  <div class="top" id="__top"> </div>
<div class="body">
    <!-- condition -->

	<div id="conditionDiv" >
		<form id="searchForm" action="${uri}">
		<table border="0" class="grid" width="605" >
		
	
		<tr class="title">
			<td class="text-c">月份 <#if nowYear!=year || nowMonth !=month>[<A href="#" onclick="goMonth(${nowYear},${nowMonth})">前往本月</A>]</#if></td></tr>
			<tr> 
			<td><div id="ms"></div></td>
		 </tr>
		</table>
		</form>
	
	</div>


	<!-- edit form -->
	
	
	<table border="0" class="grid" width="605">
	<colgroup>
	<col style="width:100px"/>
	<col />
	<col style="width:60px"/>
	<col style="width:60px"/>
	<col style="width:60px"/>
	<col style="width:60px"/>
	</colgroup>
	<tr class="title">
	<td>职员编号</td>
	<td>职员姓名</td>
	<td class="text-r">上午</td>
	<td class="text-r">中午</td>
	<td class="text-r">晚上</td>
	<td class="text-r">合计</td>
	<#list results as res>	
	<tr>
		<td >${res.no!?html}</td>
		<td >${res.name!?html}</td>
		<td  class="text-r">${res.period0!0}</td>
		<td  class="text-r">${res.period1!0}</td>
		<td  class="text-r">${res.period2!0}</td>
		<td  class="text-r">${ (res.periodSum)}</td>
	  </tr>
	</#list>	   
	 
	</table>



	<!-- show the list of  etc-->
<#if currentMonth>
<table border="0" class="grid" width="605">
<tr class="title"><td>其他未排班职员(${leftEmployee?size})</td></tr>
<tr><td>
<#list leftEmployee as k>
${k.no!?html}-${k.name!?html}<#if k_has_next>,</#if>
</#list>

</td></tr>
</table></#if>
</div>	

<!-- others -->
<#include "/calendar.ftl"/>
<link rel="stylesheet" type="text/css" href="${base}/static/app/css/t-style.css"></link>
<script type="text/javascript" src="${base}/static/library/puerta/t-table.js"></script> 
<script type="text/javascript" src="${base}/static/library/puerta/t-simple-no.js"></script>
<script type="text/javascript" src="${base}/static/library/puerta/t-moonselector.js"></script>
<script type="text/javascript">

var pageVars = {
	queryString:'',
	action : '_create',
	id :'',
	base :'${base}',
	imagePath:'${base}/static/laputa/images/icons/'
}

var lang = {
	name:'名称',no:'编号',domain:'工厂排班月汇总',
	materialNo:'材料编号',
	materialName:'材料名称',space:'区域',spec:'规格',msgSelectFirst:'请选择...',
	quantity:'数量',lineMsg:"第{index}行:{message}\n",needQty:'需要填写数量',needSpace:'需要选取区域',
	needDetail:"至少需要填写一行明细数据",cancel:'取消'
}


var goMonth = function(y,m){
	goURL("${uri}?month="+m+"&year="+y+"&backURL=${backURL!?url}&id=${id!?html}");
}

PA.on (function(){
		var tb = new PA.TToolBar({
					container:"__top",
					title:lang.domain,
					buttons:[
						
						
					]
				});


		$$('.trRow').addEvent('mouseover',function(){
			this.addClass('hover');
		}).addEvent('mouseout',function(){
			this.removeClass('hover');
		}).addEvent('click',function(){
		
		});


		var ms = new TMoonSelector({
				container:'ms',
				year:${year},
				month:${month},
				url:"${uri}?month={m}&year={y}&backURL=${backURL!?url}&id=${id!?html}",
				directForward:true,
				count:7
				});
});
	




</script>
</@b.html>