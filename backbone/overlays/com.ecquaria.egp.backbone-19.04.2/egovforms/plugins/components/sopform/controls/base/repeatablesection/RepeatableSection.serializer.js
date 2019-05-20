(function($jq) {

var FQN = 'sopform.controls.base.RepeatableSection';

var RepeatableSectionSerializer = Form.Design.AbstractSerializer.extend({
	tmpl: Form.getTemplate(FQN, 'xml')
});

Form.registerSerializer(FQN, new RepeatableSectionSerializer());

}(jQuery));