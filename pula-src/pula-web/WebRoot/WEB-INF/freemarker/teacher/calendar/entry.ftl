<#import "/teacher/macros/common.ftl" as b>
<@b.html title="排班" pageId="container_calendar">
<style>
#calendar{
	width:700px;
	float:left;
	position:relative;
	padding:0px 0px ;
}
#calendar .calendar-header{
	text-align:center;
	height:30px;
	font-size:14px;
	font-weight:bold;
}	
#calendar .calendar-buttons{
	position:absolute;
	right:20px;
	top:5px;
}

#calendar .calendar-branch{
	position:absolute;
	left:20px;
	top:10px;
	font-size:12px;
}
#calendar ul{
	list-style:none;
	padding:0px;
	margin:0px;
	margin:10px ;
}

#leader{
	position:absolute;
	left:200px;
	top:-50px;
	color:white;
	font-size:14px;
}


#leader A{
	color:white;
}

.weekday-block{
	width:95px;
	height:25px;
	float:left;
	border-top:1px solid #ccc;
	border-left:1px solid #ccc;
	text-align:center;
	line-height:25px;
	background:#ccffff;
}

.block-f{
	
	border-right:1px solid #ccc;
}

.block-b{
	
	border-bottom:1px solid #ccc;
}
.block-e{
	
	border-right:1px solid #ccc;
	__border-bottom:1px solid #ccc;
}

.none-block{
	background:#f0f0f0;
	 text-indent: -40em;
}


.day-block{
	width:95px;
	float:left;
	border-top:1px solid #ccc;
	border-left:1px solid #ccc;
	position:relative;
	height:40px;
}

.day-block .date{
	text-align:center;
	font-size:14px;
	padding-top:4px;
	position:absolute;
	z-index:0;
	width:40px;
	height:40px;
	top:20px;
	right:0px;
}

.day-block .checkbox{
	position:absolute;
	top:1px;
	left:1px;

}
.day-block .items{
	margin:5px;
}

/*.day-block .items h3{
	margin:0px;
	padding-left:4px;
	font-size:12px;
	background:#e9f9fe;
	height:25px;
	line-height:25px;
	border-bottom:1px dashed #e6e8fb;
	overflow:hidden;
	cursor:pointer;
	position:relative;
	text-align:center;
	font-weight:normal;
	color:#ccc;
	
}
*/


.day-block .items div a{
	display:block;
	cursor:text;
	height:40px;
	
}

.day-block .items div a.empty{
}


.restday {
	color:#358fc6;
	font-weight:bold;
	cursor:pointer;
}
#calendar h3.workday {
	color:#ff0000;
	font-weight:normal;
	text-align:center;
}

.weekend{
	background:#ceecff;
}
.book-hover{
	font-weight:normal;
}

#lblSelect{
	background:#ffffcc;
}

#indicator_add{
	position:absolute;
	right:4px;
	top:2px;
	background:url('${base}/static/laputa/images/icons/add.gif') ;
	height:16px;
	width:16px;	
	display:block;
	cursor:pointer;
}

#spEmployee a {
	padding-right:10px;
	display:block;
	margin:2px;
}

#spEmployee a.hover{
	padding:2px;
}

#spEmployee a.hover:hover{
	text-decoration: line-through;
	border:1px dashed #ccc;
}


#spEmployee a div.plan-text{
	font-weight:bold;
}
#spEmployee a div.plan-tips{
	margin:5px;
	background:#fff4cc;
	border:1px solid #ff9900;
}


.c{
	clear:both;
	width:0px;height:0px;
}

#btnSummary{
	color:#339900;
}

#spDate{
	color:#336600;
}


</style>
  <script type="text/javascript" src="${base}/static/teacher/js/calendar.js"></script>

<div id="calendar">
	<div id="leader">
	<a href="#" onclick="gotoMonth(${prevYear},${prevMonth})">上个月</a>

	${year}年 ${month+1}月

	<a href="#" onclick="gotoMonth(${nextYear},${nextMonth})">下个月</a>
	</div>
	<div class="calendar_container"></div>
</div>
<script type="text/javascript">
<!--

var pageVars = {
	year :${year},
	month :${month}
}
var lang = {
	weekDays : new Array("星期日","星期一","星期二","星期三","星期四","星期五","星期六")
}

	var p = new PlanCalendar();

	p.initData();


	function gotoMonth(y,m){
		window.location="${uri}?year="+y+"&month="+m;
	}
	
//-->
</script>
</@b.html>
