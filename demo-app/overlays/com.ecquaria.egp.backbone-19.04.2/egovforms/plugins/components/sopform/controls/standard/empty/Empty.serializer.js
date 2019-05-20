(function($jq) {

var FQN = 'sopform.controls.standard.Empty';

var LabelSerializer = Form.Design.AbstractSerializer.extend({
	tmpl: Form.getTemplate(FQN, 'xml')
});

Form.registerSerializer(FQN, new LabelSerializer());

}(jQuery));