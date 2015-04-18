/**
 * autoSuggest.js
 *
 * http://www.theWittyRejoinder.com/autoSuggest.php
 * CHANGELOG:
 *    25 August 2009:  Added explicit support for 'tabbing'; the auto-suggest now supports familiar form tabbing action.
 *                     Fixed bug which prevented removal of list when user clicks away (pseudo-blur)
 *                     Added support to allow a re-query when user re-clicks into the display input
 *                     Added support to allow a re-query when vertical arrow keys are pressed and display is not currently shown
 *    26 August 2009:  Added support for selection with 'enter' key
 * @version		0.9.3
 *
 * @license		MIT-style license http://www.opensource.org/licenses/mit-license.php
 * @author		Matthew (matthew at theWittyRejoinder.com)
 * 
 */



var autoSuggest = new Class({
	Implements: [Options, Events],
	options: {
			method: 'post', // HTTP Method by which to submit AJAX request 
			minLength: 3,	// the minimum length (character count) before requests are sent
			cache: true,	// cache previous queries (registered per page refresh). When set to true, onyl one request per case-sensitive query term will be sent  
			queryTerm: 'query', // the queryString name to be sent to the remote suggestion server (.../?query=searchForMe)
			suggestionClass: 'suggestionClass', // CSS class used to style the 'li' element containing each suggested item
			highlightClass:'suggestionHover', 	// CSS class used to style the 'li' element when it is selected (hover, etc.)
			queryValueHighlightClass: 'queriedValue', // CSS class used to mark the queried portion of suggestion.
			markQuery: true,	// set to true to mark the queried portion of the display with the class name set above
			queryOnFocus: false // set to true to send a query request when the user clicks into a form element
	},
	/**
	 * @param string url the url of the ajax-processing page
	 * @param string display the id of the element which will be the visible form element
	 * @param string hidden the id of the hidden form element
	 * @param array options see above 
	 */
	initialize: function(url,display,hidden,options) {
		this.url = url;
		this.display = display;
		this.hidden = hidden;
		this.setOptions(options);
		if($(this.hidden) != null){
			this.useHidden = true;
		} else {
			
			this.hidden = null;
		}
		
		$(document.body).addEvent('click', function(e){
			this.gcList();
		}.bind(this));
		
		this.build();
		
		if(this.options.queryOnFocus){
			$(this.display).addEvent('focus', function(e){
				this.handleInput();		
			}.bind(this));
		}

	},
	
	handleInput: function(){
		this.enteredText = $(this.display).value;
		if(($(this.display).value.length) >= this.options.minLength){
			this.makeRemoteRequest(this.enteredText);
		}
	},
	
	/**
	 * create and populate the displayed element
	 */
	build: function(){
		this.suggestions = new Element('ul', {
			'id':   'suggestions',
			'class':'suggestions h',
			'styles': {
				'opacity':'.80'
				}
			}).inject(document.body);
		
		 // IE wants to display the UL to the right of the input box, not below it.  
		 // This 'fix' puts the content below the input box but will shoft the alignment
		 // of any content in the same block-level element as the input but after it. 
		 if(Browser.Engine.trident){
			house = new Element('div');
		 	house.inject($(this.display), 'after');
			this.suggestions.inject(house);
		 } else {
		 	this.suggestions.inject($(this.display), 'after');
		 }
	
		this.addEventsToList();
		this.gcList();
	},
	
	/**
	*	Make a request to the remote site to get a JSON-encoded list of suggestions.
	*	When the list is received, parse it and update the suggestion list.
	*/
	makeRemoteRequest: function(query,queryTerm){
		if(this.options.cache && this.cachedData.has(query)){
			this.handleIncomingData(this.cachedData.get(query));
		} else {
	
		var req = new Request({  
			method: this.options.method,  
			url: this.url,
			data: {'query': query},  
			onRequest: function() {  },  
			onComplete: function(response) {
				this.handleIncomingData(response);
			}.bind(this)}
		).send(); 
	} 
	},
	
	/**
	 * Parses the incoming data from an AJAX request
	 */
	handleIncomingData:  function(response){
		if(!JSON.parse){
			JSON.parse = JSON.decode ;
		}
		data = JSON.parse(response);
		
		if(this.options.cache && !this.cachedData.has(data.query)){
			var hash = new Hash();
			var hash = $H();
			hash.set(data.query,response); 
			this.cachedData.extend(hash);	
		}
		
		this.suggestionData = data.suggestions;
		if(this.suggestionData.length > 0){
			this.populate();
		} else {
			this.gcList();
		}
	},
	
	/**
	*	Arrange to have the suggestions and events added to the list
	*/
	populate: function(){
		this.gcList();
			this.suggestionData.each(function(item,index){
				for(key in item){
					this.addSuggestion(item[key],key);
				}
			}.bind(this));
			this.addEventsToSuggestions();
			this.suggestionsShown = true;
			this.suggestions.removeClass('h');
			if(this.suggestions.getChildren('li').length > 0){
				this.suggestions.fade('in');
			}
	},
	
	/**
	* Adds a 'suggestion' to the current list of suggestions.
	* 
	* @param displayValue:  The text to be displayed as the suggestion and the text to be displayed in the input after choosing a selection.
	* @param hiddenValue: The value to be entered in the hidden element after making a selection. 
	*/
	addSuggestion: function(displayValue,hiddenValue){
		var suggestion = new Element('li', {
			'html': this.markupSuggestion(displayValue),
			'class':this.options.suggestionClass,
		 	'styles':{ 	}
		});	
			var hiddenValue = new Element('input',{
				'type':'hidden',
				'value':hiddenValue
			});	
		var displayValue = new Element('input',{
			'type':'hidden',
			'value':displayValue
		});			
		hiddenValue.inject(suggestion);
		displayValue.inject(suggestion);
		suggestion.inject(this.suggestions);
	},
	/**
	* Wraps the entered text in a highlighting class to display the 
	* entered text in the suggestion.		
	*/
	markupSuggestion: function(str){
		if(this.options.markQuery){
			var marked = '<span class="' + this.options.queryValueHighlightClass + '">' + this.enteredText + '</span>';
			var et_regex = new RegExp('(' + this.enteredText + ')', 'i');
			//return str.replace(et_regex,marked);
			return str.replace(et_regex,'<span class="' + this.options.queryValueHighlightClass + '">$1</span>');
		} else {
			return str;
		}
	},
	
	/**
	* Adds events to the items in the suggestion list (mouseover, click)
	*/
	addEventsToSuggestions: function(){
		this.suggestions.getChildren().each(function(item,index){
			$(item).addEvent('mouseover',function(e){
				this.setSelection($(item));
			}.bind(this));
			$(item).addEvent('mouseout',function(e){
				$(item).removeClass(this.options.highlightClass);
			}.bind(this));
			
			
			$(item).addEvent('click',function(e){
				e.stop();
				this.handleUserChoice($(item));
				this.gcList();
			}.bind(this));
			
		return this.suggestions;
		}.bind(this));
	},
	
	/**
	*	Add events which apply to the list 
	*/
	addEventsToList: function(){
		$(this.display).addEvent('dblclick', function(e){
			this.handleInput();
		}.bind(this));
		
		$(this.display).addEvent('keydown',function(e){
			if(e.key == 'enter'){
				e.stop();
				this.handleUserChoice(this.currentSelection);
			} else if(e.key == 'tab'){
				this.handleUserChoice(this.currentSelection);
			}
		}.bind(this));
		
		
		
		$(this.display).addEvent('keydown',function(e){
			
			var next = null;
			if(e.key == 'down' || e.key == 'up'){
				e.stop();
				
				if(e.key == 'down'){
					if(this.currentSelection == null){
						next = this.suggestions.getFirst('li');
					} else if(this.currentSelection.getNext('li') == null) {
						next = this.suggestions.getFirst('li');
					} else {
						next = this.currentSelection.getNext('li');
					}
				} else if(e.key == 'up'){
					if(this.currentSelection == null){
						next = this.suggestions.getLast();
					}else if(this.currentSelection.getPrevious() == null) {
						next = this.suggestions.getLast('li');
					} else {
						next = this.currentSelection.getPrevious('li');
					}
				}
				this.setSelection(next);
				}
		}.bind(this));
		
		
		
		
		$(this.display).addEvent('keyup', function(e) {
			if(e.key == 'esc') {
				this.gcList();
			} else if(e.key == 'up' || e.key == 'down'){
				e.stop();
				if(!this.suggestionsShown){
					this.handleInput();
				}
			} else if(e.key == 'enter'){
				e.stop();
			} else {
				this.handleInput();
			}
		}.bind(this));
		
		
		
		
	},
	setSelection: function(el){
		this.suggestions.getChildren().each(function(item,index){
			$(item).removeClass(this.options.highlightClass);
		}.bind(this));
		this.currentSelection = el;
		if(el != null){
			el.addClass(this.options.highlightClass);
		}
	},

	/**
	* called when a choice was made; populate the displayed and hidden fields
	*/
	handleUserChoice: function(item){
		if(item == null) return;
		if(this.useHidden) {
			$(this.hidden).value  = item.getChildren('input')[0].value; 
		}
		$(this.display).value = item.getChildren('input')[1].value;
		this.gcList();
	},
	
	/**
	*	gc == 'Garbage Collector'
	*   call this function to hide the list and remove the suggestions ('li') from the list. 
	*/
	gcList: function(){
		this.currentSelection = null;
		this.suggestionsShown = false;
		this.suggestions.getChildren().each(function(item,index){
			$(item).dispose();
		});
		this.suggestions.fade('out',{
		 	onComplete: function(){
				
			}
		 });  
	},
	cachedData: new Hash(),
	suggestionData: null,
	currentSelection: null,
	suggestions: null,
	enteredText: '',
	url: null,
	display: null,
	hidden: null,
	useHidden: false,
	suggestionsShown: false
});
	