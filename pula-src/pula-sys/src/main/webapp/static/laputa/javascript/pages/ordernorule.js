YAHOO.example.DynamicData = function() {
	
	
	var requestBuilder = function(oState, oSelf) { 
		var startIndex, results ;
		oState = oState || {pagination: null, sortedBy: null};
		startIndex = (oState.pagination) ? oState.pagination.recordOffset : 0;
		results = (oState.pagination) ? oState.pagination.rowsPerPage : null;
        var sort = "" ;
		var dir = "" ;
		/*var sort = oState.sortedBy.key;       
        var dir = oState.sortedBy.dir;       
        var startIndex = oState.pagination.recordOffset;       
        var results = oState.pagination.rowsPerPage;   */
        var reqStr = "sort=" + sort +    
                "&dir=" + dir +    
                "&startIndex=" + startIndex +    
                "&results=" + results +    
                pageVars.queryString  ;
   
           return reqStr;    
     };    


	var formatCheckbox = function(el, oRecord, oColumn, oData) {
        //var bChecked = oData;
        //bChecked = (bChecked) ? " checked=\"checked\"" : "";
		var value = ' value="'+oData+'"' ;
        el.innerHTML = "<input type=\"checkbox\"" + value +
                " class=\"" + YAHOO.widget.DataTable.CLASS_CHECKBOX + "\" />";
    };

    // Column definitions
    var myColumnDefs = [ // sortable:true enables sorting
		{key:"id",label:"<input type='checkbox' id='checkAll'/>",formatter:formatCheckbox},
        {key:"no", label:"编号"},
        {key:"name", label:"名称"},
        {key:"noLength", label:"计数宽度"},
        {key:"dateFormat", label:"日期格式"}
    ];
  // Custom parser   
     var booleanToString = function(b) {   
		if(b){ return "是" ;}else { return "否";}
     };   
        


    // DataSource instance
    var myDataSource = new YAHOO.util.DataSource("list?");
    myDataSource.responseType = YAHOO.util.DataSource.TYPE_JSON;
    myDataSource.responseSchema = {
        resultsList: "records",
        fields: [
            {key:"no"},
            {key:"id"},
            {key:"dateFormat"},
            {key:"name"},
            {key:"noLength"}
        ],
		metaFields: { 
			totalRecords:"totalRecords"
		}
    };
    
    // DataTable configuration
    var myConfigs = {
		selectionMode:"single",
		height:"280px",
        //initialRequest: "sort=id&dir=asc&startIndex=0&results=25", // Initial request for first page of data
        dynamicData: true, // Enables dynamic server-driven data
		generateRequest :requestBuilder,
        //sortedBy : {key:"id", dir:YAHOO.widget.DataTable.CLASS_ASC}, // Sets UI initial sort arrow
        paginator: new YAHOO.widget.Paginator({ 
					rowsPerPage:40,
					firstPageLinkLabel : "首页",
                    lastPageLinkLabel : "末页",
                    previousPageLinkLabel:"上页",
                    nextPageLinkLabel:"下页",
					template:"{FirstPageLink} {PreviousPageLink} {PageLinks} {NextPageLink} {LastPageLink}  <a class='yui-pg-last'>总数：{TotalRows}</a>"
		}) // Enables pagination 
    };
    
    // DataTable instance
    var myDataTable = new YAHOO.widget.ScrollingDataTable("dynamicdata", myColumnDefs, myDataSource, myConfigs);
    // Update totalRecords on the fly with value from server
    myDataTable.handleDataReturnPayload = function(oRequest, oResponse, oPayload) {
		//alert(oPayload+':'+ oResponse.meta.totalRecords);
        oPayload.totalRecords = oResponse.meta.totalRecords;
        return oPayload;
    }


	myDataTable.subscribe("buttonClickEvent", function(oArgs){   
            var oRecord = this.getRecord(oArgs.target);   
             alert(YAHOO.lang.dump(oRecord.getData()));   
     });   

      // Subscribe to events for row selection
        myDataTable.subscribe("rowMouseoverEvent", myDataTable.onEventHighlightRow);
        myDataTable.subscribe("rowMouseoutEvent", myDataTable.onEventUnhighlightRow);
        myDataTable.subscribe("rowClickEvent", onSelectRow);
    
	function onSelectRow(t){
		
		myDataTable.onEventSelectRow(t);
		var elTarget = t.target;		
		//拿到该行数据
		var elTargetRow = this.getTrEl(elTarget);
		if(elTargetRow) {
			var oTargetRecord = this.getRecord(elTargetRow);
			//alert(YAHOO.lang.dump(oTargetRecord));
			
			showData(oTargetRecord);
		}else{
			
			showData(null);
		}
		
	}

	function showData(d){
		if(d==null){
			$('orderNoRule.no').value = "" ;
			$('orderNoRule.name').value = "" ;
			$('orderNoRule.prefix').value = "" ;
			$('orderNoRule.suffix').value = "" ;
			$('orderNoRule.cacheRule').value = "0" ;
			$('orderNoRule.noLength').value = "4" ;
			$('orderNoRule.dateFormat').value = "yyMMdd" ;
			$('orderNoRule.id').value = "";
			$('recount').checked = true ;
			$('btnGenerate').disabled=  true;
			$('btnViewLog').disabled=  true;
			pageVars.action='_create';
			updateMode();
		}else{
			//find it 
			gf('get','id='+d._oData.id,function (e){
				if(e.error) { alert (e.message); return ;}
				$('orderNoRule.no').value = e.data.no ;
				$('orderNoRule.name').value = e.data.name ;
				$('orderNoRule.prefix').value = e.data.prefix ;
				$('orderNoRule.suffix').value = e.data.suffix ;
				$('orderNoRule.noLength').value = e.data.noLength ;
				$('orderNoRule.cacheRule').value = e.data.cacheRule ;
				$('orderNoRule.dateFormat').value = e.data.dateFormat ;
				$('orderNoRule.id').value =  e.data.id;		
				$('recount').checked = e.data.reCountByDay ;
				$('btnGenerate').disabled=  false;	
				$('btnViewLog').disabled=  false;		
				pageVars.action='_update';
				updateMode();
			});

		}
		$("orderNoRule.no").focus();
	}

	function updateMode(){
		
		if(pageVars.action=='_create'){
			$('pageMode').innerHTML = '新增';
		}else{
			$('pageMode').innerHTML = '修改';
		}
	}

	function reload(){
		//myDataTable.showTableMessage("Loading...");
		//myDataSource.sendRequest('');
		myDataTable.sortColumn(myDataTable.getColumn('id'),'asc');


	}
	myDataTable.subscribe("checkboxClickEvent", function(oArgs){   
		var elCheckbox = oArgs.target;   
		var oRecord = this.getRecord(elCheckbox);   
		oRecord.setData("check",elCheckbox.checked);   
	});   

	
	
    return {
        ds: myDataSource,
        dt: myDataTable,
		um: updateMode,
		sd: showData,
		reload : reload
    };

        
}();

$('addForm').addEvent('submit',function(e){
	$('addForm').action = pageVars.action;
	disableBtn(true);
	e.stop();
	sf($('addForm'),function (e ){
		disableBtn(false);
		if(e.error){
			alert(e.message);	
		}else{
			topSuccess();
			YAHOO.example.DynamicData.reload();
			pageVars.action='_create';
			YAHOO.example.DynamicData.sd(null);
			YAHOO.example.DynamicData.um();
		}		
	});
});

function disableBtn(b){
	$('submitBtn').disabled = b; 
	$('resetBtn').disabled = b; 
}
function _getQueryString(){
	var qs = "" ;
	var checked =false;
	$$("#dynamicdata .yui-dt-checkbox").each(function (ee){
			if(ee.checked){
				qs +="&objId="+ee.value;
				checked = true ;
			}

	});
	if(!checked){
		alert("请至少选择一项");
		return null ;
	}
	return qs ;
}
function remove(){
	var qs = _getQueryString();
	if(qs == null ) return ;
	//alert(qs);
	gf('remove',qs,function (e){
		if(e.error){
			alert(e.message);	
		}else{
			topSuccess();
			YAHOO.example.DynamicData.reload();
		}
	});

}

window.addEvent('domready',function(){
	$('checkAll').addEvent('click',function (e){
		$$("#dynamicdata .yui-dt-checkbox").each(function (ee){
			ee.checked = e.target.checked;
		});

	});

	

});



	function createPage(){
		pageVars.action = '_create';

		YAHOO.example.DynamicData.um();
		YAHOO.example.DynamicData.sd(null);		
	}

	function formatWith(format){
		$('orderNoRule.dateFormat').value = format;
	}

