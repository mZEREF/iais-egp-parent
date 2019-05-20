var FQN = "sopform.stages.standard.PendingApprovalStage";

FormFlow.PendingApprovalStage = FormFlow.Stage.extend({
	FQN: FQN,
	
	setupCustomizeButtonForStage: function(){
		var _handle = $("#customizedButtons_div > table tr:first");
		
		// draws out the headers
		var entry = $("<tr><th>Default</th><th>Custom</th><th>&nbsp;</th></tr>")
		entry.find("th").addClass("toolboxLabel");
		_handle.before(entry);
		
		// the buttons modifiable in submission stage are
		// Save, Approve, Reject, Forward, Query, OK, PDF, Printer Friendly
		_handle.before(this.generateModButtonRow("Save", "Save"));
		_handle.before(this.generateModButtonRow("Approve", "Approve"));
		_handle.before(this.generateModButtonRow("Reject", "Reject"));
		_handle.before(this.generateModButtonRow("Forward", "Forward"));
		_handle.before(this.generateModButtonRow("Query", "Query"));
		_handle.before(this.generateModButtonRow("Discuss", "Discuss"));		
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
	
	processInvokerPrepopulateSelect: function(stage, append){
		// following processInvoker.js prepopulateSelect
		var projectNames = {}
		
		var _projectNames = Form.Service.getProjectNames();
		for (var i = 0, n = _projectNames.length; i < n; i++) {
			var _projectName = _projectNames[i];
			projectNames[_projectName] = _projectName;
		}
		
		var optionsVal = stage.properties.propertiesData["processSelection" + append + "Project"];
		
		$("select[name='processSelection" + append + "Project']").addOption(projectNames);
		$("select[name='processSelection" + append + "Project']").selectOptions(optionsVal);
		$("select[name='processSelection" + append + "Project']").change(function() {
			$("input[name='processSelection" + append + "Process']").flushCache();
			$("input[name='processSelection" + append + "Version']").flushCache();
			$("input[name='processSelection" + append + "Component']").flushCache();
		});
	},
	
	processInvokerInitPropertiesPanel: function(stage, append){
		var getProcessList = function(project, filter, callback_func) {
			
			var processList = [];
			var processNamesAjax = new SOPAjax("getProcessNames");
			processNamesAjax.enable_indicator = false;
			processNamesAjax.parameter = project + ',' + filter;
			processNamesAjax.callback(function(data) {
				var processList = JSON.parse(data.responseText);
				callback_func(processList);
			});
			return processList;
		}
		$("input[name='processSelection" + append + "Process']").autocomplete(
				getProcessList, {
					minChars :0,
					autoFill :true,
					mustMatch :true,
					cacheLength :1,
					extraParams : {
						"project" : function() {
							return $(
									"select[name='processSelection" + append + "Project']")
									.getValue();
						}
					}
				}).result(function(event, data, formatted) {
					FormFlow.getStageById(FormFlow.currentStage).properties.propertiesData = $("#flowPropertiesForm").formHash();
				});
	
		var getVersionList = function(project, process, filter, callback_func) {
			var versionList = [];
			var processVersionsAjax = new SOPAjax("getProcessVersions");
			processVersionsAjax.enable_indicator = false;
			processVersionsAjax.parameter = project + ',' + process + ',' + filter;
			processVersionsAjax.callback(function(data) {
				var versionList = JSON.parse(data.responseText);
				callback_func(versionList);
			});
			return versionList;
		}
		$("input[name='processSelection" + append + "Version']").autocomplete(
				getVersionList, {
					minChars :0,
					autoFill :true,
					mustMatch :true,
					cacheLength :1,
					extraParams : {
						"project" : function() {
							return $(
									"select[name='processSelection" + append + "Project']")
									.getValue();
						},
						"process" : function() {
							return $("input[name='processSelection" + append + "Process']").getValue();
						}
					}
				}).result(function(event, data, formatted) {
					FormFlow.getStageById(FormFlow.currentStage).properties.propertiesData = $("#flowPropertiesForm").formHash();
				});
	
		var getComponentList = function(project, process, version, filter, callback_func) {
			var componentList = [];
			var componentNamesAjax = new SOPAjax("getComponentNames");
			componentNamesAjax.enable_indicator = false;
			componentNamesAjax.parameter = project + ',' + process + ',' + version + ',' + filter;
			componentNamesAjax.callback(function(data) {
				var componentList = JSON.parse(data.responseText);
				callback_func(componentList);
			});
			return componentList;
		}
		$("input[name='processSelection" + append + "Component']").autocomplete(
				getComponentList, {
					minChars :0,
					autoFill :true,
					mustMatch :true,
					cacheLength :1,
					extraParams : {
						"project" : function() {
							return $(
									"select[name='processSelection" + append + "Project']")
									.getValue();
						},
						"process" : function() {
							return $("input[name='processSelection" + append + "Process']").getValue();
						},
						"version" : function() {
							return $("input[name='processSelection" + append + "Version']").getValue();
						}
					}
				}).result(function(event, data, formatted) {
					FormFlow.getStageById(FormFlow.currentStage).properties.propertiesData = $("#flowPropertiesForm").formHash();
				});
		
		$("input[name='processSelection" + append + "Process']").change(function() {
			$("input[name='processSelection" + append + "Version']").flushCache();
			$("input[name='processSelection" + append + "Component']").flushCache();
		})
		$("input[name='processSelection" + append + "Version']").change(function() {
			$("input[name='processSelection" + append + "Component']").flushCache();
		})
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
		if(stage.properties.propertiesData.customizedButton_label) {
			var length = stage.properties.propertiesData.customizedButton_label.split(',').length;
			$("#customizedButtons_div tr.customButton").remove();
			for(var i = 0 ; i < length ; i++) {
				this.addNewButtonRow();
			}
		}

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
		
		// for discussion
		$("select[name='allowDiscussionsRoleId']").addOption(userRoles);
		$("select[name='allowDiscussionsRoleId']").selectOptions(stage.properties.propertiesData.allowDiscussionsRoleId);
		
		this.processInvokerPrepopulateSelect(stage, "OPL");
		this.processInvokerPrepopulateSelect(stage, "OPS");
		this.processInvokerPrepopulateSelect(stage, "AltStage");
		
	},
	initPropertiesPanel: function(stage) {
		if(stage.properties.propertiesData.customizedButton_label) {
			var length = stage.properties.propertiesData.customizedButton_label.split(',').length;
			var label = stage.properties.propertiesData.customizedButton_label.split(',');
			var value = stage.properties.propertiesData.customizedButton_value.split(',');
			for(var i = 0;i<length ;i++) {
				$("input[name='customizedButton_label']").eq(i).setValue(label[i]);
				$("input[name='customizedButton_value']").eq(i).setValue(value[i]);
			}
			
		}

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
			
			// page load
			if($("input[name='useOnPageLoad']").getValue() == 'on') {
				$(".properties---processSelectionOPLProject").show();
				$(".properties---processSelectionOPLProcess").show();
				$(".properties---processSelectionOPLVersion").show();
				$(".properties---processSelectionOPLComponent").show();
			} else {
				$(".properties---processSelectionOPLProject").hide();
				$(".properties---processSelectionOPLProcess").hide();
				$(".properties---processSelectionOPLVersion").hide();
				$(".properties---processSelectionOPLComponent").hide();
			}

			// page submit
			if($("input[name='useOnPageSubmit']").getValue() == 'on') {
				$(".properties---processSelectionOPSProject").show();
				$(".properties---processSelectionOPSProcess").show();
				$(".properties---processSelectionOPSVersion").show();
				$(".properties---processSelectionOPSComponent").show();
			} else {
				$(".properties---processSelectionOPSProject").hide();
				$(".properties---processSelectionOPSProcess").hide();
				$(".properties---processSelectionOPSVersion").hide();
				$(".properties---processSelectionOPSComponent").hide();
			}

			// alternate submission page
			if($("input[name='useAlternateStage']").getValue() == 'on') {
				$(".properties---processSelectionAltStageProject").show();
				$(".properties---processSelectionAltStageProcess").show();
				$(".properties---processSelectionAltStageVersion").show();
			} else {
				$(".properties---processSelectionAltStageProject").hide();
				$(".properties---processSelectionAltStageProcess").hide();
				$(".properties---processSelectionAltStageVersion").hide();
			}

			// buttons
			if($("input[name='customizedButtonsSelection']").getValue() == 'on') {
				$(".properties---customizedButtons").show();
			} else {
				$(".properties---customizedButtons").hide();
			}
			
			// allows discussion
			if($("input[name='allowsDiscussionSelection']").getValue() == 'on') {
				$(".properties---allowsDiscussions").show();
			} else {
				$(".properties---allowsDiscussions").hide();
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
		
		$("select[name='customizedAckPageSelection']").change(function() {
			refreshHideShow(FormFlow.getStageById(FormFlow.currentStage));
		});
		$("select[name='taskAssignmentType']").change(function() {
			refreshHideShow(FormFlow.getStageById(FormFlow.currentStage));
		});
		$("input[name='useOnPageLoad']").change(function() {
			refreshHideShow(FormFlow.getStageById(FormFlow.currentStage));
		});
		$("input[name='useOnPageSubmit']").change(function() {
			refreshHideShow(FormFlow.getStageById(FormFlow.currentStage));
		});
		$("input[name='useAlternateStage']").change(function() {
			refreshHideShow(FormFlow.getStageById(FormFlow.currentStage));
		});
		
		this.processInvokerInitPropertiesPanel(stage, "OPL");
		this.processInvokerInitPropertiesPanel(stage, "OPS");
		this.processInvokerInitPropertiesPanel(stage, "AltStage");
				
		$("input[name='customizedButtonsSelection']").change(refreshHideShow);
		if(!this.setupCustomizeButtonForStageDone){
			this.setupCustomizeButtonForStage();
			this.setupCustomizeButtonForStageDone = true;
		}
		
		$("input[name='allowsDiscussionSelection']").change(function() {
			refreshHideShow(FormFlow.getStageById(FormFlow.currentStage));
		});
		
		$(".customizedButton-add").click(this.addNewButtonRow);
		
		$(".customizedButton-remove").click(function() {
			$(this).parent().parent().remove();
			FormFlow.saveFlowOptions();
		});

		
		refreshHideShow(this);
	},
	addNewButtonRow : function() {
		var newRow = $("<tr><td>" +
				"<input size='10' type='text' name='customizedButton_label' " +
				"class='toolboxLabel'/></td><td><input size='10' type='text' " +
				"name='customizedButton_value' class='toolboxLabel'/></td><td>" +
				"<button type='button' class='customizedButton-remove' " +
				"style='padding-left: 0; padding-right: 0; padding-top: 0; height: 20px;'>" +
				"<img src='" + sopFormTheme + "images/general/icon-delete.png' alt=''/></button></td></tr>");
		$("#customizedButtons_div tr:last").before(newRow);
		$(".customizedButton-remove").click(function() {
			$(this).parent().parent().remove();
			FormFlow.saveFlowOptions();
		});
		FormFlow.updateMonitorForm();
	
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
		
		if(this.properties.propertiesData.useOnPageLoad == "on") {
			if(this.properties.propertiesData.processSelectionOPLProject == "" ||
			   this.properties.propertiesData.processSelectionOPLProcess == "" ||
			   this.properties.propertiesData.processSelectionOPLVersion == "" ||
			   this.properties.propertiesData.processSelectionOPLComponent == "") {
				stageValid.isValid = false;
				messages = messages.concat("Project, Process, Version and Component have to be entered when On Page Load checkbox is selected");
			}
		}
		
		if(this.properties.propertiesData.useOnPageSubmit == "on") {
			if(this.properties.propertiesData.processSelectionOPSProject == "" ||
			   this.properties.propertiesData.processSelectionOPSProcess == "" ||
			   this.properties.propertiesData.processSelectionOPSVersion == "" ||
			   this.properties.propertiesData.processSelectionOPSComponent == "") {
				stageValid.isValid = false;
				messages = messages.concat("Project, Process, Version and Component have to be entered when On Page Submit checkbox is selected");
			}
		}

		if(this.properties.propertiesData.useAlternateStage == "on") {
			if(this.properties.propertiesData.processSelectionAltStageProject == "" ||
			   this.properties.propertiesData.processSelectionAltStageProcess == "" ||
			   this.properties.propertiesData.processSelectionAltStageVersion == "" ||
			   this.properties.propertiesData.processSelectionAltStageComponent == "") {
				stageValid.isValid = false;
				messages = messages.concat("Project, Process, Version and Component have to be entered when Alternate Approval Stage checkbox is selected");
			}
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

FormFlow.registerStageType(FQN, FormFlow.PendingApprovalStage);				 userList = JSON.parse(data.responseText);