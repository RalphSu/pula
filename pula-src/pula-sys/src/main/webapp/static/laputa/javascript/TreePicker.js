var  metal = {};



metal.TreePicker = function (cfg){
	this.init(cfg);
}

metal.TreePicker.prototype = {
	
	config : null ,
	state : null ,
	/*
     * configs 
	 * 
	 * url , selectElement, upClickElement,
	 * params
	 */
	init : function ( cfgs ) {

		this.config = cfgs; 
		this.config.selectElement.picker = this ;
		this.config.selectElement.addEvent('change',this.gotoItem);

		this.config.upClickElement.picker =this; 
		this.config.upClickElement.addEvent('click',this.moveUp);

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
		this.reload ( this.state.parentId);
	},

	reload : function (pid) {
		//lock
		this.config.selectElement.disabled = true ;
		this.config.upClickElement.disabled = true ;

		//ajax
		var myXHR = new XHRExt({method:'post'});
		myXHR.attach = this ;
		myXHR.addEvent( 'onSuccess', function (e,e2,attach ) { 
			//alert(e2);
			var obj = Json.evaluate(e);
				
			if(obj.error){
				alert(obj.message);
			}else{
				//loadNew
				attach.update(obj.children);
			}
		} );
		
		myXHR.send(this.config.url,this.config.params+pid);

	},
	update : function (vs){

		this.config.selectElement.options.length = 0 ;
		var lv = this.state.level;
		//root
		//alert(lv);
		if( lv == 0 ) {
			var item = new Option("(全部)","");	
			this.config.selectElement.options.add(item);
			item.selected = true ;
		}else{
			var item = new Option(this.state.parentName,this.state.parentNo);	
			this.config.selectElement.options.add(item);
			item.selected = true ;		
			if(this.config.listener){
				this.config.listener('change',this.state.parentNo);
			}
		}

		for(var i = 0 ; i < vs.length ;i ++){
			var obj = vs[i] ;
			var name = this.makeName(obj.name);
			var no = obj.no;
			var item = new Option(name,no);
			item.dataId = obj.id;
			item.leaf = obj.leaf ;

			this.config.selectElement.options.add(item);
			
			if(item.dataId == this.state.selectedId){
				item.selected = true ;

			}
		}

		this.config.selectElement.disabled = false;
		//show btns
		if(lv == 0) {
			this.config.upClickElement.style.display = "none";

		}else{
			this.config.upClickElement.style.display = "";
			this.config.upClickElement.disabled = false;
		}

		this.config.selectElement.focus();
	},

	makeName : function ( n ) {

		var parentName = this.state.parentName ;
		if(this.state.level !=0) {
			return parentName + '→' + n ;
		}else{
			return n ;
		}
	},
	popLast : function ( s ) {
		if(s.length == 0) return "";
		var r = s.getLast();
		s.remove(r);
		return r; 
	},
	pushLast : function(s ,s1) {
		//alert(s);
		if(s1==""){ return ;} 
		s.push(s1);
		//alert("s1=>"+s);
	},
	moveUp : function () { 
		var event = new Event(event);
		var btn = event.target ;
		var state = btn.picker.state;

		var sel = btn.picker.config.selectElement ;
		if(sel.selectedIndex!=0){
			//alert(sel.selectedIndex);
			sel.selectedIndex = 0 ;
			return ;
		}
		

		//up to 


		//popup id ;
		btn.picker.popLast( state.idPath );
		btn.picker.popLast( state.noPath );
		btn.picker.popLast( state.namePath );
		var id = btn.picker.popLast( state.idPath ) ;
		var no = btn.picker.popLast( state.noPath ) ;
		var name = btn.picker.popLast( state.namePath ) ;

		//alert(id);

		//应用到parent

		btn.picker.applyParent(id,no,name);

		//层级返回
		state.level -- ;

		btn.picker.reload ( id ) ;
		btn.picker.test();

	},

	gotoItem : function (event) {
		//load child
		//get Selected
		var event = new Event(event);
		var sel = event.target ;
		var opt = sel.options[sel.selectedIndex];
		//alert(opt.dataId);

		//make state


		if(opt.dataId&& !isEmpty( opt.dataId) && opt.leaf != "true"){
			var state = sel.picker.state;
			sel.picker.applyParent(opt.dataId,opt.value,opt.text);

			//alert( state.idPath);
			
			//level 加深
			state.level ++ ;
			sel.picker.reload ( opt.dataId ) ;
		}

		sel.picker.test();

	},
	applyParent : function(id,no,name){
		this.state.parentName = name;
		this.state.parentId = id ;
		this.state.parentNo = no ;
		
		this.pushLast( this.state.idPath , this.state.parentId) ;
		this.pushLast( this.state.noPath , this.state.parentNo) ;
		this.pushLast( this.state.namePath , this.state.parentName) ;
	},
	onsubmit: function (){
		var sel = this.config.selectElement ;
		var id = "";
		if(sel.selectedIndex>0){
			var opt = sel.options[sel.selectedIndex];
			if(opt.dataId){
				id = opt.dataId;
			}
		}

		this.state.selectedId = id;

		return Json.toString(this.state);

	},
	test : function () {
		if(!$defined("parentName")){
			alert("not defined parentName");
		}	
		$("parentName").value = this.state.namePath + ">" + this.state.level ;

	}
	
}