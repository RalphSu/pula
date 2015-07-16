var PPage = new Class({
		Extends: PBasePage,
		initialize: function (configs) {			
			this.initVars(configs);
			this.init();
		},
	
		init : function(){
			this.initBase({focusField:'course.no'});
			this.initToolBar({add:true,remove:true,condition:false});
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
					this.navigateHistory("id", ''+data.id);
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
					{label:lang.no,width:150,key:'no'},
					{label:lang.usedCost,width:200,key:'usedCost'},
					{label:lang.usedCount,width:200,key:'usedCount'},
					{label:lang.courseNo,width:150,key:'courseNo'},
					{label:lang.orderNo,width:150,key:'orderNo'},
					{label:lang.studentNo,width:200,key:'studentNo'},
					{label:lang.updateTime,width:150, key:'updateTime'},
					{label:lang.updator,width:150, key:'updator'},
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

			$('addForm').reset();
			if(d==null){
				this.vars.action='_create';
				this.updateMode();
			}else{

				PA.ajax.gf('get','id='+d.id,function(ed){
					if(ed.error){
						alert(ed.message);
						return false;
					}
					var data =ed.data; 

					var data =ed.data; 
					for( var k in data ) {
						if($('course.'+k)){
							$('course.'+k).value = PA.utils.defaultStr(data[k]);
						}
					}

					if( data.startTime) {
						$('course.startTimeText').value =  data.startTime;// new Date( ).format('yyyy-MM-dd');
					}
					if( data.endTime) {
						$('course.endTimeText').value = data.endTime; // ).format('yyyy-MM-dd');
					}

					this.showInput(true);
					this.vars.action='_update';
					pageVars.id  = d.id;
					this.updateMode();
					
				}.bind(this));
			}

			
		}
		
		
});

var WORKER_TPL = "<a href='javascript:pes.eraseWorker(\"{no}\")'>{no} {name}<input type='hidden' name='course.courseNos' value='{no}'/></a>";
var pes = null ;
window.addEvent('domready',function(){
	pes = new PPage({"id":"pes","pageMode":"pageMode","searchForm":"searchForm","addForm":"addForm"});
	//pes.mockCheck();
	//alert('final:'+$("conditionDiv").className);
});


