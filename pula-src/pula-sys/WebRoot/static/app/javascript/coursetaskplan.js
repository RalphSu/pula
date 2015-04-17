var PPage = new Class({
		Extends: PBasePage,
		initialize: function (configs) {			
			this.initVars(configs);
			this.init();
		},
	
		init : function(){
			this.initBase();
			this.initToolBar({remove:false});
			//this.initCalendar();

			this.initButtons();
			var $this = this; 
			//this.reload();

			$$('#selYear,#selMonth').addEvent('change',function(){
				$('btnGoto').removeClass('h');
			});
			
			this.changeMonth();
			
			$('frmPost').addEvent('submit',function(e){
				e.stop();
				//check date
				if($this.getQueryString()==null){
					return ;
				}

				//$('saveBtn').set('disabled',true);
				$$("input[type=button],input[type=submit]").set('disabled',true);
				PA.ajax.gf('_setupBatch',$('frmPost').toQueryString(),function(ed){
					$$("input[type=button],input[type=submit]").set('disabled',false);
					if(ed.error){
						alert(ed.message);
						return;
					}
					topSuccess();
					//更新界面
					$this.initData() ;
				});	
			});
			if($('frmSetup')){
				$('frmSetup').addEvent('submit',function(e){
					e.stop();
					//check date
					if(!checkSetup()){
						return ;
					}

					//$('saveBtn').set('disabled',true);
					$$("input[type=button],input[type=submit]").set('disabled',true);
					PA.ajax.gf('_update',$('frmSetup').toQueryString(),function(ed){
						$$("input[type=button],input[type=submit]").set('disabled',false);
						if(ed.error){
							alert(ed.message);
							return;
						}
						//alert(lang.sendOk);
						//topSuccess();
						//更新界面
						//$this.initData() ;

						$this.updateEmployees( ed.data ) ;
						//$('fEmployee').set('value','');
						//$('frmSetup').reset();
						$this.teacherNo.reset();
						$this.assistant1No.reset();
						$this.assistant2No.reset();

					});	
				});
			

				

				this.vars.indicator = $('indicator_add') ;
				this.vars.indicator.addEvent('click',function(e){
					var h3 = this.getParent('div');
					//alert(h3.get('data'));
					//if(h3){
					//	h3.fireEvent('click');
					//}
					//$this.showDateInput( h3.get('data') );
				});

				$('cancelBtn').addEvent('click',function(){
					
					$('frmSetup').addClass('h');
				});

				$('btnSummary').addEvent('click',function(){
					//show the data 
					var url = encodeURIComponent('../attendanceschedule/info?year='+pageVars.year + "&month="+pageVars.month);
					centerWindow('../my/window?u='+url,650,500);
				});
				
				this.teacherNo = new TSimpleNo({
					field:'form.teacherNo',
					field_id:'cTeacherNo',
					text:'',
					value:'',
					showTips:true,
					tips:lang.tipsOfTSN,
					url:'../teacher/find',
					container:'sTeacherNo'
				});	
				this.assistant1No = new TSimpleNo({
					field:'form.assistant1No',
					field_id:'cAssistant1No',
					text:'',
					value:'',
					showTips:true,
					tips:lang.tipsOfTSN,
					url:'../teacher/find',
					container:'sAssistant1No'
				});	
				this.assistant2No = new TSimpleNo({
					field:'form.assistant2No',
					field_id:'cAssistant2No',
					text:'',
					value:'',
					showTips:true,
					tips:lang.tipsOfTSN,
					url:'../teacher/find',
					container:'sAssistant2No'
				});	



				$this.vars.courseList = {};
				$this.vars.courseCategory = {} ;
				$this.vars.courseId = {} ;
				
				$("courseCategoryId").addEvent('change' ,function(){
					/*var v = $('classroomId').value ;
					if(isEmpty(v)){
						alert('请先选取教室');
						return ;
					}*/
					$this.loadCourse( this,$this.loadCourseToSelect.bind([$this,this]) );
				}).fireEvent('change');

				$('classroomId').addEvent('change',function(){
					var v = $('classroomId').value ;
					if(isEmpty(v)){
						$('frmSelect').addClass('h');
						$('lblSelect').removeClass('h');
					}else{
						$('frmSelect').removeClass('h');
						$('lblSelect').addClass('h');

					}
				}).fireEvent('change');
			}
			
			if($('branchId')){

				$('branchId').addEvent('change',function(){
					window.location = "?year="+pageVars.year +"&month="+pageVars.month+"&branchId="+$F("branchId");
				});
			}
		},//init ends
		loadCourse : function(sender,_callback){
			var $this = this ;
			var wid= sender.value;
			var sid = sender.get('id');

			var v = $('classroomId').value ;
			if(isEmpty(v)){
				return ;
			}
			
			//var shouldReload =false;
			if( !this.vars.courseCategory[sid]  || ( this.vars.courseCategory[sid] && this.vars.courseCategory[sid] != wid ) ){
				//shouldReload = true ;
			}else{
				return ;
			}

			if(isEmpty(wid)){
				wid= "0";
			}

			//ajax
			PA.ajax.gf(pageVars.base+'/app/coursedeployment/listByClassroom',
				'categoryId='+ wid +'&classroomId='+v,
				function( e ) {
					if(e.error){ alert (e.message) ; return ;} 
					//load
					$this.vars.courseList[ sid ]  = e.data ;
					_callback();
				}
			);


		},
		loadCourseToSelect : function( sel ,sender) {
			var $this = this ;
			if(!sender){
				sender =$this[1].get('id');
				$this = $this[0];
			}
			
			if( typeof( $this.vars.courseList[sender]) == "undefined"){
				return ;
			}

			var _loadSpaceToSelect = function ( obj ) {
				PA.utils.clearSelect( obj ) ;

				var opt = new Element('option',{   
					 text: '请选取...',
					 value: ''
				});
				obj.adopt(opt);
				$this.vars.courseList[sender].each ( function(el){
						var opt = new Element('option',{   
							 text: el.name,
							 value: el.id
						});
						if( $this.vars.courseId[sender] && $this.vars.courseId[sender]  == el.id ){
							opt.set('selected',true);
						}
						obj.adopt(opt);
				});
			}
			if(sel ){
				//load from remote ?
				//local data ;
				_loadSpaceToSelect( $(sel) );
			}else{
				$$("._"+sender).each ( function( ss ) {
					_loadSpaceToSelect( $(ss) );
				});
			}

		},

		remove : function (){
			var qs = this.getQueryString();
			if(qs == null ) return ;
			if(!confirm(_lang.removeConfirm)){
				return 
			}	
			PA.ajax.gf('_cancelDays',qs,function (e){
				if(e.error){
					topHiddenSuccess();
					alert(e.message);
				}else{
					topSuccess();
					this.reload();
				}			
			}.bind(this));
		},
		isCurrentMonth : function(){
			if(pageVars.nowMonth == pageVars.month  && pageVars.nowYear == pageVars.year){
				return true ;	
			}
			return false;
		},

		initButtons: function(){
			var $this = this; 
			/*$('btnAll').addEvent('click',function(){
				$$('.checkbox input').set('checked',true);
			});
			$('btnWorkDay').addEvent('click',function(){
				$$('.checkbox input').set('checked',true);
				$$('.weekend .checkbox input').set('checked',false);
			});
			$('btnWeekEnd').addEvent('click',function(){
				$$('.checkbox input').set('checked',false);
				$$('.weekend .checkbox input').set('checked',true);
			});
			$('btnReserve').addEvent("click",function(){
				$$('.checkbox input').each (function(el){
					el.set('checked',!el.get('checked'));
				});
			});
			$('btnClear').addEvent("click",function(){
				$$('.checkbox input').set('checked',false);
			});*/

			$("btnPrevMonth").addEvent("click",function(){
				var m = pageVars.month ;
				if(m <= 1 ){
					var y = pageVars.year - 1 ;
					if( y < pageVars.minOfYear){
						alert(lang.minOfYear);
						return ;
					}
					pageVars.year = y;
					pageVars.month = 12;
				}else{
					pageVars.month = m -1;
				}

				
				//fireChange
				$this.changeMonth () ;
			});
			$("btnNextMonth").addEvent("click",function(){
				var m = pageVars.month ;
				if(m >= 12){
					var y = pageVars.year + 1 ;
					if( y > pageVars.maxYear){
						alert(lang.maxOfYear);
						return ;
					}
					pageVars.year =y;
					pageVars.month = 1;
				}else{
					pageVars.month = m + 1;
				}
				//fireChange
				$this.changeMonth () ;
			});

			$("btnGoto").addEvent('click' ,function(){
				pageVars.year = $('selYear').value ;
				pageVars.month = $('selMonth').value ;
				$this.changeMonth () ;
			});
		},

		changeMonth : function(){
			$$("input").set('disabled',true);
			$('btnGoto').addClass('h');
			this.initCalendar();
			//remote part
			if( $('frmSetup')){
				this.initData();
				$('frmSetup').addClass('h');
				$('tblDayInfo').addClass('h');
			}
		},
		initData : function(){
			var $this = this ;
			$$(".container").addClass('h');
			PA.ajax.gf('_calendar','year='+pageVars.year+'&month='+pageVars.month+"&branchId="+pageVars.branchId , function(e,x){
				$$(".container").removeClass('h');
				$$("input").set('disabled',false);
				if(e.error){
					alert(e.message);
					return ;
				}

				//clear ui 
				$$(".items div").set('html',"<a class='empty'></a>");
				pageVars.canSetup = false;
				//load it to ui
				for(var k in e.data){
					if( k == 'canSetup') {
						pageVars.canSetup = !pageVars.forceView && e.data[k];
						continue ;
					}

					var css ="#day"+k +" .items h3";
					var elements = $$(css);

					var divs = $$("#day"+k +" .items div");

					var arr = e.data[k]['employee'];

					if(!arr|arr.length==0){
						//build empty
						divs.set('html',"<a class='empty'></a>");
						continue;
					}
					var sb = buildPeriodBlock( arr ) ;
					divs.set('html',sb);
					
					//rest info 
					/*for( var i = 1 ;i <=3 ; i ++){
	
						if(e.data[k][i]){
							elements[ i-1].addClass('restday').removeClass('workday');
						}else{
							elements[ i-1].addClass('workday').removeClass('restday');
						}
						
						var arr = e.data[k]['employee'+(i-1)];

						if(!arr){
							continue ;	
						}
						var sb = buildPeriodBlock( arr ) ;
						
						divs[ i-1].set('html',sb);

					} *///for				
				}//for k in data
				if( !pageVars.canSetup ){
					//$('removeLnk').addClass('h');
				}else{
					//$('removeLnk').removeClass('h');
				}

				//build ui
				$this.rebuildGrid();
			
			});

		},
		rebuildGrid :function(){
			var height = 0 ;
			var objs = [] ;
			var applyHeight = function( h,arr){
				arr.each ( function( el,idx){
					el.setStyle('height',h);
				});
			}
			$$(".container ul li").setStyle('height','');
			$$(".container ul li").each ( function (el,idx){
				
				height = Math.max(height, el.offsetHeight);
				objs.push( el );
				if( el.hasClass('block-f') || el.hasClass('block-e')){
					//apply the height
					applyHeight( height, objs );
					//end of row
					height = 0 ;
					objs =[];
				}

			});
		},
		
		initCalendar: function(){
			var $this = this ;
			var getDateText = function(d){
				return pageVars.year+"-"+pageVars.month+"-"+ d ;
			}
			function montharr(m0, m1, m2, m3, m4, m5, m6, m7, m8, m9, m10, m11) 
			{
			this[0] = m0;
			this[1] = m1;
			this[2] = m2;
			this[3] = m3;
			this[4] = m4;
			this[5] = m5;
			this[6] = m6;
			this[7] = m7;
			this[8] = m8;
			this[9] = m9;
			this[10] = m10;
			this[11] = m11;
			}
			var monthDays = new montharr(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31);
			var year = pageVars.year; 
			//闰年计算二月
			if (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0)) monthDays[1] = 29;

			var sb ="";
			//headers
			//sb = "<div>"+lang.calendarHeader.substitute({ 'year' : pageVars.year , 'month': pageVars.month })+"</div>";
			$('selYear').set('value',pageVars.year);
			$('selMonth').value = pageVars.month ;
			
			//days

			var nDays = monthDays[ pageVars.month - 1 ];
			var today = new Date(pageVars.year,pageVars.month-1,1);
			var firstDay = today ;
			firstDay.setDate(1);
			//var weekDay = firstDay.getDate();
			//if (weekDay == 2) firstDay.setDate(0);
			var startDay = firstDay.getDay();
			//write date
			sb += "<ul>";
			
			lang.weekDays.each ( function(el,index){
				sb += "<li class='weekday-block"
					+ ( ( (index+1) %7==0)? " block-f" : "")
					+	"'>"+el+"</li>";

				
			});

			for (i=0; i<startDay; i++) {
				sb += "<li class='none-block day-block";				
				sb+="'></li>";	
				//column++;
			}

			var totalBlocks = nDays+startDay ;
			var leftBlocks = 7 - (totalBlocks % 7);
			if(leftBlocks==7){
				leftBlocks=0;
			}
			
			for (i=1; i<=nDays; i++) {
				sb += "<li class='dayBlock day-block";
				if( (i+startDay) %7 == 0 ){
					sb+=" block-f weekend";
				}

				if( (nDays - i ) < 7-leftBlocks){
					sb+=" block-b";
				}
							
				if( ( i+startDay ) % 7 == 1 ){
					sb+= " weekend";
				}
				

				sb+="' id='day"+i+"'><div class='date'><label for='chk"+i+"'>"+i+"</label></div><div class='checkbox h'><input type='checkbox' value="
					+getDateText(i)+" name='date' id='chk"
					+i+"' class='objId'/></div>"
					+ "<div class='items' data='"+i+"'><div ></div></div>"
					+"</li>";
					
				if( (i+startDay) %7 == 0 ){
					sb += "<div class='c'></div>";
				}
			}

			//fix block
			
			for(i = 0 ; i < leftBlocks ; i ++){
				sb += "<li class='none-block day-block block-b";		
				if(i== leftBlocks -1 ){
					sb+=" block-e";
				}
				sb+="'></li>";
			}


			sb+="</ul>";
			//$$("#calendar h3").removeEvent('mouseover').removeEvent('click');
			$$('#calendar .container').set('html',sb);

			$$("#calendar div.items").addEvent('mouseover',function(){
				if(pageVars.canSetup){
					$this.vars.indicator.inject ( this ) ;
				}
			}).addEvent('click' ,function(){
				//take the value 
				var day = this.get('data') ;
				//data 
				$this.showDateInput( day ) ;
				$this.vars.day = day ; 

			});
		},
		updateEmployees :  function( list ) {
			
				$$('#spEmployee a').removeEvent('click');
				$('spEmployee').empty();

				var sb = "";
				list.each ( function(el){
					sb += "<a href='#' data='"+el.id+"' class='hover' ><div class='plan-text'>"+ el.no +"-"+ el.name + "</div><div class='plan-tips'>"+replaceToValue(el.comments)+"</div></a>";
				});

				$('spEmployee').set('html',sb) ;

				var $this = this; 
				if(pageVars.canSetup){
					$$('#spEmployee a').addEvent( 'click' , function ( ekk ) {
						
						ekk.stop();

						if(!confirm('确认删除该排班计划?')){
							return ;
						}
						//alert('ho');
						var ah = this ;
						//remove
						PA.ajax.gf ( '_cancel', 'id='+ this.get('data') , function(eee){
							if(eee.error){
								alert(eee.message);return ;
							}
							var parent = $('spnEmpl'+ah.get('data')).getParent("div.items div");
							var v = Number.from (  $('spEmployeeCount').get('html') ) - 1;
							$('spnEmpl'+ah.get('data')).dispose();
							if(v<=0){
								parent.set('html',"<a class='empty'></a>");
							}
							
							$('spEmployeeCount').set('html', v ) ;
							ah.dispose();
							$this.rebuildGrid()
						});

					});
				}else{
					$$('#spEmployee a').removeClass('hover');
				}

				//show data to ui 
				var divs = $$("#day"+this.vars.day +" .items div");

				var sb = buildPeriodBlock(list) ;
				if(sb==""){sb = "<a class='empty'></a>";}
				divs.set('html',sb);
				
				$('spEmployeeCount').set('html', list.length ) ;
				this.rebuildGrid();

		},
		showDateInput : function( day) {

			
			var $this = this ;
			var date = pageVars.year + "-"+ pageVars.month +"-"+ day ;
			//load data from db ;

			PA.ajax.gf( '_get','date='+date+"&branchId="+pageVars.branchId, function(e){
				//load it 
				if(e.error){ alert(e.message) ; return ; }
				//load to ui 

				//show the form 
				
				$('spDate').set('html',date  ) ;
	
				//$('fEmployee').set('value','');
				$('fDate').set('value',date);

				$this.updateEmployees ( e.data ) ;
				$('tblDayInfo').removeClass('h');
				if(pageVars.canSetup){
					$('frmSetup').removeClass('h');
				}
				//alert(pageVars.canSetup);
				if(pageVars.canSetup){
					$$(".inputRow").removeClass('h');
					//$('fEmployee').focus();
				}else{
					$$(".inputRow").addClass('h');
				}

			});

		},		
		showData: function(d){
			
		},
		reload : function(){
			this.initData();
			
			//this.updateMode();		
		},
		updateMode:function(){

		},
		disableBtn:function (b){
			
		}
	
});
var pes = null ;
window.addEvent('domready',function(){
	pes = new PPage({"id":"pes","pageMode":"pageMode","searchForm":"searchForm","addForm":"addForm"});
	//pes.mockCheck();
	//alert('final:'+$("conditionDiv").className);
});


var buildPeriodBlock = function(arr){
	var sb = "" ;
	arr.each ( function(ell,idx){								
		var text = ell.name;
		var idx = text.indexOf(','); 
		if(idx>0){
			text = text.substring(0,idx);
		}
		sb += '<a id="spnEmpl'+ell.id+'" data="'+ell.id+'">'+text;
		/*if( idx < arr.length - 1 ) {
			sb += ",";
		}*/
		sb +="</a>" ;
	});

	return sb ;
}


function replaceToValue(v){
	if(v){
		return v.replace(/'/g,'');
	}
	return "";
}