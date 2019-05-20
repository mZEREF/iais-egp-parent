(function($jq) {

var FQN = 'sopform.controls.standard.Password';

var Password = Form.Controls.SingleInput.extend({
	init: function(){
		conditionManager.doControlInit(this);
		validationManager.doControlInit(this);
	},

	getDefaults: function() {
		return {
			caption: 'Label',
			text: '',
			size: 30,
			maxLength: null,
			_id: this.id
		}
	},

	FQN: FQN,

	getValue: function() {
		return this.properties.text;
	},

	bind: ArrayElement.bind,
	getElement: ArrayElement.getElement,
	getElementId: ArrayElement.getElementId,
	
	setViewData: function(){
		if(this.isInRepeatableSection() || this.isInRepeatable()) {
			return;
		}
		var p = data_json[this.properties.id];
		var value = "";
		if (p != null) {
			value = p.value;
		}

		var el = this.getElement.apply(this, arguments);
		if (!el) return;
	
		var options = optionLookupManager.getOptionsToRender(this);
		if(options) {
			for(var i in options) {
				if(options[i].code == value) {
					value = options[i]. description;
					break;
				}
			}
		}
		var dot = "*";
		if(!value || value.length == 0) {
			value = Form.defaultEmptyValue;
			$('.control-input', el).html(value + "");
		}else {
			var result = "";
			for(var i = 0; i < value.length; i++) {
				result += dot;
			}
			$('.control-input', el).html(result + "");
		}
	},
	
	setRepeatedViewData: function(setID, rowNum){
		var p = data_json[setID];
		var value = "";
		if (p != null) {
			var v = p.value[rowNum][this.properties.id];
			if (v == null) return;
			value = v.value;
		}
		
		var el = this.getElement(rowNum);
		if (!el) return;
		
		var dot = "*";
		if(!value || value.length == 0) {
			value = Form.defaultEmptyValue;
			$('.control-input', el).html(value + "");
		}else {
			var result = "";
			for(var i = 0; i < value.length; i++) {
				result += dot;
			}
			$('.control-input', el).html(result + "");
		}
	}
});

Form.registerClass(FQN, Password);

Form.Loader.load(FQN, 'renderer');

Form.Loader.load(FQN, 'serializer');

}(jQuery));