
<#macro basePath><#if springMacroRequestContext.getContextPath()=="/"><#else>${springMacroRequestContext.getContextPath()}</#if></#macro>
<#macro urlPath>${springMacroRequestContext.getRequestUri()}<#if springMacroRequestContext.getQueryString()?exists>?${springMacroRequestContext.queryString}</#if></#macro>
<#macro text key><#if mls?exists>${mls.t(key,null)?if_exists?html}<#else>${key}</#if></#macro>
<#global base><@basePath/></#global>
<#global url><@urlPath/></#global>


<html>
<head>
<!-- reponsive page -->
　<meta name="viewport" content="width=device-width, initial-scale=1" />

<!-- wechat title and images -->
<div id='wx_pic' style='margin:0 auto;display:none;'>
<img src='${base}/static/app/images/icon/index/logo_300.jpg' class="img-responsive" alt="Responsive image" > </img>
</div>

</head>
<body>
<link rel="stylesheet" media="screen" type="text/css" href="${base}/static/library/bootstrap/css/bootstrap.min.css" />
    
	<div class="panel panel-primary">
		<div class="panel-heading">
			<h3 class="panel-title">${notice.title}</h3>
		</div>
		<div class="panel-body">
    		${notice.content}
    		<br/>
    		<br/>
    		<!-- <div class="panel-body">活动特惠次数：${noticeOrder.count}</div>
    		<#if notice.noticeCourseNo != "" >
    			<a href="../timecourse/appshow?no=${notice.noticeCourseNo}">${notice.noticeCourseName}</a>
    		</#if>
    		-->
        	<p>订单创建日期: ${notice.createTime}
        	<p>最后修改日期: ${notice.updateTime}
    	</div>
	</div>
<br/>
</body>
</html>
