TTabFrame = function (config){
		this.config = config ;		
		this.init();

}



TTabFrame.prototype = {
		init: function(){
			this.tabs = [] ;
			this.iframes = [] ;
			this.activedFrame = null ;
			this.tabIndex = -1 ;
			this.tabNo = "";
			var $this = this;
			if(this.config.prevTab){
				$(this.config.prevTab).addEvent('click',function(){
					$this.goPrevTab();
				});
			}
			if(this.config.nextTab){
				$(this.config.nextTab).addEvent('click',function(){
					$this.goNextTab();
				});
			}
			this.resize();
		},
		addTab : function( text ,no , url ) {
			var _tf = this;
			var closeSelf = function(e){
				//alert(no);
				e.stop();
				_tf.closeTab(no ) ;
			};
			var activeSelf = function(){
				_tf.selectTab(no);
			}

			if(this.tabs.contains(no )) {
				//focus on the tab
				this.selectTab(no) ;
				return ;
			}

			this.tabs.push(no);

			//remove the clear div 
			$(this.config.container).getChildren("div.c")[0].dispose();

			var tab= new Element('DIV.tab',{ "id":"tab_"+no,events:{click:activeSelf}} );

			var sText =  new Element("span");
			sText.innerHTML = text ;
			sText.inject( tab ) ;
			
			var sHref = new Element("a",{ "href":"#" ,events:{ "click":closeSelf}});		
			var sBtn = new Element("span.close");
			sBtn.inject(sHref);
			sHref.inject( tab);

			tab.inject ( this.config.container) ; 
			
			var c= new Element('DIV.c');
			c.inject ( this.config.container) ; 

			//build iframe
			var iframe = new Element('iframe',{
				src : url ,
				id : 'frm_'+no ,
				width:'100%',
				frameBorder:'0',
				scrolling:'yes'
			});
			iframe.inject( $('t-body') ) ;
			this.iframes.push( iframe) ;

			//width changed ;
			var tw = 0 ;
			this.tabs.each ( function (el ){
				tw += $("tab_"+no).getSize().x + 5 ;
			});
			$(this.config.container).setStyle('width',tw+500); //500 给下面的准备，避免文字缩小

			this.selectTab(no ) ;
		},
		closeTab : function( no ) {
			
			var idx = this.tabs.indexOf(no);
			if(idx < 0  ) {
				return ;
			}

			//free iframe
			//free no ;
			var closeCurrent = false;
			if(this.tabNo == no ){
				this.tabNo = "";
				this.activedFrame = null ;
				this.tabIndex = -1 ;
				closeCurrent = true ;
			}

			this.tabs.erase(no ) ;
			$("tab_"+no).dispose();
			//var got = null ;
			/*this.iframes.each ( function(el){
				if(el.id == 'frm'+no ){
					got = el ;
				}else{
					
				}
			});*/
			this.iframes.erase(this.iframes[idx]);
			//alert(got);
			//got.dispose();
			$("frm_"+no).dispose();

			//if there is a next ,then do it ;
			if(closeCurrent){
				if ( this.tabs.length>idx )
				{
					this.selectTab( this.tabs[idx]);
				}else if ( this.tabs.length>0)
				{
					this.selectTab( this.tabs[this.tabs.length-1]);
				}else{

				}
			}
			
		
		},
		selectTab : function(no){
			$(this.config.container).getChildren(".tab").removeClass("actived");
			$("tab_"+no).addClass("actived");
			this.tabNo = no ;
			//scroll
			var ps = $("tab_"+no).getPosition($(this.config.container));
			var sz = $("tab_"+no).getSize();
			var szw = $(this.config.wrapper).getSize();
			var scroll = $(this.config.wrapper).getScroll();
			//log(JSON.encode({position:ps,wrapperSize:szw}));
			if( (ps.x +sz.x ) > szw.x){
				if(szw.x <= sz.x){
					$(this.config.wrapper).scrollTo(ps.x,0);
				}else{
					var x =  ( sz.x - (szw.x - (ps.x - scroll.x )) + scroll.x)+2;
					$(this.config.wrapper).scrollTo(x,0);
				}
				//log("x==>"+x);
			}else if (ps.x < scroll.x )
			{
				$(this.config.wrapper).scrollTo(ps.x,0);
			}			
			//show iframe;
			//alert($("frm_"+no));
			this.activedFrame = $("frm_"+no) ;
			this.tabIndex = this.tabs.indexOf(no);;
			$this = this; 
			//hidden all ;
			this.iframes.each ( function(el){
				if(el.id == $this.activedFrame.id ){
					el.removeClass('h');
				}else{
					el.addClass('h');
				}
			});
			

			//update state
			if(this.tabIndex<1 && this.config.prevTab){
				$(this.config.prevTab).disabled =true ;
				
				$(this.config.prevTab).getChildren("a span").addClass('disabled');
			}else if (this.config.prevTab)
			{
				$(this.config.prevTab).disabled =false;
				$(this.config.prevTab).getChildren("a span").removeClass('disabled');
			}
			if( this.tabIndex>= this.tabs.length -1 && this.config.nextTab){
				$(this.config.nextTab).disabled =true ;
				$(this.config.nextTab).getChildren("a span").addClass('disabled');
			}else if (this.config.nextTab)
			{
				$(this.config.nextTab).disabled =false;
				$(this.config.nextTab).getChildren("a span").removeClass('disabled');
			}

			resizeFrame();
		},
		
		goPrevTab : function(){
			if(this.tabIndex<1){
				return ;
			}
			this.selectTab( this.tabs[ this.tabIndex-1 ] );
		},
		goNextTab : function(){
			if(this.tabIndex>= this.tabs.length -1 ){
				return ;
			}
			this.selectTab( this.tabs[ this.tabIndex + 1 ] );
		},
		resize : function(){
			var w = 0; 
			$$(".tab-left").each ( function(el){
				w += el.getSize().x;
			});
			//log('left size:'+w);

			var w = window.getSize().x - w;
			if (Browser.ie6 ){
				w -= 10;
			}
			//log('got size:'+w);
			if(w<0) w = 0 ;
			$(this.config.wrapper).setStyle('width',w ) ;

		},
		length : function(){
			return this.iframes.length ; 
		},
		hasTab : function(no){
			return this.tabs.indexOf(no) >=0 ;
		}

}