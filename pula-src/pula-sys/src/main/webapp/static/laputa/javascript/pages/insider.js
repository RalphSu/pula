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
	var formatEnable = function(el, oRecord, oColumn, oData) {
		var imgPath = "" ;
        if(oData){
	        imgPath = pageVars.base + '/static/laputa/images/icons/unlocked.gif';
		}else{
	        imgPath = pageVars.base + '/static/laputa/images/icons/lockdis.gif';
		}
		el.innerHTML = '<img src="'+imgPath+'"/>';
    };
    // Column definitions
    var myColumnDefs = [ // sortable:true enables sorting
		{key:"id",label:"<input type='checkbox' id='checkAll'/>",formatter:formatCheckbox},
        {key:"loginId", label:"编号"},
        {key:"name", label:"名称"},
        {key:"enabled", label:"状态",formatter:formatEnable},
        {key:"comments", label:"备注"}
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
            {key:"loginId"},
            {key:"id"},
            {key:"name"},
            {key:"enabled"},
            {key:"comments"}
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
			pageVars.action='_create';updateMode();
		}
		
	}

	function showData(d){
		if(d==null){
			$('insider.loginId').value = "" ;
			$('insider.name').value = "" ;
			$('insider.password').value = "" ;
			$('insider.comments').value = "" ;
			$('chPwd').addClass('h');
			$('insider.id').value ="";
			$('btnPurview').disabled=  true;
			$('btnViewLog').disabled=  true;
		}else{
			//find it 
			gf('get','id='+d._oData.id,function (e){
				if(e.error) { alert (e.message); return ;}
				$('insider.loginId').value = e.data.loginId ;
				$('insider.name').value = e.data.name ;
				$('insider.password').value = "";
				$('chPwd').removeClass('h');
				$('insider.comments').value = e.data.comments;
				$('insider.id').value = e.data.id ;
				$('btnPurview').disabled=  false;	
				$('btnViewLog').disabled=  false;
				$('cb').checked =false;
				pageVars.action = '_update';
				
				updateMode();
			});

		}
		$("insider.loginId").focus();
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
	gf('remove',qs,function (e){
		if(e.error){
			alert(e.message);	
		}else{
			topSuccess();
			YAHOO.example.DynamicData.reload();
		}
	});
}
function enable(b){
	var qs = _getQueryString();
	if(qs == null ) return ;
	qs += "&enable="+b;
	gf('enable',qs,function (e){
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

	$("btnPurview").addEvent('click',function (e){
		top.openDialog({
			title: '权限设置' ,
			url: "../insider/purview?id="+$F("insider.id"),
			width: 580,
			height: 400,
			caller: window,
			asset:dialogAsset
		});			
	});

	$('btnViewLog').addEvent('click',function (e){
		//$('btnViewLog').disabled = true ;
		
		top.openDialog({
			title: '查看日志' ,
			url: "../insiderlog/view?id="+$F("insider.id"),
			width: 780,
			height: 300,
			caller: window,
			asset:dialogAsset
		});	

	});

	YAHOO.example.DynamicData.um();
	YAHOO.example.DynamicData.sd();
});



	function createPage(){
		pageVars.action = '_create';

		YAHOO.example.DynamicData.um();
		YAHOO.example.DynamicData.sd(null);		
	}

	function formatWith(format){
		$('orderNoRule.dateFormat').value = format;
	}

