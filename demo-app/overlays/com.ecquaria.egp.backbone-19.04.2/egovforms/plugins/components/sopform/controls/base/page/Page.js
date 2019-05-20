(function($jq) {

var FQN = 'sopform.controls.base.Page';

/**
 * This class models a Page.
 */
Form.Controls.Page = Form.Controls.Container.extend({
	init: function(){
		conditionManager.doControlInit(this);
	},

	getDefaults: function() {
		return {
			title: 'Page 1',
			captionOrientation: 'horizontal',
			cols: 1,
			width: ''
		}
	},

	FQN: FQN
});

Form.registerClass(FQN, Form.Controls.Page);

Form.Loader.load(FQN, 'renderer');

Form.Loader.load(FQN, 'serializer');

}(jQuery));