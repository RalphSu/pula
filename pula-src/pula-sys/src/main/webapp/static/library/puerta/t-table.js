

TTablePager = function(config){
	
	this.config = config ;
	this.init();
}
TTablePager.TEMPLET = "{firstPage}{prevPage} {nextPage}{lastPage}{reload} {totalRecords} {totalPage} {jumpText}  <div class='c'></div>";
TTablePager.prototype ={
	init : function(){
		this.pageIndex = this.config.pageIndex || 1 ;
		//make input 
		var s = TTablePager.TEMPLET.substitute(
				{
					firstPage:'<div class="DtPagerLink DtFirstPage"></div>',
					prevPage:'<div class="DtPagerLink DtPrevPage"></div>',
					jumpText:'<div class="DtJumpPage">第<input type="text" value="" size="5"/>页&nbsp;</div>',
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
		this.totalPageDiv.innerHTML = "共"+ tp +"页";
		this.totalRecordsDiv.innerHTML = "共"+tr +"个记录";
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
					alert("超出了最大页码:"+tp);
					e.stop();
					return ;
				}
				this.pageIndex = n; 
				this.config.owner.reload();
			}else{
				alert("无效的页码");
			}
			e.stop();
		}
	}
	

}

/*** table ***/
TTable = function (config){
		this.config = config ;

		this.config.noFooter = this.config.noFooter|| false;
		this.config.unSelectText = this.config.unSelectText|| false;
		this.config.dblClickRowToCheck = this.config.dblClickRowToCheck || null ;
		this.config.heightFix = this.config.heightFix || 80 ;
		this.config.lockedResize = this.config.lockedResize || false ;
		this.init();

}


TTable.TABLE ="<div id=\"{id}\" class=\"{tableCss}\">{header}{body}</div>";
TTable.SCROLL = 18;
TTable.prototype = {
		init: function(){
			this.tabId			=	"tab"+this.config.id;
			this.dataTabId		=	"datatab"+this.config.id;
			this.headerId		=	"header"+this.config.id;
			this.bodyId			=	"body"+this.config.id;
			this.footerId		=	"footer"+this.config.id;
			this.rowIndex		=	-1;
			this.valueChecked	=	new Array();
			this.vars = {};

			if(this.config.rows == null){
				this.rows = new Array();
			}else{
				this.rows = this.config.rows ;
			}
			this.myFields = new Hash();
			$(this.config.container).addClass('t-table');
		},
		draw : function(){
			//make html 
			var sb = TTable.TABLE.substitute({id:this.tabId,header:this.getHeader()}) ;
			
			sb = sb+"<div id='"+this.bodyId+"' class='yScroll'></div>";
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
				this.pager = new TTablePager({owner:this,container:$(this.footerId)});
			}

			if(this.config.unSelectText){
				$(this.config.container).addEvent('selectstart',function(e){
					return false;
				});
			}
		

			if(!this.config.lockedResize){
				
				var $this = this ;
				var timer = null ;
				var t = 0; 
				var lastSize = {x:0,y:0} ;
				var fun = function(){
					//$this.log('got resize ---');
					var now = new Date();   
					now = now.getTime(); 
					//$this.log('time='+ (now-t )+"&bodyWidth:"+document.body.clientWidth+'&offsetWidh='+document.body.offsetWidth );
					/*if ( (now - t <= 300)) {
						return ;
					}*/
					
					var height = document.documentElement.clientHeight ;
					var width = document.documentElement.clientWidth ;
					//$this.log('got resize :'+height);
					if(height == lastSize.y && width == lastSize.x ){
						//$this.log('sameSize:'+width );
						return ;
					}
					lastSize.x = width; 
					lastSize.y = height;

					t= now ;
					if(timer!=null){
						//$this.log('clear resize timer' );
						clearTimeout(timer);
						timer = null ;
					}

					timer = function(){
						//$this.log('working resize timer' );
						$this.fixSize();
						
					}.delay(100);
				};

				window.addEvent('resize',fun);
			}
			
		},
		showRuler : function(w){			
			if(this.ruler){
				
			}else{
				this.ruler = new Element('div.ruler');
				this.ruler.inject(document.body);
			}
			this.ruler.setStyle('width',w);
		},
		getSizes:function(){
			var fix_width = TTable.SCROLL + this.config.columns.length*1 ;
			var total_width = this.solidWidth + fix_width ;
			
			//$(this.config.container).addClass('h');
			//this.log( {"thisWidth:":this.solidWidth ,"fixWidth:":fix_width,"bodyWidth:":document.documentElement.clientWidth} );
			//this.log("---:"+fix_width);
			//this.log("getSizes:"+total_width);
			if(this.config.autoSize){
				total_width = document.documentElement.clientWidth ;
				//this.log(' document.clientWidth '+ total_width) ;
				//alert(total_width );
				//this.log( "BB>"+this.solidWidth + '>'+total_width);
				/*if(total_width < this.solidWidth + 100){
					total_width = this.solidWidth+100;
					//this.log(  "AA>>"+ total+width);
				}*/
				
				total_width -=25 ;	
				//$(this.logger).setStyle('width',total_width);

				//
				var w = total_width - this.solidWidth - fix_width - (this.config.columns.length  );
				if( PGlobals.minColumnSize && PGlobals.minColumnSize > w ) {
					
					//this.log('[min]fix width:'+fix_width +":tw="+total_width +" sWidth="+this.solidWidth);
					total_width += PGlobals.minColumnSize - w;
				}else{
					//this.log('[min]so sure not fix:'+w);
				}
			}else{
				//this.log ("not auto ?");
			}
			//this.showRuler(total_width);
			
			//if(Browser.ie6){
				
			//}
			//$(this.config.container).removeClass('h');
			//this.log('fix_width ---'+ fix_width +"& total="+total_width );
			return [ fix_width , total_width ] ;
			
		},
		fixSize:function(){
			
			this.vars.dimension = this.getSizes();
			var fix_width = this.vars.dimension[0];
			var total_width = this.vars.dimension[1];
			//this.log('fixSize:'+total_width);
			
			$(this.bodyId).setStyle('width',total_width);
			if(!this.config.noFooter){
				$(this.footerId).setStyle('width',total_width);
			}
			$(this.headerId).setStyle('width',total_width);

			this.fixAutoSizeColumn(this.vars.dimension);
			if(Browser.ie){
			}else{
				//$(this.dataTabId).setStyle('width',this.solidWidth);
				//$(this.headerId).setStyle('width',this.solidWidth+fix_width);
			}
			var h = 0 ;
			//alert(typeOf ( this.config.height));

			if( typeOf ( this.config.height) == 'function'){
				
				var ch = document.documentElement.clientHeight - this.config.heightFix;
				var result_height  = this.config.height();
								
				if(result_height == 0 ) {
					//alert ( this.config[0].getSize().x) ;
					//return ;
				}
				h = ch + result_height ;
				//this.log ( {cli:document.documentElement.clientHeight,ch:ch ,h:h }) ;
				//alert(ch +  "::result = "+ result_height + " reuslt="+h);
			}else{
				h = this.config.height - 20;
				//alert('stiil?='+this.config.height);
			}
			if(this.config.noFooter){	
				h+= 30;
			}
			//alert(h);
			//this.log('tw aa:'+total_width);
			if(h>0){
				$(this.bodyId).setStyle('height',h);
			}
			$(this.config.container).setStyle('width',total_width);
		},
		log : function(v){
			
			if(this.logger){
				
			}else{
				this.logger = new Element('div.logger');
				this.logger.inject(document.body);
			}
			this.logger.innerHTML = JSON.encode(v) + "<BR/>"+ this.logger.innerHTML ;
		},
		fixAutoSizeColumn : function(sss){

			if(this.config.autoSize){		
				//var sss = this.getSizes();
				var fix_width = sss[0];
				var total_width = sss[1];
				var w = total_width - this.solidWidth - fix_width - (this.config.columns.length  );
				//if( PGlobals.minColumnSize && PGlobals.minColumnSize > w ) {
				//	$$(".t-table-autosize").setStyle('width',PGlobals.minColumnSize);
				//this.log('go min width:'+PGlobals.minColumnSize +' left='+w);
				//}else{
					$$(".t-table-autosize").setStyle('width',w);
				//}
				//alert(total_width );
			}
			
		},
		addHeight:function(h){
			
			var hh = $(this.bodyId).getSize().y + h;
			if(hh>0){
				$(this.bodyId).setStyle('height', hh );
			}
			
		},
		getHeader:function(){
			var cols = this.config.columns ;
			var w = 0 ;
			/*var sb = "<colgroup>";
			
			for(var i = 0 ;i < cols.length ; i ++){
				var col  = cols[i];
				var tw = col.width ;
				sb += "<col style=\"width:"+tw+"px\"></col>";
				w += col.width ;
			}*/
			var sb = "<div class='t-table-header' id="+this.headerId+">";
			var w = 0 ;
			

			for(var i = 0 ;i < cols.length ; i ++){
				var col  = cols[i];
				var tw = '';
				if(col.width){
					tw = col.width ;
					w += col.width +4 ; // 4 = padding in column 
				}else{
					this.config.autoSize = true ;
				}
				
				var css ="";
				if(i==0){
					css+= " firstColumn";
				}
				if(col.headerCss){
					css += " "+col.headerCss;
				}

				var styleText = "";
				if(tw!=''){
					styleText = "width:"+tw+"px";
					css +=" t-table-column";
				}else{
					css += " t-table-column t-table-autosize";
				}

				if(col.align=='center'){
					css += " text-c";
				}else if (col.align=='right')
				{
					css += " text-r";
				}

				sb += "<div class='"+css+"' style='"+styleText+"'>"+col.label+"</div>";
			}
			sb+="<div class='c'></div>";
			this.solidWidth = w ;
			
			sb+="</div>";
			//alert(sb);
			return sb;
		},
		getRows:function(){
			var dt = this ;
			//也得拼凑column 一整套
			var sb = "" ;
			this.rows.each( function (e,rowIndex){
				var css = " t-table-row ";
				if( (rowIndex % 2) ==1){
					css += " oddRow";
				}else{
					css += " evenRow";
				}
				sb +="<div class='"+css+"' id='"+dt.tabId+rowIndex+"'>";
				dt.config.columns.each( function(c,index){
					var data = e[c.key];
					var tw = "" ;
					if(c.width){
						tw = c.width ;
						if ( index == dt.config.columns.length - 1)
						{
							//tw -= 2; 
						}
					}
					if(index==0){
						//tw -- ;
					}
					
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
						if(c.formatter== TTable.formatCheckbox && dt.hasChecked(data) ){
							e[c.key +".checked" ] = true ;
						}
						data = c.formatter( e,c, data);
					}
					
					var css = " t-table-data";
					if(rowIndex!=dt.rows.length-1){
						css += " normal";
					}else{
						css += " lastRow";
					}
					if(index == dt.config.columns.length -1){
						css += " last-col";
					}else{
						css += " normal-col";
					}
					var textCss = "";
					if (c.css)
					{
						textCss = c.css;
					}


					var styleText = "";
					if(tw!=''){
						styleText = "width:"+tw+"px";
						//css +=" ";
					}else{
						css += " t-table-autosize";
					}

					if(c.align=='center'){
						css += " text-c";
					}else if (c.align=='right')
					{
						css += " text-r";
					}



					sb += "<div style='"+styleText+"' class='"+css+' '+textCss+"'>"+data+"</div>";


				});
				sb+="<div class='c'></div></div>";
			});
			var table = TTable.TABLE.substitute({id:this.dataTabId,header:'',body:sb}) ;
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
						this.config.onReload(e,this);
					}
				}

			}.bind(this));
			this._clear();

			//创建一个层，用来显示loading
			if(!this.loading ){
				this.loading = new Element('DIV.loading');
				this.loading.inject(document.body);
			}

			
			PA.ui.centerAt(this.loading,this.bodyId);
			this.loading.setStyle('display','block');

		},
		_clear : function(){
			//clear listener
			$(this.bodyId).getElements(".dtData").removeEvents(['mouseover','mouseout','click','dblclick']);
			//界面切换成载入状态
			$(this.bodyId).innerHTML = "";
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
			this._clear();
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
			if(this.config.onRemoveRows){
				this.config.onRemoveRows(this);
			}
			$(this.bodyId).innerHTML =dataRows;
			var dt = this ;
			$(this.bodyId).getElements(".t-table-row").each( function (tr,index ){
				tr.addEvent('mouseover',function(e){
					
					tr.addClass('onRow');
					if(tr.hasClass('oddRow')){
						tr.removeClass('oddRow');
						tr.addClass('_oddRow');
					}
					if(tr.hasClass('evenRow')){
						tr.removeClass('evenRow');
						tr.addClass('_evenRow');
					}
				
					if(dt.config.hover){
						dt.config.hover(index,tr);
					}
				}).addEvent('mouseout',function(e){
					if(tr!=null){
						tr.removeClass('onRow');
						if(tr.hasClass('_oddRow')&&!tr.hasClass('selectedRow')){
							tr.removeClass('_oddRow');
							tr.addClass('oddRow');
						}
						if(tr.hasClass('_evenRow')&&!tr.hasClass('selectedRow')){
							tr.removeClass('_evenRow');
							tr.addClass('evenRow');
						}
					}
					if(dt.config.leave){
						dt.config.leave(index,tr);
					}
				}).addEvent('click',function(e){
					if(tr!=null){
						dt.selectedRow(tr,e);
					}
				}).addEvent('dblclick',function(e){
					var tr = dt.getTr(e.target);
					if(tr!=null){
						dt.intoRow(tr);
					}
				});
			});
			//data load;
			//this.log("data load");
			this.fixAutoSizeColumn(this.vars.dimension);
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
		selectedRow:function(tr,e){
			if(tr==null){
				this.unSelectRow();
				this.rowIndex = -1 ;
				if(this.config.selectRow) this.config.selectRow(this.rowIndex,null,e);
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
					this.config.selectRow(this.rowIndex,tr,e);
				}
			}
		},
		unSelectRow:function(){
			if(this.rowIndex!=-1){
				var arr = $(this.bodyId).getElements("div.t-table-row");
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
			if(t.hasClass('t-table-row')){
				return t; 
			}
			var obj = t.getParent("div.t-table-row");
			return obj ;
		},
		hideLoading:function(){
			if(this.loading ){
				this.loading.setStyle('display','none');
			}
		},
		fireSelect:function(){

			if(this.config.selectRow)
				this.config.selectRow(this.rowIndex,null,e);
			//this.selectedRow(null);
			
		},
		isScroll : function (el) {
             // test targets
             var elems = el ? [el] : [document.documentElement, document.body];
             var scrollX = false, scrollY = false;
             for (var i = 0; i < elems.length; i++) {
                 var o = elems[i];
                 // test horizontal
                 var sl = o.scrollLeft;
                 o.scrollLeft += (sl > 0) ? -1 : 1;
                 o.scrollLeft !== sl && (scrollX = scrollX || true);
                 o.scrollLeft = sl;
                 // test vertical
                 var st = o.scrollTop;
                 o.scrollTop += (st > 0) ? -1 : 1;
                 o.scrollTop !== st && (scrollY = scrollY || true);
                 o.scrollTop = st;
             }
             // ret
             return {
                 scrollX: scrollX,
                 scrollY: scrollY
             };
         }
		
}

TTable.formatCheckbox = function( oRecord, oColumn, oData) {
		var value = ' value="'+oData+'"' ;
		var checked = (oRecord[oColumn.key+".checked"])? ' checked' : '';
        return "<input type=\"checkbox\"" + value +
                " name=\"objId\" class=\"objId\" "+checked+" />";
};


TTable.formatBoolean = function( oRecord, oColumn, oData) {
		return  (oData)? _lang.yes: "<font color='#cccccc'>"+_lang.no+"</font>";
};
TTable.formatEnabled = function( oRecord, oColumn, oData) {
		if('\u0000'==oData){
			return _lang.disabled;
		}
		return  (oData )? _lang.enabled:_lang.disabled;
};
TTable.formatEmail = function( oRecord, oColumn, oData) {
		if(oData&& oData != "" ){
			return "<A href='mailto:"+oData+"'>"+oData+"</a>";
		}
		return "";
};
TTable.formatLinkJs = function(oRecord, oColumn, oData) {
		
		if(oData&& oData != "" ){
			var label = null;
			if(typeof(this.label)=='function'){
				label = this.label(oRecord);
			}else{
				label = this.label;
			}
			if(!label){
				label = oData ;
			}

			if(this.field){
				oData = oRecord[this.field];
			}

			var func = null;
			if(typeof(this.func) == 'function'){
				func = this.func(oRecord);
			}else{
				func = this.func ;
			}
			
			return "<A href='#' onclick='javascript:"+func+"(\""+oData+"\",event);'>"+label+"</a>";
		}
		return "";
};
TTable.formatDateTime = function(oRecord, oColumn, oData) {
		if(oData){
			var m = "yyyy-MM-dd hh:mm:ss";
			if(this.mask){
				m = this.mask ;
			}
			var d= new Date()  ;
			d.setTime(oData);
			//alert(d);
			return d.format( m ); 
		}
		return "";
};
TTable.formatDate = function(oRecord, oColumn, oData) {
		if(oData){
			var m = "yyyy-MM-dd";
			if(this.mask){
				m = this.mask ;
			}
			var d= new Date()  ;
			d.setTime(oData);
			//alert(d);
			return d.format( m ); 
		}
		return "";
};

TTable.formatDateTimeLite = function(oRecord, oColumn, oData) {
		if(oData){
			var m = "yyyy-MM-dd hh:mm";
			if(this.mask){
				m = this.mask ;
			}
			var d= new Date()  ;
			d.setTime(oData);
			//alert(d);
			return d.format( m ); 
		}
		return "";
};

TTable.checkAll =  "<input type='checkbox' class='checkAll' onclick='checkAllCss(this.checked,\".objId\")'  style='margin-top:6px;*margin-top:0px;'/> ";
TTable.checkAllExt = "<input type='checkbox' class='checkAll' onclick='checkAllCss(this.checked,\"#{id} .objId\")'/>";

Date.prototype.format = function(format) //author: meizz 
{ 
  var o = { 
    "M+" : this.getMonth()+1, //month 
    "d+" : this.getDate(),    //day 
    "h+" : this.getHours(),   //hour 
    "m+" : this.getMinutes(), //minute 
    "s+" : this.getSeconds(), //second 
    "q+" : Math.floor((this.getMonth()+3)/3),  //quarter 
    "S" : this.getMilliseconds() //millisecond 
  } 
  if(/(y+)/.test(format)) format=format.replace(RegExp.$1, 
    (this.getFullYear()+"").substr(4 - RegExp.$1.length)); 
  for(var k in o)if(new RegExp("("+ k +")").test(format)) 
    format = format.replace(RegExp.$1, 
      RegExp.$1.length==1 ? o[k] : 
        ("00"+ o[k]).substr((""+ o[k]).length)); 
  return format; 
} 
