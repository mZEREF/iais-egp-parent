(function($jq) {

var FQN = 'sopform.controls.standard.RichTextBox';

var RichTextBoxRendererCommon = {
	tmpl: Form.getTemplate(FQN),

	renderBody: function(control, placeholder, index) {
		// runtime implementation exists - do changes there too.
		this.renderElementCommon(placeholder, this.tmpl,
				{ control: control, index: index });
	}
}

var RichTextBoxRenderer = Form.Design.AbstractRenderer.extend($jq.extend({
	propertiesTemplate: Form.getTemplate(FQN, 'properties'),

	beforeRender: function(control, placeholder) {
		this.base.apply(this, arguments);
		var el = control.getElement();
		if (el) {
			var $textarea = $('textarea', el);
			var tid = $textarea.attr('id');
			var richtext = CKEDITOR.instances[tid];
			if (richtext) {
				richtext.destroy();
			}
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
		input = Form.Common.createFromTemplate(Form.Properties.textarea, {
			id: control.properties.id,
			name: 'text',
			value: control.properties.text
		}, null, {
			blur: function(e) {
				var text = control.properties.text = $jq(this).val();
				var tid = $('textarea', control.getElement()).attr('id');
				var richtext = CKEDITOR.instances[tid];
				if (richtext) richtext.setData(text);
			}
		});

		properties['text'] = input[0];

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

		input = Form.Common.createFromTemplate(Form.Properties.textbox, {
			id: control.properties.id,
			name: 'width',
			value: control.properties.width
		}, null, {
			'keydown paste': function(e) {
				Form.Common.invokeLater(function(e, control) {
					var pattern = /^([0-9]+(%|px))?$/;
					var val = $jq(this).val();
					if (!pattern.test(val)) return;
					
					var width = control.properties.width = val;
					var tid = $jq('textarea', control.getElement()).attr('id');
					var richtext = CKEDITOR.instances[tid];
					if (width === undefined) width = null;
					if (richtext) {
						$(richtext.container.$).css('width', width);
					}
				}, { context: this, uid: 'changeProperty' }, e, control);
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
		
		// for ACL properties
		this.renderPropertiesCommonWithTabSupport(placeholder, this.propertiesTemplate, properties, true);

		componentLockManager.setupAll(control);
		conditionManager.setupAll(control);
		validationManager.setupAll(control);
	},
	
	afterRender: function(control, placeholder){
		this.base(control, placeholder);
		var $el = $(control.getElement());
		
		var $textarea = $el.find('textarea');
		var tid = $textarea.attr('id');
		var richtext = CKEDITOR.instances[tid];
		if (richtext) {
			CKEDITOR.remove(richtext);
		}
		CKEDITOR.replace(tid, { width: control.properties.width });
	}
}, RichTextBoxRendererCommon));

Form.registerRenderer(FQN, 'design', new RichTextBoxRenderer());

// RUNTIME
var RichTextBoxRuntimeRenderer = Form.Runtime.AbstractRenderer.extend({
	tmpl: Form.getTemplate(FQN),

	renderBody: function(control, placeholder, index) {
		this.renderElementCommon(placeholder, this.tmpl,
			{ control: control, index: index });
	},

	afterRender: function(control, placeholder){
		this.base(control, placeholder);
		var $el = $(control.getElement());
		
		var $textarea = $el.find('textarea');
		var tid = $textarea.attr('id');
		var richtext = CKEDITOR.instances[tid];
		if (richtext) {
			CKEDITOR.remove(richtext);
		}
		CKEDITOR.replace(tid, { width: control.properties.width });

		var onchangeScript = control.properties.onchangescript;
		var _script = null;
		if(onchangeScript != null && $.trim(onchangeScript) != ''){
			var richtext = CKEDITOR.instances[tid];
			_script = new Function("e", onchangeScript);
			$(richtext).change(_script);
		}
	}
});

Form.registerRenderer(FQN, 'runtime', new RichTextBoxRuntimeRenderer());

//PRINTER FRIENDLY
var RichTextBoxPrinterFriendlyRenderer = Form.Runtime.AbstractRenderer.extend({
	tmpl: Form.getTemplate(FQN),

	renderBody: function(control, placeholder, index) {
		this.renderElementCommon(placeholder, this.tmpl,
			{ control: control, index: index });
	},

	afterRender: function(control, placeholder){
		this.base(control, placeholder);
		var $el = $(control.getElement());
		
		var $textarea = $el.find('textarea');
		var tid = $textarea.attr('id');
		CKEDITOR.replace(tid, { width: control.properties.width, toolbar: [], toolbarStartupExpanded: false});
	}
});

Form.registerRenderer(FQN, 'printerFriendly', new RichTextBoxPrinterFriendlyRenderer());

}(jQuery));