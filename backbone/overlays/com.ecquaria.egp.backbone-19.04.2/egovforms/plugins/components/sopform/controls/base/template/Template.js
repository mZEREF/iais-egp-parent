(function($jq) {

Form.Template = {
	createControlFromTemplate: function(templateName) {
		var template = this.TEMPLATES ? this.TEMPLATES[templateName] : null;
		return template.create();
	},

	getSectionTemplateNameLabels: function() {
		var result = [];
		if (!this.TEMPLATES) return result;

		$jq.each(this.TEMPLATES, function(name, template) {
			if (template.type == 'section') result.push([name, template.label]);
		});

		return result;
	},

	getPageTemplateNameLabels: function() {
		var result = [];
		if (!this.TEMPLATES) return result;

		$jq.each(this.TEMPLATES, function(name, template) {
			if (template.type == 'page') result.push([name, template.label]);
		});

		return result;
	},

	getDefaultPageTemplateName: function() {
		return 'singlecolumn';
	},
	
	refreshPageTemplate: function() {
		$.getScript(sopFormContext+'TemplateScript.jsp', function() {
			 var layoutSelect = $jq('#addPageDialog').find('select[name=layout]');
			 layoutSelect.find('option[value!=]').remove();
			 $jq.each(Form.Template.getPageTemplateNameLabels().sort(), function(idx, nameLabel) {
				 $jq($jq.OPTION({value: nameLabel[0]}, nameLabel[1])).appendTo(layoutSelect);
			 }); 
		});
	}
}

}(jQuery));