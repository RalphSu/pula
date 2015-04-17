var PPage = new Class({
		Extends: PBasePage,
		initialize: function (configs) {			
			this.initVars(configs);
			this.init();
		},
	
		init : function(){
			this.initBase({focusField:'material.no'});
			this.initToolBar(true);

			this.initSearchForm();
			this.initViewTable();
			this.initHistory();

			var $this = this;
			this.edtMaterial = new TSuggest({
				field:'condition.materialNo',
				field_id:'cMaterialNo',
				text:'',
				value:'',
				showTips:true,
				tips:_lang.tipsOfSGS,
				url:'../material/suggest',
				container:'sMaterial'
			});	
			
			this.showCondition();


			
			
			this.reload();

			//c();
		},//init ends
		initToolBar : function(){
					var _buttons = [] ;
					_buttons.push(
						{
							label:lang.exportTo,
							link :'javascript:pes.exportFile()',
							icon: pageVars.base+'/static/laputa/images/16X16/export.png',
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
					);
					

					
					var tb = new PA.TToolBar({
						container:'__top',
						title:lang.domain,
						buttons: _buttons
					});


					
				

				},

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
				url:"list",
				requestParam:getRequestParam.bind(this),
				selectRow:onSelectRow.bind(this),
				columns: [
					{label:TTable.checkAll,width:24,key:'id',formatter:TTable.formatCheckbox},
					{label:lang.materialNo,width:160,key:'no'},
					{label:lang.materialName,key:'name'},
					{label:'分支机构',width:140,key:'branchName'},
					{label:lang.quantity,width:120,key:'quantity',align:'right'},
					{label:'单位',width:60,key:'materialUnit',align:'center'}
				]
			};

			if(PGlobals.smallScreen()){
				cfgs.selectRow = null ;
				cfgs.intoRow = onSelectRow.bind(this) ;
			}

			this.dt = new TTable(cfgs);		
			
			this.dt.draw();
			
		},
		
		loadSpace : function(_callback){
			var $this = this ;
			var wid= $('warehouseList').value;
			//var shouldReload =false;
			if( !this.vars.warehouseId || ( this.vars.warehouseId && this.vars.warehouseId != wid ) ){
				//shouldReload = true ;
			}else{
				return ;
			}

			if(isEmpty(wid)){
				wid= "0";
			}

			//ajax
			PA.ajax.gf(pageVars.base+'/app/warehousespace/listByWarehouse',
				'warehouseId='+ wid,
				function( e ) {
					if(e.error){ alert (e.message) ; return ;} 
					//load
					$this.vars.spaceList = e.data ;
					_callback();
				}
			);


		},
		loadSpaceToSelect : function( sel ) {
			var $this = this ;
			if(!$this.vars.spaceList){
				return ;
			}

			var _loadSpaceToSelect = function ( obj ) {
				PA.utils.clearSelect( obj ) ;

				var opt = new Element('option',{   
					 text: lang.all,
					 value: '0'
				});
				obj.adopt(opt);
				$this.vars.spaceList.each ( function(el){
						var opt = new Element('option',{   
							 text: el.name,
							 value: el.id
						});
						obj.adopt(opt);
				});
			}
			if(sel ){
				//load from remote ?
				//local data ;
				_loadSpaceToSelect( $(sel) );
			}else{
				$$(".selectSpace").each ( function( ss ) {
					_loadSpaceToSelect( $(ss) );
				});
			}

		},
		exportFile: function(){
			window.open( 'export?'+$(this.config.searchForm).toQueryString());
		},
		updateMode: function(){},
		showData:function (d){}		
		
});

var pes = null ;
window.addEvent('domready',function(){
	pes = new PPage({"id":"pes","pageMode":"pageMode","searchForm":"searchForm","addForm":"addForm"});
	
});


