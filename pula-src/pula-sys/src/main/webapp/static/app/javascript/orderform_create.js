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

			if($("imgNew")){
				$("imgNew").addEvent('click',function(){
					Mbox.open( {
						url:'divStudent',
						title:'创建学生信息'
					});

					setTimeout( function(){
						$('student.name').focus();
					},400);
				});
			}

			this.edtStudent = new TSimpleNo({
				field:'form.studentNo',
				field_id:'cStudentNo',
				text:'',
				value:'',
				showTips:true,
				tips:lang.tipsOfTSN,
				url:'../student/find',
				container:'sStudent'
			});	

			this.master = new TSimpleNo({
				field:'form.masterSalesmanNo',
				field_id:'cmSalesmanNo',
				text:'',
				value:'',
				showTips:true,
				tips:lang.tipsOfTSN,
				url:'../salesman/find',
				container:'smsm'
			});	

			

			this.slave = new TSimpleNo({
				field:'form.slaveSalesmanNo',
				field_id:'csSalesmanNo',
				text:'',
				value:'',
				showTips:true,
				tips:lang.tipsOfTSN,
				url:'../salesman/find',
				container:'sssm'
			});
			this.edtTeacher = new TSimpleNo({
				field:'form.teacherNo',
				field_id:'cTeacherNo',
				text:'',
				value:'',
				showTips:true,
				tips:lang.tipsOfTSN,
				url:'../teacher/find',
				container:'sTeacherNo'
			});	
			this.edtStudent.on('blur',function(){
				//load more info about student
				if($this.vars.studentNo == $('cStudentNo').value  ){
					//skip
					return ;
				}
				$this.vars.studentNo = $('cStudentNo').value.trim();
				
				if(isEmpty( $this.vars.studentNo)){
					return ;
				}

				$$(".ready-for-student").set('html','尚未选取学员').setStyle('color','#ccc');
				$this.showPic('default');
				PA.ajax.gf('../student/getByNo','no='+encodeURIComponent( $this.vars.studentNo ) + '&orderFormId='+pageVars.id, function(ed){
					if(ed.error) {alert (ed.message); return; }
					var data =ed.data;
					$this.showStudent(data);
				});
			});
			$("frmStudent").addEvent('submit',function(e){
				e.stop();
				if( !checkStudent()){
					return ;
				}
				//send to remote
				PA.ajax.gf(pageVars.base+'/app/student/_createLite', $('frmStudent').toQueryString(), function(ed){
					if(ed.error){ //alert(ed.message); 
						return ;} 
					//data
					var data = ed.data;
					$this.showStudent(data);
					$this.edtStudent.setValue( data.no , data.name);
					Mbox.close();
					
				});


			}.bind(this));
			$('form.courseProductId').addEvent('change',function(){
				if( this.value == 0 ){
					$$(".ready-for-product").set('html','尚未选取产品').setStyle('color','#ccc');
				}else{
					var opt = this.options[this.selectedIndex];
					var p = $(opt).get('price');
					$this.vars.price = p ;
					$this.calcLeftPrice();
					$('spanPrice').set('html', p ) ;
					$('spanCourseCount').set('html', $(opt).get('courseCount'));
					$$(".ready-for-product").setStyle('color','');
				}
			});
			
			$$(".rbPayStatus").addEvent('click',function(){
				if(this.value == pageConsts.PS_PREPAY){
					$$(".prepay-ui").removeClass('h');
				}else{
					$$(".prepay-ui").addClass('h');
				}
			});

			$('form.prepay').addEvent('change',function(){
				$this.calcLeftPrice();
			});

			this.resetForm();

		},//init ends
		showStudent : function(data){
			$('spnGender').set('html', data.genderName );
			$('spnAge').set('html',this.makeAge( data.birthday ));
			if(data.iconId){
				this.showPic ( data.iconFileId , data.iconId);
			}
			$$(".ready-for-student").setStyle('color','');
	
		},
		calcLeftPrice :function(){
			var p = 0 ;
			if( this.vars.price ){
				 p= this.vars.price - Number.from( $('form.prepay').value);
			}
			if(isNaN(p)){
				p=0;
			}
			$('divLeftPrice').set('html',p.round(2));
		},

		showPic  : function(p,id){

			if(p=='default'){
				$$(".photo a img").set('src',pageVars.base+"/static/app/images/nophoto.jpg");
			}else{
			//put to ui 
				$$(".photo a img").set('src',pageVars.base+"/app/student/icon?fp="+encodeURIComponent( p  ) +"&id="+id);
			}
		},
		makeAge: function(dt){
			if(dt==null || dt == 0 ){
				return "N/A";
			}
			var y = (new Date()).getFullYear()- (new Date(dt)).getFullYear() ;
			return y ;
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
						top.window.opener.reloadData() ;
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
			var $this= this;
			$('addForm').reset();
				
			$('form.id').value = "0";

			$$(".ready-for-student").set('html','尚未选取学员').setStyle('color','#ccc');
			$this.showPic('default');
	
			$$(".ready-for-product").set('html','尚未选取产品').setStyle('color','#ccc');	

			$$(".prepay-ui").addClass('h');

			this.edtTeacher.reset();
			this.edtStudent.reset();
			this.master.reset();
			this.slave.reset();
		},
		mock : function(){
			
		}
		
});