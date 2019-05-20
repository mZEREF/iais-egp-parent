(function($jq) {

var FQN = 'sopform.controls.standard.Html';

var HtmlRendererCommon = {
	tmpl: Form.getTemplate(FQN)
}

var HtmlRenderer = Form.Design.AbstractRenderer.extend($jq.extend({
	propertiesTemplate: Form.getTemplate(FQN, 'properties'),

	renderBody: function(control, placeholder) {
		// runtime implementation exists - do changes there too.
		this.renderElementCommon(placeholder, this.tmpl,
				{ control: control });
	},
	
	/* Uncomment this if need the HTML source to be evaluated in the canvas itself (currently only works for preview).
	afterRender: function(control, placeholder){
		var div = control.getElement();
		$(div).html(control.properties.source);
	},
	*/
	
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
		
		// code
		input = Form.Common.createFromTemplate(Form.Properties.textarea, {
			id: control.properties.id,
			name: 'code',
			value: control.properties.source
		}, null, {
			blur: function(e) {
				control.properties.source = $jq(this).val();
				
				//var div = control.getElement();
				//$(div).html(control.properties.source);
			}
		});

		properties['source'] = input[0];
		
		//display only
		input = Form.Common.createFromTemplate(Form.Properties.checkbox, {
			id: control.properties.id,
			name: 'displayOnly',
			options: [['displayOnly', '']],
			selected: control.properties.displayOnly
		}, null, null);

		properties['displayOnly'] = input[0];
		var displayOnlyInput = input;
		$('input[type=checkbox]', displayOnlyInput).click(function(e) {
			var checked = [];
			$(':checked', displayOnlyInput).each(function() {
				checked.push($(this).val());
			});
			control.properties.displayOnly = checked;
			if(checked && checked.length) {
				$(properties['source']).parent().hide();
			}else {
				$(properties['source']).parent().show();
			}
		});
		
		
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

		if(control.properties.displayOnly && control.properties.displayOnly.toString()) {
			$(properties['source']).parent().hide();
		}
		componentLockManager.setupAll(control);
		conditionManager.setupAll(control);
	}
}, HtmlRendererCommon));

Form.registerRenderer(FQN, 'design', new HtmlRenderer());

// RUNTIME
var HtmlRuntimeRenderer = Form.Runtime.AbstractRenderer.extend({
	tmpl: Form.getTemplate(FQN),

	renderBody: function(control, placeholder) {
		// runtime implementation exists - do changes there too.
		this.renderElementCommon(placeholder, this.tmpl,
				{ control: control });
	},

	afterRender: function(control, placeholder){
		this.base.apply(this, arguments);
		var div = control.getElement();
		var src = control.properties.source;
		var displayOnly = control.properties.displayOnly;
		if(src){
			if(displayOnly && displayOnly[0]) {
				$(div).text("");
			}else {
				$(div).html(src);
			}
		}
		else{
			$(div).html("");
		}
	}
});

Form.registerRenderer(FQN, 'runtime', new HtmlRuntimeRenderer());

// PRINTER FRIENDLY
var HtmlPrinterFriendlyRenderer = Form.Runtime.AbstractRenderer.extend({
	tmpl: Form.getTemplate(FQN),

	renderBody: function(control, placeholder) {
		this.renderElementCommon(placeholder, this.tmpl,
				{ control: control });
	},

	afterRender: function(control, placeholder){
		this.base.apply(this, arguments);
		var div = control.getElement();
		var src = control.properties.source;
		var displayOnly = control.properties.displayOnly;
		if(src){
			if(displayOnly && displayOnly[0]) {
				$(div).text(src);
			}else {
				$(div).html(src);
			}
		}
		else{
			$(div).html("");
		}
	}
});

Form.registerRenderer(FQN, 'printerFriendly', new HtmlPrinterFriendlyRenderer());

}(jQuery));