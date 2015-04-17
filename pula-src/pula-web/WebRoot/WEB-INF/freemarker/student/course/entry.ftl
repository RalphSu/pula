<#import "/student/macros/common.ftl" as b>
<@b.html title="我的积分" pageId="container_course">



<div class="courses">
<div style='color:white;margin-bottom:10px;'> 课程类型 <select id="selCourseType">
<#list courseTypes as ct>
<option value="${ct.id}" <#if ct.selected>selected</#if>>${ct.name!?html}</option>
</#list>
</select></div>

<#list results as rr>

	<div class="course-item" title="${rr.comments!?js_string}">
	${rr.name!?html} ${rr.id}

	<#if hits[rr.id?string]??>
	${hits[rr.id?string].gamePlayed?string("已经玩过游戏了","尚未玩过游戏")}  <a href="game?id=${hits[rr.id?string].id}">ID=${hits[rr.id?string].id}</a>
	<#else>
	未上过这节课
	</#if>
	</div>

</#list>

</div>

<script language="javascript">
<!--
	

	$('selCourseType').addEvent('change',function(){
		window.location='${uri}?type='+this.value;

	});
//-->
</SCRIPT>
</@b.html>
