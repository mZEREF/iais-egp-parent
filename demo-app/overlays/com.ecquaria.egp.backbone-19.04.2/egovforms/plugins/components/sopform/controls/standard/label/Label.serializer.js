(function($jq) {

var FQN = 'sopform.controls.standard.Label';

var LabelSerializer = Form.Design.AbstractSerializer.extend({
	tmpl: Form.getTemplate(FQN, 'xml')
});

Form.registerSerializer(FQN, new LabelSerializer());

}(jQuery));