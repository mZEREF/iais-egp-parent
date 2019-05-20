(function($jq) {

var FQN = 'sopform.controls.standard.ListBox';

var ListBox = Form.Controls.MultiSelect.extend({
	init: function(){
		optionLookupManager.doControlInit(this);
		conditionManager.doControlInit(this);
		validationManager.doControlInit(this);
	},

	getDefaults: function() {
		return {
			caption: 'Label',
			size: 5,
			options: [{"code":"item1","description":"item1"},
						{"code":"item2","description":"item2"},
						{"code":"item3","description":"item3"}],
			_id: this.id,
			selected: []
		}
	},

	FQN: FQN,

	bind: ArrayElement.bind,
	getElement: ArrayElement.getElement,
	getElementId: ArrayElement.getElementId,

	getRuntimeData: function() {
		var el = this.getElement.apply(this, arguments);
		var value = [];
		if (el && !Form.isView) {
			$('.control-input option', el).each(function() {
				if (this.selected) value.push(this.value);
			});
		} else if (data_json) {
			var p = data_json[this.properties.id];
			if (p === undefined) value = this.properties.selected;
			else if (p) value = p.value;
		}

		return { type: Form.Constants.KEY_MULTI_VALUE, value: value };
	},
	
	setValue: function(values){
		var index;
		if (typeof values == 'number') {
			index = values;
			values = arguments[1];
		}

		if (values === undefined || values === null) value = [];
		
		// make sure value is of array type
		if (values.constructor != [].constructor) values = [values];
		
		var el = this.getElement(index);
		if (el) {
			$('.control-input', el).each(function() {
				var selected = false;
				for (var i = 0, n = values.length;i < n; i++) {
					if (this.value == values[i]){
						selected = true;
						break;
					}
				}
				this.selected = selected;
			});
		} else if (window.data_json) {
			var rr = this.getContainingRepeatableRow();
			var id = this.properties.id;
			if (rr) {
				var rrId = rr.properties.id;
				var r = data_json[rrId];
				if (!r) r = data_json[rrId] = { type: Form.Constants.KEY_MULTI_ROW, value: [{}] };
				if (r.value && r.value[index]) {
					var p = r.value[index][id];
					if (p === undefined) r.value[index][id] = { type: Form.Constants.KEY_MULTI_VALUE, value: values };
					else p.value = values;
				}
			} else {
				var p = data_json[id];
				if (p === undefined) data_json[id] = { type: Form.Constants.KEY_MULTI_VALUE, value: values };
				else p.value = values;
			}
		}
	},

	setRuntimeData: function(){
		var p = data_json[this.properties.id];
		if (p == null) return;
	
		var el = this.getElement.apply(this, arguments);
		if (!el) return;

		var values = p.value;
		$('.control-input option', el).each(function() {
			for (var i = 0, n = values.length;i < n; i++) {
				if (this.value == values[i]){
					this.selected = true;
					break;
				}
			}
		});
	},
	
	setRepeatedInitValues: function(setID, rowNum){
		var initValues = Form.initValues;
		var p = initValues[setID];
		if (p == null) return;
		
		var v = p.value[rowNum][this.properties.id];
		if (v == null) return;

		var el = this.getElement(rowNum);
		if (!el) return;

		var values = v.value;
		$('.control-input option', el).each(function() {
			for (var i = 0, n = values.length; i < n; i++){
				if (this.value == values[i]) {
					this.selected = true;
					break;
				}
			}
		});
	},
	
	setRepeatedRuntimeData: function(setID, rowNum){
		var p = data_json[setID];
		if (p == null) return;
		
		var v = p.value[rowNum][this.properties.id];
		if (v == null) return;

		var el = this.getElement(rowNum);
		if (!el) return;

		var values = v.value;
		$('.control-input option', el).each(function() {
			for (var i = 0, n = values.length; i < n; i++){
				if (this.value == values[i]) {
					this.selected = true;
					break;
				}
			}
		});
	},
	
	_renderOptions: function(index) {
		var renderer = this.getRenderer();
		if (renderer._renderOptions && typeof renderer._renderOptions == 'function') {
			renderer._renderOptions(this, index);
		}
	}
});

Form.registerClass(FQN, ListBox);

Form.Loader.load(FQN, 'renderer');

Form.Loader.load(FQN, 'serializer');

}(jQuery));