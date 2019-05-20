(function($jq) {

var FQN = 'sopform.controls.standard.DatePicker';

var DatePicker = Form.Controls.SingleInput.extend({
	init: function(){
		conditionManager.doControlInit(this);
		validationManager.doControlInit(this);
	},

	getDefaults: function() {
		return {
			caption: 'Label',
			text: '',
			format: 'dd/mm/yy',
			_id: this.id,
			options: ""
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

Form.registerClass(FQN, DatePicker);

Form.Loader.load(FQN, 'renderer');

Form.Loader.load(FQN, 'serializer');

}(jQuery));