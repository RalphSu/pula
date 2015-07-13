	<form action="${uri}" method="get" id="searchForm">
	<table border="0" class="grid" width="800" id="queryForm"><colgroup>
	<col style="width:60px"/>
	<col /><col style="width:60px"/>
	<col /><col style="width:60px"/>
	<col />
	</colgroup>
      <tr class="title">
            <td colspan="4">查询条件</td></tr>
	  <tr> 
		<td colspan="1">课程ID</td>
		<td>
		<input type="text" name="condition.courseId" value="${condition.courseId?if_exists?html}"/>
		</td>
		<td colspan="1">或者 课程名字</td>
		<td>
		<input type="text" name="condition.courseName" value="${condition.courseName?if_exists?html}"/>
		</td>
	  </tr>
	  
	  <tr> 
		<td colspan="1">学生ID</td>
		<td>
		<input type="text" name="condition.studentId" value="${condition.studentId?if_exists?html}"/>
		</td>
		<td colspan="1">或者 学生名字</td>
		<td>
		<input type="text" name="condition.studentName" value="${condition.studentName?if_exists?html}"/>
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