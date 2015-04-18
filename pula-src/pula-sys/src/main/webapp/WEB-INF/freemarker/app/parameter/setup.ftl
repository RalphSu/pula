<#import "/app/macros/commonBase.ftl" as b>
<@b.html title="系统参数">
<script type="text/javascript" src="${base}/static/library/yui/yui_2.8.1/build/container/container_core.js"></script>
<script type="text/javascript" src="${base}/static/library/yui/yui_2.8.1/build/element/element-min.js"></script>
<script type="text/javascript" src="${base}/static/library/yui/yui_2.8.1/build/yahoo-dom-event/yahoo-dom-event.js"></script>
<script type="text/javascript" src="${base}/static/library/yui/yui_2.8.1/build/tabview/tabview-min.js"></script>
<link rel="stylesheet" type="text/css" href="${base}/static/laputa/css/yui/tabview/tabview.css" />
  <div class="top" id="__top"> </div>
<div class="body">
<form action="_setup" method="post" id="sForm">
<div id="tabs1" class="yui-navset">
	<ul class="yui-nav">
<#list pages as p >
		<li <#if p_index==0>class="selected"</#if>><a href="#tab${p.id}"><em>${p.name?if_exists?html}</em></a></li>
</#list>
	</ul>
<div class="yui-content">  
	<#list pages as p >
	<div id="tab${p.id}">
		<table border="0" class="grid" width="700">
		<colgroup><col style="width:120px"/><col/></colgroup>
		<#if folders[p.id]?exists>
		<#list folders[p.id] as f>
		<tr class="title">
			<td colspan="2">${f.name?if_exists?html}</td>
		</tr>
		<#list parameters[f.id] as pp>
			<tr>
				<td width="240">${pp.name?if_exists?html}</td>
				
				<td>
				<#if pp.handleEditor>
				${pp.editor}
				<#else>
				<INPUT TYPE="${pp.editorType}" NAME="values" value="${pp.value?if_exists}" size="60">
				</#if>
				<INPUT type="hidden" name="params" value="${pp.id?if_exists}">
				</td>
			</tr>
		</#list>
		</#list><#else></#if>
		</table>
	</div>
</#list>
</div>
	
</div>
	<BR>
<input type="submit" value="保存设置" id="submitBtn"/>
	<input type="reset" value="重填" id="resetBtn"/>
	<input type="hidden" name="_json" value="1">
</form>


<div style="height:40px;padding:10px 10px 10px 10px;line-height:20px;display:none" class="tips"> 

<b>$</b><b>{webRoot}</b>=${webRoot?if_exists?html}<BR>
<b>$</b><b>{contextPath}</b>=${contextPath?if_exists?html}
</div>
</div>
<script type="text/javascript">



	window.addEvent('domready',function (){
		var tabView = new YAHOO.widget.TabView('tabs1');  
		var tb = new PA.TToolBar({
						container:"__top",
						title:'系统参数',
						buttons:[
						
					
						]
					});

	});
	
	function check(){
		disableBtn3(true);
		return true ;
	}

	$('sForm').addEvent('submit',function (e){
			e.stop();
			if(!check()) return ;
			var aj = new Request.JSON({
						url:$('sForm').get('action'),

						onSuccess:function (e){
							
							disableBtn3(false);
							if(e.error){
								topHiddenSuccess();
								alert(e.message);
							}else{
								topSuccess();
								
							}
						}
					}).send($('sForm').toQueryString());
			

		});

	function disableBtn3(b){
		$("submitBtn").disabled = b ;
		$("resetBtn").disabled = b; 
	}
</script>

</@b.html>