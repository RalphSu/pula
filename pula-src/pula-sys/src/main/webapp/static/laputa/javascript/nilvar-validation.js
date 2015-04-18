
function isBetween(val,lo,hi){
	if (val <lo){

		return false;
	}

	if (val >hi){ 

		return(false);
	}
	
	return(true);

}

function isBigDate(srcDate,destDate){

	//not date format
	if((!isDate(srcDate))||(!isDate(destDate))){
		return -1 
	}

	var the1st = srcDate.indexOf('-');
	var the2nd = srcDate.lastIndexOf('-');
	var srcy = srcDate.substring(0,the1st);
	var srcm = srcDate.substring(the1st+1,the2nd);
	var srcd = srcDate.substring(the2nd+1,srcDate.length);			

	the1st = destDate.indexOf('-');
	the2nd = destDate.lastIndexOf('-');
	var desty = destDate.substring(0,the1st);
	var destm = destDate.substring(the1st+1,the2nd);
	var destd = destDate.substring(the2nd+1,destDate.length);			
	if(srcy<desty){
		return 0;
	}

	if(srcm<destm){
		return 0 ;
	}

	if(srcd>destd){
			return 1 ;
	}
	
	return 0 ;

}

function isDate(theStr){

	var the1st = theStr.indexOf('-');
	var the2nd = theStr.lastIndexOf('-');
		
	if(the1st == the2nd){ 
		return (false);
	}else{
		var y = theStr.substring(0,the1st);
		var m = theStr.substring(the1st+1,the2nd);
		var d = theStr.substring(the2nd+1,theStr.length);
		var maxDays = 31 ;
		if(isInt(m)==false||isInt(d)==false||isInt(y)==false){
			return false;			
		}else if(y.length<4){
			return false;
		}
		else if(!isBetween(m,1,12)){ return(false);}
		else if(m==4||m==6||m==9||m==11)maxDays = 30 ;
		else if(m==2) {
			if(y % 4 >0 ) maxDays = 28 ;
			else if (y%100 ==0 && y%400 >0) maxDays = 28 ;
			else maxDays = 29 ;
		}
		if (isBetween(d,1,maxDays)==false){return(false);}

		else if(y<1900){return false;}
		else{return(true);}
	}

}


function isTime(theStr){
	var colonDex = theStr.indexOf(':');
	if((colonDex<1)||(colonDex>2)){return(false);}
	else{
		var hh = theStr.substring(0,colonDex);
		var ss = theStr.substring(colonDex+1,theStr.length);
		if((hh.length<1)||(hh.length>2)||(!isInt(hh))){return (false);}
		else if((ss.length<1)||(ss.length>2)||(!isInt(ss))){return (false);}
		else if((!isBetween(hh,0,23))||(!isBetween(ss,0,59))){return(false);}
		else{ return(true);}
	}
}


function isTimeHM(theStr){
	var colonDex = theStr.indexOf(':');
	if((colonDex<1)||(colonDex>2)){return(false);}
	else{
		var hh = theStr.substring(0,colonDex);
		var ss = theStr.substring(colonDex+1,theStr.length);
		if((hh.length<1)||(hh.length>2)||(!isInt(hh))){return (false);}
		else if((ss.length<1)||(ss.length>2)||(!isInt(ss))){return (false);}
		else if((!isBetween(hh,0,23))||(!isBetween(ss,0,59))){return(false);}
		else{ return(true);}
	}
}

	function IsTimeOrEmpty(v){
		if(isEmpty(v)){
			return true ;
		}

		if(isTimeHM(v)){
			return true ;

		}

		return false;

	}

function isDigit(theNum){
	var theMask = '0123456789';
	if(isEmpty(theNum)) return (false);
	else if(theMask.indexOf(theNum) == -1) return(false);
	return(true);
}

function isEmpty(str){
	if((str==null)||(str.length==0)) return true ;
	if(str=='EMPTY-STRING') return true ;
	else return(false);
}


function isInt(theStr){
	var flag = true ;
	if(isEmpty(theStr)){flag = false;}
	else
	{
		for(var i=0;i<theStr.length;i++){
			if(isDigit(theStr.substring(i,i+1))==false){
				flag = false ; break;
			}
		}
	}
	return(flag);
}

function isInteger(str){
	var integer = parseInt(str); 
	if (isNaN(integer)) 
	{ 

		return false; 
	} 
	return true ;
}

function isUInt(theStr){
	return ((isInt(theStr))&&(eval(theStr)>0));
}

function isNInt(theStr){
	return ((isInt(theStr))&&(eval(theStr)>=0));
}

function isReal(theStr){
	if(theStr == null ){
		return false;
	}
	var dot1st = theStr.indexOf('.');
	var dot2nd = theStr.lastIndexOf('.');
	var OK = true ;
	if (isEmpty(theStr)) return false ;
	if (dot1st == -1){
		if(!isInt(theStr)) return (false);
		else return(true);
	}
	else if(dot1st != dot2nd) return (false);
	else if(dot1st == 0 ) return(false);
	else{
		var intPart = theStr.substring(0,dot1st);
		var decPart = theStr.substring(dot2nd+1);
//		if(decPart.length >decLen) return(false);
		if(!isInt(intPart) || !isInt(decPart)) return(false);
		else if(isEmpty(decPart)) return(false);
		else return(true);
	}
}

//
function isUReal(theStr){
	if(!isReal(theStr)){return false;}
	if(theStr<=0){return false;}
	return true;
}

function isNReal(theStr){
	if(!isReal(theStr)){return false;}
	if(theStr<0){return false;}
	return true;
}

function isQuot(str){
	var slen = str.length ;
	var i,s ;
	for (i=0;i<slen;i++)
	{
		 s = str.substring(i,i+1) ;
		if(s=="'"||s=="\""){
			return true ;
		}
	}
	return false;
}

function isDateTime(str){

  var n = str.indexOf(' ');
  if (n<0){
	  return false;
  }

  var datestr = str.substring(0,n);
  var timestr = str.substring(n+1);
  if(!(isDate(datestr)&&isTime(timestr))){
		return false ;
  }
  
  return true;
}

//email check
function isEmail(strEmail) { 
  var myReg = /^[_a-zA-Z0-9+\.]+@([\-_a-zA-Z0-9]+\.)+[A-Za-z0-9]{2,3}$/; 
  if(myReg.test(strEmail)) return true; 
  return false; 
}

function isCharsInBag (s, bag){ 
	var i,c;
	for (i = 0; i < s.length; i++){ 
		c = s.charAt(i);
		if (bag.indexOf(c)<= -1) {
			return "";
		}
	}
	return "find";
}

function isKeyStr(s){
	var errorChar;
	var theChar = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_-.";
	errorChar = isCharsInBag(s, theChar)
	if (errorChar != "" ){
		return true;
	} 
	return false;
}


function goURL(url){
	url=trimMark(url);
	if(url.indexOf('?')>=0){
		url += "&mark="+getDateStr() ;
	}else{
		url += "?mark="+getDateStr() ;
	}
	location.href = url;
}



function goMainFrameURL(url){
	if(url.indexOf('?')>=0){
		url += "&mark="+getDateStr() ;
	}else{
		url += "?mark="+getDateStr() ;
	}
	top.mainFrame.location.href = url;
}



function goURL2(url,obj){
	if(url.indexOf('?')>=0){
		url += "&mark="+getDateStr() ;
	}else{
		url += "?mark="+getDateStr() ;
	}
	obj.location = url ;
}

function centerWindow(url,width,height) {
	if (document.all)
		var xMax = screen.width, yMax = screen.height;
	else
		if (document.layers)
		var xMax = window.outerWidth, yMax = window.outerHeight;
	else
		var xMax = 640, yMax=480;
		var xOffset = (xMax - width)/2, yOffset = (yMax - height)/2;

	if(url.indexOf('?')>=0){
		url += "&mark="+getDateStr() ;
	}else{
		url += "?mark="+getDateStr() ;
	}

	var newwin = window.open(url,'', 'toolbar=0,resizable=1,scrollbars=1,dependent=0,width='+width+',height='+height+',screenX='+xOffset+',screenY='+yOffset+', top='+yOffset+',left='+xOffset+',statusbar=yes');
//	var newwin = window.open(url,'');
	newwin.focus();
	return newwin ;
}

function backWindow(url) {
	
	if(url.indexOf('?')>=0){
		url += "&mark="+getDateStr() ;
	}else{
		url += "?mark="+getDateStr() ;
	}

	var newwin = window.open(url,'', 'toolbar=0,resizable=0,scrollbars=0,dependent=0,width=1,height=1,screenX=1,screenY=1, top=1,left=1,statusbar=no');
	newwin.opener.focus();
	return newwin ;
}

function queryString( key ) {
	var href = window.location.search;
	var lhref = href.toLowerCase();

	var pos = lhref.indexOf( key + "=" );
	if (pos==-1) return null;

	var next = lhref.indexOf( "&", pos );
	var value = href.substring( pos + key.length + 1, (next==-1) ? 999 : next );

	return value;
}

function formatMoney(obj){

		if(!isReal(obj.value)){
			obj.style.backgroundColor= 'pink' ;
			return;
		}else{
			obj.style.backgroundColor= '';
		}

		var srcValue = obj.value*1 ;
		var destValue = srcValue ;

		var intValue = parseInt(srcValue); 

		//alert(intValue);

		if(isNaN(intValue)){
			
			destValue = srcValue ;

		}else if(intValue==srcValue){
			destValue = intValue+".00";
		}else{
			intValue = parseInt(srcValue*100);
			if(isNaN(intValue)){
				//destValue = srcValue ;
			}else if(intValue!=srcValue*100){
				destValue = intValue/100;
			}
		}

		//fill the zero 
		var tmps  = ""+srcValue;
		//alert(srcValue);
		var i = tmps.indexOf(".");
		
		if (i>=0&&tmps.length-i<3)
		{
			
			destValue = srcValue+"0";
		}
		
		//alert(i+";tmps="+tmps);
		if (destValue==0)
		{
			destValue = "0.00";
		}
		
		obj.value = destValue ;


}

function getMoney(v){
	return (Math.round(v*100)/100);
}

function isEmptyURL(url){
		if(isEmpty(url)) return true ;
		if(url=="about:blank"||url=="welecome.action") return true ;
		return false;

}

function goEmail(email){
	goURL("mailto:"+email);
}

function goURL3(url,destWin){
	var url = url.replace("http://http://" , "http://");
	//alert(url);
	if (destWin=="_blank")
	{
		window.open(url);
	}
	else{
		goURL2(url,destWin);
	}
}

 


 


//by tiyi 2005-07-30 01:10:42 - the result of learning regex
function isFilePath(strFilePath) { 
  var myReg = /^(\\|(\w{1}:))/; 
  if(myReg.test(strFilePath)) return true; 
  return false; 
}

function isHour(h){
	if(!isUInt(h)){
		return false ;
	}

	var ih = eval(h);
	if(ih<0||ih>=24){
		return false;
	}

	return true ;
}

function isMinute(m){
	if(!isUInt(m)){
		return false ;
	}

	var ih = eval(m);
	if(ih<0||ih>=60){
		return false;
	}

	return true ;
}



function isMoney(str){
	var str = Math.abs(str);
  return (/^([0-9]\d+|[0-9])(\.\d\d?)*$/.test(str));
}

function isEmptyURL(url){
		if(isEmpty(url)) return true ;
		if(url=="about:blank"||url=="welecome.action") return true ;
		return false;

}

function isYear(n){
if(!isInt(n)){
	return false;
}

var y = eval(n);

/*if(y<2000){
	return false;
}*/

return true ;
}


function formatNumberObj(obj){
	
	var v = obj.value ;
	var newValue = formatNumberNut(v,"0.00");
	if(isNaN(newValue)){
		obj.value = "0.00";
	}else{
		obj.value = newValue ;
	}

}

	function formatNumberNut(number,pattern)
	{
		//alert(number);
		//return "ho";
		var str			= number.toString();
		var strInt;
		var strFloat;
		var formatInt;
		var formatFloat;
		if(/\./g.test(pattern))
		{
			formatInt		= pattern.split('.')[0];
			formatFloat		= pattern.split('.')[1];
		}
		else
		{
			formatInt		= pattern;
			formatFloat		= null;
		}

		if(/\./g.test(str))
		{
			if(formatFloat!=null)
			{
				var tempFloat	= Math.round(parseFloat('0.'+str.split('.')[1])*Math.pow(10,formatFloat.length))/Math.pow(10,formatFloat.length);
				strInt		= (Math.floor(number)+Math.floor(tempFloat)).toString();				
				strFloat	= /\./g.test(tempFloat.toString())?tempFloat.toString().split('.')[1]:'0';			
			}
			else
			{
				strInt		= Math.round(number).toString();
				strFloat	= '0';
			}
		}
		else
		{
			strInt		= str;
			strFloat	= '0';
		}
		if(formatInt!=null)
		{
			var outputInt	= '';
			var zero		= formatInt.match(/0*$/)[0].length;
			var comma		= null;
			if(/,/g.test(formatInt))
			{
				comma		= formatInt.match(/,[^,]*/)[0].length-1;
			}
			var newReg		= new RegExp('(\\d{'+comma+'})','g');

			if(strInt.length<zero)
			{
				outputInt		= new Array(zero+1).join('0')+strInt;
				outputInt		= outputInt.substr(outputInt.length-zero,zero)
			}
			else
			{
				outputInt		= strInt;
			}

			var 
			outputInt			= outputInt.substr(0,outputInt.length%comma)+outputInt.substring(outputInt.length%comma).replace(newReg,(comma!=null?',':'')+'$1')
			outputInt			= outputInt.replace(/^,/,'');

			strInt	= outputInt;
		}

		if(formatFloat!=null)
		{
			var outputFloat	= '';
			var zero		= formatFloat.match(/^0*/)[0].length;

			if(strFloat.length<zero)
			{
				outputFloat		= strFloat+new Array(zero+1).join('0');
				//outputFloat		= outputFloat.substring(0,formatFloat.length);
				var outputFloat1	= outputFloat.substring(0,zero);
				var outputFloat2	= outputFloat.substring(zero,formatFloat.length);
				outputFloat		= outputFloat1+outputFloat2.replace(/0*$/,'');
			}
			else
			{
				outputFloat		= strFloat.substring(0,formatFloat.length);
			}

			strFloat	= outputFloat;
		}
		else
		{
			if(pattern!='' || (pattern=='' && strFloat=='0'))
			{
				strFloat	= '';
			}
		}
		

		return strInt+(strFloat==''?'':'.'+strFloat);
	}

function isMobile(mobile){ 
   
     var reg0=/^13\d{5,9}$/; //130--139。至少7位 
     var reg1=/^153\d{8}$/; //联通153。至少7位 
     var reg2=/^159\d{8}$/; //移动159。至少7位 
     var reg3=/^158\d{8}$/; 
     var reg4=/^150\d{8}$/; 
     var reg5=/^18\d{9}$/; 
     var my=false; 
     if (reg0.test(mobile))my=true; 
     if (reg1.test(mobile))my=true; 
     if (reg2.test(mobile))my=true; 
     if (reg3.test(mobile))my=true; 
     if (reg4.test(mobile))my=true; 
     if (reg5.test(mobile))my=true; 
	 return my ;
} 


function isChinese(temp) 
{ 
var re = /[^\u4e00-\u9fa5]/; 
for(var i = 0 ; i < temp.length ; i++){
	if(!re.test(temp[i])) {
		return true ;
	}else{

	}
}
return false; 
} 
