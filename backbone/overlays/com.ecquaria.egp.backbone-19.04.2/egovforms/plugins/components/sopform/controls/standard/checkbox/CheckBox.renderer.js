(function($jq) {

var FQN = 'sopform.controls.standard.CheckBox';

var CheckBoxRendererCommon = {
	tmpl: Form.getTemplate(FQN),
	inputTableTmpl: Form.getTemplate(FQN, 'input'),

	_renderInputTable: function(control, options, id) {
		return Form.Common.mergeTemplate(this.inputTableTmpl, {
			control: control,
			options: options,
			id: id
		});
	}
}

var CheckBoxRenderer = Form.Design.AbstractRenderer.extend($jq.extend({
	inputTableTmpl: Form.getTemplate(FQN, 'input'),

	propertiesTemplate: Form.getTemplate(FQN, 'properties'),

	prepareControl: function(control) {
		this.base.apply(this, arguments);
		var element = control.getElement();
		var $element = $(element);
		
		var $containers = $element.find('.control-item-aux-container').filter(function() {
			return $(this).parents('.control')[0] == element;
		});
		
		$containers.droppable(SortableDroppable.options);
	},
	
	renderBody: function(control, placeholder, index) {
		var opts = control.properties.options;
		
		// when in repeatable
		if (control.isInRepeatable()) {
			opts = optionLookupManager.getOptionsToRender(control,index);
		}
		
		this.renderElementCommon(placeholder, this.tmpl,
			{ control: control, options: opts, index: index, renderer: this });
	},

	renderPropertiesBody: function(control, placeholder) {
		var input,
		    inputMax,
			inputMin;
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
			name: 'caption',
			value: control.properties.caption
		}, null, {
			'keydown paste': function(e) {
				if (control.isInRepeatable()) return;

				Form.Common.invokeLater(function(e, control) {
					var caption = control.properties.caption = $jq(this).val();
					var $label = $jq('label:first', control.getElement());
					$label.text(caption);
				}, { context: this, uid: 'changeProperty' }, e, control);
			},
			blur: function(e) {
				if (control.isInRepeatable()) {
					control.properties.caption = $jq(this).val();
					Form.Design.refresh(control);
				}
			}
		});

		properties['caption'] = input[0];
		
		// width
		inputMax = 2000;
		inputMin = 20;
		var width = control.properties.width < 0? '' : control.properties.width;
		input = Form.Common.createFromTemplate(Form.Properties.textbox, {
			id: control.properties.id,
			name: 'width',
			value: width
		}, null, {
			'keydown paste': function(e) {
				if (control.isInRepeatable()) return;

				Form.Common.invokeLater(function(e, control) {
					var tabWidth = Form.Util.toInteger($jq(this).val(), -1);
					if (tabWidth < 0) {
						
					} else if (tabWidth >= 0 && tabWidth < inputMin) {
						tabWidth = inputMin;
					} else if (tabWidth > inputMax) {
						tabWidth = inputMax;
					}
					control.properties.width = tabWidth;
					control.refresh();
					Form.Design.choose(control.getElement(), false);
				}, { context: this, uid: 'changeProperty' }, e, control);
			},
			blur: function(e) {
				if (control.isInRepeatable()) {
					var tabWidth = Form.Util.toInteger($jq(this).val(), -1);
					if (tabWidth < 0) {
						
					} else if (tabWidth >= 0 && tabWidth < inputMin) {
						tabWidth = inputMin;
					} else if (tabWidth > inputMax) {
						tabWidth = inputMax;
					}
					control.properties.width = tabWidth;
					Form.Design.refresh(control);
				}
			}
		});

		properties['width'] = input[0];

		// 3
		input = Form.Common.createFromTemplate(Form.Properties.textbox, {
			id: control.properties.id,
			name: 'cols',
			value: control.properties.cols
		}, null, {
			'keydown paste': function(e) {
				if (control.isInRepeatable()) return;

				Form.Common.invokeLater(function(e, control) {
					control.properties.cols = Form.Util.toInteger($jq(this).val(), 1);
					control.refresh();
					Form.Design.choose(control.getElement(), false);
				}, { context: this, uid: 'changeProperty' }, e, control);
			},
			blur: function(e) {
				if (control.isInRepeatable()) {
					control.properties.cols = Form.Util.toInteger($jq(this).val(), 1);
					Form.Design.refresh(control);
				}
			}
		});

		properties['cols'] = input[0];

		// alignment
		input = Form.Common.createFromTemplate(Form.Properties.radio, {
			id: control.properties.id,
			name: 'alignment',
			options: [
				['left', 'Left'],
				['center', 'Center'],
				['right', 'Right']
			],
			value: control.properties.alignment
		});

		var alignment = input;
		SOP.SOPForms.setupRadio(alignment);
		$jq('input[type=button]', input).click(function(e) {
			control.properties.alignment = $jq(this).val();
			control.applyAlignment();
			control.refresh();
		});

		properties['alignment'] = input[0];

		// 4
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

		// 5
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
		
		// for lookup properties
		input = Form.Common.createFromTemplate(Form.Properties.lookup,
				optionLookupManager.constructArguments(control), null, null);
		optionLookupManager.setupTriggers(input, control);

		$('#source_selection', input).change(function(e) {
			var $containers = control.getChildrenContainers();
			if (this.value == 'default') $containers.show();
			else $containers.hide();
		});
		
		properties['lookup-properties'] = input[0];

		// for condition properties
		input = Form.Common.createFromTemplate(Form.Properties.conditions,
				conditionManager.constructArguments(control), null, null);
		conditionManager.setupTriggers(input);

		properties['conditions-properties'] = input[0];

		// for attachment properties
		input = Form.Common.createFromTemplate(Form.Properties.checkbox, 
				attachmentManager.constructArguments(control));
		properties['file_attachment'] = input[0];
		attachmentManager.setupTriggers(input, control);
		
		// for validation properties
		input = Form.Common.createFromTemplate(Form.Properties.validations,
				validationManager.constructArguments(control), null, null);
		validationManager.setupTriggers(input, control);

		properties['validation-properties'] = input[0];
		
		// for developer properties
		input = Form.Common.createFromTemplate(Form.Properties.developer,
				developerManager.constructArguments(control), null, null);
		developerManager.setupTriggers(input, control);
		properties['developer-properties'] = input[0];
		/*input = Form.Common.createFromTemplate(Form.Properties.developer,{
				value: control.properties.onchangescript,
				click_value: ""
			}, null,{
				change: function(e) {
					var script_input = $(this).find("#script_input").val();
					control.properties.onchangescript = $.trim(script_input);
				}
		});
		properties['developer-properties'] = input[0];
		$("#div_change", input).show();*/
		
		this.renderPropertiesCommonWithTabSupport(placeholder, this.propertiesTemplate, properties, true);

		componentLockManager.setupAll(control);
		optionLookupManager.setupAll(control);
		conditionManager.setupAll(control);
		validationManager.setupAll(control);
	},
	
	_renderOptions: function(control, index) {
		if (!control.isInRepeatable()) return;
			
		var elem = control.getElement();
		if (!elem) return;
		
		var opts = optionLookupManager.getOptionsToRender(control,index);
		var $elem = $('.control-input-span', control.getElement());
		var self = this;
		$elem.each(function() {
			var $this = $(this);
			var $row = $this.parents('tr:first');
			var index = $row.data('index');
			var id = control.getElementId(index);
			var html = self._renderInputTable(control, opts, id);
			var $table = $(html);
			$this.empty().append($table);
		});
		control.parent.setInitValues();
	}
}, CheckBoxRendererCommon));

Form.registerRenderer(FQN, 'design', new CheckBoxRenderer());

// RUNTIME
var CheckBoxRuntimeRenderer = Form.Runtime.AbstractRenderer.extend($jq.extend({

	renderBody: function(control, placeholder, index) {
		var opts = optionLookupManager.getOptionsToRender(control,index);

		this.renderElementCommon(placeholder, this.tmpl, {
			control: control,
			options: opts,
			index: index,
			renderer: this
		});
	},

	afterRender: function(control, placeholder, index){
		this.base(control, placeholder, index);
		var $input = $(':checkbox', control.getElement(index));
		$input.change(optionLookupManager.triggerReferenceChain);

		if (this.isMSIE()) {
			$input.click(function() {
				this.blur();
				this.focus();
			});
		}
		
		$input.change(conditionManager.doNotifyTrigger);
		
		var onchangeScript = control.properties.onchangescript;
		var _script = null;
		if(onchangeScript != null && $.trim(onchangeScript) != ''){
			_script = new Function("e", onchangeScript);
			$input.change(_script);
		}	
	}
}, CheckBoxRendererCommon));

Form.registerRenderer(FQN, 'runtime', new CheckBoxRuntimeRenderer());

// PRINTER FRIENDLY
var CheckBoxPrinterFriendlyRenderer = Form.Runtime.AbstractRenderer.extend({
	tmpl: Form.getTemplate(FQN, 'printerFriendly'),
	
	renderBody: function(control, placeholder, index) {
		var val = sopformsApi.getJSONDataValue(control.properties.id, index, control);
		var _hasValue = false;
		var _value = null;
		if(val != null){
			_value = val.value;
			if(_value != null && _value.length > 0){
				_hasValue = true;
			}
		}
		
		_value = _value==null?"":_value;
		var opts = optionLookupManager.getOptionsToRender(control,index);
		this.renderElementCommon(placeholder, this.tmpl, {
			control: control,
			options: opts,
			index: index,
			hasValue: _hasValue,
			text: _value,
			instructions: sopformsApi.getInstruction(control.properties.id)
		});
	}
});

Form.registerRenderer(FQN, 'printerFriendly', new CheckBoxPrinterFriendlyRenderer());

var CheckBoxRuntimeViewRenderer = Form.Runtime.AbstractRenderer.extend({
	tmpl: Form.getTemplate(FQN, 'view'),
	
	renderBody: function(control, placeholder, index) {
		var val = sopformsApi.getJSONDataValue(control.properties.id, index, control);
		var _hasValue = false;
		var _value = null;
		if(val != null){
			_value = val.value;
			if(_value != null && _value.length > 0){
				_hasValue = true;
			}
		}
		
		_value = _value==null?"":_value;
		var opts = optionLookupManager.getOptionsToRender(control,index);
		this.renderElementCommon(placeholder, this.tmpl, {
			control: control,
			options: opts,
			index: index,
			hasValue: _hasValue,
			text: _value,
			instructions: sopformsApi.getInstruction(control.properties.id)
		});
	},
	
	afterRender: function(control, placeholder){
		this.base(control, placeholder);
		var $input = $(':checkbox', control.getElement());
		$input.attr('disabled','');
	}
});

Form.registerRenderer(FQN, 'view', new CheckBoxRuntimeViewRenderer());
}(jQuery));