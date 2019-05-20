(function($jq) {

var FQN = 'sopform.controls.base.MultiSelect';

/**
 * This class serves as the base class for all
 * controls which are multiple select input.
 */
Form.Controls.MultiSelect = Form.Controls.Input.extend({
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

Form.registerClass(FQN, Form.Controls.MultiSelect);

}(jQuery));