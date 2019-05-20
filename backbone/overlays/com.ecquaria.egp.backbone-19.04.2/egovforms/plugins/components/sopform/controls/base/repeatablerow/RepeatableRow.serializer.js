(function($jq) {

var FQN = 'sopform.controls.base.RepeatableRow';

var RepeatableRowSerializer = Form.Design.AbstractSerializer.extend({
	tmpl: Form.getTemplate(FQN, 'xml')
});

Form.registerSerializer(FQN, new RepeatableRowSerializer());

}(jQuery));