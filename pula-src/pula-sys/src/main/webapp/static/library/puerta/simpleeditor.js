
NilSimpleEditor = function (config){
	
		this.config = config ;

		if(!$defined (this.config.param ) ) {
			this.config.param = "" ;
		}
		//alert(this.config.param);
		this.init();

}

NilSimpleEditor.INPUT  = '<div class="l"><input type="text" name="{field}" value="{value}" id="{field_id}"/></div>';
NilSimpleEditor.SELECT = '<div class="l"><select id="select{field_id}" class="h"></select></div>';
NilSimpleEditor.DIV_NAME = '<div id="text{field_id}" class="l">{text}</div>';
NilSimpleEditor.TIPS = '<div class="tipse l" id="tips{field_id}">{tips}</div>';
NilSimpleEditor.CLEAR = '<div class="cl"></div>';

NilSimpleEditor.prototype = {
		draw: function(){
			var sb = "";
			sb+=NilSimpleEditor.INPUT.substitute(this.config);
			sb+=NilSimpleEditor.SELECT.substitute(this.config);
			sb+=NilSimpleEditor.DIV_NAME.substitute(this.config);
			if(this.config.showTips){
				sb+=NilSimpleEditor.TIPS.substitute(this.config);
			}
			sb+=NilSimpleEditor.CLEAR.substitute(this.config);
			$(this.config.container).innerHTML = sb;
		},
	
		init : function(){
			this.draw();
			this.data = new Array();
			
			var el = $(this.config.field_id);
			el.addEvent('keydown',function (e){

				this.onKeyDown(e,this);
			}.bind(this));

			el = $('select'+this.config.field_id);
			el.addEvent('change',this.onSelectChange.bind(this));
			el.addEvent('keydown',this.onSelectKeyDown);
		},

		reset: function(){
			
			$('text'+this.config.field_id).addClass('h');
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
					if(obj.list.length<=0){
						alert("没有找到指定的数据:"+text);
						fieldObj.focus();
						fieldObj.select();
						return ;
					}else if(obj.list.length==1){
						obj = obj.list[0];
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
						for(var n=0 ;n < obj.list.length;n++){
							var item = obj.list[n];
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