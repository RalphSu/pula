var PPage = new Class({
		Extends: PBasePage,
		initialize: function (configs) {			
			this.initVars(configs);
			this.init();
		},
	
		init : function(){
			
			this.initBase();
			this.initToolBar(true);
			this.initViewTable();
			
			
			var $this = this ;
			
			this.reload();

		},//init ends

		initViewTable : function(){
			var getRequestParam = function(){
				return "&id="+pageVars.id;
			}
			var onSelectRow = function(i,tr){
				if(i==-1){
					this.navigateHistory("id", 'create');
				}else{
					var data = this.dt.rows[i] ;
					if(data.workId){
						this.viewPic(data.workId);
					}
				}
			}

			
			var cfgs = {
				id :'dt',
				container:'dt',
				height:100,
				url:"../coursetaskresultwork/listInResult",
				requestParam:getRequestParam.bind(this),
				intoRow:onSelectRow.bind(this),noFooter:true,
				columns: [
					//{label:TTable.checkAll,width:24,key:'id',formatter:TTable.formatCheckbox},
					{label:'学生编号',width:80,key:'studentNo',},
					{label:'学生姓名',width:80,key:'studentName'},
					{label:'作品下载',width:80,key:'workId',formatter:TTable.formatLinkJs.bind({func:'pes.viewPic',label:'下载'}),align:'center'},
					{label:'评分1',key:'score1',width:60,align:'center'},
					{label:'评分2',key:'score2',width:60,align:'center'},
					{label:'评分3',key:'score3',width:60,align:'center'},
					{label:'评分4',key:'score4',width:60,align:'center'},
					{label:'评分5',key:'score5',width:60,align:'center'},
					{label:'评分时间',width:160,key:'scoreTime',formatter:TTable.formatDateTime},
				]
			};

			this.dt = new TTable(cfgs);		
			
			this.dt.draw();

		},

		
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
		viewPic  : function(id){
				//find the item remove it 
			window.open( pageVars.base+'/app/coursetaskresultwork/file?id='+id) ;
		}
		,updateMode:function(){

		}
		
});
function executeSelect(fp,fn){
	//$('tokenTrial.fileName').value = fn ;
	//$('tokenTrial.filePath').value = fp ;
	//$('fileName').set('html',fn).removeClass('none');
	//$('tokenTrial.uploadFile').value = 'true';
	//$('tokenTrial.emptyFile').value = 'false';
	//alert(fp+"::"+fn);
	//var const_icon_type = 20 ;
	//var const_etc_type = 22 ;
	//var this_one = {  fileId: fp , fileName : fn, type : const_icon_type} ;
	var all = "id="+pageVars.courseTaskResultStudentId+"&form.fileId="+fp+"&form.fileName="+fn;

	PA.ajax.gf('../coursetaskresultwork/_replaceWork',all,function(e){
		if(e.error) { alert (e.message); return ;}
		pes.reload();
	});
	Mbox.close();
}

window.onbeforeunload= function(){
	try{
		top.window.opener.reloadData() ;
	}catch(e){
	}
}
