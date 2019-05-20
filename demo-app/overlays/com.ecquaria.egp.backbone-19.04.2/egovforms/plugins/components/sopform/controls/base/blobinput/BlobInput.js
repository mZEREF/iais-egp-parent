(function($jq) {

var FQN = 'sopform.controls.base.BlobInput';

/**
 * This class serves as the base class for all
 * controls which are single value blob input.
 */
Form.Controls.BlobInput = Form.Controls.Input.extend({
	FQN: FQN,

	verifyFormValidity: function() {
		var messages = [];
		
		if (conditionManager) {
			messages.push.apply(messages, conditionManager.verifyFormValidity(this));
		}
		
		return messages;
	},

	getRuntimeData: function() {
		var el = this.getElement.apply(this, arguments);
		var value = null;
		if (el && !Form.isView) {
			value = $('.control-input', el).val();
		} else if (data_json) {
			var p = data_json[this.properties.id];
			if (p === undefined) value = this.properties.text;
			else if (p) value = p.value;
		}
	
		return { type: Form.Constants.KEY_BLOB_VALUE, value: value };
	},
	
	setRuntimeData: function(){
		var p = data_json[this.properties.id];
		if (p == null) return;

		var el = this.getElement.apply(this, arguments);
		if (!el) return;

		var value = p.value;
		$('.control-input', el).val(value);
	},

	setRepeatedInitValues: function(setID, rowNum){
		var initValues = Form.initValues;
		var p = initValues[setID];
		if (p == null) return;

		var v = p.value[rowNum][this.properties.id];
		if (!v) return;
		
		var el = this.getElement(rowNum);
		if (!el) return;
		
		var value = v.value;
		$('.control-input', el).val(value);
	},
	
	setRepeatedRuntimeData: function(setID, rowNum){
		var p = data_json[setID];
		if (p == null) return;

		var v = p.value[rowNum][this.properties.id];
		if (v == null) return;
		
		var el = this.getElement(rowNum);
		if (!el) return;
		
		var value = v.value;
		$('.control-input', el).val(value);
	}
});

Form.registerClass(FQN, Form.Controls.BlobInput);

}(jQuery));