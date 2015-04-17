NilSimpleTableRow = function (tr,tab){
		this.rowId = null ;
		this.tr = tr ;
		this.table= tab ;
}
NilSimpleTableRow.HIDDEN = '<input type="hidden" name="{field}" class="hid{field}" id="{id}"/>';
NilSimpleTableRow.prototype =  {


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

		for(var i = 0 ;i < cols.length ; i ++){
			var col= cols[i];
			var id = col.field+this.rowId;
			if(col.type=='hidden'){
				hiddenFields+= NilSimpleTableRow.HIDDEN.substitute({id:id,field:col.field});
			}
		}


		for(var i = 0 ;i < cols.length ; i ++){
			var col= cols[i];
			var id = col.field+this.rowId;
			if(col.type=='hidden'){
				continue;
			}
			var td = this.tr.insertCell(colIndex++);
			var css = col.css || '';
			if(col.type=='text'){
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
			}
			line = line.substitute({id:id,field:col.field,width:this.getPrefWidth(col.width),css:css});
			if(i==0){
				line += hiddenFields;
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
		if(!this.table.config.view){
			td = this.tr.insertCell(colIndex++);
			td.innerHTML = '<A HREF="javascript:'+this.table.config.id+'.clear(\''+this.rowId+'\');"><IMG SRC="'+this.table.config.imagePath+'delete.gif" WIDTH="16" HEIGHT="16" BORDER="0" align="absbottom" title="新增" ></A>';
		}
	},
	getPrefWidth:function (w){
		return (Number.from(w) - 2) + 'px';
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

NilSimpleTable = function (config){
		this.config = config ;
		this.init();

}
NilSimpleTable.APPEND_LINKS = "<td><A HREF=\"javascript:{id}.addRow();void(0);\"><IMG SRC=\"{imagePath}add.gif\" WIDTH=\"16\" HEIGHT=\"16\" BORDER=\"0\" align=\"absbottom\"></A></td>";
NilSimpleTable.TABLE ="<table width=\"100%\" id=\"{id}\" class=\"{tableCss}\">{header}{rows}</table>";
NilSimpleTable.prototype = {

		init: function(){
			this.tabId = "tab"+this.config.id;
			this.rows = new Array();
			this.myFields = new Hash();
		},

		addRow : function (){
			var tab = $(this.tabId);
			var tr = tab.insertRow(-1);
			var row = new NilSimpleTableRow(tr,this);
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
			tab.deleteRow(i+1);
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
			var sb = "<colgroup>";
			for(var i = 0 ;i < cols.length ; i ++){
				var col  = cols[i];
				if(col.type=='hidden'){
					continue ;
				}
				sb += "<col style=\"width:"+col.width+"\"></col>";
			}
			//append links
			sb += "<col style=\"width:16px\"></col>";
			sb+="<tr class='header'>";
			for(var i = 0 ;i < cols.length ; i ++){
				var col  = cols[i];
				if(col.type=='hidden'){
					continue ;
				}
				if(col.type=='my'){
					var v = new col.toolClass();
					this.myFields.set(col.field,v);
				}
				var css ="";
				if(col.headerCss){
					css = "class=\""+ col.headerCss + "\"";
				}
				sb += "<td "+css+">"+col.text+"</td>";
			}
			if(!this.config.view){
				sb += NilSimpleTable.APPEND_LINKS.substitute({id:this.config.id,imagePath:this.config.imagePath});
			}
			sb +="</tr>";
			return sb;
		},
		needNewRow: function(ev,rowId){
			if(ev.key != 'tab' && ev.key!='enter'){
				return ;
			}
			ev.stop();
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
			var sb = NilSimpleTable.TABLE.substitute({id:this.tabId,header:this.getHeader(),tableCss:this.config.tableCss}); 
			$(this.config.container).innerHTML = sb ;
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
						}else if ( cols[j].type == 'my')
						{
							this.myFields.get(cols[j].field).setValue(f+id, c);
						}else{
							try{
								$(f+id).value =  c;
							}catch(e){
								alert(f+id);
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
			}
		},
		reset:function(){
			//init();
			var tab = $(this.tabId);
			for(var i = 0 ; i < this.rows.length ; i ++){
				tab.deleteRow(1);
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
			}else
			{
				return $(_col+_row).value ;
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
			var objs = "[";
			this.rows.each( function ( row , nIndex ) {
				var obj = "{";
				
				var firstColumn = true;
				this.config.columns.each(function (e,index){
					var jsonFlag = {ignore:true,quote:false};
					c({col:e.field,row:row,flag:jsonFlag});
					var v = this.getValueAt(e.field,row) ;

					if(jsonFlag.ignore){
						return ;	
					}
					if(!firstColumn){
						obj += ",";
					}
					firstColumn = false;

					obj += "\""+e.field+"\":";
					if(jsonFlag.quote){
						obj+="\""+v.replace(/[\x00-\x1f\\"]/g, JSON.$replaceChars)+"\"";
					}else{
						obj += this.fixIfEmpty(v) ;
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
		fixIfEmpty : function (v){
			if(v==""){
				return 0 ;
			}
			return v ;
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

NilSimpleTableRow.getId = function(id){
	return id.substring(id.length-36  ,id.length);
}