String.prototype.StartWith=function(s){
	if(s==null||s==""||this.length==0||s.length>this.length)
		return false;
	if(this.substr(0,s.length)==s)
		return true;
	else
		return false;
	return true;
};

String.prototype.EndWith=function(s){
	if(s==null||s==""||this.length==0||s.length>this.length)
		return false;
	if(this.substring(this.length-s.length)==s)
		return true;
	else
		return false;
	return true;
};

function getEvent(){

	if(window.event){
		return window.event;
	}else{
		var e = arguments.callee.caller;
	 	while(e.caller!=null){
			e = e.caller;
	 	}
	  	return e.arguments[0];
	}
};

function bindTableViewHandler(tableclass, _func){
	if(!tableclass){
		return;
	}

	$("table." + tableclass + " tbody tr").each(function(){
	   if($(this).find(':checkbox').length!=0){
		$(this).hover(function(){
			$(this).css('cursor','pointer');
			});
		$(this).find(':checkbox').each(function(){
			$(this).hover(function(){
				$(this).css('cursor','auto');
				});
		});
		}
		$(this).click(function(){
			var args = [$(this)];
			if (typeof _func == 'string') {
				eval(_func);
			} else if (typeof _func == 'function') {
				_func.apply(window, args);
			}

		});
	});
}

if(!window.EGP) window.EGP = {};
if(!EGP.Common) EGP.Common = {};

(function(namespace) {
	namespace._formDialogDefaultOptions = {
    	modal: true,
    	resizable: false,
    	dialogSpace: '<div id="formDialogSpace" style="display:none"></div>',
    	dialogClass: "form-viewer",
    	trigger: "click",
    	refresh: true,
    	open: function(event, ui) {
    		$('.ui-widget-overlay').addClass('form-viewer');

    		var timerid, that = this;
    		var resizeFormFunc = function () {
	            (timerid && clearTimeout(timerid));
	            timerid = setTimeout(function () {
	            	 $(that).dialog("option","width", $(window).width()*3/4)
							.dialog('option', 'height', $(window).height()*3/4)
							.dialog('option', 'position', 'center');
	            }, 200);
	        }
	        $(window).resize(resizeFormFunc);
	        $(this).data("resizeFormFunc", resizeFormFunc);
    	},
    	beforeClose: function() {
    		$('.ui-widget-overlay').removeClass('form-viewer');
    		var resizeFormFunc = $(this).data("resizeFormFunc");
    		if(resizeFormFunc) {
    			$(window).unbind("resize", resizeFormFunc);
    			$(this).data("resizeFormFunc", null);
    		}
    	}
	};
	namespace.formIframeCount = 0;

	namespace.setPreSubmitListener = function(func) {
		this.palFunc = func;
	};

    namespace.afterSetup = function(func) {
        this.afterSetupFunc = func;
    };

	namespace.setPreSubmitListener(function(e) {
	    return true;
    });

	namespace.clearPreSubmitListener = function() {
		this.palFunc = null;
	}

	namespace.enableCloseOfFormDialog = function(enabled) {
		this.formDlgOpts.closeOnEscape = enabled;
		this.formDlgOpts.dialogClass = 'form-viewer' + (enabled ? '' : ' no-close');
	};

	namespace.setupFormDialog = function() {
		var that = this;

		var selector, url, options;
		if(arguments && arguments.length == 1) {
			if(typeof arguments[0] == "object") {
				selector = arguments[0].selector;
				url = arguments[0].url;
				options = arguments[0];
			}
		}else if(arguments && arguments.length == 2) {
			if(typeof arguments[0] == "string" && typeof arguments[1] == "object") {
				selector = arguments[0];
				url = arguments[1].url;
				options = arguments[1];
			}
		}else if(arguments && arguments.length == 3) {
			if(typeof arguments[0] == "string" && typeof arguments[1] == "string" && typeof arguments[2] == "object") {
				selector = arguments[0];
				url = arguments[1];
				options = arguments[2];
			}
		}

		selector = $(selector);
		if(!selector.length) {
			this.setupSelector = null;
			this.setupTrigger = null;
			this.callbackFunc = null;
			return;
		}

		options = $.extend({}, namespace._formDialogDefaultOptions, options);
		var formDialog = $("#formDialogSpace");
		if(formDialog.length == 0) {
			formDialog = $(options.dialogSpace).appendTo("body");
		}

		var clickCount = 0, iframeCount = 0;
		function bindIframe() {
			if(options.refresh ? options.refresh : !clickCount) {
				if(!options.iframe) {
					options.iframe = "iframe_part_" + namespace.formIframeCount++;
				}
				var $iframe = $('<iframe name="' + options.iframe + '"width="100%" height="100%" frameborder="no" scrolling="no"></iframe>');
				$iframe[0].onload = function() {
					$(this).contents().children().css('overflow-x','hidden');
					//console.log("Form iframe load...");
					SOP.Common.hideMask();
				};

				if(!options.formTarget) {
					options.formTarget = options.iframe;
				}
				formDialog.append($iframe);
			}
		}

		window.setFormDlgTitle = function(title) {
			if(!title || title == "null" || title == "undefined") {
				title = "";
			}
			namespace.setOpt4FormDlg('title', title);
		}

		this.formDlgOpts = options;
		var that = this;
		if(this.formDlgOpts.form) {
			var form;
			if(typeof this.formDlgOpts.form == "object") {
				form = $(this.formDlgOpts.form);
			}else if(typeof this.formDlgOpts.form == "string"){
				form = $("form");
			}
			if(form && form.length) {
				var originalTarget = form.attr("target");
				var setupCallback = function(e) {
					if (!that.palFunc || that.palFunc(e)) {
						e.preventDefault();
						bindIframe();
						that.formDlgOpts.width = $(window).width() * 3 / 4;
						that.formDlgOpts.height = $(window).height() * 3 / 4;
						formDialog.dialog(that.formDlgOpts);
						if(that.formDlgOpts.refresh) {
							form.attr("target", that.formDlgOpts.formTarget).submit()
								.attr("target", originalTarget?originalTarget:"");
							formDialog.dialog("option", "close", function() {$(this).find("iframe").remove();});
						}else if(!clickCount) {
							form.attr("target", that.formDlgOpts.formTarget).submit()
								.attr("target", originalTarget?originalTarget:"");
							clickCount ++;
						}
						return true;
					}

					return false;
				};
				selector.bind(this.formDlgOpts.trigger, setupCallback);
				this.setupSelector = selector;
				this.setupTrigger = this.formDlgOpts.trigger;
				this.setupCallback = setupCallback;
			}else {
				alert("Can't find form.");
			}
		}else {
			var setupCallback = function(e) {
				if (!that.palFunc || that.palFunc(e)) {
					e.preventDefault();
					bindIframe();
					that.formDlgOpts.width = $(window).width() * 3 / 4;
					that.formDlgOpts.height = $(window).height() * 3 / 4;
					formDialog.dialog(that.formDlgOpts);

					if(that.formDlgOpts.refresh) {
						if(url) {
							formDialog.find("iframe").attr("src", url);
						}
						formDialog.dialog("option", "close", function() {$(this).find("iframe").remove();});
						clickCount ++;
					}else if(!clickCount) {
						if(url) {
							formDialog.find("iframe").attr("src", url);
						}
						clickCount ++;
					}
					return true;
				}

				return false;
			};
			selector.bind(this.formDlgOpts.trigger, setupCallback);
			this.selector = selector;
			this.setupTrigger = this.formDlgOpts.trigger;
			this.setupCallback = setupCallback;
		}
	};

	namespace.unsetupFormDialog = function() {
		if (this.setupSelector && this.setupTrigger && this.setupCallback)
			this.setupSelector.unbind(this.setupTrigger, this.setupCallback);
	};

	namespace.setupFormDIV = function(formElement, targetSelector, options) {
		this.setSetupModeDIV(true);
		var html = '<iframe id="__egovform-iframe" name="__egovform-iframe" frameborder="no"';
		if (options.width && $.trim(options.width).length > 0)
			html += ' width="' + options.width + '"';
		if (options.height && $.trim(options.height).length > 0) {
			this.setAutoHeightMode(0);
			html += ' scrolling="yes" height="' + options.height + '"';
		} else {
			html += ' scrolling="no"';
			this.setAutoHeightMode(1);
		}
		var $iframe = $(html + '></iframe>');
		$iframe.bind("load", function(){
			SOP.Common.hideMask();
		})
		$(targetSelector).append($iframe);
		formElement.target = '__egovform-iframe';
		var that = this;
        if(this.afterSetupFunc) {
            that.afterSetupFunc();
        }
	};

	namespace.setAutoHeightMode = function(mode) {
		this.__autoHeightMode = mode;
	};

	namespace.getAutoHeightMode = function() {
		return typeof this.__autoHeightMode == 'undefined' ? "" : this.__autoHeightMode;
	};

	namespace.setSetupModeDIV = function(value) {
		this.__setupModeDIV = value;
	};

	namespace.isSetupModeDIV = function() {
		return typeof this.__setupModeDIV == 'undefined' ? false : this.__setupModeDIV;
	};

	namespace.onEgovFormReady = function(contentHeight) {
		if (typeof this.__autoHeightMode != 'undefined' && this.__autoHeightMode == 1)
			$('#__egovform-iframe').height(contentHeight);
		$(document).trigger('eGovFormReady', [contentHeight]);
	};

	namespace.onFrameDocumentChange = function(contentHeight) {
		var events = $(document).data("events");
		if(events && events['eGovFormFrameDocumentChange']) {
			$(document).trigger('eGovFormFrameDocumentChange', [contentHeight]);
		}
		if($("#__egovform-iframe").length) {
			$('#__egovform-iframe').height(contentHeight);
		}

	};

	namespace.onEgovFormWizardPageChange = function(contentHeight) {
		var events = $(document).data("events");
		if(events && events['eGovFormWizardPageChange']) {
			$(document).trigger('eGovFormWizardPageChange', [contentHeight]);
		}
		if($("#__egovform-iframe").length) {
			$('#__egovform-iframe').height(contentHeight);
			window.scrollTo(0, $("#__egovform-iframe").position().top -50);
		}
	};

	namespace.setOpt4FormDlg = function(optName, optValue) {
		$("#formDialogSpace").dialog('option', optName, optValue);
	};

	namespace.setAutoWidth4Selector = function(selector) {
		if(!selector) {
			selector = "select.autoOptWidth";
		}
		if(window.navigator.userAgent.match(/MSIE (8.0)|(7.0)/)){
			$(document).ready(function() {
				$(selector).each(function() {
					el = $(this);
					var width = el.width();
					var paddingLeft  = parseInt(el.css('padding-left' ), 10);
					var paddingRight = parseInt(el.css('padding-right'), 10);
					var borderLeft  = parseInt(el.css('border-left-width' ), 10);
					var borderRight = parseInt(el.css('border-right-width'), 10);
					var totalWidth = width
							+ (isNaN(paddingLeft)?0:paddingLeft)
							+ (isNaN(paddingRight)?0:paddingRight)
							+ (isNaN(borderLeft)?0:borderLeft)
							+ (isNaN(borderRight)?0:borderRight);
					el.data("origWidth", totalWidth);
				})
				.bind("focusin", function(){
					el=$(this);
					el.css("cssText","width: auto !important");
					if(el.width() < el.data("origWidth")){
						el.css("cssText", "width:" + el.data("origWidth") + "px !important");
					}
				})
				.bind("blur change ", function(){
					el = $(this);
					el.css("cssText", "width:" + el.data("origWidth") + "px");
					$("body")[0].focus();
				});
			});
		}
    };

    namespace.getLocation = function() {
    	/**
        var options={
            enableHighAccuracy:true,
            maximumAge:1000
        }

        var onSuccess = function(position){
            var longitude =position.coords.longitude;
            var latitude = position.coords.latitude;
            var $lo = $('form').find('[name="egp_longitude"]');
            if(!$lo[0]){
                $('form').append('<input type="hidden" name="egp_longitude" value="'+longitude+'"/>');
            }

            var $la = $('form').find('[name="egp_latitude"]');
            if(!$la[0]){
                $('form').append('<input type="hidden" name="egp_latitude" value="'+latitude+'"/>');
            }
        }

        var onError = function(error){

        }
        if(navigator.geolocation){
            navigator.geolocation.getCurrentPosition(onSuccess,onError,options);
        }
        */
	}
})(EGP.Common);


(function($){
    $.fn.extend({
        limit: function(limit,element) {

			var interval, f;
			var self = $(this);

			$(this).focus(function(){
				interval = window.setInterval(substring,100);
			});

			$(this).blur(function(){
				clearInterval(interval);
				substring();
			});

			substringFunction = "function substring(){ var val = $(self).val();var length = val.length;if(length > limit){$(self).val($(self).val().substring(0,limit));}";
			if(typeof element != 'undefined')
				substringFunction += "$(element).html((limit-length<=0)?'0':limit-length);"

			substringFunction += "}";

			eval(substringFunction);

			substring();

       }
   });
})(jQuery);

