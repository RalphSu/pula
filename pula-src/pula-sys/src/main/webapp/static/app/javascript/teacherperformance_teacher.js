var PPage = new Class({
		Extends: PBasePage,
		initialize: function (configs) {			
			this.initVars(configs);
			this.init();
		},
	
		init : function(){
			this.initBase();
			
			this.initSearchForm();
			this.initViewTable();
			this.initHistory();

			
			this.showCondition();
			

			loadCalCss('.dateField');

			var $this = this;

			
			this.reload();

			//window.top.resizeTo(850,600);

			//c();
		},//init ends


		initViewTable : function(){
			var getRequestParam = function(){
				return this.vars.requestParam;
			}
			var onSelectRow = function(i,tr){
				if(i==-1){
					this.navigateHistory("id", 'create');
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
				url:"list4teacher",
				requestParam:getRequestParam.bind(this),
				selectRow:onSelectRow.bind(this),
				columns: [
					
					
					{label:'年月',width:80,key:'month',align:'center'},
					{label:'分校',key:'branchName',width:100},
					{label:'课时',key:'courseCount',width:80,align:'center'},
					{label:'工作日',key:'workdays',width:80,align:'center'},
					{label:'实际工作日',key:'factWorkDays',width:80,align:'center'},
					{label:'迟到',key:'later',width:80,align:'center'},
					{label:'早退',key:'earlier',width:80,align:'center'},
					{label:'请假',key:'leave',width:80,align:'center'},
					{label:'综合',key:'complex',width:80,align:'center'},
					{label:'绩效',key:'performance',width:80,align:'center'},
					{label:'订单数',key:'orders',width:80,align:'center'},
					{label:'退单数',key:'chargebacks',width:80,align:'center'}
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

var WORKER_TPL = "<a href='javascript:pes.eraseWorker(\"{no}\")'>{no} {name}<input type='hidden' name='course.courseNos' value='{no}'/></a>";
var pes = null ;
window.addEvent('domready',function(){
	pes = new PPage({"id":"pes","pageMode":"pageMode","searchForm":"searchForm","addForm":"addForm"});
	//pes.mockCheck();
	//alert('final:'+$("conditionDiv").className);
});


