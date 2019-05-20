(function($jq) {

var FQN = 'sopform.controls.standard.DropDown';

var DropDownRendererCommon = {
	tmpl: Form.getTemplate(FQN),

	renderBody: function(control, placeholder, index) {
		//var opts = control.properties.options;
		var opts = [];
		if(control.properties.empty_option && control.properties.empty_option === 'true' ){
			opts.push({"code":"","description":control.properties.empty_option_desc});
		}
		
		// when in repeatable
		if (control.isInRepeatable()) {
			opts.push.apply(opts, optionLookupManager.getOptionsToRender(control,index));
		} else {
			opts.push.apply(opts, control.properties.options);
		}
		
		this.renderElementCommon(placeholder, this.tmpl,
			{ control: control, options: opts, index: index });
	}
}

var DropDownRenderer = Form.Design.AbstractRenderer.extend($jq.extend({
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
		// moved to properties.lookup.tpl

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
		var lookupArgs = {
				id: control.properties.id,
				name: 'options',
				empty_option : control.properties.empty_option,
				empty_option_desc :  control.properties.empty_option_desc,
				options: control.properties.options,
				sourceOptions: optionLookupManager.sourceOptions,
				referenceOptions: optionLookupManager.getReferenceSelectionOptions(control),
				lookupOptions: optionLookupManager.getLookupOptions()
			};
		input = Form.Common.createFromTemplate(Form.getTemplate(FQN, 'lookup'),
				lookupArgs, null, null);
		optionLookupManager.setupTriggers(input, control);
		
		//this._setupTriggers(input, control);

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
		
		//for lookup event : empty option
		$('#empty_option_div :checkbox').click(function(){
			if($(this).attr('checked')){
				control.properties.empty_option = 'true';
				$('#empty_option_div>:text').attr('disabled', false);
				
			} else {
				control.properties.empty_option = 'false';
				$('#empty_option_div>:text').attr('disabled', true);
			}
			Form.Design.refresh(control);
		});
		$('#empty_option_div>:text').keyup(function(){
			control.properties.empty_option_desc = $.trim($(this).val());
			Form.Design.refresh(control);
		});
	},
	
	_renderOptions: function(control, index) {
		if (!control.isInRepeatable()) return;
			
		var elem = control.getElement();
		if (!elem) return;
		
		var opts = [];
		if(control.properties.empty_option && control.properties.empty_option === 'true' ){
			opts.push({"code":"","description":control.properties.empty_option_desc});
		}
		opts.push.apply(opts, optionLookupManager.getOptionsToRender(control,index));
		
		var optsObj = {};
		for (var i = 0, n = opts.length; i < n; i++) {
			var opt = opts[i];
			optsObj[opt.code] = opt.description;
		}
		var $select = $('.control-input', elem);
		$select.empty().addOption(optsObj);
		//$select.each(function() { this.selectedIndex = -1; });
		control.parent.setInitValues();
		Form.Design.refresh(control);
	}
}, DropDownRendererCommon));

Form.registerRenderer(FQN, 'design', new DropDownRenderer());

// RUNTIME
var DropDownRuntimeRenderer = Form.Runtime.AbstractRenderer.extend({
	tmpl: Form.getTemplate(FQN),

	renderBody: function(control, placeholder, index) {
		//var opts = optionLookupManager.getOptionsToRender(control);
		var opts = [];
		if(control.properties.empty_option && control.properties.empty_option === 'true' ){
			opts.push({"code":"","description":control.properties.empty_option_desc});
		}
		opts.push.apply(opts, optionLookupManager.getOptionsToRender(control,index));

		this.renderElementCommon(placeholder, this.tmpl, {
			control: control,
			options: opts,
			index: index
		});
	},

	afterRender: function(control, placeholder, index){
		this.base(control, placeholder, index);
		var $input = $('select', control.getElement());
		$input.bind('change keyup', optionLookupManager.triggerReferenceChain);
		$input.change(conditionManager.doNotifyTrigger);
		
		var onchangeScript = control.properties.onchangescript;
		var _script = null;
		if(onchangeScript != null && $.trim(onchangeScript) != ''){
			_script = new Function("e", onchangeScript);
			$input.change(_script);
		}	
	}
});

Form.registerRenderer(FQN, 'runtime', new DropDownRuntimeRenderer());

// PRINTER FRIENDLY
var DropDownPrinterFriendlyRenderer = Form.Runtime.AbstractRenderer.extend({
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
	
		var opts = [];
		if(control.properties.empty_option && control.properties.empty_option === 'true' ){
			opts.push({"code":"","description":control.properties.empty_option_desc});
		}
		opts.push.apply(opts, optionLookupManager.getOptionsToRender(control,index));

		this.renderElementCommon(placeholder, this.tmpl, {
			control: control,
			options: opts,
			index: index,
			hasValue: _hasValue,
			text: _value,
			instructions: sopformsApi.getInstruction(control.properties.id)
		});
		
		$(control.getElement()).find('select').bind('change keyup', optionLookupManager.triggerReferenceChain);
	}
});

Form.registerRenderer(FQN, 'printerFriendly', new DropDownPrinterFriendlyRenderer());

}(jQuery));