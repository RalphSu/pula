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

			
			var ms = new TMoonSelector({
				container:'ms',
				year:pageVars.year,
				month:pageVars.month,
				directForward:false,
				count:7,applyYear:'hidYear',
					applyMonth:'hidMonth',form:'searchForm'
			});


			if($('branchId')){
				$('branchId').addEvent('change',function(){
					$('searchForm').submit();
				});
			}
		},//init ends
		goMonth: function(y,m){
			$('hidYear').value = y ;
			$('hidMonth').value = m ;
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
