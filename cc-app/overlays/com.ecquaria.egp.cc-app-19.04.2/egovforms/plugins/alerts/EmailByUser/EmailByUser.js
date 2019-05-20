var EmailByUser = FormSLA.getAlertClass('BaseAlert').extend({
	renderPropertiesBox: function($placeholder, params) {
		params = params? params : {};
		
		var userDomains = [];
		var _userDomains = Form.Service.getUserDomainNames();
		for (var i = 0, n = _userDomains.length; i < n; i++) {
			var _userDomain = _userDomains[i];
			userDomains.push({ value: _userDomain, label: _userDomain });
		}
		
		var html = Form.Common.mergeTemplate(this.properties.dropdown, {
			options : userDomains,
			name: 'userDomain',
			value:params.userDomain
		});
		
		var $userDomain = $(html);
		$placeholder.append($userDomain);

		html = Form.Common.mergeTemplate(this.properties.textbox, {
			name: 'userId',
			value: params.userId?params.userId:''
		});
		
		var $userId = $(html);
		$placeholder.append($userId);
		
		
		var getUserList = function(domain, filter, callback_func) {
			var userList = [];
			var userListAjax = new SOPAjax("getUserList");
			userListAjax.enable_indicator = true;
			userListAjax.ajax_asynchronous = false;
			userListAjax.parameter = domain + ',' + filter;
			userListAjax.callback(function(data) {
				userList = JSON.parse(data.responseText);
				 callback_func(userList);
			});
			 return userList;
		};
		
		var $auto = $userId.autocomplete(
			getUserList, {
				minChars :0,
				autoFill :false,
				mustMatch :true,
				cacheLength :30,
				extraParams : {
					"domain" : function() {
						return $userDomain.val();
					}
				}
			});
			
		$userDomain.change(function(){
			$userId.val('');
			$auto.flushCache();
		});
	},
	
	collectParams: function($placeholder) {
		var $userDomain = $placeholder.find('[name=userDomain]');
		var $userId = $placeholder.find('[name=userId]');
		var params = {
			userDomain : $userDomain.val(),
			userId: $userId.val()
		};
		return params;
	}
});
