(function($jq) {

var FQN = 'sopform.controls.standard.ListBox';

var ListBoxSerializer = Form.Design.AbstractSerializer.extend({
	tmpl: Form.getTemplate(FQN, 'xml')
});

Form.registerSerializer(FQN, new ListBoxSerializer());

}(jQuery));