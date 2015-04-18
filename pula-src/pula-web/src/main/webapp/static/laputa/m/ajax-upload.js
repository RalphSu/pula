 var AjaxUpload = new Class({
      initialize: function(form,url,data){
         this.form = $(form);
         if(this.form.get('tag')!='form'){
             alert('is not form');
             this.form = null;
             return this;
      }
      if(this.form.encoding){
          this.form.setAttribute('encoding','multipart/form-data');
      }else{
          this.form.setAttribute('enctype','multipart/form-data');
         }
         this.form.setAttribute('method','post');
         this.form.getAttribute('action')||this.form.Attribute('action',url);
         this.processId = 'ajaxupload_'+Math.floor(Math.random()*10000);
         this.target = (new Element('iframe',{
              styles:{
                  width:0,
                  height:0,
                  border:0,
                  visibility:'hidden',
                  position:'absolute'
             },
              name: this.processId,
              id: this.processId,
              src:   window.ie ? 'javascript:false' : 'about:blank'
         })).inject(document.body);
         this.form.setProperty('target',this.processId);
         this.appendElements = [];
         this.appendElements.push(this.target);
         this.appendData({ajax:1});
         this.appendData(data);
     },
      appendData:function(data){
        if($type(data)=='string'){
            var value = data.split('&');
            data = {};
            for(var i=0; i < value.length; i++){
                var pos = value[i].indexOf('=');
                data[value[i].substring(0,pos)] = value[i].substring(pos+1);
            }
        }
        for(var key in data){
             this.appendElements.push((new Element('input',{
                  type:'hidden',
                  name:key,
                  value:data[key]
             })).inject(this.form));
         }
     },
      upload:function(){
        try{
            this.form.submit();
        }catch(e){
            return this.onError(e);
        }
        this.timer = (function(){
            try{
                //var doc = this.target.contentDocument||document.frames[this.processId].document;
                var doc = this.target.contentWindow.document||this.target.contentDocument;
                if(!doc) return;
				var body = doc.body?doc.body:doc.documentElement;
				if( ! body){
					return ;
				}
                var location = window.opera ? this.target.location : doc.location;
            }catch(e){
                this.timer = $clear(this.timer);
                return this.onError(e);
            }
            if(!location||location=='about:blank'){
                return;
            }else{
                this.timer = this.clear(this.timer);
                this.responseText = doc.body?doc.body.innerHTML:doc.documentElement.textContent;
                this.responseXML   = doc.XMLDocument||doc;
                this.fireEvent('onFinish',[this.responseText,this.responseXML]);
                this.cancel();
            }
        }).periodical(window.opera ? 150 : 100,this);
        return this;
     },
      onError:function(error){
         this.fireEvent('onError',error);
         this.fireEvent('onFinish',null);
         this.cancel();
         return this;
     },
      cancel:function(){
         this.clear(this.timer);
         this.target.location = 'about:blank';
         this.appendElements.forEach(function(el){el.dispose();});
     },
	clear:function(v){
		clearTimeout(v);
	 }
 });
 AjaxUpload.implement(new Events);
 Element.extend({
      upload:function(){
         return new AjaxUpload(this).upload();
     }
 });