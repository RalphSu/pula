	<form action="${uri}" method="get" id="searchForm">
	<table border="0" class="grid" width="800" id="queryForm"><colgroup>
	<col style="width:60px"/>
	<col /><col style="width:80px"/>
	<col />
	<#if headquarter>
	<#assign col_count=6>
	<col style="width:80px"/>
	<col />
	<#else>
	<#assign col_count=4>
	</#if>
	</colgroup>
          <tr class="title">
            <td colspan="${col_count}">查询条件</td></tr>
		 <tr> 
		<td>关键词</td>
		<td>
		<input type="text" name="condition.keywords" value="${condition.keywords?if_exists?html}"/>
		</td>
		<#if headquarter>
		<td>分支机构</td>
		<td>
		<select name="condition.branchId" id="condition.branchId">
		<option value="">(全部)</option>
		<#list branches as tp>
		<option value="${tp.id?if_exists?html}">${tp.no?if_exists?html} ${tp.name?if_exists?html}</option>
		</#list>
		</select>
		</td>
		</#if>
		<td>性别</td>
	<td><label><input type="radio" name="condition.gender" value="0" class="rGender1" checked/>全部</label>
		<#list genders as tp>
		<label><input type="radio" name="condition.gender" value="${tp.id?if_exists?html}" class="rGender1"/>${tp.name?if_exists?html}</label>
		</#list></td>
	  </tr>
	  <tr> 
		<td colspan="${col_count}">
		<input type="submit" value="查询" id="searchBtn"/>
		<input type="reset" value="重填" id="resetBtn"/>
		</td>
	  </tr> 
	</table>
	 </form>