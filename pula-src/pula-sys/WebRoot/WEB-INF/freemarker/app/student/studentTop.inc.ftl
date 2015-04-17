<div class="top" id="__top">
	  <div class="t-head">
		<div class="<#if wt_target=="main">t-tab-over<#else>t-tab</#if> l "><A href="#" onclick="goThere('student/view?id=${id?if_exists}')">学生信息</A></div>
		<div class="<#if wt_target=="history">t-tab-over<#else>t-tab</#if> l"><A href="#" onclick="goThere('coursetaskresultstudent/student?id=${id?if_exists}')">授课历史</A></div>
		<div class="<#if wt_target=="personal_logs">t-tab-over<#else>t-tab</#if> l"><A href="#" onclick="goThere('student/personalLogs?id=${id?if_exists}')">学员日志</A></div>
		<div class="l tools_container">
			
		</div>
		<div class="l text">
		<#if student_meta??>学生：${student_meta.no!?html} ${student_meta.name!?html}</#if>
		</div>
	</div>
  </div>