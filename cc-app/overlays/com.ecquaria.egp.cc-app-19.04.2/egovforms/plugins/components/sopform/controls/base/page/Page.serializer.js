(function($jq) {

var FQN = 'sopform.controls.base.Page';

var PageSerializer = Form.Design.AbstractSerializer.extend({
	tmpl: Form.getTemplate(FQN, 'xml')
});

Form.registerSerializer(FQN, new PageSerializer());

}(jQuery));