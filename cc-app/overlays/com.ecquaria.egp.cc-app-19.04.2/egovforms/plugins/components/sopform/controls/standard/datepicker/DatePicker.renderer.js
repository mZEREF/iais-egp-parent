(function($jq) {

var FQN = 'sopform.controls.standard.DatePicker';

var DatePickerRendererCommon = {
	tmpl: Form.getTemplate(FQN)
}

var DatePickerRenderer = Form.Design.AbstractRenderer.extend($jq.extend({
	propertiesTemplate: Form.getTemplate(FQN, 'properties'),

	// runtime implementation exists - do changes there too.
	renderBody: function(control, placeholder, index) {
		var input = this.renderElementCommon(placeholder, this.tmpl,
				{ control: control, index: index });

		$jq('input[type=text]', input)
			.datepicker({showOn: 'button', buttonImage: sopFormTheme + 'images/forms/cal1.png', buttonImageOnly: true})
			.datepicker('disable');
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
			name: 'caption',
			value: control.properties.caption
		}, null, {
			'keydown paste': function(e) {
				if (control.isInRepeatable()) return;

				Form.Common.invokeLater(function(e, control) {
					var caption = control.properties.caption = $jq(this).val();
					var $label = $jq('label', control.getElement());
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

		// 3
		input = Form.Common.createFromTemplate(Form.Properties.textbox, {
			id: control.properties.id,
			name: 'text',
			size: 10,
			value: control.properties.text
		}, null, {
			'keydown paste': function(e) {
				Form.Common.invokeLater(function(e, control) {
					$jq(this).change();
				}, { context: this, uid: 'changeProperty' }, e, control);
			},
			change: function(e) {
				var text = control.properties.text = $jq(this).val();
				var $text = $jq('input[type=text]', control.getElement());
				$text.val(text);
			}
		});

		var text = input;
		properties['text'] = input[0];

		// 4
		input = Form.Common.createFromTemplate(Form.Properties.dropdown, {
			id: control.properties.id,
			name: 'format',
			value: control.properties.format,
			options: [
				['dd/mm/yy', 'dd/mm/yyyy'], ['d/mm/yy', 'd/mm/yyyy'],
				['dd/mm/y', 'dd/mm/yy'], ['d/m/yy', 'd/m/yyyy'], ['dd-M-yy', 'dd-M-yyyy']
			]
		}, null, {
			change: function(e) {
				control.properties.format = $jq(this).val();
				text.datepicker('change', {
					dateFormat: control.properties.format || 'dd/mm/yy'
				});
				control.properties.text = text.val();
				Form.Design.refresh(control);
			}
		});

		properties['format'] = input[0];
		
		// options 
		input = Form.Common.createFromTemplate(Form.Properties.textbox, {
			id: control.properties.id,
			name: 'options',
			value: control.properties.options
		}, null, {
			blur: function(e) {
				control.properties.options = $jq(this).val();
			}
		});
		
		properties['options'] = input[0];

		// 5 alignment
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
		});

		properties['alignment'] = input[0];

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

		// for attachment properties
		input = Form.Common.createFromTemplate(Form.Properties.checkbox, 
				attachmentManager.constructArguments(control));
		properties['file_attachment'] = input[0];
		attachmentManager.setupTriggers(input, control);
		
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

		// for validation properties
		input = Form.Common.createFromTemplate(Form.Properties.validations,
				validationManager.constructArguments(control), null, null);
		validationManager.setupTriggers(input);
		validationManager.setupDatePickerComponents(input);

		properties['validation-properties'] = input[0];

		this.renderPropertiesCommonWithTabSupport(placeholder, this.propertiesTemplate, properties, true);

		componentLockManager.setupAll(control);
		conditionManager.setupAll(control);
		validationManager.setupAll(control);

		text.datepicker({
			showOn: 'button',
			buttonImage: sopFormTheme + 'images/forms/cal1.png',
			buttonImageOnly: true,
			dateFormat: control.properties.format || 'dd/mm/yy'
		});
	}
}, DatePickerRendererCommon));

Form.registerRenderer(FQN, 'design', new DatePickerRenderer());

// RUNTIME
var DatePickerRuntimeRenderer = Form.Runtime.AbstractRenderer.extend($jq.extend({
	renderBody: function(control, placeholder, index) {
		var input = this.renderElementCommon(placeholder, this.tmpl,
				{ control: control, index: index });
		var defaultOptions = {
				showOn: 'button',
				buttonImage: sopFormTheme + 'images/forms/cal1.png',
				buttonImageOnly: true,
				dateFormat: control.properties.format,
				changeMonth: true,
				changeYear: true,
				disabled: true
			}
		eval("var options = {"+(control.properties.options == "undefined"?"":control.properties.options)+"}");
		console.log(control.properties.options)
		var options = $.extend({}, defaultOptions, options);
		$jq('input[type=text]', input).attr("disabled", options.disabled);
		delete options.disabled;
		$jq('input[type=text]', input).datepicker(options)
	},

	afterRender: function(control, placeholder){
		this.base(control, placeholder);
		var $input = $(':input', control.getElement());
		$input.change(conditionManager.doNotifyTrigger);
		
		var onchangeScript = control.properties.onchangescript;
		var _script = null;
		var $datePickerPanel = $('#ui-datepicker-div');
		if(onchangeScript != null && $.trim(onchangeScript) != ''){
			_script = new Function("e", onchangeScript);
			$input.change(_script);
		}	
	}
}, DatePickerRendererCommon));

Form.registerRenderer(FQN, 'runtime', new DatePickerRuntimeRenderer());

// PRINTER FRIENDLY
var DatePickerPrinterFriendlyRenderer = Form.Runtime.AbstractRenderer.extend({
	tmpl: Form.getTemplate(FQN, 'printerFriendly'),
	
	renderBody: function(control, placeholder, index) {
		var val = sopformsApi.getJSONDataValue(control.properties.id, index, control);
		var _hasValue = false;
		var _value = null;
		if(val != null){
			_value = val.value;
			if(_value != null && $.trim(_value).length > 0){
				_hasValue = true;
			}
		}
		
		this.renderElementCommon(placeholder, this.tmpl, { 
			control: control, 
			index: index,
			hasValue: _hasValue,
			text: _value,
			instructions: sopformsApi.getInstruction(control.properties.id)
		});
	}
});

Form.registerRenderer(FQN, 'printerFriendly', new DatePickerPrinterFriendlyRenderer());

}(jQuery));