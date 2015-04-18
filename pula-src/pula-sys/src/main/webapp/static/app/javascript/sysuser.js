var PPage = new Class({
		Extends: PBasePage,
		initialize: function (configs) {			
			this.initVars(configs);
			this.init();
		},
	
		init : function(){
			this.initBase();
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
					this.navigateHistory("id", ''+data.id);
				}
			}

			var _columns =  [
					{label:TTable.checkAll,width:24,key:'id',formatter:TTable.formatCheckbox},
					{label:lang.loginId,width:200,key:'loginId'},
					{label:lang.name,key:'name'},
					{label:lang.groupName,width:180,key:'groupName'},
					{label:lang.roleName,width:180,key:'roleName'},
					{label:lang.status,width:40,key:'enabled',formatter:TTable.formatEnabled},
					{label:lang.purview,width:40,key:'id',formatter:TTable.formatLinkJs.bind({func:'pes.setupPurview',label:lang.setup})}
					
					
				];

			if(pageVars.admin){
				_columns.push({label:lang.beHim,width:40,key:'id',formatter:TTable.formatLinkJs.bind({func:'pes.beHim',label:lang.beHim})});
			}

			var cfgs = {
				id :'dt',
				container:'dt',
				height:PGlobals.minusHeight.bind(['conditionDiv','__top']),
				url:"list",
				requestParam:getRequestParam.bind(this),
				selectRow:onSelectRow.bind(this),
				columns:_columns
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
			$$('.no_sales_men').removeClass('h');
			$$('.no_sales_men_lock').set('disabled',false);
			$$('.show_sales_men').addClass('h');

			$('purviewBtn').addClass('h');

			pageVars.salesman_mode = false;
			if(d==null){
				this.vars.action='_create';
				$('lblChangePassword').addClass('h');
				$('sysUser.loginId').removeClass('h');
				$('spanLoginId').addClass('h');

			}else{
				$('lblChangePassword').removeClass('h');
				PA.ajax.gf('get','id='+d.id,function(ed){
					if(ed.error){
						alert(ed.message);
						return false;
					}
					var data =ed.data; 
					for( var k in data ) {
						if($('sysUser.'+k)){
							var ctn = PA.utils.defaultStr(data[k]);
							$('sysUser.'+k).set('value', ctn);
						}
					}

					$('sysUser.loginId').addClass('h');
					$('spanLoginId').removeClass('h');
					$('spanLoginId').set('html',data.loginId);


					//根据角色不同有不同的锁定
					if(data.roleNo == pageConsts.ROLE_SALES){
						$$('.no_sales_men_lock').set('disabled',true);
						$$('.no_sales_men').addClass('h');
						$$('.show_sales_men').removeClass('h');
						pageVars.salesman_mode = true ;
					}else{
					}

					this.vars.action='_update';
					pageVars.id  = d.id;
					this.updateMode();
					$('purviewBtn').removeClass('h');
					

				}.bind(this));
			}

			
		},setupPurview:function(id){
			var url = "purview?id="+id;
			Mbox.open({
				url: url,
				type: "iframe",
				ajax: true,
				width:480,
				height:360,
				title:lang.purview
			});	
		},beHim : function(id){
			PA.ajax.gf('beHim','id='+id,function(el){
				if(el.error){
					alert(el.message);
					return ;
				}
				topSuccess();
			});
		},
		
		
});

var pes = null ;
window.addEvent('domready',function(){
	pes = new PPage({"id":"pes","pageMode":"pageMode","searchForm":"searchForm","addForm":"addForm"});
	//pes.mockCheck();
	//alert('final:'+$("conditionDiv").className);
});


