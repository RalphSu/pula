
NilShortcutEditor = function (config){
	
		this.config = config ;

		if(!$defined (this.config.param ) ) {
			this.config.param = "" ;
		}
		//alert(this.config.param);
		this.init();

}

NilShortcutEditor.prototype = {

	
		init : function(){
			
			var el = $(this.config.editor);
			//alert(el);
			el.shortcut = this ;
			el.addEvent('keydown',this.onKeyDown);

			el = $(this.config.select);
			el.style.display ="none";
			el.shortcut = this ;
			el.addEvent('change',this.onChange);
			el.addEvent('keydown',this.onSelectKeyDown);
			
		},


		onSelectKeyDown : function (e1){
			var e = new Event(e1);
			if(e.key != 'enter'){
				return ;
			}

			event.keyCode = 9;

			return true ;

		},

		onChange: function (e){
			var ref = (this.shortcut);
			var obj = ref.config.select.options[ref.config.select.selectedIndex].itemObj ;
			//alert(obj);
			if(obj){
				ref.config.editor.value = obj.no ;
				ref.config.container.innerHTML = obj.name;
			}

		},

		onKeyDown: function( e1){
			var e = new Event(e1);
			//alert(e.key);
			if(e.key != 'enter'){
				return ;
			}

			e.stop();

			var ref = (this.shortcut);

			var text = ref.config.editor.value ;
			ref.config.container.innerHTML ="" ;
			ref.config.container.style.display="none";
			ref.config.select.style.display ="none";

			var selectObj = ref.config.select;

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
						ref.config.editor.focus();
						ref.config.editor.select();
						return ;
					}else if(obj.list.length==1){
						obj = obj.list[0];
						//alert(ref.config.container);
						//alert(obj.name);
						ref.config.container.style.display="block";
						ref.config.select.style.display ="none";
						ref.config.container.innerHTML = obj.name;
						ref.config.editor.value = obj.no;
					}else{
						
						
						ref.config.container.style.display="none";
						ref.config.select.style.display ="block";
						selectObj.options.length = 0 ;
						selectObj.options[0] = new Option("请选取...","");
						selectObj.options[0].selected = true ;
						selectObj.options[0].style.color="blue";
						for(var n=0 ;n < obj.list.length;n++){
							var item = obj.list[n];
							selectObj.options[n+1] = new Option(item.name,item.no);
							selectObj.options[n+1].itemObj = item ;

						}//for
						
						selectObj.focus();
						//alert('hi');
						
						
					}//else
				}//else
			} );

			
			if(ref.config.callback){
				if(!ref.config.callback(ref.config)){
					return ;
				}	
			}
			myXHR.send("_json=1&no="+encodeURIComponent(text)+ref.config.param);

		}

		



}