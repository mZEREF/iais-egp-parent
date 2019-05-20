(function($jq) {

var FQN = 'sopform.controls.standard.Session';

var SessionSerializer = Form.Design.AbstractSerializer.extend({
	tmpl: Form.getTemplate(FQN, 'xml')
});

Form.registerSerializer(FQN, new SessionSerializer());

}(jQuery));