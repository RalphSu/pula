var PPage = new Class({
		Extends: PBasePage,
		initialize: function (configs) {			
			this.initVars(configs);
			this.init();
		},
	
		init : function(){
			
			this.initBase();
			this.initToolBar(true);

			
			//new autoSuggest(pageVars.base+'/suggest', 'keywords','');
			if(pageVars.updateMode){
				this.vars.action='_update';
			}else{
				this.vars.action='_create';
			}

			loadCalCss('.dateField');
			var $this = this ;
			
			$(this.config.addForm).addEvent('submit',function(e){
				this.sendToServer(e);
			}.bind(this));

			$('saveBtn').addEvent('click',function(){
				$('submitBtn').click () ;
			});

		},//init ends
		
		initToolBar : function(){
			var _buttons = [] ;
		

			
			var tb = new PA.TToolBar({
				container:'__top',
				title:lang.domain,
				buttons: _buttons
			});


			
		

		},
		
		
		prepareData : function(){

			var data = [] ; 
			$$(".teacher_points").each (function(el){
				data.push({ 'points': el.value , 'teacherId':el.get('data')});
			});



			$('json').value = JSON.encode( data ) ;
			debugger;
			
		},
		showData:function (d){
			
		},
		disableBtn:function (b){
			$('saveBtn').disabled = b; 	
		},
		sendToServer :function(e){
			if(!check()){
				e.stop();
				return ;
			}
			this.prepareData () ;
			this.disableBtn(true);
			e.stop();
			
			PA.ajax.gf( this.vars.action ,$(this.config.addForm).toQueryString(),  function (e){
				this.disableBtn(false);
				if(e.error){ alert (e.message) ; return }
				
				if(pageVars.updateMode){
					try{
						
						
					}catch(e){
						//alert(e);
					}		
					top.window.close();
				}else{
					this.resetForm(true);
				}
				//$('stockIn.type').focus();
				topSuccess();
			}.bind(this));
		},
		resetForm: function(b){
			
			$('addForm').reset();
				
			$('trainLog.id').value = "0";

		
		},
		mock : function(){
			/*$('stockIn.type').value = '1';
			$('stockIn.orderDateText').value = '2012-12-10';
			this.edtWorker.setValue('00013','mock');
			this.edtCompany.setValue('001','client');
			$('stockIn.warehouseId').value = 1;
			$('stockIn.comments').value = "mock_data";
			$('stockIn.warehouseId').fireEvent('change');

			var items = [
			{materialNo:'02.02.002.1370152',quantity:1,space:1}]
			this.grid.fill(items);*/
			
			//this.vars.attachments.push ( { fileId: 'aaid' , fileName : 'file.abc', type : 1}  );
			//this.updateAttachments();
		}
		
});