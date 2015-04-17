


PA.history ={
	init : function(configs){
	
		PA.history.url = window.location.href ;
		PA.history.callback = configs.callback;
		PA.history.base = deleteSharp(configs.base) ;

		window.setTimeout('PA.history.check()',200);
	},
	check : function(){
		var cu = window.location.href;
		window.status  = cu;
		if(PA.history.url != cu ) {
			var len = PA.history.base.length ;
			//var left =cu.length-len;
			var v = cu.substring( len,cu.length  );
			//alert('aa:'+v + '\n'+cu+'\n'+len +'\n'+PA.history.base);
			PA.history.callback( v);
			PA.history.url = cu ;
		}
		window.setTimeout('PA.history.check()',200);
	},
	go:function(mark){
		window.location.href= PA.history.base + "#"+mark ;
	}
};

PA.history.url = "";
PA.history.callback = null ;