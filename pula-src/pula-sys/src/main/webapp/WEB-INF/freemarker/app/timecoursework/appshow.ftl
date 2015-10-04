<html>
<body>

	<div class="panel panel-primary">
		<div class="panel-heading">
			<h3 class="panel-title">${work.studentNo}</h3>
		</div>
		<div class="panel-body">${work.courseNo}</div>
		<br />
	</div>

	<br />

	<p>发布日期: ${work.updateTime}
	<hr />
	作品：
	<br />
	
	<#if af??>
		<img src='./icon?fp=${af.fileId}&id=${af.id}' class="img-responsive" alt="Responsive image">
		<br/>
	</#if>
</body>
</html>
