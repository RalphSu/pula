var PPage = new Class({
		Extends: PBasePage,
		initialize: function (configs) {			
			this.initVars(configs);
			this.init();
		},
	
		init : function(){
			this.initBase();
			this.initToolBar(true);

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
				
			$('course.no').value = "0";

			//img
			$$(".photo a img").set('src',pageVars.noPhoto);
			this.vars.attachments = [] ;
			this.updateAttachments();
		},
		mock : function(){
		},
		showPic  : function(p,id){
			//put to ui 
			var url = pageVars.base+"/app/timecourse/icon?fp="+encodeURIComponent( p  ) +"&id="+id;
			$$(".photo a img").set('src', url);
			$('openInNew').addEvent('click',function(){
				window.open(url, url, 'window settings');
				  return false;
			});
		},
		updateAttachments: function(){
		}
		
});
function executeSelect(fp,fn){
	var const_icon_type = 16;
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
