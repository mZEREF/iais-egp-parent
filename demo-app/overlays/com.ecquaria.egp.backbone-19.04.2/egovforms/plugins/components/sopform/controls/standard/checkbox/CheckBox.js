(function($jq) {

var FQN = 'sopform.controls.standard.CheckBox';

var CheckBox = Form.Controls.MultiSelect.extend({
	children: null,

	init: function(){
		optionLookupManager.doControlInit(this);
		conditionManager.doControlInit(this);
		validationManager.doControlInit(this);
		this.children = [];
	},

	getDefaults: function() {
		return {
			caption: 'Label',
			options: [{"code":"item1","description":"item1"},
						{"code":"item2","description":"item2"},
						{"code":"item3","description":"item3"}],
			_id: this.id,
			selected: [],
			width: -1,
			cols: 1,
			supportEscapeOption: true
		}
	},

	bind: ArrayElement.bind,
	getElement: ArrayElement.getElement,
	getElementId: ArrayElement.getElementId,

	FQN: FQN,

	getRuntimeData: function() {
		var el = this.getElement.apply(this, arguments);
		var value = [];
		if (el && !Form.isView) {
			var controlId = this.properties.id;
			var mode = Form.currentMode;
			var preId = 'control--'+mode+'--'+controlId+'--';
			var selector = '.control-input[id^="'+preId+'"]';
			$(selector, el).each(function() {
				if (this.checked) value.push(this.value);
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

		if (values === undefined || values === null) values = [];
		
		// make sure value is of array type
		if (values.constructor != [].constructor) values = [values];
		
		var el = this.getElement(index);
		if (el) {
			$('.control-input', el).each(function() {
				var checked = false;
				for (var i = 0, n = values.length;i < n; i++) {
					if (this.value == values[i]){
						checked = true;
						break;
					}
				}
				this.checked = checked;
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
		var id = this.properties.id;
		var p = data_json[id];
		if (p == null) return;

		var el = this.getElement.apply(this, arguments);
		if (!el) return;

		var values = p.value;
		var preId = 'control--'+this.mode+'--'+id+'--';
		var selector = '.control-input[id^="'+preId+'"]';
		$(selector, el).each(function() {
			for (var i = 0, n = values.length;i < n; i++) {
				if (this.value == values[i]){
					this.checked = true;
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
		$('.control-input', el).each(function() {
			for (var i = 0, n = values.length; i < n; i++){
				if (this.value == values[i]) {
					this.checked = true;
					break;
				}
			}
		});
	},
	
	/**
	 * Append a control to this instance at the given position.
	 */
	append: function(control, position) {
		var render = true;
		if (typeof arguments[0] == 'boolean') {
			render = arguments[0];
			control = arguments[1];
			position = arguments[2];
		}

		if (!render) {
			control.parent = this;
			control.position = position;
			this.children.push(control);
			return;
		}

		var $containers = this.getChildrenContainers();
		if ($containers.length < position) return;

		var placeholder = Form.Common.createPlaceholder($containers[position]);
		control.parent = this;
		control.render(placeholder);
		control.bind();
		control.prepareControl();
		this.rearrange();
	},

	/* several methods derived from Container to support container behavior */
	traverse: Form.Controls.Container.traverse,
	isChild: Form.Controls.Container.isChild,

	refresh: function(placeholder) {
		var that = this;
		this.base.apply(this, arguments);

		var $containers = this.getChildrenContainers();
		$.each(this.children || [], function() {
			var child = this;
			var placeholder;
			if ("printerFriendly" == Form.currentMode){
				placeholder = Form.Common.createPlaceholder($(".control-item-aux-container",that.getElement()).show());
			}
			else {
				placeholder = Form.Common.createPlaceholder($containers[child.position]);
			}
			this.refresh(placeholder);
		});
	},

	getChildrenContainers: function() {
		var element = this.getElement();
		var $element = $(element);
		
		var $containers = $element.find('.control-item-aux-container').filter(function() {
			return $(this).parents('.control')[0] == element;
		});
		
		return $containers;
	},
	
	rearrange: function() {
		var $containers = this.getChildrenContainers();
		this.children = [];
		var me = this;
		$containers.each(function(idx, elem) {
			var $this = $(this);
			var $control = $this.children('.control:not(.ui-draggable-dragging)');
			if (!$control.length) return;
			
			var control = Form.getControl($control[0]);
			control.position = idx;
			control.parent = me;
			me.children.push(control);
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
		$('.control-input', el).each(function() {
			for (var i = 0, n = values.length; i < n; i++){
				if (this.value == values[i]) {
					this.checked = true;
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

Form.registerClass(FQN, CheckBox);

Form.Loader.load(FQN, 'renderer');

Form.Loader.load(FQN, 'serializer');

}(jQuery));