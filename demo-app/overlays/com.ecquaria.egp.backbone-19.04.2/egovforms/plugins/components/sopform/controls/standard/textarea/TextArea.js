(function($jq) {

var FQN = 'sopform.controls.standard.TextArea';

var TextArea = Form.Controls.BlobInput.extend({
	init: function(){
		conditionManager.doControlInit(this);
		validationManager.doControlInit(this);
	},

	getDefaults: function() {
		return {
			caption: 'Label',
			text: '',
			rows: 3,
			cols: 27,
			_id: this.id
		}
	},

	FQN: FQN,

	getValue: function() {
		return this.properties.text;
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

	bind: ArrayElement.bind,
	getElement: ArrayElement.getElement,
	getElementId: ArrayElement.getElementId
});

Form.registerClass(FQN, TextArea);

Form.Loader.load(FQN, 'renderer');

Form.Loader.load(FQN, 'serializer');

}(jQuery));