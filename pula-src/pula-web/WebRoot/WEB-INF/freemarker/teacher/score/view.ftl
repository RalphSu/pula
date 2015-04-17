<#import "/teacher/macros/common.ftl" as b>
<@b.html title="查看作品" pageId="container_viewscore">
<style>
.score{width:40px}
</style>
<div class="l">
<table class="grid" width="400">
<colgroup>
<col style="width:50px"/><!-- no -->
<col style="width:350px"/><!-- name -->
</colgroup>
<tr >
<td>学生</td>
<td>${result.studentName!?html}</td>
</tr><tr>
<td>时间</td>
<td>${result.startTime!?datetime} - ${result.endTime?datetime}</td>

</td>
</tr><tr>
<td>地点</td>
<td>${result.classroomName!?html}</td>

</td>
</tr><tr>
<td>课程</td>
<td>${result.categoryName!?html} - ${result.courseName!?html}</td>

</td>
</tr>
<#if result.scoreTime??>
<tr>
<td>初次评分时间</td>
<td>${result.scoreTime!?datetime}</td>

</td>
</tr>
</#if>
</table>
</div>
<div class="l" style="margin-left:10px">
<#if result.attachmentKey??>
<div id="divWork">
	<img src="viewWork?id=${result.id}" width="200" height="100" onclick="window.open('viewWork?id=${result.id}')" style="cursor:pointer"/>

</div>
</#if>
</div>
<div class="c"></div>

<form id="frmPost">
<table class="grid" width="600">
<colgroup>
<#list 1..5 as a>
<col style="width:60px"/>
</#list>
</colgroup>
<tr class="title">

<#list 1..5 as a>
<td>评分${a} </td>
</#list>
</tr>
<tr >
<#list 1..5 as a>
<td><input type="text" name="s${a}" id="s${a}" class="numberEdit score"/></td>
</#list>
</tr>
<tr>
<td colspan="5">
<input type="hidden" name="id" value="${result.id}"/>
<input type="submit" value="评分" />

<input type="button" value="返回" onclick="goBack()"/>
</tr>
</table>
</form>
<script type="text/javascript">
<!--
	window.addEvent('domready',function(){

		var obj = {s1:${result.score1},s2:${result.score2},s3:${result.score3},s4:${result.score4},s5:${result.score5}};

		for(var i =1 ; i < 6; i++){
			$('s'+i).value = obj['s'+i];
		}	

		$('frmPost').addEvent('submit',function(e){
			e.stop();
			if(!check()){
				return ;
			}

			PA.ajax.gf('_score',$('frmPost').toQueryString(),function(ed){
				if(ed.error) { alert (ed.message); return ;}
				goBack();
			});

		});

		function check(){
			var mel = null;
			$$(".score").each (function(el){
				if(mel==null && !isInteger(el.value)){
					mel = el ;
				}
			});

			if(mel!=null){
				alert('请正确填写数字');
				mel.focus();
				return false;
			}
			return true ;
		}

});
//-->
</script>
</@b.html>
