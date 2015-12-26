<#macro basePath><#if springMacroRequestContext.getContextPath()=="/"><#else>${springMacroRequestContext.getContextPath()}</#if></#macro>
<#macro urlPath>${springMacroRequestContext.getRequestUri()}<#if springMacroRequestContext.getQueryString()?exists>?${springMacroRequestContext.queryString}</#if></#macro>
<#macro text key><#if mls?exists>${mls.t(key,null)?if_exists?html}<#else>${key}</#if></#macro>
<#global base><@basePath/></#global>
<#global url><@urlPath/></#global>
<html>
<head>
</head>
<body>
    <link rel="stylesheet" media="screen" type="text/css" href="${base}/static/library/jquery.rating/jquery.rating.css" />
    <script type="text/javascript" src="${base}/static/library/jquery.rating/jquery-1.6.1.min.js"></script>
    <script type="text/javascript" src="${base}/static/library/jquery.rating/jquery.rating.js"></script>

	<div class="panel panel-primary">
		<div class="panel-heading">
			<h3 class="panel-title">课程：${work.courseName !? js_string}</h3>
		</div>
		<div class="panel-body">学员姓名：${work.studentName !? js_string}</div>
	</div>
	<p>发布日期: ${work.updateTime !? js_string}</p>
	<#if af??>
                        评价：<select id="work.rate" name="work.rate" class="rating">
                <option value="1" <#if work.rate == 1> selected='true' </#if> >还不错哦</option>
                <option value="2" <#if work.rate == 2> selected='true' </#if> >画的很好</option>
                <option value="3" <#if work.rate == 3> selected='true' </#if> >很棒的作品</option>
                <option value="4" <#if work.rate == 4> selected='true' </#if> >非常棒</option>
                <option value="5" <#if work.rate == 5> selected='true' </#if> >最棒的</option>
            </select>
        <br/>
        <img src='./icon?fp=${af.fileId}&id=${af.id}' class="img-responsive" alt="Responsive image" > </img>
        <br/>
    </#if>
    
    <script type="text/javascript">
        $(document).ready(function() {
            $('.rating').rating({disabled:true,showCancel:false});
        });
    </script>
	
</body>
</html>
