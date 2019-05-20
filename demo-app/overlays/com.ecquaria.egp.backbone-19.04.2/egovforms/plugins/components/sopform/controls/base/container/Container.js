(function($jq) {

var FQN = 'sopform.controls.base.Container';

/**
 * This class serves as a base class for controls
 * which can contain other controls.
 */
Form.Controls.Container = Form.Controls.Control.extend({
	constructor: function() {
		this.base.apply(this, arguments);
		this.children = [];
	},

	setViewData: function(){
		var p = data_json[this.properties.id];
		if (p == null) return;

		if(this instanceof Form.Controls.RepeatableRow){
			var rows = data_json[this.properties.id].value.length;
			var rowCount = this.getGrid().columns('rows');
			if(rows > rowCount){
				for(var i=rowCount;i<rows;i++){
					this.appendRow();
				}
			}
			
			var children = this.children;
	
			for (var i = 0; i < rows; i++) {
				var n = children.length;
				var obj = {};
				for (var j = 0; j < n; j++) {
					var child = children[j];
					if(child.setRepeatedViewData){
						child.setRepeatedViewData(this.properties.id, i);
					}
				}
			}
		}else if(this instanceof Form.Controls.RepeatableSection){
			var rows = data_json[this.properties.id].value.length;
			for(var i=0;i<rows;i++){
				this.appendRow();
			}
			
			var children = this.children;
	
			for (var i = 0; i < rows; i++) {
				var n = children.length;
				var obj = {};
				for (var j = 0; j < n; j++) {
					var child = children[j];
					if(child.setRepeatedViewData){
						child.setRepeatedViewData(this.properties.id, i);
					}
				}
			}
		}
	},
	
	/**
	 * Remove a child control instance.
	 */
	removeChild: function(control) {
		var idx = $jq(this.children).index(control);

		if (idx < 0) return;

		this.children.remove(idx);
	},

	verifyFormValidity: function() {
		var messages = [];
		
		if (conditionManager) {
			messages.push.apply(messages, conditionManager.verifyFormValidity(this));
		}
		
		return messages;
	},
	
	/**
	 * Refresh this control and all controls which
	 * are inside it.
	 *
	 * @see Control#refresh()
	 */
	refresh: function(placeholder) {
		// this is required for ckeditor to run properly
		// in design mode
		if (Form.currentMode == 'design') {
			var el = this.getElement();
			if (el) {
				$('textarea', el).each(function() {
					var richtext = CKEDITOR.instances[this.id];
					if (richtext) {
						richtext.destroy();
					}
				});
			}
		}
		this.base(placeholder);

		var $grid = this.getGrid();
		$jq.each(this.children, function() {
			var placeholder = Form.Common.placeholder();
			$grid.grid('append', placeholder, this.position);
			this.refresh(placeholder);
		});
		
		//uncomment this line to hide the color block when there is nothing.
		//but then again.. if you comment this.. the loaded design form will have color.
		//if(Form.currentMode != 'design'){
			var el = $grid;
			var theme = Form.Theme.getTheme(Form.properties.themeName);
			if(theme.Page){
				$(el).find(" > * > tr:odd").css("background-color", theme.Page.tr_odd_row.bgcolor);
				$(el).find(" > * > tr:even").css("background-color", theme.Page.tr_even_row.bgcolor);
			}
		//}
	},

	/**
	 * Rearrange the control. When invoked this method
	 * will keep the displayed element and the control
	 * graph in sync.
	 */
	rearrange: function() {
		var $grid = this.getGrid();
		this.children = [];
		var me = this;

		var $td = $grid.find('> tbody > tr > td');

		var el = me.getElement();
		$grid.find('.control:not(.ui-draggable-dragging)').filter(function() {
			return $jq(this).parents('.control').get(0) == el;
		}).each(function() {
			var idx = $td.index($jq(this).parent().get(0));
			var control = Form.getControl(this);
			control.position = idx;
			control.parent = me;
			me.children.push(control);
		});
		
		var el = $grid;
		var theme = Form.Theme.getTheme(Form.properties.themeName);
		if(theme.Page){
			$(el).find(" > * > tr:odd").css("background-color", theme.Page.tr_odd_row.bgcolor);
			$(el).find(" > * > tr:even").css("background-color", theme.Page.tr_even_row.bgcolor);
		}
	},

	/**
	 * Get grid element which is used to put all
	 * its children control.
	 */
	getGrid: function() {
		var elem = this.getElement();
		var $table = $jq(elem).find('.control-grid').eq(0);
		return $table;
	},

	/**
	 * Test whether the given control is a child of this instance.
	 */
	isChild: function(control) {
		return $jq(this.children).index(control) >= 0;
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

		var placeholder = $jq('<div>').get(0);
		var $grid = this.getGrid();
		$grid.grid('append', placeholder, position);
		control.parent = this;
		control.render(placeholder);
		control.bind();
		control.prepareControl();
		this.rearrange();
	},

	/**
	 * Traverse through this instance and all its children
	 * recursively.
	 *
	 * @see Control#traverse()
	 */
	traverse: function(fn) {
		var ret = this.base.apply(this, arguments);
		if (ret === false) return;
		var args = arguments;
		$jq.each(this.children || [], function() {
			this.traverse.apply(this, args);
		});
	},

	/**
	 * @see Control#filter()
	 */
	filter: function(fn) {
		var result = this.base.apply(this, arguments);
		var args = arguments;
		$jq.each(this.children || [], function() {
			result.push.apply(result, this.filter.apply(this, args));
		});
		return result;
	},

	children: null,

	FQN: FQN
}, {
	/**
	 * Traverse through this instance and all its children
	 * recursively.
	 *
	 * This method is duplicated to allow reuse by CheckBox and Radio.
	 *
	 * @see Control#traverse()
	 */
	traverse: function(fn) {
		var ret = this.base.apply(this, arguments);
		if (ret === false) return;
		var args = arguments;
		$jq.each(this.children || [], function() {
			this.traverse.apply(this, args);
		});
	},

	/**
	 * Test whether the given control is a child of this instance.
	 *
	 * This method is duplicated to allow reuse by CheckBox and Radio.
	 */
	isChild: function(control) {
		return $jq(this.children).index(control) >= 0;
	}
});

Form.registerClass(FQN, Form.Controls.Container);

}(jQuery));