(function($jq) {

var FQN = 'sopform.controls.standard.Empty';

var Label = Form.Controls.Control.extend({
	init: function(){
		conditionManager.doControlInit(this);
	},

	getDefaults: function() {
		return {
			height: 20
		};
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

Form.registerClass(FQN, Label);

Form.Loader.load(FQN, 'renderer');

Form.Loader.load(FQN, 'serializer');

}(jQuery));