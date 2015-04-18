NilCalendar = function(){
	//alert('hi');
	this.div = null ;
	this.target = null ;
	this.targets = new Array();
	
	this.init() ;
}

NilCalendar.prototype = {

	init : function () {
		this.div = document.createElement("div");
		this.div.id = "calendarDiv";
		$('calendarHere').appendChild(this.div);
		//alert(this.div);

		//var input = document.createElement("input");
		//$('calendarHere').appendChild(input);


		var mySelectHandler = function(type,args,obj) { 
			//alert('hi');
			var selected = args[0];   
			//alert("Selected: " + (selected[0]));   
			obj.selectDate(selected);
		};   

		document.addEvent('click',this.clickDom);
		document.nilnutCalendar=this;
		//mySelectHandler.cal = this;

		this.cal = new YAHOO.widget.Calendar("cal","calendarDiv", { title:"请选取日期", close:true } );    
		this.cal.selectEvent.subscribe(mySelectHandler, this, true);  
	
		this.applyCfg();

		this.cal.render();  

	},
	clickDom : function (e){
		//alert(document.nilnutCalendar);
		if(document.nilnutCalendar!=null){
			var event = new Event(e) ;
			var cal = document.nilnutCalendar;
			if(!cal.isInnerElement(event.target)){
				document.nilnutCalendar.hide();
			}
		}
	},
	isInnerElement : function (obj){
			if(this.div.contains(obj)) return true ;
			var c = this.targets.length ;
			for( var i = 0 ; i < c; i++ ) {	
				if (this.targets[i]==obj)
				{
					return true ;
				}
			}

	},
	addTextBox: function ( textBox ) {
		$(textBox).addEvent("focus",this.focusTextBox );
		$(textBox).addEvent("blur",this.blurTextBox);
		$(textBox).cal = this ;
		this.targets.push($(textBox));
	},

	applyCfg: function() { 
		this.cal.cfg.setProperty("MDY_YEAR_POSITION", 1);   
		this.cal.cfg.setProperty("MDY_MONTH_POSITION", 2);   
		this.cal.cfg.setProperty("MDY_DAY_POSITION", 3);   

		this.cal.cfg.setProperty("MY_YEAR_POSITION", 1);   
		this.cal.cfg.setProperty("MY_MONTH_POSITION", 2);   
		
		this.cal.cfg.setProperty("MONTHS_SHORT",   ["1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"]);
		this.cal.cfg.setProperty("MONTHS_LONG",    ["1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"]);
		this.cal.cfg.setProperty("WEEKDAYS_1CHAR", ["\u65E5", "一", "二", "三", "四", "五", "六"]);
		this.cal.cfg.setProperty("WEEKDAYS_SHORT", ["\u65E5", "一", "二", "三", "四", "五", "六"]);
		this.cal.cfg.setProperty("WEEKDAYS_MEDIUM",["\u65E5", "一", "二", "三", "四", "五", "六"]);
		this.cal.cfg.setProperty("WEEKDAYS_LONG",  ["\u65E5", "一", "二", "三", "四", "五", "六"]);

		this.cal.cfg.setProperty("MY_LABEL_YEAR_POSITION",  1);
		this.cal.cfg.setProperty("MY_LABEL_MONTH_POSITION",  2);
		this.cal.cfg.setProperty("MY_LABEL_YEAR_SUFFIX",  "\u5E74");
		this.cal.cfg.setProperty("MY_LABEL_MONTH_SUFFIX",  "");


	},
	hide : function () {
		this.div.style.display="none";	

	},
	//events
	focusTextBox : function ( e ) {
		this.cal.target = this ;
		this.cal.initDate(this.value) ;

		var div = (this.cal.div);
		var c = $(this).getCoordinates();
		div.style.display="block";
		div.style.left = c.left;
		div.style.top = c.top + c.height  ;
		//alert(this.getLeft());	


	},
	blurTextBox: function( e ){
		//var div= (this.cal.div);
		//this.cal.hide();
		//div.style.display="none";


	},
	selectDate : function ( d ) {
			//alert( d );
			//alert(this.target);
			d = d[0];
			if(this.target!=null){
				var year = d[0], month = d[1], day = d[2];  
				this.target.value = year + "-" + month + "-" + day;
			}
			this.target.fireEvent('change');
			//alert('ho');
			this.hide();

	},
	initDate : function( d) {
		if(!isDate(d) ) return ;
		var the1st = d.indexOf('-');
		var the2nd = d.lastIndexOf('-');
		var year = d.substring(0,the1st) ;
		var month = d.substring(the1st+1,the2nd);
		var day = d.substring(the2nd+1);

		//this.cal.setYear ( year) ;
		this.cal.select ( year + "/"+ month+"/"+day);
		//alert(year + "/"+ month+"/"+day);
		var firstDate = this.cal.getSelectedDates()[0];  
		//alert (firstDate.getFullYear());
		this.cal.cfg.setProperty("pagedate", firstDate.getFullYear()+"/"+ (firstDate.getMonth()+1));  

		this.cal.render();
		//alert(year + "/"+ month+"/"+day);
	},
	check : function () {
		for( var i = 0 ; i < this.targets.length ; i++ ) {	

			var vempday = this.targets[i].value;
			if(!isEmpty(vempday)&&!isDate(vempday)){
				alert("请正确填写或选取日期，格式为:YYYY-MM-DD ；或者请留空白");
				this.targets[i].focus();
				return false;
			}
		}
		return true ;
	}
}


