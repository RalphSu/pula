<#macro basePath><#if springMacroRequestContext.getContextPath()=="/"><#else>${springMacroRequestContext.getContextPath()}</#if></#macro>
<#macro urlPath>${springMacroRequestContext.getRequestUri()}<#if springMacroRequestContext.getQueryString()?exists>?${springMacroRequestContext.queryString}</#if></#macro>
<#macro text key><#if mls?exists>${mls.t(key,null)?if_exists?html}<#else>${key}</#if></#macro>
<#global base><@basePath/></#global>
<#global url><@urlPath/></#global>

<html>
<head>
　<meta name="viewport" content="width=device-width, initial-scale=1" />

<title>${work.studentName !? js_string} 普拉星球个人作品</title>
<!-- wechat title and images -->
<div id='wx_pic' style='margin:0 auto;display:none;'>
    <img src='${base}/static/app/images/icon/index/logo_300.png' class="img-responsive" alt="Responsive image" > </img>
</div>

</head>
<body>
<link rel="stylesheet" media="screen" type="text/css" href="${base}/static/library/bootstrap/css/bootstrap.min.css" />
    
    <link rel="stylesheet" media="screen" type="text/css" href="${base}/static/library/jquery.rating/jquery.rating.css" />
    <script type="text/javascript" src="${base}/static/library/jquery.rating/jquery-1.6.1.min.js"></script>
    <script type="text/javascript" src="${base}/static/library/jquery.rating/jquery.rating.js"></script>

	<div class="panel panel-primary">
		<div class="panel-heading">
			<h3 class="panel-title">课程：${work.courseName !? js_string}</h3>
		</div>
		<div class="panel-body">学员姓名：${work.studentName !? js_string}
	
	    <p>发布日期: ${work.workEffectDate !? js_string}</p>
    	<#if af??>
                            评价：${work.comments !? js_string}
            <select id="work.rate" name="work.rate" class="rating">
                    <option value="1" <#if work.rate == 1> selected='true' </#if> >还不错哦</option>
                    <option value="2" <#if work.rate == 2> selected='true' </#if> >画的很好</option>
                    <option value="3" <#if work.rate == 3> selected='true' </#if> >很棒的作品</option>
                    <option value="4" <#if work.rate == 4> selected='true' </#if> >非常棒</option>
                    <option value="5" <#if work.rate == 5> selected='true' </#if> >最棒的</option>
                </select>
            <br/>
            <img src='./icon?fp=${af.fileId}&id=${af.id}' class="img-responsive" alt="Responsive image" > </img>
            <br/>
                </div>
        </#if>
    <script type="text/javascript">
        $(document).ready(function() {
            $('.rating').rating({disabled:true,showCancel:false});
        });
    </script>
	</div>
</body>
</html>
