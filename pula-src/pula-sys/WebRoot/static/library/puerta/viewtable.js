

NilViewTablePager = function(config){
	
	this.config = config ;
	this.init();
}
NilViewTablePager.TEMPLET = "{firstPage}{prevPage} {nextPage}{lastPage}{reload} {totalRecords} {totalPage} {jumpText}  <div class='c'></div>";
NilViewTablePager.prototype ={
	init : function(){
		this.pageIndex = this.config.pageIndex || 1 ;
		//make input 
		var s = NilViewTablePager.TEMPLET.substitute(
				{
					firstPage:'<div class="DtPagerLink DtFirstPage"></div>',
					prevPage:'<div class="DtPagerLink DtPrevPage"></div>',
					jumpText:'<div class="DtJumpPage">'+_lang.vt.indexPage.substitute({
						pageNo:'<input type="text" value="" size="5"/>'}
						)+'&nbsp;</div>',
					totalPage:'<div class="DtTotalPage"></div>',
					nextPage:'<div class="DtPagerLink DtNextPage"></div>',
					lastPage:'<div class="DtPagerLink DtLastPage"></div>',
					reload:'<div class="DtPagerLink DtReload"></div>',
					totalPage:'<div class="DtTotalPage"></div>',//this.getTotalPage()
					totalRecords:'<div class="DtTotalRecords"></div>'//this.getTotalPage()
				}
			);
		
		var ct = this.config.container;
		ct.innerHTML = s ;
		this.totalPageDiv= ct.getElement(".DtTotalPage");
		this.totalRecordsDiv = ct.getElement(".DtTotalRecords");
		this.pageInput= ct.getElement(".DtJumpPage INPUT");
		this.firstPageDiv = ct.getElement(".DtFirstPage");
		this.nextPageDiv = ct.getElement(".DtNextPage");
		this.prevPageDiv = ct.getElement(".DtPrevPage");
		this.lastPageDiv = ct.getElement(".DtLastPage");
		this.reload = ct.getElement(".DtReload");

		
		this.pageInput.addEvent('keydown',this.changePage.bind(this));
		this.firstPageDiv.addEvent('click',this.firstPage.bind(this));
		this.nextPageDiv.addEvent('click',this.nextPage.bind(this));
		this.prevPageDiv.addEvent('click',this.prevPage.bind(this));
		this.lastPageDiv.addEvent('click',this.lastPage.bind(this));
		this.reload.addEvent('click',this.reloadPage.bind(this));

		//
		this.updateState();

	},
	reloadPage: function (){
		this.config.owner.reload();
	},
	update : function(tps,pi,trs){
		
		this.totalPages = tps;	
		//alert(this.pageIndex+":"+tps);
		if(this.pageIndex>tps&&tps!=0){
			//this.pageInput.value = pi ;	
		}else{

			if(this.pageIndex!=pi)
				this.pageIndex = pi ;
			this.pageInput.value = pi ;
		}
		this.totalRecords =  trs; 
		
		this.updateState();
	},
	updateState:function(){
		var tp = this.getTotalPages();
		var tr = this.getTotalRecords();
		var pi = this.pageIndex;
		this.totalPageDiv.innerHTML = _lang.vt.totalPages.substitute({t:tp});
		this.totalRecordsDiv.innerHTML = _lang.vt.totalRecords.substitute({t:tr});
		if(tp==1||tp==0){
			this.switchCss(this.firstPageDiv,"DtFirstPage","DtFirstPageD");
			this.switchCss(this.firstPageDiv,"DtPagerLink","DtPagerLinkD");
			this.switchCss(this.nextPageDiv,"DtNextPage","DtNextPageD");
			this.switchCss(this.nextPageDiv,"DtPagerLink","DtPagerLinkD");
			this.switchCss(this.prevPageDiv,"DtPrevPage","DtPrevPageD");
			this.switchCss(this.prevPageDiv,"DtPagerLink","DtPagerLinkD");
			this.switchCss(this.lastPageDiv,"DtLastPage","DtLastPageD");
			this.switchCss(this.lastPageDiv,"DtPagerLink","DtPagerLinkD");
		}
		if(tp>1&&pi<tp){
			this.switchCss(this.lastPageDiv,"DtLastPageD","DtLastPage");
			this.switchCss(this.lastPageDiv,"DtPagerLinkD","DtPagerLink");
			this.switchCss(this.nextPageDiv,"DtNextPageD","DtNextPage");
			this.switchCss(this.nextPageDiv,"DtPagerLinkD","DtPagerLink");	
		}else{
			this.switchCss(this.lastPageDiv,"DtLastPage","DtLastPageD");
			this.switchCss(this.lastPageDiv,"DtPagerLink","DtPagerLinkD");
			this.switchCss(this.nextPageDiv,"DtNextPage","DtNextPageD");
			this.switchCss(this.nextPageDiv,"DtPagerLink","DtPagerLinkD");
		}
		if(tp>1&&pi>1){
			this.switchCss(this.firstPageDiv,"DtFirstPageD","DtFirstPage");
			this.switchCss(this.firstPageDiv,"DtPagerLinkD","DtPagerLink");	
			this.switchCss(this.prevPageDiv,"DtPrevPageD","DtPrevPage");
			this.switchCss(this.prevPageDiv,"DtPagerLinkD","DtPagerLink");	
		}else{
			this.switchCss(this.firstPageDiv,"DtFirstPage","DtFirstPageD");
			this.switchCss(this.firstPageDiv,"DtPagerLink","DtPagerLinkD");
			this.switchCss(this.prevPageDiv,"DtPrevPage","DtPrevPageD");
			this.switchCss(this.prevPageDiv,"DtPagerLink","DtPagerLinkD");
		}
		this.pageInput.value = pi;
	},
	switchCss:function(obj,css,toCss,css2,toCss2){
			obj.removeClass(css);
			obj.removeClass(css2);
			obj.addClass(toCss);
			obj.addClass(toCss2);
	},
	firstPage:function(e){
		var tp = this.getTotalPages();
		var pi = this.pageIndex;
		if(tp>1&&pi>1){
			this.pageIndex = 1; 
			this.config.owner.reload();
		}
	},
	nextPage:function(e){
		var tp = this.getTotalPages();
		var pi = this.pageIndex;
		if(tp>1&&pi<tp){
			this.pageIndex ++; 
			this.config.owner.reload();
		}
	},
	lastPage:function(e){
		var tp = this.getTotalPages();
		var pi = this.pageIndex;
		if(tp>1&&pi<tp){
			this.pageIndex = this.getTotalPages(); 
			this.config.owner.reload();
		}
	},
	prevPage:function(e){
		var tp = this.getTotalPages();
		var pi = this.pageIndex;
		if(tp>1&&pi>1){
			this.pageIndex--; 
			this.config.owner.reload();
		}
	},
	getTotalPages:function(){

		return this.totalPages||1;
	},
	getTotalRecords:function(){

		return this.totalRecords||0;
	},
	changePage: function(e){
		if(e.key=='enter'){
			var v = e.target.value ;
			var n = Number.from(v);
			if(n>0){
				var tp = this.getTotalPages();
				if(n>tp){
					alert(_lang.vt.outOfMaxPage.substitute({t:tp}));
					e.stop();
					return ;
				}
				this.pageIndex = n; 
				this.config.owner.reload();
			}else{
				alert(_lang.vt.invalidPageNo.substitute({t:v}));
			}
			e.stop();
		}
	}
	

}

/*** table ***/
NilViewTable = function (config){
		this.config = config ;

		this.config.noFooter = this.config.noFooter|| false;
		this.config.unSelectText = this.config.unSelectText|| false;
		this.config.dblClickRowToCheck = this.config.dblClickRowToCheck || null ;
		this.init();

}


NilViewTable.TABLE ="<table id=\"{id}\" class=\"{tableCss}\">{header}{body}</table>";
NilViewTable.SCROLL = 18;
NilViewTable.prototype = {
		init: function(){
			this.tabId			=	"tab"+this.config.id;
			this.dataTabId		=	"datatab"+this.config.id;
			this.headerId		=	"header"+this.config.id;
			this.bodyId			=	"body"+this.config.id;
			this.footerId		=	"footer"+this.config.id;
			this.rowIndex		=	-1;
			this.valueChecked	=	new Array();

			if(this.config.rows == null){
				this.rows = new Array();
			}else{
				this.rows = this.config.rows ;
			}
			this.myFields = new Hash();
			$(this.config.container).addClass('VT');
		},
		draw : function(){
			//make html 
			var sb = NilViewTable.TABLE.substitute({id:this.tabId,header:this.getHeader(true)}) ;
			sb = "<div id='"+this.headerId+"' class='dtHeader'>"+sb+"</div><div id='"+this.bodyId+"' class='yScroll'></div>";
			if(!this.config.noFooter){
				sb+="<div id='"+this.footerId+"' class='dtFooter'></div>";
			}
			
			$(this.config.container).innerHTML = sb ;
			if(this.rows.length!=0){
				var dataRows = this.getRows();
				$(this.bodyId).innerHTML =dataRows;
			}
			this.fixSize();
			
			$(this.bodyId).setStyle('borderLeft','1px solid #ccc');
			$(this.bodyId).setStyle('borderRight','1px solid #ccc');
			if(this.config.noFooter){
				$(this.bodyId).setStyle('borderBottom','1px solid #ccc');		
			}else{
				this.pager = new NilViewTablePager({owner:this,container:$(this.footerId)});
			}

			if(this.config.unSelectText){
				$(this.config.container).addEvent('selectstart',function(e){
					return false;
				});
			}
		},
		fixSize:function(){
			if(Browser.ie){
				$(this.bodyId).setStyle('width',this.width+NilViewTable.SCROLL);
				if(!this.config.noFooter){
					
					$(this.footerId).setStyle('width',this.width+NilViewTable.SCROLL);
				}
				$(this.headerId).setStyle('width',this.width+NilViewTable.SCROLL);
			}else{
				$(this.bodyId).setStyle('width',this.width+NilViewTable.SCROLL);
				if(!this.config.noFooter){
					$(this.footerId).setStyle('width',this.width+NilViewTable.SCROLL);
				}
				//$(this.dataTabId).setStyle('width',this.width);
				$(this.headerId).setStyle('width',this.width+NilViewTable.SCROLL);
			}
			$(this.bodyId).setStyle('height',this.config.height - 20);
			$(this.config.container).setStyle('width',this.width+NilViewTable.SCROLL);
		},
		getHeader:function(header){
			var cols = this.config.columns ;
			var sb = "<colgroup>";
			var w = 0 ;
			for(var i = 0 ;i < cols.length ; i ++){
				var col  = cols[i];
				var tw = col.width ;
				sb += "<col style=\"width:"+tw+"px\"></col>";
				w += col.width ;
			}
			this.width = w ;
			if(header){
				sb+="<tr class='dtColumn'>";
				for(var i = 0 ;i < cols.length ; i ++){
					var col  = cols[i];
					var css ="";
					if(i==0){
						css = " firstColumn";
					}
					if(col.headerCss){
						css += " "+col.headerCss;
					}
					sb += "<td class='"+css+"'>"+col.label+"</td>";
				}
				sb +="</tr>";
			}else{
				sb +="</colgroup>";
			}

			return sb;
		},
		getRows:function(){
			var dt = this ;
			//也得拼凑column 一整套
			var sb = "" ;
			this.rows.each( function (e,rowIndex){
				var css = "dtData";
				if( (rowIndex % 2) ==1){
					css += " oddRow";
				}
				sb +="<tr class='"+css+"' id='"+dt.tabId+rowIndex+"'>";
				dt.config.columns.each( function(c,index){
					var data = e[c.key];
					//if(typeof(data)=='number')
					//alert(typeof(data) + "-----------:"+data);
					if(typeof(data)=='string'){
						data = PA.utils.escapeHTML( data ) ;
					}
					if(typeof(data)=='undefined'){
						data = "";
					}
					if(data==null){
						data = "";
					}
					var w = c.width ;
					if(c.formatter){
						if(c.formatter== NilViewTable.formatCheckbox && dt.hasChecked(data) ){
							e[c.key +".checked" ] = true ;
						}
						data = c.formatter( e,c, data);
					}
					/*if(index==0){
						w=-3 ;
						
					}*/
					var css = "";
					if(rowIndex!=dt.rows.length-1){
						css += " normal";
					}else{
						css = " lastRow";
					}
					var textCss = "";
					if (c.css)
					{
						textCss = c.css;
					}

					if(Browser.ie){
						sb += "<td style='word-break:break-all;' class='"+css+' '+textCss+"'>"+data+"</td>";
					}else{
						sb += "<td  class='"+css+"'><div style='word-break:break-all;overflow:auto;width:"+(w-3)+"px;' class='"+textCss+"'>"+data+"</div></td>";
					}
				});
				sb+="</tr>";
			});
			var table = NilViewTable.TABLE.substitute({id:this.dataTabId,header:this.getHeader(false),body:sb}) ;
			return table;
		},
		storeCheckState:function(){
			this.valueChecked.empty();
			var dt = this ;
			var ss = $(this.config.container).getElements(".objId:checked");
			ss.each( function (element){
				dt.valueChecked.push(element.value);
			});
			dt = null ;			
		},
		hasChecked:function(value){
			if(this.valueChecked.contains(String(value))){
				return true ;
			}
			return false;
		},
		reload:function(){

			this.storeCheckState();
			//向AJAX发出请求

			var param = "";
			if(!this.config.noFooter){	
				param = "pageIndex="+this.pager.pageIndex;
			}else{
			}
			if(this.config.requestParam){
				param += (this.config.requestParam() || "") ;
			}

			PA.ajax.gf(this.config.url,param,function (e){
				if(e.error){
					alert(e.message);
					this.hideLoading();
				}else{
					//alert(JSON.encode(e));
					this.showData(e);
					this.hideLoading();
					if(this.config.onReload){
						this.config.onReload(e.data,this);
					}	
				}

			}.bind(this));
			
			//clear listener
			$(this.bodyId).getElements(".dtData").removeEvents(['mouseover','mouseout','click','dblclick']);

			//界面切换成载入状态
			$(this.bodyId).innerHTML = "";
			//创建一个层，用来显示loading
			if(!this.loading ){
				this.loading = new Element('DIV.loading');
				this.loading.inject(document.body);
			}

			
			PA.ui.centerAt(this.loading,this.bodyId);
			this.loading.setStyle('display','block');

		},
		showData:function(dl){
			this.rowIndex = -1 ;
			this.rows = dl.records ;
			//alert(JSON.encode(dl));
			this._showData();
			//pager
			if(!this.config.noFooter){
				this.pager.update(dl.totalPages,dl.pageIndex,dl.totalRecords);
			}
		},
		clear:function(){
			function EmptyData(){
				this.records = [] ;
				this.totalPages = 0 ;
				this.pageIndex = 0 ;
				this.totalRecords = 0 ;
			}
			var obj  = new EmptyData();
			this.showData( obj );

		},
		manualData:function(d){
			this.rows = d; 
			this.rowIndex = -1 ;
			this._showData();
		},
		_showData : function(){
			var dataRows = this.getRows();
			$(this.bodyId).innerHTML =dataRows;
			var dt = this ;
			$(this.bodyId).getElements(".dtData").addEvent('mouseover',function(e){
				var tr = dt.getTr(e.target);
				if(tr!=null){
					tr.addClass('onRow');
					if(tr.hasClass('oddRow')){
						tr.removeClass('oddRow');
						tr.addClass('_oddRow');
					}
				}
			}).addEvent('mouseout',function(e){
				var tr = dt.getTr(e.target);
				if(tr!=null){
					tr.removeClass('onRow');
					if(tr.hasClass('_oddRow')&&!tr.hasClass('selectedRow')){
						tr.removeClass('_oddRow');
						tr.addClass('oddRow');
					}
				}
			}).addEvent('click',function(e){
				var tr = dt.getTr(e.target);
				if(tr!=null){
					dt.selectedRow(tr);
				}
			}).addEvent('dblclick',function(e){
				var tr = dt.getTr(e.target);
				if(tr!=null){
					dt.intoRow(tr);
				}
			});

		},
		intoRow:function(tr){
			if(tr==null){
				return ;
			}
			var idx = Number.from(tr.id.substring(this.tabId.length,tr.id.length));


			if(this.config.dblClickRowToCheck != null ){
				var d = this.rows[idx] ;
				var cid = d[this.config.dblClickRowToCheck];
				$$("#"+this.config.container+" input.objId").each(function (e){
					if(e.value == cid){
						e.checked = !e.checked;
					}
				});
			}

			if(this.config.intoRow){
				this.config.intoRow(idx,tr);
			}
		},
		selectedRow:function(tr){
			if(tr==null){
				this.unSelectRow();
				this.rowIndex = -1 ;
				if(this.config.selectRow) this.config.selectRow(this.rowIndex,null);
			}else{
				var id = Number.from(tr.id.substring(this.tabId.length,tr.id.length));
				this.unSelectRow();
				this.rowIndex = id; 
				if(tr.hasClass('oddRow')){
					tr.removeClass('oddRow');
					tr.addClass('_oddRow');
				}
				tr.addClass('selectedRow');
				if(this.config.selectRow){
					this.config.selectRow(this.rowIndex,tr);
				}
			}
		},
		unSelectRow:function(){
			if(this.rowIndex!=-1){
				var arr = $(this.bodyId).getElements("tr");
				if(arr.length > this.rowIndex){
					var obj = arr[this.rowIndex] ;
					obj.removeClass('selectedRow');
					if(obj.hasClass('_oddRow')){
						obj.removeClass('_oddRow');
						obj.addClass('oddRow');
					}
				}
			}
		},
		getTr: function(t){
			if(t.tagName=='tr'){return t;};
			return t.getParent("tr");
		},
		hideLoading:function(){
			if(this.loading ){
				this.loading.setStyle('display','none');
			}
		},
		fireSelect:function(){

			if(this.config.selectRow)
				this.config.selectRow(this.rowIndex,null);
			//this.selectedRow(null);
			
		}
		
}

NilViewTable.formatCheckbox = function( oRecord, oColumn, oData) {
		var value = ' value="'+oData+'"' ;
		var checked = (oRecord[oColumn.key+".checked"])? ' checked' : '';
        return "<input type=\"checkbox\"" + value +
                " name=\"objId\" class=\"objId\" "+checked+"/>";
};
NilViewTable.formatBoolean = function( oRecord, oColumn, oData) {
		return  (oData)? _lang.yes: "<font color='#cccccc'>"+_lang.no+"</font>";
};
NilViewTable.formatEnabled = function( oRecord, oColumn, oData) {
		return  (oData)? _lang.enabled:_lang.disabled;
};
NilViewTable.checkAll = "<input type='checkbox' id='checkAll' onclick='checkAllCss(this.checked,\".objId\")'/>";
NilViewTable.checkAllExt = "<input type='checkbox' id='checkAll' onclick='checkAllCss(this.checked,\"#{id} .objId\")'/>";

