var PPage = new Class({
		Extends: PBasePage,
		initialize: function (configs) {			
			this.initVars(configs);
			this.init();
		},
	
		init : function(){
			this.initBase({focusField:'teacher.no'});
			this.initToolBar();
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
					this.updatePage(data.id);
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
					{label:lang.no,width:100,key:'no'},
					{label:lang.name,key:'name'},
					{label:lang.gender,width:60,key:'genderName',align:'center'},			
					{label:lang.levelName,width:60,key:'levelName',align:'center'},			
					{label:lang.branchName,width:180,key:'branchName'},			
					{label:lang.birthday,width:100,key:'birthday',align:'center',formatter:TTable.formatDate},			
					{label:lang.barcode,width:100,key:'barcode',align:'center'},			
					{label:lang.status,width:60,key:'statusName',align:'center'},			
					{label:'启用',width:60,key:'enabled',align:'center',formatter:TTable.formatEnabled},			
					{label:lang.assign,width:40,key:'id',formatter:TTable.formatLinkJs.bind({func:'pes.assign',label:lang.assign}),align:'center'},
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
        centerWindow('../my/window?u=' + url,650, 580);
    },
	disableBtn:function (b){
		$('submitBtn').disabled = b; 
	},
	assign: function(id){
		var matchNodes = this.dt.rows.filter ( function (el ,idx){
			if(el.id == id ) {
				return true ;
			}
		});

		if(matchNodes.length == 0 ){
			return ;
		}

		$("spnTeacherNo").set('html',matchNodes[0].no ) ;
		$("spnTeacherName").set('html',matchNodes[0].name ) ;
		if(matchNodes[0].branchId){
			$("branchId").set('value',matchNodes[0].branchId) ;
		}else{
			$("branchId").set('value',0) ;
		}
		$('assign.id').set('value',id);

		Mbox.open({
			url: "divAssign",
			title:lang.assign
		});	


	},
	createPage : function(b){
		
        var url = encodeURIComponent('../teacher/create');
        centerWindow('../my/window?u=' + url,850, 600);
	},
	updatePage : function(id ){
		var url = encodeURIComponent('../teacher/update?id='+id);
		        centerWindow('../my/window?u=' + url,850, 600);
	},view : function(id ){
		var url = encodeURIComponent('../teacher/view?id='+id);
		        centerWindow('../my/window?u=' + url,950, 600);
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
