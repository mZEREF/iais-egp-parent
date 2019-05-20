(function($jq) {

var FQN = 'sopform.controls.standard.DatePicker';

var DatePickerSerializer = Form.Design.AbstractSerializer.extend({
	tmpl: Form.getTemplate(FQN, 'xml')
});

Form.registerSerializer(FQN, new DatePickerSerializer());

}(jQuery));