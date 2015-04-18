
TSimpleNo = function (config){
	
		this.config = config ;

		if(!$defined (this.config.param ) ) {
			this.config.param = "" ;
		}
		//alert(this.config.param);
		this.init();

}

TSimpleNo.INPUT  = '<div class="l"><input type="text" name="{field}" value="{value}" id="{field_id}"/></div>';
TSimpleNo.SELECT = '<div class="l"><select id="select{field_id}" class="h"></select></div>';
TSimpleNo.DIV_NAME = '<div id="text{field_id}" class="l">{text}</div>';
TSimpleNo.TIPS = '<div class="t-simple-no-tips" id="tips{field_id}"><label  for="{field_id}">{tips}</label></div>';
TSimpleNo.CLEAR = '';

TSimpleNo.prototype = {
		draw: function(){
			var sb = "";
			sb+=TSimpleNo.INPUT.substitute(this.config);
			sb+=TSimpleNo.SELECT.substitute(this.config);
			sb+=TSimpleNo.DIV_NAME.substitute(this.config);
			if(this.config.showTips){
				sb+=TSimpleNo.TIPS.substitute(this.config);
			}
			sb+=TSimpleNo.CLEAR.substitute(this.config);
			if($(this.config.container) == null ){
				alert(this.config.container);
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

			el = $('select'+this.config.field_id);
			el.addEvent('change',this.onSelectChange.bind(this));
			el.addEvent('keydown',this.onSelectKeyDown);

			
		},

		reset: function(){
			
			$('text'+this.config.field_id).addClass('h');
			if(! this.config.showTips){
				
			}else{
				$('tips'+this.config.field_id).removeClass('h');
			}
			$(this.config.field_id).value = "" ;
			var el = $('select'+this.config.field_id);
			el.addClass('h');
			
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
		onSelectKeyDown : function (e1){
			
			if(e1.key != 'enter'){
				return ;
			}
			if(e1.event.which){
				e1.event.which = 9 ;
			}else{
				e1.event.keyCode=9;
			}
			return true ;
		},

		onSelectChange: function (e){
			var target = $(e.target);
			var index = target.selectedIndex;
			var obj = this.data[index-1];
			if(obj){
				$(this.config.field_id).value= obj.no;
				$('text'+this.config.field_id).innerHTML = obj.name;
				target.title = obj.name;
			}
		},
		setValue:function(no,name){
			$('text'+this.config.field_id).innerHTML = name;
			$('text'+this.config.field_id).removeClass('h');
			$(this.config.field_id).value = no;
			if(this.config.showTips){
				$('tips'+this.config.field_id).addClass('h');
			}
			$('select'+this.config.field_id).addClass('h');
		},
		on :function(event,f){
			$(this.config.field_id).addEvent(event,f);
			$('select'+this.config.field_id).addEvent(event,f);
		},
		onKeyDown: function( e,t){
			if(this.config.showTips){$('tips'+t.config.field_id).addClass('h');}
			if(e.key != 'enter'){
				return ;
			}

			e.stop();

			var ref = t;
			var fieldObj = $(ref.config.field_id);
			var text = fieldObj.value;
			var textObj = $('text'+ref.config.field_id);
			textObj.innerHTML ="" ;
			if(!textObj.hasClass('h')){
				textObj.addClass('h');
			}
			var selectObj = $('select'+ref.config.field_id);
			if(!selectObj.hasClass('h')){
				selectObj.addClass('h');
			}
			//reset to empty
			ref.data.empty();
			if(ref.config.beforeLoad){
				if(!ref.config.beforeLoad ( ref ) ){
					return ;
				}
			}
			var myXHR = new Request({url:ref.config.url,method:'post'});
			myXHR.addEvent( 'onSuccess', function (e ) { 
				
				var obj = JSON.decode(e,true);
				if(obj==null){
					alert("无效的JSON数据:"+e);
					return;
				}
				if(obj.error){
					alert(obj.message);
				}else{
					//alert(obj.list.length);
					var data = obj.list ;
					if(!data){
						data = obj.data ;
					}

					if(data.length<=0){
						alert("没有找到指定的数据:"+text);
						fieldObj.focus();
						fieldObj.select();
						return ;
					}else if(data.length==1){
						obj = data[0];
						textObj.removeClass("h");
						selectObj.addClass("h");
						textObj.innerHTML = obj.name;
						fieldObj.value = obj.no;
					}else{
						textObj.addClass("h");
						selectObj.removeClass("h");
						selectObj.options.length = 0 ;
						selectObj.options[0] = new Option("请选取...","");
						selectObj.options[0].selected = true ;
						selectObj.options[0].style.color="blue";
						for(var n=0 ;n < data.length;n++){
							var item = data[n];
							selectObj.options[n+1] = new Option(item.name,item.no);
							ref.data.push(item);

						}//for
						
						selectObj.focus();
						//alert('hi');
						
						
					}//else

					if(ref.config.showTips){
						$("tips"+ref.config.field_id).addClass('h');
					}
				}//else
			} );

			
			if(ref.config.callback){
				if(!ref.config.callback(ref.config)){
					return ;
				}	
			}
			var data = "_json=1&no="+encodeURIComponent(text)+ref.config.param;
			myXHR.send(data);

		}

}