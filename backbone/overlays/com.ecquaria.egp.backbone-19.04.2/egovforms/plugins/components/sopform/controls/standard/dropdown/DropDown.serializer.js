(function($jq) {

var FQN = 'sopform.controls.standard.DropDown';

var DropDownSerializer = Form.Design.AbstractSerializer.extend({
	tmpl: Form.getTemplate(FQN, 'xml')
});

Form.registerSerializer(FQN, new DropDownSerializer());

}(jQuery));