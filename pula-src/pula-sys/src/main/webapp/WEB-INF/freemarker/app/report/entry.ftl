<#import "/app/macros/commonBase.ftl" as b><@b.html title="学生课程消费查询">

<#macro basePath><#if springMacroRequestContext.getContextPath()=="/"><#else>${springMacroRequestContext.getContextPath()}</#if></#macro>
<#macro urlPath>${springMacroRequestContext.getRequestUri()}<#if springMacroRequestContext.getQueryString()?exists>?${springMacroRequestContext.queryString}</#if></#macro>
<#macro text key><#if mls?exists>${mls.t(key,null)?if_exists?html}<#else>${key}</#if></#macro>
<#global base><@basePath/></#global>
<#global url><@urlPath/></#global>


<link rel="stylesheet" media="screen" type="text/css" href="${base}/static/library/bootstrap/css/bootstrap.min.css" />


<#include "/calendar.ftl"/>
<link rel="stylesheet" type="text/css" href="${base}/static/app/css/t-style.css"></link>
<script type="text/javascript" src="${base}/static/library/puerta/t-table.js"></script>
<script type="text/javascript" src="${base}/static/app/javascript/timecoursework_view.js"></script>
<script type="text/javascript" src="${base}/static/library/mootools/modules/mbox/mt.mbox.js"></script>
<script type="text/javascript" src="${base}/static/library/puerta/t-simple-no.js"></script>
<script type="text/javascript" src="${base}/static/library/puerta/t-datagrid.js"></script>
<script type="text/javascript" src="${base}/static/library/mootools/modules/UUID.js"></script>


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
			<td>
			<select name="branch" id="branch"> 
                <#list branches as b>
                    <option value="${b?if_exists?html}" <#if b==branch>selected</#if> >${b?if_exists?html}</option>
                </#list>
			</td>
			<td tooltip="格式示例：2016-04-08">消费时间(比如：2016-04-15):</td>
			<td><input type="text" id="date" name="date" class="dateField" value="${date?if_exists?html}" /></td>
		</tr>

		<tr>
			<td colspan="4"><input type="submit" value="查询" id="searchBtn" />
		</tr>
	</table>
</form>

<table border="1" class="table table-striped table-condensed" class="grid" width="1200" >
	<thead>
	<tr>
		<td>学号</td>
		<td>学生名字</td>
		<td>消费日期</td>
		<td>本日消费次数</td>
		<td>消费系统课程次数</td>
		<td>消费特殊课程次数</td>
		<td>消费工坊次数</td>
		<td>消费活动次数</td>
		<td>课程名字</td>
	</tr>
	</thead>
	
	<tbody>
	
	<#list usages as u>
		<tr>
			<td>${u.studentNo?if_exists?html}</td>
			<td>${u.studentName?if_exists?html}</td>
			<td>${u.usageTime?if_exists?html}</td>
			<td>${u.dayCount?if_exists?html}
			<td>${u.usedCount?if_exists?html}</td>
			<td>${u.usedSpecialCourseCount?if_exists?html}</td>
			<td>${u.usedGongfangCount?if_exists?html}</td>
			<td>${u.usedHuodongCount?if_exists?html}</td>
			<td>${u.courseName?if_exists?html}</td>
		</tr>
	</#list>
	</tbody>

</table>

    </@b.html>
