<#import "/app/macros/commonBase.ftl" as b><@b.html title="stocklog">
  <div class="top" id="__top"> </div>
<div class="body">
    <!-- condition -->
    <div id="conditionDiv" class="h forList">
	<form action="${uri}" method="get" id="searchForm">
	<table border="0" class="grid" width="800" id="queryForm"><colgroup>
		<col style="width:100px"/>
		<col />
		<col style="width:100px"/>
		<col style="width:180px"/>
	</colgroup>
          <tr class="title">
            <td colspan="4">查询条件</td></tr>
		 <tr> 
		<td>材料编号</td>
		<td colspan="3"><!-- use suggest -->
		<div id="scMaterialNo"></div>
		</td>
		</tr>
		<tr>
		<td>类型</td>
		<td ><select name="condition.type" id="condition.type">
		<option value="0">(全部)</option>
		<#list typeList as tp>
		<option value="${tp.id?if_exists?html}">${tp.name?if_exists?html}</option>
		</#list>
		</select></td>
		<td>分支机构</td>
		<td>
		<#if headquarter><select name="condition.branchId" id="branchList">
		<option value="0">(全部)</option>
		<#list branchList as tp>
		<option value="${tp.id!?html}">${tp.no!?html} ${tp.name!?html}</option>
		</#list>
		</select>
		<#else>
			${branch.name!?html}
		</#if>
		</td></tr>
		<tr>
		<td>日期</td>
	 <td colspan="1"><input type="text" value="${condition.beginDate?if_exists?html}" name="condition.beginDate" id="beginDate" maxlength="10" size="12" class="dateField"/>
		 <img src='${base}/static/laputa/images/icons/arrow_blue.gif' border=0 align="absmiddle">
		 <input type="text" value="${(condition.endDate?if_exists?html)}" name="condition.endDate" id="endDate" maxlength="10" size="12" class="dateField"/> </td>
		
		 <td>外单号</td><td><input type="text" name="condition.outNo"/></td>
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
		<td >材料编号<span class="redStar">*</span></td>
		<td colspan="3"><div id="sMaterialNo"></div></td>
	  </tr><tr>
		<td >数量<span class="redStar">*</span></td>
		<td colspan="3"><input type="text" name="form.quantity" id="form.quantity" size="10" maxlength="10" class="numberEdit" value="0"/><span id="spanUnit"></span> </td>
	  </tr>
	  <tr>
	  <td>类型<span class="redStar">*</span></td>
		<td colspan="3"><select name="form.target" id="form.target">
		<option value="0">请选择...</option>
		<#list typeList as tp>
		<option value="${tp.id?if_exists?html}">${tp.name?if_exists?html}</option>
		</#list>
		</select></tr>
	  <tr> 
		<td >外单号</td>
		<td colspan="3"><input type="text" name="form.outNo" id="form.outNo" size="40" maxlength="40"/> </td>
	  </tr>
	   <tr> 
		<td >备注</td>
		<td colspan="3"><input type="text" name="form.comments" id="form.comments" size="60" maxlength="100"/> </td>
	  </tr> 
	  <tr> 
		<td colspan="4">
		<input type="submit" value="<@b.text key="submitBtn"/>" id="submitBtn"/>
		<input type="hidden" name="form.id" id="form.id" value="0"/>
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
<script type="text/javascript" src="${base}/static/app/javascript/stockevent.js"></script>
<script type="text/javascript" src="${base}/static/library/mootools/modules/mbox/mt.mbox.js"></script>
<script type="text/javascript" src="${base}/static/app/javascript/modules/autoSuggest.js"></script>
<script type="text/javascript" src="${base}/static/library/puerta/t-suggest.js"></script>
<script type="text/javascript">

var pageVars = {
	queryString:'',
	action : '_create',
	id :'',
	base :'${base}',flag:'${pageVars.flag!?js_string}'
}

var lang = {
	name:'名称',no:'编号',domain:'${pageVars.title!?html}',materialNo:'材料编号',materialName:'材料名称',warehouse:'仓库',space:'区域',quantity:'数量',no:'内单号',outNo:'外单号'
,exportTo:'导出',typeName:'类型',eventTime:'日期',all:'全部',
	noTypes :["入库","出库","盘点","转仓"]
}


	function check(){
		
		if(isEmpty($F("materialNo"))){
			alert("请填写材料编号");
			$("materialNo").focus();
			return false; 
		}
		var v = $F("form.target");
		if(v == "0") {
			alert("请填写类型");
			$("form.target").focus();
			return false; 			
		}
	
		var b = true;
		var eel = null ;
		$$(".numberEdit").each ( function (el){
			var v = Number.from( el.value ) ;
			if( b && !v && v!=0   ) {
				eel = el ;
				b= false;
			}
			if(b&& !v && v<=0){
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
		
		return true ;
	}
</script>
</@b.html>