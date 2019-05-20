(function($jq) {

var FQN = 'sopform.controls.standard.Html';

var HtmlSerializer = Form.Design.AbstractSerializer.extend({
	tmpl: Form.getTemplate(FQN, 'xml')
});

Form.registerSerializer(FQN, new HtmlSerializer());

}(jQuery));