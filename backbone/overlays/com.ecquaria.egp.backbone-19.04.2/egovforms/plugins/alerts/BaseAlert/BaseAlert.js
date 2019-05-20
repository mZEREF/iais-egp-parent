var BaseAlert = Base.extend({
	properties: {
		textbox: FormSLA.getAlertTemplate(FQN, 'properties.textbox'),
		dropdown: FormSLA.getAlertTemplate(FQN, 'properties.dropdown')
	}
});
