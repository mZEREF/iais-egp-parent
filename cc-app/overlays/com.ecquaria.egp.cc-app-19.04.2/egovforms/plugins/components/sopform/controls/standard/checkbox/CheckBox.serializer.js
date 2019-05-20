(function($jq) {

var FQN = 'sopform.controls.standard.CheckBox';

var CheckBoxSerializer = Form.Design.AbstractSerializer.extend({
	tmpl: Form.getTemplate(FQN, 'xml'),

	serialize: function(control, includeId) {
		includeId = (includeId === false) ? false : true;
		
		var childrenXml = null;
		var optionLookup;
		if (optionLookupManager) {
			optionLookup = optionLookupManager.getDataForTemplate({
				control: control
			});
		}
		if (optionLookup && (optionLookup.source_type == 'default' || optionLookup.source_type == '' || optionLookup.source_type === undefined)) {
			if (control.children && control.children.length) {
				var children = [];
				$jq(control.children).each(function() {
					var serializer = Form.getSerializer(this.FQN);
					children.push(serializer.serialize(this, includeId));
				});
				childrenXml = children.join('\n');
			}
		}
	
		var result = Form.Common.mergeTemplate(this.tmpl,
				{ serializer: this, control: control, children: childrenXml, includeId: includeId });
	
		return result;
	}
});

Form.registerSerializer(FQN, new CheckBoxSerializer());

}(jQuery));