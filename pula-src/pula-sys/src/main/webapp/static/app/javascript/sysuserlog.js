
var PPage = new Class({
		Extends: PBasePage,
		initialize: function (configs) {			
			this.initVars(configs);
			this.init();
		},
	
		init : function(){
		var tb = new PA.TToolBar({
			container:"__top",
			title:lang.domain,
			buttons:[
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


		this.initSearchForm();
		this.initViewTable();

		this.reload();
		this.showCondition();

		loadCal('beginDate','endDate');		

	},
	initViewTable: function(){
		var getRequestParam = function(){
			return this.vars.requestParam;
		}
		var onSelectRow = function(i,tr){
			if(i==-1){
				this.showData(null);
			}else{
				var data = this.dt.rows[i] ;
				this.showData(data);
			}
		}
		

		this.dt = new TTable({
			id :'dt',
			container:'dt',
			height:PGlobals.minusHeight.bind(['conditionDiv','__top']),
			url:"list",
			requestParam:getRequestParam.bind(this),
			columns: [
				{label:lang.eventTime,width:150,key:'eventTime'},
				{label:lang.actor,width:200,key:'name'},
				{label:lang.ip,width:180,key:'ip'},
				{label:lang.event,width:120,key:'event'},
				{label:lang.extendInfo,key:'extendInfo'}
			]
		});		
		
		this.dt.draw();		
	},	
	reload : function(){
		this.dt.reload();
	}
});
var pes = null ;
window.addEvent('domready',function(){
	pes = new PPage({"id":"pes","searchForm":"searchForm"});
	//pes.mockCheck();
});


