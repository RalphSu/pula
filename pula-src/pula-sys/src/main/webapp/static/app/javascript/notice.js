var PPage = new Class({
		Extends: PBasePage,
		initialize: function (configs) {			
			this.initVars(configs);
			this.init();
		},
	
		init : function(){
			this.initBase({focusField:'course.courseNo'});
			this.initToolBar({add:true,remove:true,condition:false});
			this.initSaveForm();
			this.initSearchForm();
			this.initViewTable();
			this.initHistory();
			
			this.showCondition();
			
			this.vars.attachments = [] ;

			loadCalCss('.dateField');
			
//			$('uploadPic').addEvent('click',function(e){ e.stop();   $this.uploadPic(lang.uploadPic,1) } );
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
					{label:lang.no,width:200,key:'no'},
					{label:lang.title, width:400, key:'title'},
					{label:lang.comment,width:200,key:'comment'},
					// {label:lang.updateTime,width:120,key:'updateTime',formatter:TTable.formatDate},
					{label:lang.status,width:40,key:'enabled',formatter:TTable.formatEnabled}
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
	        var url = encodeURIComponent('../notice/view?id=' + id);
	        centerWindow('../my/window?u=' + url,1100, 580);
	    },

		showData:function (d){
			
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
		},
		
		createPage : function(b){
	        var url = encodeURIComponent('../notice/create');
	        centerWindow('../my/window?u=' + url,1100, 480);
		},
		updatePage : function(id ){
			var url = encodeURIComponent('../notice/update?id='+id);
			centerWindow('../my/window?u=' + url,1100, 480);
		},view : function(id ){
			var url = encodeURIComponent('../notice/view?id='+id);
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
});


