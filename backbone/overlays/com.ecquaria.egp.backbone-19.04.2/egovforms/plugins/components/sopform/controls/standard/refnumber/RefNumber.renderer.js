(function($jq) {

var FQN = 'sopform.controls.standard.RefNumber';

var RefNumberRenderer = Form.Design.AbstractRenderer.extend({
	propertiesTemplate: Form.getTemplate(FQN, 'properties'),
	tmpl: Form.getTemplate(FQN),

	renderBody: function(control, placeholder, index) {
		this.renderElementCommon(placeholder, this.tmpl,
				{ control: control, index: index });
	},

	renderPropertiesBody: function(control, placeholder) {
		var input;
		var properties = this.base.apply(this, arguments);

		// 2
		input = Form.Common.createFromTemplate(Form.Properties.textbox, {
			id: control.properties.id,
			name: 'caption',
			value: control.properties.caption
		}, null, {
			'keydown paste': function(e) {
				Form.Common.invokeLater(function(e, control) {
					var caption = control.properties.caption = $jq(this).val();
					var $label = $jq('label', control.getElement());
					$label.text(caption);
				}, { context: this, uid: 'changeProperty' }, e, control);
			}
		});

		properties['caption'] = input[0];

		// 3
		input = Form.Common.createFromTemplate(Form.Properties.textbox, {
			id: control.properties.id,
			name: 'format',
			value: Form.properties.refNumber.format
		}, null, {
			blur: function(e) {
				Form.properties.refNumber.format = $jq(this).val();
			}
		});

		properties['format'] = input[0];

		// format action button
		var $formatInput = input;
		var self = this;
		var insertText = function(text) {
			var input = $formatInput.get(0);
			var range = self.getSelection(input);
			var start = range.start;
			var end = range.end;
			var currentText = $formatInput.val();
			var newText = currentText.substr(0, start) + text + currentText.substr(end === undefined ? start : end);
			$formatInput.val(newText);
			self.moveCaretTo(input, start + text.length);
			Form.properties.refNumber.format = $formatInput.val();
		};
		

		var btnSeq = $jq('<input type="button" value="{seq}" />');
		btnSeq.click(function(){ insertText('{seq}'); });
		var btnDate = $jq('<input type="button" value="{date}" />');
		btnDate.click(function(){ insertText('{date}'); });
		var btnYY = $jq('<input type="button" value="{yy}" />');
		btnYY.click(function(){ insertText('{yy}'); });
		var btnYYYY = $jq('<input type="button" value="{yyyy}" />');
		btnYYYY.click(function(){ insertText('{yyyy}'); });

		input = $jq('<div>').append(btnSeq, ' ', btnDate, ' ', btnYY, ' ', btnYYYY);
		input.children().css('padding', 0);
		
		properties['format-action-button'] = input[0];

		//
		input = Form.Common.createFromTemplate(Form.Properties.textbox, {
			id: control.properties.id,
			name: 'startnum',
			size: 5,
			maxLength: 5,
			value: Form.properties.refNumber.startNum
		}, null, {
			blur: function(e) {
				var size = Form.properties.refNumber.startNum = Form.Util.toInteger($jq(this).val(), 1);
				$jq(this).val(size);
			}
		});

		properties['startnum'] = input[0];
		
		//
		input = Form.Common.createFromTemplate(Form.Properties.textbox, {
			id: control.properties.id,
			name: 'digits',
			size: 5,
			maxLength: 5,
			value: Form.properties.refNumber.digits
		}, null, {
			blur: function(e) {
				var size = Form.properties.refNumber.digits = Form.Util.toInteger($jq(this).val(), 5);
				$jq(this).val(size);
			}
		});

		properties['digits'] = input[0];

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
		validationManager.setupTriggers(input);

		properties['validation-properties'] = input[0];

		// for attachment properties
		input = Form.Common.createFromTemplate(Form.Properties.checkbox, 
				attachmentManager.constructArguments(control));
		properties['file_attachment'] = input[0];
		attachmentManager.setupTriggers(input, control);
		
		// for ACL properties

		this.renderPropertiesCommonWithTabSupport(placeholder, this.propertiesTemplate, properties, true);

		componentLockManager.setupAll(control);
		conditionManager.setupAll(control);
	},
	
	getSelection: function(inputBox) {
		if (!$.browser.msie) {
			return {
				start : inputBox.selectionStart,
				end : inputBox.selectionEnd
			}
		}

		//and now, the blinkered IE way
		inputBox.focus();
		var bookmark = document.selection.createRange().getBookmark();
		var selection = inputBox.createTextRange();
		selection.moveToBookmark(bookmark);

		var before = inputBox.createTextRange();
		before.collapse(true);
		before.setEndPoint('EndToStart', selection);

		var beforeLength = before.text.length;
		var selLength = selection.text.length;

		return {
			start: beforeLength,
			end: beforeLength + selLength
		}
	},
	
	moveCaretTo: function(inputBox, pos) {
		if (!$.browser.msie) {
			inputBox.selectionStart = pos;
			inputBox.selectionEnd = pos;
			return;
		}
		
		inputBox.focus();
		var r = inputBox.createTextRange();
		r.move('character', pos);
		r.select();
	}
});

Form.registerRenderer(FQN, 'design', new RefNumberRenderer());

// RUNTIME
var RefNumberRuntimeRenderer = Form.Runtime.AbstractRenderer.extend({
	tmpl: Form.getTemplate(FQN, 'runtime'),

	renderBody: function(control, placeholder, index) {
		this.renderElementCommon(placeholder, this.tmpl,
			{ control: control, index: index, Form: Form });
	},

	afterRender: function(control, placeholder){
		this.base(control, placeholder);
	}
});

Form.registerRenderer(FQN, 'runtime', new RefNumberRuntimeRenderer());

// PRINTER FRIENDLY
var RefNumberPrinterFriendlyRenderer = Form.Runtime.AbstractRenderer.extend({
	tmpl: Form.getTemplate(FQN, 'runtime'),

	renderBody: function(control, placeholder, index) {
		this.renderElementCommon(placeholder, this.tmpl,
			{ control: control, index: index, Form: Form });
	}
});

Form.registerRenderer(FQN, 'printerFriendly', new RefNumberPrinterFriendlyRenderer());


}(jQuery));