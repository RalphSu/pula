
PGlobals = {minColumnSize : 100 ,
	uriOfCreate:'_create' ,
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
	},
	minusHeight: function(){
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
	},
	initSpanFromData : function (data){
		for( var k in data ) {
			var dom = $('span.'+k);
			if(dom){
				if(dom.hasClass('span-to-time')){
					var time = data[k];
					if(time){
						var d = new Date(time) ;
						
						var ds = d.format( "yyyy-MM-dd hh:mm:ss");
						$('span.'+k).set('html', ds);
					}else{
						$('span.'+k).set('html','');
					}
				}else{
					$('span.'+k).set('html',PA.utils.escapeHTML(data[k]));
				}
			}
		}
	},
	initFormData : function (data){
		for( var k in data ) {
			var dom = $('form.'+k);
			if(dom){
				if(dom.hasClass('form-to-time')||dom.hasClass('form-to-date')){
					var mask = "yyyy-MM-dd hh:mm:ss";
					if(dom.hasClass('form-to-date')){
						mask = "yyyy-MM-dd";
					}
					var time = data[k];

					if(time){
						var d = new Date(time) ;
						var ds = d.format( mask );
						$('form.'+k).set('value', ds);
					}else{
						$('form.'+k).set('value','');
					}
				}else{
					$('form.'+k).set('value',PA.utils.escapeHTML(data[k]));
				}
			}
		}
	}
}

PSinglePage = {
	showCondition:function(){
		if(	$("conditionDiv").hasClass('h')){
			$("conditionDiv").removeClass('h');
			$("conditionDiv").removeClass('force-h');
			var size=  $("conditionDiv").getSize();
			//alert(size.x);
			if($$("#queryLink div.t-button")[0]){
				$$("#queryLink div.t-button")[0].innerHTML = _lang.hiddenCondition;
			}
			if(this.dt){
				this.dt.addHeight (size.y*-1);
			}
		}else{
			var size=  $("conditionDiv").getSize();
			$("conditionDiv").addClass('h');
			$("conditionDiv").addClass('force-h');
			$$("#queryLink div.t-button")[0].innerHTML = _lang.showCondition;			
			if(this.dt){
				this.dt.addHeight (size.y);
			}
		}
	}
}

var PBasePage = new Class({
	initialize: function () { 

	},
    initBase: function ( cfgs){
		this.baseConfig = cfgs || {};
	},
  	initVars : function(cfgs){
		this.config = cfgs;
		this.dt = null ;
		this.vars = {} ;
			
	},
	initToolBar:  function(simple){
		if(typeof(simple)=='object'){
			
		}else{
			if(simple){
				simple = {add:true,remove:true,condition:true};
			}else{
				simple = {all :true }
			}
		}

		var _buttons = [] ;

		if(simple.all || simple.add ) {
				_buttons.push({
					label:_lang.create,
					link :'javascript:pes.createPage()',
					icon: pageVars.base+'/static/laputa/images/icons/add.gif'
				});
		}
		if(simple.all || simple.remove ) {
			_buttons.push({
					label:_lang.remove,
					id:'removeLnk',
					link :'javascript:pes.remove()',
					icon: pageVars.base+'/static/laputa/images/icons/delete.gif',
					css:'forList'
				});
		}
		if(simple.all || simple.enabled ) {
			var enableText = _lang.enabled;
			var enableImg = pageVars.base+'/static/laputa/images/icons/unlocked.gif';
			_buttons.push({
					label:enableText,
					link :'javascript:pes.enable(true)',
					icon: enableImg,
					css:'forList'
				});
		}		
			
		if(simple.all || simple.disable ) {
			var disableText = _lang.disabled;
			var disableImg = pageVars.base+'/static/laputa/images/icons/lockdis.gif';
			_buttons.push({
					label:disableText,
					link :'javascript:pes.enable(false)',
					icon: disableImg,
					css:'forList'
				});
		}		
		
			
		if( simple.all || simple.condition ) {
			_buttons.push({
					label:_lang.hiddenCondition,
					link :'javascript:pes.showCondition()',
					icon: pageVars.base+'/static/laputa/images/icons/view.gif',
					id:'queryLink',
					css:'forList',
					width:'100px'
				});
		}		
	

			var tb = new PA.TToolBar({
				container:"__top",
				title:lang.domain,
				buttons:_buttons				
			});
		
	},
	initSaveForm : function(){
		$(this.config.addForm).addEvent('submit',function(e){
		//put text		
			e.stop();
			this.sendToServer(true);
		}.bind(this));
	},
	sendToServer: function(resetHere){
		if(this.baseConfig.beforeSubmit){
			if(!this.baseConfig.beforeSubmit()){
				return;
			}	
		}
		if(!check()){
			return ;
		}
		this.disableBtn(true);
		PA.ajax.gf( this.vars.action ,$(this.config.addForm).toQueryString(),  function (e){
			this.disableBtn(false);
			if(e.error){ alert (e.message) ; return }
			topSuccess();

			if(this.baseConfig.afterSubmitAll){
				this.baseConfig.afterSubmitAll(e);
				return ;
			}

			if(resetHere){
				if(this.vars.action!= PGlobals.uriOfCreate){					
					this.navigateHistory("id", "");					
				}else{
					if(this.baseConfig.afterSubmit){
						this.baseConfig.afterSubmit();
					}
					this.incIndexNo();
					
					if(this.baseConfig.focusField){
						$(this.baseConfig.focusField).focus();
					}
				}
				this.reload();
			}			
		}.bind(this));
	},
	initSearchForm: function(){
		$(this.config.searchForm).addEvent('submit',function(e){
			e.stop();
			this.vars.requestParam = "&"+$(this.config.searchForm).toQueryString();
			if(this.dt.pager)
				this.dt.pager.pageIndex = 1 ;
			this.reload();
		}.bind(this));
		
		this.vars.requestParam = "&"+$(this.config.searchForm).toQueryString();
	},
	initHistory: function(){
		/*if(!this.baseConfig.historyPrefix){
			this.baseConfig.historyPrefix = Math.floor(Math.random()*24)+"_"+Math.floor(Math.random()*1000);//0-23;
		}
		YAHOO.util.History.register(this.baseConfig.historyPrefix+"id", "", function (state) {
			// This is called after calling YAHOO.util.History.navigate,
			// or after the user has trigerred the back/forward button.
			// We cannot distinguish between these two situations.
			this.changeState(state);
			//loadSection(state);
		}.bind(this));
		YAHOO.util.History.initialize("yui-history-field", "yui-history-iframe");*/
	},
	log : function(v){
		if(this.logger){			
		}else{
			this.logger = new Element('div.logger');
			this.logger.inject(document.body);
		}
		this.logger.innerHTML += "<BR/>" + JSON.encode(v) ;
	},
	changeState:function(n){
		if(n==""){
			this.showInput(false);			
			return ;
		}
		if(n=="create"){
			this.showData(null);
			this.showInput(true);
		}else{
			this.showData({id:n});
			this.showInput(true);
		}
		if(this.baseConfig.focusField){
			$(this.baseConfig.focusField).focus();
		}
	},	
	getQueryString : function(){
		var qs = "" ;
			var checked =false;
			$$(".objId").each(function (ee){
					if(ee.checked){
						qs +="&objId="+ee.value;
						checked = true ;
					}

			});

			if(!checked){
				alert(_lang.chooseOne);
				return null ;
			}
			return qs ;
	},
	reload : function(){
		this.dt.reload();
		this.vars.action='_create';
		
		this.dt.selectedRow(null);
		this.showData(null);
		this.updateMode();		
	},

	disableBtn:function (b){
		$('submitBtn').disabled = b;
		if( $('resetBtn') ) {
			$('resetBtn').disabled = b; 
		}
	},
	showInput: function(b){
		if(this.baseConfig.beforeShowInput){
			this.baseConfig.beforeShowInput();
		}
		if(b){
			$("inputPanel").removeClass('h');
			$$(".forList").addClass('h');

		}else{
			$("inputPanel").addClass('h');
			$$(".forList").filter( function(el){
				return !(el.hasClass('force-h'));
			}).removeClass('h');
		}
	},
	incIndexNo: function(){
		if(this.baseConfig.orderingField){
			$(this.baseConfig.orderingField).value = Number.from($(this.baseConfig.orderingField).value)+10;
		}
	},
	updateMode : function (){
		
		if(this.vars.action=='_create'){
			$('pageMode').innerHTML = _lang.create;
			
		}else{
			$('pageMode').innerHTML = _lang.update;
		}
	},
	
	remove : function (){
		var qs = this.getQueryString();
		if(qs == null ) return ;
		if(!confirm(_lang.removeConfirm)){
			return 
		}		
		PA.ajax.gf('remove',qs,function (e){
			if(e.error){
				topHiddenSuccess();
				alert(e.message);
			}else{
				topSuccess();
				this.reload();
			}			
		}.bind(this));
	},
	
	enable : function (b){
		var qs = this.getQueryString();
		if(qs == null ) return ;
		//alert(qs);
		PA.ajax.gf('enable',qs+'&enable='+b,function (e){
			if(e.error){
				alert(e.message);	
			}else{
				topSuccess();
				this.reload();
			}
		}.bind(this));	
	},
	createPage : function(){
		
		this.disableBtn(false);
		this.navigateHistory("id", 'create');
	},
	showCondition:function(){
		
		PSinglePage.showCondition.bind(this)();
	},backToList : function(){
		this.navigateHistory("id", '');
	},
	mockCheck : function(){
		var e = new Element("div");
		e.inject(document.body);
		e.innerHTML = ("<input type='button' onclick='check()' value='CHECK'/>");
	},
	navigateHistory: function( d , v ) {
		//YAHOO.util.History.navigate(this.baseConfig.historyPrefix+d,v);
		this.changeState(v);
	}
});

	function goThere(u){
		var i = u.indexOf('/app/');
		if(i>=0){
			//cut
			u = u.substring(i+5,u.length);
		}

		if(window.parent != window ){
			window.parent.location = pa_vars.base + "/app/my/window?u="+encodeURIComponent( "../"+u) ;
		}else{
			window.location = pa_vars.base + "/app/"+u  ;
		}
	}	