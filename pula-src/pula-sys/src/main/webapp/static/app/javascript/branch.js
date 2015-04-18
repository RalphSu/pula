var PPage = new Class({
		Extends: PBasePage,
		initialize: function (configs) {			
			this.initVars(configs);
			this.init();
		},
	
		init : function(){
			this.initBase({focusField:'form.no'});
			this.initToolBar();
			this.initSaveForm();
			this.initSearchForm();
			this.initViewTable();
			this.initHistory();

			
			this.showCondition();
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
					{label:lang.no,width:100,key:'no'},
					{label:lang.name,key:'name'},
					{label:lang.linkman,width:100,key:'linkman'},
					{label:lang.phone,width:120,key:'phone'},
					{label:lang.email,width:320,key:'email',formatter:TTable.formatEmail},
					{label:lang.prefix,width:120,key:'prefix'},
					{label:lang.status,width:33,key:'enabled',formatter:TTable.formatEnabled}
					
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
			$('spnHeadquarter').addClass('h');
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
					for( var k in data ) {
						if($('form.'+k)){
							var ctn = PA.utils.defaultStr(data[k]);
							$('form.'+k).set('value', ctn);
						}
					}
					$('showInWeb').checked = data.showInWeb;
					if(data.headquarter){
						$('spnHeadquarter').removeClass('h');
					}

					this.vars.action='_update';
					pageVars.id  = d.id;
					this.updateMode();
					

				}.bind(this));
			}

			
		}
		
		
});

var pes = null ;
window.addEvent('domready',function(){
	pes = new PPage({"id":"pes","pageMode":"pageMode","searchForm":"searchForm","addForm":"addForm"});
	//pes.mockCheck();
	//alert('final:'+$("conditionDiv").className);
});


