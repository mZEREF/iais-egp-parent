(function($jq) {

var FQN = 'sopform.controls.base.Serializer';

Form.Design.AbstractSerializer = Base.extend({
	tmpl: null,
	aclPropertiesTemplate: Form.getTemplate(FQN, 'aclProperties'),
	fontPropertiesTemplate: Form.getTemplate(FQN, 'fontProperties'),

	serialize: function(control, includeId) {
		includeId = (includeId === false) ? false : true;
		
		var childrenXml = null;
		if (control.children && control.children.length) {
			var children = [];
			$jq(control.children).each(function() {
				var serializer = Form.getSerializer(this.FQN);
				children.push(serializer.serialize(this, includeId));
			});
			childrenXml = children.join('\n');
		}

		var result = null;
		if (control.isContainer() || childrenXml != null) {
			result = Form.Common.mergeTemplate(this.tmpl,
				{ serializer: this, control: control, children: childrenXml, includeId: includeId });
		} else {
			result = Form.Common.mergeTemplate(this.tmpl,
				{ serializer: this, control: control, children: null, includeId: includeId });
		}

		return result;
	},

	serializeControlId: function(control, includeId) {
		if (includeId) {
			return Form.Common.mergeTemplate('<Entry name="id"><String>${id}</String></Entry>',
				{ id: control.properties.id });
		}else{
			return Form.Common.mergeTemplate('<Entry name="__preid"><String>${__preid}</String></Entry>',
					{ __preid: control.properties.id });
		}
	},
	
	serializeAclProperties: function(control) {
		return Form.Common.mergeTemplate(this.aclPropertiesTemplate,
			{ control: control });
	},

	serializeFontProperties: function(control) {
		return Form.Common.mergeTemplate(this.fontPropertiesTemplate,
			{ control: control });
	}
});

Form.Serializer = {
	formPropertiesTemplate: Form.getTemplate(FQN, 'formProperties'),
	elementTemplate: Form.getTemplate(FQN, 'element'),
	modelTemplate: Form.getTemplate(FQN, 'model'),
	globalElementXmlTemplate: Form.getTemplate(FQN, 'globalXml'),
	groupingElementXmlTemplate: Form.getTemplate(FQN, 'groupingXml'),
	caseSlaElementXmlTemplate: Form.getTemplate(FQN, 'caseSlaXml'),
	milestoneSlaElementXmlTemplate: Form.getTemplate(FQN, 'milestoneSlaXml'),
	slaSpecsElementXmlTemplate: Form.getTemplate(FQN, 'slaSpecsXml'),
	stageSlaElementXmlTemplate: Form.getTemplate(FQN, 'stageSlaXml'),

	getElementXml: function() {
		var initValueString = '{}';
		if (Form.initValues) {
			initValueString = new mJSON().stringify(Form.initValues);
		}
		var formProperties = Form.Common.mergeTemplate(this.formPropertiesTemplate,
			{ control: Form, initValueString: initValueString });

		var childrenXml = '';
		var children = [];
		$jq.each(Form.PAGES, function() {
			var serializer = Form.getSerializer(this.FQN);
			children.push(serializer.serialize(this));
		});

		childrenXml = children.join('\n');

		var result = Form.Common.mergeTemplate(this.elementTemplate,
			{ formProperties: formProperties, children: childrenXml });
		return result;
	},

	getModelXml: function() {
		var refNames = [];
		var self = this;
		$jq.each(Form.PAGES, function() {
			self.internalToModelXml(this, refNames);
		});

		var result = Form.Common.mergeTemplate(this.modelTemplate,
			{ refNames: refNames });
		return result;
	},

	internalToModelXml: function(control, refNames) {
		if (control.isContainer()) {
			var self = this;
			$jq.each(control.children || [], function() {
				self.internalToModelXml(this, refNames);
			});
		} else {
			refNames.push(control.properties.name);
		}
	},

	getGlobalXml: function(data) {
		var name = $jq('#formName').text();
		var data = { name: name, majorVersion: 1, minorVersion: 1, properties: [] };
		var result = Form.Common.mergeTemplate(
			Form.Serializer.globalElementXmlTemplate, data);
		return result;
	},
	
	getGroupingXml: function(){
		var MILESTONES = FormSLA.MILESTONES || [];
		var milestones = [];
		for (var i = 0, n = MILESTONES.length; i < n; i++) {
			var m = MILESTONES[i];
			var stages = [];
			stages.push.apply(stages, m.stages);

			var stageIds = [];
			$.each(m.stages, function() {
				stageIds.push('#' + this.id);
			});
			
			var $nestedStages = $(stageIds.join()).find('.formFlowStage');
			$nestedStages.each(function() {
				var stage = FormFlow.stages[this.id];
				stages.push(stage);
			});
			
			milestones.push({ id: m.id, stages: stages });
		}
		var data = {
			segments: FormSLA.SEGMENTS || [],
			milestones: milestones
		};
		var result = Form.Common.mergeTemplate(
				Form.Serializer.groupingElementXmlTemplate, data);
		return result;
	},
	
	getCaseSlaXml: function() {
		var slaConfig = FormSLA.caseSlaConfig;
		var data = {
			expectedResponseTimeValue: slaConfig.expectedResponseTimeValue,
			expectedResponseTimeUnit: slaConfig.expectedResponseTimeUnit,
			responseTimeWarning: slaConfig.responseTimeWarning,
			warningPercentage: slaConfig.warningPercentage,
			failPercentage: slaConfig.failPercentage,
			alerts: slaConfig.alerts || [],
			triggers: slaConfig.triggers || []
		};
		var result = Form.Common.mergeTemplate(
				Form.Serializer.caseSlaElementXmlTemplate, data);
		return result;
	},
	
	getMilestoneSlaXml: function() {
		var milestones = FormSLA.MILESTONES || [];
		var data = {
			milestones: milestones
		};
		var result = Form.Common.mergeTemplate(
				Form.Serializer.milestoneSlaElementXmlTemplate, data);
		return result;
	},

	getStageSlaXml: function() {
		var stages = FormSLA.getStages();
		var _stages = [];
		for (var i = 0, n = stages.length; i < n; i++) {
			var id = stages[i].id;
			var config = FormSLA.stagesSlaConfig[id];
			var _stage = { id: id, config: config };
			_stages.push(_stage);
		}
		
		var data = {
			stages: _stages
		};
		var result = Form.Common.mergeTemplate(
				Form.Serializer.stageSlaElementXmlTemplate, data);
		return result;
	},

	getSlaSpecsXml: function() {
		var data = {
			caseSlaXml: this.getCaseSlaXml(),
			milestoneSlaXml: this.getMilestoneSlaXml(),
			stageSlaXml: this.getStageSlaXml()
		};
		var result = Form.Common.mergeTemplate(
				Form.Serializer.slaSpecsElementXmlTemplate, data);
		return result;
	}
}

}(jQuery));