<#import "/student/macros/common.ftl" as b>

<@b.html title="我的积分" pageId="container_points">


<span style='color:white'>当前积分 : ${totalPoints}</span>

<table class="grid" width="700" >
<colgroup>
<col style="width:100px"/><!-- date -->
<col style="width:110px"/><!-- points -->
<col style="width:80px"/><!-- type -->
<col /><!-- comments -->
</colgroup>
<tr>
<td colspan="4"  id="pager1"><@b.pager results/></td>
</tr>
<tr class="title">
<td>时间</td>
<td class="text-c">积分</td>
<td class="text-c">类型</td>
<td>备注</td>
</td>
</tr>
<#list results.items as tt>
<tr >
<td>${tt.createdTime?date}</td>
<td class="text-c">${ tt.points}</td>
<td class="text-c">${tt.fromTypeName!?html}</td>
<td>${tt.comments!?html}</td>
</tr>
</#list>
<#if results.items?size==0><tr><td colspan="4">暂无记录</td></tr></#if>
</table>

<script language="javascript">
<!--
	
//-->
</SCRIPT>
</@b.html>
