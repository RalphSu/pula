function _goPageJs(p,n){
			
		if(!_isNInt(p)){
			alert("请正确填写页码");
			return ;
		}
		
		if(typeof(n)!='undefined'){
			if(eval(p)>n){
				alert("请正确填写页码，最大页码为:"+n);
				return ;
			}
		}
		var loc = window.location.href ;
		loc = deleteParam(deleteSharp(loc),"p");
		loc = addParam(loc,"p",p);
		window.location.href=(loc);
		return true ;
	}

function _isInteger(str){
	var integer = parseInt(str); 
	if (isNaN(integer)) 
	{ 

		return false; 
	} 
	return true ;
}

function _isNInt(theStr){
	return ((_isInteger(theStr))&&(eval(theStr)>=0));
}

function deleteParam(u,p){
	var n = u.indexOf("?"+p);
	var tmp = "";
	if (n<0)
	{
		n = u.indexOf("&"+p);
	}
	if(n<0){
		return u ;
	}
	
	tmp = u.substring(0,n);
	len = p.length + 2; 
	//alert('1:'+u);
	u = u.substring(n+len+1);
	n = u.indexOf("&");
	if(n<0){
		n = u.length;
	}
	//alert(n+":"+u);
	tmp = tmp + u.substring(n);
	//alert(tmp);
	return tmp ;
}

function addParam(u,p,v){
	var n = u.lastIndexOf("?");
	//alert(n + ";"+u.length);
	
	while(n == ( u.length-1 ) )
	{
		u = u.substring(0,n);
		n = u.lastIndexOf("?");
		noQ = true ;
	}
	var noQ = u.lastIndexOf("?")==-1;
	if(noQ ){
		u +="?";
	}else{
		u +="&";
	}
	u += p + "="+ encodeURIComponent(v);
	
	return u ;
}
function deleteSharp(u){
	var n = u.indexOf("#");
	if(n<0) return u ;

	return u.substring(0,n);
}

function fireChangePage(p,n){
	

	_goPageJs(p,n);
}

function firePagerKeydown(e){
	var ev = new Event(e);
	//alert(ev.key);
	if(ev.key =='enter'){
		//alert(ev);
		var maxPage = (ev.target.maxPage);
		var v = (ev.target.value);
		_goPageJs(v,maxPage);
		ev.stop();
	}else{
		//alert(e.keyCode);
	}

}

window.addEvent('domready',function (){
	var es = $$(".pagerEdt");
	var pagerGoImg= $$(".pagerGoImg");

	//alert(es.length);
	for (var i = 0;  i < es.length ; i ++ )
	{
		//alert(es[i]);
		es[i].addEvent('keypress',firePagerKeydown);

		if(pagerGoImg.length > i ){
			pagerGoImg[i].setAttribute("index",i);
			pagerGoImg[i].addEvent('click',clickPageGo);
		}
	}


});

function clickPageGo(e){
	var ev = new Event(e);
	var ind = (ev.target.index);
	var es = $$(".pagerEdt");
	var pno = es[ind].value ; 
	var maxP = es[ind].maxPage;
	_goPageJs(pno,maxP);
}

