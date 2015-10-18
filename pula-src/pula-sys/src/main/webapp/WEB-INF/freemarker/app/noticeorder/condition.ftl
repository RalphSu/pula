
<form action="${uri}" method="get" id="searchForm">
	<table border="0" class="grid" width="800" id="queryForm">
		<colgroup>
			<col style="width: 80px" />
			<col />
			<col style="width: 60px" />
			<col />
			<col style="width: 60px" />
			<col />
		</colgroup>
		<tr class="title">
			<td colspan="4">查询条件</td>
		</tr>
		<tr>
			<td>学生编号</td>
			<td><input type="text" name="condition.studentNo"
				value="${condition.studentNo?if_exists?html}" /></td>
			<td colspan="2"></td>
		</tr>

		<tr>
			<td colspan="4"><input type="submit" value="查询" id="searchBtn" />
				<input type="reset" value="重填" id="resetBtn" /></td>
		</tr>
	</table>
</form>