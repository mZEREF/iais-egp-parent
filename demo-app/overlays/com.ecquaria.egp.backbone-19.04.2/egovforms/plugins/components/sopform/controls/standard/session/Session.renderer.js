(function($jq) {

var FQN = 'sopform.controls.standard.Session';

var SessionRendererCommon = {
	renderBody: function(control, placeholder) {
		// runtime implementation exists - do changes there too.
		this.renderElementCommon(placeholder, this.tmpl,
			{ control: control });
	}
}

var SessionRenderer = Form.Design.AbstractRenderer.extend($jq.extend({
	tmpl: Form.getTemplate(FQN, 'design'),
	propertiesTemplate: Form.getTemplate(FQN, 'properties'),

	renderPropertiesBody: function(control, placeholder) {
		var input;
		var properties = this.base.apply(this, arguments);

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

		// 1
		input = Form.Common.createFromTemplate(Form.Properties.textbox, {
			id: control.properties.id,
			name: 'key',
			value: control.properties.key
		}, null, {
			'keydown paste': function(e) {
				Form.Common.invokeLater(function(e, control) {
					var key = control.properties.key = $jq(this).val();
					var $label = $jq('label', control.getElement());
					$label.text('[session:' + key + ']');
				}, { context: this, uid: 'changeProperty' }, e, control);
			}
		});

		properties['key'] = input[0];

		input = Form.Common.createFromTemplate(Form.Properties.radio, {
			id: control.properties.id,
			name: 'isEL',
			options: [
				['true', 'Yes'],
				['false', 'No']
			],
			value: '' + control.properties.isEL
		});

		var $isEl = input;
		SOP.SOPForms.setupRadio($isEl);
		$jq('input[type=button]', input).click(function(e) {
			control.properties.isEL = $jq(this).val();
		});

		properties['isEL'] = input[0];

		// 2
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

		// 3
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
		
		input = Form.Common.createFromTemplate(Form.Properties.dropdown, {
			id: control.properties.id,
			name: 'type',
			value: control.properties.type,
			options: [
				['control-font-header', 'Header'],
				['control-font-label', 'Label'],
				['control-font-normal', 'Normal'],
				['control-font-title', 'Title']
			]
		}, null, {
			change: function(e){
				control.properties.type = $jq(this).val();
				control.refresh();
				Form.Design.choose(control.getElement(), false);
			}
		});
		
		properties['type'] = input[0];
		
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
		this.renderPropertiesCommonWithTabSupport(placeholder, this.propertiesTemplate, properties, true);
		
		componentLockManager.setupAll(control);
		conditionManager.setupAll(control);
	}
}, SessionRendererCommon));

Form.registerRenderer(FQN, 'design', new SessionRenderer());

// RUNTIME
var SessionRuntimeRenderer = Form.Runtime.AbstractRenderer.extend({
	tmpl: Form.getTemplate(FQN, 'runtime'),

	renderBody: function(control, placeholder) {
		this.renderElementCommon(placeholder, this.tmpl, {
			control: control
		});
	},

	afterRender: function(control, placeholder){
		this.base(control, placeholder);
	}
});

Form.registerRenderer(FQN, 'runtime', new SessionRuntimeRenderer());

// PRINTER FRIENDLY
var SessionPrinterFriendlyRenderer = Form.Runtime.AbstractRenderer.extend({
	tmpl: Form.getTemplate(FQN, 'runtime'),

	renderBody: function(control, placeholder) {
		this.renderElementCommon(placeholder, this.tmpl, {
			control: control
		});
	}
});

Form.registerRenderer(FQN, 'printerFriendly', new SessionPrinterFriendlyRenderer());

}(jQuery));