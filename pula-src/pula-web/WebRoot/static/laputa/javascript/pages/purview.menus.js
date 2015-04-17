//menuitem - add;
function addChild(node){
			var param = 'purviewId=';
			var data = node.data ;
			if(data.type=='p'){
			}else{
				param = 'moduleId=';
			}
			reload = false;
			top.openDialog({
				title: lang.createPurview,
				url: pageVars.base+"/purview/create?"+param+data.id,
				width: 780,
				height: 280,
				caller: window,
				onClose:onCloseDialog
			});
}
//menuitem - edit
function editPurview(node){
	var data = node.data ;
	reload = false;
	top.openDialog({
		title: lang.updatePurview ,
		url: pageVars.base+"/purview/update?id="+data.id,
		width: 780,
		height: 300,
		caller: window,
		onClose:onCloseDialog
	});
}

//menuitem - remove
function removePurview(node){
	if(!confirm(lang.deleteConfirm)){
		return ;
	}
	var data = node.data;
	var aj = new Request.JSON({
				url:'remove',
				onSuccess:function (e){
					if(e.error){
						alert(e.message);
					}else{
						top.showSuccess();
						removeCurrentNode();
					}
				}
	}).send('objId='+data.id+'&_json=1');

}
//menuitem - export
	function exportToXML(node){
		var data = node.data;
		var aj = new Request({
				url:'getXml',
				onSuccess:function (e){
						top.showSuccess();
						copyToClipboard(e);					
				}
		}).send('id='+data.id);

		
	}

//menuitem - move up
function moveUp(node){

		var data = node.data;
		var aj = new Request.JSON({
				url:'move',
				onSuccess:function (e){
					if(e.error){
						alert(e.message);
					}else{
						top.showSuccess();
						reloadTree();
					}				
				}
		}).send('id='+data.id+'&up=true');
	

}
//menuitem - move down
function moveDown(node){
		var data = node.data;
		var aj = new Request.JSON({
				url:'move',
				onSuccess:function (e){
					if(e.error){
						alert(e.message);
					}else{
						top.showSuccess();
						reloadTree();
					}				
				}
		}).send('id='+data.id+'&down=true');

}
//dialog.callback




