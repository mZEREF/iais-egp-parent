(function($jq) {

var FQN = 'sopform.controls.standard.RefNumber';

var RefNumber = Form.Controls.Control.extend({
	init: function(){
		conditionManager.doControlInit(this);
	},

	getDefaults: function() {
		return {
			caption: 'Label',
			format: '{seq}',
			name: 'refNumber'
		}
	},

	verifyFormValidity: function() {
		var messages = [];
		
		if (conditionManager) {
			messages.push.apply(messages, conditionManager.verifyFormValidity(this));
		}
		
		return messages;
	},

	FQN: FQN
});

Form.registerClass(FQN, RefNumber);

Form.Loader.load(FQN, 'renderer');

Form.Loader.load(FQN, 'serializer');

}(jQuery));