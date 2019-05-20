(function($jq) {

var FQN = 'sopform.controls.standard.Html';

var Html = Form.Controls.Control.extend({
	init: function(){
		conditionManager.doControlInit(this);
	},

	getDefaults: function() {
		return {
			_id: this.id
		}
	},
	
	
	getRuntimeData: function() {
		var el = this.getElement.apply(this, arguments);
		var value = null;
		if (data_json) {
			var p = data_json[this.properties.id];
			if (p === undefined) value = "";
			else if (p) value = p.value;
		}
		return { type: Form.Constants.KEY_SINGLE_VALUE, value: value };
	},
	
	setRuntimeData: function() {
		var id = this.properties.id;
		var p = data_json[id];
		if (p == null) return;

		var el = this.getElement.apply(this, arguments);
		if (!el) return;
		
		var values = p.value;
		if(this.properties.displayOnly) {
			$jq(el).text(values);
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

Form.registerClass(FQN, Html);

Form.Loader.load(FQN, 'renderer');

Form.Loader.load(FQN, 'serializer');

}(jQuery));