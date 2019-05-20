(function($jq) {

var FQN = 'sopform.controls.base.RepeatableRow';

var RepeatableRowRenderer = Form.Design.AbstractRenderer.extend({
	tmpl: Form.getTemplate(FQN),

	propertiesTemplate: Form.getTemplate(FQN, 'properties'),

	afterRender: function(control, placeholder) {
		this.base.apply(this, arguments);

		var elem = control.getElement();
		if (!elem) return;
		// disable all input since we're in design mode
		var $control = $jq('.repeatable-row-control-button', elem);
		$control.css('position', 'relative');
		var $mask = $('<div class="control-mask"></div>');
		$mask.appendTo($control);
	},

	renderBody: function(control, placeholder) {
		// runtime implementation exists - do changes there too.
		this.renderElementCommon(placeholder, this.tmpl,
				{ control: control });
	},

	constructPopulateDialog: function(control){
		// check if it already exists.
		if($("#populateDialog").length > 0){
			//return;
			$("#populateDialog").remove();
		}
		
		var template = $('<div class="flora" id="populateDialog" class="flora"><table style="width:100%"><tbody><tr><td>Copy and paste the cvs formatted content in this box.</td></tr><tr><td><textarea rows="25" style="width:100%;"></textarea></td></tr></tbody></table></div>');
		template.appendTo("body");
		
		$("#populateDialog").dialog({
			autoOpen : false,
			modal : true,
			title : 'Populate Repeatable Row',
			width : 480,
			height : 480,
			zIndex : 9999,
			closeOnEscape: false,
			overlay : {
				'background-color' : '#000000',
				'opacity' : 0.5
			},
			buttons : {
				'Populate' : function(e) {
					var csvFormattedString = $("#populateDialog").find("textarea").val();
					//console.log("formattedString: " + csvFormattedString );
					
					/* 
					 * Have to call csv twice on the same string as a workaround.
					 * 
					 * lets say you ( copy and paste / type out )a csv string into the textbox, the first
					 * call to $.csv()(csvFormattedString) will give you a list of length 1,
					 * call it again, you would have a list of length equal to the number
					 * of lines in the given csv.
					 */
					var list = $.csv()(csvFormattedString);
					list = $.csv()(csvFormattedString);		
					
					var controlIds = control.getAllAvailableControlId();
					if((controlIds && controlIds.length > 0) &&
							(list && list.length > 0)){
						
						// reset the current repeatable control.
						control.resetRowsToLength(list.length);
						control.properties.rows = list.length;
						control._toggleEditMode();
						//var minColsFound = list.length<controlIds.length?list.length:controlIds.length;
						//console.log("minColsFound", minColsFound);
						
						// in the event both the values given and the control count is != then get min to use.
						var minColsFound = -1;
						// set the actual values here.
						$.each(list, function(seti, set){
							minColsFound = set.length<controlIds.length?set.length:controlIds.length;
							
							for(var i=0;i<minColsFound;i++){
								//console.log("controlId[",controlIds[i],"] value[",set[i],"] row[",seti,"]");
								sopformsApi.setValueByControlId(controlIds[i], set[i], seti);
							}
						});
						control['rowValues'] = control.getRuntimeData().value;
						Form.Design.endEditRepeatableRow();
					}
					
					$(this).dialog('close');
				},
				'Clear' : function(e) {
					$("#populateDialog").find("textarea").val("");
				},
				'Cancel' : function(e) {
					$(this).dialog('close');
				}
			}
		});
	},

	prepareControl: function(control) {
		this.base(control);
		var elem = control.getElement();
		var $imgSectionSelect = $('<img class="section-select" src="' + sopFormTheme + 'images/general/empty.gif" title="select" />');
		$imgSectionSelect.click(function(e) {
			e.stopPropagation();
			Form.Design.choose(elem);
		});
		var $imgRepeatableEdit = $('<img class="repeatable-edit" src="' + sopFormTheme + 'images/general/empty.gif" title="toggle edit" />');
		if (control.isEdit) $imgRepeatableEdit.addClass('repeatable-end-edit');
		else $imgRepeatableEdit.removeClass('repeatable-end-edit');

		$imgRepeatableEdit.click(function(e) {
			e.stopPropagation();
			control._toggleEditMode();
			if (!control.isEdit) control.setInitValues();
		});
		
		var me = this;
		var $imgPopulate = $('<img class="repeatable-populate" src="' + sopFormTheme + 'images/general/empty.gif" title="populate" />');
		$imgPopulate.click(function(e) {
			//if($("#populateDialog").length == 0){
				/*var showProperties = !$(control.getElement()).is('.' + Form.Design.choosenClass);
				var el = control.getElement();
				Form.Design.choose(el, showProperties);
				*/
				
				me.constructPopulateDialog(control);
			//}
			$("#populateDialog").dialog('open');
		});
		
		var $handle = $jq('<div class="section-handle"><div></div></div>');
		$handle.children('div').append($imgPopulate, $imgRepeatableEdit, $imgSectionSelect);
		//$handle.children('div').append($imgRepeatableEdit, $imgSectionSelect);
		$handle.prependTo($jq(elem));
		var $table = control.getGrid();
		Form.makeSortableDroppableGrid2($table.get(0));
		for (var i = 0; i < control.properties.rows; i++) {
			$table.columns('appendRow');
		}

		// mask the "remove-row" button
		var $control = $jq('.repeatable-remove-row > div', elem);
		$control.css('position', 'relative');
		var $mask = $('<div class="control-mask"></div>');
		$mask.appendTo($control);

		// add "add-row" handler
		$(elem).find('.repeatable-row-control-button input').click(function(e) {
			control.appendRow();
			var $grid = control.getGrid();
			var $control = $grid.find('.repeatable-remove-row:last > div');
			$control.css('position', 'relative');
			var $mask = $('<div class="control-mask"></div>');
			$mask.appendTo($control);
			
			if (control.isEdit) $grid.find('> tr:last, > tbody > tr:last').find('.control-mask').addClass('hide');
		});

		var aggregation = control.properties.aggregation; 
		if(aggregation && aggregation.length == 1){
			control.getGrid().columns('appendAggregateRow');
		}
		else{
			control.getGrid().columns('removeAggregateRow');
		}
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
		
		//addRow
		input = Form.Common.createFromTemplate(Form.Properties.textbox, {
			id: control.properties.id,
			name: 'controlButton',
			value: control.properties.controlButton
		}, null, {
			'keydown paste': function(e) {
				Form.Common.invokeLater(function(e, control) {
					var controlButton = control.properties.controlButton = $jq(this).val();
					var elem = control.getElement();
					var $addRow = $jq(elem).find('.repeatable-row-control-button input')
					if($addRow) $addRow.val(controlButton);
				}, { context: this, uid: 'changeProperty' }, e, control);
			}
		});

		properties['controlButton'] = input[0];
		
		//tooltip
		input = Form.Common.createFromTemplate(Form.Properties.textbox, {
			id: control.properties.id,
			name: 'tooltip',
			value: control.properties.tooltip
		}, null, {
			blur: function(e) {
				control.properties.tooltip = $jq(this).val();
			}
		});

		properties['tooltip'] = input[0];

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

		input = Form.Common.createFromTemplate(Form.Properties.textbox, {
			id: control.properties.id,
			name: 'rows',
			size: 5,
			maxLength: 5,
			value: control.properties.rows+''
		}, null, {
			blur: function(e) {
				control.properties.rows = Form.Util.toInteger($jq(this).val(), control.getDefaults().rows);
				control.refresh();
				Form.Design.choose(control.getElement(), false);
				for(var i = 0; i < control.children.length; i++) {
					validationManager.currentControlID = control.children[i].properties.id;
					validationManager.requiredToggled();
				}
			}
		});

		properties['rows'] = input[0];
		
		//min_rows
		input = Form.Common.createFromTemplate(Form.Properties.textbox, {
			id: control.properties.id,
			name: 'min_rows',
			size: 5,
			maxLength: 5,
			value: control.properties.min_rows+''
		}, null, {
			blur: function(e) {
				control.properties.min_rows = Form.Util.toInteger($jq(this).val(), control.getDefaults().min_rows);
				if(control.properties.min_rows < 0){
					control.properties.min_rows = 0;
					$jq(this).val('0');
				}
			}
		});
		
		properties['min_rows'] = input[0];
		
		//prompt
		input = Form.Common.createFromTemplate(Form.Properties.textarea, {
			id: control.properties.id,
			name: 'prompt',
			value: control.properties.prompt
		}, null, {
			blur: function(e) {
				control.properties.prompt = $jq(this).val()?$jq(this).val() : control.getDefaults().prompt;
				control.refresh();
				Form.Design.choose(control.getElement(), false);
			}
		});

		properties['prompt'] = input[0];

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

		// for lock field
		input = Form.Common.createFromTemplate(Form.Properties.checkbox, 
				componentLockManager.constructArguments(control));
		properties['component_lock'] = input[0];
		componentLockManager.setupTriggers(input, control);
		
		// for attachment properties
		input = Form.Common.createFromTemplate(Form.Properties.checkbox, 
				attachmentManager.constructArguments(control));
		properties['file_attachment'] = input[0];
		attachmentManager.setupTriggers(input, control);
		
		// for aggregation
		input = Form.Common.createFromTemplate(Form.Properties.checkbox, 
				aggregationManager.constructArguments(control));
		properties['aggregation'] = input[0];
		aggregationManager.setupTriggers(input, control);
		
		// for condition properties
		input = Form.Common.createFromTemplate(Form.Properties.conditions,
				conditionManager.constructArguments(control), null, null);
		conditionManager.setupTriggers(input);

		properties['conditions-properties'] = input[0];
		
		// for developer properties
		input = Form.Common.createFromTemplate(Form.Properties.developer,
				developerManager.constructArguments(control), null, null);
		developerManager.setupTriggers(input, control);
		properties['developer-properties'] = input[0];
		
		this.renderPropertiesCommonWithTabSupport(placeholder, this.propertiesTemplate, properties, true);

		componentLockManager.setupAll(control);
		conditionManager.setupAll(control);
	}
});

Form.registerRenderer(FQN, 'design', new RepeatableRowRenderer());

//RUNTIME
var RepeatableRowRuntimeRenderer = Form.Runtime.AbstractRenderer.extend({
	tmpl: Form.getTemplate(FQN, 'runtime'),

	renderBody: function(control, placeholder) {
		this.renderElementCommon(placeholder, this.tmpl,
				{ control: control });
	},

	prepareControl: function(control) {
		var $grid = control.getGrid();
		$grid.columns({
			remove: function(e, ui) {
				ui.instance.removeRow(ui.row,false);
			}
		});
		var numRows = control.properties.rows;
		if(isRuntime){
			if(data_json){
				if(data_json[control.properties.id]){
					numRows = data_json[control.properties.id].value.length;
				}
			}
		}
		
		var rowCount = $grid.columns('rows');
		for (var i = rowCount; i < numRows; i++) {
			$grid.columns('appendRow');
		}
		var elem = control.getElement();
		$jq(elem).find('.repeatable-row-control-button input').click(function(e) {
			var onclickScript = control.properties.onclickscript;
			if(onclickScript != null && $.trim(onclickScript) != ''){
				eval(onclickScript);
			}
			control.appendRow();
			if(window.onDocumentChange) {
				window.onDocumentChange();
			}
		});
		if(Form.isView) {
			$jq(".repeatable-row-control-button", elem).hide();
			$jq(".repeatable-remove-row > div", elem).hide();
		}
		if(control.properties.aggregation &&
				control.properties.aggregation.length != 0){
			// if aggregate row is requested, create it.
			control.getGrid().columns('appendAggregateRow');
		}
	}

});

Form.registerRenderer(FQN, 'runtime', new RepeatableRowRuntimeRenderer());

// PRINTER FRIENDLY
var RepeatableRowPrinterFriendlyRenderer = Form.Runtime.AbstractRenderer.extend({
	tmpl: Form.getTemplate(FQN, 'printerFriendly'),

	renderBody: function(control, placeholder) {
		this.renderElementCommon(placeholder, this.tmpl, { 
			control: control, 
			instructions: sopformsApi.getInstruction(control.properties.id)
		});
	},

	prepareControl: function(control) {
		var $grid = control.getGrid();
		$grid.columns({
			remove: function(e, ui) {
				ui.instance.removeRow(ui.row,false);
			}
		});
		var numRows = control.properties.rows;
		if(isRuntime){
			if(data_json){
				if(data_json[control.properties.id]){
					numRows = data_json[control.properties.id].value.length;
				}
			}
		}
		
		var rowCount = $grid.columns('rows');
		for (var i = rowCount; i < numRows; i++) {
			$grid.columns('appendRow');
		}
		
		if(control.properties.aggregation &&
				control.properties.aggregation.length != 0){
			// if aggregate row is requested, create it.
			control.getGrid().columns('appendAggregateRow');
		}
	}
});

Form.registerRenderer(FQN, 'printerFriendly', new RepeatableRowPrinterFriendlyRenderer());

}(jQuery));