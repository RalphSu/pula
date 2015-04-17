WxlParams = function (config){
	
		this.config = config ;
		
		this.hash = new Hash();
		this.init();

}

WxlParams.prototype = {

	
		init : function(){
			
			var str = this.config.params ;
			var index = str.indexOf("?");
			if ( index != -1) { 
				str = str.substring(index+1);
			}
			//alert(str);
			strs = str.split("&");
			
			for(var i = 0; i < strs.length; i ++) { 
				 // alert(strs[i]);
				 var items=strs[i].split("=");
				 var pn = items[0];
				 var pv = decodeURIComponent(items[1]);
				 //paraList[items[0]]=unescape(items[1]);
				//alert(pn+"="+pv);
				var arrays = null ;
				if(this.hash.hasKey(pn)){
					arrays = this.hash.get(pn);
				}else{
					arrays  =new  Array();
					this.hash.set(pn,arrays);
				}
				arrays.push(pv);
			}  
			
		},

		getArrays : function (name){

			return this.hash.get(name);
		}

}