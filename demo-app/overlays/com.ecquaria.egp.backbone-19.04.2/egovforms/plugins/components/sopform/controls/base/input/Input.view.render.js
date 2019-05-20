(function($jq) {

var FQN = 'sopform.controls.base.Input';

// RUNTIME
var InputRuntimeViewRenderer = Form.Runtime.AbstractRenderer.extend({
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
Form.registerRenderer(FQN, 'view', new InputRuntimeViewRenderer());
}(jQuery));