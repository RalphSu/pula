	<form action="${uri}" method="get" id="searchForm">
	<table border="0" class="grid" width="1000" id="queryForm"><colgroup>
	<col style="width:80px"/>
	<col /><col style="width:80px"/>
	<col /><col style="width:80px"/>
	<col />
	</colgroup>
          <tr class="title">
            <td colspan="6">查询条件</td></tr>
		 <tr> 
		<td>学号</td>
		<td>
		<input type="text" name="condition.keywords" value=""/>
		</td><td>课程</td>
		<td>
		<select name="condition.categoryId" id="condition.categoryId">
		<option value="">(全部)</option>
		<#list ['A','B','C','D','E'] as tp>
		<option value="${tp?if_exists?html}">${tp?if_exists?html}</option>
		</#list>
		</select>
		<select name="condition.categoryId" id="condition.categoryId"><option value="">(全部)</option>
		</select>
		</td><td>分校-教室</td>
		<td>
		<select name="condition.categoryId" id="condition.categoryId">
		<option value="">(全部)</option>
		<#list ['001 金桥总校'] as tp>
		<option value="${tp?if_exists?html}">${tp?if_exists?html}</option>
		</#list>
		</select>
		<select name="condition.categoryId" id="condition.categoryId"><option value="">(全部)</option>
		<option value="">1号教室</option>
		</select>
		</td>
		</tr><tr>
		<td>教师编号</td>
		<td>
		<input type="text" name="condition.keywords" value=""/>
		</td><td>日期</td>
	 <td colspan="3"><input type="text" value="${condition.beginDate?if_exists?html}" name="condition.beginDate" id="beginDate" class='dateField' maxlength="10" size="12"/>
		 <img src='${base}/static/laputa/images/icons/arrow_blue.gif' border=0 align="absmiddle">
		 <input type="text" value="${(condition.endDate?if_exists?html)}" name="condition.endDate" id="endDate" class='dateField' maxlength="10" size="12"/> </td>
	  </tr>
	  <tr> 
		<td colspan="6">
		<input type="submit" value="查询" id="searchBtn"/>
		<input type="reset" value="重填" id="resetBtn"/>
		</td>
	  </tr> 
	</table>
	 </form>