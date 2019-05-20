(function($jq) {

var FQN = 'sopform.controls.standard.TextBox';

var TextBoxRendererCommon = {
	tmpl: Form.getTemplate(FQN),

	renderBody: function(control, placeholder, index) {
		// runtime implementation exists - do changes there too.
		this.renderElementCommon(placeholder, this.tmpl,
				{ control: control, index: index });
	}
}

var TextBoxRenderer = Form.Design.AbstractRenderer.extend($jq.extend({
	propertiesTemplate: Form.getTemplate(FQN, 'properties'),

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
			value: control.properties.text
		}, null, {
			'keydown paste': function(e) {
				Form.Common.invokeLater(function(e, control) {
					var text = control.properties.text = $jq(this).val();
					var $text = $jq('input[type=text]', control.getElement());
					$text.val(text);
				}, { context: this, uid: 'changeProperty' }, e, control);
			}
		});

		properties['text'] = input[0];

		input = Form.Common.createFromTemplate(Form.Properties.textbox, {
			id: control.properties.id,
			name: 'mask',
			value: control.properties.mask
		}, null, {
			'keydown paste': function(e) {
				Form.Common.invokeLater(function(e, control) {
					control.properties.mask = $jq(this).val();
				}, { context: this, uid: 'changeProperty' }, e, control);
			}
		});

		properties['mask'] = input[0];

		// 4
		/*input = Form.Common.createFromTemplate(Form.Properties.textbox, {
			id: control.properties.id,
			name: 'size',
			value: control.properties.size
		}, null, {
			'keydown paste': function(e) {
				Form.Common.invokeLater(function(e, control) {
					var size = control.properties.size = Form.Util.toInteger($jq(this).val(), undefined);
					var $text = $jq('input[type=text]', control.getElement());
					if (size === undefined) size = control.getDefaults().size;
					$text.attr('size', size);
				}, { context: this, uid: 'changeProperty' }, e, control);
			}
		});

		properties['size'] = input[0];*/

		// 5
		input = Form.Common.createFromTemplate(Form.Properties.textbox, {
			id: control.properties.id,
			name: 'maxLength',
			value: control.properties.maxLength
		}, null, {
			'keydown paste': function(e) {
				Form.Common.invokeLater(function(e, control) {
					var maxLength = control.properties.maxLength = Form.Util.toInteger($jq(this).val(), undefined);
					var $text = $jq('input[type=text]', control.getElement());
					if (maxLength === undefined) maxLength = control.getDefaults().maxLength;
					$text.attr('maxlength', maxLength);
				}, { context: this, uid: 'changeProperty' }, e, control);
			}
		});

		properties['maxLength'] = input[0];

		// 5.1 alignment
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

        // 8
        input = Form.Common.createFromTemplate(Form.Properties.textbox, {
            id: control.properties.id,
            name: 'myInfo',
            value: control.properties.myInfo
        }, null, {
            blur: function(e) {
                control.properties.myInfo = $jq(this).val();
            }
        });
        properties['myInfo'] = input[0];

		// for lock field
		input = Form.Common.createFromTemplate(Form.Properties.checkbox, 
				componentLockManager.constructArguments(control));
		properties['component_lock'] = input[0];
		componentLockManager.setupTriggers(input, control);
		
		// for condition properties
		input = Form.Common.createFromTemplate(Form.Properties.conditions,
				conditionManager.constructArguments(control), null, null);
		conditionManager.setupTriggers(input);

		properties['conditions-properties'] = input[0];

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
		
		// for attachment properties
		input = Form.Common.createFromTemplate(Form.Properties.checkbox, 
				attachmentManager.constructArguments(control));
		properties['file_attachment'] = input[0];
		attachmentManager.setupTriggers(input, control);
		
		// for ACL properties
		this.renderPropertiesCommonWithTabSupport(placeholder, this.propertiesTemplate, properties, true);

		componentLockManager.setupAll(control);
		conditionManager.setupAll(control);
		validationManager.setupAll(control);
	}
}, TextBoxRendererCommon));

Form.registerRenderer(FQN, 'design', new TextBoxRenderer());

// RUNTIME
var TextBoxRuntimeRenderer = Form.Runtime.AbstractRenderer.extend({
	tmpl: Form.getTemplate(FQN),

	renderBody: function(control, placeholder, index) {
		this.renderElementCommon(placeholder, this.tmpl,
			{ control: control, index: index });
	},

	afterRender: function(control, placeholder, index){
		this.base(control, placeholder, index);
		var $input = $(':input', control.getElement(index));
		
		if (control.properties.mask) {
			$input.mask(control.properties.mask);
		}
		
		$input.change(conditionManager.doNotifyTrigger);
		$input.change(calculationManager.doCalculation);
		$input.change(aggregationManager.doAggregation);
		
		$input.keyup(optionLookupManager.triggerReferenceChain);
		
		var onchangeScript = control.properties.onchangescript;
		var _script = null;
		if(onchangeScript != null && $.trim(onchangeScript) != ''){
			_script = new Function("e", onchangeScript);
			$input.change(_script);
		}
	}
});

Form.registerRenderer(FQN, 'runtime', new TextBoxRuntimeRenderer());

//PRINTER FRIENDLY
var TextBoxPrinterFriendlyRenderer = Form.Runtime.AbstractRenderer.extend({
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

Form.registerRenderer(FQN, 'printerFriendly', new TextBoxPrinterFriendlyRenderer());

}(jQuery));