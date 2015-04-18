var PPage = new Class({
		Extends: PBasePage,
		initialize: function (configs) {			
			this.initVars(configs);
			this.init();
		},
	
		init : function(){
			this.initBase({focusField:'materialNo'});
			this.initToolBar(false);
			this.initSaveForm();
			this.initSearchForm();
			this.initViewTable();
			this.initHistory();

			
			this.showCondition();
			this.edtMaterialC = new TSuggest({
				field:'condition.materialNo',
				field_id:'cMaterialNo',
				text:'',
				value:'',
				showTips:true,
				tips:_lang.tipsOfTSN,
				url:'../material/suggest',
				container:'scMaterialNo'
			});	
			this.edtMaterial = new TSuggest({
				field:'materialRequire.materialNo',
				field_id:'materialNo',
				text:'',
				value:'',
				showTips:true,
				tips:_lang.tipsOfTSN,
				url:'../material/suggest',
				container:'sMaterialNo',
				callback : this.showUnit.bind(this)
			});	

			loadCalCss('.dateField');

			var $this = this;

			
			this.reload();

			//c();
		},//init ends
		showUnit : function(data){
			$('spanUnit').set('html',PA.utils.escapeHTML(data[2]));
		},
		initToolBar : function(){
			var _buttons = [
					{
						label:_lang.create,
						link :'javascript:pes.createPage()',
						icon: pageVars.base+'/static/laputa/images/icons/add.gif'
					},
					
					
						{
						label:'提交选中',
						link :'javascript:pes.submitSelected()',
						icon: pageVars.base+'/static/laputa/images/16X16/apply.png',width:'90px',
					css:'forList'
					},
					
				{
					label:'删除',
					link :'javascript:pes.remove()',
					icon: pageVars.base+'/static/laputa/images/icons/delete.gif',
					css:'forList'
				},{
					label:_lang.hiddenCondition,
					link :'javascript:pes.showCondition()',
					icon: pageVars.base+'/static/laputa/images/icons/view.gif',
					id:'queryLink',
					css:'forList',
					width:'100px'
				}
				];
			
			//
			
			var tb = new PA.TToolBar({
				container:$('__top'),
				title:lang.domain,
				buttons:_buttons
			});
		},
		submitSelected : function(){
			var qs = this.getQueryString();
			if(qs == null ) return ;
			//alert(qs);
			PA.ajax.gf('_submit?_json=1',qs,function (e){
				if(e.error){
					alert(e.message);	
				}else{
					this.reload();
				}
			}.bind(this));	
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
					{label:'ID',width:80,key:'id'},
					{label:'材料编号',key:'materialNo',width:160},
					{label:'材料名称',key:'materialName'},
					{label:'数量',key:'quantity',width:100},
					{label:'单位',key:'materialUnit',width:60,align:'center'},
					{label:'状态',width:55,key:'statusName',align:'center'},
					{label:'填写时间',width:120,key:'createdTime',formatter:TTable.formatDateTimeLite},
					{label:'提交时间',width:120,key:'submitTime',formatter:TTable.formatDateTimeLite}
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
			$('tblView').addClass('h');
			$('addForm').addClass('h');

			$('spanUnit').set('html','');

			if(d==null){
				this.edtMaterial.reset();
				this.vars.action='_create';
				$('addForm').removeClass('h');
				this.updateMode();

			}else{

				PA.ajax.gf('get','id='+d.id,function(ed){
					if(ed.error){
						alert(ed.message);
						return false;
					}
					var data =ed.data; 
					
					if(data.status == 1 ) {
						$('addForm').removeClass('h');
						for( var k in data ) {
							if($('materialRequire.'+k)){
								$('materialRequire.'+k).value = PA.utils.defaultStr(data[k]);
							}
						}
						
						this.edtMaterial.setValue( data.materialNo,data.materialName);
						$('spanUnit').set('html',PA.utils.escapeHTML(data.materialUnit));

					}else{
						$('tblView').removeClass('h');
						$$("#tblView span").set('html','');
						PGlobals.initSpanFromData( data ) ;
					}

					this.showInput(true);
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


