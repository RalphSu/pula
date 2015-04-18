
TreePicker = function (cfg){
	this.init(cfg);
}

TreePicker.SELECT = '<select id="select{field}" name="{field}"></select>';
TreePicker.BUTTON = '<input type="button" value="{buttonText}" id="button{field}" class="h"/>';
TreePicker.prototype = {
	
	config : null ,
	state : null ,
	selectId : null,
	buttonId : null ,
	dataHash : null ,
	/*
     * configs 
	 * 
	 * url , selectElement, upClickElement,
	 * params
	 */
	init : function ( cfgs ) {

		this.config = cfgs; 
		var sb = TreePicker.SELECT.substitute(this.config);
		sb += TreePicker.BUTTON.substitute(this.config);
		$(this.config.container).innerHTML = sb;
		this.selectId= 'select'+this.config.field;
		$(this.selectId).addEvent('change',this.gotoItem.bind(this));
		this.buttonId = 'button'+this.config.field;
		$(this.buttonId).addEvent('click',this.moveUp.bind(this));
		this.dataHash = new Hash();

	},
	/*
     * state
	 *
	 * idPath ,noPath,namePath
	 * parentName,parentId
	 */
	initState: function ( state) {

		this.state = state ;

		if(!this.state.level) {
			this.state.level = 0 ;
		}

		//alert(this.state.selectedId);
		


		this.restoreState();

	},

	restoreState : function () {
		//put the parentid to datahash
		this.dataHash.set(this.state.parentNo,{"id":this.state.parentId,"leaf":false});
		this.reload ( this.state.parentId);
	},

	reload : function (pid) {
		//lock
		$(this.selectId).disabled = true ;
		$(this.buttonId).disabled = true ;

		//ajax
		new Request.JSON({url: this.config.url,method:'post',
			onSuccess : function (obj){
				if(obj.error){
					alert(obj.message);
				}else{
					//loadNew
					this.update(obj.list);
				}
			}.bind(this)
		
		}).send(this.config.params+pid);

	},
	update : function (vs){

		$(this.selectId).options.length = 0 ;
		var lv = this.state.level;
		//root
		//alert(lv);
		var firstItem = null ;
		if( lv == 0 ) {
			firstItem = new Option(this.config.defaultOptionText,"");	
			$(this.selectId).options.add(firstItem);
			firstItem.selected = true ;
		}else{
			//var leaf = this.dataHash.get( this.state.parentNo ).leaf ;
			var text = this.state.parentName;
			//if(!leaf ){
				//text = this.state.parentName+"("+vs.length+")";
			//}
			firstItem = new Option(text,this.state.parentNo);	
			$(this.selectId).options.add(firstItem);
			firstItem.selected = true ;		
			if(this.config.listener){
				this.config.listener('change',this.state.parentNo);
			}
		}


		for(var i = 0 ; i < vs.length ;i ++){
			var obj = vs[i] ;
			var name = this.makeName(obj.name);
			var no = obj.no;
			var item = new Option(name,no);
			
			this.dataHash.set(obj.no,obj);

			$(this.selectId).options.add(item);			
			if(obj.id == this.state.selectedId){
				item.selected = true ;
			}
		}

		$(this.selectId).disabled = false;
		//show btns
		if(lv == 0) {
			$(this.buttonId).addClass("h");
		}else{
			$(this.buttonId).removeClass("h");
			$(this.buttonId).disabled = false;
		}

		$(this.selectId).focus();
	},

	makeName : function ( n ) {

		var parentName = this.state.parentName ;
		if(this.state.level !=0) {
			return parentName + ' >> ' + n ;
		}else{
			return n ;
		}
	},
	popLast : function ( s ) {
		if(s.length == 0) return "";
		var r = s.getLast();
		s.erase(r);
		return r; 
	},
	pushLast : function(s ,s1) {
		//alert(s);
		if(s1==""){ return ;} 
		s.push(s1);
		//alert("s1=>"+s);
	},
	moveUp : function (event) { 
		//var event = new Event(event);
		var btn = event.target ;
		var state = this.state;

		var sel = $(this.selectId) ;
		if(sel.selectedIndex!=0){
			//alert(sel.selectedIndex);
			sel.selectedIndex = 0 ;
			return ;
		}	
		this.state.selectedId = "";
		//up to 
		//popup id ;
		this.popLast( state.idPath );
		this.popLast( state.noPath );
		this.popLast( state.namePath );
		var id = this.popLast( state.idPath ) ;
		var no = this.popLast( state.noPath ) ;
		var name = this.popLast( state.namePath ) ;
		if(id==""){
			id = this.config.rootId ;
		}
		//应用到parent

		this.applyParent(id,no,name);


		//层级返回
		state.level -- ;

		this.reload ( id ) ;
		this.test();

	},

	gotoItem : function (event) {
		//load child
		//get Selected
		//var event = new Event(event);
		var sel = event.target ;
		var opt = sel.options[sel.selectedIndex];
		//alert(opt.dataId);

		//make state
		var obj  = this.dataHash.get(opt.value);
		var dataId = obj.id;
		var leaf = obj.leaf;

		this.state.selectedId = dataId ;

		if(!isEmpty( dataId) && !leaf ){
			var state = this.state;
			this.applyParent(dataId,opt.value,opt.text);

			//alert( state.idPath);
			
			//level 加深
			state.level ++ ;
			this.reload ( dataId ) ;
		}

		this.test();

	},
	applyParent : function(id,no,name){
		this.state.parentName = name;
		this.state.parentId = id ;
		this.state.parentNo = no ;
		
		this.pushLast( this.state.idPath , this.state.parentId) ;
		this.pushLast( this.state.noPath , this.state.parentNo) ;
		this.pushLast( this.state.namePath , this.state.parentName) ;

		this.dataHash.set(this.state.parentNo,{"id":this.state.parentId,"leaf":false});
	},
	onsubmit: function (){
		var sel = $(this.selectId) ;
		var id = "";
		if(sel.selectedIndex>0){
			var opt = sel.options[sel.selectedIndex];
			if(opt.dataId){
				id = opt.dataId;
			}
		}

		this.state.selectedId = id;

		return JSON.encode(this.state);

	},
	test : function () {
		if(!$defined("parentName")){
			alert("not defined parentName");
		}	
		$("parentName").value = this.state.namePath + ">" + this.state.level ;

	},
	getId : function (){
		var sel = $(this.selectId) ;
		var opt = sel.options[sel.selectedIndex];
		var obj  = this.dataHash.get(opt.value);
		return obj.id;
	}
	
}