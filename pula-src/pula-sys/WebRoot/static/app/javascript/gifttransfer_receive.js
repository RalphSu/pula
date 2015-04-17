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
			this.edtGiftC = new TSuggest({
				field:'condition.giftNo',
				field_id:'cGiftNo',
				text:'',
				value:'',
				showTips:true,
				tips:_lang.tipsOfTSN,
				url:'../gift/suggest',
				container:'scGiftNo'
			});	

			loadCalCss('.dateField');

			var $this = this;
			
			
			
			this.reload();

			//c();
		},//init ends
		initToolBar : function(){
			var _buttons = [
					
					
				{
						label:'批量签收',
						link :'javascript:pes.recvSelected()',
						icon: pageVars.base+'/static/laputa/images/16X16/apply.png',
					css:'forList',width:'90px'
					},
					
				{
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
		recvSelected : function(){
			var qs = this.getQueryString();
			if(qs == null ) return ;
			//alert(qs);
			PA.ajax.gf('_receive',qs,function (e){
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
				url:"list4Receive",
				requestParam:getRequestParam.bind(this),
				selectRow:onSelectRow.bind(this),
				columns: [
					{label:TTable.checkAll,width:24,key:'id',formatter:TTable.formatCheckbox},
					{label:'ID',width:80,key:'id'},
					{label:'礼品编号',key:'giftNo',width:160},
					{label:'礼品名称',key:'giftName'},
					{label:'分支机构',key:'branchName',width:120},
					{label:'发出数量',key:'sentQuantity',width:100},
					{label:'单位',key:'giftUnit',width:60,align:'center'},
					{label:'状态',width:55,key:'statusName',align:'center'},
					{label:'发出时间',width:120,key:'sentTime',formatter:TTable.formatDateTimeLite}
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



			var $this = this ;
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
					$('tblView').removeClass('h');
					$$("#tblView span").set('html','');
					PGlobals.initSpanFromData( data ) ;

					$this.vars.action='_receive';
					$('giftRequire.id').set('value',data.id);
					$('quantity').set('value',data.sentQuantity);
				});
				
			}

			
		},updateMode:function(){}
		
		
});

var pes = null ;
window.addEvent('domready',function(){
	pes = new PPage({"id":"pes","pageMode":"pageMode","searchForm":"searchForm","addForm":"addForm"});
	//pes.mockCheck();
	//alert('final:'+$("conditionDiv").className);
});


