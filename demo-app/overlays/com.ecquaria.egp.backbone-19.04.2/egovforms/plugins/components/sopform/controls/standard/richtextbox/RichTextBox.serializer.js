(function($jq) {

var FQN = 'sopform.controls.standard.RichTextBox';

var RichTextBoxSerializer = Form.Design.AbstractSerializer.extend({
	tmpl: Form.getTemplate(FQN, 'xml')
});

Form.registerSerializer(FQN, new RichTextBoxSerializer());

}(jQuery));