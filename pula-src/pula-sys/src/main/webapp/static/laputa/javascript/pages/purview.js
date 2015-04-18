	function searchMe(id){
		$('condition.moduleId').value=id;
		//submitForm('searchForm');

		var arr = $$(".searchMe");
		arr.each(function(e){
			if(e.get('id')=="searchMe"+id){
				e.addClass('selected');
			}else{
				e.removeClass('selected');
			}
		});

		loadJson();
		
	}
	var reload = false;
	function needRefresh(){
		reload = true ;
	}
	function onCloseDialog(){
		if(reload){
			reloadTree();
			reload = false;
		}

	}
	function reloadTree(){
		var arr = $$("A.selected");
		if(arr.length == 0 ) return ;
		var id = arr[0].get('id');
		searchMe(id.substring(8));
	}

	function clickType(obj){
		$('condition.moduleId').value = "" ;
		submitForm('searchForm');
		//loadJson();
	}
	function hideLoading(){
		$('loading').setStyle('visibility','hidden');
	}

	function loadJson(){
		var aj = new Request.JSON({
						url:'getJson',
						onSuccess:function (e){
							//alert('got:'+e + 'ho'+e1);
							if(e.error){
								alert(e.message);
								return ;
							}
							var s = null;
							if(typeof(e)=='object'){
							var s = e;
							}else{
								s = JSON.decode(e);
							}
							tree.removeChildren(tree.getRoot());
							
							buildTreeNodeFromArray(s,tree.getRoot());
							tree.draw();
							tree.expandAll();
						}
		}).send($('searchForm').toQueryString()+"&_json=1");		
	}

	function assignTo(){
		var lid = $F("loginIdToAssign");
		//alert(lid);
		if(isEmpty(lid)){
			alert(lang.inputUserNo);
			$("loginIdToAssign").focus();
			return ;
		}
		var aj = new Request.JSON({
						url:'assign',
						onSuccess:function (e){
							if(e.error){
								alert(e.message);
							}else{
								top.showSuccess();
							}
						}
					}).send('type='+pageVars.appFieldNo+'&loginId='+lid+'&_json=1');
	}
	window.addEvent('domready',function(){
		$("goAssignForm").addEvent('submit',function(e){
			e.stop();
			assignTo();
		});
	});

/////////////////////////// trees and menus
var tree;
var nodesArray = [];
var oContextMenu;
var oTarget = null ;


    function treeInit() {
        tree = new YAHOO.widget.TreeView("treeDiv1");
		buildTreeNodeFromArray(treeData,tree.getRoot());


       // Expand and collapse happen prior to the actual expand/collapse,
       // and can be used to cancel the operation
       tree.subscribe("expand", function(node) {
              //alert(node.index + " was expanded");
              // return false; // return false to cancel the expand
           });
       tree.subscribe("collapse", function(node) {
              //alert(node.index + " was collapsed");
			  return false;
           });
       // Trees with TextNodes will fire an event for when the label is clicked:

       tree.draw();tree.expandAll();

		makeMenu();

		
		

    }

	function buildTreeNodeFromArray(arr,parentNode){
		var arrLen = arr.length ; 
		for(var i  = 0 ; i < arrLen ; i++){
			var jsonNode = arr[i];
			var tmpNode = new YAHOO.widget.TextNode(jsonNode, parentNode, false);
			
			nodesArray.push(tmpNode);

			if(jsonNode.children1 ) {
				buildTreeNodeFromArray(jsonNode.children1,tmpNode);
			}
		}
	}
    
    
	function makeMenu(){
		/*var na = [] ;
		var arrLen = nodesArray.length ; 
		//alert(arrLen);
		var type,oid ,last,first;
		for(var i =0 ; i < arrLen ;i ++){
			type = (nodesArray[i].data.type);
			oid = nodesArray[i].data.id;
			last = nodesArray[i].data.last; 
			first = nodesArray[i].data.first;
			var ele = nodesArray[i].getLabelEl();

			ele.dataId = oid;
			//alert(nodesArray[i].text);
			ele.dataType = type	 ;
			ele.last = last ;
			ele.first = first ;


			na.push(ele);
			
			YAHOO.util.Event.addListener(ele, "click", onContextMenuTargetClick);
		}
		*/

		//alert(nodesArray.length);
		var aMenuItems = [ { text: lang.miNewSubPurview } ,
							{text: lang.miNewShortcut },
							{text: lang.miListShortcut },
							{text: lang.miEdit },
							{text: lang.miRemove },
							{text: lang.miMoveUp},
							{text: lang.miMoveDown} ,
							{text: lang.miExport} ];
		oContextMenu = new YAHOO.widget.ContextMenu("mycontextmenu", 
														{ itemdata: aMenuItems} 
													   );


		oContextMenu.renderEvent.subscribe(onContextMenuRender, oContextMenu, true);

		oContextMenu.render("menuDiv");

		oContextMenu.showEvent.subscribe(onContextMenuShow,this);

		tree.subscribe( 'clickEvent',clickTree );
		//tree.subscribe("clickEvent",tree.onEventToggleHighlight);

	}

	function clickTree( oArgs   ) {
		//alert(oArgs.node);
		var p_oEvent = oArgs.event;
		oTarget = oArgs.node;
		var nX = YAHOO.util.Event.getPageX(p_oEvent);
		var nY = YAHOO.util.Event.getPageY(p_oEvent);
		oContextMenu.cfg.applyConfig( { x:nX, y:nY, visible:true } );
		oContextMenu.cfg.fireQueue();
		YAHOO.util.Event.stopEvent(p_oEvent);
	}
	
		function onContextMenuShow() {
				

				disabledMenu(GetListItemFromEventTarget(oContextMenu.contextEventTarget));
                
		}



		function disabledMenu(vo){
			//alert(vo);
				//alert(vo);
				vo = vo.data;
				if(vo.last||vo.data.type=='m'){
					oContextMenu.getItem(MENUITEM_DOWN).cfg.setProperty("disabled", true);
					//alert(oContextMenu.getItem(3));
				}else{
					oContextMenu.getItem(MENUITEM_DOWN).cfg.setProperty("disabled", false);
				}
				
				if(vo.first||vo.data.type=='m'){
					oContextMenu.getItem(MENUITEM_UP).cfg.setProperty("disabled", true);
				}else{
					oContextMenu.getItem(MENUITEM_UP).cfg.setProperty("disabled", false);

				}

				var p = (vo.data.type=='p');

				oContextMenu.getItem(MENUITEM_EDIT).cfg.setProperty("disabled", !p);
				oContextMenu.getItem(MENUITEM_DELETE).cfg.setProperty("disabled", !p);
				oContextMenu.getItem(MENUITEM_ADDSHORTCUT).cfg.setProperty("disabled", !p);
				oContextMenu.getItem(MENUITEM_LIST_SC).cfg.setProperty("disabled", !p);

		}

		function onContextMenuRender(p_sType, p_aArgs, p_oMenu) {
		
						//  Add a "click" event handler to the ewe context menu
			
			this.clickEvent.subscribe(onContextMenuClick, this, true);
			
		}

		function GetListItemFromEventTarget(p_oNode) {
			if(p_oNode==null){
				p_oNode= oTarget;
			}
			return p_oNode ;

		}

		
		function addShortcut(data){
			var param = 'purviewId=';
			

			var loc = 'saveShortcut!input?'+param+data.dataId+'&backURL=${url?url}';
			goURL(loc);

			//showFunction(loc);

		}

		function listSc(data){
			var param = 'purviewId=';

			var loc = 'listShortcut?'+param+data.dataId+'&mark='+getDateStr();
			//goURL(loc);

			showFunction(loc,'<@b.text key="platform.purview.listShortcut"/>');

		}
		
		


		

		


		

		function onContextMenuClick(p_sType, p_aArgs, p_oMenu){

                    var oItem = p_aArgs[1];
					
                    if(oItem) {
						
                        var oLI = GetListItemFromEventTarget(this.contextEventTarget);
        
                        switch(oItem.index) {
                        
                            case MENUITEM_ADDCHILD:     // add child
								
								addChild(oLI);
                                //alert(oLI.dataId);
                            
                            break;
        
                            case MENUITEM_ADDSHORTCUT :    // add child
								
								addShortcut(oLI);
                                //alert(oLI.dataId);
                            
                            break;
							case MENUITEM_LIST_SC :    // add child
								
								listSc(oLI);
                                //alert(oLI.dataId);
                            
                            break;        
                            case MENUITEM_EDIT:     // edit
        
                                editPurview(oLI);
        
                            break;
                            
        
                            case MENUITEM_DELETE:     // delete
        
                                removePurview(oLI);
        
                            break;
							
							case MENUITEM_UP: // move up 
								moveUp(oLI);
							break ;

							case MENUITEM_DOWN : // move down 
								moveDown(oLI);
							break ;

							case MENUITEM_EXPORT : 

								exportToXML(oLI);
							break;
                        
                        }
                    
                    }


		}
	if(pageVars.moduleExists) YAHOO.util.Event.addListener(window, "load", treeInit);

	
	function mouseOver(e){

		//obj = event.srcElement ? event.srcElement : event.target ;
//
		//obj.style.backgroundColor = '#F0F0F0';
	}
                    
	
	function mouseOut(e){

		//obj = event.srcElement ? event.srcElement : event.target ;

		//obj.style.backgroundColor = '#00000';
	}




/************************ ext pages *****************************/
	var tabFuncs = null ;
	window.addEvent('domready',function(){
		
		if(pageVars.moduleExists) 
			var tabView = new YAHOO.widget.TabView('tabs1');  
			 tabView.on('activeTabChange', function(ev) {   
				 //var v = JSON.decode(ev);
				 //alert(v);
				 //alert(YAHOO.lang.dump);
				//alert((ev.newValue.get('labelEl').id));
				searchMe(ev.newValue.get('labelEl').id);
				 
				 $('tabPurviews').removeClass('yui-hidden');
			 });   


		if(pageVars.purviewIdExists){
			var param = 'purviewId=${purviewId}';
			var loc = 'listShortcut?'+param+'&mark='+getDateStr();

			showFunction(loc,'<@b.text key="platform.purview.listShortcut"/>');			
		}

		hideLoading();
	});


	function showFunction(url,text){
		
        var updater = tabFuncs.getUpdateManager();
		tabFuncs.setText(text);
        updater.setDefaultUrl(url);
		//alert(url);
        tabFuncs.on('activate', updater.refresh, updater, true);
		tabFuncs.enable();
		tabFuncs.activate();
	}
	

	function goEditShortcut(id){

		goURL("editShortcut!input?id="+id+"&backURL=${url?url}");
	}

	function goAddShortcut(id){
		var param = 'purviewId=';
		

		var loc = 'saveShortcut!input?'+param+id+'&backURL=${url?url}';
		goURL(loc);

		//showFunction(loc);

	}

	function remove(){
		var obj = document.all["objId"];
		//alert(obj.length);
		if(!checkSelected(obj)){
			//alert('none');
			return 
		}

		if(!confirm(lang.removeConfirm)){
			return 
		}

		$('batchForm').action="removeShortcut";
		
		$('removeBackURL').value = "${url}";
		$('batchForm').submit() ;


	}
//callbacks

		function updatePurviewName(v){
			//alert(oTarget);
			oTarget.getLabelEl().innerHTML = v ;
		}
		function removeCurrentNode(){
			//var node = (tree.getNodeByElement(currentNode));
			tree.removeNode(oTarget,true);
			oTarget = null ;
			//tree.draw();
			//还会引起周边的last 或first 变化

		}
		