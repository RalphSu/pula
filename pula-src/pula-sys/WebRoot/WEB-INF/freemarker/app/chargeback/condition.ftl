<div id="conditionDiv" class="h forList">
	<form action="${uri}" method="get" id="searchForm">
	<table border="0" class="grid" width="1000" id="queryForm"><colgroup>
	<col style="width:70px"/>
	<col style="width:140px"/><col style="width:70px"/>
	<col style="width:90px"/><col style="width:80px"/>
	<col style="width:90px"/><col style="width:80px"/>
	<col />
	</colgroup>
	<#assign total_col=8>
          <tr class="title">
            <td colspan="${total_col}">查询条件</td></tr>
		 <tr> 
		<td>订单号</td>
		<td>
		<input type="text" name="condition.no" value="${condition.no?if_exists?html}" style="width:100px"/>
		</td>
		<td>日期</td>
	 <td colspan="3"><input type="text" value="${condition.beginDate?if_exists?html}" name="condition.beginDate" id="beginDate" maxlength="10" size="12" class="dateField"/>
		 <img src='${base}/static/laputa/images/icons/arrow_blue.gif' border=0 align="absmiddle">
		 <input type="text" value="${(condition.endDate?if_exists?html)}" name="condition.endDate" id="endDate" maxlength="10" size="12" class="dateField"/> </td>

		 <td>学员编号</td>
		 <td><div id="sStudent"></div></td>
	  </tr><tr>
<td>状态</td>
		<td>
		<#if cm_4_apply??>
		待审
		<#elseif cm_4_chargeback??>
		已审
		<#else>
		<select name="condition.status" id="condition.status">
		<option value="0">(全部)</option>
		<#list statusList as tp>
		<option value="${tp.id?if_exists?html}">${tp.name?if_exists?html}</option>
		</#list>
		</select>
		</#if>
		</td>
		
		
<td>佣金类型</td><td colspan="3">
	 <select name="condition.commissionType" id="condition.commissionType">
		<option value="0">(全部)</option>
		<#list commissionTypeList as tp>
		<option value="${tp.id?if_exists?html}">${tp.name?if_exists?html}</option>
		</#list>
		</select>
		</td>
	
				 <td>教师编号</td>
		 <td><div id="sTeacher"></div></td>

	  </tr>
	  <tr> 
<td>课程产品</td>
		<td>
		<select name="condition.courseProductId" id="condition.courseProductId">
		    <option value="0">(全部)</option>
			<#list courseProducts as cp>
		    <option value="${cp.id}" >${cp.name!?html}</option>
			</#list>
        </select>
		</td>
		<#if headquarter><td>分支机构</td>
		<td colspan="3">
		<select name="condition.branchId" id="condition.branchId">
		<option value="0">(全部)</option>
		<#list branches as tp>
		<option value="${tp.id?if_exists?html}">${tp.no!?html} ${tp.name?if_exists?html}</option>
		</#list>
		</select>
		</td></#if>
				 <td>销售编号</td>
		 <td <#if !headquarter>colspan="5"</#if>><div id="sSalesman"></div></td>

	  <tr> 
		<td colspan="${total_col}">
	<input type="submit" value="查询" id="searchBtn"/>

		</td>
	  </tr> 
	</table>
	 </form>
	 </div>