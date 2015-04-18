var PPage = new Class({
		Extends: PBasePage,
		initialize: function (configs) {			
			this.initVars(configs);
			this.init();
		},
	
		init : function(){
			
			this.initBase();
			
			this.initToolBar();
		
		    $$("input").set('readonly',true);
			
			var $this = this ;
			
		

			//c();
		},//init ends
		
		initToolBar : function(){
			var _buttons = [] ;
		

			
			var tb = new PA.TToolBar({
				container:'__top',
				title:lang.domain,
				buttons: _buttons
			});


			
		

		},
		
		showData:function (d){

			

			
		},
		disableBtn:function (b){
			$('saveBtn').disabled = b; 	
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

