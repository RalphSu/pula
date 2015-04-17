<#import "/app/macros/commonDialog.ftl" as b>

<@b.html title="用户权限">
<script type="text/javascript" src="${base}/static/library/yui/yui_2.8.1/build/yahoo-dom-event/yahoo-dom-event.js"></script>
<script type="text/javascript" src="${base}/static/library/yui/yui_2.8.1/build/treeview/treeview.js"></script>
<script type="text/javascript" src="${base}/static/laputa/javascript/TaskNode.js"></script> 
<link rel="stylesheet" type="text/css" href="${base}/static/laputa/css/yui/treeview/check/tree.css?">


<SCRIPT LANGUAGE="JavaScript">
<!--
	function searchMe(id){
		//alert($('condition.moduleId'));
		$('condition.moduleId').value=id;
		
		submitForm('searchForm');
	}

	function clickType(obj){
		$('condition.moduleId').value = "" ;
		submitForm('searchForm');
	}
//-->
</SCRIPT>
<script type="text/javascript">


//<![CDATA[
	function hideLoading(){
		var obj = document.getElementById("loading");
		obj.style.visibility='hidden';

	}


var treeData = ${json?if_exists};


var tree;
var nodesArray = [];
var oContextMenu;
(function() {

    function treeInit() {
        tree = new YAHOO.widget.TreeView("treeDiv1");
		buildTreeNodeFromArray(treeData,tree.getRoot());


       // Expand and collapse happen prior to the actual expand/collapse,
       // and can be used to cancel the operation
       tree.subscribe("expand", function(node) {
              //alert(node.index + " was expanded");
              // return false; // return false to cancel the expand
           });

       tree.subscribe("collapse", function(node) {
              //alert(node.index + " was collapsed");
           });

       // Trees with TextNodes will fire an event for when the label is clicked:
       tree.subscribe("labelClick", function(node) {
              //alert(node.index + " label was clicked");
			  alert('hi');
           });

		
       tree.draw();tree.expandAll();

		//makeMenu();


		hideLoading();

    }

	function buildTreeNodeFromArray(arr,parentNode){
		var arrLen = arr.length ; 
		for(var i  = 0 ; i < arrLen ; i++){
			var jsonNode = arr[i];
			//var tmpNode = new YAHOO.widget.TaskNode(jsonNode.label, parentNode, false);
			var tmpNode = new YAHOO.widget.TaskNode(jsonNode, parentNode, false);
			nodesArray.push(tmpNode);

			if(jsonNode.children1 ) {
				buildTreeNodeFromArray(jsonNode.children1,tmpNode);
			}else if(jsonNode.checked){
				tmpNode.check();
				if(tmpNode.disabled ) {
					//tmpNode.disabled = true ;
					//tmpNode.updateParent();
				}
			}
		}
	}
    
    
	

    YAHOO.util.Event.addListener(window, "load", treeInit);





})();

//]]>
</script>


<table border="0" class="grid" width="100%">
  <tr class="title"> 
    <td >用户信息:${sysUser.loginId?if_exists?html}(${sysUser.name?if_exists?html}) </td>
  </tr>

  
</table>
<div style="position: absolute;top:260px;left:5px;height:20px;width:100px;visibility:visible;font-size:9pt;background-color:#FFCC00;border:1px #ccc solid" id="loading"><B>加载中...</B>
</div>
<div id="treeDiv1" onselectstart="return false;" style="-moz-user-select:none;height:250px;overflow-y:scroll;"></div>

<hr>
<form action="_purview" method="post" id="addForm">
<input type="submit" name="submitBtn" style="display:none" id="addForm.submitBtn">
<input type="hidden" name="id" value="${id?if_exists}"/>
<input type="hidden" name="pid" value="" id="pid"/><input type="submit" value="确定设置" id="okBtn">
<input type="button" value="返回" id="closeBtn" onclick="parent.Mbox.close()"/>
</form>
<script type="text/javascript">

	function disableBtn3(b){
		$('closeBtn').disabled = b ;
		$('okBtn').disabled = b; 
	}

	function check(){
		showTreeState();
		return true ;
	}
<@b.submitForm 'addForm'>
//$('addForm').reset();
parent.Mbox.close();
</@b.submitForm>	

	function showTreeState() {
        var ids = '';
        for (var i in tree._nodes) {
			
            var n = tree._nodes[i];
            if (n && "undefined" != typeof n.checkState&&n.checkState>0&&n.data.type=='p') {
                ids += (n.data.id)+",";
            }
        }

        //alert(out.join("\n"));
		
		$('pid').value = ids;
    }


</script>

</@b.html>
