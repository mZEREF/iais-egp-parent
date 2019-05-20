(function($jq) {

var FQN = 'sopform.controls.standard.Session';

var Session = Form.Controls.Control.extend({
	init: function(){
		conditionManager.doControlInit(this);
	},

	getDefaults: function() {
		return {
			key: '',
			type: 'control-font-label',
			isEL: false
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

Form.registerClass(FQN, Session);

Form.Loader.load(FQN, 'renderer');

Form.Loader.load(FQN, 'serializer');

}(jQuery));