

PlanCalendar = function(){
	this.initCalendar();
};

PlanCalendar.prototype = {
	initCalendar: function(){
			var $this = this ;
			var getDateText = function(d){
				return pageVars.year+"-"+(pageVars.month+1)+"-"+ d ;
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
			//$('selYear').set('value',pageVars.year);
			//$('selMonth').value = pageVars.month ;
			
			//days

			var nDays = monthDays[ pageVars.month ];
			var today = new Date(pageVars.year,pageVars.month,1);
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
			$$('#calendar .calendar_container').set('html',sb);

			$$("#calendar div.items").addEvent('mouseover',function(){
				if(pageVars.canSetup){
					$this.vars.indicator.inject ( this ) ;
				}
			}).addEvent('click' ,function(){
				//take the value 
				//var day = this.get('data') ;
				//data 
				//$this.showDateInput( day ) ;
				//$this.vars.day = day ; 

			});
		},
		initData : function(){
			var $this = this ;
			$$(".container").addClass('h');
			PA.ajax.gf('_calendar','year='+pageVars.year+'&month='+pageVars.month, function(e,x){
				$$(".container").removeClass('h');
				$$("input").set('disabled',false);
				if(e.error){
					alert(e.message);
					return ;
				}

				//clear ui 
				$$(".items div").set('html',"<a class='empty'></a>");
				
				//load it to ui
				for(var kk in e.data){
					var k = e.data[kk];
					//var css ="#day"+k +" .items h3";
					//var elements = $$(css);

					var divs = $$("#day"+kk +" .items div");

					
					var sb = buildPeriodBlock( k ) ;
					divs.set('html',sb);
					
							
				}//for k in data

			
			});

		}

}


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