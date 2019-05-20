(function($jq) {

var FQN = 'sopform.controls.base.Section';

var SectionSerializer = Form.Design.AbstractSerializer.extend({
	tmpl: Form.getTemplate(FQN, 'xml')
});

Form.registerSerializer(FQN, new SectionSerializer());

}(jQuery));