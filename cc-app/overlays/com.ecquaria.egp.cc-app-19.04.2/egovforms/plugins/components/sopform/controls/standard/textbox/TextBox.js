(function($jq) {

var FQN = 'sopform.controls.standard.TextBox';

var TextBox = Form.Controls.SingleInput.extend({
	init: function(){
		conditionManager.doControlInit(this);
		validationManager.doControlInit(this);
	},

	getDefaults: function() {
		return {
			caption: 'Label',
			text: '',
			size: 30,
			maxLength: null,
			_id: this.id
		}
	},

	FQN: FQN,

	getValue: function() {
		return this.properties.text;
	},

	bind: ArrayElement.bind,
	getElement: ArrayElement.getElement,
	getElementId: ArrayElement.getElementId
});

Form.registerClass(FQN, TextBox);

Form.Loader.load(FQN, 'renderer');

Form.Loader.load(FQN, 'serializer');

}(jQuery));