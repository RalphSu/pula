
TSuggest = function (config){
	
		this.config = config ;

		if(!$defined (this.config.param ) ) {
			this.config.param = "" ;
		}
		this.vars = [] ;
		//alert(this.config.param);
		this.init();

}

TSuggest.INPUT  = '<div class="l"><input type="text" name="{field}" value="{value}" id="{field_id}"/></div>';
TSuggest.DIV_NAME = '<div id="text{field_id}" class="l">{text}</div>';
TSuggest.TIPS = '<div class="t-simple-no-tips" id="tips{field_id}"><label  for="{field_id}">{tips}</label></div>';
TSuggest.CLEAR = '';

TSuggest.prototype = {
		draw: function(){
			var sb = "";
			sb+=TSuggest.INPUT.substitute(this.config);
			sb+=TSuggest.DIV_NAME.substitute(this.config);
			if(this.config.showTips){
				sb+=TSuggest.TIPS.substitute(this.config);
			}
			sb+=TSuggest.CLEAR.substitute(this.config);
			if($(this.config.container) == null ){
				alert(this.config.container + ' is null ');
				return ;
			}
			
			$(this.config.container).addClass('t-simple-no');	
			$(this.config.container).innerHTML = sb;

			this.blurEditor();

		},
	
		init : function(){
			this.draw();
			this.data = new Array();

			var el = $(this.config.field_id);
			el.addEvent('keydown',function (e){
				this.onKeyDown(e,this);
			}.bind(this)).addEvent('blur',this.blurEditor.bind(this));
			
			var el = $(this.config.field_id);
			this.vars.as = new autoSuggest(this.config.url, this.config.field_id,'');
			//events
			var $this = this ;
			var afterSelect = function( s ) {
				var obj = JSON.decode ( s ) ;
				var row = this ;
				$('text'+$this.config.field_id).set('html',obj[1]).removeClass('h') ;
				$($this.config.field_id).value = obj[0];

				if($this.config.callback){
					$this.config.callback( obj ) ;
				}

			}

			this.vars.as.callback = afterSelect  ; 
		},

		reset: function(){
			
			$('text'+this.config.field_id).addClass('h');
			if(! this.config.showTips){
				
			}else{
				$('tips'+this.config.field_id).removeClass('h');
			}
			$(this.config.field_id).value = "" ;
			
			
			$('text'+this.config.field_id).addClass('h');
			el = null;
		},

		blurEditor:function(){
			if($(this.config.field_id).value.trim() == ""){
				//$('tips'+this.config.field_id).removeClass('h');
			}else{
				if(this.config.showTips){
					$('tips'+this.config.field_id).addClass('h');
				}
			}
			
		},
		setValue:function(no,name){
			$('text'+this.config.field_id).innerHTML = name;
			$('text'+this.config.field_id).removeClass('h');
			$(this.config.field_id).value = no;
			if(this.config.showTips){
				$('tips'+this.config.field_id).addClass('h');
			}
		},
		on :function(event,f){
			$(this.config.field_id).addEvent(event,f);
		},
		onKeyDown: function( e,t){
			if(this.config.showTips){$('tips'+t.config.field_id).addClass('h');}
		}
		

}