<#import "/app/macros/commonBase.ftl" as b><@b.html title="course.title">

<style>
#workerList A:hover{
	text-decoration:line-through;
}
</style>
  <div class="top" id="__top"> </div>
<div class="body">
	<!--  search form -->
	<div id="conditionDiv" class="h forList">
		<#include "condition.ftl"/>
	</div>

    <!-- listview -->
    <div class="l forList" width="100%" id="listview">
      <form id="batchForm">
        <div id="dt"> </div>
      </form>
      <!-- left ends --> 
    </div>

	<!-- edit form -->
	<div id="inputPanel" class="l h">
	<form action="${uri}" method="post" id="addForm">
	<div class="l"><table border="0" class="grid" width="700">
	<colgroup><col style="width:100px"/>
	<col /><col style="width:100px"/>
	<col />
	</colgroup>
	  <tr class="title"> 
            <td colspan="4"><div class="l">请填写下列信息 <span id="pageMode" style="color:blue"></span></div>
              <div class="r">
              <div class="backToList " id="backToList"><A href="javascript:pes.backToList()">返回至列表</A></div></td>
	  </tr> <tr> 
		<td >编号<span class="redStar">*</span></td>
		<td colspan="3"><input type="text" name="course.no" id="course.no" size="20" maxlength="40"/> </td>
	  </tr> 
	  <tr>
		<td >标题<span class="redStar">*</span></td>
		<td colspan="3"><input type="text" name="course.title" id="course.title" size="60" maxlength="40"/> </td>
	  </tr>
	  <tr>
		<td >格式化标题<span class="redStar"></span></td>
		<td colspan="3"><input type="text" name="course.formattedTitle" id="course.formattedTitle" size="60" maxlength="40"/> </td>
	  </tr>

	  <tr> 
		<td >内容</td>
		<td colspan="3">
		<textarea name="course.content" id="course.content" cols="50" rows="10"></textarea>
		
		</td>
	  </tr>
	   <tr> 
		<td >备注</td>
		<td colspan="3"><input type="text" name="course.comment" id="course.comment" size="60" maxlength="80"/> </td>
	  </tr> 
	  <tr> 
		<td colspan="4">
		<input type="submit" value="<@b.text key="submitBtn"/>" id="submitBtn"/>
		<input type="hidden" name="course.id" id="course.id"/>
		</td>
	  </tr> 
	</table>

</div>
	
	</form>
	</div>
  </div>
<!-- others -->
<#include "/calendar.ftl"/>
<link rel="stylesheet" type="text/css" href="${base}/static/app/css/t-style.css"></link>
<script type="text/javascript" src="${base}/static/library/puerta/t-table.js"></script> 
<script type="text/javascript" src="${base}/static/app/javascript/notice.js"></script>
<script type="text/javascript" src="${base}/static/library/mootools/modules/mbox/mt.mbox.js"></script>
<script type="text/javascript" src="${base}/static/library/puerta/t-simple-no.js"></script>
<script type="text/javascript" src="${base}/static/library/puerta/t-selectorloader.js"></script>
<!-- editor -->
<link rel="stylesheet" type="text/css" href="${base}/static/library/yui/yui_2.8.1/build/assets/skins/sam/skin.css"></link>
<script src="${base}/static/library/yui/yui_2.8.1/build/yahoo/yahoo.js"></script>
<!-- Utility Dependencies -->
<script type="text/javascript" src="${base}/static/library/yui/yui_2.8.1/build/yahoo-dom-event/yahoo-dom-event.js"></script>
<script type="text/javascript" src="${base}/static/library/yui/yui_2.8.1/build/element/element-min.js"></script>
<!-- Needed for Menus, Buttons and Overlays used in the Toolbar -->
<script type="text/javascript" src="${base}/static/library/yui/yui_2.8.1/build/container/container_core-min.js"></script>
<script type="text/javascript" src="${base}/static/library/yui/yui_2.8.1/build/menu/menu-min.js"></script>
<script type="text/javascript" src="${base}/static/library/yui/yui_2.8.1/build/button/button-min.js"></script>
<!-- Source file for Rich Text Editor-->
<script type="text/javascript" src="${base}/static/library/yui/yui_2.8.1/build/editor/editor-min.js"></script>
<script type="text/javascript" src="${base}/static/library/yui/yui_2.8.1/build/editor/simpleeditor-min.js"></script>

<script type="text/javascript">

var pageVars = {
	queryString:'',
	action : '_create',
	id :'',
	base :'${base}'
}

var lang = {
	name:'名称',no:'编号',domain:'课程',title:'标题',content:'内容',formattedTitle:'格式化标题',status:'状态',updateTime:'更新时间',comment:'备注'
}

var myEditor = new YAHOO.widget.SimpleEditor('course.content', {
    height: '300px',
    width: '522px',
    dompath: true, //Turns on the bar at the bottom
    animate: true //Animates the opening, closing and moving of Editor windows
});
myEditor.render();

	function check(){
		if(isEmpty($F("course.no"))){
			alert("请填写编号");
			$("course.no").focus();
			return false; 
		}
		if(isEmpty($F("course.title"))){
			alert("请填写标题");
			$("course.name").focus();
			return false; 
		}

		var b = true;
		var eel = null ;
		$$(".numberEdit").each ( function (el){
			var v = Number.from( el.value ) ;
			if( b && !v && v!=0  ) {
				eel = el ;
				b= false;
			}
			
		});

		if(!b){
			alert("请正确填写数值");
			eel.select();
			eel.focus();
			return false;
		}

		len = $F("course.comment").length ;
		if(len>200){
			alert("备注信息过长，请填写200字以内信息");
			$("course.comment").focus();
			return false;
		}

		var pass = true ;
		$$(".dateField").each ( function(el){
			if( !pass ) return ;
			var dv = $F( el );
			if( !isEmpty( dv ) &&  !isDate( dv )){
				alert("请正确填写日期");
				el.focus();
				pass =false; 
				return ;
			}
		});

		if(!pass ) {
			return false ;
		}
		
		return true ;
	}
</script>
</@b.html>