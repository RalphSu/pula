var PPage = new Class({
		Extends: PBasePage,
		initialize: function (configs) {			
			this.initVars(configs);
			this.init();
		},
	
		init : function(){
			this.initBase({"focusField":"sysUserGroup.no"});
			this.initToolBar();
			this.initSaveForm();
			this.initSearchForm();
			this.initViewTable();
			this.initHistory();
			this.showCondition();
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
					var d = this.dt.rows[i] ;
					this.navigateHistory("id", d.id);
					this.vars.data = d; 
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
					{label:lang.status,width:40,key:'enabled',formatter:TTable.formatEnabled},
					{label:lang.purview,width:40,key:'id',formatter:TTable.formatLinkJs.bind({func:'pes.setupPurview',label:lang.setup})}
					
				]
			};

			if(PGlobals.smallScreen()){
				cfgs.selectRow = null ;
				cfgs.intoRow = onSelectRow.bind(this) ;
			}

			this.dt = new TTable(cfgs);		
			
			this.dt.draw();
			
		},
		showManagerInput : function(){
		},
		getQueryString : function(){
			var qs = "" ;
				var checked =false;
				$$(".objId").each(function (ee){
						if(ee.checked){
							qs +="&objId="+ee.value;
							checked = true ;
						}

				});

				if(!checked){
					alert(_lang.chooseOne);
					return null ;
				}
				return qs ;
		},
		reload : function(){
			this.dt.reload();
			this.vars.action='_create';
			
			this.dt.selectedRow(null);
			this.showData(null);
			this.updateMode();		
		},
		disableBtn:function (b){
			$('submitBtn').disabled = b; 
			$('resetBtn').disabled = b; 
		},

		showData:function (d){

			if(d==null){
				
				$('sysUserGroup.name').value = "" ;
				$('sysUserGroup.no').value = "" ;

				$('sysUserGroup.id').value = "";

				this.vars.action='_create';
				this.updateMode();
				

			}else{
				var $this =this ;
				PA.ajax.gf('get','id='+d.id,function(e){
					if(e.error) {alert (e.message);return} 
					var d = e.data;
					$('sysUserGroup.name').value = d.name ;
					$('sysUserGroup.no').value = d.no ;
					if(d.groupId!=null){
						$('r'+d.groupId).checked = true;
						
					}
					$('sysUserGroup.id').value = d.id;
					$this.vars.action='_update';
					pageVars.id  = d.id;
					$this.updateMode();
				});
			}

			
		},
		updateMode : function (){
			
			if(this.vars.action=='_create'){
				$('pageMode').innerHTML = _lang.create;

				$('purviewBtn').addClass('h');
			}else{
				$('pageMode').innerHTML = _lang.update;
				$('purviewBtn').removeClass('h');
			}
		},
		
		purviewThis : function(){
			//generate purview ;
			this.setupPurview(pageVars.id);
		},
		setupPurview:function(id){
			var url = "purview?id="+id;
			Mbox.open({
				url: url,
				type: "iframe",
				ajax: true,
				width:480,
				height:360,
				title:lang.purview
			});	
		}
	
});
var pes = null ;
window.addEvent('domready',function(){
	pes = new PPage({"id":"pes","pageMode":"pageMode","searchForm":"searchForm","addForm":"addForm"});
	//pes.mockCheck();
	//alert('final:'+$("conditionDiv").className);
});


