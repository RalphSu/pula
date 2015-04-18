
var dt = null;
window.addEvent('domready',function(){

	var getRequestParam = function(){
		return pageVars.requestParam;
	}
	var onSelectRow = function(i,tr){
		var data = dt.rows[i] ;
		showData(data);
	}
	

	var tb = new PA.ToolBar({
		container:"tb",
		buttons:[
		{
			label:'新增',
			link :'javascript:createPage()',
			icon: pageVars.base+'/static/laputa/images/icons/add.gif'
		},
		{
			label:'导入本地设置',
			link :'javascript:importPage()',
			icon: pageVars.base+'/static/laputa/images/icons/import.gif'
		},
		{
			label:'检查控制器',
			link :'javascript:checkController()',
			icon: pageVars.base+'/static/laputa/images/16X16/flag.png'
		},
		{
			label:'刷新持有器',
			link :'javascript:reloadPage()',
			icon: pageVars.base+'/static/laputa/images/icons/refresh.gif'
		},
		{
			label:'删除',
			link :'javascript:remove()',
			icon: pageVars.base+'/static/laputa/images/icons/delete.gif'
		},
		{
			label:'查询',
			link :'javascript:condition()',
			icon: pageVars.base+'/static/laputa/images/icons/view.gif'
		}
		]
	});

		dt = new NilViewTable({
		id :'dt',
		container:'dt',
		height:380,
		url:"list",
		requestParam:getRequestParam,
		selectRow:onSelectRow,
		dblClickRowToCheck:'id',
		columns: [
			{label:"<input type='checkbox' id='checkAll' onclick='checkAllCss(this.checked,\".objId\")'/>",width:28,key:'id',formatter:NilViewTable.formatCheckbox},
			{label:'URI',width:350,key:'uri'},
			{label:'在线检查',width:70,key:'onlineCheck',formatter:NilViewTable.formatBoolean},
			{label:'权限检查',width:70,key:'purviewCheck',formatter:NilViewTable.formatBoolean},
			{label:'关联数',width:60,key:'assignedCount',css:'numberEdit',headerCss:'numberEdit'}
		]
	});

	dt.draw();
	dt.reload();


	$('addForm').addEvent('submit',function(e){
		$('addForm').action = pageVars.action;
		disableBtn(true);
		e.stop();
		sf($('addForm'),function (e ){
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
		$('conditionDiv').setStyle('display','none');
	});

	$('btnSetOnline').addEvent('click',function (e){
		setOnline(true);		
	});
	$('btnSetOnline2').addEvent('click',function (e){
		setOnline(false);		
	});
	$('btnSetPurview').addEvent('click',function (e){
		setPurview(true);		
	});
	$('btnSetPurview2').addEvent('click',function (e){
		setPurview(false);		
	});


	function setOnline(b){
		var qs = _getQueryString();
		if(qs == null ) return ;
		//alert(qs);
		PA.ajax.gf('onlineCheck',qs+'&onlineCheck='+b,function (e){
			if(e.error){
				alert(e.message);	
			}else{
				topSuccess();
				reload();
			}
		});	
	}
	function setPurview(b){
		var qs = _getQueryString();
		if(qs == null ) return ;
		//alert(qs);
		PA.ajax.gf('purviewCheck',qs+'&purviewCheck='+b,function (e){
			if(e.error){
				alert(e.message);	
			}else{
				topSuccess();
				reload();
			}
		});	
	}
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
			alert("请至少选择一项");
			return null ;
		}
		return qs ;
	}

	function reload(){
		dt.reload();
		pageVars.action='_create';
		showData(null);
		updateMode();

	}

});
	function importPage(){
		gf('importPackage','',cb_default);	
	}

	function reloadPage(){
		gf('reload','',cb_default);
	}
	function disableBtn(b){
		$('submitBtn').disabled = b; 
		$('resetBtn').disabled = b; 
	}
	function createPage(){
		pageVars.action = '_create';
		showData(null);
	}

	function showData(d){
		if(d==null){
			$('requestUri.uri').value = "" ;
			$('pc').checked = false;
			$('oc').checked = false;
			$('requestUri.id').value = "";
			
		}else{
			$('requestUri.uri').value = d.uri ;
			$('pc').checked = (d.purviewCheck );
			$('oc').checked = (d.onlineCheck);
			$('requestUri.id').value =  d.id;
			pageVars.action='_update';
		}
		$("requestUri.uri").focus();
		updateMode();
	}

	function updateMode(){
		if(pageVars.action=='_create'){
			$('pageMode').innerHTML = '新增';
		}else{
			$('pageMode').innerHTML = '修改';
		}
	}

	function condition(){
		if(	$('conditionDiv').getStyle('display')=='none'){
			$('conditionDiv').setStyle('display','block');
			PA.ui.center('conditionDiv');
		}else{
			
			$('conditionDiv').addClass('h');
			$('conditionDiv').setStyle('display','none');
		}

		
	}

	function remove(){
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
				dt.rowIndex = -1 ;
				dt.reload();
			}			
		});
	}

	function checkController(){
		PA.ajax.gf('checkController','',function (e){
			if(e.error){
				topHiddenSuccess();
				alert(e.message);
			}else{
				topSuccess();
				var ds = e.data;
				var sb ="<div> 总数:"+ ds.length +"</div>";
				ds.each (function(el,idx){
					sb+= "<div class='error"+el.id+"'>"+el.name+"</div>";
				});
				$$(".checkResultViewer").set('html',sb);
			}			
		});
	}
