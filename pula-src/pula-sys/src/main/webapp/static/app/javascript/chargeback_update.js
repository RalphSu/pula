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
				this.vars.action='../chargeback/_update';
			}else{
				this.vars.action='../chargeback/_create';
			}
			
			var $this = this ;
			
			
			$(this.config.addForm).addEvent('submit',function(e){
				this.sendToServer(e);
			}.bind(this));

			$('saveBtn').addEvent('click',function(){
				$('submitBtn').click () ;
				
			});
			
			

			

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
				
				try{
					top.window.opener.reloadData() ;
				}catch(e){

				}

				top.window.close();
				
				//$('stockIn.type').focus();
				topSuccess();
			}.bind(this));
		},
		resetForm: function(b){
			var $this= this;
			$('addForm').reset();
				


			this.edtTeacher.reset();
			this.edtStudent.reset();
			this.master.reset();
			this.slave.reset();
		},
		mock : function(){
			
		}
		
});