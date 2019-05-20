(function($jq) {

var FQN = 'sopform.controls.base.Section';

Form.Controls.Section = Form.Controls.Container.extend({
	init: function(){
		conditionManager.doControlInit(this);
	},

	getDefaults: function() {
		return {
			title: 'Section',
			captionOrientation: 'horizontal',
			cols: 1,
			border: false,
			width: '',
			_id: this.id
		}
	},

	FQN: FQN
});

Form.registerClass(FQN, Form.Controls.Section);

Form.Loader.load(FQN, 'renderer');

Form.Loader.load(FQN, 'serializer');
}(jQuery));