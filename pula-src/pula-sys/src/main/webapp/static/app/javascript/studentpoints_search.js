var PPage = new Class({
		Extends: PBasePage,
		initialize: function (configs) {			
			this.initVars(configs);
			this.init();
		},
	
		init : function(){
			this.initBase({focusField:'agent.no'});
			this.initToolBarHere();
			this.initSearchForm();
			this.initViewTable();
			this.initHistory();

		
			this.showCondition();
			this.reload();


			
			this.edtUser_condition = new TSimpleNo({
				field:'condition.loginId',
				field_id:'cUserNo',
				text:'',
				value:'',
				showTips:true,
				tips:_lang.tipsOfTSN,
				url:'../student/find',
				container:'conditionUserNo'
			});	

			
			loadCal('beginDate','endDate');

			var $this = this ;
			

			//c();
		},//init ends
	initSearchForm: function(){
		
		$(this.config.searchForm).addEvent('submit',function(e){
			e.stop();
			this.vars.requestParam = "&"+$(this.config.searchForm).toQueryString();
			this.dt.pager.pageIndex = 1 ;
			this.reload();
			//Mbox.close();
			$('backBtn').addClass('h');
		}.bind(this));
		this.vars.requestParam = "&"+$(this.config.searchForm).toQueryString();
		
	},
	initToolBarHere:  function(b){
		

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
	},
		initViewTable : function(){
			var getRequestParam = function(){
				return this.vars.requestParam;
			}
			var onSelectRow = function(i,tr){
				
			}

			var formatOwner = function( oRecord, oColumn, oData) {

					return "<a href='#' onclick='javascript:pes.showMe(\""+oRecord.ownerNo+"\")'>"+oRecord.ownerNo +" "+ oRecord.ownerName +"</a>";
			};
			var cfgs = {
				id :'dt',
				container:'dt',
				height:PGlobals.minusHeight.bind(['conditionDiv','__top']),
				url:"list",
				requestParam:getRequestParam.bind(this),
				selectRow:onSelectRow.bind(this),
				columns: [
					{label:lang.createdTime,width:150,key:'createdTime'},
					{label:lang.points,width:120,key:'points',align:'right'},
					{label:lang.owner,width:300,key:'owner',formatter:formatOwner},
					{label:lang.admin,width:80,key:'admin'},
					{label:lang.type,width:40,key:'type'},
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
		
		
		showMe:function(id){			
			this.vars.condition = this.conditionObject();
			//$('searchForm').reset();
			this.edtUser_condition.setValue(id,'');
			$('searchBtn').click();
			$('backBtn').removeClass('h');
		},
		showData:function (d){

			

			
		},updateMode: function(){

		},
		conditionObject:function(){
			var obj = {};

			obj.cUserNo = $F('cUserNo') ;
			obj.conditionType = $F('conditionType') ;
			obj.beginDate = $F('beginDate') ;
			obj.endDate = $F('endDate') ;
			obj.conditionKeywords = $F('conditionKeywords') ;

			return obj ;
		},
		restoreCondition : function(obj){
			for(k in obj ) {
				$(k).value = obj[k];
			}
		}
		
		
});

var pes = null ;
window.addEvent('domready',function(){
	pes = new PPage({"id":"pes","pageMode":"pageMode","searchForm":"searchForm","addForm":"addForm"});
	//pes.mockCheck();
	//alert('final:'+$("conditionDiv").className);
});


