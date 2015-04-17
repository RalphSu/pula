<div class="top" id="__top">
	  <div class="t-head">
		<div class="<#if wt_target=="main">t-tab-over<#else>t-tab</#if> l "><A href="#" onclick="goThere('teacher/view?id=${id?if_exists}')">教师信息</A></div>
		<div class="<#if wt_target=="history">t-tab-over<#else>t-tab</#if> l"><A href="#" onclick="goThere('coursetaskresultstudent/teacher?id=${id?if_exists}')">工作历史</A></div>
		<#if utils.headquarter><div class="<#if wt_target=="assign_history">t-tab-over<#else>t-tab</#if> l"><A href="#" onclick="goThere('teacher/assignHistory?id=${id?if_exists}')">调拨历史</A></div></#if>
		<div class="<#if wt_target=="personal_performance">t-tab-over<#else>t-tab</#if> l"><A href="#" onclick="goThere('teacherperformance/teacher?id=${id?if_exists}')">个人绩效</A></div>
		<div class="<#if wt_target=="personal_logs">t-tab-over<#else>t-tab</#if> l"><A href="#" onclick="goThere('teacher/personalLogs?id=${id?if_exists}')">教师日志</A></div>
		<div class="l tools_container">
			
		</div>
		<div class="l text">
		<#if teacher_meta??>教师：${teacher_meta.no!?html} ${teacher_meta.name!?html}</#if>
		</div>
	</div>
  </div>