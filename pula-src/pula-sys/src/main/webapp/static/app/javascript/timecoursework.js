var PPage = new Class({
		Extends: PBasePage,
		initialize: function (configs) {			
			this.initVars(configs);
			this.init();
		},
	
		init : function(){
			this.initBase({focusField:'course.courseNo'});
			this.initToolBar({add:true,remove:true,condition:true});
			this.initSaveForm();
			this.initSearchForm();
			this.initViewTable();
			this.initHistory();
			
			this.showCondition();
			
			this.vars.attachments = [] ;

			loadCalCss('.dateField');
			
			$('uploadPic').addEvent('click',function(e){ e.stop();   $this.uploadPic(lang.uploadPic,1) } );
			// $('uploadFile').addEvent('click',function(e){ e.stop();   $this.uploadPic(lang.uploadFile,0) } );
			

			var $this = this;

			this.reload();

			//c();
		},//init ends


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
					this.updatePage(data.id);
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
					// {label:lang.no,width:200,key:'no'},
					{label:lang.courseNo,width:200, key:'courseNo'},
					{label:lang.studentNo,width:200, key:'studentNo'},
					{label:lang.workEffectDate, width:200, key:'workEffectDate'},
					{label:lang.comments,width:200, key:'comments'},
//					{label:lang.image,width:200, key:'imagePath'},
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
	        var url = encodeURIComponent('../timecoursework/view?id=' + id);
	        centerWindow('../my/window?u=' + url,1100, 580);
	    },

		// by default view data in current page
		showData:function (d){
			/*$('addForm').reset();
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

					var data =ed.data; 
					for( var k in data ) {
						if($('course.'+k)){
							$('course.'+k).value = PA.utils.defaultStr(data[k]);
						}
					}

					this.showInput(true);
					this.vars.action='_update';
					pageVars.id  = d.id;
					this.updateMode();
					
				}.bind(this));
			}*/
		},
		
		
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
		
		showPic  : function(p,id){
			//put to ui 
			$$(".photo a img").set('src',pageVars.base+"/app/image/icon?fp="+encodeURIComponent( p  ) +"&id="+id);
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
		},
		
		createPage : function(b){
	        var url = encodeURIComponent('../timecoursework/create');
	        centerWindow('../my/window?u=' + url,1100, 480);
		},
		updatePage : function(id ){
			var url = encodeURIComponent('../timecoursework/update?id='+id);
			centerWindow('../my/window?u=' + url,1100, 480);
		},view : function(id ){
			var url = encodeURIComponent('../timecoursework/view?id='+id);
			centerWindow('../my/window?u=' + url,1100, 480);
		}
});

function executeSelect(fp,fn){
	//$('tokenTrial.fileName').value = fn ;
	//$('tokenTrial.filePath').value = fp ;
	//$('fileName').set('html',fn).removeClass('none');
	//$('tokenTrial.uploadFile').value = 'true';
	//$('tokenTrial.emptyFile').value = 'false';
	//alert(fp+"::"+fn);
	var const_icon_type = 20 ;
	var const_etc_type = 22 ;
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



var WORKER_TPL = "<a href='javascript:pes.eraseWorker(\"{no}\")'>{no} {name}<input type='hidden' name='course.courseNos' value='{no}'/></a>";
var pes = null ;
window.addEvent('domready',function(){
	pes = new PPage({"id":"pes","pageMode":"pageMode","searchForm":"searchForm","addForm":"addForm"});
	//pes.mockCheck();
	//alert('final:'+$("conditionDiv").className);
});


