var FQN = "sopform.stages.standard.Branch";

FormFlow.Branch = FormFlow.Stage.extend({
	FQN: FQN,
	htmlTmpl: FormFlow.getTemplate(FQN,"FormFlowBranch"),
	xmlTmpl: FormFlow.getTemplate(FQN,"FlowElementXmlBranch"),
	
	constructor: function(options, skipAddColumn) {
		this.base.apply(this, arguments);
		if (!skipAddColumn) {
			this.addColumn();
			this.addColumn();
		}
	},
	
	getXml : function() {
		var columns = [];

		$(this.getHtml()).children(".columnsList").children(".branchColumn").each(function() {
			var column = FormFlow.getStageById($(this).attr("id"));
			columns.push(column.getXml());
		});
		this.columns = columns;
		return Form.Common.mergeTemplate(this.xmlTmpl, this);
	},
	addColumn: function(options) {
		var newColumn = FormFlow.createStage("sopform.stages.standard.BranchColumn",options);
		$(this.getHtml()).children(".columnsList").append(newColumn.getHtml());
		FormFlow.triggerModified();
		
		return newColumn
	},
	generateColumns: function() { 
		var prevStage = $(this.getHtml()).prevAll(".formFlowStage:first");
		var prevStageId = $(this.getHtml()).prevAll(".formFlowStage:first").attr('id');
		if(!prevStageId) {
			prevStageId = $(this.getHtml()).parent().prevAll(".formFlowStage:first").attr('id');
		}

		var prevStageResult = FormFlow.getStageById(prevStageId).definition.stageResult;

		if(FormFlow.getStageById(prevStageId).properties.propertiesData.customizedButton_value && FormFlow.getStageById(prevStageId).properties.propertiesData.customizedButton_value != "undefined") {
			var prevStageBtnResult = FormFlow.getStageById(prevStageId).properties.propertiesData.customizedButton_value.split(',');
			for(var key in prevStageBtnResult) {
				if(key != 'remove' && key != 'insert' && key != 'indexOf') {
					prevStageResult.push({'result': prevStageBtnResult[key]});
				}
			}
		}
		
		var results = [];
		for(var key in prevStageResult) {
			if(key != 'remove' && key != 'insert' && key != 'indexOf') {
				results.push(prevStageResult[key].result);
			}
		}
		
		$(this.getHtml()).children(".columnsList").children(".branchColumn").remove();
		for(var i =0;i<results.length;i++) {
			if(results[i] == undefined) {
				continue;
			}
			var newColumn = this.addColumn({propertiesData: {resultValue: results[i], compare: "eq"}});
			$(newColumn.getHtml()).children(".branchColumnHeader").children(".formFlowName").html(newColumn.properties.propertiesData.stageName);
			$(newColumn.getHtml()).children(".branchColumnHeader").children(".branchColumnCondition").html(newColumn.getConditionString());
			$(newColumn.getHtml()).children(".branchColumnHeader").children(".branchDefaultCondition").html(newColumn.getDefaultString());			
		}
		FormFlow.emptyPropertiesPanel();
		FormFlow.updatePropertiesPanel(this);
		
	},
	arrangeColumn: function() {
		var $columnsList = $(this.getHtml()).children(".columnsList");
		var $children = $columnsList.children(".branchColumn");
		var childrenNo = $children.size();
		var paddingWidth = parseInt($columnsList.css('padding-left'), 10) + parseInt($columnsList.css('padding-right'), 10);
		var dWidth = (childrenNo+1)*4;
		var totalColumnWidth = $columnsList.width() - paddingWidth - dWidth;

		var columnWidth = totalColumnWidth/childrenNo + "px";
		$children.width(columnWidth);
		$("#"+this.id+" > .columnsList > .branchColumn > .stagesList > .formFlowBranch, #"+this.id+" > .columnsList > .branchColumn > .stagesList > .formFlowSplit").each(function() {
			FormFlow.stages[$(this).attr("id")].arrangeColumn();
		});
		
		$(this.getHtml()).children(".columnsList").children(".branchColumn").equalizeCols();
	},

	afterPropertiesPanelSet: function() {
		// hide properties
		$(".properties---generateColumn," +
			".properties---selectField").hide();
	},
	initPropertiesPanel: function() {
		var refreshHideShow = function() {
			if($("input[name='branchCondition']").getValue() =='result') {
				$(".properties---generateColumn").show();
				$(".properties---selectField").hide();
			} else if($("input[name='branchCondition']").getValue() == 'field') {
				$(".properties---generateColumn").hide();
				$(".properties---selectField").show();
			}
		};
		
		var options = {};
		Form.traverse(function(arg) {
			if(arg.isSingleInput() || arg.isSingleSelect() || arg.isMultiSelect()) {
				options[arg.properties.id] = arg.properties.caption; 
			}
		});
		var selectFieldValue = this.properties.propertiesData.selectField;
		$("select[name='selectField']").empty().addOption(options);
		if(selectFieldValue)
			$("select[name='selectField']")[0].value = selectFieldValue;
		if(!selectFieldValue) {
			this.properties.propertiesData.selectField = $("select[name='selectField']").val();
		}
		
		$("input[name='branchCondition']").change(refreshHideShow);
		
		$("input[name='generateColumn']").bind("click", this, function(e) {
			if($(e.data.getHtml()).find(".formFlowStage, .formFlowBranch, .formFlowSplit").size()>0) {
				$("#formFlowBranchGenerateColumnDialog").dialog("open");
			} else {
				e.data.generateColumns();
			}
		});

		$("input[name='addColumn']").click(function() {
			var newColumn = FormFlow.getStageById(FormFlow.currentStage).addColumn();
			FormFlow.toggleStage(newColumn.getHtml());
		});
		var prevStageId = $(this.getHtml()).prevAll(".formFlowStage:first").attr('id');
		if(!prevStageId) {
			prevStageId = $(this.getHtml()).parent().prevAll(".formFlowStage:first").attr('id');
		}
		if(prevStageId && $("input[name='branchCondition']").getValue() == "") {
			$("input[name='branchCondition']").setValue( FormFlow.getStageById(prevStageId).definition.stageResult['0'] ? 'result' : 'field')
			this.properties.propertiesData.branchCondition = $("input[name='branchCondition']").getValue();
		}

		$("input[name='branchCondition'][value='result']").removeAttr("disabled");
		$("input[name='generateColumn']").removeAttr("disabled");
		
		if(!prevStageId || !(FormFlow.getStageById(prevStageId).definition.stageResult['0'] || FormFlow.getStageById(prevStageId).properties.propertiesData.customizedButton_value)) {
			$("input[name='branchCondition'][value='result']").attr("disabled", true);
			$("input[name='generateColumn']").attr("disabled", true);
		} 
		
		
		
		refreshHideShow();

	},
	verifyFormValidity : function() {
		var stageValid = {
				isValid: true,
				errors: {}
		}
		
		var messages = [];
		
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

FormFlow.registerStageType(FQN, FormFlow.Branch);

$(function() {
	$("#formFlowBranchGenerateColumnDialog").dialog({
		autoOpen: false,
		modal: true,
		title: 'Generate Column Confirmation',
		height: 170,
		zIndex: 9999,
		overlay: { 'background-color': '#000000', 'opacity': 0.5},
		buttons: {
			'Cancel': function(e) {
				$(this).dialog('close');
			},
			'Generate': function(e) {
				FormFlow.getStageById(FormFlow.currentStage).generateColumns();
				$(this).dialog('close');
			}
		}
	});
});