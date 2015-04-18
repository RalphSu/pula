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
		height:280,
		url:"list",
		requestParam:getRequestParam,
		selectRow:onSelectRow,
		columns: [
			{label:"<input type='checkbox' id='checkAll'/>",width:28,key:'id',formatter:NilViewTable.formatCheckbox},
			{label:'编号',width:200,key:'no'},
			{label:'名称',width:100,key:'name'},
			{label:'序号',width:40,key:'indexNo'},
			{label:'类型',width:100,key:'type'}
		],
		rows : [
			{id:'abc',no:'123123',name:'好得很',indexNo:23,type:'好'},
			{id:'jixuhun',no:'9889',name:'哈哈',indexNo:24,type:'不好'},
			{id:'jixuhun',no:'9889',name:'哈哈',indexNo:24,type:'不好'}
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

	function disableBtn(b){
		$('submitBtn').disabled = b; 
		$('resetBtn').disabled = b; 
	}
});


	function showData(d){
		if(d==null){
			$('module.no').value = "";
			$('module.name').value = "";
			$('module.id').value =  "";
			$('module.comments').value =  "";
			document.getElementsByName("module.appField.no")[0].checked = true ;
			PA.ajax.gf('getMaxIndexNo',"appFieldNo="+pageVars.appFieldNo,function(e){
				if(e.error){alert(e.message);}else{
					$('module.indexNo').value =  e.data;
				}
				pageVars.action='_create';
				updateMode();
			});


			
		}else{
			PA.ajax.gf('get',"id="+d.id,function(e){
				if(e.error){alert(e.message);}else{
					$('module.no').value = e.data.no ;
					$('module.name').value = e.data.name;
					$('module.id').value =  e.data.id;
					$('module.appField.no_'+e.data.typeNo).checked = true ;
					$('module.indexNo').value = e.data.indexNo;
					$('module.comments').value = e.data.comments;
				}
				pageVars.action='_update';
				updateMode();
			});

			
		}
		$("module.no").focus();
		
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
		var qs = $('batchForm').toQueryString();
		PA.ajax.gf('remove',qs,function (e){
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

	function clickType(v){
		//goURL("entry?appFieldNo="+v.value);
		pageVars.requestParam ='&appFieldNo='+v.value;
		pageVars.appFieldNo = v.value;
		reload();
		condition();
	}
	function reload(){
		dt.reload();
		pageVars.action='_create';
		showData(null);

	}
	function createPage(){
		pageVars.action = '_create';
		showData(null);
	}