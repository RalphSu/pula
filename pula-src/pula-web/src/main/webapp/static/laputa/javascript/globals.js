
PGlobals = {
	smallScreen:function(){
			//screen.width
		return true ;
	},
	listViewHeight:function(){
		var h = window.getSize().y;
		h -=100;
		return h ;
	},	
	listViewHeightExclude:function(condition){
		var size=  $(condition).getScrollSize();
		var h = window.getSize().y;
		h -= (size.y +100);

		return h ;
	},minusHeight: function(){
		 var numargs = this.length;
		 var h = 0 ;
		 //alert(numargs);
		 for(var i = 0 ; i < numargs ;i++){
			if($(this[i]).hasClass('h')){
				continue ;
			}
			h -= $(this[i]).getSize().y  ;
			//alert(this[i]+"HH:"+h);
		 }
		 return h; 
	}
}

PSinglePage = {
	showCondition:function(){
		if(	$("conditionDiv").hasClass('h')){
			$("conditionDiv").removeClass('h');
			var size=  $("conditionDiv").getSize();
			$$("#queryLink div.t-button")[0].innerHTML = _lang.hiddenCondition;
			if(this.dt){
				this.dt.addHeight (size.y*-1);
			}
		}else{
			var size=  $("conditionDiv").getSize();
			$("conditionDiv").addClass('h');
			$$("#queryLink div.t-button")[0].innerHTML = _lang.showCondition;			
			if(this.dt){
				this.dt.addHeight (size.y);
			}
		}
	}
}