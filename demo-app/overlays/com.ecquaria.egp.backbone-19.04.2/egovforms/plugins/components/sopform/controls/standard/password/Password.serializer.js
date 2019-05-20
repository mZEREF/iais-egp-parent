(function($jq) {

var FQN = 'sopform.controls.standard.Password';

var PasswordSerializer = Form.Design.AbstractSerializer.extend({
	tmpl: Form.getTemplate(FQN, 'xml')
});

Form.registerSerializer(FQN, new PasswordSerializer());

}(jQuery));