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
<#if json_data??>
<form action="${uri}" method="post" id="addForm">
	
	<table border="0" class="grid">
	


		
		<tr>
		<td>
		<#if allowEdit><div class="tips" style="margin:10px 0px;width:100px;padding:5px">编辑模式</div></#if>
		<div id="detailTable"></div></td>
		</tr><tr> 
		<td >
		
		<div class="l">
			<input type="submit" value="保存" id="saveBtn" accesskey="s"/>
		<input type="hidden" name="branchId" value="${branchId}"  />
		<input type="hidden" name="year" value="${year}" />
		<input type="hidden" name="month" value="${month}" />
		<input type="hidden" name="json" id="jsonDetail" />
		</div>
		<div class="r">
			ALT+S 保存
		</div>
		
		</td>
	  </tr> 
	</table></form></#if>


  </div>
<!-- others -->
<#include "/calendar.ftl"/>
<link rel="stylesheet" type="text/css" href="${base}/static/app/css/t-style.css"></link>
<script type="text/javascript" src="${base}/static/library/puerta/t-table.js"></script> 
<script type="text/javascript" src="${base}/static/app/javascript/teacherperformance.js"></script>
<script type="text/javascript" src="${base}/static/library/mootools/modules/mbox/mt.mbox.js"></script>
<script type="text/javascript" src="${base}/static/library/puerta/t-simple-no.js"></script>
<script type="text/javascript" src="/pula-sys/static/library/puerta/t-moonselector.js"></script>
<script type="text/javascript" src="${base}/static/library/puerta/t-datagrid.js"></script>
<script type="text/javascript" src="${base}/static/library/mootools/modules/UUID.js"></script>
<script type="text/javascript">

var pageVars = {
	queryString:'',
	action : '_create',
	id :'',
	imagePath:'${base}/static/laputa/images/icons/',
	base :'${base}',year:${year},month:${month},
	allowEdit :${allowEdit?string("true","false")},
	start:${json_data???string("true","false")}
}

var lang = {
	name:'名称',no:'编号',domain:'教师每月绩效',
	branchName:'分支机构',enabled:'状态',mobile:'手机',gender:'性别',giftPoints:'赠分上限'
}


	function check(){
		
		
		var b = true;
		var ael = null ;
		$$("input.numberEdit").each ( function (el){
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
		

		
		
		return true ;
	}
	var pes = null ;
window.addEvent('domready',function(){

	pes = new PPage({"id":"pes","pageMode":"pageMode","searchForm":"searchForm","addForm":"addForm"});

	<#if json_data??>
	var fillItems = function(){
			var items =  ${json_data} ;
			pes.grid.fill(items);
			if(! pageVars.allowEdit){
				pes.grid.fixLine();
			}
		};


		
		fillItems();
	</#if>
});
	</script>
</@b.html>