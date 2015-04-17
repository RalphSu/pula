function cb_default(e){
	if(e.error){ alert (e.message);} else{ topSuccess() ;}
}
function cb_reload(e){
	if(e.error){ alert (e.message);} else{ topSuccess() ;window.location.reload() ;}
}

function gr(url,data,ret){
	var aj = new Request.JSON(
			{
				url:url,
				onSuccess:function (e){
					if(!e.error){
						go(ret);
					}else{
						alert(e.message);
					}
				}
			}).send(data);
}

function gf(url,data,func){
	var aj = new Request.JSON(
			{
				url:url,
				onSuccess:func
	}).send(data);
}
function gfh(url,data,func){
	var aj = new Request(
			{
				url:url,
				onSuccess:func
	}).send(data);
}
function sf(form,func){
	gf(form.action,form.toQueryString(),func);
}
function go(url){
	window.location.href = url;
}
function topSuccess(){
	if(top.showSuccess){
		top.showSuccess();
	}else{
		alert('it\' s ok');
	}
}
function topHiddenSuccess(){
	if(top.hideSuccess){
		top.hideSuccess();
	}else{
	}
}
function topDialogChanged(){
	if(top.dialogChanged){
		top.dialogChanged();
	}
}
	function _remove(){
		if(!checkSelectedCss(".objId")){
					alert("请至少选取一项");
					return ;
		}
		if(!confirm("是否确定要删除选中的数据？")){
					return 
		}
		
		PA.ajax.gf('remove',$('batchForm').toQueryString(),function (e){
			if(e.error){
				topHiddenSuccess();
				alert(e.message);
			}else{
				topSuccess();
				location.reload();
			}			
		});
	}
