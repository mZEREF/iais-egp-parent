var FQN = "sopform.stages.standard.BranchColumn";

FormFlow.BranchColumn = FormFlow.Stage.extend({
	FQN: FQN,
	htmlTmpl: FormFlow.getTemplate(FQN,"FormFlowBranchColumn"),
	xmlTmpl: FormFlow.getTemplate(FQN,"FlowElementXmlBranchColumn"),
	append: function(content) {
		$(this.getHtml()).children(".stagesList").append(content);
	},
	compare : {
		eq: "=",
		ne: "<>",
		gt: ">",
		gte: ">=",
		lt: "<",
		lte: "<="
	},
	getXml: function() {
		var stagesXml = "";
		var stages = FormFlow.getStages($(this.getHtml()).children(".stagesList"));
		for(var stage in stages) {
			if(stages.hasOwnProperty(stage)) {
				stagesXml += stages[stage].getXml();
			}
		}
		this.stagesXml = stagesXml; 
		return Form.Common.mergeTemplate(this.xmlTmpl, this);
	},
	beforeRemove: function() {
		if($(this.getHtml()).siblings(".branchColumn").size() < 2 ) {
			$("#formFlowBranchColumnRemoveDialog").dialog("open");
			return false;
		} else {
			return true;
		}
	}, 
	afterElementRendered: function() {
		$(this.getHtml()).draggable($.extend({},FormFlow.Design.draggableOptions, {
			cursorAt: {
			top: 0,
			left: 10
			},
			appendTo: 'body',
			helper: function() {
				var helper = $(this).clone().css({width: this.offsetWidth, height: this.offsetHeight, marginLeft: 0, marginTop: 0, position: 'absolute'})[0];
				return helper;
			}, 
			start: function(e, ui) {
			}
		}));
		$(this.getHtml()).children(".stagesList").droppable(FlowSortableDroppable.options);
	},
	afterPropertiesPanelSet: function() {
		// hide properties
		$(".propertiesresultValue," +
			".propertiesfieldValueDropDown, " +
			".propertiesfieldValueText").hide();

	},
	prepopulateSelect: function() {
		$("select[name='compare']").addOption(this.compare).selectOptions("eq");
		
		var prevStageId = $(this.getHtml()).parent().parent().prevAll(".formFlowStage:first").attr('id');
		var parentId =  $(this.getHtml()).parent().parent().attr('id');

		if(parentId) {
			var branchCondition = FormFlow.getStageById(parentId).properties.propertiesData.branchCondition;
		}

		if(branchCondition == 'result') {
			if(prevStageId) {
				var resultValueOptions = {};
				var prevStageResult = FormFlow.getStageById(prevStageId).definition.stageResult;
				for(var key in prevStageResult) {
					if(key != 'remove' && key != 'insert') {
						resultValueOptions[prevStageResult[key].result] = prevStageResult[key].result;
					}
				}
				$("select[name='resultValue']").addOption(resultValueOptions);
			}
		} else if(branchCondition == 'field') {
			var parentStage = FormFlow.getStageById(parentId);
			var selectedControlId = parentStage.properties.propertiesData.selectField;
			var control = Form.getControl(selectedControlId);
			
			if(control != null && (control.isSingleSelect() || control.isMultiSelect())) {
				var fieldValueOptions = {};
				var options = control.properties.options;
				for(var key in options) {
					if(key != 'remove' && key != 'insert') {
						fieldValueOptions[options[key].code] = options[key].description;
					}
				}
				$("select[name='fieldValueDropDown']").addOption(fieldValueOptions);
			}
		}
	},
	initPropertiesPanel: function() {

		var prevStageId = $(this.getHtml()).parent().parent().prevAll(".formFlowStage:first").attr('id');
		var parentId =  $(this.getHtml()).parent().parent().attr('id');

		if(parentId) {
			var branchCondition = FormFlow.getStageById(parentId).properties.propertiesData.branchCondition;
		}

		if(branchCondition == 'result') {
			$(".propertiesresultValue").show();
			$(".propertiesfieldValueDropDown").hide();
			$(".propertiesfieldValueText").hide();
			
			$(".propertiesselectField").hide();
			$("input[name='branchCondition']").setValue("Result");
		} else if (branchCondition == 'field') {
			$(".propertiesresultValue").hide();

			var parentStage = FormFlow.getStageById(parentId);
			var selectedControlId = parentStage.properties.propertiesData.selectField;
			var control = Form.getControl(selectedControlId);

			$(".propertiesselectField").show();
			$("input[name='branchCondition']").setValue("Field");
			if(control) {
				$("input[name='selectField']").setValue(control.properties.caption);
			}
			if(control == null) {
				$(".propertiesfieldValueDropDown").hide();
				$(".propertiesfieldValueText").hide();				
			} else if (control.isSingleSelect() || control.isMultiSelect()) {
				$(".propertiesfieldValueDropDown").show();
				$(".propertiesfieldValueText").hide();
			} else if(control.isSingleInput()) {
				$(".propertiesfieldValueDropDown").hide();
				$(".propertiesfieldValueText").show();
			}
		}
	},
	getConditionString: function() {
		var compare = this.compare[this.properties.propertiesData.compare];
		var parentId = this.getHtml().parent().parent().attr("id");
		if(parentId) {
			var branchCondition = FormFlow.getStageById(parentId).properties.propertiesData.branchCondition;
		}
		if(branchCondition) {
			
			if(branchCondition == 'result') {
				var condition = "Result";
				var value = this.properties.propertiesData.resultValue;
			} else if (branchCondition == 'field') {
				var control = Form.getControl(FormFlow.getStageById(parentId).properties.propertiesData.selectField);
				if(control == null) {
					return "";
				}
				var condition = control.properties.caption;
				if(control.isSingleSelect() || control.isMultiSelect()) {
					var value =  this.properties.propertiesData.fieldValueDropDown;
				} else {
					var value = this.properties.propertiesData.fieldValueText;
				}
			}

		} else {
			return "";
		}
		
		conditionString = "[ " + condition + " " + compare + " " + value +" ]"
		return conditionString;
	},
	getDefaultString: function() {
		if(this.properties.propertiesData.isDefault == "on") {
			return "- DEFAULT -";
		} else {
			return "";
		}
	},
	verifyFormValidity : function() {
		var stageValid = {
				isValid: true,
				errors: {}
		}
		
		var messages = [];
		
		var parentId = this.getHtml().parent().parent().attr("id");
		var branchCondition = FormFlow.getStageById(parentId).properties.propertiesData.branchCondition;
		var parentStage = FormFlow.getStageById(parentId);
		var selectedControlId = parentStage.properties.propertiesData.selectField;
		var control = Form.getControl(selectedControlId);

		
		if(branchCondition == "result") {
			if(this.properties.propertiesData.compare == "" || this.properties.propertiesData.resultValue == "") {
				stageValid.isValid = false;
				messages = messages.concat("Please provide comparison operator and result value to determine branch condition");
			}
		} else if(branchCondition == "field" && control) {
			if(control.isSingleSelect() || control.isMultiSelect()) {
				if(this.properties.propertiesData.compare == "" || this.properties.propertiesData.fieldValueDropDown == "") {
					stageValid.isValid = false;
					messages = messages.concat("Please provide comparison operator and field value to determine branch condition");
				}
			} else {
				if(this.properties.propertiesData.compare == "" || this.properties.propertiesData.fieldValueText == "") {
					stageValid.isValid = false;
					messages = messages.concat("Please provide comparison operator and field value to determine branch condition");
				}
			}

		}

		if(this.properties.propertiesData.branchCondition == "field" && this.properties.propertiesData.selectField == "") {
			stageValid.isValid = false;
			messages = messages.concat("Please select the appropriate field to determine branch condition");
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

FormFlow.registerStageType(FQN, FormFlow.BranchColumn);

$(function() {
	$("#formFlowBranchColumnRemoveDialog").dialog({
		autoOpen: false,
		modal: true,
		title: 'Delete Branch Column',
		height: 170,
		zIndex: 9999,
		overlay: { 'background-color': '#000000', 'opacity': 0.5},
		buttons: {
			'OK': function(e) {
				$(this).dialog('close');
			}
		}
	});
});