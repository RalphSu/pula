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
				$('tblStudent').removeClass('h');
				this.initViewTable();
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

			
			this.edtMasterNo = new TSimpleNo({
				field:'form.masterNo',
				field_id:'cMasterNo',
				text:'',
				value:'',
				showTips:true,
				tips:lang.tipsOfTSN,
				url:'../teacher/find',
				container:'sMasterNo'
			});	
			this.edtAssistant1No = new TSimpleNo({
				field:'form.assistant1No',
				field_id:'cAssistant1No',
				text:'',
				value:'',
				showTips:true,
				tips:lang.tipsOfTSN,
				url:'../teacher/find',
				container:'sAssistant1No'
			});		
			this.edtAssistant2No = new TSimpleNo({
				field:'form.assistant2No',
				field_id:'cAssistant2No',
				text:'',
				value:'',
				showTips:true,
				tips:lang.tipsOfTSN,
				url:'../teacher/find',
				container:'sAssistant2No'
			});	
			
			this.edtStudentNo = new TSimpleNo({
				field:'studentNo',
				field_id:'cStudentNo',
				text:'',
				value:'',
				showTips:true,
				tips:lang.tipsOfTSN,
				url:'../student/find',
				container:'sStudentNo'
			});	

			$('form.classroomId').addEvent('change',function(){
				if( this.value =="" || this.value =="0"){
					$('courseTips').removeClass('h');
					$('courseDiv').addClass('h');
				}else{
					$('courseTips').addClass('h');
					$('courseDiv').removeClass('h');
				}
			}).fireEvent('change');

			var obj = new TSelectLoader( {'leader':'categoryId','fireChangeAfterLoad':true,
					'uri':pageVars.base+'/app/coursedeployment/listByClassroom',
					'params':function(v){
						return "categoryId="+v+"&classroomId="+$F('form.classroomId');
					},'allowReload':function(){
						var v= $F('form.classroomId');
						if(v==""||v=="0"){
							return false;	
						}
						return true;
					},'defaultValue':['categoryId' ,pageVars.courseId]}
			);

			//还有个教室?! 总部根据分部来加载教室.TODO


			//保存学生
			$("saveForm").addEvent('submit',function(e){
				e.stop();
				if( !checkStudent()){
					return;
				}
				$('saveBtn2').set('disabled',true);
				PA.ajax.gf('../coursetaskresultstudent/_createStudent', $('saveForm').toQueryString()+"&id="+pageVars.id, function(ed){
					$('saveBtn2').set('disabled',false);
					if(ed.error){
						alert(ed.message);
						return;
					}
					$this.edtStudentNo.reset();
					topSuccess();
					$this.reload();

				});
			});

			if(pageVars.updateMode){
				this.reload();
			}

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
					//this.showData(data);
					if(!data.allowEdit){
						this.view(data.id);
					}else{
						this.manage(data.id);
					}
				}
			}

			
			var cfgs = {
				id :'dt',
				container:'dt',
				height:100,
				url:"../coursetaskresultwork/listInResult",
				requestParam:getRequestParam.bind(this),
				selectRow:onSelectRow.bind(this),noFooter:true,
				columns: [
					//{label:TTable.checkAll,width:24,key:'id',formatter:TTable.formatCheckbox},
					{label:'学生编号',width:80,key:'studentNo',},
					{label:'学生姓名',width:80,key:'studentName'},
					{label:'作品下载',width:80,key:'workId',formatter:TTable.formatLinkJs.bind({func:'pes.viewPic',label:'下载'}),align:'center'},
					{label:'作品上传',width:80,key:'id',formatter:TTable.formatLinkJs.bind({func:'pes.uploadPicProxy',label:'上传'}),align:'center'},
					{label:'评分1',key:'score1',width:60,align:'center'},
					{label:'评分2',key:'score2',width:60,align:'center'},
					{label:'评分3',key:'score3',width:60,align:'center'},
					{label:'评分4',key:'score4',width:60,align:'center'},
					{label:'评分5',key:'score5',width:60,align:'center'},
					{label:'评分时间',width:160,key:'scoreTime',formatter:TTable.formatDateTime},
					{label:'管理',width:40,key:'id',formatter:TTable.formatLinkJs.bind({func:'pes.remove',label:'删除'}),align:'center'}
				]
			};

			
			cfgs.selectRow = null ;

			this.dt = new TTable(cfgs);		
			
			this.dt.draw();

		},

		remove:function(id){
			if(!confirm("确认删除?")){
				return ;
			}
			var $this =this;
			//remove it
			PA.ajax.gf('../coursetaskresultstudent/_removeStudent','id='+id,function(ed){
				if(ed.error){
					alert(ed.message);
					return ;
				}
				//删除成功

					topSuccess();
					$this.reload();
			});
		},
		uploadPicProxy : function(id){
			pageVars.courseTaskResultStudentId = id ;
			this.uploadPic(lang.uploadFile,0);
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
		sendToServer :function(e){
			
			if(!check()){
				e.stop();
				return ;
			}
			
			this.disableBtn(true);
			e.stop();
			
			PA.ajax.gf( this.vars.action ,$(this.config.addForm).toQueryString(),  function (e){
				this.disableBtn(false);
				if(e.error){ alert (e.message) ; return }

				if(!pageVars.updateMode){
					$('tblStudent').removeClass('h');
					$$(".t-title").set("html","修改授课记录");
					pageVars.updateMode = true ;					
					this.vars.action='_update';
					$('form.id').value = e.data ;
					pageVars.id = e.data;
					
					this.initViewTable();

				}
				
				/*if(pageVars.updateMode){
					try{
						
						
					}catch(e){
						//alert(e);
					}		
					top.window.close();
				}else{
					this.resetForm(true);
				}*/
				//$('stockIn.type').focus();
				topSuccess();
			}.bind(this));
		},
		resetForm: function(b){
			
			$('addForm').reset();
				

			//img
			$$(".photo a img").set('src',pageVars.noPhoto);
			this.vars.attachments = [] ;
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
