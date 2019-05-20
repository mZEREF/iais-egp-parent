(function($jq) {

var FQN = 'sopform.controls.standard.ComputedField';

var ComputedField = Form.Controls.SingleInput.extend({
	init: function(){
		conditionManager.doControlInit(this);
		validationManager.doControlInit(this);
		calculationManager.doControlInit(this);
	},

	getDefaults: function() {
		return {
			caption: 'Label',
			text: '',
			size: 20,
			maxLength: null,
			_id: this.id
		}
	},

	FQN: FQN,

	getValue: function() {
		return this.properties.text;
	},

	setValue: function(value) {
		this.properties.text = value;
	},

	bind: ArrayElement.bind,
	getElement: ArrayElement.getElement,
	getElementId: ArrayElement.getElementId,
	isInput: function() {
		return false;
	}
});

Form.registerClass(FQN, ComputedField);

Form.Loader.load(FQN, 'renderer');

Form.Loader.load(FQN, 'serializer');

}(jQuery));