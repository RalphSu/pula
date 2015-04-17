	<form action="${uri}" method="get" id="searchForm">
	<table border="0" class="grid" width="600" id="queryForm"><colgroup>
	<col style="width:60px"/>
	<col /><col style="width:80px"/>
	<col />
	</colgroup>
          <tr class="title">
            <td colspan="4">查询条件</td></tr>
		 <tr> 
		<td>关键词</td>
		<td>
		<input type="text" name="condition.keywords" value="${condition.keywords?if_exists?html}"/>
		</td><td>分支机构</td>
		<td>
		<select name="condition.branchId" id="condition.branchId">
		<option value="">(全部)</option>
		<#list branches as tp>
		<option value="${tp.id?if_exists?html}">${tp.no?if_exists?html} ${tp.name?if_exists?html}</option>
		</#list>
		</select>
		</td>
	  </tr>
	  <tr> 
		<td colspan="4">
		<input type="submit" value="查询" id="searchBtn"/>
		<input type="reset" value="重填" id="resetBtn"/>
		</td>
	  </tr> 
	</table>
	 </form>