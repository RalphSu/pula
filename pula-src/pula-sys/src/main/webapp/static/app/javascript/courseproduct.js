var PPage = new Class({
		Extends: PBasePage,
		initialize: function (configs) {			
			this.initVars(configs);
			this.init();
		},
	
		init : function(){
			this.initBase({focusField:'courseProduct.no'});
			this.initToolBar(false);
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
					{label:lang.no,width:200,key:'no'},
					{label:lang.name,key:'name'},
					{label:lang.courseCount,width:100,key:'courseCount',align:'right'},
					{label:lang.price,width:100,key:'price',align:'right'},
					{label:lang.beginTime,width:120,key:'beginTime',formatter:TTable.formatDate},
					{label:lang.endTime,width:120,key:'endTime',formatter:TTable.formatDate},
					{label:lang.branchName,width:120,key:'branchName'},
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
						if($('courseProduct.'+k)){
							$('courseProduct.'+k).value = PA.utils.defaultStr(data[k]);
						}
					}

					if( data.beginTime) {
						$('courseProduct.beginTimeText').value = new Date( data.beginTime).format('yyyy-MM-dd');
					}
					if( data.endTime) {
						$('courseProduct.endTimeText').value = new Date( data.endTime).format('yyyy-MM-dd');
					}					
					
					this.showInput(true);
					this.vars.action='_update';
					pageVars.id  = d.id;
					this.updateMode();
					
					var $this = this ;
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


