(function($jq) {

var FQN = 'sopform.controls.standard.MyInfo';

var Button = Form.Controls.Control.extend({
	init: function(){
		conditionManager.doControlInit(this);
	},

	getDefaults: function() {
		return {
			caption: 'Load MyInfo Data',
			_id: this.id
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

Form.registerClass(FQN, Button);

Form.Loader.load(FQN, 'renderer');

Form.Loader.load(FQN, 'serializer');

}(jQuery));