(function($jq) {

var FQN = 'sopform.controls.standard.MyInfo';

var ButtonRendererCommon = {
	tmpl: Form.getTemplate(FQN)
}

var ButtonRenderer = Form.Design.AbstractRenderer.extend($jq.extend({
	propertiesTemplate: Form.getTemplate(FQN, 'properties'),

	renderBody: function(control, placeholder) {
		// runtime implementation exists - do changes there too.
		this.renderElementCommon(placeholder, this.tmpl,
				{ control: control });
	},
	
	renderPropertiesBody: function(control, placeholder) {
		var input;
		var properties = this.base.apply(this, arguments);

		// 1
		input = Form.Common.createFromTemplate(Form.Properties.textbox, {
			id: control.properties.id,
			name: 'caption',
			value: control.properties.caption
		}, null, {
			'keydown paste': function(e) {
				Form.Common.invokeLater(function(e, control) {
					var caption = control.properties.caption = $jq(this).val();
					var $button = $jq('input[type=button]', control.getElement());
					$button.val(caption);
				}, { context: this, uid: 'changeProperty' }, e, control);
			}
		});

		properties['caption'] = input[0];

		// 2
		input = Form.Common.createFromTemplate(Form.Properties.textarea, {
			id: control.properties.id,
			name: 'script',
			value: control.properties.script
		}, null, {
			blur: function(e) {
				control.properties.script = $jq(this).val();
			}
		});

		properties['script'] = input[0];

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

		// 3
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

		// 4
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
		
		// for condition properties
		input = Form.Common.createFromTemplate(Form.Properties.conditions,
				conditionManager.constructArguments(control), null, null);
		conditionManager.setupTriggers(input);

		properties['conditions-properties'] = input[0];

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
		
		/*input = Form.Common.createFromTemplate(Form.Properties.developer,{
				value: "",
				click_value: control.properties.onclickscript
			}, null,{
				change: function(e) {
					var script_input = $(this).find("#script_input_click").val();
					control.properties.onclickscript = $.trim(script_input);
				}
		});
		properties['developer-properties'] = input[0];
		$("#div_click", input).show();*/
		
		this.renderPropertiesCommonWithTabSupport(placeholder, this.propertiesTemplate, properties, true);
		
		componentLockManager.setupAll(control);
		conditionManager.setupAll(control);
	}
}, ButtonRendererCommon));

Form.registerRenderer(FQN, 'design', new ButtonRenderer());

// RUNTIME
var ButtonRuntimeRenderer = Form.Runtime.AbstractRenderer.extend({
	tmpl: Form.getTemplate(FQN),

	renderBody: function(control, placeholder) {
		this.renderElementCommon(placeholder, this.tmpl,
				{ control: control });
		var div = control.getElement();
		var button = $(div, "input[type='button']");
		button.click(function(){
			try{
				eval(control.properties.script);
			}catch(e){
				console.log("invalid script detected!", e);
			}
		});
	},

	afterRender: function(control, placeholder){
		this.base(control, placeholder);
		
		var onclickScript = "fillMyInfoData();"+control.properties.onclickscript;
		var _script = null;
		if(onclickScript != null && $.trim(onclickScript) != ''){
			_script = new Function(onclickScript);
			$(control.getElement()).find(':button').click(_script);
		}
	}
});

Form.registerRenderer(FQN, 'runtime', new ButtonRuntimeRenderer());

// PRINTER FRIENDLY
var ButtonPrinterFriendlyRenderer = Form.Runtime.AbstractRenderer.extend({
	tmpl: Form.getTemplate(FQN, 'printerFriendly'),

	renderBody: function(control, placeholder) {
		this.renderElementCommon(placeholder, this.tmpl,
			{ control: control });
	},
	
	afterRender: function(control, placeholder){
		this.base(control, placeholder);
		$(control.getElement()).hide();
	}
});

Form.registerRenderer(FQN, 'printerFriendly', new ButtonPrinterFriendlyRenderer());

}(jQuery));