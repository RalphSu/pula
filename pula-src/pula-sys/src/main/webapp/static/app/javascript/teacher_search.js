var PPage = new Class({
		Extends: PBasePage,
		initialize: function (configs) {			
			this.initVars(configs);
			this.init();
		},
	
		init : function(){
			this.initBase({focusField:'teacher.no'});
			this.initToolBar({condition:true,add:false});
			this.initSaveForm();
			this.initSearchForm();
			this.initViewTable();
			this.initHistory();

			
			this.showCondition();
			this.reload();
			

			var $this = this ;
			$('frmAssign').addEvent('submit',function(e){
				e.stop();
				//check
				if(!checkAssign()){
					return ;
				}
				PA.ajax.gf('_assign',$('frmAssign').toQueryString(),function(ed){
					if(ed.error){
						alert(ed.message);
						return ;
					}
					$this.reload();
					Mbox.close();
				});
			});

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
					//this.navigateHistory("id", ''+data.id);
					this.view(data.id);
				}
			}
			var cfgs = {
				id :'dt',
				container:'dt',
				height:PGlobals.minusHeight.bind(['conditionDiv','__top']),
				url:"list4Search",
				requestParam:getRequestParam.bind(this),
				selectRow:onSelectRow.bind(this),
				columns: [
					{label:TTable.checkAll,width:24,key:'id',formatter:TTable.formatCheckbox},
					{label:lang.no,width:100,key:'no'},
					{label:lang.name,key:'name'},
					{label:lang.gender,width:60,key:'genderName',align:'center'},			
					{label:lang.levelName,width:60,key:'levelName',align:'center'},			
					{label:lang.branchName,width:180,key:'branchName'},			
					{label:lang.birthday,width:100,key:'birthday',align:'center',formatter:TTable.formatDate},			
					{label:lang.barcode,width:100,key:'barcode',align:'center'},				
					{label:lang.status,width:60,key:'statusName',align:'center'},	
					{label:'启用',width:60,key:'enabled',align:'center',formatter:TTable.formatEnabled},		
					{label:lang.view,width:40,key:'id',formatter:TTable.formatLinkJs.bind({func:'pes.view',label:lang.view}),align:'center'}
				]
			};

			if(PGlobals.smallScreen()){
				cfgs.selectRow = null ;
				cfgs.intoRow = onSelectRow.bind(this) ;
			}

			this.dt = new TTable(cfgs);		
			
			this.dt.draw();
			
		},
		
		showFile: function(id) {
        var url = encodeURIComponent('../teacher/view?id=' + id);
        centerWindow('../my/window?u=' + url,850, 600);
    },
	disableBtn:function (b){
		$('submitBtn').disabled = b; 
	},
	view : function(id ){
		var url = encodeURIComponent('../teacher/view?id='+id);
		        centerWindow('../my/window?u=' + url,850, 600);
	},

		showData:function (d){

			

			
		}
		
		
});

var pes = null ;
window.addEvent('domready',function(){
	pes = new PPage({"id":"pes","pageMode":"pageMode","searchForm":"searchForm","addForm":"addForm"});
});

var reloadData = function (){
	pes.reload();
}
