var PPage = new Class({
		Extends: PBasePage,
		initialize: function (configs) {			
			this.initVars(configs);
			this.init();
		},
	
		init : function(){
			
			this.initBase();
			

		
		    $$("input").set('readonly',true);
			
			var $this = this ;
			
			
			
			$this.vars.attachments = [] ;

			//c();
		},//init ends
		
		
		
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
		},
		showPic  : function(p,id){
			//put to ui 
			$$(".photo a img").set('src',pageVars.base+"/app/teacher/icon?fp="+encodeURIComponent( p  ) +"&id="+id);
		},
		updateAttachments: function(){
			$$("#attachmentDiv a").removeEvent('click');
			var html = "" ;
			this.vars.attachments.each ( function( el ) 
			{
				html += lang.attachmentItem.substitute ( el ) ;
			});

			if(this.vars.attachments.length==0){
				html = lang.noneAttachment;
			}

			var $this = this ;

			$("attachmentDiv").set('html',html);

			$$("#attachmentDiv a.download").addEvent('click', function( e ) {
				var data= this.get('data');
				var dataId = this.get('dataId');
				//find the item remove it 
				window.open( pageVars.base+'/app/teacher/file?id='+dataId+'&fp='+encodeURIComponent( data) ) ;

			});

			$('spanQty').set('html',$this.vars.attachments.length ) ;
			
		}
		
});

