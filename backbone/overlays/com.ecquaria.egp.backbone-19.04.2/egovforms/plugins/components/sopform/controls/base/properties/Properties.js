(function($jq) {

var FQN = 'sopform.controls.base.Properties';

Form.Properties = {
	dropdown:              			Form.getTemplate(FQN, 'dropdown'),
	option:                			Form.getTemplate(FQN, 'option'),
	radio:                 			Form.getTemplate(FQN, 'radio'),
	readonlyTextbox:       			Form.getTemplate(FQN, 'readonlyTextbox'),
	textarea:              			Form.getTemplate(FQN, 'textarea'),
	textbox:               			Form.getTemplate(FQN, 'textbox'),
	optionSources:        			Form.getTemplate(FQN, 'optionSources'),
	lookup:				  			Form.getTemplate(FQN, 'lookup'),
	conditions:			  			Form.getTemplate(FQN, 'conditions'),
	conditionsParameter:  			Form.getTemplate(FQN, 'conditionsParameters'),
	validations:		  			Form.getTemplate(FQN, 'validations'),
	validationsParameter: 			Form.getTemplate(FQN, 'validationParams'),
	checkbox:              			Form.getTemplate(FQN, 'checkbox'),
	colorpicker:           			Form.getTemplate(FQN, 'colorpicker'),
	button:		           			Form.getTemplate(FQN, 'button'),
	developer:						Form.getTemplate(FQN, 'developer'),
	calculation:					Form.getTemplate(FQN, 'calculation')
}

}(jQuery));