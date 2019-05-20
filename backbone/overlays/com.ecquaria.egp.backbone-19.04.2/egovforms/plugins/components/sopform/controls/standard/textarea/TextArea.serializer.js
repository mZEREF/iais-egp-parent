(function($jq) {

var FQN = 'sopform.controls.standard.TextArea';

var TextAreaSerializer = Form.Design.AbstractSerializer.extend({
	tmpl: Form.getTemplate(FQN, 'xml')
});

Form.registerSerializer(FQN, new TextAreaSerializer());

}(jQuery));