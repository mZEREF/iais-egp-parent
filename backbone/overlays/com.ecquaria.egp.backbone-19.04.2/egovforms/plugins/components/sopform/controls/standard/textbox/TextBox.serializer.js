(function($jq) {

var FQN = 'sopform.controls.standard.TextBox';

var TextBoxSerializer = Form.Design.AbstractSerializer.extend({
	tmpl: Form.getTemplate(FQN, 'xml')
});

Form.registerSerializer(FQN, new TextBoxSerializer());

}(jQuery));