var PPage = new Class({
		Extends: PBasePage,
		initialize: function (configs) {			
			this.initVars(configs);
			this.init();
		},
	
		init : function(){
			this.initBase({focusField:'form.applyComments'});
			this.initToolBar({condition:true});
			this.initSaveForm();
			this.initSearchForm();
			this.initViewTable();
			this.initHistory();

			
			this.showCondition();
			this.reload();
			

			loadCalCss('.dateField');
			var $this = this ;



			$$(".rStatus").addEvent("click",function(){
				if(this.value == pageVars.const_normal) {
					$$(".normalInput").set('disabled',false);
					$$(".normalInputIn").removeClass('h');
				}else{
					$$(".normalInput").set('disabled',true);
					$$(".normalInputIn").addClass('h');
				}
			});
			

			$this.vars.classroomList = {};
			$this.vars.branchId = {} ;

			$("branchId").addEvent('change' ,function(){
				$this.loadClassroom( this,$this.loadClassroomToSelect.bind([$this,this]) );
			}).fireEvent('change');

			//c();
		},//init ends
		loadClassroom : function(sender,_callback){
			var $this = this ;
			var wid= sender.value;
			var sid = sender.get('id');

			
			//var shouldReload =false;
			if( !this.vars.branchId[sid]  || ( this.vars.branchId[sid] && this.vars.branchId[sid] != wid ) ){
				//shouldReload = true ;
			}else{
				return ;
			}

			if(isEmpty(wid)){
				wid= "0";
			}

			//ajax
			PA.ajax.gf(pageVars.base+'/app/classroom/listByBranch',
				'branchId='+ wid,
				function( e ) {
					if(e.error){ alert (e.message) ; return ;} 
					//load
					$this.vars.classroomList[ sid ]  = e.data ;
					_callback();
				}
			);


		},
		loadClassroomToSelect : function( sel ,sender) {
			var $this = this ;
			if(!sender){
				sender =$this[1].get('id');
				$this = $this[0];
			}
			
			if( typeof( $this.vars.classroomList[sender]) == "undefined"){
				return ;
			}

			var _loadSpaceToSelect = function ( obj ) {
				PA.utils.clearSelect( obj ) ;

				var opt = new Element('option',{   
					 text: '请选取...',
					 value: ''
				});
				obj.adopt(opt);
				$this.vars.classroomList[sender].each ( function(el){
						var opt = new Element('option',{   
							 text: el.name,
							 value: el.id
						});
						obj.adopt(opt);
				});
			}
			if(sel ){
				//load from remote ?
				//local data ;
				_loadSpaceToSelect( $(sel) );
			}else{
				$$("._"+sender).each ( function( ss ) {
					_loadSpaceToSelect( $(ss) );
				});
			}

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
					this.navigateHistory("id",""+data.id);
					
					
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
					//{label:TTable.checkAll,width:24,key:'id',formatter:TTable.formatCheckbox},
					{label:'ID',width:100,key:'id'},
					{label:'机器码',key:'machineNo'},
					{label:'IP',width:140,key:'ip'},
					{label:'申请时间',width:160,key:'createdTime',align:'center',formatter:TTable.formatDateTime}			
					
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
				for( var k in data ) {
					if($('span.'+k)){
						$('span.'+k).set('html',PA.utils.escapeHTML(data[k]));
					}
				}
			
				
				if( data.createdTime) {
					$('createdTimeText').set('html', new Date( data.createdTime).format('yyyy-MM-dd hh:mm:ss'));
				}
				$('form.id').value = data.id ;

				this.vars.action='_apply';
				pageVars.id  = d.id;
				this.updateMode();
				$$(".rStatus")[0].set('checked',true).fireEvent('click');
				
			}.bind(this));
		}


		
	}
		
		
});

var pes = null ;
window.addEvent('domready',function(){
	pes = new PPage({"id":"pes","pageMode":"pageMode","searchForm":"searchForm","addForm":"addForm"});
});

var reloadData = function (){
	pes.reload();
}
