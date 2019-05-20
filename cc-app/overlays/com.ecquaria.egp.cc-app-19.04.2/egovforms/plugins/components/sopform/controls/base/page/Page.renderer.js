(function($jq) {

var FQN = 'sopform.controls.base.Page';

var PageRendererCommon = {
	tmpl: Form.getTemplate(FQN),

	renderBody: function(control, placeholder) {
		this.renderElementCommon(placeholder, this.tmpl,
			{ control: control });
	},

	afterRender: function(control, placeholder) {
		this.base.apply(this, arguments);
		var elem = control.getElement();
		if (!elem) return;
		//Form.Theme.applyTheme(control, Form.properties.themeName);
	}
}

var PageRenderer = Form.Design.AbstractRenderer.extend($jq.extend({
	propertiesTemplate: Form.getTemplate(FQN, 'properties'),

	prepareControl: function(control) {
		this.base(control);
		var $table = control.getGrid();
		Form.makeSortableDroppableGrid($table.get(0), control.properties.cols, control.properties.width);
	},

	renderPropertiesBody: function(control, placeholder) {
		var input;
		var properties = this.base.apply(this, arguments);

		input = Form.Common.createFromTemplate(Form.Properties.radio, {
			id: control.properties.id,
			name: 'requiresLogin',
			value: Form.properties.requiresLogin,
			options: [['true', 'Yes'],['false', 'No']]
		});
		var alignment = input;
		SOP.SOPForms.setupRadio(alignment);
		$jq('input[type=button]', input).click(function(e) {
			Form.properties.requiresLogin = $jq(this).val();
			if(Form.properties.requiresLogin=='true'){
				$("#publishFormFor_tr").show();
			}
			else{
				$("#publishFormFor_tr").hide();
			}
		});
		
		properties['requiresLogin'] = input[0];
		
		var domain_options = [];
		var userDomains = Form.Service.getUserDomainNames();
		for (var i = 0, n = userDomains.length; i < n; i++) {
			var userDomain = userDomains[i];
			domain_options.push([userDomain, userDomain]);
		}
		
		input = Form.Common.createFromTemplate(Form.Properties.dropdown, {
			id: control.properties.id,
			name: 'publishFormFor',
			value: Form.properties.publishFormFor,
			options: domain_options
		}, null, {
			change: function(e) {
				Form.properties.publishFormFor = $jq(this).val();
			}
		});
		properties['publishFormFor'] = input[0];
		
		/*var themeOptions = [];
		$jq.each(Form.Theme.getThemeNames(), function(index, value) {
			themeOptions.push([value, value]);
		});

		input = Form.Common.createFromTemplate(Form.Properties.dropdown, {
			id: control.properties.id,
			name: 'theme',
			value: Form.properties.themeName,
			options: themeOptions
		}, null, {
			change: function(e) {
				var themeName = Form.properties.themeName = $jq(this).val();
				Form.Theme.removeTheme();
				Form.Theme.applyTheme(control, themeName);
				Form.traverse(function(control) {
					control.applyFont();
					// this is added to refresh the controls to get new CSS.
					// not sure if this would affect performance.
					control.refresh();
				});
			}
		});

		properties['theme'] = input[0];*/

		input = Form.Common.createFromTemplate(Form.Properties.dropdown, {
			id: control.properties.id,
			name: 'submissionMode',
			value: Form.properties.submissionMode,
			options: [
			    ['tab', 'Tab'],
			    ['wizard', 'Wizard']
			]
		}, null, {
			change: function(e) {
				Form.properties.submissionMode = $jq(this).val();
			}
		});

		properties['submissionMode'] = input[0];

		input = Form.Common.createFromTemplate(Form.Properties.dropdown, {
			id: control.properties.id,
			name: 'processingMode',
			value: Form.properties.processingMode,
			options: [
			    ['tab', 'Tab'],
			    ['wizard', 'Wizard']
			]
		}, null, {
			change: function(e) {
				Form.properties.processingMode = $jq(this).val();
			}
		});

		properties['processingMode'] = input[0];

		// 1
		input = Form.Common.createFromTemplate(Form.Properties.textbox, {
			id: control.properties.id,
			name: 'title',
			value: control.properties.title
		}, null, {
			'keydown paste' : function(e) {
				Form.Common.invokeLater(function(e, control) {
					control.properties.title = $jq(this).val();
					Form.setSelectedPageTabText(control.properties.title);
				}, { context: this, uid: 'changeProperty' }, e, control);
			}
		});

		properties['title'] = input[0];

		// 2
		input = Form.Common.createFromTemplate(Form.Properties.textbox, {
			id: control.properties.id,
			name: 'cols',
			value: control.properties.cols
		}, null, {
			'keydown paste' : function(e) {
				Form.Common.invokeLater(function(e, control) {
					control.properties.cols = Form.Util.toInteger($jq(this).val(), null);
					control.refresh();
					Form.Design.choose(control.getElement(), false);
				}, { context: this, uid: 'changeProperty' }, e, control);
			}
		});

		properties['cols'] = input[0];

		input = Form.Common.createFromTemplate(Form.Properties.textbox, {
			id: control.properties.id,
			name: 'width',
			value: control.properties.width
		}, null, {
			blur: function(e) {
				control.properties.width = $jq(this).val();
				control.refresh();
				Form.Design.choose(control.getElement(), false);
			}
		});

		properties['width'] = input[0];

		// 3
		input = Form.Common.createFromTemplate(Form.Properties.radio, {
			id: control.properties.id,
			name: 'captionOrientation',
			value: control.properties.captionOrientation,
			options: [
				['horizontal', 'Horizontal'],
				['vertical', 'Vertical']
			]
		});
		var alignment = input;
		SOP.SOPForms.setupRadio(alignment);
		$jq('input[type=button]', input).click(function(e) {
			control.properties.captionOrientation = $jq(this).val();
			control.refresh();
			Form.Design.choose(control.getElement(), false);
		});

		properties['captionOrientation'] = input[0];
		
		// 4
		input = Form.Common.createFromTemplate(Form.Properties.button, {
			id: control.properties.id,
			name: 'saveAsTemplate',
			value: 'Save as Template',
			cssClass: "page-bttn"
		}, null, {
			click: function(e) {
				var templateSaveDialog = $jq('#pageTemplateSaveDialog');
				
				if (!templateSaveDialog.is('.ui-dialog-content')) {
					templateSaveDialog.dialog({
						autoOpen: false,
						modal: true,
						title: 'Save as Page Template',
						height: 170,
						zIndex: 9999,
						overlay: { 'background-color': '#000000', 'opacity': 0.5},
						buttons: {
							'Add': function(e) {
								var that = this;
								$jq(this).find('> .sopform-error').empty();
								var elements = $jq(this).find(':input');
								var templateName, templateLabel, errors = [];
						
								elements.each(function() {
									switch($jq(this).attr('name')) {
										case 'templateName':
											if ($jq(this).val()) templateName = $jq(this).val();
										break;
										case 'templateLabel':
											if ($jq(this).val()) templateLabel = $jq(this).val();
										break;
									}
								});
								
								if (!templateName) errors.push('Template name is required');
								if (!templateLabel) errors.push('Template label is required');
						
								// TODO show validation error
								if (errors.length == 0) {
									var control = Form.currentPage;
									if (!control) return;
									var isOverride = false;
									for(var i in Form.Template.TEMPLATES) {
										if(Form.Template.TEMPLATES[i].type == "page" && i == templateName) {
											SOP.Common.confirm({
												message: "The template ["+ templateName +"] already exists, do you want to overwrite it?",
												func: function() {
													var serializer = Form.getSerializer(control.FQN); 
													var pageContent = serializer.serialize(control, false);
													
													var saveTemplateAjax = new SOPAjax("saveTemplate");
													saveTemplateAjax.enable_indicator = false;
													var param_delimeter = "=^|^=";
													var parameter = templateName + param_delimeter + '<Template name="' + templateName
															+ '" label="' + templateLabel + '" type="page">'
															 + pageContent + '</Template>';
													saveTemplateAjax.parameter = parameter;
													saveTemplateAjax.param_delimeter = param_delimeter;
													saveTemplateAjax.callback(function() {
														// reload the javascript from /sop/sopforms/TemplateScript.jsp and run the script
														$.getScript(sopFormContext+'TemplateScript.jsp');
													})
													$jq(that).dialog('close');
												}
											});
											isOverride = true;
											break;
										}
									}
									if(!isOverride) {
										var serializer = Form.getSerializer(control.FQN); 
										var pageContent = serializer.serialize(control, false);
										
										var saveTemplateAjax = new SOPAjax("saveTemplate");
										saveTemplateAjax.enable_indicator = false;
										var param_delimeter = "=^|^=";
										var parameter = templateName + param_delimeter + '<Template name="' + templateName
												+ '" label="' + templateLabel + '" type="page">'
												 + pageContent + '</Template>';
										saveTemplateAjax.parameter = parameter;
										saveTemplateAjax.param_delimeter = param_delimeter;
										saveTemplateAjax.callback(function() {
											// reload the javascript from /sop/sopforms/TemplateScript.jsp and run the script
											$.getScript(sopFormContext+'TemplateScript.jsp');
										})
										$jq(this).dialog('close');
									}
								} else {
									var errorDiv = $jq(dialog).find('.sopform-error');
									var ul = $jq('<ul></ul>');
									$jq.each(errors, function(idx, value) {
										var li = $jq('<li></li>');
										li.text(value);
										ul.append(li);
									});
									errorDiv.append(ul);
								}
							},
							'Cancel': function(e) {
								$jq(this).dialog('close');
							}
						}
					});
				}
						
				templateSaveDialog.find('[name=templateName]').val("");
				templateSaveDialog.find('[name=templateLabel]').val("");		
				templateSaveDialog.find('> .sopform-error').empty();
				templateSaveDialog.dialog('open');
			}
		});
		
		properties['saveAsTemplate'] = input[0];

		// for lock field
		input = Form.Common.createFromTemplate(Form.Properties.checkbox, 
				componentLockManager.constructArguments(control));
		properties['component_lock'] = input[0];
		componentLockManager.setupTriggers(input, control);
		
		// for condition properties
		input = Form.Common.createFromTemplate(Form.Properties.conditions,
				conditionManager.constructArguments(control), null, null);
		conditionManager.setupTriggers(input);

		properties['conditions-properties'] = input[0];

		this.renderPropertiesCommonWithTabSupport(placeholder, this.propertiesTemplate, properties, true);

		//setup login and publish for properties.
		if(Form.properties.requiresLogin=='true'){
			$("#publishFormFor_tr").show();
		}
		else{
			$("#publishFormFor_tr").hide();
		}
		
		componentLockManager.setupAll(control);
		conditionManager.setupAll(control);
	}
}, PageRendererCommon));

Form.registerRenderer(FQN, 'design', new PageRenderer());

// RUNTIME
var PageRuntimeRenderer = Form.Runtime.AbstractRenderer.extend($jq.extend({
	prepareControl: function(control) {
		var $grid = control.getGrid();
		$grid.grid({ cols: control.properties.cols, width: control.properties.width });
	}
}, PageRendererCommon));

Form.registerRenderer(FQN, 'runtime', new PageRuntimeRenderer());

// PRINTER FRIENDLY
var PagePrinterFriendlyRenderer = Form.Runtime.AbstractRenderer.extend({
	tmpl: Form.getTemplate(FQN, 'printerFriendly'),

	renderBody: function(control, placeholder) {
		this.renderElementCommon(placeholder, this.tmpl,
			{ control: control });
	},

	prepareControl: function(control) {
		var $grid = control.getGrid();
		$grid.grid({ cols: control.properties.cols });
	},

	afterRender: function(control, placeholder) {
		this.base.apply(this, arguments);
		var elem = control.getElement();
		if (!elem) return;
		Form.Theme.applyTheme(control, Form.properties.themeName);
	}
});

Form.registerRenderer(FQN, 'printerFriendly', new PagePrinterFriendlyRenderer());
}(jQuery));