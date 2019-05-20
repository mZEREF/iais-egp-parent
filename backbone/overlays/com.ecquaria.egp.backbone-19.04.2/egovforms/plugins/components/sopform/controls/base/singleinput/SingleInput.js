(function($jq) {

var FQN = 'sopform.controls.base.SingleInput';

/**
 * This class serves as the base class for all
 * controls which are single value input.
 */
Form.Controls.SingleInput = Form.Controls.Input.extend({
	FQN: FQN,

	verifyFormValidity: function() {
		var messages = [];
		
		if (conditionManager) {
			messages.push.apply(messages, conditionManager.verifyFormValidity(this));
		}
		
		return messages;
	},

	setValue: function(value) {
		var index;
		if (typeof value == 'number') {
			index = value;
			value = arguments[1];
		}

		value = value || '';

		var el = this.getElement(index);
		if (el) {
			$('.control-input', el).val(value);
		} else if (window.data_json) {
			var rr = this.getContainingRepeatableRow();
			var id = this.properties.id;
			if (rr) {
				var rrId = rr.properties.id;
				var r = data_json[rrId];
				if (!r) r = data_json[rrId] = { type: Form.Constants.KEY_MULTI_ROW, value: [{}] };
				if (r.value && r.value[index]) {
					var p = r.value[index][id];
					if (p === undefined) r.value[index][id] = { type: Form.Constants.KEY_SINGLE_VALUE, value: value };
					else p.value = value;
				}
			} else {
				var p = data_json[id];
				if (p === undefined) data_json[id] = { type: Form.Constants.KEY_SINGLE_VALUE, value: value };
				else p.value = value;
			}
		}
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
	
		return { type: Form.Constants.KEY_SINGLE_VALUE, value: value };
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

Form.registerClass(FQN, Form.Controls.SingleInput);

}(jQuery));