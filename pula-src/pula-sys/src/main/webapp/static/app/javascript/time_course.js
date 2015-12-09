var PPage = new Class({
		Extends: PBasePage,
		initialize: function (configs) {			
			this.initVars(configs);
			this.init();
		},
	
		init : function(){
			this.initBase({focusField:'course.no'});
			this.initToolBar({add:true,remove:true,condition:true});
			this.initSaveForm();
			this.initSearchForm();
			this.initViewTable();
			this.initHistory();
			
			this.showCondition();
			
			loadCalCss('.dateField');

			var $this = this;

			this.reload();

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
					{label:lang.no,width:200,key:'no'},
					{label:lang.name,key:'name'},
					{label:lang.branchName, width:200, key:'branchName'},
					{label:lang.classRoomName,width:200, key:'classRoomName'},
//					{label:lang.publishTime,width:120,key:'startTime',formatter:TTable.formatDate},
//					{label:lang.expiredTime,width:120,key:'endTime',formatter:TTable.formatDate},
					{label:lang.status,width:40,key:'enabled',formatter:TTable.formatEnabled}
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
		},
		
		createPage : function(b){
	        var url = encodeURIComponent('../timecourse/create');
	        centerWindow('../my/window?u=' + url,1100, 480);
		},
		updatePage : function(id ){
			var url = encodeURIComponent('../timecourse/update?id='+id);
			centerWindow('../my/window?u=' + url,1100, 480);
		},view : function(id ){
			var url = encodeURIComponent('../timecourse/view?id='+id);
			centerWindow('../my/window?u=' + url,1100, 480);
		}
		
});

var WORKER_TPL = "<a href='javascript:pes.eraseWorker(\"{no}\")'>{no} {name}<input type='hidden' name='course.courseNos' value='{no}'/></a>";
var pes = null ;
window.addEvent('domready',function(){
	pes = new PPage({"id":"pes","pageMode":"pageMode","searchForm":"searchForm","addForm":"addForm"});
	//pes.mockCheck();
	//alert('final:'+$("conditionDiv").className);
});


