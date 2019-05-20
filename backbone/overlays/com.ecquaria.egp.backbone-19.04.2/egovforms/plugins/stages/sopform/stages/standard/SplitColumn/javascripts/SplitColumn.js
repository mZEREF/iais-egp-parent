var FQN = "sopform.stages.standard.SplitColumn";

FormFlow.SplitColumn = FormFlow.Stage.extend({
	FQN: FQN,
	htmlTmpl: FormFlow.getTemplate(FQN,"FormFlowSplitColumn"),
	xmlTmpl: FormFlow.getTemplate(FQN,"FlowElementXmlSplitColumn"),
	append: function(content) {
		$(this.getHtml()).children(".stagesList").append(content);
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
		if($(this.getHtml()).siblings(".splitColumn").size() < 2 ) {
			$("#formFlowSplitColumnRemoveDialog").dialog("open");
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
	}
},{});

FormFlow.registerStageType(FQN, FormFlow.SplitColumn);

$(function() {
	$("#formFlowSplitColumnRemoveDialog").dialog({
		autoOpen: false,
		modal: true,
		title: 'Delete Split Column',
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