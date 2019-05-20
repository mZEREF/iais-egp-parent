(function($jq) {

var FQN = 'sopform.controls.base.Input';

/**
 * This class serves as the base class for all
 * controls which accept input.
 */
Form.Controls.Input = Form.Controls.Control.extend({
	FQN: FQN,
	setViewData: function(){
		if(this.isInRepeatableSection() || this.isInRepeatable()) {
			return;
		}
		var p = data_json[this.properties.id];
		if(p == null)
			return;
		var value = "";
		var isOption;
		if (p != null) {
			value = p.value;
		}

		var el = this.getElement.apply(this, arguments);
		if (!el) return;
	
		var options = optionLookupManager.getOptionsToRender(this);
		if(options) {
			if(p["type"] == 'multi-value'){
				var v = "";
				for(var i in options) {
					var code = options[i].code;
					if($.inArray(code, value) >= 0) {
						if(!v)
							v = options[i].description;
						else
							v += ',' + options[i].description;
						isOption = true;
					}
				}
				
				if(v){
					value = v;
				}
			}else{
				for(var i in options) {
					if(options[i].code == value) {
						value = options[i].description;
						isOption = true;
						break;
					}
				}
			}
		}
		if(!value || value.length == 0) {
			value = Form.defaultEmptyValue;
		}
		if (isOption) {
			$('.control-input', el).html(value);
		}else {
			$('.control-input', el).text(value);
		}
		
	},
	
	setRepeatedViewData: function(setID, rowNum){
		var p = data_json[setID];
		var value = "";
		var isOption;
		if (p != null) {
			var v = p.value[rowNum][this.properties.id];
			if (v == null) return;
			value = v.value;
		}
		
		var el = this.getElement(rowNum);
		if (!el) return;
		var options = optionLookupManager.getOptionsToRender(this);
		if(options) {
			for(var i in options) {
				if(options[i].code == value) {
					value = options[i]. description;
					isOption = true;
					break;
				}
			}
		}
		if(!value || value.length == 0) {
			value = Form.defaultEmptyValue;
		}
		if (isOption) {
			$('.control-input', el).html(value);
		}else {
			$('.control-input', el).text(value);
		}
	}
});

Form.registerClass(FQN, Form.Controls.Input);

}(jQuery));