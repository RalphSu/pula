var PPage = new Class({
		Extends: PBasePage,
		initialize: function (configs) {			
			this.initVars(configs);
			this.init();
		},
	
		init : function(){
			this.initBase();
			this.initToolBar({condition:true});
			
			this.showCondition();
			

			var $this = this ;

			var afterSubmitAll = function(e){				
				
				$this.vars.toClose.each (function(el){
					$this.grid.clear( el );
				});

				if($this.grid.rows.length==0){
					$this.grid.addRow();
				}
				
			}
			var beforeSubmit = function(){
				//data 
				if(!check () || !$this.checkGrid()){
					return false;
				}
				$this.prepareData();
				return true ;
			}
			this.initBase({afterSubmitAll:afterSubmitAll,beforeSubmit:beforeSubmit});
			this.initToolBar({});
			if(pageVars.start){
				this.initSaveForm();
				this.buildGrid();
			}
			this.vars.action = "_update";
			var ms = new TMoonSelector({
				container:'ms',
				year:pageVars.year,
				month:pageVars.month,
				directForward:false,
				count:7,applyYear:'hidYear',
					applyMonth:'hidMonth',form:'searchForm'
			});


			if($('branchId')){
				$('branchId').addEvent('change',function(){
					$('searchForm').submit();
				});
			}
		},//init ends
		goMonth: function(y,m){
			$('hidYear').value = y ;
			$('hidMonth').value = m ;
			$('searchForm').submit();
		},checkGrid : function (){
			var rowCount = 0 ;
			var rowFlag = {hasPart:false};
			var msgs  = this.grid.onValidate( function ( box,table){
				//alert(box.col + ' hasPart:'+box.flag.hasPart + ' value='+box.value);
				/*if(box.col == 'student' && !isEmpty(box.value) ){
					box.flag.hasPart = true ;
					return null;
					
				}else if (box.col != 'student' && !box.flag.hasPart && !isEmpty(box.value))
				{
					return "请填写学生姓名,否则该行不保存";				
				}

				if(box.flag.hasPart){
					rowCount ++ ;
				}*/
				return null;

			},rowFlag);

			if(msgs.length!=0){
				var sb = "";
				msgs.each(function(e){
					sb += lang.lineMsg.substitute( {'index': (e.index+1) , "message": e.message});
				});
				alert(sb);
				return false;
			}
			/*if( rowCount == 0 ){
				alert(lang.needDetail);
				return false;
			}*/
			return true;
		},
		disableBtn: function( b ){
			$('saveBtn').disabled = b ;
		},
		prepareData : function(){
			//var main = PA.ajax.f2j($('addForm'));
			//alert(main);
			var $this = this;
			$this.vars.toClose = [] ;
			$this.vars.rowCount = 0 ;
			var c = this.grid.buildJson( function (x){
				x.flag.ignore = false;
				x.flag.quote = false;
				if(x.col == 'name'||x.col=='courseCount'||x.col=='orders'||x.col=='chargebacks' ){

					x.flag.ignore = true;
					return ;
				}
				
			});
			
			$('jsonDetail').value = c ;

			//alert(c);
		},
		buildGrid:function(){
			var $this = this ;
						

			var afterAddRow = function (row){
				
			};

			var edt = "text";
			if(!pageVars.allowEdit){
				edt = "display";
			}


			this.grid = new TDataGrid ( {columns:[
					{width:'60px',text:'教师',type:'display',field:'name'},
					{width:'40px',text:'课时',type:'display',field:'courseCount',css:'text-c',headerCss:'text-c'},
					{width:'100px',text:'工作日',type:edt,field:'workdays',css:'numberEdit',headerCss:'numberEdit'},
					{width:'100px',text:'实际工作日',type:edt,field:'factWorkDays',css:'numberEdit',headerCss:'text-r'},
					{width:'80px',text:'迟到',type:edt,field:'later',css:'numberEdit',headerCss:'text-r'},
					{width:'80px',text:'早退',type:edt,field:'earlier',css:'numberEdit',headerCss:'text-r'},
					{width:'80px',text:'请假',type:edt,field:'leave',css:'numberEdit',headerCss:'text-r'},
					{width:'100px',text:'综合',type:edt,field:'complex',css:'numberEdit',headerCss:'text-r'},
					{width:'110px',text:'绩效',type:edt,field:'performance',css:'numberEdit',headerCss:'text-r'},
					{width:'50px',text:'订单数',type:'display',field:'orders',css:'text-c',headerCss:'text-c'},
					{width:'50px',text:'退单数',type:'display',field:'chargebacks',css:'text-c',headerCss:'text-c'},
				    {type:'hidden',field:'id'},
				    {type:'hidden',field:'teacherId'}
					],
					container:"detailTable",
					id:"pes.grid",
					imagePath:pageVars.imagePath,
					tableCss:'view-grid',view:true
			});
			this.grid.draw();

			
		},
		

		showData:function (d){

			

			
		},updateMode:function(){}
		
		
});


