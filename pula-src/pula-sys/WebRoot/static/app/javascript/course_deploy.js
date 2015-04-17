var PPage = new Class({
		Extends: PBasePage,
		initialize: function (configs) {			
			this.initVars(configs);
			this.init();
		},
	
		init : function(){
			this.initBase({focusField:'course.no'});
			this.initToolBar();
			//this.initSaveForm();
			this.initSearchForm();
			this.initViewTable();
			this.initHistory();

			
			this.showCondition();
			

			loadCalCss('.dateField');

			var $this = this;

			
			this.reload();


			var $this = this ;
			$('frmDeploy').addEvent('submit',function(e){
				e.stop();
				//check
				if(!checkDeploy()){
					return ;
				}
				var qs = $this.getQueryString();
				PA.ajax.gf(pageVars.base+'/app/coursedeployment/_deploy',
					$('frmDeploy').toQueryString()+"&"+qs,
					function(ed){
						if(ed.error){
							alert(ed.message);
							return ;
						}
						//$this.reload();
						Mbox.close();
				});
			});
			$this.vars.classroomList = {};
			$this.vars.branchId = {} ;
			$this.vars.classroomId = {} ;
			$$(".c-branchId").addEvent('change' ,function(){
				$this.loadClassroom( this,$this.loadClassroomToSelect.bind([$this,this]) );
			}).fireEvent('change');

			//c();
		},//init ends
		initToolBar:  function(simple){
			

			var _buttons = [] ;

			
			_buttons.push({
				label:lang.deployTo,
				link :'javascript:pes.deployTo()',
				icon: pageVars.base+'/static/laputa/images/16X16/map.png'
			});
			
			_buttons.push({
					label:_lang.hiddenCondition,
					link :'javascript:pes.showCondition()',
					icon: pageVars.base+'/static/laputa/images/icons/view.gif',
					id:'queryLink',
					css:'forList',
					width:'100px'
			});

			var tb = new PA.TToolBar({
				container:"__top",
				title:lang.domain,
				buttons:_buttons				
			});
		
		},

		initViewTable : function(){
			var getRequestParam = function(){
				return this.vars.requestParam;
			}
			
			var cfgs = {
				id :'dt',
				container:'dt',
				height:PGlobals.minusHeight.bind(['conditionDiv','__top']),
				url:"list4Deploy",
				requestParam:getRequestParam.bind(this),
				//selectRow:onSelectRow.bind(this),
				columns: [
					{label:TTable.checkAll,width:24,key:'id',formatter:TTable.formatCheckbox},
					{label:lang.no,width:200,key:'no'},
					{label:lang.name,key:'name'},
					{label:lang.categoryName,width:140,key:'categoryName'},
					{label:lang.publishTime,width:120,key:'publishTime',formatter:TTable.formatDate},
					{label:lang.expiredTime,width:120,key:'expiredTime',formatter:TTable.formatDate},
					{label:lang.indexNo,width:100,key:'indexNo',align:'center'}
				]
			};

			

			this.dt = new TTable(cfgs);		
			
			this.dt.draw();
			
		},
		deployTo : function(){
			var qs = this.getQueryString();
			if(qs == null ) return ;
			var idList = [] ;
			$$(".objId").each(function (ee){
					if(ee.checked){
						idList.push( ee.value ) ;
					}

			});

			

			//拿出课程来
			var matchNodes = this.dt.rows.filter ( function (el ,idx){
				var idx = ( idList.indexOf( ""+el.id ) );
				if(idx>=0) {
					return true ;
				}
			});

			//界面
			$('spnCourseCount').set('html',matchNodes.length ) ;
			//htmlpart
			var html = "";
			matchNodes.each ( function( el ,idx ) {
				html+=" "+el.no +" "+el.name;
				if(idx < matchNodes.length -1 ){
					html+=",";
				}
			});
			$('divAllCourse').set('html',html);

			Mbox.open({
				url: "divDeploy",
				title:lang.deployTo
			});	

		},	
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
						if( $this.vars.classroomId[sender] && $this.vars.classroomId[sender]  == el.id ){
							opt.set('selected',true);
						}
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
		showData:function (d){

			
			
		},
		updateMode:function(){}
		
		
});

var WORKER_TPL = "<a href='javascript:pes.eraseWorker(\"{no}\")'>{no} {name}<input type='hidden' name='course.courseNos' value='{no}'/></a>";
var pes = null ;
window.addEvent('domready',function(){
	pes = new PPage({"id":"pes","pageMode":"pageMode","searchForm":"searchForm","addForm":"addForm"});
	//pes.mockCheck();
	//alert('final:'+$("conditionDiv").className);
});


