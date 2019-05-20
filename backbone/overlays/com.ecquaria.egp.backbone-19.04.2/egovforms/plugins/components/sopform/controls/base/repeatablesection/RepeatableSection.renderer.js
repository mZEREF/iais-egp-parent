(function($jq) {

var FQN = 'sopform.controls.base.RepeatableSection';

var RepeatableSectionRendererCommon = {
}

var RepeatableSectionRenderer = Form.Design.AbstractRenderer.extend($jq.extend({
	tmpl: Form.getTemplate(FQN),

	propertiesTemplate: Form.getTemplate(FQN, 'properties'),

	renderBody: function(control, placeholder) {
		// runtime implementation exists - do changes there too.
		this.renderElementCommon(placeholder, this.tmpl,
				{ control: control });
	},

	prepareControl: function(control) {
		this.base(control);
		var elem = control.getElement();
		var handle = $jq('<div class="section-handle"><div><img class="section-select" src="' + sopFormTheme + 'images/general/accept.gif" title="select" /></div></div>');
		handle.prependTo($jq(elem));
		handle.click(function(e) {
			e.stopPropagation();
			Form.Design.choose(elem);
		});
		var $table = control.getGrid();
		for (var i = 0, n = $table.length; i < n; i++) {
			Form.makeSortableDroppableGrid3($table.get(i), control.properties.cols, control.properties.width);
		}
	},

	renderPropertiesBody: function(control, placeholder) {
		var input;
		var properties = this.base.apply(this, arguments);

		// 1
		input = Form.Common.createFromTemplate(Form.Properties.textbox, {
			id: control.properties.id,
			name: 'name',
			value: control.properties.name
		}, null, {
			blur: function(e) {
				control.properties.name = $jq(this).val();
			}
		});

		properties['name'] = input[0];

		// 2
		input = Form.Common.createFromTemplate(Form.Properties.textbox, {
			id: control.properties.id,
			name: 'title',
			value: control.properties.title
		}, null, {
			'keydown paste': function(e) {
				Form.Common.invokeLater(function(e, control) {
					var title = control.properties.title = $jq(this).val();
					var $title = $jq('div.repeatable-section-header:eq(0)', control.getElement());
					
					if ($title && $title.length && Form.getControl($title.get(0)) == control) {
						$title.text(title);
					}
				}, { context: this, uid: 'changeProperty' }, e, control);
			}
		});

		properties['title'] = input[0];
		
		// 3
		input = Form.Common.createFromTemplate(Form.Properties.textbox, {
			id: control.properties.id,
			name: 'cols',
			value: control.properties.cols
		}, null, {
			'keydown paste': function(e) {
				Form.Common.invokeLater(function(e, control) {
					control.properties.cols = Form.Util.toInteger($jq(this).val(), null);
					control.refresh();
					Form.Design.choose(control.getElement(), false);
				}, { context: this, uid: 'changeProperty' }, e, control);
			}
		});

		properties['cols'] = input[0];

		// 3a
		input = Form.Common.createFromTemplate(Form.Properties.textbox, {
			id: control.properties.id,
			name: 'repeat',
			value: control.properties.repeat
		}, null, {
			'keydown paste': function(e) {
				Form.Common.invokeLater(function(e, control) {
					control.properties.repeat = Form.Util.toInteger($jq(this).val(), null);
				}, { context: this, uid: 'changeProperty' }, e, control);
			}
		});

		properties['repeat'] = input[0];

		// 3b
		input = Form.Common.createFromTemplate(Form.Properties.textbox, {
			id: control.properties.id,
			name: 'controlButton',
			value: control.properties.controlButton
		}, null, {
			'keydown paste': function(e) {
				Form.Common.invokeLater(function(e, control) {
					var controlButton = control.properties.controlButton = $jq(this).val();
					$('#' + control.getElementId() + '--add-row').val(controlButton);
				}, { context: this, uid: 'changeProperty' }, e, control);
			}
		});

		properties['controlButton'] = input[0];

		// 4
		input = Form.Common.createFromTemplate(Form.Properties.radio, {
			id: control.properties.id,
			name: 'captionOrientation',
			value: control.properties.captionOrientation,
			options: [
				['horizontal', 'Horizontal'],
				['vertical', 'Vertical']
			]
		});
		
		var captionOrientation = input;
		SOP.SOPForms.setupRadio(captionOrientation);
		$jq('input[type=button]', input).click(function(e) {
			control.properties.captionOrientation = $jq(this).val();
			control.refresh();
			Form.Design.choose(control.getElement(), false);
		});

		properties['captionOrientation'] = input[0];
		
		
		// 5
		input = Form.Common.createFromTemplate(Form.Properties.radio, {
			id: control.properties.id,
			name: 'type',
			value: control.properties.type,
			options: [
				['vertical', 'Vertical'],
				['pagination', 'Pagination']
			]
		});
		
		var type = input;
		SOP.SOPForms.setupRadio(type);
		$jq('input[type=button]', input).click(function(e) {
			control.properties.type = $jq(this).val();
			control.refresh();
			Form.Design.choose(control.getElement(), false);
		});

		properties['type'] = input[0];
		
		// 5
//		input = Form.Common.createFromTemplate(Form.Properties.radio, {
//			id: control.properties.id,
//			name: 'border',
//			value: '' + control.properties.border,
//			options: [
//				['true', 'True'],
//				['false', 'False']
//			]
//		});
//
//		var border = input;
//		SOP.SOPForms.setupRadio(border);
//		$jq('input[type=button]', input).click(function(e) {
//			control.properties.border = $jq(this).val() == 'true';
//			control.refresh();
//			Form.Design.choose(control.getElement(), false);
//		});
//
//		properties['border'] = input[0];

		// 6
		input = Form.Common.createFromTemplate(Form.Properties.textbox, {
			id: control.properties.id,
			name: 'width',
			value: control.properties.width
		}, null, {
			blur: function(e) {
				control.properties.width = $jq(this).val();
				control.refresh();
				Form.Design.choose(control.getElement(), false);
			}
		});

		properties['width'] = input[0];

		// 6
		input = Form.Common.createFromTemplate(Form.Properties.textbox, {
			id: control.properties.id,
			name: 'hint',
			value: control.properties.hint
		}, null, {
			blur: function(e) {
				control.properties.hint = $jq(this).val();
			}
		});

		properties['hint'] = input[0];

		// 7
		input = Form.Common.createFromTemplate(Form.Properties.textarea, {
			id: control.properties.id,
			name: 'help',
			value: control.properties.help
		}, null, {
			blur: function(e) {
				control.properties.help = $jq(this).val();
			}
		});

		properties['help'] = input[0];
		
		// for lock field
		input = Form.Common.createFromTemplate(Form.Properties.checkbox, 
				componentLockManager.constructArguments(control));
		properties['component_lock'] = input[0];
		componentLockManager.setupTriggers(input, control);
		
		// for developer properties
		input = Form.Common.createFromTemplate(Form.Properties.developer,
				developerManager.constructArguments(control), null, null);
		developerManager.setupTriggers(input, control);
		properties['developer-properties'] = input[0];
		
		// for condition properties
		input = Form.Common.createFromTemplate(Form.Properties.conditions,
				conditionManager.constructArguments(control), null, null);
		conditionManager.setupTriggers(input);

		properties['conditions-properties'] = input[0];

		this.renderPropertiesCommonWithTabSupport(placeholder, this.propertiesTemplate, properties, true);

		componentLockManager.setupAll(control);
		conditionManager.setupAll(control);
	}
}, RepeatableSectionRendererCommon));

Form.registerRenderer(FQN, 'design', new RepeatableSectionRenderer());

// RUNTIME
var RepeatableSectionRuntimeRenderer = Form.Runtime.AbstractRenderer.extend($jq.extend({
	tmpl: Form.getTemplate(FQN, 'runtime'),
	rowTmpl: Form.getTemplate(FQN, 'runtime.row'),

	renderBody: function(control, placeholder) {
		this.renderElementCommon(placeholder, this.tmpl,
				{ control: control });
		if(data_json && data_json[control.properties.id]) {
			return;
		}
		for (var i = 0, n = control.properties.repeat; i < n; i++) {
			this._appendRow(control);
		}
		if(control.properties.type == "pagination") {
			control._renderPagination(0);
		}
	},

	prepareControl: function(control) {
		$(control.getElement()).click(function(e) {
			var $target = $(e.target);
			if ($target.hasClass('repeatable-section-remove-button') && !$('#' + control.getElementId() + '--add-row').attr("disabled")) {
				var sectionDlg = $("#repeatable-section-remove-dlg");
				if(!$("#repeatable-section-remove-dlg").length){
					sectionDlg = $('<div id="repeatable-section-remove-dlg" style="display:none">Are you sure you want to delete?</div>');
					$("body").append(sectionDlg);
				}
				// sectionDlg.dialog({
				// 	draggable: false,
				// 	modal: true,
				// 	height: 170,
				// 	title: 'Delete Dialog',
				// 	buttons: {
				// 		'Yes': function(e) {
				// 			var $sectionEntry = $target.parent();
				// 			var index = $sectionEntry.data('index');
				// 			control.removeRow(index.real);
				// 			$(this).dialog("close");
				// 			if(window.onDocumentChange) {
				// 				window.onDocumentChange();
				// 			}
				// 		},
				// 		'No': function(e) {
				// 			$(this).dialog("close");
				// 		}
				// 	}
				// });
                scrollToCenter();
				SOP.Common.confirm({
						message: isSureDelete,
						func:function(){
						var $sectionEntry = $target.parent();
						var index = $sectionEntry.data('index');
						control.removeRow(index.real);
                            window.parent.document.documentElement.scrollTop=$target.offset().top;
                            window.parent.document.body.scrollTop=$target.offset().top;
                        },
                        cancelFunc: function(){
                            $( this ).dialog( "close" );
                            window.parent.document.documentElement.scrollTop=$target.offset().top;
                            window.parent.document.body.scrollTop=$target.offset().top;
                        }
                });
			}
		});
        function scrollToCenter(){
            var iframeParentScrollTop=$('#__egovform-iframe',parent.document).offset().top;
            var iframeHeight=document.body.scrollHeight;
            var clientHeight =  window.parent.document.documentElement.clientHeight;
            window.parent.document.documentElement.scrollTop = (iframeHeight-clientHeight)/2+iframeParentScrollTop;
            window.parent.document.body.scrollTop = (iframeHeight-clientHeight)/2+iframeParentScrollTop;
        }
		$('#' + control.getElementId() + '--add-row').click(function() {
			var index = control.appendRow();
			if(control.properties.type == "pagination") {
				control._renderPagination(index.real);
			}
			if(window.onDocumentChange) {
				window.onDocumentChange();
			}
		});
		var elem = control.getElement();
		if(Form.isView) {
			$jq(".repeatable-section-remove-button", elem).hide();
			$jq('#' + control.getElementId() + '--add-row', elem).hide();
		}
	},
	
	_appendRow: function(control) {
		var el = control.getElement();
		var nonePlace = $('#none-place',el);
		if(nonePlace.length) {
			nonePlace.remove();
		}
		var $lastItem = $(el).children().filter('.repeatable-section-item:last');
		var index = null;
		if ($lastItem.length) {
			var lastIndex = $lastItem.data('index');
			index = {
				actual: lastIndex.actual + 1,
				real: lastIndex.real + 1
			};
		} else {
			index = { actual: 0, real: 0 };
		}

		var $controlButton = $(el).children().filter('.repeatable-section-control-button');
		var placeholder = Form.Common.placeholder();
		$(placeholder).insertBefore($controlButton);
		this.renderElementCommon(placeholder, this.rowTmpl,
				{ control: control, index: index });

		var $item = $('#' + control.getElementId(index.actual));
		$item.data('index', index);
		
		console.log();
		$item.find('> .control-grid')
				.grid({ cols: control.properties.cols, width: control.properties.width });
		if(Form.isView) {
			$jq(".repeatable-section-remove-button", $item).hide();
		}
		return index;
	}

}, RepeatableSectionRendererCommon));

Form.registerRenderer(FQN, 'runtime', new RepeatableSectionRuntimeRenderer());

// PRINTER FRIENDLY
var RepeatableSectionPrinterFriendlyRenderer = Form.Runtime.AbstractRenderer.extend($jq.extend({
	tmpl: Form.getTemplate(FQN, 'printerFriendly'),
	rowTmpl: Form.getTemplate(FQN, 'printerFriendly.row'),
	
	renderBody: function(control, placeholder) {
		this.renderElementCommon(placeholder, this.tmpl, { 
			control: control, 
			instructions: sopformsApi.getInstruction(control.properties.id)
		});
		
		
	},

	prepareControl: function(control) {
		if(data_json && data_json[control.properties.id]) {
			control.getGrid().parents("fieldset").remove();
			return;
		}
		var $grid = control.getGrid();
		$grid.grid({ cols: control.properties.cols });
		
		if(control.properties.type == "pagination") {
			control._renderPagination(0);
		}
	},
	
	_appendRow: function(control) {
		var el = control.getElement();
		var nonePlace = $('#none-place',el);
		if(nonePlace.length) {
			nonePlace.remove();
		}
		var $lastItem = $(el).children().filter('fieldset:last');
		var index = null;
		if ($lastItem.length && $lastItem.data('index')) {
			var lastIndex = $lastItem.data('index');
			index = {
				actual: lastIndex.actual + 1,
				real: lastIndex.real + 1
			};
		} else {
			index = { actual: 0, real: 0 };
		}
		var placeholder = Form.Common.placeholder();
		$(el).append(placeholder);
		var $item = this.renderElementCommon(placeholder, this.rowTmpl,
				{ control: control, index: index });

		$item.data('index', index);
		
		$item.find('> .control-grid')
				.grid({ cols: control.properties.cols, width: control.properties.width });
		return index;
	}

}));

Form.registerRenderer(FQN, 'printerFriendly', new RepeatableSectionPrinterFriendlyRenderer());
}(jQuery));