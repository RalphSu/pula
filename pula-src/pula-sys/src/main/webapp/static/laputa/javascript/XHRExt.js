var XHRExt = XHR.extend({
	attach : null ,

	onSuccess: function(){
		this.response = {
			'text': this.transport.responseText,
			'xml': this.transport.responseXML
		};
		this.fireEvent('onSuccess', [this.response.text, this.response.xml,this.attach]);
		this.callChain();
	}

});