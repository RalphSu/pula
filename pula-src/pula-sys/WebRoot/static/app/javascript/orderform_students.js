var PPage = new Class({
		Extends: PBasePage,
		initialize: function (configs) {			
			this.initVars(configs);
			this.init();
		},
	
		init : function(){
			this.initBase({focusField:'student.no'});
			this.initToolBar({condition:true});
			this.initSearchForm();
			this.initViewTable();
			this.initHistory();

			
			this.showCondition();
			this.reload();
			

			var $this = this ;

			loadCalCss(".dateField");

			this.edtStudent = new TSimpleNo({
				field:'condition.studentNo',
				field_id:'cStudentNo',
				text:'',
				value:'',
				showTips:true,
				tips:lang.tipsOfTSN,
				url:'../student/find',
				container:'sStudent'
			});	

				
			//c();
		},//init ends
		initViewTable : function(){
			var getRequestParam = function(){
				return this.vars.requestParam;
			}
			var onSelectRow = function(i,tr){
				/*if(i==-1){
					this.navigateHistory("id", 'create');
				}else{
					var data = this.dt.rows[i] ;
					//this.showData(data);
					//this.navigateHistory("id", ''+data.id);
					if( data.status == pageConsts.S_INPUT){ //input
						this.updatePage(data.id);
					}else{
						this.view(data.id);
					}
				}*/
			}
			var cfgs = {
				id :'dt',
				container:'dt',
				height:PGlobals.minusHeight.bind(['conditionDiv','__top']),
				url:"list4students",
				requestParam:getRequestParam.bind(this),
				selectRow:onSelectRow.bind(this),
				columns: [
					{label:TTable.checkAll,width:24,key:'id',formatter:TTable.formatCheckbox},
					{label:'订单号',width:100,key:'no',align:'center'},
					{label:lang.branchName,width:180,key:'branchName'},			
					{label:'学员编号',width:80,key:'studentNo'},		
					{label:'学员名称',width:80,key:'studentName'},		
					{label:'课程产品',key:'courseProductName'},		
					{label:'课程数',key:'courseCount',width:40},		
					{label:'已上课程数',key:'consumeCourseCount',width:80},		
					{label:'订单日期',width:100,key:'createdTime',formatter:TTable.formatDate}
				]
			};

			if(PGlobals.smallScreen()){
				cfgs.selectRow = null ;
				cfgs.intoRow = onSelectRow.bind(this) ;
			}

			this.dt = new TTable(cfgs);		
			
			this.dt.draw();
			
		},
		
	disableBtn:function (b){
		$('submitBtn').disabled = b; 
	},
	
	createPage : function(b){
		
        var url = encodeURIComponent('../orderform/create');
        centerWindow('../my/window?u=' + url,850, 380);
	},
	updatePage : function(id ){
		var url = encodeURIComponent('../orderform/update?id='+id);
		        centerWindow('../my/window?u=' + url,850, 380);
	},view : function(id ){
		var url = encodeURIComponent('../orderform/view?id='+id);
		        centerWindow('../my/window?u=' + url,950, 380);
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
