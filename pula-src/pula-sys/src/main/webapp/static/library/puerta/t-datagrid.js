TDataGridEditor = function(id,generator,after,hidden){
	this.id = id ;
	this.generator = generator ; 
	this.hidden = hidden ;
	this.after = after;
}

TDataGridEditor.prototype = {

	init : function(){

	},
	build : function(p){
		return this.generator ( p ) ;
	},
	finish: function(obj,col){
		if(this.after){
			this.after(obj,col);
		}
	}
}

TDataGridEditor.DEFAULT_GENERATOR = function(g){
	return this.substitute(g);
};

TDataGridEditor.BUILD_DEFAULT = function(id,templet,hidden){
	return new TDataGridEditor( id , TDataGridEditor.DEFAULT_GENERATOR.bind(templet),null ,hidden) ;
}
TDataGridEditor.BUILD_AFTER = function(id,templet,aft,hidden){
	return new TDataGridEditor( id , TDataGridEditor.DEFAULT_GENERATOR.bind(templet),aft,hidden) ;
}

/**  internal editors        */
//hidden
TDataGridEditor.Hidden = TDataGridEditor.BUILD_DEFAULT( "hidden",'<input type="hidden" name="{field}" class="hid{field}" id="{id}"/>',true);
//text
TDataGridEditor.Text = TDataGridEditor.BUILD_DEFAULT( "text",('<input type="text" name="{field}" style="width:{width}" id="{id}" class="{css}"/>'),false);
//checkbox
TDataGridEditor.Checkbox = TDataGridEditor.BUILD_DEFAULT( "checkbox",	('<div class="text-c"><input type="checkbox" name="{field}" id="{id}" class="{css}"/></div>'),false);
//radio
TDataGridEditor.Radio = TDataGridEditor.BUILD_DEFAULT( "radio",	('<input type="radio" name="{field}" id="{id}" class="{css}"/>'),false);
//select
TDataGridEditor.Select = TDataGridEditor.BUILD_DEFAULT( "select",	('<select id="{id}" name="{field}" class="{css}" style="width:{width}" ></select>'),false);
//display
TDataGridEditor.Display = TDataGridEditor.BUILD_AFTER( "display",	('<div id="{id}" style="width:{width}" class="{css}"></div>'),function(obj,col){
	obj.addClass('t-datagrid-display');	
},false);

/**  internal editors ends  */

TDataGridRow = function (tr,tab){
		this.rowId = null ;
		this.tr = tr ;
		this.table= tab ;
}
TDataGridRow.HIDDEN = '<input type="hidden" name="{field}" class="hid{field}" id="{id}"/>';
TDataGridRow.prototype =  {


	init : function (){
		this.rowId = Number.uuid();
		this.tr.id = "tr"+this.rowId;
		this.table.rows.push(this.rowId);

		var cols = this.table.config.columns ;
		var lastName = null ;
		var line = "" ;
		var colIndex = 0 ;
		var hiddenFields = "";
		var hash = new Hash();	
		var hasAppendHidden = false;

		for(var i = 0 ;i < cols.length ; i ++){
			var col= cols[i];
			var id = col.field+this.rowId;

			if(!this.table.editors.has(col.type)){
				continue;
			}
			var edt = this.table.editors.get(col.type);

			if(edt.hidden){
				hiddenFields += edt.build({id:id,field:col.field}) ;				
			}	
		}


		for(var i = 0 ;i < cols.length ; i ++){
			var col= cols[i];
			var id = col.field+this.rowId;
			
			if(!this.table.editors.has(col.type)){
				continue;
			}
			var edt = this.table.editors.get(col.type);

			if(edt.hidden){
				hiddenFields += edt.build({id:id,field:col.field}) ;
				continue ;
			}			
			var tw = this.table.killPx(col.width) +4 ; //appendifx
			var td = new Element('div.t-datagrid-data',{				
				styles:{
					width:(tw +this.table.config.fixWidth )+'px'
				}				
			});
			td.inject(this.tr);

			var css = col.css || '';
			/*if(col.type=='text'){
				line = '<input type="text" name="{field}" style="width:{width}" id="{id}" class="{css}"/>';
			}else if(col.type=='checkbox'){
				line = '<input type="checkbox" name="{field}" id="{id}" class="{css}"/>';
			}else if(col.type=='radio'){
				line = '<input type="radio" name="{field}" id="{id}" class="{css}"/>';
			}else if(col.type=='bind'){
				line = '<div class="l"><select id="{id}" class="{css}"></select></div><div class="l" id="{id}name"></div>';
			}else if (col.type=='select'){
				line = '<select id="{id}" name="{field}" class="{css}"></select>';
			}else if (col.type=='display'){
				line = '<div id="{id}" style="width:{width}" class="{css}"></div>';				
			}else if (col.type=='my'){
				var fnMy = this.table.myFields.get(col.field);
				line = fnMy.getHtmls();
			}*/
			line = edt.build({id:id,field:col.field,width:this.getPrefWidth(col.width),css:css});
			if(!hasAppendHidden){
				line+=hiddenFields;
				hasAppendHidden = true ;
			}
			td.innerHTML = line;
			//alert(line);
			//events
			if(col.type=='text' && col.onchange ){
				$(id).addEvent('change',col.onchange.bind({col:col,id:id,rowId:this.rowId}));
			}
			if(col.type=='select' && col.data ) {
				this.fillOptions($(id),col.data );

			}
			if(col.type!='display'){
				lastName = id ;
			}

			/*if(col.data){
				col.data.each(function (item,index){
					
					var theOption=document.createElement("option"); 
					theOption.innerHTML= item.name; 
					theOption.value= item.no; 
					$(id).appendChild(theOption); 
				});
			}*/

			if(col.group){
				if(col.type == 'text'){
					hash.set('text',id);
					//gotblur
					$(id).addEvent('blur',this.table.blurGroupEditor.bind({col:col,id:id,rowId:this.rowId,table:this.table}));
				}else if (col.type=='bind')
				{
					hash.set('select',id);
					hash.set('name',id+"name");
					hash.set('url',col.url);
					$(id).addEvent('blur',this.table.blurGroupEditor.bind({col:col,id:id,rowId:this.rowId,table:this.table}));
				}
			}

			edt.finish(td,col);
		}

		if(hash.getLength()>0){
			//make shortcuteditor
			var edt = new NilShortcutEditor({
				editor:$(hash.get('text')),
				container:$(hash.get('name')),
				url :hash.get('url'),
				select:$(hash.get('select'))
			});	
		}
		
		if(lastName!=null&&!this.table.config.view){
			$(lastName).addEvent('keydown',function (ev){
				this.table.needNewRow(ev,this.rowId)
			}.bind(this));
		}
		if(this.table.config.remove){
			var td = new Element('div.t-datagrid-tool');
			td.inject(this.tr);
			td.innerHTML = '<A HREF="javascript:'+this.table.config.id+'.clear(\''+this.rowId+'\');"><IMG SRC="'+this.table.config.imagePath+'delete.gif" WIDTH="16" HEIGHT="16" BORDER="0" align="absbottom" title="删除" ></A>';
		}
		(new Element('span.c')).inject(this.tr);
	},
	getPrefWidth:function (w){
		return (Number.from(w) +this.table.config.fixWidth ) + 'px';
	},
	fillOptions :function(id,data){
		
		var ops = data.map(function(item){
			return new Element('option',{   
                                text: item.text,
                                value: item.value
            });
			
		});

		id.adopt(ops);
	}
}

TDataGrid = function (config){
		this.config = { add:true, remove : true,fixWidth:4 ,fixTotalWidth : 2} ;
		Object.merge(this.config,config);
		this.config.add = PA.utils.defaultTo( config.add ,this.config.add);
		this.config.remove = PA.utils.defaultTo( config.remove ,this.config.remove);

		if(config.view){
			this.config.add = false;
			this.config.remove = false;
		}
		this.tabId = "tab"+Number.uuid();
		this.init();

}
TDataGrid.APPEND_LINKS = "<div class='t-datagrid-tool'><A HREF=\"#\" onclick=\"javascript:{id}.addRow();void(0);\"><IMG SRC=\"{imagePath}add.gif\" WIDTH=\"16\" HEIGHT=\"16\" BORDER=\"0\" align=\"absbottom\"></A></div>";
TDataGrid.EMPTY_BLOCK = "<div class='t-datagrid-tool'></div>";
//TDataGrid.TABLE ="<table width=\"100%\" id=\"{id}\" class=\"{tableCss}\">{header}{rows}</table>";
TDataGrid.TABLE ="<div id=\"{id}\" class=\"t-datagrid {tableCss}\">{header}{body}</div>";
TDataGrid.prototype = {

		init: function(){
			
			this.rows = new Array();
			this.myFields = new Hash();
			this.editors = new Hash();
			this.registerDefaultEditors();
			this.vars =  {visibleColumnCount :0 } ;
		},
		registerDefaultEditors: function(){
			this.editors.set(TDataGridEditor.Hidden.id , TDataGridEditor.Hidden) ;
			this.editors.set(TDataGridEditor.Text.id , TDataGridEditor.Text) ;
			this.editors.set(TDataGridEditor.Checkbox.id , TDataGridEditor.Checkbox) ;
			this.editors.set(TDataGridEditor.Radio.id , TDataGridEditor.Radio) ;
			this.editors.set(TDataGridEditor.Select.id , TDataGridEditor.Select) ;
			this.editors.set(TDataGridEditor.Display.id , TDataGridEditor.Display) ;
		},
		addRow : function (){
			var tab = $(this.tabId);
			var tr = new Element('div.t-datagrid-row');
			tr.inject(tab);
			var row = new TDataGridRow(tr,this);
			row.init();
			if(this.config.afterAddRow){
				this.config.afterAddRow(row);
			}	
			return row;
		},
		clear : function (id){
			var tab = $(this.tabId);
			/*$('name'+id).value ="";
			$('mobile'+id).value ="";
			$('phone'+id).value ="";
			$('email'+id).value ="";*/
			var i = this.seekId(id);
			this.rows.erase(id);
			tab.getChildren("div.t-datagrid-row")[i].dispose();
			//alert('hi');
			if(this.config.removeListener){
				this.config.removeListener(id);
			}
		},
		seekId:function (id){
			for(var i = 0 ;i < this.rows.length ;i ++){
				if(this.rows[i]==id){
					//remove
					return i ;
				}		
			}
			return -1; 
		},
		nextId:function (id){
			var index = this.seekId (id);
			if(index+1 < this.rows.length){
				return this.rows[index+1];
			}
			return id;
		},
		getHeader:function(){
			var cols = this.config.columns ;
			/*/var sb = "<colgroup>";
			for(var i = 0 ;i < cols.length ; i ++){
				var col  = cols[i];
				if(col.type=='hidden'){
					continue ;
				}
				sb += "<col style=\"width:"+col.width+"\"></col>";
			}
			//append links
			sb += "<col style=\"width:16px\"></col>";*/
			var sb = "<div class='t-datagrid-header'>";
			var col_count = 0 ;
			for(var i = 0 ;i < cols.length ; i ++){
				var col  = cols[i];
				//unknow type .not register;
				//alert(col.type);
				if(!this.editors.has( col.type ) ){
					sb += "<div>N/A</div>";
					continue ;
				}
				var ce = this.editors.get(col.type) ;
				if(ce.hidden){
					continue ;
				}

				col_count ++ ;

				/*if(col.type=='my'){
					var v = new col.toolClass();
					this.myFields.set(col.field,v);
				}*/
				var tw = this.killPx(col.width) +4 ; //appendifx
				//alert(tw);
				var css ="t-datagrid-column t-datagrid-column-text";
				if(col.headerCss){
					css += " "+ col.headerCss ;
				}
				css= "class=\""+ css + "\"";
				sb += "<div "+css+" style='width:"+tw+"px'>"+col.text+"</div>";
				
			}
			
			if(this.config.add){
				sb += TDataGrid.APPEND_LINKS.substitute({id:this.config.id,imagePath:this.config.imagePath});
			}else if ( this.config.remove)
			{
				sb += TDataGrid.EMPTY_BLOCK;
			}
			sb +="<span class='c'></span>";
			this.vars.visibleColumnCount = col_count ;
			return sb;
		},
		killPx:function(v){
			var _v = String.from(v);
			if(_v.indexOf('px')>0){
				return Number.from( _v.substring(0,_v.length-2));
			}
			return Number.from(v);
		},
		needNewRow: function(ev,rowId){
			if(ev.key != 'tab' && ev.key!='enter'){
				return ;
			}
			ev.stop();

			if(this.rows.length >= this.config.limitRows){
				return ;
			}
			if(this.rows[this.rows.length-1] != rowId ){
				this.focusFirstColumn(this.nextId(rowId));
				return ;
			}
			var row = this.addRow();
			this.focusFirstColumn(row.rowId);
			
			return ;
		},
		focusFirstColumn:function(rid){
			var cols = this.config.columns ;
			if(cols.length>0){
				var fn = cols[0].field+rid;
				$(fn).focus();
			}
		},
		draw:function(){
			var sb = TDataGrid.TABLE.substitute({id:this.tabId,header:this.getHeader(),tableCss:this.config.tableCss}); 			
			$(this.config.container).innerHTML = sb ;
			//fix size
			var ff = function(){
				var w = 0 ;
				var id = "#"+this.tabId +' .t-datagrid-column,#'+this.tabId +' .t-datagrid-tool';
				//alert(id+">"+$$(id).length );
				$$(id).each ( function (el){
					var size = el.getSize();
					//alert( JSON.encode(size )) ;
					w+=size.x;
				});
				$(this.tabId).setStyle('width',w+this.config.fixTotalWidth);
			}
			
			ff.bind(this)();
			
		},
		fill:function (data,append){
			var rc = this.rows.length ;
			var startRow = 0 ;
			if(append){
				startRow = rc ;
			}
			for(var i = rc ; i < startRow + data.length ;i ++){
				this.addRow();	
			}
			
			for(var i = 0 ; i < data.length ;i ++){
				var id = this.rows[i+startRow];
				var cols = this.config.columns ;
				for(var j = 0 ; j < cols.length;  j ++){
					var f = cols[j].field;
					var c = data[i][f];
					//alert(c+ ' field='+f);
					if(c){
						if(cols[j].type=='bind' ){
							$(f+id+"name").innerHTML = c; 
						}else if(cols[j].type=='display'){
							$(f+id).innerHTML = c;
						}else if(cols[j].type=='checkbox'){
							$(f+id).checked = c;
						}else if ( cols[j].type == 'my')
						{
							this.myFields.get(cols[j].field).setValue(f+id, c);
						}else{
							try{
								$(f+id).value =  c;
							}catch(e){
								alert('cannot found='+(f+id));
							}
						}
					}
				}
				if(data[i].properties){
					if(data[i].properties.disabled&& data[i].properties.disabled.length!=0){
						data[i].properties.disabled.each(function (ee){
								this.setDisabledAt(ee,id,true);
						}.bind(this));
					}
					if(data[i].properties.css&& data[i].properties.css.length!=0){
						data[i].properties.css.each(function (ee){
								this.addCss(ee[0],id,ee[1]);
						}.bind(this));
					}
					if(data[i].properties.checked && data[i].properties.checked.length!=0){
						data[i].properties.checked.each(function (ee){
								$(ee+id).set('checked',true);
						}.bind(this));
					}
				}
			}//for
			if(this.config.afterFill){
				this.config.afterFill();
			}	
		},
		reset:function(){
			//init();
			var tab = $(this.tabId);
			for(var i = 0 ; i < this.rows.length ; i ++){
				tab.getChildren()[1].dispose();
			}
			this.init();
		},
		//events 

		blurGroupEditor:function(e){
			if(this.table.config.blurGroupEditor){
				this.table.config.blurGroupEditor.bind(this)(e);
			}
		},
		/////////////////////////// get values ///////////////////////////////////////
		getColumn:function(_col){
			var gotCol = null;
			this.config.columns.each(function (e){
				if(gotCol !=null){
					return ;
				}
				if(e.field==_col){
					gotCol = e; 
				}
			});
			return gotCol;
		},
		getValueAt:function(_col,_row){
			var col = this.getColumn(_col);
			if(col==null){
				return null; 
			}
			//var index = this.rows.indexOf(_row);
			if(col.type=='display'){
				return $(_col+_row).innerHTML ;

			}else if(col.type=='checkbox'){
				return $(_col+_row).checked;
			}else
			{
				try{
					return $(_col+_row).value ;	
				}catch(e){
					alert ("error on getValueAt:"+ _col + " r="+_row);
					throw e; 
				}
			}
		},
		getFloatAt:function(_col,_row){
			var v = this.getValueAt(_col,_row);
			if(v==null){
				return 0;
			}
			if(v==""){
				v= "0";
			}
			var n=  Number.from(v);
			if(n==null){
				return 0 ;
			}
			return n.toFloat() ;
		},
		eachRow : function ( _colo, call){
			this.rows.each( function ( row , nIndex ) {
				//var col = this.getColumn(_col);
				if( typeOf(_colo) == 'array'){
					_colo.each( function (e){
						var v = this.getValueAt(e,row);
						call(v,nIndex,e);
					}.bind(this));
				}else{
					var v = this.getValueAt(_colo,row);
					call(v,nIndex,_colo);
				}
			}.bind(this));
		},
		///////////////////// set values ////////////////////////////////////////////
		setValueAt:function(_col,_row,v){
			var col = this.getColumn(_col);
			if(col==null){
				return false;
			}
			//var index = this.rows.indexOf(_row);
			if(col.type=='display'){
				$(_col+_row).innerHTML = v ;
			}else
			{
				$(_col+_row).value = v ;
			}
			return true ;
		},
		toJson:function(){
			var objs = "[";

			this.rows.each( function ( row , nIndex ) {
				var obj = "{";
				this.config.columns.each(function (e,index){
					var v = this.getValueAt(e.field,row) ;
					if(index ==0 ){
						obj += e.field +":'"+v+"'";
					}else{
						obj += "," + e.field +":'"+v+"'";
					}
					
				}.bind(this));

				objs+=(obj)+"}";

				if(nIndex == this.rows.length -1 ){
					objs +="\n";
				}else{
					objs +=",\n";
				}
			}.bind(this));
			objs+="]";
			return objs;
		},
		buildJson:function( c ) {
			var objs = [];
			this.rows.each( function ( row , nIndex ) {
				var obj = {};
				
				var firstColumn = true;
				this.config.columns.each(function (e,index){
					var jsonFlag = {ignore:true,quote:false};
					
					var v = this.getValueAt(e.field,row) ;
					c({col:e.field,row:row,flag:jsonFlag,value:v});

					if(jsonFlag.ignore){
						return ;	
					}
					//if(!firstColumn){
					//	obj += ",";
					//}
					//firstColumn = false;

					//obj += "\""+e.field+"\":";
					if(jsonFlag.quote){
						//obj+="\""+v.replace(/[\x00-\x1f\\"]/g, JSON.$replaceChars)+"\"";
						obj[ e.field ] = v ;
					}else{
						//obj += this.fixIfEmpty(v) ;
						obj[ e.field ] = this.fixIfEmpty( v ) ;
					}

				}.bind(this));
				//objs+=(obj)+"}";
				objs.push( obj ) ;
				/*if(nIndex == this.rows.length -1 ){
					objs +="\n";
				}else{
					objs +=",\n";
				}*/
			}.bind(this));
			//objs+="]";
			return JSON.encode( objs );			

		},
		fixIfEmpty : function (v){
			if(v==""){
				return 0 ;
			}

			return eval( v ) ;

			//if(v.length>=2&&v.substring(0,1)=="0"){
			//	return this.killFirstZero(v);
			//}
//			return v ;
		},
		fixLine: function(){
			var css = ".t-datagrid .t-datagrid-row";
			var els = $(this.config.container).getChildren(css);
			//alert(els.length + ' >'+css);
			els.each (function (el){
				var height = el.getSize().y;
				var datas = el.getChildren(".t-datagrid-data");
				//alert(datas.length + '>height:'+height);
				datas.each ( function (data){
					data.setStyle('height',height);
				});
			});
		},
		killFirstZero :function(v){
			for(var i = 0 ; i < v.length ; i++){
				var c = v.charAt(i);
				if(c=='0'){
					continue ;
				}else{
					return v.substring(i,v.length ) ;
				}
			}
		},
		///////////////////// validate ////////////////////////////////////////
		onValidate : function ( call, flag){
			//check all row ;
			var eiHash = [];
			this.rows.each( function ( row , index ) {
				var _f = Object.clone(flag);
				this.config.columns.each(function (e){
					var ei = call( { row : row, col: e.field , flag: _f, value :this.getValueAt(e.field,row)},this);
					if (ei!=null)
					{
						eiHash.push({index:index,message:ei});
					}

				}.bind(this));

				
			}.bind(this));
			return eiHash;
		},
		////////////////////////////// set ui /////////////////////////////////
		setDisabledAt : function ( _col, _row, d){
			$(_col+_row).set('disabled',d);			
		},
		addCss : function ( _col, _row, css){
			$(_col+_row).addClass(css);			
		}
		////////////////////////////// calc /////////////////////////////////

}

TDataGridRow.getId = function(id){
	return id.substring(id.length-36  ,id.length);
}