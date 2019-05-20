(function($jq) {

var FQN = 'sopform.controls.standard.RefNumber';

var RefNumberSerializer = Form.Design.AbstractSerializer.extend({
	tmpl: Form.getTemplate(FQN, 'xml')
});

Form.registerSerializer(FQN, new RefNumberSerializer());

}(jQuery));