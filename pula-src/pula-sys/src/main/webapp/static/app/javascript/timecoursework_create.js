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

			$('uploadPic').addEvent('click',function(e){ e.stop();   $this.uploadPic(lang.uploadPic,1) } );
			// $('uploadFile').addEvent('click',function(e){ e.stop();   $this.uploadPic(lang.uploadFile,0) } );
			
			$this.vars.attachments = [] ;
			//c();
		},//init ends
		
		uploadPic:function(_title,_type){
			Mbox.open({
				content:this._uploadFrame(_type),
				ajax: true,
				width:560,
				height:100,
				title:_title
			});	
			pageVars.uploadType = _type ;
		},
		_uploadFrame : function(pType){
			
			if(this.uploadFrame == null){
				var url = pageVars.base + "/app/fileupload/upload?type="+pType;

				this.uploadFrame = new IFrame({
				src: url,
				styles: {
					width:560,
					height: 100,
					border: 0
				},
				'class': 'mbox_ajax_iframe',
				frameborder: 0
				});
			}

			return this.uploadFrame;
		},
		initToolBar : function(){
			var _buttons = [] ;
			var tb = new PA.TToolBar({
				container:'__top',
				title:lang.domain,
				buttons: _buttons
			});
		},
		
		
		prepareData : function(){

			$('jsonAttachment').value = JSON.encode( this.vars.attachments ) ;
			
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
			// this.disableBtn(true);
			e.stop();
			
			PA.ajax.gf( this.vars.action ,$(this.config.addForm).toQueryString(),  function (e){
				// this.disableBtn(false);
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
				
			$('work.no').value = "0";

			//img
			$$(".photo a img").set('src',pageVars.noPhoto);
			this.vars.attachments = [] ;
			this.updateAttachments();
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
			var url = pageVars.base+"/app/timecoursework/icon?fp="+encodeURIComponent( p  ) +"&id="+id+"&sub=timecoursework";
			$$(".photo a img").set('src', url);
			$('openInNew').addEvent('click',function(){
				window.open(url, url, 'window settings');
				  return false;
			});
		},
		updateAttachments: function(){
//			$$("#attachmentDiv a").removeEvent('click');
//			var html = "" ;
//			this.vars.attachments.each ( function( el ) 
//			{
//				html += lang.attachmentItem.substitute ( el ) ;
//			});
//
//			if(this.vars.attachments.length==0){
//				html = lang.noneAttachment;
//			}
//
//			var $this = this ;
//
//			$("attachmentDiv").set('html',html);
//			$$("#attachmentDiv a.remove").addEvent('click', function( e ) {
//				var data= this.get('data');
//				//find the item remove it 
//				var find = null;
//				$this.vars.attachments.each ( function( fel){
//					if(find ==null && fel.fileId == data ){
//						find = fel ;
//					}
//				});
//				if(find != null){
//					//remove
//					$this.vars.attachments.erase( find ) ;
//					$this.updateAttachments();
//				}
//
//			});
//			$$("#attachmentDiv a.download").addEvent('click', function( e ) {
//				var data= this.get('data');
//				var dataId = this.get('dataId');
//				//find the item remove it 
//				window.open( pageVars.base+'/app/student/file?id='+dataId+'&fp='+encodeURIComponent( data) ) ;
//
//			});
//
//			$('spanQty').set('html',$this.vars.attachments.length ) ;
		}
		
});
function executeSelect(fp,fn){
	//$('tokenTrial.fileName').value = fn ;
	//$('tokenTrial.filePath').value = fp ;
	//$('fileName').set('html',fn).removeClass('none');
	//$('tokenTrial.uploadFile').value = 'true';
	//$('tokenTrial.emptyFile').value = 'false';
	//alert(fp+"::"+fn);
	var const_icon_type = 14;
	var const_etc_type = 12;
	var this_one = { fileId: fp , fileName : fn, type : const_icon_type} ;

	if(pageVars.uploadType == 1 ){
		//replace old icon 
		var find = null;
		pes.vars.attachments.each ( function(el){
			if(find == null && el.type == const_icon_type){
				find = el;
			}
		});
		if(find!=null){
			pes.vars.attachments.erase( find ) ;
		}

		pes.vars.attachments = [this_one].concat( pes.vars.attachments ) ;
		
		//show the pics
		pes.showPic( fp,0 ) ;

	}else{
		this_one.type = const_etc_type ; 
		pes.vars.attachments.push( this_one ) ;
	}
	pes.updateAttachments();
	Mbox.close();
}

window.onbeforeunload= function(){
	top.window.opener.reloadData() ;
}
