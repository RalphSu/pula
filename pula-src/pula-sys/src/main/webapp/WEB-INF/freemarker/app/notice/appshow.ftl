
<#macro basePath><#if springMacroRequestContext.getContextPath()=="/"><#else>${springMacroRequestContext.getContextPath()}</#if></#macro>
<#macro urlPath>${springMacroRequestContext.getRequestUri()}<#if springMacroRequestContext.getQueryString()?exists>?${springMacroRequestContext.queryString}</#if></#macro>
<#macro text key><#if mls?exists>${mls.t(key,null)?if_exists?html}<#else>${key}</#if></#macro>
<#global base><@basePath/></#global>
<#global url><@urlPath/></#global>


<html>
<head>
　<meta name="viewport" content="width=device-width, initial-scale=1" />

<title>${notice.title !? js_string} 普拉星球活动简介</title>

<!-- wechat title and images -->
<div id='wx_pic' style='margin:0 auto;display:none;'>
    <img src='${base}/static/app/images/icon/index/logo_300.png' class="img-responsive" alt="Responsive image" > </img>
</div>

<style type="text/css">
.panel-notice {
  // background-color : #D7B704;
  border: 3px;
  border-color: #D7B704;
}
.panel-notice > .panel-heading {
  // color: #fff;
  background-color: #D7B704;
  border: 3px;
  border-color: #D7B704;
}
.panel-notice > .panel-heading + .panel-collapse > .panel-body {
  border-top-color: #D7B704;
  border: 3px;
}
.panel-notice > .panel-heading .badge {
  // color: #fff;
  background-color: #D7B704;
}
.panel-notice > .panel-footer + .panel-collapse > .panel-body {
  border-bottom-color: #D7B704;
   border-bottom: 3px solid;
}

</style>

</head>
<body>
<link rel="stylesheet" media="screen" type="text/css" href="${base}/static/library/bootstrap/css/bootstrap.min.css" />
    
	<div class="panel-notice">
		<div class="panel-heading">
			<h3 class="panel-title"><b>${notice.title !? js_string}</b></h3>
		</div>
		<div class="panel-body">
            <p>${notice.content}</p>
    		<!-- 活动特惠价格：${notice.noticePrice} 活动特惠次数：${notice.noticeCount}<br/>
    		<#if notice.noticeCourseNo != "" >
    			<a href="../timecourse/appshow?no=${notice.noticeCourseNo}">${notice.noticeCourseName}</a>
    		</#if>
    		-->
    		<p> 最后更新日期: ${notice.updateTime !? js_string}
    		<br/>
    		<#if af?? >
                <img src='./icon?fp=${af.fileId}&id=${af.id}' class="img-responsive" alt="Responsive image" > </img>
            </#if>
        </div>
	</div>

<br/>
	
</body>
</html>
