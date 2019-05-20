var FQN = "sopform.stages.standard.SubmissionStage";

FormFlow.SubmissionStage = FormFlow.Stage.extend({
	constructor : function(options) {
		if (options && options.id) {
			this.id = options.id;
		} else {
			this.id = "stage_" + FormFlow.stageCounter++;
		}
		this.properties = {
				propertiesData: {
					stageName: "Submission Stage"
				}
		};

		if(options) {
			if (options.propertiesData) {
				this.properties.propertiesData = options.propertiesData;
			}
			if(options.definition) {
				this.definition = options.definition;
			}
			this.html = $(this.createHtml()).click(function() {
				FormFlow.toggleStage(this);
				return false;
			});
		}
		
		FormFlow.registerStage(this);

		this.render();
	
		var propertiesData = {
				stageDef: FormFlow.stageType[this.FQN].definition,
				userGroups: FormFlow.userGroups
		};
		this.propertiesPanel = Form.Common.mergeTemplate(this.propertiesTmpl, propertiesData);
		FormFlow.emptyPropertiesPanel();
		FormFlow.updatePropertiesPanel(this);
		
		if(!this.properties.propertiesData.allowSaveDraft) {
			this.properties.propertiesData = $("#flowPropertiesForm").formHash();
		}
		
		if($("#flowPropertiesForm").find("input[name='stageName']").getValue() == '') {
			$("#flowPropertiesForm").find("input[name='stageName']").setValue(this.id);
			this.properties.propertiesData.stageName = this.id;
		}
		FormFlow.emptyPropertiesPanel();
	},
	
	setupCustomizeButtonForStage: function(){
		var _handle = $("#customizedButtons_div > table tr:first");
		
		// draws out the headers
		var entry = $("<tr><th>Default</th><th>Custom</th><th>&nbsp;</th></tr>")
		entry.find("th").addClass("toolboxLabel");
		_handle.before(entry);
		
		// the buttons modifiable in submission stage are
		// Submit, Save Draft, Check Form, OK, PDF, Printer Friendly
		_handle.before(this.generateModButtonRow("Submit", "Submit"));
		_handle.before(this.generateModButtonRow("SaveDraft", "Save Draft"));
		_handle.before(this.generateModButtonRow("CheckForm", "Check Form"));
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
	
	FQN: FQN,
	htmlTmpl: FormFlow.getTemplate(FQN,"FormFlowSubmissionStage"),
	render: function() {
		$("#mainColumn").before(this.html);
	},
	prepopulateSelect : function(stage) {
		if(stage.properties.propertiesData.customizedButton_label) {
			var length = stage.properties.propertiesData.customizedButton_label.split(',').length;
			$("#customizedButtons_div tr:not(:first, :last)").remove();
			for(var i = 0 ; i < length ; i++) {
				this.addNewButtonRow();
			}
		}
		
		var userRoleAjax = new SOPAjax('getAllRole');
		userRoleAjax.enable_indicator = false;
		userRoleAjax.callback(function(data) {
			var result = JSON.parse(data.responseText);
			var userRoles = {};
			for (var i = 0, n = result.length; i < n; i++) {
				var obj = result[i];
				userRoles[obj.id] = obj.name;
			}
			$('select[name=approverRoleId]').addOption(userRoles);
			$('select[name=approverRoleId]').selectOptions(stage.properties.propertiesData.approverRoleId);
		})

		this.processInvokerPrepopulateSelect(stage, "OPL");
		this.processInvokerPrepopulateSelect(stage, "OPS");
		this.processInvokerPrepopulateSelect(stage, "AltStage");
	},
	afterPropertiesPanelSet: function() {
		// hide properties
		var selector = [
			'.properties---customizedAckPageMessage',
			'.properties---customizedAckPageURL',
			'.properties---customizedButtons',
			'.properties---approverRoleId'
		].join();
		$(selector).hide();
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
		
		var refreshHideShow = function() {
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
			
			// buttons
			if($("input[name='customizedButtonsSelection']").getValue() == 'on') {
				$(".properties---customizedButtons").show();
			} else {
				$(".properties---customizedButtons").hide();
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
			
			if ($('input[name=specifyApprover]').getValue() == 'on') {
				$('.properties---approverRoleId').show();
			} else {
				$('.properties---approverRoleId').hide();
			}
		};

		$('input[name=specifyApprover]').change(refreshHideShow);
		$("select[name='customizedAckPageSelection']").change(refreshHideShow);
		$("input[name='customizedButtonsSelection']").change(refreshHideShow);
		$("input[name='useOnPageLoad']").change(refreshHideShow);
		$("input[name='useOnPageSubmit']").change(refreshHideShow);
		$("input[name='useAlternateStage']").change(refreshHideShow);
		
		$(".customizedButton-add").click(this.addNewButtonRow);
		
		$(".customizedButton-remove").click(function() {
			$(this).parent().parent().remove();
			FormFlow.saveFlowOptions();
		});
		
		this.processInvokerInitPropertiesPanel(stage, "OPL");
		this.processInvokerInitPropertiesPanel(stage, "OPS");
		this.processInvokerInitPropertiesPanel(stage, "AltStage");
		
		this.setupCustomizeButtonForStage();
		
		refreshHideShow();
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
				messages = messages.concat("Project, Process, Version and Component have to be entered when Alternate Submission Stage checkbox is selected");
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
	
	

},{

});

FormFlow.registerStageType(FQN, FormFlow.SubmissionStage);