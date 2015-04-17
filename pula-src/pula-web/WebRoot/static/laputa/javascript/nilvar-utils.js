
function goURL(url){
	url=trimMark(url);
	if(url.indexOf('?')>=0){
		url += "&mark="+getDateStr() ;
	}else{
		url += "?mark="+getDateStr() ;
	}
	location.href = url;
}

function trimMark(url){
	var n = url.indexOf('?mark=');
	//has mark
	if(n>=0){
		return url.substring(0,n);
	}else{
		n = url.indexOf('&mark=');
		if(n>=0){
			return url.substring(0,n);

		}
	}

	return url;
}

function appendMark(url){
	
	if(url.indexOf('?')>=0){
		url += "&mark="+getDateStr() ;
	}else{
		url += "?mark="+getDateStr() ;
	}	
	

	return url ;

}

function getDateStr(){
	var cDate = new Date();
	var DateStr = ""+cDate.getYear()+""+cDate.getMonth()+""+cDate.getDay()+cDate.getHours()+cDate.getMinutes()+cDate.getSeconds();
	return DateStr ;
}

function getDateStr1(){
	var cDate = new Date();
	var DateStr = ""+cDate.getYear()+""+cDate.getMonth()+""+cDate.getDay()+cDate.getHours()+cDate.getMinutes()+cDate.getSeconds()+cDate.getMilliseconds();
	return DateStr ;
}

function goURL2(url,obj){
	if(url.indexOf('?')>=0){
		url += "&mark="+getDateStr() ;
	}else{
		url += "?mark="+getDateStr() ;
	}
	obj.location = url ;
}

function focusFirst(){
	document.forms[0].elements[0].focus();
}

function $F(v){
	if(!$defined ($(v))){
		alert("not defined:"+v);
		return "";
	}
	return $(v).value ;
}

//日期增加年数或月数或天数
function DateAdd(BaseDate, interval, DatePart){
 var dateObj = new Date(BaseDate.replace("-",","));
// alert(dateObj.toLocaleString());
 var millisecond=1;
 var second=millisecond*1000;
 var minute=second*60;
 var hour=minute*60;
 var day=hour*24;
 var year=day*365;

 var newDate;
 var dVal = new Date(dateObj)
 var dVal=dVal.valueOf();
 switch(DatePart)
 {
  case "ms": newDate=new Date(dVal+millisecond*interval); break;
  case "s": newDate=new Date(dVal+second*interval); break;
  case "mi": newDate=new Date(dVal+minute*interval); break;
  case "h": newDate=new Date(dVal+hour*interval); break;
  case "d": newDate=new Date(dVal+day*interval); break;
  case "y": newDate=new Date(dVal+year*interval); break;
  default: return escape("日期格式不对");
 }
 newDate = new Date(newDate);
 return newDate.getFullYear() + "-" + (newDate.getMonth() + 1) + "-" + newDate.getDate() ; 
} 


if   (window.Node)   
  {   
  Node.prototype.contains   =   function(Node)   
  {   
  if(!Node)   return   false;   
  var   o_tag   =   Node.tagName;   
  var   o   =   this.getElementsByTagName(o_tag);   
  var   l   =   o.length;   
  for   (var   i=0;   i<l;   i++)   
  {   
  if   (o[i]   ==   Node)   
  {   
  return   true;   
                                  }   
  }   
  return   false;   
  }   
  }   

	function changeRow(obj){
		
		var clsName="uncheckedRow";
		if(obj.checked){
			clsName="checkedRow";
		}
		$("row"+obj.value).className=clsName;
	}


function ToDBC(obj)//全角转半角
{ 
	var str=obj.value;
	var result="";
	for (var i = 0; i < str.length; i++)
	{
		if (str.charCodeAt(i)==12288)
		{
			result+= String.fromCharCode(str.charCodeAt(i)-12256);
			continue;
		}
		if (str.charCodeAt(i)>65280 && str.charCodeAt(i)<65375)
		result+= String.fromCharCode(str.charCodeAt(i)-65248);
		else result+= String.fromCharCode(str.charCodeAt(i));
	} 
	obj.value=result;
} 

function copyToClipboard(txt) {    
     if(window.clipboardData) {    
             window.clipboardData.clearData();    
             window.clipboardData.setData("Text", txt);    
     } else if(navigator.userAgent.indexOf("Opera") != -1) {    
          window.location = txt;    
     } else if (window.netscape) {    
          try {    
               netscape.security.PrivilegeManager.enablePrivilege("UniversalXPConnect");    
          } catch (e) {    
               alert("被浏览器拒绝！\n请在浏览器地址栏输入'about:config'并回车\n然后将'signed.applets.codebase_principal_support'设置为'true'");    
          }    
          var clip = Components.classes['@mozilla.org/widget/clipboard;1'].createInstance(Components.interfaces.nsIClipboard);    
          if (!clip)    
               return;    
          var trans = Components.classes['@mozilla.org/widget/transferable;1'].createInstance(Components.interfaces.nsITransferable);    
          if (!trans)    
               return;    
          trans.addDataFlavor('text/unicode');    
          var str = new Object();    
          var len = new Object();    
          var str = Components.classes["@mozilla.org/supports-string;1"].createInstance(Components.interfaces.nsISupportsString);    
          var copytext = txt;    
          str.data = copytext;    
          trans.setTransferData("text/unicode",str,copytext.length*2);    
          var clipid = Components.interfaces.nsIClipboard;    
          if (!clip)    
               return false;    
          clip.setData(trans,null,clipid.kGlobalClipboard);    
          alert("复制成功！")    
     }    
}   

function renderTable(obj){
	var arr = $ES("tr",obj);
	
	for(var i = 0 ; i < arr.length ;i ++ ) {
		if(arr[i].className =="title") continue ;
		if(i%2==0){
			
			arr[i].className = "evenRow"; 
		}else{
			arr[i].className = "oddRow";
		}

	}

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

function betweenDays(b,e){

	b = b.replace(/-/g, "/"); 
	e = e.replace(/-/g, "/"); 
	var s1 = new Date(b);
	var s2 = new Date(e);

	var days= s1.getTime() - s2.getTime(); 
	var time = parseInt(days / (1000 * 60 * 60 * 24));

	//alert("相差天数: " + days);
	return time;
}

function focusDelay(obj){
	if(Browser.ie){
		window.setTimeout(function(){$(obj).focus();},100);
	}else{
		//alert('ho');
		$(obj).focus();
	}
}

function loadCal(){
	var numargs = arguments.length;
	for(var i = 0 ; i < numargs ; i ++){
		Calendar.setup(
		{
			  inputField  : arguments[i],         // ID of the input field
			  ifFormat    : "%Y-%m-%d",    // the date format
			  button      : arguments[i]       // ID of the button
		});
	}
}

function loadCalCss(css){
	
	$$(css).each( function (el){
		Calendar.setup(
		{
			  inputField  : el,         // ID of the input field
			  ifFormat    : "%Y-%m-%d",    // the date format
			  button      : el       // ID of the button
		});
	});
}

function _getBirthday(idcard){
	if(idcard.length<18){
		return "";
	}

	var y = idcard.substring(6,10);
	var m = idcard.substring(10,12);
	var d = idcard.substring(12,14);

	return y+"-"+m+"-"+d;
}

/**
*功能：返回格式化后的日期字符串
*参数：formatStr为格式字符串，其中表示形式如下列表
*“ddd”－汉字星期几
*“yyyy”－四位数年份
*“MM”－两位数月份
*“dd”－两位数日期
*“hh”－两位数小时
*“mm”－两位数分钟
*“ss”－两位数秒数
*“y”－年份
*“M”－月份
*“d”－日期
*“h”－小时
*“m”－分钟
*“s”－秒数
*/
Date.prototype.format = function (formatStr) {
var WEEK = new Array("星期日","星期一","星期二","星期三","星期四","星期五","星期六");
var s = formatStr;

s = s.replace(/d{3}/g, WEEK[this.getDay()]);

s = s.replace(/y{4}/g, this.getFullYear());
s = s.replace(/M{2}/g, (this.getMonth()+1)<10 ? "0"+(this.getMonth()+1) : (this.getMonth()+1));
s = s.replace(/d{2}/g, this.getDate()<10 ? "0"+this.getDate() : this.getDate());
s = s.replace(/h{2}/g, this.getHours()<10 ? "0"+this.getHours() : this.getHours());
s = s.replace(/m{2}/g, this.getMinutes()<10 ? "0"+this.getMinutes() : this.getMinutes());
s = s.replace(/s{2}/g, this.getSeconds()<10 ? "0"+this.getSeconds() : this.getSeconds());

s = s.replace(/y{1}/g, this.getFullYear());
s = s.replace(/M{1}/g, this.getMonth() + 1);
s = s.replace(/d{1}/g, this.getDate());
s = s.replace(/h{1}/g, this.getHours());
s = s.replace(/m{1}/g, this.getMinutes());
s = s.replace(/s{1}/g, this.getSeconds());

return(s);
}