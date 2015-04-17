var PPage = new Class({
		Extends: PBasePage,
		initialize: function (configs) {			
			this.initVars(configs);
			this.init();
		},
	
		init : function(){
			this.initBase({focusField:'agent.no'});
			this.initToolBarHere();
			this.initSaveForm();
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

			var edt = new TSimpleNo({
				field:'studentId',
				field_id:'studentId',
				text:'',
				value:'',
				showTips:true,
				tips:_lang.tipsOfTSN,
				url:'../student/find',
				container:'sUser'
			});
			var edt2 = new TSimpleNo({
				field:'studentId',
				field_id:'studentId_cost',
				text:'',
				value:'',
				showTips:true,
				tips:_lang.tipsOfTSN,
				url:'../student/find',
				container:'sUser2'
			});			

			this.edtGift = new TSuggest({
				field:'giftNo',
				field_id:'cGiftNo',
				text:'',
				value:'',
				showTips:true,
				tips:_lang.tipsOfSGS,
				url:'../gift/suggest',
				container:'sGift'
			});	
			loadCal('beginDate','endDate');

			var $this = this ;
			$('costForm').addEvent('submit',function(e){
				e.stop();
				if(!checkCost()){
					return ;
				}
				$('submitBtn2').disabled = true;
				PA.ajax.gf( '_consume' ,$('costForm').toQueryString(),  function (e){
					$('submitBtn2').disabled = false;
					if(e.error){ alert (e.message) ; return }
					topSuccess();
					$this.reload();
					
				}.bind(this));

			});

			$('backBtn').addEvent("click",function(){
				$this.restoreCondition( $this.vars.condition ) ;
				$('searchBtn').click();
				$('backBtn').addClass('h');
			});

			$('cGiftNo').addEvent('blur',function(){
				pageVars.points = 0 ;
				var no = $('cGiftNo').value;
				if(pageVars.giftNo== no){
					$this.calcPoints();
					pageVars.giftNo = no;
					return ;
				}
				PA.ajax.gf('../gift/getByNo','no='+encodeURIComponent(no),function(ed){
					if(ed.error){
						pageVars.points = 0 ;
					}else{
						pageVars.points = ed.data.points;

 					}
					$this.calcPoints();
				});
			});
			$('cost_quantity').addEvent('blur',$this.calcPoints);
			//c();
		},//init ends
		calcPoints :function(){
			
			var v = Number.from( $('cost_quantity').value );
			//alert(pageVars.points);
				if( pageVars.points == 0 ){
					$('tips').set('html','无效的礼品,请重新指定礼品');
					return ;
				}
				if(isNaN(v) ){
					$('tips').set('html','请正确填写数量');
					return ;
				}

				//mul
				var r = pageVars.points * v ;
				$('tips').set('html','本次扣除积分:'+r); 
		},
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
					{label:lang.points,width:40,key:'points'},
					{label:lang.owner,width:100,key:'owner',formatter:formatOwner},
					{label:lang.admin,width:80,key:'admin'},
					{label:lang.type,width:40,key:'type'},
					{label:lang.comments,width:150,key:'comments'}
					
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
					$("agent.no").value = PA.utils.defaultStr(data.no);
					$("agent.name").value = PA.utils.defaultStr(data.name);
					$("agent.comments").value = PA.utils.defaultStr(data.comments);
					$("agent.password").value = PA.utils.defaultStr(data.password);
					$("agent.loginId").value = PA.utils.defaultStr(data.loginId);
					
					
					
					$("agent.id").value = data.id ;

					this.showInput(true);
					this.vars.action='_update';
					pageVars.id  = d.id;
					this.updateMode();
					

				}.bind(this));
			}

			
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


