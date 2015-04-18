
var dt = null;
window.addEvent('domready',function(){

	var getRequestParam = function(){
		return pageVars.requestParam;
	}
	var onSelectRow = function(i,tr){
		if(i==-1){
			showData(null);
		}else{
			var data = dt.rows[i] ;
			showData(data);
		}
	}
	var onDblClickRow = function(i,tr){
		if(i==-1){
			return ;
		}else{
			//reload /set parentId
			pushParent(dt.rows[i]);
			
			reloadLastParent();
		}
	}
	var reloadLastParent = function (){
		var d = pageVars.parent.getLast();

		if(d==null){
			$('pid').value = "";
			$('dictLimb.parentId').value = "";
			$('backToParentBtn').addClass('h');
		}else{
			$('pid').value = d.id;
			$('dictLimb.parentId').value = d.id;	
			$('backToParentBtn').removeClass('h');
		}
		$('searchBtn').click();
	}

	var pushParent = function (data){
		pageVars.parent.push(data);
	}
	var removeLastParent = function(){
		pageVars.parent.erase(pageVars.parent.getLast());
	}

	var tb = new PA.ToolBar({
		container:"tb",
		buttons:[
		{
			label:_lang.create,
			link :'javascript:createPage()',
			icon: pageVars.base+'/static/laputa/images/icons/add.gif'
		},
		{
			label:lang.refresh,
			link :'javascript:reloadPage()',
			icon: pageVars.base+'/static/laputa/images/icons/refresh.gif'
		},
			{
			label:lang.organize,
			link :'javascript:organizePage()',
			icon: pageVars.base+'/static/laputa/images/16X16/map.png'
		},
		{
			label:_lang.remove,
			link :'javascript:remove()',
			icon: pageVars.base+'/static/laputa/images/icons/delete.gif'
		}
		]
	});

		dt = new NilViewTable({
		id :'dt',
		container:'dt',
		height:280,
		url:"list",
		requestParam:getRequestParam,
		selectRow:onSelectRow,
		intoRow:onDblClickRow,
		columns: [
			{label:NilViewTable.checkAll,width:24,key:'id',formatter:NilViewTable.formatCheckbox},
			{label:lang.no,width:260,key:'no'},
			{label:lang.name,width:340,key:'name'}
		]
	});


	pageVars.requestParam = "&"+$('searchForm').toQueryString();
	dt.draw();


	$('addForm').addEvent('submit',function(e){
		if(!check()){
			e.stop();
			return ;
		}
		disableBtn(true);
		e.stop();
		var data = encodeURIComponent(PA.ajax.f2j($('addForm')));
		//alert(PA.ajax.f2j($('addForm')));
		PA.ajax.gf(pageVars.action,'formData='+data,function (e ){
			disableBtn(false);
			if(e.error){
				alert(e.message);	
			}else{
				topSuccess();
				reload();
			}		
		});
	});
	$('searchForm').addEvent('submit',function(e){
		e.stop();
		pageVars.requestParam = "&"+$('searchForm').toQueryString();
		dt.pager.pageIndex = 1 ;
		reload();
		Mbox.close();
	});

	$('backToParentBtn').addEvent('click',function (e){
		removeLastParent();
		reloadLastParent();
	});

	
	reload();
	$('backToParentBtn').addClass('h');
});

	function _getQueryString(){
		var qs = "" ;
		var checked =false;
		$$(".objId").each(function (ee){
				if(ee.checked){
					qs +="&objId="+ee.value;
					checked = true ;
				}

		});
		if(!checked){
			alert(_lang.chooseOne);
			return null ;
		}
		return qs ;
	}

	function reload(){
		dt.reload();
		pageVars.action='_create';
		
		dt.selectedRow(null);
		showData(null);
		updateMode();

	}
	function disableBtn(b){
		$('submitBtn').disabled = b; 
		$('resetBtn').disabled = b; 
	}
	function createPage(){
		dt.selectedRow(null);
		showData(null);
	}

	function showData(d){
		if(d==null){
			$('dictLimb.no').value = "" ;
			$('dictLimb.name').value = "" ;
			$('dictLimb.id').value = "";
			pageVars.action='_create';
			$('dictLimb.indexNo').value = "0" ;
			updateMode();
			$("dictLimb.no").focus();
		}else{
			PA.ajax.gf('get','id='+d.id,function(e){
				if(e.error){alert(e.message);}
				var d = e.data ;
				$('dictLimb.no').value = d.no;
				$('dictLimb.name').value = d.name;
				$('dictLimb.id').value = d.id;
			$('dictLimb.indexNo').value =  d.indexNo ;
				$("dictLimb.no").focus();
				pageVars.action='_update';
				updateMode();
			});
			
		}
		
		
	}

	function updateMode(){
		if(pageVars.action=='_create'){
			$('pageMode').innerHTML = _lang.create;
		}else{
			$('pageMode').innerHTML = _lang.update;
		}

		var d = pageVars.parent.getLast();
		if(d!=null){
			$("underMode").innerHTML = lang.under+":"+d.no + "("+d.name+")";
		}else{
			$("underMode").innerHTML = "";
		}

	}

	function condition(){
		/*
		if(	$('conditionDiv').getStyle('display')=='none'){
			$('conditionDiv').setStyle('display','block');
			PA.ui.center('conditionDiv');
		}else{
			
			$('conditionDiv').addClass('h');
			$('conditionDiv').setStyle('display','none');
		}*/
		Mbox.open({
			url: "conditionDiv",
			openFrom: $("queryLink"),
			title:_lang.condition
		});	


		
	}

	function remove(){
		if(!checkSelectedCss(".objId")){
					alert(_lang.chooseOne);
					return ;
		}
		if(!confirm(_lang.removeConfirm)){
					return 
		}
		
		PA.ajax.gf('remove',$('batchForm').toQueryString(),function (e){
			if(e.error){
				topHiddenSuccess();
				alert(e.message);
			}else{
				topSuccess();
				reload();
			}			
		});
	}


	function reloadPage(){
		gf('reload','',cb_default);
	}

	function organizePage(){
		gf('organize','',function (e ){
			
			cb_default(e);
			reload();
			
		});
	}

