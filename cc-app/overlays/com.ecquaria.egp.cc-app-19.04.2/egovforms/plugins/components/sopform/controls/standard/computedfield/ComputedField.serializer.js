(function($jq) {

var FQN = 'sopform.controls.standard.ComputedField';

var ComputedFieldSerializer = Form.Design.AbstractSerializer.extend({
	tmpl: Form.getTemplate(FQN, 'xml')
});

Form.registerSerializer(FQN, new ComputedFieldSerializer());

}(jQuery));