var FQN = "sopform.stages.standard.ProcessInvoker";

FormFlow.ProcessInvoker = FormFlow.Stage.extend({
	FQN: FQN,
	
	prepopulateSelect: function(stage) {
		var projectNames = {}
		
		var _projectNames = Form.Service.getProjectNames();
		for (var i = 0, n = _projectNames.length; i < n; i++) {
			var _projectName = _projectNames[i];
			projectNames[_projectName] = _projectName;
		}
		
		$("select[name='processSelectionProject']").addOption(projectNames);
		$("select[name='processSelectionProject']").selectOptions(stage.properties.propertiesData.processSelectionProject);
		$("select[name='processSelectionProject']").change(function() {
			$("input[name='processSelectionProcess']").flushCache();
			$("input[name='processSelectionVersion']").flushCache();
			$("input[name='processSelectionComponent']").flushCache();
		});

	},
	initPropertiesPanel: function() {
		
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
		$("input[name='processSelectionProcess']").autocomplete(
				getProcessList, {
					minChars :0,
					autoFill :true,
					mustMatch :true,
					cacheLength :1,
					extraParams : {
						"project" : function() {
							return $(
									"select[name='processSelectionProject']")
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
		$("input[name='processSelectionVersion']").autocomplete(
				getVersionList, {
					minChars :0,
					autoFill :true,
					mustMatch :true,
					cacheLength :1,
					extraParams : {
						"project" : function() {
							return $(
									"select[name='processSelectionProject']")
									.getValue();
						},
						"process" : function() {
							return $("input[name='processSelectionProcess']").getValue();
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
		$("input[name='processSelectionComponent']").autocomplete(
				getComponentList, {
					minChars :0,
					autoFill :true,
					mustMatch :true,
					cacheLength :1,
					extraParams : {
						"project" : function() {
							return $(
									"select[name='processSelectionProject']")
									.getValue();
						},
						"process" : function() {
							return $("input[name='processSelectionProcess']").getValue();
						},
						"version" : function() {
							return $("input[name='processSelectionVersion']").getValue();
						}
					}
				}).result(function(event, data, formatted) {
					FormFlow.getStageById(FormFlow.currentStage).properties.propertiesData = $("#flowPropertiesForm").formHash();
				});
		
		$("input[name='processSelectionProcess']").change(function() {
			$("input[name='processSelectionVersion']").flushCache();
			$("input[name='processSelectionComponent']").flushCache();
		})
		$("input[name='processSelectionVersion']").change(function() {
			$("input[name='processSelectionComponent']").flushCache();
		})

	},
	verifyFormValidity : function() {
		var stageValid = {
				isValid: true,
				errors: {}
		}
		
		var messages = [];
		
		if(this.properties.propertiesData.processSelectionProject == "" ||
		   this.properties.propertiesData.processSelectionProcess == "" ||
		   this.properties.propertiesData.processSelectionVersion == "" ||
		   this.properties.propertiesData.processSelectionComponent == "") {
			stageValid.isValid = false;
			messages = messages.concat("Project, Process, Version and Component have to be entered.");
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

FormFlow.registerStageType(FQN, FormFlow.ProcessInvoker);