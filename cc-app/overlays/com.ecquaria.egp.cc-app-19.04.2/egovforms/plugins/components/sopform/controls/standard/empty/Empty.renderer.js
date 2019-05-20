(function($jq) {

var FQN = 'sopform.controls.standard.Empty';

var LabelRendererCommon = {
	tmpl: Form.getTemplate(FQN),

	renderBody: function(control, placeholder) {
		// runtime implementation exists - do changes there too.
		this.renderElementCommon(placeholder, this.tmpl,
			{ control: control });
	}
}

var LabelRenderer = Form.Design.AbstractRenderer.extend($jq.extend({
	propertiesTemplate: Form.getTemplate(FQN, 'properties'),

	renderPropertiesBody: function(control, placeholder) {
		var input;
		var properties = this.base.apply(this, arguments);

		// height
		input = Form.Common.createFromTemplate(Form.Properties.textbox, {
			id: control.properties.id,
			name: 'height',
			size: 4,
			value: control.properties.height
		}, null, {
			'keydown paste': function(e) {
				Form.Common.invokeLater(function(e, control) {
					var height = Form.Util.toInteger($jq(this).val(), 20);
					if (height < 20) height = control.getDefaults().height;
					if (height > 1000) height = 1000;
					control.properties.height = height;
					$jq(control.getElement()).css('height', height + 'px');
				}, { context: this, uid: 'changeProperty' }, e, control);
			}
		});

		properties['height'] = input[0];

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
		
		// for condition properties
		input = Form.Common.createFromTemplate(Form.Properties.conditions,
				conditionManager.constructArguments(control), null, null);
		conditionManager.setupTriggers(input);

		properties['conditions-properties'] = input[0];
		this.renderPropertiesCommonWithTabSupport(placeholder, this.propertiesTemplate, properties, true);
		
		componentLockManager.setupAll(control);
		conditionManager.setupAll(control);
	},
	
	afterRender: function(control, placeholder){
		this.base(control, placeholder);
		$jq(control.getElement()).css('height', control.properties.height + 'px');
	}
}, LabelRendererCommon));

Form.registerRenderer(FQN, 'design', new LabelRenderer());

// RUNTIME
var LabelRuntimeRenderer = Form.Runtime.AbstractRenderer.extend({
	tmpl: Form.getTemplate(FQN),

	renderBody: function(control, placeholder) {
		this.renderElementCommon(placeholder, this.tmpl, {
			control: control
		});
	},

	afterRender: function(control, placeholder){
		this.base(control, placeholder);
		$jq(control.getElement()).css('height', control.properties.height + 'px');
	}
});

Form.registerRenderer(FQN, 'runtime', new LabelRuntimeRenderer());

// PRINTER FRIENDLY
var EmptyPrinterFriendlyRenderer = Form.Runtime.AbstractRenderer.extend({
	tmpl: Form.getTemplate(FQN),

	renderBody: function(control, placeholder) {
		this.renderElementCommon(placeholder, this.tmpl, {
			control: control
		});
	},

	afterRender: function(control, placeholder){
		this.base(control, placeholder);
		$jq(control.getElement()).css('height', control.properties.height + 'px');
	}
});

Form.registerRenderer(FQN, 'printerFriendly', new EmptyPrinterFriendlyRenderer());
}(jQuery));