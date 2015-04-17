var PPage = new Class({
		Extends: PBasePage,
		initialize: function (configs) {			
			this.initVars(configs);
			this.init();
		},
	
		init : function(){
			this.initBase({focusField:'gift.no'});
			this.initToolBar(true);
			this.initSaveForm();
			this.initSearchForm();
			this.initViewTable();
			this.initHistory();

			
			this.showCondition();
			this.reload();


			$('uploadForm').addEvent('submit',function(e){
				$('uploadBtn').disabled = true ;
			});

			loadCalCss('.dateField');

			//c();
		},//init ends

		initToolBar:  function(simple){


			var tb = new PA.TToolBar({
			container:"__top",
			title:lang.domain,
			buttons:[
					{
						label:_lang.create,
						link :'javascript:pes.createPage()',
						icon: pageVars.base+'/static/laputa/images/icons/add.gif'
					},
					{
						label:_lang.enabled,
						link :'javascript:pes.enable(true)',
						icon: pageVars.base+'/static/laputa/images/icons/unlocked.gif',
						css:'forList'
					},{
						label:_lang.disabled,
						link :'javascript:pes.enable(false)',
						icon: pageVars.base+'/static/laputa/images/icons/lockdis.gif',
						css:'forList'
					},
					{
						label:_lang.remove,
						link :'javascript:pes.remove()',
						icon: pageVars.base+'/static/laputa/images/icons/delete.gif',
						css:'forList'
					},
					{
						label:_lang.hiddenCondition,
						link :'javascript:pes.showCondition()',
						icon: pageVars.base+'/static/laputa/images/icons/view.gif',
						id:'queryLink',
						css:'forList',
						width:'100px'
					},
					{
						label:lang.importTo,
						link :'javascript:pes.importXls()',
						icon: pageVars.base+'/static/laputa/images/icons/import.gif',
						id:'queryLink',
						css:'forList'
					}
			]
			});
				
		},importXls : function(){
		Mbox.open({
		url: "pnl_upload",
		title:lang.upload
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
					{label:lang.no,width:160,key:'no'},
					{label:lang.name,key:'name'},
					{label:'拼音码',key:'pinyin',width:170},
					{label:lang.giftTypeName,width:140,key:'categoryName'},
					{label:'品牌',width:100,key:'brand'},
					{label:lang.points,width:100,key:'points',align:'right'},
					{label:'单位',width:80,key:'unit',align:'center'},	
					{label:lang.beginTime,width:120,key:'beginTime',formatter:TTable.formatDate},
					{label:lang.endTime,width:120,key:'endTime',formatter:TTable.formatDate},
					
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
			if(d==null){
				
				
				$$(".numberEdit").set('value','0');

				this.vars.action='_create';



			}else{

				PA.ajax.gf('get','id='+d.id,function(ed){
					if(ed.error){
						alert(ed.message);
						return false;
					}
					var data =ed.data; 
					for( var k in data ) {
						if($('gift.'+k)){
							$('gift.'+k).set('value',PA.utils.defaultStr(data[k]));
						}
					}
					if( data.beginTime) {
						$('gift.beginTimeText').value = new Date( data.beginTime).format('yyyy-MM-dd');
					}
					if( data.endTime) {
						$('gift.endTimeText').value = new Date( data.endTime).format('yyyy-MM-dd');
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


