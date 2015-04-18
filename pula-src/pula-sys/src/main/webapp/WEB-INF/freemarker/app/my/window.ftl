<#import "/app/macros/commonWin.ftl" as b>
<@b.html title="window">
<iframe id="contentFrame" name="contentFrame" width="100%" src="${u?if_exists}" frameBorder=0 scrolling=yes style="visibility:hidden"></iframe>
<SCRIPT LANGUAGE="JavaScript">
<!--
	
	function resizeFrame(){
		var h = document.documentElement.offsetHeight ;
		if(h==0 || h < document.documentElement.clientHeight ){
			h = document.documentElement.clientHeight ; 
		}
		$("contentFrame").setStyle('height',h );
		$("contentFrame").style.visibility='visible';
	}	
	window.addEvent('domready', resizeFrame);
	window.addEvent('resize', resizeFrame);
//-->
</SCRIPT>

</@b.html>
