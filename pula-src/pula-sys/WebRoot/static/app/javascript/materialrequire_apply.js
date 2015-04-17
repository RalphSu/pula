var PPage = new Class({
		Extends: PBasePage,
		initialize: function (configs) {			
			this.initVars(configs);
			this.init();
		},
	
		init : function(){
			this.initBase({focusField:'quantity'});
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

			loadCalCss('.dateField');

			var $this = this;

			$('rejectBtn').addEvent('click', function(){
				if(!checkReject()){
					return ;
				}
				//数量不care
				$this.disableBtn(false);
				PA.ajax.gf('_reject',$('addForm').toQueryString(),function(ed){
					$this.disableBtn(true);
					if(ed.error){ alert(ed.message) ;return }
					$this.reload();
					topSuccess();
					//back
					$this.navigateHistory("id", "");
				});

			});
			
			this.reload();

			//c();
		},//init ends
		initToolBar : function(){
			var _buttons = [
					
					
				{
						label:'受理',
						link :'javascript:pes.submitSelected()',
						icon: pageVars.base+'/static/laputa/images/16X16/apply.png',
					css:'forList'
					},
					
				{
					label:'拒绝',
					link :'javascript:pes.cancel()',
					icon: pageVars.base+'/static/laputa/images/16X16/cancel.png',
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
		cancel : function(){
			var qs = this.getQueryString();
			if(qs == null ) return ;
			//alert(qs);
			PA.ajax.gf('_reject',qs,function (e){
				if(e.error){
					alert(e.message);	
				}else{
					this.reload();
				}
			}.bind(this));	
		},
		submitSelected : function(){
			var qs = this.getQueryString();
			if(qs == null ) return ;
			//alert(qs);
			PA.ajax.gf('_apply',qs,function (e){
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
				url:"list4Apply",
				requestParam:getRequestParam.bind(this),
				selectRow:onSelectRow.bind(this),
				columns: [
					{label:TTable.checkAll,width:24,key:'id',formatter:TTable.formatCheckbox},
					{label:'ID',width:80,key:'id'},
					{label:'材料编号',key:'materialNo',width:160},
					{label:'材料名称',key:'materialName'},
					{label:'分支机构',key:'branchName',width:120},
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
		
		disabledBtn: function(b){
			$('submitBtn').disabled = b;
			$('rejectBtn').disabled = b;		
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
					
					
				
					$$("#addForm span").set('html','');
					PGlobals.initSpanFromData( data ) ;
					$('materialRequire.id').set('value',d.id);
					$('quantity').set('value',data.quantity);

					this.vars.action='_apply';
					pageVars.id  = d.id;
					this.updateMode();
					
					
				}.bind(this));
			}

			
		},updateMode:function(){}
		
		
});

var pes = null ;
window.addEvent('domready',function(){
	pes = new PPage({"id":"pes","pageMode":"pageMode","searchForm":"searchForm","addForm":"addForm"});
	//pes.mockCheck();
	//alert('final:'+$("conditionDiv").className);
});


