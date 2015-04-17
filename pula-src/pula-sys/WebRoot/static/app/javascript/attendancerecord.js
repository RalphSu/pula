var PPage = new Class({
		Extends: PBasePage,
		initialize: function (configs) {			
			this.initVars(configs);
			this.init();
		},
	
		init : function(){
			this.initBase({focusField:'attendanceRecord.no'});
			this.initToolBar({add:true,remove:true,condition:true});
			this.initSaveForm();
			this.initSearchForm();
			this.initViewTable();
			this.initHistory();

			

			this.showCondition();
			this.reload();
			
			loadCalCss('.dateField');

			this.edtEmployee = new TSimpleNo({
				field:'condition.employeeNo',
				field_id:'cEmployeeNo',
				text:'',
				value:'',
				showTips:true,
				tips:_lang.tipsOfTSN,
				url:'../employee/find',
				container:'sEmployeeC'
			});	
			

			//c();
		},//init ends
initToolBar:  function(simple){
		if(typeof(simple)=='object'){
			
		}else{
			if(simple){
				simple = {add:true,remove:true,condition:true};
			}else{
				simple = {all :true }
			}
		}

		var _buttons = [] ;

		if(simple.all || simple.add ) {
				_buttons.push({
					label:_lang.create,
					link :'javascript:pes.createPage()',
					icon: pageVars.base+'/static/laputa/images/icons/add.gif'
				});
		}
		if(simple.all || simple.remove ) {
			_buttons.push({
					label:_lang.remove,
					id:'removeLnk',
					link :'javascript:pes.remove()',
					icon: pageVars.base+'/static/laputa/images/icons/delete.gif',
					css:'forList'
				});
		}
	

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
					{label:lang.checkTime,width:160,key:'checkTime',formatter:TTable.formatDateTime},
					{label:lang.collectTime,width:160,key:'collectTime',formatter:TTable.formatDateTime},
					{label:lang.no,key:'no',width:80},
					{label:lang.employeeName,key:'ownerName'},
					{label:lang.ip,width:180,key:'ip'},			
					{label:lang.dataFromName,width:70,key:'dataFromName',align:'center'},			
					{label:lang.machine,width:60,key:'machine',align:'center'},			
					{label:lang.status,width:40,key:'enabled',formatter:TTable.formatBoolean,align:'center'}		
				]
			};

			cfgs.selectRow = null ;
			

			this.dt = new TTable(cfgs);		
			
			this.dt.draw();
			
		},
		remove : function (){
		var qs = this.getQueryString();
		if(qs == null ) return ;
		if(!confirm(_lang.removeConfirm)){
			return 
		}		
		PA.ajax.gf('cancel',qs,function (e){
			if(e.error){
				topHiddenSuccess();
				alert(e.message);
			}else{
				topSuccess();
				this.reload();
			}			
		}.bind(this));
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
					$("employee.no").value = PA.utils.defaultStr(data.no);
					$("employee.name").value = PA.utils.defaultStr(data.name);
					$("employee.departmentId").value = PA.utils.defaultStr(data.departmentId);
					$("employee.id").value = data.id ;
					$('employee.attendanceNo2').value = data.attendanceNo2;
					$('employee.attendanceNo1').value = data.attendanceNo1;

					$$(".attendanceType").each (function(el){
						if(el.value == data.attendanceType){
							el.checked = true ;
						}
					});

					this.showInput(true);
					this.vars.action='_update';
					pageVars.id  = d.id;
					this.updateMode();
					

				}.bind(this));
			}

			
		},
		printSelected : function(){
			var qs = this.getQueryString();
			if(qs == null ) return ;
			//alert(qs);
			PA.ajax.gf('_sendPrint?_json=1',qs,function (e){
				if(e.error){
					alert(e.message);	
				}else{
					alert(lang.printOK);
				}
			}.bind(this));	
		}
		
		
});

var pes = null ;
window.addEvent('domready',function(){
	pes = new PPage({"id":"pes","pageMode":"pageMode","searchForm":"searchForm","addForm":"addForm"});
});


