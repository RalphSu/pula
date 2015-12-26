
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
<#if af?? >
    <img src='./icon?fp=${af.fileId}&id=${af.id}' class="img-responsive" alt="Responsive image" > </img>
<#else>
    <img src='${base}/static/app/images/icon/index/logo_300.png' class="img-responsive" alt="Responsive image" > </img>
</#if>
</div>

</head>
<body>
<link rel="stylesheet" media="screen" type="text/css" href="${base}/static/library/bootstrap/css/bootstrap.min.css" />
    
	<div class="panel panel-primary">
		<div class="panel-heading">
			<h3 class="panel-title">${course.name !? js_string}</h3>
		</div>
		<div class="panel-body">
		                  分部：<b>${course.branchName}</b><p/>适用年龄：${course.applicableAges} 岁
		      <br/>
		      ${course.comments}
	          <p>最后更新: ${course.updateTime}
		      <br/>
		      <#if af?? >
                <img src='./icon?fp=${af.fileId}&id=${af.id}' class="img-responsive" alt="Responsive image" > </img>
              </#if>
		</div>
	</div>
</body>
</html>
