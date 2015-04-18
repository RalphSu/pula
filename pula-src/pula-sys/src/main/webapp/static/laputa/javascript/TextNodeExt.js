YAHOO.widget.TextNodeExt = function(oData, oParent, expanded) {

    if (oData) { 
        this.init(oData, oParent, expanded);
        this.setUpLabel(oData);
    }

};

YAHOO.extend(YAHOO.widget.TextNodeExt, YAHOO.widget.TextNode, {
    
   	hasChildren: function(b){

		//alert(b);
		return false;
	},
    getToggleLink: function() {
        return "selectNode(\'" + this.data.no + "\')";
    }

});