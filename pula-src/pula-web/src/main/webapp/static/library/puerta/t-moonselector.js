TMoonSelector = function (config){
		this.config = config ;
		this.init();
}
//TMoonSelector.DIV = "<div class='nilmoon'>{content}</div>";

TMoonSelector.CLEAR = "<div class='c'></div>";
TMoonSelector.SPAN = "<div class='t-moon-item {css}' y='{y}' m='{m}'><h3>{y}</h3> <h4>{m}</h4>"+TMoonSelector.CLEAR+"</div>";
TMoonSelector.LINKS_PREV = "<span class='links prev'></span>";
TMoonSelector.LINKS_NEXT = "<span class='links next'></span>";
TMoonSelector.prototype = {

		init: function(){
			this.position = {m:this.config.month,y: this.config.year};
			$(this.config.container).addClass('t-moon-selector');
			$(this.config.container).addEvent('click',this.clickHere.bind(this));

			this.config.middle = true;

			//apply
			if(this.config.applyYear){
				$(this.config.applyYear).value = this.config.year ;
				$(this.config.applyMonth).value = this.config.month ;
			}

			this.draw();
			
		},
		clickHere: function(e){
			var target = $(e.target);
			if(target.tagName=='H3' || target.tagName=='H4'){
				target = $(target).getParent("div.t-moon-item");
				
			}

			
			if(target.hasClass('links')&&target.hasClass('prev')){
				//pages
				this.config.middle = false;
				this.position = this.scroll(this.starts,this.config.count*-1) ;
				this.draw();
				return ;
			}
			if(target.hasClass('links')&&target.hasClass('next')){
				//pages
				this.config.middle = false;
				this.position = this.ends ;
				this.draw();
				return ;
			}
			if(target.tagName=='DIV' && !target.hasClass('t-moon-item')){
				return ;
			}
			var children = $(this.config.container).getChildren(".selected");
			if(children.length>0){
				children[0].removeClass('selected');
			}
			
			var m = target.get('m');
			var y = target.get('y');
			if(this.config.directForward){
				//direct do it 
				var url = this.config.url.substitute({y:y,m:m});
				window.location.href= url;
				return ;
			}
				$(this.config.applyYear).value = y ;
				$(this.config.applyMonth).value = m ;			
			if(this.config.form&&this.config.applyYear&&this.config.applyMonth){

				$(this.config.form).submit();
				return ;
			}

			//redraw the ui 
			this.config.year = y ; 
			this.config.month = m ;
			this.draw();
		},
		getLinks: function(){
			var sb = "";
			if(this.config.middle){
				var mid = (this.config.count/2).floor();
				var bm = this.config.count-mid ;
				this.starts = this.scroll(this.position,bm*-1);
			}else{
				this.starts = this.position;
			}
			var ms = this.starts ;
			for(var i = 0 ;i <this.config.count ;i++){
				ms = this.scroll(ms,1);	
				var moreCss ="" ;
				if( ms.y== this.config.year && ms.m == this.config.month){
					moreCss="selected";
				}
				sb += TMoonSelector.SPAN.substitute({y:ms.y,m:this.fixZero(ms.m),css:moreCss});
			}
			
			this.ends = ms ;
			return sb ;
		},
		fixZero: function (m){
			if((''+m).length == 1 ) {return "0"+m;}
			return m; 
		},
		scroll : function(obj,addv){
			var m = obj.m;
			var y = obj.y;
			var i = 1 ;
			if(addv<0){
				i = -1;
			}
			addv = addv.abs();
			for(var j = 0 ; j < addv ; j++){
				m += i ;
				if(m>12){
					y+= i ;
					m=1;
				}else if (m<1)
				{
					y+= i ;
					m=12;
				}
			}
			return {m:m,y:y};
		},
		getAll:function(c){
			return TMoonSelector.LINKS_PREV + c + TMoonSelector.LINKS_NEXT + TMoonSelector.CLEAR; ;
		},
		draw:function(){
			var sb = this.getAll( this.getLinks() ) ;
			$(this.config.container).innerHTML = sb ;
		}
}