var fireChangeTotal = function(){
		var sb = "" ;
		var counter = 0 ;
		$$(".chk").each ( function(el){
		
			if(el.checked){
				counter ++ ;
				sb += el.value +",";
			}

		});

		sb = sb.substring(0,sb.length-1);
		sb += lang.total +counter;

		$('total').set('html',sb);

		if(counter == 0 ){
			$('total').addClass('h');
		}else{
			$('total').removeClass('h');
		}

	}

	var pushInSelect = function(obj){
		var got =false;
		for(var i = 0 ; i < $('tableSelect').options.length ; i ++){
			var opt = $('tableSelect').options[i] ; 
			if(opt.value == obj.value ) {
				got= true ;
				if( !obj.checked ){
					$('tableSelect').options[i] = null ;
				}
				break ;
			};
		}
		
		if(obj.checked && !got){
			var newItem = new Element('option',{   
							 text: obj.value,
							 value: obj.value
			});
			$('tableSelect').adopt (newItem);
		}
		
	}

	var changeButton = function(){
		$$('.moveBtn').set('disabled',true);
		var f = false ,lastIndex = -1 ;
		for(var i = 0 ; i < $('tableSelect').options.length ; i ++){
			var opt = $('tableSelect').options[i] ; 
			var s = opt.get('selected');
			if(s ){
				if( !f){ 
					f = true ;
					if( i > 0 ){
						$('moveUpBtn').set('disabled',false);
					}
				}
				lastIndex = i ;
			}
		}

		if(lastIndex < $('tableSelect').options.length -1 ){
			$('moveDownBtn').set('disabled',false);
		}
	}

	var moveItem = function(){
		
		var moveUp = ( this ==1 ) ;
		var fi = -1,li = -1 ;
		for(var i = 0 ; i < $('tableSelect').options.length ; i ++){
			var opt = $('tableSelect').options[i] ; 
			var s = opt.get('selected');
		
			if(s ){
				if(fi<0){
					fi = i ; 
				}
				li = i ;
			}
		}
		
		var target = fi - 1; 
		if( !moveUp ) {
			target = li + 1 ;
		}
		
		var topt = $('tableSelect').options[target];

		if(moveUp){
			var lastData = topt.value ;

			for(var i = fi -1 ; i <= li ; i ++){
				var opt = $('tableSelect').options[i] ;
				if( i== li ){
					opt.value = lastData;
					opt.text = lastData;
					opt.selected = false ;
				}else{
					opt.value = $('tableSelect').options[i+1].value ;
					opt.text = $('tableSelect').options[i+1].text ;
					opt.selected = true;
				}
			}

		}else{
			var lastData = topt.value ;
			
			for(var i = li+1 ; i >= fi ; i --){
				var opt = $('tableSelect').options[i] ;

				if( i == fi ){
					opt.value = lastData;
					opt.text = lastData;
					opt.selected = false ;
				}else{
					opt.value = $('tableSelect').options[i-1].value ;
					opt.text = $('tableSelect').options[i-1].text ;
					opt.selected = true;
				}
			}

		}
		changeButton();

	}

	PA.on( function () {

		
		$$(".chk").addEvent('click',function(){
			var label = this.getParent('label') ;
			if( this.get('checked') ) {
				$(label).addClass('checked');
			}else{
				$(label).removeClass('checked');
			}
			fireChangeTotal();

			pushInSelect( this) ;

		});
			

		if(pageVars.hasData){

			fireChangeTotal();

			$('frmPost').addEvent('submit', function(e){
				PA.utils.selectAll( 'tableSelect' ) ;
			});

			$('tableSelect').addEvent('change' ,function(e){
				//alert('ho');
				changeButton();
			});

			$('moveUpBtn').addEvent('click', moveItem.bind(1));
			$('moveDownBtn').addEvent('click', moveItem.bind(2));

			changeButton();

			$('btnPick').addEvent('click' ,function(){
				$$(".chk").set('checked',false).getParent("label").removeClass('checked');
				PA.utils.clearSelect( $('tableSelect') ) ;
				var v = $('txtRule').value ;
				var array = v.split(",");
				/*$$(".chk").each ( function (el){
					if(array.contains( el.value )){
						el.checked = true ;
						pushInSelect( el ) ;
					}

				});*/
				
				array.each ( function (el ){
					var obj = $('chk_'+el);
					if(obj == null){

					}else{
						obj.checked = true ;
						obj.fireEvent('click');
						//pushInSelect( obj ) ;
					}
				});
				
				
				//fireChangeTotal();
			});
			fireChangeTotal();
		}
	});
