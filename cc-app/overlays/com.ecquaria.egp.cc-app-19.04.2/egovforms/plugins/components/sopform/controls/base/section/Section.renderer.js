(function($jq) {

var FQN = 'sopform.controls.base.Section';

var SectionRendererCommon = {
	renderBody: function(control, placeholder) {
		// runtime implementation exists - do changes there too.
		this.renderElementCommon(placeholder, this.tmpl,
				{ control: control });
	}
}

var SectionRenderer = Form.Design.AbstractRenderer.extend($jq.extend({
	tmpl: Form.getTemplate(FQN),

	propertiesTemplate: Form.getTemplate(FQN, 'properties'),

	prepareControl: function(control) {
		this.base(control);
		var elem = control.getElement();
		var handle = $jq('<div class="section-handle"><div><img class="section-select" src="' + sopFormTheme + 'images/general/empty.gif" title="select" /></div></div>');
		handle.prependTo($jq(elem));
		handle.click(function(e) {
			e.stopPropagation();
			Form.Design.choose(elem);
		});
		var $table = control.getGrid();
		Form.makeSortableDroppableGrid($table.get(0), control.properties.cols, control.properties.width);
	},

	renderPropertiesBody: function(control, placeholder) {
		var input;
		var properties = this.base.apply(this, arguments);

		// 1
		input = Form.Common.createFromTemplate(Form.Properties.textbox, {
			id: control.properties.id,
			name: 'name',
			value: control.properties.name
		}, null, {
			blur: function(e) {
				control.properties.name = $jq(this).val();
			}
		});

		properties['name'] = input[0];

		// 2
		input = Form.Common.createFromTemplate(Form.Properties.textbox, {
			id: control.properties.id,
			name: 'title',
			value: control.properties.title
		}, null, {
			'keydown paste': function(e) {
				Form.Common.invokeLater(function(e, control) {
					var oldTitle = control.properties.title;
					var title = control.properties.title = $jq(this).val();
					
					if (!control.properties.border && ((oldTitle && !title) || (!oldTitle && title))) {
						Form.Design.refresh(control);
						return;
					}
				
					var $title;
					if (control.properties.border) {
						$title = $jq('legend.control-font-header:eq(0)', control.getElement());
					} else {
						$title = $jq('.control-font-header>label:eq(0)', control.getElement());
					}
					
					if ($title && $title.length && Form.getControl($title.get(0)) == control) {
						$title.text(title);
					}
				}, { context: this, uid: 'changeProperty' }, e, control);
			}
		});

		properties['title'] = input[0];
		
		// 3
		input = Form.Common.createFromTemplate(Form.Properties.textbox, {
			id: control.properties.id,
			name: 'cols',
			value: control.properties.cols
		}, null, {
			'keydown paste': function(e) {
				Form.Common.invokeLater(function(e, control) {
					control.properties.cols = Form.Util.toInteger($jq(this).val(), null);
					control.refresh();
					Form.Design.choose(control.getElement(), false);
				}, { context: this, uid: 'changeProperty' }, e, control);
			}
		});

		properties['cols'] = input[0];

		// 4
		input = Form.Common.createFromTemplate(Form.Properties.radio, {
			id: control.properties.id,
			name: 'captionOrientation',
			value: control.properties.captionOrientation,
			options: [
				['horizontal', 'Horizontal'],
				['vertical', 'Vertical']
			]
		});

		var captionOrientation = input;
		SOP.SOPForms.setupRadio(captionOrientation);
		$jq('input[type=button]', input).click(function(e) {
			control.properties.captionOrientation = $jq(this).val();
			control.refresh();
			Form.Design.choose(control.getElement(), false);
		});

		properties['captionOrientation'] = input[0];

		// 5
		input = Form.Common.createFromTemplate(Form.Properties.radio, {
			id: control.properties.id,
			name: 'border',
			value: '' + control.properties.border,
			options: [
				['true', 'True'],
				['false', 'False']
			]
		});

		var border = input;
		SOP.SOPForms.setupRadio(border);
		$jq('input[type=button]', input).click(function(e) {
			control.properties.border = $jq(this).val() == 'true';
			control.refresh();
			Form.Design.choose(control.getElement(), false);
		});

		properties['border'] = input[0];

		// 6
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

		// 6
		input = Form.Common.createFromTemplate(Form.Properties.textbox, {
			id: control.properties.id,
			name: 'hint',
			value: control.properties.hint
		}, null, {
			blur: function(e) {
				control.properties.hint = $jq(this).val();
			}
		});

		properties['hint'] = input[0];

		// 7
		input = Form.Common.createFromTemplate(Form.Properties.textarea, {
			id: control.properties.id,
			name: 'help',
			value: control.properties.help
		}, null, {
			blur: function(e) {
				control.properties.help = $jq(this).val();
			}
		});

		properties['help'] = input[0];
		
		// 8
		input = Form.Common.createFromTemplate(Form.Properties.button, {
			id: control.properties.id,
			name: 'saveAsTemplate',
			value: 'Save as Template',
			cssClass:	"page-bttn"
		}, null, {
			click: function(e) {
				var templateSaveDialog = $jq('#sectionTemplateSaveDialog');
				
				if (!templateSaveDialog.is('.ui-dialog-content')) {
					templateSaveDialog.dialog({
						autoOpen: false,
						modal: true,
						title: 'Save as Template',
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
									var control = Form.getControl($('.control-choosen', Form.currentPage.getElement())[0]);
									if (!control) return;
									
									var isOverride = false;
									for(var i in Form.Template.TEMPLATES) {
										if(Form.Template.TEMPLATES[i].type == "section" && i == templateName) {
											SOP.Common.confirm({
												message: "The template ["+ templateName +"] already exists, do you want to overwrite it?",
												func: function() {
													var serializer = Form.getSerializer(control.FQN); 
													var sectionContent = serializer.serialize(control, false);
													
													var saveTemplateAjax = new SOPAjax("saveTemplate");
													saveTemplateAjax.enable_indicator = false;
													var param_delimeter = "=^|^=";
													var parameter = templateName + param_delimeter + '<Template name="' + templateName
															+ '" label="' + templateLabel + '" type="section">'
															 + sectionContent + '</Template>';
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
										var sectionContent = serializer.serialize(control, false);
										
										var saveTemplateAjax = new SOPAjax("saveTemplate");
										saveTemplateAjax.enable_indicator = false;
										var param_delimeter = "=^|^=";
										var parameter = templateName + param_delimeter + '<Template name="' + templateName
										+ '" label="' + templateLabel + '" type="section">'
										+ sectionContent + '</Template>';
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
		
		// for developer properties
		input = Form.Common.createFromTemplate(Form.Properties.developer,
				developerManager.constructArguments(control), null, null);
		developerManager.setupTriggers(input, control);
		properties['developer-properties'] = input[0];
		
		// for attachment properties
		input = Form.Common.createFromTemplate(Form.Properties.checkbox, 
				attachmentManager.constructArguments(control));
		properties['file_attachment'] = input[0];
		attachmentManager.setupTriggers(input, control);
		
		// for condition properties
		input = Form.Common.createFromTemplate(Form.Properties.conditions,
				conditionManager.constructArguments(control), null, null);
		conditionManager.setupTriggers(input);

		properties['conditions-properties'] = input[0];

		this.renderPropertiesCommonWithTabSupport(placeholder, this.propertiesTemplate, properties, true);

		componentLockManager.setupAll(control);
		conditionManager.setupAll(control);
	}
}, SectionRendererCommon));

Form.registerRenderer(FQN, 'design', new SectionRenderer());

// RUNTIME
var SectionRuntimeRenderer = Form.Runtime.AbstractRenderer.extend($jq.extend({
	tmpl: Form.getTemplate(FQN, 'runtime'),

	renderBody: function(control, placeholder) {
		this.renderElementCommon(placeholder, this.tmpl,
				{ control: control });
	},

	prepareControl: function(control) {
		var $grid = control.getGrid();
		$grid.grid({ cols: control.properties.cols, width: control.properties.width });
	}

}, SectionRendererCommon));

Form.registerRenderer(FQN, 'runtime', new SectionRuntimeRenderer());

// PRINTER FRIENDLY
var SectionPrinterFriendlyRenderer = Form.Runtime.AbstractRenderer.extend($jq.extend({
	tmpl: Form.getTemplate(FQN, 'printerFriendly'),
	
	renderBody: function(control, placeholder) {
		this.renderElementCommon(placeholder, this.tmpl, { 
			control: control, 
			instructions: sopformsApi.getInstruction(control.properties.id)
		});
	},

	prepareControl: function(control) {
		var $grid = control.getGrid();
		$grid.grid({ cols: control.properties.cols });
	}

}));

Form.registerRenderer(FQN, 'printerFriendly', new SectionPrinterFriendlyRenderer());
}(jQuery));