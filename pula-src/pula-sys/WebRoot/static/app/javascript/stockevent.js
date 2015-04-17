var PPage = new Class({
		Extends: PBasePage,
		initialize: function (configs) {			
			this.initVars(configs);
			this.init();
		},
	
		init : function(){
			this.initBase({focusField:'materialNo'});
			this.initToolBar(true);
			this.initSaveForm();
			this.initSearchForm();
			this.initViewTable();
			this.initHistory();

			var $this = this;
			this.edtMaterialC = new TSuggest({
				field:'condition.materialNo',
				field_id:'cMaterialNo',
				text:'',
				value:'',
				showTips:true,
				tips:_lang.tipsOfSGS,
				url:'../material/suggest',
				container:'scMaterialNo'
			});	
			this.edtMaterial = new TSuggest({
				field:'form.materialNo',
				field_id:'materialNo',
				text:'',
				value:'',
				showTips:true,
				tips:_lang.tipsOfSGS,
				url:'../material/suggest',
				container:'sMaterialNo',
				callback : this.showUnit.bind(this)
			});	
			this.showCondition();
			this.reload();

			loadCalCss('.dateField');
			PGlobals.uriOfCreate = '_create'+pageVars.flag ;
			

			//c();
		},//init ends
		showUnit : function(data){
			$('spanUnit').set('html',PA.utils.escapeHTML(data[2]));
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
			var formatNo = function(r,c,d){
				if( r.noType){
					return "<a href='javascript:pes.viewForm("+r.noType+","+r.noId+")'>"+lang.noTypes[ r.noType-1 ]+":"+ d+"</a>";
				}else{
					return d; 
				}
			}
			var cfgs = {
				id :'dt',
				container:'dt',
				height:PGlobals.minusHeight.bind(['conditionDiv','__top']),
				url:"list4"+pageVars.flag,
				requestParam:getRequestParam.bind(this),
				selectRow:onSelectRow.bind(this),
				columns: [
					{label:TTable.checkAll,width:24,key:'id',formatter:TTable.formatCheckbox},
					{label:'单号',key:'id',width:80},
					{label:lang.eventTime,width:90,key:'eventTime',formatter:TTable.formatDate,align:'center'},
					{label:lang.materialNo,width:160,key:'no'},
					{label:lang.materialName,key:'name'},
					{label:'分支机构',width:140,key:'branchName'},
					{label:lang.quantity,width:40,key:'quantity',align:'right'},	
					{label:'单位',key:'materialUnit',align:'center',width:60},				
					{label:lang.outNo,width:140,key:'outNo'}
				]
			};

			if(PGlobals.smallScreen()){
				cfgs.selectRow = null ;
				cfgs.intoRow = onSelectRow.bind(this) ;
			}

			this.dt = new TTable(cfgs);		
			
			this.dt.draw();
			
		},
		updateMode : function (){
			
			if(this.vars.action=='_create'+pageVars.flag){
				$('pageMode').innerHTML = _lang.create;
				
			}else{
				$('pageMode').innerHTML = _lang.update;
			}
		},
		showData:function (d){

			$('addForm').reset();
			$('spanUnit').set('html','');

			if(d==null){
				this.edtMaterial.reset();
				this.vars.action='_create'+pageVars.flag;

			}else{

				PA.ajax.gf('get','id='+d.id,function(ed){
					if(ed.error){
						alert(ed.message);
						return false;
					}
					var data =ed.data; 
					
					
					$('addForm').removeClass('h');
					for( var k in data ) {
						if($('form.'+k)){
							$('form.'+k).value = PA.utils.defaultStr(data[k]);
						}
					}
					
					this.edtMaterial.setValue( data.no,data.name);
					$('spanUnit').set('html',PA.utils.escapeHTML(data.materialUnit));

				
					this.vars.action='_update'+pageVars.flag;
					pageVars.id  = d.id;
					
					
				}.bind(this));
			}

			
		},
		remove : function (){
			var qs = this.getQueryString();
			if(qs == null ) return ;
			if(!confirm(_lang.removeConfirm)){
				return 
			}		
			PA.ajax.gf('_remove'+pageVars.flag,qs,function (e){
				if(e.error){
					topHiddenSuccess();
					alert(e.message);
				}else{
					topSuccess();
					this.reload();
				}			
			}.bind(this));
		}
		
		
});

var pes = null ;
window.addEvent('domready',function(){
	pes = new PPage({"id":"pes","pageMode":"pageMode","searchForm":"searchForm","addForm":"addForm"});
	//pes.mockCheck();
	//alert('final:'+$("conditionDiv").className);
});


