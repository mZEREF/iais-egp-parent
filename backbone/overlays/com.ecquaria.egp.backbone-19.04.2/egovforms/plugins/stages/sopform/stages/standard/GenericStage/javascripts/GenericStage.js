var FQN = "sopform.stages.standard.GenericStage";

FormFlow.GenericStage = FormFlow.Stage.extend({
	FQN: FQN,
	
	setupCustomizeButtonForStage: function(){
		var _handle = $("#customizedButtons_div > table tr:first");
		
		// draws out the headers
		var entry = $("<tr><th>Default</th><th>Custom</th><th>&nbsp;</th></tr>")
		entry.find("th").addClass("toolboxLabel");
		_handle.before(entry);
		
		// the buttons modifiable in submission stage are
		// Approve, Reject, Query, Delegate, Forward, OK, PDF, Printer Friendly
		_handle.before(this.generateModButtonRow("Approve", "Approve"));
		_handle.before(this.generateModButtonRow("Reject", "Reject"));
		_handle.before(this.generateModButtonRow("Query", "Query"));
		_handle.before(this.generateModButtonRow("Delegate", "Delegate"));
		_handle.before(this.generateModButtonRow("Forward", "Forward"));
		_handle.before(this.generateModButtonRow("OK", "OK"));
		_handle.before(this.generateModButtonRow("PDF", "PDF"));
		_handle.before(this.generateModButtonRow("PrinterFriendly", "Printer Friendly"));
		
		// draws out the separator
		var separator = $('<tr><td colspan="3" class="toolboxLabel"><hr class="separator"></td></tr>');
		_handle.before(separator);
	},
	
	/**
	 * Need to push to a base class so all stages can use.
	 */
	generateModButtonRow: function(key, defaultLabel, customValue){
		var _customValue = null;
		if(this.properties.propertiesData && this.properties.propertiesData["customized" + key + "Label"]){
			_customValue = this.properties.propertiesData["customized" + key + "Label"];
		}
		else{
			_customValue = "";
		}
		
		var control = $("<tr><td>" + defaultLabel + "</td><td><input size='10' class='toolboxLabel' type='text' value='" + _customValue + "' name='customized" + key + "Label'></td><td>&nbsp;</td></tr>");
		
		control.find(":input").bind("keyup", function(){
			FormFlow.saveFlowOptions();
		});
		
		return control; 
	},
	
	afterPropertiesPanelSet: function() {
		// hide properties
		$(".properties---taskAssignmentUserId," +
			".properties---taskAssignmentUserDomain, " +
			".properties---taskAssignmentRoleId ").hide();
		
		$(".properties---customizedAckPageMessage," +
			".properties---customizedAckPageURL").hide();
	},
	prepopulateSelect: function(stage) {
		var userDomains = {};

		var _userDomainNames = Form.Service.getUserDomainNames();
		for (var i = 0, n = _userDomainNames.length; i < n; i++) {
			var _userDomainName = _userDomainNames[i];
			userDomains[_userDomainName] = _userDomainName;
		}
		$("select[name='taskAssignmentUserDomain']").addOption(userDomains);
		$("select[name='taskAssignmentUserDomain']").selectOptions(stage.properties.propertiesData.taskAssignmentUserDomain);
		
		var userRoles = {};
		
		var _userRoles = Form.Service.getAllRole();
		for (var i = 0, n = _userRoles.length; i < n; i++) {
			var _userRole = _userRoles[i];
			userRoles[_userRole.id] = _userRole.name;
		}
		$("select[name='taskAssignmentRoleId']").addOption(userRoles);
		$("select[name='taskAssignmentRoleId']").selectOptions(stage.properties.propertiesData.taskAssignmentRoleId);
	},
	initPropertiesPanel: function() {
		var refreshHideShow = function(stage) {
			// ack page
			if($("select[name='customizedAckPageSelection']").getValue() =='message') {
				$(".properties---customizedAckPageMessage").show();
				$(".properties---customizedAckPageURL").hide();
			} else if($("select[name='customizedAckPageSelection']").getValue() =='url') {
				$(".properties---customizedAckPageMessage").hide();
				$(".properties---customizedAckPageURL").show();
			} else {
				$(".properties---customizedAckPageMessage").hide();
				$(".properties---customizedAckPageURL").hide();
			}

			// task assignment
			if($("select[name='taskAssignmentType']").getValue() =='direct') {
				$(".properties---taskAssignmentUserId").show();
				$(".properties---taskAssignmentUserDomain").show();
				$(".properties---taskAssignmentRoleId").hide();
			} else {
				$(".properties---taskAssignmentUserId").hide();
				$(".properties---taskAssignmentUserDomain").hide();
				$(".properties---taskAssignmentRoleId").show();
			}
			
			if(stage.getHtml){
				var prevStageId = $(stage.getHtml()).prevAll(".formFlowStage:first").attr('id');
				var prevStage = FormFlow.getStageById(prevStageId);
				if(prevStage && prevStage.FQN == "sopform.stages.standard.ManualForwardStage") {			
					$(".properties---taskAssignmentType").hide();
					$(".properties---taskAssignmentUserDomain").hide();
					$(".properties---taskAssignmentUserId").hide();
					$(".properties---taskAssignmentRoleId").hide();
				} 
			}
			
			// buttons
			if($("input[name='customizedButtonsSelection']").getValue() == 'on') {
				$(".properties---customizedButtons").show();
			} else {
				$(".properties---customizedButtons").hide();
			}
		};
		
		var getUserList = function(domain, filter, callback_func) {
			var userList = [];
			var userListAjax = new SOPAjax("getUserList");
			userListAjax.enable_indicator = false;
			userListAjax.parameter = domain + ',' + filter;
			userListAjax.callback(function(data) {
				userList = JSON.parse(data.responseText);
				 callback_func(userList);
			});
			 return userList;
		};
		
		$("input[name='taskAssignmentUserId']").autocomplete(
				getUserList, {
					minChars :0,
					autoFill :true,
					mustMatch :1,
					cacheLength :10,
					extraParams : {
						"domain" : function() {
							return $(
									"select[name='taskAssignmentUserDomain']")
									.getValue();
						}
					}
				}).result(function(event, data, formatted) {
					FormFlow.getStageById(FormFlow.currentStage).properties.propertiesData = $("#flowPropertiesForm").formHash();
				});
		
		$("select[name='customizedAckPageSelection']").change(refreshHideShow);
		$("select[name='taskAssignmentType']").change(refreshHideShow);
		$("input[name='customizedButtonsSelection']").change(refreshHideShow);
		
		if(!this.setupCustomizeButtonForStageDone){//why is this init method running twice? init method for submission stage only run once.
			this.setupCustomizeButtonForStage();
			this.setupCustomizeButtonForStageDone = true;
		}
		
		refreshHideShow(this);
	},
	verifyFormValidity : function() {
		var stageValid = {
				isValid: true,
				errors: {}
		}
		
		var messages = [];
		
		if(this.properties.propertiesData.taskAssignmentType == "direct" && this.properties.propertiesData.taskAssignmentUserId == "") {
			stageValid.isValid = false;
			messages = messages.concat("When Task Type is direct, User Id has to be entered");
		}

		if(messages.length > 0) {
			stageValid.errors[this.id] = {
					name: this.properties.propertiesData.stageName,
					label: this.properties.propertiesData.stageName,
					messages: messages
			}
		}
		
		return stageValid;	
	}


},{});

FormFlow.registerStageType(FQN, FormFlow.GenericStage);