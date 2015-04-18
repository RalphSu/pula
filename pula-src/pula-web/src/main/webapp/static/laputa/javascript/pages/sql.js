
window.addEvent('domready',function(){	
		$('textForm').addEvent('submit',function (e){
			e.stop();
			if(!check1()) return ;
			PA.ajax.gf('_execute',$('textForm').toQueryString(),function(e){
					$("submitBtn2").disabled = false;
					if(e.error){
						alert(e.message);
					}else{
						topSuccess();

						if(e.data == null){
							$('textForm').reset();
							$('textForm').value = "";
							$("resultDiv").addClass("expired");
						}else{
							processResult(e.data);
						}
					}

			});

		});
	});

	
	function check1(form){
		if(isEmpty($F("textHere"))){
			alert(lang.inputText);
			return false;
		}
		
		$("submitBtn2").disabled = true ;
		return true ;

	}


function processResult(da){
	$
	//build labels 
	sb = "<div class='columns'>";
	sb += "<h3>"+lang.row+"</h3>";
	da.labels.each ( function (l){
		sb += "<h3>"+l+"</h3>";
	});
	sb+="<div class='c'></div></div>";
	sb += "<div class='rows'>";
	//rows
	var rowIndex = 1 ;
	da.rows.each ( function(l){
		sb+= "<div class='row'><h4>"+rowIndex+"</h4>";

		da.labels.each ( function(label){
			var d = l[label];			
			sb+= "<h4>"+d+"</h4>";
		});
		rowIndex++;
		sb+="<div class='c'></div></div>";
	});
	sb +="</div>";

	$("resultDiv").empty();
	$("resultDiv").innerHTML = sb ;
	$("resultDiv").removeClass("h");

	//width calc

	//columns 
	var maxRow = new Array();
	$$("#resultDiv .columns h3").each ( function (el ) {
		//make a array for then max length 
		maxRow.push( el.getSize().x ) ;
	});

	
	//every row now 
	$$("#resultDiv .rows div").each ( function (el){
		el.getChildren("h4").each ( function( cell,index) {
			var max = maxRow[index];
			var cellX = cell.getSize().x;
			maxRow[index] = Math.max(max, cellX);
		});
	});

	var total_width =0;
	//apply the max width
	$$("#resultDiv .columns h3").each ( function (el ,index) {
		el.setStyle('width',maxRow[index]);
		total_width += maxRow[index]+10;
	});

	$$("#resultDiv .rows div.row").each ( function (el,n){
		if(n%2==1){
			el.addClass('even');
		}
		el.getChildren("h4").each ( function( cell,index) {
			cell.setStyle('width',maxRow[index]);
		});
	});
	//alert(JSON.encode(maxRow));

	$("resultDiv").setStyle('width',total_width);
}

var log = function(v){
		if(this.logger){			
		}else{
			this.logger = new Element('div.logger');
			this.logger.inject(document.body);
		}
		this.logger.innerHTML += "<BR/>" + JSON.encode(v) ;
	}