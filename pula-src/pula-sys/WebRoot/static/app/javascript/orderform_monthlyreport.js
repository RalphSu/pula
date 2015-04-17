var PPage = new Class({
		Extends: PBasePage,
		initialize: function (configs) {			
			this.initVars(configs);
			this.init();
		},
	
		init : function(){
			this.initBase();
			this.initToolBar({condition:true});
			
			this.showCondition();
			

			var $this = this ;



			if($('branchId')){
				$('branchId').addEvent('change',function(){
					$('searchForm').submit();
				});
			}

			$('year').addEvent('keydown',function(e){
				var ev = new Event(e);
				if(ev.key=='enter'){
					if(check()){
						$('searchForm').submit();
					}
				}
			});
		},//init ends
		goYear: function(y){
			$('year').value = y ;
			$('searchForm').submit();
		},

		showData:function (d){

			

			
		},updateMode:function(){}
		
		
});

var pes = null ;
window.addEvent('domready',function(){
	pes = new PPage({"id":"pes","pageMode":"pageMode","searchForm":"searchForm","addForm":"addForm"});
});

var reloadData = function (){
	pes.reload();
}
