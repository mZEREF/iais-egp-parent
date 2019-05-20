(function($jq) {

var FQN = 'sopform.controls.standard.DropDown';

var DropDown = Form.Controls.SingleSelect.extend({
	init: function(){
		optionLookupManager.doControlInit(this);
		conditionManager.doControlInit(this);
		validationManager.doControlInit(this);
	},

	getDefaults: function() {
		return {
			caption: 'Label',
			empty_option : 'true',
			empty_option_desc : '-Please Select-',
			options: [{"code":"item1","description":"item1"},
						{"code":"item2","description":"item2"},
						{"code":"item3","description":"item3"}],
			_id: this.id,
			selected: undefined
		}
	},

	FQN: FQN,

	bind: ArrayElement.bind,
	getElement: ArrayElement.getElement,
	getElementId: ArrayElement.getElementId,

	getRuntimeData: function() {
		var el = this.getElement.apply(this, arguments);
		var value = null;
		if (el  && !Form.isView) {
			$('.control-input option', el).each(function() {
				if (this.selected) value = this.value;
			});
		} else if (data_json) {
			var p = data_json[this.properties.id];
			var selected = this.properties.selected;
			if (p === undefined && selected !== undefined) value = selected;
			else if (p) value = p.value;
		}

		return { type: Form.Constants.KEY_SINGLE_VALUE, value: value };
	},

	setValue: function(value){
		var index;
		if (typeof value == 'number') {
			index = value;
			value = arguments[1];
		}

		if (value === undefined) value = null;
		
		var el = this.getElement(index);
		if (el) {
			$('.control-input', el).each(function() {
				this.selected = this.value == value;
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

	setRuntimeData: function(){
		var p = data_json[this.properties.id];
		if (p == null) return;
		
		var el = this.getElement.apply(this, arguments);
		if (!el) return;

		var value = p.value;
		$('.control-input option', el).each(function() {
			if (this.value == value) {
				this.selected = true;
				return false;
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

		var value = v.value;
		$('.control-input option', el).each(function() {
			if (this.value == value) this.selected = true;
		});
	},
	
	setRepeatedRuntimeData: function(setID, rowNum){
		var p = data_json[setID];
		if (p == null) return;

		var v = p.value[rowNum][this.properties.id];
		if (v == null) return;

		var el = this.getElement(rowNum);
		if (!el) return;

		var value = v.value;
		$('.control-input option', el).each(function() {
			if (this.value == value) this.selected = true;
		});
	},
	
	_renderOptions: function(index) {
		var renderer = this.getRenderer();
		if (renderer._renderOptions && typeof renderer._renderOptions == 'function') {
			renderer._renderOptions(this, index);
		}
	}
});

Form.registerClass(FQN, DropDown);

Form.Loader.load(FQN, 'renderer');

Form.Loader.load(FQN, 'serializer');

}(jQuery));