(function($jq) {

var FQN = 'sopform.controls.base.SingleSelect';

/**
 * This class serves as the base class for all
 * controls which are single select input.
 */
Form.Controls.SingleSelect = Form.Controls.Input.extend({
	FQN: FQN,

	verifyFormValidity: function() {
		var messages = [];
		
		if (optionLookupManager) {
			messages.push.apply(messages, optionLookupManager.verifyFormValidity(this));
		}
	
		if (conditionManager) {
			messages.push.apply(messages, conditionManager.verifyFormValidity(this));
		}
		
		return messages;
	}
});

Form.registerClass(FQN, Form.Controls.SingleSelect);

}(jQuery));