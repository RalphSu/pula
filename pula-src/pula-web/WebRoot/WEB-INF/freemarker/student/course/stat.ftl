<#import "/student/macros/common.ftl" as b>
<@b.html title="我的评估" pageId="container_stat">
<script type="text/javascript" src="${base}/static/student/js/jquery-1.2.6.min.js"></script>
<script type="text/javascript" src="${base}/static/student/js/jquery-ui-personalized-1.6rc2.min.js"></script>

<script type="text/javascript" src="${base}/static/student/js/swfobject.js"></script>
<script type="text/javascript">

swfobject.embedSWF(
  "${base}/static/student/swf/open-flash-chart.swf", "my_chart",
  "100%", "100%", "9.0.0", "${base}/static/student/swf/expressInstall.swf",
  {"data-file":"_stat"}, {wmode:"transparent"} );

$(document).ready(function(){
  //$("#resize").resizable();
});
</script>

<div id="resize" style="width:350px; height:250px; padding: 10px;margin-left:95px;">
<div id="my_chart"></div>
</div>


<script language="javascript">
<!--
	

//-->
</SCRIPT>
</@b.html>
