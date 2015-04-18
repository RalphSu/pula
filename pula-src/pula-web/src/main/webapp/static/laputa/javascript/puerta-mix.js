var PA = {version:1.1};
/**
puerta-
	ajax
	v 1.1.0
	2011-03-07
	@tiyi
**/


PA.on = function ( f){
	window.addEvent('domready',f);
}


PA.ajax = {
	gr : function (url,data,ret){
		var aj = new Request.JSON(
			{
				url:url,
				onSuccess:function (e){
					if(!e.error){
						go(ret);
					}else{
						alert(e.message);
					}
				}
			}).send(data);

	},
	gf : function (url,data,func){
		if(data!=null&&data.length>0){
			data += "&_json=1";
		}
		if(data==null){
			data = "_json=1";
		}
		//with tracker
		if(typeof(tk_current_id)!='undefined'){
			data += "&_tracker="+tk_current_id();
		}
		var onErrorFunc = function(text,error){
			puerta_ajax_server_halt = true ;
			if(typeof(puerta_ajax_slient)!='undefined' && puerta_ajax_slient){
				return
			}
			alert(_lang.serverError.substitute({text:text,error:error}));
		}

		var aj = new Request.JSON(
				{
					url:url,
					onSuccess:func,
					onError:onErrorFunc,
					onFailure: function(xh){
						if(xh.status==0) return ;
						puerta_ajax_server_halt = true ;
						if(typeof(puerta_ajax_slient)!='undefined' && puerta_ajax_slient){
							return
						}
						alert( _lang.serverDown.substitute(xh) );
					}
		}).send(data);
	},
	gfh : function (url,data,func){
		var aj = new Request(
				{
					url:url,
					onSuccess:func
		}).send(data);
	},
	sf  : function(form,func){
		PA.ajax.gf(form.action,form.toQueryString(),func);
	},
	f2j : function(form){
		json = '{';
		isarray = false;
		for (i = 0, max = form.elements.length; i < max; i++) {
			e = form.elements[i];
			name = e.name;
			if(name==""){
				continue ;
			}
			if (name.substring(name.length - 2) == '[]') {
				name = name.substring(0, name.length - 2);
				lastarr = name;
				if (isarray == false) {
					json += '"' + name + '":[';
				}
				isarray = true;
			} else {
				if (isarray) {
					json = json.substring(0, json.length - 1) + '],';
				}
				isarray = false;
			}
			
			if(e.tagName=='SELECT'){
				json += '"' + name + '":';
				json += '"' +  e.value.replace(new RegExp('(["\\\\])', 'g'), '\\$1') + '",';
				continue;
			}
			if(e.tagName=='TEXTAREA'){
				json += '"' + name + '":';
				var s = e.value.replace(new RegExp('(["\\\\])', 'g'), '\\$1');
				s = s.replace( /\r\n/g,"\\n");
				s = s.replace( /\n/g,"\\n");
				json += '"' +  s+ '",';
				continue;	
			}
			
			switch (e.type) {
			case 'checkbox':
			case 'radio':
				if (!e.checked) { break; }
			case 'hidden':
			case 'password':
			case 'text':
				if (!isarray) {
					json += '"' + name + '":';
				}
				
				json += '"' + e.value.replace(new RegExp('(["\\\\])', 'g'), '\\$1') + '",';
				break;


			case 'button':
			case 'file':
			case 'image':
			case 'reset':
			case 'submit':
			default:
			}
		};
		return json.substring(0, json.length - 1) + '}';

	},
	bindList:function(cfgs){
		PA.ajax.gf(cfgs.url,"",function(e){
			if(e.error) { alert(e.message) ; return ;} 
			var obj = $(cfgs.id);
			e.data.each( function (el){
					var selected = (el.id== cfgs.defaultValue) ;
					var op = new Element('option',{   
										 text: el.name,
										 value: el.id,
										selected:selected
					});
					obj.adopt(op);
			});
			obj=null;

		});
	}
};


/**
ui

**/

PA.ui = {

	center:function(id){
		var obj = $(id);
		//obj.setStyle('display','block');
		var w = window.getScrollWidth() - obj.getSize().x;
		var h = window.getScrollHeight() - obj.getSize().y;
		obj.setStyle('left',(w /2 )) ;
		obj.setStyle('top',(h /2 )) ;
	},
	centerAt:function(id,at){
		var obj = $(id);
		obj.setStyle('display','block');
		var ps = $(at).getPosition(true);
		
		var w = $(at).getScrollWidth() - obj.getSize().x;
		var h = $(at).getScrollHeight()  + obj.getSize().y;
		obj.setStyle('left',(w /2 ) + ps.x) ;
		obj.setStyle('top',(h /2 )+ ps.y) ;
	},
	screenCenter:function(id){
		var obj = $(id);
		obj.setStyle('display','block');
		var sz = window.getSize();
		var w = window.getScrollWidth() - obj.getSize().x - document.documentElement.scrollLeft;
		var h = sz.y - obj.getSize().y ;
		obj.setStyle('left',(w /2 )) ;
		obj.setStyle('top',(h /2 )+document.documentElement.scrollTop) ;
	}
}

PA.utils ={
	escapeHTML: function(str) { 
		
		return String(str||'').replace(/&/g,'&amp;').replace(/</g,'&lt;').replace(/>/g,'&gt;'); 
	},
	substituteHTML: function(string,object, regexp){
		return string.replace(regexp || (/\\?\{([^{}]+)\}/g), function(match, name){
			if (match.charAt(0) == '\\') return match.slice(1);
			return (object[name] != null) ? PA.utils.escapeHTML(object[name]) : '';
		});
	},
	clearHTML:function(str){
		return str.replace(/<[^>].*?>/g,"");

	},
	nl2br : function ( str ){
		var s = str.replace(/\n/g,'<BR>');
		return s; 

	},
	output:function(str){
		return  PA.utils.nl2br( PA.utils.escapeHTML(str));
	},
	selectAll: function(id){
		var obj = $(id);
		for(var i= 0 ;i < obj.options.length ;i ++){
			obj.options[i].selected = true ;
		}
		obj = null;

	},
	clearSelect:function(id){
		$(id).getElements('option').destroy();
	},
	clearSelectExceptFirst : function(id){
		var obj = $(id);
		if(obj==null) {alert(' obj is null ='+id) ;return;}
		var first = new Element('option',{   
							 text: obj.options[0].text,
							 value: obj.options[0].value
		});
		$(id).getElements('option').destroy();
		$(id).adopt(first);
	},
	defaultStr: function(s){
		if(s==null) return "";
		return s; 
	},
	checkCss: function( css ,value ){
		$$(css).each( function (ff) { if(ff.value == value ) { ff.checked = true ;}});
	},
	checked:function( css ){
		var v = false;
		$$(css).each( function (ff ) { if (!v && ff.checked) { v = true ;} } );
		return v ;
	},
	defaultTo:function ( v ,dv ){
		if(v == null){
			return dv ;
		}
		return v ;
	},
	switchCss : function ( obj , css,exp){
		if(exp){
			obj.addClass(css);
		}else{
			obj.removeClass(css);
		}
	},
	dateToText: function ( oData ) {
		if(oData){
			var m = "yyyy-MM-dd";
			var d= new Date()  ;
			d.setTime(oData);
			return d.format( m ); 
		}
		return "" ;
	}
}

PA.ToolBar = function(configs){
	this.config = configs;
	this.init();
}
PA.ToolBar.CONTAINER = '<div class="tbLeft" ></div>{content}<div class="tbRight"></div><div class="c"></div>';
PA.ToolBar.BUTTON = '<div class="topbutton{classFix}" id="{id}"><a href="{link}" ><IMG SRC="{icon}" WIDTH="16" HEIGHT="16" BORDER="0" align="absmiddle">{label}</A></DIV>';

PA.ToolBar.prototype = {
	
	init : function(){
		var buttons = this.config.buttons ;
		var sb = "";
		buttons.each(function(item,index){
			var css = "";
			if(index!=0){
				css = " bl";	
			}
			sb += PA.ToolBar.BUTTON.substitute( Object.merge({ classFix:css},item));
		});

		sb = PA.ToolBar.CONTAINER.substitute( {content: sb } );
		$(this.config.container).addClass('toolBar');
		$(this.config.container).innerHTML = sb ;
	}
	


}

PA.TToolBar = function(configs){
	this.config = configs;
	this.init();
}
PA.TToolBar.CONTAINER = '<div class="t-head "><div class="t-title l">{title}</div><div class="t-bar l">{buttons}<div class="c"></div></div><div class="c"></div></div>';
PA.TToolBar.CONTAINER_WITH_RIGHT = '<div class="t-head "><div class="t-title l">{title}</div><div class="t-bar l">{buttons}<div class="c"></div></div><div class="t-bar r">{buttons_r}<div class="c"></div></div><div class="c"></div></div>';
PA.TToolBar.BUTTON = '<A href="{link}" id="{id}"><div class="t-button-l l {classFix}" style="width:{width}"><div class="t-button-r"><div class="t-button" style="background-image:url({icon})">{label}</div></div></div></A>';
//'<div class="topbutton{classFix}" id="{id}"><a href="{link}" ><span class="tbItem" style="background-image:url({icon})" >{label}</span></A></DIV>';

PA.TToolBar.prototype = {
	
	init : function(){
		var buttons = this.config.buttons ;
		
		var sb = this.buttonsHtml( this.config.buttons);
		
		if(this.config.right){
			var sb_r = this.buttonsHtml( this.config.right);
			sb = PA.TToolBar.CONTAINER_WITH_RIGHT.substitute( {buttons: sb,title:this.config.title,buttons_r:sb_r } );
		}else{
			sb = PA.TToolBar.CONTAINER.substitute( {buttons: sb,title:this.config.title} );
		}


		
		
		if($(this.config.container)==null){
			alert(this.config.container);
			return ;
		}
		$(this.config.container).innerHTML = sb ;
		
		if(!this.config.title || this.config.title == null){
			//alert('aa:'+this.config.title);
			$(this.config.container).getChildren(".t-head .t-title").addClass('h');
		}
	},
	
	setTitle : function ( v ) {
		$$("#"+this.config.container+" div.t-title")[0].set('html',v);

	},
	buttonsHtml : function(buttons){
		var sb = "";
		if(buttons){
			buttons.each(function(item,index){
			//var css = "";
			//if(index!=0){
			var css = "";	
			if(item.css){
				css = item.css ;
			}
			//}
			if(!item.width){
				item.width= "65px";
			}
			sb += PA.TToolBar.BUTTON.substitute( Object.merge({ classFix:css},item));
			});
		}
		return sb;
	}

}

