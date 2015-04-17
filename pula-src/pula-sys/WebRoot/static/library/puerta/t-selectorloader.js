TSelectLoader = function(config){
	
	this.config = config ;
	this.init();
}

TSelectLoader.prototype ={

	init:function(){
		var leaders = $(this.config.leader) ;
		this.config.multiLeader = false;
		this.config.allowReload = this.config.allowReload || this._allowReload ;
		if(leaders.length >1){
			this.config.multiLeader = true ;
		}
		this.vars = {} ;
		this.vars.dataList ={};
		this.vars.leaderId = {};
		this.vars.dataSelectedId = {};
		var $this = this; 
		leaders.addEvent('change',function(){
			if($this.config.allowReload()){

				$this.loadData( this,$this.loadDataToSelect.bind([$this,this]) );
			}
		});

		if(this.config.fireChangeAfterLoad){
			leaders.fireEvent('change');
		}
		
		if( typeof (this.config.defaultValue)== 'function' ){
		
		}else if (this.config.defaultValue){
			this.vars.leaderId[ this.config.defaultValue[0] ] =  this.config.defaultValue[1] ;
		}

		leaders = null ;
	},
	_allowReload :function(){
		return true ;
	},
	loadData: function(sender,_callback){
			var $this = this ;
			var wid= sender.value;
			var sid = sender.get('id');

			
			//var shouldReload =false;
			if( !this.vars.leaderId[sid]  || ( this.vars.leaderId[sid] && this.vars.leaderId[sid] != wid ) ){
				//shouldReload = true ;
			}else{
				return ;
			}

			if(isEmpty(wid)){
				wid= "0";
			}

			//ajax
			PA.ajax.gf( $this.config.uri,
				 $this.config.params ( wid ) ,
				function( e ) {
					if(e.error){ alert (e.message) ; return ;} 
					//load
					$this.vars.dataList[ sid ]  = e.data ;
					_callback();
				}
			);


	},
	loadDataToSelect : function( sel ,sender) {
		var $this = this ;
		if(!sender){
			sender =$this[1].get('id');
			$this = $this[0];
		}
		
		if( typeof( $this.vars.dataList[sender]) == "undefined"){
			return ;
		}

		var _loadDataToSelect = function ( obj ) {
			PA.utils.clearSelect( obj ) ;

			var opt = new Element('option',{   
				 text: _lang.selectItem,
				 value: ''
			});
			obj.adopt(opt);

			var opt_selected = NaN;
			if(typeof ($this.config.defaultValue)== 'function'){
				opt_selected = $this.config.defaultValue();				
			}
			$this.vars.dataList[sender].each ( function(el){
					var opt = new Element('option',{   
						 text: el.name,
						 value: el.id
					});

					if( $this.vars.leaderId[sender] && $this.vars.leaderId[sender]  == el.id){
						opt.set('selected',true);
					}

					if(opt_selected == el.id ){
						opt.set('selected',true);
					}
					obj.adopt(opt);
			});
		}
		if(sel ){
			//load from remote ?
			//local data ;
			_loadDataToSelect( $(sel) );
		}else{
			$$("._"+sender).each ( function( ss ) {
				_loadDataToSelect( $(ss) );
			});
		}

	}
	
}