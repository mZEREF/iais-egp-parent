(function($jq) {

var FQN = 'sopform.controls.base.Layout';

/**
 * This module contains all predefined layout that is available in the system.
 * A layout is basically a set of instructions which will be executed to
 * create basic layout of the page.
 */
Form.Layout = {

	LAYOUT_FUNCTIONS: {
		/**
		 * Initially the page is already in single column layout.
		 */
		'Single Column':  function(page) {
			// do nothing
		},

		/**
		 * Set a double column layout to a page.
		 * Basically set the page's properties to 2 columns
		 * and add a section to each column.
		 */
		'Double Column':  function(page) {
			page.properties.cols = 2;
			page.refresh();

			var s1 = Form.createControl('sopform.controls.base.Section', {
				captionOrientation: 'horizontal',
				border: false
			});
			page.append(s1);

			var s2 = Form.createControl('sopform.controls.base.Section', {
				captionOrientation: 'horizontal',
				border: false
			});
			page.append(s2);
		}
	},

	/**
	 * This is the main method to be used by caller
	 * to apply a layout to a page.
	 */
	applyLayout: function(name, page) {
		var fn = this.LAYOUT_FUNCTIONS[name];
		if (fn != undefined && typeof fn == 'function') {
			fn.apply(fn, $jq.makeArray(arguments).slice(1));
		}
	},

	/**
	 * Get default layout name.
	 */
	getDefaultLayoutName: function() {
		return 'Single Column';
	},

	/**
	 * Get layout names available in the system.
	 */
	getLayoutNames: function() {
		var names = [];
		$jq.each(this.LAYOUT_FUNCTIONS, function(name) {
			names.push(name);
		});
		return names;
	}
}

}(jQuery));