var PPage = new Class({
		Extends: PBasePage,
		initialize: function (configs) {			
			this.initVars(configs);
			this.init();
		},
	
		init : function(){
			this.initBase({focusField:'form.no'});
			this.initToolBar();
			this.initSearchForm();
			this.initViewTable();
			this.initHistory();

			
			this.showCondition();
			this.reload();


			

			//c();
		},//init ends
		initToolBar:  function(){


			var tb = new PA.TToolBar({
			container:"__top",
			title:lang.domain,
			buttons:[

					{
						label:_lang.remove,
						link :'javascript:pes.remove()',
						icon: pageVars.base+'/static/laputa/images/icons/delete.gif',
						css:'forList'
					},{
						label:_lang.enabled,
						link :'javascript:pes.enable(true)',
						icon: pageVars.base+'/static/laputa/images/icons/unlocked.gif',
						css:'forList'
					},{
						label:_lang.disabled,
						link :'javascript:pes.enable(false)',
						icon: pageVars.base+'/static/laputa/images/icons/lockdis.gif',
						css:'forList'
					},
					{
						label:lang.importTo,
						link :'javascript:pes.importXls()',
						icon: pageVars.base+'/static/laputa/images/icons/import.gif',
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
				
		},
			importXls : function(){
				Mbox.open({
				url: "pnl_upload",
				title:lang.upload
				});
		},

		initViewTable : function(){
			var getRequestParam = function(){
				return this.vars.requestParam;
			}
			var onSelectRow = function(i,tr){
				if(i==-1){
					//this.navigateHistory("id", 'create');
				}else{
					var data = this.dt.rows[i] ;
					//this.showData(data);
					//this.navigateHistory("id", ''+data.id);
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
					{label:lang.no,width:160,key:'no'},
					{label:lang.mac,key:'mac',width:180},
					{label:lang.enabled,width:33,key:'enabled',formatter:TTable.formatEnabled},
					{label:lang.status,width:33,key:'statusName'},
					{label:lang.comments,key:'comments'}
					
				]
			};

			if(PGlobals.smallScreen()){
				cfgs.selectRow = null ;
				cfgs.intoRow = onSelectRow.bind(this) ;
			}

			this.dt = new TTable(cfgs);		
			
			this.dt.draw();
			
		},
		
		

		showData:function (d){

			

			
		},updateMode:function(){}
		
		
});

var pes = null ;
window.addEvent('domready',function(){
	pes = new PPage({"id":"pes","pageMode":"pageMode","searchForm":"searchForm","addForm":"addForm"});
	//pes.mockCheck();
	//alert('final:'+$("conditionDiv").className);
});



