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
		<div id="sStudent"></div>
		</td><td>课程</td>
		<td>
		<select name="condition.courseCategoryId" id="categoryId">
		<option value="">请选择...</option>
		<#list courseCategories as tp>
		<option value="${tp.id?if_exists?html}">${tp.name?if_exists?html}</option>
		</#list>
		</select>
		<select name="condition.courseId" id="condition.courseId" class="_categoryId"><option value="0">请选择...</option>
		</select>
		</td><td><#if headquarter></#if>教室</td>
		<td>
		<#if headquarter><select name="condition.branchId" id="cBranchId" >
		<option value="">请选择...</option>
		<#list branches as tp>
		<option value="${tp.id?if_exists?html}">${tp.name?if_exists?html}</option>
		</#list>
		</select>
		</#if>
		<select name="condition.classroomId" id="condition.classroomId" class="_cBranchId"><option value="0">请选择...</option>
		<#if !headquarter><#list classroomList as tp>
		<option value="${tp.id?if_exists?html}">${tp.name?if_exists?html}</option>
		</#list></#if>
		</select>
		</td>
		</tr><tr>
		<td>教师编号</td>
		<td>
		<div id="sTeacher"></div>
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