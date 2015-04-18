<#import "/teacher/macros/common.ftl" as b>
<@b.html title="评分" pageId="container_score">

<table class="grid" width="700" style="margin-top:60px">
<colgroup>
<col style="width:70px"/><!-- no -->
<col style="width:110px"/><!-- name -->
<col style="width:140px"/><!-- partner -->
<col style="width:70px"/><!-- beginTime-->
<col style="width:70px"/><!-- endTime -->
</colgroup>
<tr>
<td colspan="5"  id="pager1"><@b.pager results/></td>
</tr>
<tr class="title">
<td>学生</td>
<td>时间</td>
<td>地点</td>
<td>课程</td>
<td align="center">操作</td>
</td>
</tr>
<#list results.items as tt>
<tr>
<td>${tt.studentName!?html}</td>
<td>${ tt.startTime?date}</td>
<td>${tt.classroomName!?html}</td>
<td>${tt.courseName!?html}</td>
<td class="text-c"><a href="view?id=${tt.id}&backURL=${url?url}">评分</a></td>
</tr>
</#list>
<#if results.items?size==0><tr><td colspan="5">暂无记录</td></tr></#if>
</table>

<script language="javascript">
<!--
	
//-->
</SCRIPT>
</@b.html>
