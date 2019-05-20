(function($jq) {

var FQN = 'sopform.controls.standard.Button';

var ButtonSerializer = Form.Design.AbstractSerializer.extend({
	tmpl: Form.getTemplate(FQN, 'xml')
});

Form.registerSerializer(FQN, new ButtonSerializer());

}(jQuery));