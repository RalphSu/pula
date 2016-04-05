
<#macro basePath><#if springMacroRequestContext.getContextPath()=="/"><#else>${springMacroRequestContext.getContextPath()}</#if></#macro>
<#macro urlPath>${springMacroRequestContext.getRequestUri()}<#if springMacroRequestContext.getQueryString()?exists>?${springMacroRequestContext.queryString}</#if></#macro>
<#macro text key><#if mls?exists>${mls.t(key,null)?if_exists?html}<#else>${key}</#if></#macro>
<#global base><@basePath/></#global>
<#global url><@urlPath/></#global>

<html>

<head>
<title>学生课程消费查询</title>
<link rel="stylesheet" media="screen" type="text/css" href="${base}/static/library/bootstrap/css/bootstrap.min.css" />

</head>

<body class="container-fluid ">

<h5>普拉星球学生课程查询:<h5>

<form method="get">

	<table border="1" class="table table-condensed" class="table" width="800" id="queryForm">
		<colgroup>
			<col style="width: 80px" />
			<col style="width: 60px" />
			<col style="width: 80px" />
			<col style="width: 60px" />
		</colgroup>
		<tr class="title">
			<td colspan="4">查询条件</td>
		</tr>
		<tr>
			<td>分部</td>
			<td><input type="text" name="branch" value="${branch?if_exists?html}" /></td>
			<td>消费时间</td>
			<td><input type="text" name="date" value="${date?if_exists?html}" /></td>
		</tr>

		<tr>
			<td colspan="4"><input type="submit" value="查询" id="searchBtn" />
		</tr>
	</table>
</form>

<table border="1" class="table table-striped table-condensed" class="grid" width="1200" >
	<thead>
	<tr>
		<td width="100">消费编号</td>
		<td>订单编号</td>
		<td>消费系统课程次数</td>
		<td>消费特殊课程次数</td>
		<td>消费工坊次数</td>
		<td>消费活动次数</td>
		<td>课程编号</td>
		<td>学号</td>
		<td>学生名字</td>
	</tr>
	</thead>
	
	<tbody>
	
	<#list usages as u>
		<tr>
			<td>${u.id?if_exists?html}</td>
			<td>${u.orderNo?if_exists?html}</td>
			<td>${u.usedCount?if_exists?html}</td>
			<td>${u.usedSpecialCourseCount?if_exists?html}</td>
			<td>${u.usedGongfangCount?if_exists?html}</td>
			<td>${u.usedHuodongCount?if_exists?html}</td>
			<td>${u.courseNo?if_exists?html}</td>
			<td>${u.studentNo?if_exists?html}</td>
			<td>${u.studentName?if_exists?html}</td>
		</tr>
	</#list>
	</tbody>

</table>

</body>

</html>