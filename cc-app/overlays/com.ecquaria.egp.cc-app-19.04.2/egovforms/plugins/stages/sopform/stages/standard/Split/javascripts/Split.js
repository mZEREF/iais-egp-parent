var FQN = "sopform.stages.standard.Split";

FormFlow.Split= FormFlow.Stage.extend({
	FQN: FQN,
	htmlTmpl: FormFlow.getTemplate(FQN,"FormFlowSplit"),
	xmlTmpl: FormFlow.getTemplate(FQN,"FlowElementXmlSplit"),

	constructor: function(options, skipAddColumn) {
		this.base.apply(this, arguments);
		if (!skipAddColumn) {
			this.addColumn();
			this.addColumn();
		}
	},
	
	getXml : function() {
		var columns = [];
	
		$(this.getHtml()).children(".columnsList").children(".splitColumn").each(function() {
			var column = FormFlow.getStageById($(this).attr("id"));
			columns.push(column.getXml());
		});
		this.columns = columns;
		return Form.Common.mergeTemplate(this.xmlTmpl, this);
	},
	addColumn: function(options) {
		var newColumn = FormFlow.createStage("sopform.stages.standard.SplitColumn", options);
		$(this.getHtml()).children(".columnsList").append(newColumn.getHtml());
		FormFlow.triggerModified();

		return newColumn
	},
	arrangeColumn: function() {
		var $columnsList = $(this.getHtml()).children(".columnsList");
		var $children = $columnsList.children(".splitColumn");
		
		var childrenNo = $children.size();		
		var paddingWidth = parseInt($columnsList.css('padding-left'), 10) + parseInt($columnsList.css('padding-right'), 10);
		var dWidth = (childrenNo+1)*4;
		var totalColumnWidth = $columnsList.width() - paddingWidth - dWidth;

		var columnWidth = totalColumnWidth/childrenNo + "px";
		$children.width(columnWidth);
		$("#"+this.id+" .columnsList > .splitColumn > .stagesList > .formFlowBranch, #"+this.id+" .columnsList > .splitColumn > .stagesList > .formFlowSplit").each(function() {
			FormFlow.stages[$(this).attr("id")].arrangeColumn();
		});
		
		$children.equalizeCols();
	},
	initPropertiesPanel: function() {
		$("input[name='addColumn']").click(function() {
			var newColumn = FormFlow.getStageById(FormFlow.currentStage).addColumn();
			FormFlow.toggleStage(newColumn.getHtml());
		});
		
	}

},{});

FormFlow.registerStageType(FQN, FormFlow.Split);