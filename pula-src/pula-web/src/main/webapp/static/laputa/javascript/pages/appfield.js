PNormalDomain = function(configs){
	this.config = configs;
	this.dt = null ;
	this.vars = {} ;
	this.init();
}
PNormalDomain.prototype = {
	//init 
	
	init : function(){
		var tb = new PA.TToolBar({
			container:"__top",
			title:lang.domain,
			buttons:[
			{
				label:_lang.create,
				link :'javascript:pes.createPage()',
				icon: pageVars.base+'/static/laputa/images/icons/add.gif'
			},
			
			{
				label:_lang.remove,
				link :'javascript:pes.remove()',
				icon: pageVars.base+'/static/laputa/images/icons/delete.gif',
				css:'forList'
			},
			{
				label:_lang.hiddenCondition,
				link :'javascript:pes.showCondition()',
				icon: pageVars.base+'/static/laputa/images/icons/view.gif',
				id:'queryLink',
				css:'forList',
				width:'100px'
			}
			]
		});
		

		$(this.config.addForm).addEvent('submit',function(e){
			if(!check()){
				e.stop();
				return ;
			}
			this.disableBtn(true);
			e.stop();

			PA.ajax.gf( this.vars.action ,$(this.config.addForm).toQueryString(),  function (e){
				this.disableBtn(false);
				if(e.error){ alert (e.message) ; return }
				topSuccess();
				if(this.vars.action!='_create'){
					this.navigateHistory("id", "");
				}
				this.reload();
			}.bind(this));

		}.bind(this));

		$(this.config.searchForm).addEvent('submit',function(e){
			e.stop();
			this.vars.requestParam = "&"+$(this.config.searchForm).toQueryString();
			this.dt.pager.pageIndex = 1 ;
			this.reload();
			//Mbox.close();
		}.bind(this));
		




		this.initViewTable();



		//if(PGlobals.smallScreen()){
			//this.showInput(false);
			//$('backToList').removeClass('h');
			//$('listview').removeClass('rightBorder');
		//}


		

		

		this.showCondition();
		this.reload();

		//c();
	},//init ends


	initViewTable : function(){
		var getRequestParam = function(){
			return this.vars.requestParam;
		}
		var onSelectRow = function(i,tr){
			if(i==-1){
				//this.showData(null);
				this.navigateHistory("id", 'create');
			}else{
				var d = this.dt.rows[i] ;
				this.navigateHistory("id", d.id);
			}
		}
		var cfgs = {
			id :'dt',
			container:'dt',
			height:PGlobals.minusHeight.bind(['conditionDiv','__top']),
			url:"list",
			requestParam:getRequestParam.bind(this),
			selectRow:onSelectRow.bind(this),
			columns: [
				{label:TTable.checkAll,width:24,key:'id',formatter:TTable.formatCheckbox},
				{label:lang.no,width:100,key:'no'},
				{label:lang.name,width:220,key:'name'},
				{label:lang.indexNo,width:60,key:'indexNo'},
				{label:lang.path,width:100,key:'path'}
				
			]
		};

		if(PGlobals.smallScreen()){
			cfgs.selectRow = null ;
			cfgs.intoRow = onSelectRow.bind(this) ;
		}

		this.dt = new TTable(cfgs);		
		
		this.dt.draw();
		
	},
	
	changeState:function(n){
		if(n==""){
			this.showInput(false);
			return ;
		}
		if(n=="create"){
			this.showData(null);
			this.showInput(true);
			$("supply.no").focus();
		}else{
			this.showData({id:n});
			this.showInput(true);
			$("appField.no").focus();
			
		}
	},
	getQueryString : function(){
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
	},
	reload : function(){
		this.dt.reload();
		this.vars.action='_create';
		
		this.dt.selectedRow(null);
		this.showData(null);
		this.updateMode();		
	},
	disableBtn:function (b){
		$('submitBtn').disabled = b; 
		$('resetBtn').disabled = b; 
	},

	showData:function (d){

		if(d==null){
			
			
			$$(".chk").set('checked', false );
			$$(".radio").set('checked', false );
			$$(".radio2").set('checked', false );
			$$(".numberEdit").set('value', '0');
			$("appField.no").value = "";
			$("appField.name").value = "";
			$("appField.indexNo").value = "10";
			$("appField.path").value = "";
			$("appField.comments").value = "";

			this.vars.action='_create';
			this.updateMode();



		}else{

			PA.ajax.gf('get','id='+d.id,function(ed){
				if(ed.error){
					alert(ed.message);
					return false;
				}
				var data =ed.data; 
				$("appField.no").value = PA.utils.defaultStr(data.no);
				$("appField.name").value = PA.utils.defaultStr(data.name);
				$("appField.indexNo").value = data.indexNo;
				$("appField.comments").value = PA.utils.defaultStr(data.comments);
				$("appField.path").value =  PA.utils.defaultStr(data.path);
				
				$("appField.id").value = data.id ;

				this.showInput(true);
				this.vars.action='_update';
				pageVars.id  = d.id;
				this.updateMode();

			}.bind(this));
		}

		
	},
	showInput: function(b){
		
		if(b){
			$("inputPanel").removeClass('h');
			$$(".forList").addClass('h');
		}else{
			$("inputPanel").addClass('h');
			$$(".forList").removeClass('h');		
		}
	},
	incIndexNo: function(){
		
	},
	updateMode : function (){
		
		if(this.vars.action=='_create'){
			$('pageMode').innerHTML = _lang.create;
		}else{
			$('pageMode').innerHTML = _lang.update;
		}
	},
	
	remove : function (){
		var qs = this.getQueryString();
		if(qs == null ) return ;
		if(!confirm(_lang.removeConfirm)){
			return 
		}		
		PA.ajax.gf('remove',qs,function (e){
			if(e.error){
				topHiddenSuccess();
				alert(e.message);
			}else{
				topSuccess();
				this.reload();
			}			
		}.bind(this));
	},
	
	enable : function (b){
		var qs = this.getQueryString();
		if(qs == null ) return ;
		//alert(qs);
		PA.ajax.gf('enable',qs+'&enable='+b,function (e){
			if(e.error){
				alert(e.message);	
			}else{
				topSuccess();
				this.reload();
			}
		}.bind(this));	
	},
	createPage : function(){
		this.navigateHistory("id", 'create');
	},
	backToList : function(){
		this.navigateHistory("id", '');
	},
	showCondition:function(){
		PSinglePage.showCondition.bind(this)();
	},
	mockCheck : function(){
		var e = new Element("div");
		e.inject(document.body);
		e.innerHTML = ("<input type='button' onclick='check()' value='CHECK'/>");
	},
	navigateHistory: function( d , v ) {
		this.changeState(v);
	}
	
}

var pes = null ;
window.addEvent('domready',function(){
	pes = new PNormalDomain({"id":"pes","pageMode":"pageMode","searchForm":"searchForm","addForm":"addForm"});
	//pes.mockCheck();
	//alert('final:'+$("conditionDiv").className);
});


