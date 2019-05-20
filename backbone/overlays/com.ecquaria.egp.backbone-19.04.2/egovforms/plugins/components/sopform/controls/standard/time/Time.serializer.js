(function($jq) {

var FQN = 'sopform.controls.standard.Time';

var TimeSerializer = Form.Design.AbstractSerializer.extend({
	tmpl: Form.getTemplate(FQN, 'xml')
});

Form.registerSerializer(FQN, new TimeSerializer());

}(jQuery));