NilMapSelect = function (config){
		this.config = config ;
		this.init();
}

NilMapSelect.prototype = {

	init: function(){
		if(this.config.optional){
			this.config.optional.addEvent('dblclick',this.choose.bind(this));
			this.config.optional.addEvent('change',this.updateButton.bind(this));
		}
		if(this.config.assigned){
			this.config.assigned.addEvent('dblclick',this.cancel.bind(this));
			this.config.assigned.addEvent('change',this.updateButton.bind(this));
		}
		this.config.btnSelect.addEvent('click',this.choose.bind(this));
		this.config.btnCancel.addEvent('click',this.cancel.bind(this));

		this.updateButton();
	},
	choose:function(){
		var select = this.config.optional;
		var toselect = this.config.assigned;
		this.moveOptions(select,toselect);

	},
	cancel:function(){
		var select = this.config.assigned;
		var toselect = this.config.optional;
		this.moveOptions(select,toselect);
	},
	moveOptions : function (select,toselect){
		//make data 
		var arr=  [] ;
		for(var i = 0 ; i < select.options.length ; i ++ ){
			if(select.options[i].selected){
				arr.push({text:select.options[i].text,value:select.options[i].value});
			}
		}
		//清理已经删除的
		for(var i = select.options.length -1 ; i >=0 ; i -- ){
			if(select.options[i].selected){
				$(select.options[i]).dispose();
			}
		}

		//放到目标区域
		arr.each( function (e){
			var op = new Element('option',{   
                                     text: e.text,
                                     value: e.value
                });
			toselect.adopt(op);
		});
		this.updateButton();
		

	},
	updateTips:function(){
		//抓出数量
		this._updateTips(this.config.optional,this.config.tipsOptional);
		this._updateTips(this.config.assigned,this.config.tipsAssigned);
	},
	_updateTips:function(sel,tips){
		var count = sel.options.length ;
		var select = sel.getElements("option:selected").length ;
		tips.innerHTML = "数量:"+ count +" 选中:"+select;
	},
	updateButton:function(e){
		var select = this.config.optional.getElements("option:selected").length ;
		this.config.btnSelect.disabled = (select==0);
		var select = this.config.assigned.getElements("option:selected").length ;
		this.config.btnCancel.disabled = (select==0);
		this.updateTips();
	}

}