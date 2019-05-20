var FQN = "sopform.stages.base.BaseStage";

FormFlow.Stage = Base.extend({
	id : "stageId",
	html : "",
	FQN: FQN, 

	htmlTmpl: FormFlow.getTemplate(FQN,"FormFlowStage"),
	xmlTmpl: FormFlow.getTemplate(FQN,"FlowElementXmlStage"),
	propertiesTmpl: FormFlow.getTemplate(FQN,"BaseStage.properties"),
	tooltipTmpl: FormFlow.getTemplate(FQN, "FlowItemTooltip"),
	
	constructor : function(options) {
		if (options && options.id) {
			this.id = options.id;
		} else {
			this.id = "stage_" + FormFlow.stageCounter++;
		}
		
		this.properties = {
		};

		if(options) {
			if (options.propertiesData) {
				this.properties.propertiesData = options.propertiesData;
			}
			
			if(options.definition) {
				this.definition = options.definition;
			}
		}
		this.html = $(this.createHtml()).click(function() {
			FormFlow.toggleStage(this);
			return false;
		});
		
		if(this.definition.draggable && this.definition.draggable == true) {
			$(this.getHtml()).draggable($.extend({},FormFlow.Design.draggableOptions, {
				cursorAt: {
				top: 0,
				left: 10
				}
			}));
		}

		FormFlow.registerStage(this);
		
		if(this.afterElementRendered) {
			this.afterElementRendered();
		}
		
		var propertiesData = {
				stageInstId: this.id,
				stageDef: FormFlow.stageType[this.FQN].definition,
				userGroups: FormFlow.userGroups
		};
		this.propertiesPanel = Form.Common.mergeTemplate(this.propertiesTmpl, propertiesData);
		FormFlow.emptyPropertiesPanel();
		if(!this.properties.propertiesData) {
			this.properties.propertiesData = $("#flowPropertiesForm").formHash();
		}
		FormFlow.updatePropertiesPanel(this);
		if(this.afterPropertiesPanelSet) {
			this.afterPropertiesPanelSet();
		}
		if($("#flowPropertiesForm").find("input[name='stageName']").getValue() == '') {
			if(this.FQN == "sopform.stages.standard.SplitColumn" || this.FQN == "sopform.stages.standard.BranchColumn" ) {
				if(this.FQN == "sopform.stages.standard.BranchColumn") {
					var name = this.id.replace(/stage_/g,"BranchColumn_");
					this.getHtml().children(".branchColumnHeader").children(".formFlowName").html(name);
				} else if (this.FQN == "sopform.stages.standard.SplitColumn") {
					var name = this.id.replace(/stage_/g,"SplitColumn_");
					this.getHtml().children(".splitColumnHeader").children(".formFlowName").html(name);
				}
				$("#flowPropertiesForm").find("input[name='stageName']").setValue(name);
				this.properties.propertiesData.stageName = name;

			} else {
				$("#flowPropertiesForm").find("input[name='stageName']").setValue(this.id);
				this.properties.propertiesData.stageName = this.id;
			}
		}

		this.propertiesPanel = $("#flowProperties-content").html();


	},
	getHtml : function() {
		if(this.html == "") {
			this.html = this.createHtml();
		}
		return this.html;
	},
	createHtml : function() {
		var html = Form.Common.mergeTemplate(
				this.htmlTmpl, this);
		return html;
	},
	getXml: function() {
		return Form.Common.mergeTemplate(this.xmlTmpl, this);
	}
}, {
});

FormFlow.registerStageType(FQN, FormFlow.Stage);

