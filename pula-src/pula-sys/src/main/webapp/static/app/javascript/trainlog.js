var PPage = new Class({
		Extends: PBasePage,
		initialize: function (configs) {			
			this.initVars(configs);
			this.init();
		},
	
		init : function(){
			this.initBase({focusField:'student.no'});
			this.initToolBar(true);
			this.initSearchForm();
			this.initViewTable();
			this.initHistory();

			
			this.showCondition();
			this.reload();
			

			var $this = this ;

			loadCalCss(".dateField");
			

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
					this.updatePage(data.id);
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
					{label:'培训日期',width:100,key:'trainDate',align:'center',formatter:TTable.formatDate},
					{label:lang.branchName,width:180,key:'branchName'},			
					{label:'培训地点',width:80,key:'location'},		
					{label:'培训内容',key:'content'},		
					{label:'培训师',width:100,key:'trainer'},
					{label:'填写人',width:90,key:'creatorName'},
					{label:'填写时间',width:160,key:'createdTime',formatter:TTable.formatDateTime},
					{label:lang.view,width:40,key:'id',formatter:TTable.formatLinkJs.bind({func:'pes.view',label:lang.view}),align:'center'}
				]
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
	
	createPage : function(b){
		
        var url = encodeURIComponent('../trainlog/create');
        centerWindow('../my/window?u=' + url,850, 480);
	},
	updatePage : function(id ){
		var url = encodeURIComponent('../trainlog/update?id='+id);
		        centerWindow('../my/window?u=' + url,850, 480);
	},view : function(id ){
		var url = encodeURIComponent('../trainlog/view?id='+id);
		        centerWindow('../my/window?u=' + url,950, 480);
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
