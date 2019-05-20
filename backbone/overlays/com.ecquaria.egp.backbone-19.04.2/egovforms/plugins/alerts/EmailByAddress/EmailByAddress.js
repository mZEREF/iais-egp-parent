var EmailByAddress = FormSLA.getAlertClass('BaseAlert').extend({
	renderPropertiesBox: function($placeholder, params) {
		params = params? params : {};

		var html = Form.Common.mergeTemplate(this.properties.textbox, {
			name: 'address',
			value: params.address
		});
		
		var $address = $(html);
		$placeholder.append($address);
	},
	
	collectParams: function($placeholder) {
		var $address = $placeholder.find('[name=address]');
		var params = {
			address: $address.val()
		};
		return params;
	}
});
