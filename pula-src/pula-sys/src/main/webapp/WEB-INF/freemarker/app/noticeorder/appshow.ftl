
<html>
<body>

	<div class="panel panel-primary">
		<div class="panel-heading">
			<h3 class="panel-title">${notice.title}</h3>
		</div>
		<div class="panel-body">${notice.content}</div>
		<br/>
		<div class="panel-body">活动特惠次数：${noticeOrder.count}</div>
		<br/>
		<#if notice.noticeCourseNo != "" >
			<a href="../timecourse/appshow?no=${notice.noticeCourseNo}">${notice.noticeCourseName}</a>
		</#if>
	</div>

<br/>
<img src="" class="img-responsive" alt="Responsive image">
	
	<p>发布日期: ${notice.updateTime}
</body>
</html>
