var PPage = new Class({
		Extends: PBasePage,
		initialize: function (configs) {			
			this.initVars(configs);
			this.init();
		},
	
		init : function(){
			this.initBase();
			this.initToolBar({condition:true});
			this.initSearchForm();
			this.initViewTable();
			this.initHistory();

			
			this.showCondition();
			this.reload();
			

			var $this = this ;

			loadCalCss(".dateField");

			this.edtSalesmanC = new TSimpleNo({
				field:'condition.salesmanNo',
				field_id:'cSalesmanNo',
				text:'',
				value:'',
				showTips:true,
				tips:lang.tipsOfTSN,
				url:'../salesman/find',
				container:'sSalesman'
			});	
			

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
					//this.updatePage(data.id);
				}
			}

			var _cols = [{label:'学生姓名',width:60,key:'student'},
					{label:'年龄',width:50,key:'age',align:'center'},
					{label:'电话',width:100,key:'phone'},
					{label:'家长',width:100,key:'parent'},
					{label:'内容',width:100,key:'content'}];
			if( pageVars.hq ){
				_cols.push( {label:lang.branchName,width:180,key:'branchName'});
			}
			
			_cols.push(
					{label:'预约1',width:80,key:'plan1'},
					{label:'预约2',width:80,key:'plan2'},
					{label:'预约3',width:80,key:'plan3'},
					{label:'预约4',width:80,key:'plan4'},
					{label:'预约5',width:80,key:'plan5'},
					{label:'结果',width:80,key:'resultName',align:'center'},
					{label:'备注',key:'comments'},
					{label:'填写人',width:90,key:'ownerName'},
					{label:'填写时间',width:160,key:'createdTime',formatter:TTable.formatDateTime}
				);

		
			var cfgs = {
				id :'dt',
				container:'dt',
				height:PGlobals.minusHeight.bind(['conditionDiv','__top']),
				url:"list",
				requestParam:getRequestParam.bind(this),
				selectRow:onSelectRow.bind(this),
				columns: _cols
			};

			if(PGlobals.smallScreen()){
				cfgs.selectRow = null ;
				cfgs.intoRow = onSelectRow.bind(this) ;
			}

			this.dt = new TTable(cfgs);		
			
			this.dt.draw();
			
		},
		
		showFile: function(id) {
        var url = encodeURIComponent('../student/view?id=' + id);
        centerWindow('../my/window?u=' + url,650, 580);
    },
	disableBtn:function (b){
		$('submitBtn').disabled = b; 
	},

		showData:function (d){

			

			
		},updateMode:function(){}
		
		
});

var pes = null ;
window.addEvent('domready',function(){
	pes = new PPage({"id":"pes","pageMode":"pageMode","searchForm":"searchForm","addForm":"addForm"});
});

var reloadData = function (){
	pes.reload();
}
