/**
 * This module contains some common methods
 * to be used by Control type which can serve as array
 * of control (can be dropped in a RepeatableRow).
 */
var ArrayElement = {};

(function($jq) {

/**
 * Get element id of this control for the specified index.
 *
 * @see Control#getElementId()
 */
ArrayElement.getElementId = function(index) {
	if (!this.isInRepeatable()
			&& !(this.mode != 'design' && this.isInRepeatableSection())
			&& !(this.mode != 'design' && this.isRepeatableSection())) {
		
		return this.base();
	}
	if (index == undefined) return this.base();
	return [this.base(), '--', index].join('');
}

/**
 * Get HTML element of this control for the specified index.
 * If there's no index specified it will return all HTML
 * element for this control as an array.
 *
 * @see Control#getElement()
 */
ArrayElement.getElement = function(index) {
	if (!this.isInRepeatable()
			&& !(this.mode != 'design' && this.isInRepeatableSection())
			&& !(this.mode != 'design' && this.isRepeatableSection())) {
		
		return this.base();
	}
	if (index == undefined) {
		var $els = ArrayElement.getIndexedElement(this);
		return $jq.makeArray($els);
	}
	var id = this.getElementId(index);
	return $jq('#' + id).get(0);
}

/**
 * Bind the HTML element to the control.
 *
 * @see Control#bind()
 */
ArrayElement.bind = function() {
	if (!this.isInRepeatable()
			&& !(this.mode != 'design' && this.isInRepeatableSection())
			&& !(this.mode != 'design' && this.isRepeatableSection())) {
		
		return this.base();
	}
	var self = this;
	ArrayElement.getIndexedElement(this).each(function() {
		Form.setControl(this, self);
	});
}

/**
 * Get all HTML element for the control.
 *
 * @see ArrayElement#getElement()
 */
ArrayElement.getIndexedElement = function(control) {
	var expr = ['div.control[id^="', control.getElementId(), '-"]'].join('');
	var parent = control.parent;
	var root = parent? parent.getElement() : null;
	var $root = null;	
	if (root) {
		$root = $(root);
	} else if (Form.$ROOT_PANEL) {
		$root = Form.$ROOT_PANEL;
	} else {
		$root = $(document.body);
	}

	return $root.find(expr);
}

}(jQuery));