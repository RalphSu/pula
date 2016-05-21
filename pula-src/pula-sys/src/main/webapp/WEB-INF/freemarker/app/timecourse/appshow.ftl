
<#macro basePath><#if springMacroRequestContext.getContextPath()=="/"><#else>${springMacroRequestContext.getContextPath()}</#if></#macro>
<#macro urlPath>${springMacroRequestContext.getRequestUri()}<#if springMacroRequestContext.getQueryString()?exists>?${springMacroRequestContext.queryString}</#if></#macro>
<#macro text key><#if mls?exists>${mls.t(key,null)?if_exists?html}<#else>${key}</#if></#macro>
<#global base><@basePath/></#global>
<#global url><@urlPath/></#global>


<html>
<head>
　<meta name="viewport" content="width=device-width, initial-scale=1" />

<title>${course.name !? js_string} 普拉星球课程简介</title>

<!-- wechat title and images -->
<div id='wx_pic' style='margin:0 auto;display:none;'>
    <img src='${base}/static/app/images/icon/index/logo_300.jpg' class="img-responsive" alt="Responsive image" > </img>
</div>


<style type="text/css">
.panel-course {
  border-color: ${color !? js_string};
}
.panel-course > .panel-heading {
  background-color: ${color !? js_string};
  border-color: ${color !? js_string};
}
.panel-course > .panel-heading + .panel-collapse > .panel-body {
  border-top-color: ${color !? js_string};
}
.panel-course > .panel-heading .badge {
  background-color: ${color !? js_string};
}
.panel-course > .panel-footer + .panel-collapse > .panel-body {
  border-bottom-color: ${color !? js_string};
}

</style>

</head>
<body>
<link rel="stylesheet" media="screen" type="text/css" href="${base}/static/library/bootstrap/css/bootstrap.min.css" />
    
	<div class="panel panel-course">
		<div class="panel-heading">
			<h3 class="panel-title"><b>${course.name !? js_string}</b></h3>
		</div>
		<div class="panel-body">
		                  分部：<b>${course.branchName !? js_string}</b><p/>适用年龄：${course.applicableAges !? js_string} 岁
		      <br/>
		      ${course.comments}
	          <p>最后更新: ${course.updateTime}
		      <br/>
		      <#if af?? >
                <img src='./icon?fp=${af.fileId}&id=${af.id}' class="img-responsive" alt="Responsive image" > </img>
              </#if>
		</div>
	</div>
<br/>

<div id="appstore">
苹果appstore下载：<p>
<img src='${base}/static/app/images/pula_app_store.png' class="img-responsive" alt="Responsive image" > </img>
</p>
</div>

	
</body>
</html>
