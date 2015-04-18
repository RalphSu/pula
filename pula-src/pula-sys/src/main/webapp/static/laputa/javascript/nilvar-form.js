function clickAll(selector,b){
	//alert($$(selector).length);
	//alert($(form).elements.length);
	//alert($(form).elements[0].name);
	var str = selector;
	//alert(str);
	var arr = $$(str); 
	//alert(arr.length);
	for(var i = 0 ; i < arr.length ; i ++ ) {

		arr[i].checked = b ; 
	}

}

function checkSelected(selector){
	var str = selector  
	//alert(str);
	var arr = $$(str); 

	for(var i = 0 ; i < arr.length ; i ++ ) {

		if(arr[i].checked ) return true ;
	}


	alert("请至少选择一项");
	return false;
}

function submitForm(formName){
	//alert(formName);
	if(window.ie){
		$(formName).submit();
	}else{
		$(formName+'.submitBtn').click();
	}
}

function checkAll(b,obj){

	var id = document.all[obj];
	//alert(id);
	if(typeof(id)=="undefined"){
		return false;
	}

	//single 
	if(typeof(id.length)=="undefined"){
		id.checked = b ;
		//changeRow(id);
	//more
	}else{
		for(var i = 0 ; i < id.length ; i ++){
			id[i].checked = b ;
			//changeRow(id[i]);
		}
	}
}

function checkListSelected(selector){
	

	for(var i = 0 ; i < selector.options.length ; i ++ ) {

		if(selector.options[i].selected ) return true ;
	}


	return false;
}

function checkAllCss(b,obj){

	var id = $$(obj);
	//alert(id);
	if(typeof(id)=="undefined"){
		return false;
	}

	//single 
	if(typeof(id.length)=="undefined"){
		id.checked = b ;
		//changeRow(id);
	//more
	}else{
		for(var i = 0 ; i < id.length ; i ++){
			id[i].checked = b ;
			//changeRow(id[i]);
		}
	}
}

function checkSelectedCss(obj){
	var id = $$(obj);
	if(typeof(id)=="undefined"){
		return false;
	}

	if(typeof(id.length)=="undefined"){
		return id.checked ;
	//more
	}else{
		var b = false;
		for(var i = 0 ; i < id.length ; i ++){
			var chkd =  (id[i].checked);
			//alert(chkd);
			b = chkd || b ;
		}

		return b ;
	}

}

function clearSelect(obj){
	for(var i=0;i<obj.options.length;) 
	{ 
		obj.removeChild(obj.options[i]); 
	}

}

function addSelectOption(obj,ops){
	for(var i = 0 ;i < ops.length ;i ++){
		obj.options[obj.options.length] = new Option(ops[i].text,ops[i].value);

	}
}

function makeSelectSelected(obj,value){
	for(var i = 0 ; i< obj.options.length; i ++){
		//alert(obj.options[i].value);
		if(obj.options[i].value == value ) {
			obj.options[i].selected = true ;
			break;
		}
	}
}