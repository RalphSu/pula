var PPage = new Class({
		Extends: PBasePage,
		initialize: function (configs) {			
			this.initVars(configs);
			this.vars.parent = [];
			this.init();
		},
	
		init : function(){
			this.initBase({focusField:'sysCategory.no'});
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
				label:_lang.enabled,
				link :'javascript:pes.enable(true)',
				icon: pageVars.base+'/static/laputa/images/icons/unlocked.gif',
				css:'forList'
			},
			{
				label:_lang.disabled,
				link :'javascript:pes.enable(false)',
				icon: pageVars.base+'/static/laputa/images/icons/lockdis.gif',
				css:'forList'
			},
			
			{
				label:_lang.back,
				link :'javascript:pes.backToUpper(false)',
				icon: pageVars.base+'/static/laputa/images/icons/back.gif',
				css:' forList',
				id:'backLink'
			},
			]
		});
			this.initSaveForm();
			this.initSearchForm();
			this.initViewTable();
			this.initHistory();

			//this.showCondition();
			var $this = this; 
			this.reload();
			this.updateState(null);
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

		var formatLink = function(oRecord, oColumn, oData) {
			if(oData&& oData != "" ){
				return "<A href='javascript:pes.into(\""+oData+"\",\""+oRecord['name']+"\")'>"+lang.nextLevel+"</a>";
			}
			return "";
		};

		var cfgs = {
			id :'dt',
			container:'dt',
			height:PGlobals.minusHeight.bind(['__top']),
			url:"list",
			requestParam:getRequestParam.bind(this),
			selectRow:onSelectRow.bind(this),
			columns: [
				{label:TTable.checkAll,width:24,key:'id',formatter:TTable.formatCheckbox},
				{label:lang.no,width:200,key:'no'},
				{label:lang.name,key:'name'},
				{label:lang.indexNo,width:40,key:'indexNo'},
				{label:lang.enabled,width:45,key:'enabled',formatter:TTable.formatEnabled,align:'center'},			
				{label:lang.nextLevel,width:60,key:'id',formatter:formatLink,align:'center'}
				
			]
		};

		if(PGlobals.smallScreen()){
			cfgs.selectRow = null ;
			cfgs.intoRow = onSelectRow.bind(this) ;
		}

		this.dt = new TTable(cfgs);		
		
		this.dt.draw();
		
	},
	pushParent : function (data){
		this.vars.parent.push(data);
	},
	removeLastParent : function(){
		this.vars.parent.erase(this.vars.parent.getLast());
	},
	into:function(id){

		PA.ajax.gf('get','id='+id,function(e){
			if(e.error) {alert (e.message); return ;} 
			this.pushParent( e.data ) ;
			this.updateState( e.data ) ;
			$('searchBtn').click();
		}.bind(this));
		

	},	
	backToUpper:function(){
		this.removeLastParent();
		var d = this.vars.parent.getLast() ;
		this.updateState( d ) ;
		$('searchBtn').click();
	},
	updateState: function(d){
		
		if(d==null){
			$('pid').value = "" ;
			$$("#__top div.t-title")[0].set('html',lang.domain);
			$('backLink').addClass('h');
		}else{
			$('pid').value = d.id ;
			var t = PA.utils.substituteHTML(lang.title,{title:d.name.substring(0,5)});
			$$("#__top div.t-title")[0].set('html',t);
			$('backLink').removeClass('h');
		}

		$("sysCategory.parentId").value = $F("pid");
	},
	

	showData:function (d){

		if(d==null){
			
			
			$$(".chk").set('checked', false );
			$$(".radio").set('checked', false );
			$$(".radio2").set('checked', false );
			$$(".numberEdit").set('value', '0');
			$("sysCategory.no").value = "";
			$("sysCategory.name").value = "";
			$("sysCategory.id").value = "" ;

			this.vars.action='_create';
			this.updateMode();
		}else{

			PA.ajax.gf('get','id='+d.id,function(ed){
				if(ed.error){
					alert(ed.message);
					return false;
				}
				var data =ed.data; 
				$("sysCategory.no").value = PA.utils.defaultStr(data.no);
				$("sysCategory.name").value = PA.utils.defaultStr(data.name);
				$("sysCategory.indexNo").value = PA.utils.defaultStr(data.indexNo);
				
				$("sysCategory.id").value = data.id ;
				$("sysCategory.parentId").value = data.parentId ;

				this.showInput(true);
				this.vars.action='_update';
				pageVars.id  = d.id;
				this.updateMode();
				//this.navigateHistory("id", d.id);

			}.bind(this));
		}

		
	}
	
	
});

var pes = null ;
window.addEvent('domready',function(){
	pes = new PPage({"id":"pes","pageMode":"pageMode","searchForm":"searchForm","addForm":"addForm"});

});


