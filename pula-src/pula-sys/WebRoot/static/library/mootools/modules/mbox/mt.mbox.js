/*
 * mt.mbox.js 
 * by zhangxinxu 2010-11-16
 * mbox mootools下的弹框插件
 * demo http://www.zhangxinxu.com/study/201011/mt-mbox-dialog-test.html
*/
//框架层的显示，此仅在IE6下生效
 var IframeShim = new Class({
 	Implements: [Options, Events],
 	options: {
 		name: '',
 		className: 'iframeShim',
 		display: false,
 		zIndex: null,
 		margin: 0,
 		offset: {
 			x: 0,
 			y: 0
 		},
 		browsers: (Browser.Engine.trident4 || (Browser.Engine.gecko && !Browser.Engine.gecko19 && Browser.Platform.mac)), //Browser.Engine.trident4 表示IE6
 		onInject: $empty
 	},
 	initialize: function(element, options) {
 		this.setOptions(options);
 		//legacy
 		if (this.options.offset && this.options.offset.top) this.options.offset.y = this.options.offset.top;
 		if (this.options.offset && this.options.offset.left) this.options.offset.x = this.options.offset.left;
 		this.element = $(element);
 		this.makeShim();
 		return;
 	},
 	makeShim: function() {
 		this.shim = new Element('iframe');
 		this.id = this.options.name || new Date().getTime() + "_shim";
 		if (this.element.getStyle('z-Index').toInt() < 1 || isNaN(this.element.getStyle('z-Index').toInt()))
 			this.element.setStyle('z-Index', 10);
 		var z = this.element.getStyle('z-Index') - 1;

 		if ($chk(this.options.zIndex) && this.element.getStyle('z-Index').toInt() > this.options.zIndex)
 			z = this.options.zIndex;

 		this.shim.setStyles({
 			'position': 'absolute',
 			'zIndex': z,
 			'border': 'none',
 			'filter': 'progid:DXImageTransform.Microsoft.Alpha(style=0,opacity=0)'
 		}).setProperties({
 			'src': 'javascript:void(0);',
 			'frameborder': '0',
 			'scrolling': 'no',
 			'id': this.id
 		}).addClass(this.options.className);

 		this.element.store('shim', this);

 		var inject = function() {
 			this.shim.inject(document.body);
 			if (this.options.display) this.show();
 			else this.hide();
 			this.fireEvent('inject');
 		};
 		if (this.options.browsers) {
 			if (Browser.Engine.trident && !IframeShim.ready) {
 				window.addEvent('load', inject.bind(this));
 			} else {
 				inject.run(null, this);
 			}
 		}
 	},
 	position: function(obj) {
 		if (!this.options.browsers || !IframeShim.ready) return this;
 		if (obj) {
 			this.shim.setStyles({
 				width: obj.width,
 				height: obj.height,
 				top: obj.top,
 				left: obj.left
 			});
 		 }
 		else { 
 			var before = this.element.getStyles('display', 'visibility', 'position');
 			this.element.setStyles({
 				display: 'block',
 				position: 'absolute',
 				visibility: 'hidden'
 			});
 			var size = this.element.getSize();
 			var pos = this.element.getPosition();
 			this.element.setStyles(before);
 			if ($type(this.options.margin)) {
 				size.x = size.x - (this.options.margin * 2);
 				size.y = size.y - (this.options.margin * 2);
 				this.options.offset.x += this.options.margin;
 				this.options.offset.y += this.options.margin;
 			}

 			this.shim.setStyles({
 				width: size.x,
 				height: size.y,
 				top: pos.y,
 				left: pos.x
 			});
 		}
 		return this;
 	},
 	hide: function() {
 		if (this.options.browsers) this.shim.setStyle('display', 'none');
 		return this;
 	},
 	show: function(obj) {
 		if (!this.options.browsers) return this;
 		this.shim.setStyle('display', 'block');
 		return this.position(obj);
 	},
 	dispose: function() {
 		if (this.options.browsers) this.shim.dispose();
 		return this;
 	}
 });
window.addEvent('load', function(){
	IframeShim.ready = true;
});



//overlay class, 这个就是弹出框的给色半透明背景层
var Overlay = new Class({
	Implements: [Options, Events],
	getOptions: function() {
		return {
			useFx: false,
			name:'',
			duration: 200,
			colour: '#000',
			opacity: 0.2,
			zIndex: 99,
			hasShim: true,
			container: document.body,
			onClick: $empty
		};
	},

	initialize: function(options) {
		this.setOptions(this.getOptions(), options);
		this.options.container = $(this.options.container);

		this.container = new Element('div').setProperty('id', this.options.name+'_overlay').setStyles({
			position: 'absolute',
			left: '0',
			top: '0',
			width: '100%',
			height: '100%',
			backgroundColor: this.options.colour,
			zIndex: this.options.zIndex,
			opacity: this.options.opacity
		}).inject(this.options.container);


		if (this.options.hasShim) this.shim = new IframeShim(this.container);
		this.options.useFx ? this.fade = new Fx.Tween(this.container, { property: 'opacity', duration: this.options.duration }).set(0) : this.fade = null;
		this.container.setStyle('display', 'none');

		this.container.addEvent('click', function() {
			this.fireEvent('click');
		} .bind(this));

		window.addEvent('resize', this.position.bind(this));
		return this.position();
	},

	position: function() {
		if (this.options.container == document.body) {
			var h = window.getScrollHeight() + 'px';
			this.container.setStyles({ top: '0px', height: h });
		} else {
			var myCoords = this.options.container.getCoordinates();
			this.container.setStyles({
				top: myCoords.top + 'px',
				height: myCoords.height + 'px',
				left: myCoords.left + 'px',
				width: myCoords.width + 'px'
			});
		}
	},
	//调用此方法可以直接显示黑色半透明的覆盖层，默认层级99，opacity为0.2
	show: function() { 
		this.container.setStyle('display', '');
		if (this.fade) this.fade.cancel().start(this.options.opacity);
		if (this.shim) this.shim.show();
		return this.position();
	},

	hide: function(dispose) {
		if (this.fade) this.fade.cancel().start(0);
		this.container.setStyle('display', 'none');
		if (this.shim) this.shim.hide();
		if (dispose) this.dispose();
		return this;
	},

	dispose: function() {
		this.container.dispose();
		if (this.shim) this.shim.dispose();
	}

});

var Mbox = {
	presets: {
		marginImage: { x: 50, y: 75 },
		width: 'auto',
		height: 'auto',
		
		url: false, //可以是元素，字符串
		type: 'ele', //ajax, string, iframe, image
		ajax: false,
		ajaxAttr: "href",
		ajaxOptions: {},
		
		title: '提示框',
		ensure: '确定',
		cancel: '取消',
		clostxt: '×',
		loadimg: '../../static/laputa/images/loading.gif',

		overlay: true,
		opacity: 0.35,
		hasShim: true,
		overlayClosable: false,
		
		reposition: true,
		
		//关闭按钮显示
		closable: true,
		titleable: true,
		optable: false,
		zIndex: 999,
		
		time: 0,

		useFx: false,
		openFrom: null,
		resizeFx: {},

		onShow: $empty,
		onClose: $empty
	},	
	//初始化
	initialize: function(presets) {
		this.options = {};
		this.setOptions(this.presets, presets || {});
		this.build();
		this.bound = {
			window: this.reposition.bind(this, [null]),
			close: this.close.bind(this),
			key: this.onKey.bind(this)
		};
		
		this.isOpen = this.elementObj = false;
		
		return this;
	},
	//构建
	build: function() {
		if (!this.overlay) {
			this.overlay = new Overlay({ name: 'mbox', hasShim: this.options.hasShim, opacity: this.options.opacity, onClick: (this.options.overlayClosable) ? this.close.bind(this) : null });	
		}
		if (!$("mboxWindow")) {
			//页面上无mbox元素
			var boxConstr = '<div id="mboxBar" class="mbox_bar">'+this.options.title+'</div><a href="javascript:;" id="mboxBtnClose" class="mbox_close" title="关闭">'+this.options.clostxt+'</a><div id="mboxContent" class="mbox_cont"></div><div id="mboxOperate" class="mbox_operate" style="display:none;"></div>';
			var oOpen = this.options.openFrom, t, w, l;
			if ($type(oOpen) === "element" && oOpen) {
				t = oOpen.getPosition().y, l = oOpen.getPosition().x, w = oOpen.getSize().x;
			} else {
				t = 250, w = 400, l = (window.getSize().x - w)	 / 2;
			}
				
			this.win = new Element('div', {
				id: 'mboxWindow',
				'class': 'mbox_win',
				styles: {
					width: w,
					top: t,
					left: l,
					position: 'absolute',
					zIndex: this.options.zIndex + 2,
					visibility: 'hidden'
				}
			}).set("html", boxConstr);
			
			document.body.adopt(this.win);
		}
		
		this.bar = $("mboxBar");
		this.closbtn = $("mboxBtnClose");
		this.cont = $("mboxContent");
		this.opt = $("mboxOperate");		
		
		return this;
	},
	
	assign: function(to, options) {
		to.addEvent('click', function(e) {
			//例如阻止链接的默认行为
			new Event(e).stop();
			Mbox.open(options, this);
		});
	},
	
	open: function(options, from) {
		options  = options || {},
		this.initialize(options);
		
		if ($type(from) === "element") {
			this.element = $(from);
			this.options.url = this.element.getProperty(this.options.ajaxAttr) || this.options.url;
		}
		//alert('open:'+this.options.title);
		this.getContent(this.options.content || null);
	},
	remind: function(message, options) {
		if (message) {
			//参数还原
			this.initParams();
			options = options || {};
			this.initialize(options);
			this.getContent('<div class="mbox_remind">'+message+'</div>');
			this.closbtn.setStyle('visibility', 'hidden');
			this.bar.setStyle('display', 'none');
			this.opt.setStyle('display', 'none');
			
			var time = this.options.time.toInt();
			if (time > 0) {
				this.close.delay(time, this);
			}
		}
		return this;
	},
	alert: function(message, surecall, options) {
		if (!message) {
			return this;	
		}
		//参数还原
		this.initParams();
		
		options = options || {};
		this.initialize(options);
		this.getContent('<div class="mbox_alert">'+message+'</div>');
		this.opt.setStyle('display', 'block');
		this.bar.setStyle('display', 'block');
		
		this.AlertBtnOk = new Element('input', {
			'id': 'mboxAlertOk',
			'class': 'mbox_btn_sure',
			'type': 'submit',
			'value': this.options.ensure
		});
		this.AlertBtnOk.addEvent('click', function() {
			this.close();
			if ($type(surecall) === 'function') {
				surecall.call(this);	
			}
		} .bind(this));
		
		this.opt.empty().adopt(this.AlertBtnOk);
		
		if ($("mboxAlertOk") && $("mboxAlertOk").getStyle("display") !== "none") {
			$("mboxAlertOk").focus();
		}
	},
	confirm: function(message, surecall, cancelcall, options) {
		if (!message) {
			return this;	
		}
		//参数还原
		this.initParams();
		
		options = options || {};
		this.initialize(options);
		this.getContent('<div class="mbox_alert">'+message+'</div>');
		this.opt.setStyle('display', 'block');
		this.bar.setStyle('display', 'block');
		
		this.ConfirmBtnOk = new Element('input', {
			'id': 'mboxConfirmOk',
			'class': 'mbox_btn_sure',
			'type': 'submit',
			'value': this.options.ensure
		});
		this.ConfirmBtnCancel = new Element('input', {
			'id': 'mboxConfirmCancel',
			'class': 'mbox_btn_cancel',
			'type': 'button',
			'value': this.options.cancel
		});
		this.opt.empty().adopt(this.ConfirmBtnOk, this.ConfirmBtnCancel);
		this.ConfirmBtnOk.addEvent('click', function() {
			if ($type(surecall) === 'function') {
				surecall.call(this);	
			}
		} .bind(this));
		this.ConfirmBtnCancel.addEvent('click', function() {
			this.close();	
			if ($type(cancelcall) === 'function') {
				surecall.call(this);
			}
		} .bind(this));
		
		if ($("mboxConfirmOk") && $("mboxConfirmOk").getStyle("display") !== "none") {
			$("mboxConfirmOk").focus();
		}
	},
	loading: function(message) {
		//this.initialize({});
		message = message || '<div class="mbox_loading"><img src="'+this.options.loadimg+'" class="mbox_loading_image" />加载中...</div>';
		this.getContent(message);
		this.closbtn.setStyle('visibility', 'hidden');
		this.bar.setStyle('display', 'none');
		this.opt.setStyle('display', 'none');
		return this;
	},
	
	close: function() {
		if (this.isOpen) {
			this.isOpen = false;
			//执行关闭回调
			if ($type(this.options.onClose) === "function") {
				this.options.onClose.call(this);	
			}
			
			//透明背景层
			if (this.overlay && this.options.overlay) {
				this.overlay.dispose();
				this.overlay = null;
			}

			//保护装载元素
			if (this.elementObj) {
				document.body.adopt(this.elementObj.setStyle('display', 'none'));
			}
			//内容清空
			this.cont.empty();
			this.opt.empty();
			this.win.setStyle('visibility', 'hidden');
			this.closbtn.setStyle('visibility', 'hidden');
			
			if (this.element) {
				this.element = null;
			}
			
			//参数还原
			this.initParams();
			//移除事件
			this.toggleListeners();
			this.options.onShow = $empty;
			this.options.onClose = $empty;

			//清除动画
			if (this.fx) {
				this.fx = null;	
			}
		}
		return false;
	},
	initParams: function() {
		//参数还原
		this.options = {};
		this.setOptions(this.presets);
	},
	
	toggleListeners: function(state) {
		var fn = (state) ? 'addEvent' : 'removeEvent';
		this.closbtn[fn]('click', this.bound.close);
		document[fn]('keydown', this.bound.key);
		if (this.options.reposition) {
			window[fn]('resize', this.bound.window)[fn]('scroll', this.bound.window);
		}
	},
	
	getContent: function(content) {
		this.toggleListeners(true);
		if (content) {

			return this.applyContent(content);	
		}
		var type = this.options.type, url = this.options.url, x = Number(this.options.width) || 600, y = Number(this.options.height) || 400;
		//alert('hoho:'+this.options.title);
		if (!url) {
			return false;	
		}
		if (this.options.ajax) {
			this.loading();
			//页面外元素
			switch (type) {
				case 'image': {
					var tempImage = new Image(), imgsrc = url;
					tempImage.onload = function() {
						//图片与浏览器窗口大小比对显示
						var box = document.getSize(), size;
						box.x -= this.options.marginImage.x;
						box.y -= this.options.marginImage.y;
						size = { x: tempImage.width, y: tempImage.height };
	
						for (var i = 2; i--; ) {
							if (size.x > box.x) {
								size.y *= box.x / size.x;
								size.x = box.x;
							} else if (size.y > box.y) {
								size.x *= box.y / size.y;
								size.y = box.y;
							}
						}
						size.x = size.x.toInt();
						size.y = size.y.toInt();
						this.applyContent('<img class="mbox_ajax_image" src="'+imgsrc+'" width="'+size.x+'" height="'+size.y+'" />');
					}.bind(this);
					tempImage.onerror = function() {
						this.alert("图片加载没有成功！");
					}.bind(this);
					tempImage.src = imgsrc;
					break;
				}
				case 'iframe': {
					var iframeOptions = {
						src: url,
						styles: {
							width: x,
							height: y,
							background: 'url('+this.options.loadimg+') no-repeat center',
							border: 0
						},
						'class': 'mbox_ajax_iframe',
						frameborder: 0
					};
					//alert('hohoo,iframe');
					this.applyContent(new IFrame(iframeOptions));
					break;	
				}
				case 'ajax': {
					var self = this;
					new Request.HTML($merge({
						method: 'get'
					}, this.options.ajaxOptions)).addEvents({
						onFailure: this.onError.bind(this),
						onComplete: function(a, b, c, d) {
							self.applyContent(c);
						}
					}).send({ 'url': url });	
					break;
				}
				case 'swf': {
					var swfHtml = '<object classid="clsid:d27cdb6e-ae6d-11cf-96b8-444553540000" width="'+x+'" height="'+y+'" codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=6,0,40,0"><param name="quality" value="high" /><param name="src" value="'+url+'" /><param name="wmode" value="transparent" /><embed type="application/x-shockwave-flash" wmode="transparent" width="'+x+'" height="'+y+'" src="'+url+'" quality="high"></embed></object>';
					this.applyContent(swfHtml);
					break;
				}
				default: {
					return this.applyContent(url);	
				}
			}
		} else {
			//页面上元素
			if ($type(url) === "string") {
				url = url.replace(/^#/, "");
				if ($(url)) {
					url = $(url);	
				}
			}
			return this.applyContent(url);	
		}
	},
	
	applyContent: function(content) {
		var position = this.cont.getStyle("position");	
		if ($type(content) === "string") {
			this.cont.set("html", content).setStyle("position", "absolute");	
		} else if ($type(content) === "element") {
			this.elementObj = content;
			this.cont.empty().adopt(content.setStyle("display", "block")).setStyle("position", "absolute");
		} else {
			return this;	
		}
		
		this.winsize = this.cont.getSize();
		this.cont.setStyle("position", position);
		//alert('now:'+this.options.title);
		this.bar.set("html", this.options.title);
		this.closbtn.set("html", this.options.clostxt);
		
		this.showControl();
		this.reposition();	
		this.win.setStyle("visibility", "visible");
		this.isOpen = true;
		//执行显示回调
		if ($type(this.options.onShow) === "function") {
			this.options.onShow.call(this);	
		}
		return this;
	},
	
	showControl: function() {
		//是否显示关闭按钮
		if (this.options.closable) {
			this.closbtn.setStyle('visibility', 'visible');
		} else {
			this.closbtn.setStyle('visibility', 'hidden');
		}
		//半透明背景层
		if (this.overlay && this.options.overlay) {
			this.overlay.show();
		}
		//是否显示标题栏，默认显示
		if (this.options.titleable) {
			this.bar.setStyle('display', 'block');
		} else {
			this.bar.setStyle('display', 'none');
		}
		//是否显示按钮，默认不显示
		if (this.options.optable) {
			this.opt.setStyle('display', 'block');
		} else {
			this.opt.setStyle('display', 'none');
		}	
		
	},
	//定位
	reposition: function(fx) {
		var d = document, w = window, box = this.win, wsize = w.getSize();
		if(this.winsize==null) return ;
		var winx = this.winsize.x || box.getSize().x, winy = box.getScrollSize().y, ie6 = (w.XMLHttpRequest) ? false: true, windowx = wsize.x, windowy = wsize.y, docsc = d.getScroll().y, c = winy < windowy, posl, post, w = this.options.width.toInt(), h = this.options.height.toInt();
		
		winy = h || winy, winx = w || winx;
		//高度定值，避免IE下动画报错
		box.setStyle("height", winy);
		
		posl = (windowx - winx) / 2;
		post = (windowy - winy) / 2 + docsc;
		
		if (ie6 || !this.options.reposition) {
			//高度不正常
			post = c? ((windowy - winy) / 2 + docsc) : (50 + docsc);
		} else {
			post = c? ((windowy - winy) / 2) : 50;	
			box.setStyle("position", "fixed");
		}
		
		var to = {
			width: winx,
			height: winy,
			left: posl,
			top: post		
		};	
		
		if (this.options.useFx || fx === true) {
			this.fx = {
				win: new Fx.Morph(this.win, $merge({
					duration: 750,
					transition: Fx.Transitions.Quint.easeOut,
					link: 'cancel',
					unit: 'px',
					onComplete: function() {
						if (!h) {
							box.setStyle("height", "auto");		
						}
					}
				}, this.options.resizeFx))
			};
		}
		
		//IE6下动画效果不佳，拜拜的说
		if (this.fx && !ie6) {
			this.fx.win.cancel().start(to);
		} else {
			box.setStyles(to);
			if (!h) {
				box.setStyle("height", "auto");		
			}
		}
		
	},
	
	onError: function() {
		this.alert('加载出了点小问题。');
	},
	onKey: function(e) {
		switch (e.key) {
			case 'esc': if (this.closbtn.getStyle("visibility") === "visible") { this.close(e); }
			case 'up': case 'down': return false;
		}
	},
	extend: function(properties) {
		return $extend(this, properties);
	}
};
Mbox.extend(new Options);