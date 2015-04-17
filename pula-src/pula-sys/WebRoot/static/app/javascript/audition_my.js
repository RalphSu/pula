var PPage = new Class({
		Extends: PBasePage,
		initialize: function (configs) {			
			this.initVars(configs);
			this.init();
		},
	
		init : function(){
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
			this.initSaveForm();
			this.buildGrid();
			this.vars.action = "_update";

			
			//c();
		},//init ends
		
	
		checkGrid : function (){
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
		prepareData : function(){
			//var main = PA.ajax.f2j($('addForm'));
			//alert(main);
			var $this = this;
			$this.vars.toClose = [] ;
			$this.vars.rowCount = 0 ;
			var c = this.grid.buildJson( function (x){
				x.flag.ignore = false;
				x.flag.quote = true;
				if(x.col == 'id' ){
					x.flag.quote = false;
					x.flag.ignore = false;
					return ;
				}
				if(x.col == 'resultId' && !isEmpty( x.value )){
					//to close;
					$this.vars.toClose.push( x.row );
				}else{
					$this.vars.rowCount++;
				}
			});
			$('jsonDetail').value = c ;

			//alert(c);
		},
		buildGrid:function(){
			var $this = this ;
						

			var afterAddRow = function (row){
				
			};


			this.grid = new TDataGrid ( {columns:[
					{width:'60px',text:'学生姓名',type:'text',field:'student'},
					{width:'40px',text:'年龄',type:'text',field:'age',css:'text-c',headerCss:'text-c'},
					{width:'150px',text:'电话',type:'text',field:'phone'},
					{width:'80px',text:'家长',type:'text',field:'parent'},
					{width:'100px',text:'试听内容',type:'text',field:'content'},
					{width:'110px',text:'预约1',type:'text',field:'plan1'},
					{width:'110px',text:'预约2',type:'text',field:'plan2'},
					{width:'110px',text:'预约3',type:'text',field:'plan3'},
					{width:'110px',text:'预约4',type:'text',field:'plan4'},
					{width:'110px',text:'预约5',type:'text',field:'plan5'},
					{width:'80px',text:'结果',type:'select',field:'resultId',data: pageVars.results },
					{width:'100px',text:'备注',type:'text',field:'comments'},
				    {type:'hidden',field:'id'}
					],
					container:"detailTable",
					id:"pes.grid",
					imagePath:pageVars.imagePath,
					tableCss:'edit-grid'
			});
			this.grid.draw();

			
		},
		
		showData:function (d){

			

			
		},
		disableBtn:function (b){
			$('saveBtn').disabled = b; 	
		}
		
		
});




