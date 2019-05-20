(function($jq) {

var FQN = 'sopform.controls.base.Renderer';

Form.Design.AbstractRenderer = Base.extend({
	direction: null,

	getDirection: function() {
		if (this.direction == null) {
			this.direction = ($('html')[0].dir || '').toLowerCase();
		}
		return this.direction;
	},

	getElement: function(control) {
		var id = 'control--' + control.mode + '--' + control.properties.id
		return $jq('#'+id).get(0);
	},

	renderElementCommon: function(placeholder, templateString, data) {
		var html = Form.Common.mergeTemplate(templateString, data);
		var elem = $jq(html);
		$jq(placeholder).replaceWith(elem);

		return elem;
	},

	afterRender: function(control, placeholder, index) {
		if (index === undefined) control.__elementCache = null;

		var elem = control.getElement(index);
		if (!elem) return;

		if (!control.isContainer()) {
			// disable all input since we're in design mode
			$jq(elem).css('position', 'relative');
			var $mask = $('<div class="control-mask"></div>');
			$mask.appendTo(elem);
			var $ele = $(elem);
			if(!$ele.is(":visible") && control.parent && $(control.parent.getElement()).is(":visible")) {
				$ele.show();
				$ele.addClass("sds-form-component-hidden");
			}else {
				$ele.removeClass("sds-form-component-hidden");
			}
		}

		control.applyFont(index);
		control.applyCaptionOrientation(index);
		control.applyAlignment(index);
	},

	setCaptionOrientation: function(control) {
        if (control.isSection() || control.isRepeatableSection()) return;
        
		var parentControl = control.parent;
		if (!parentControl) return;

		var $control = $jq(control.getElement());
		if (!parentControl.isSection()
			&& !parentControl.isPage()
			&& !parentControl.isRepeatableSection()) {

			$control.removeClass('control-caption-horizontal');
			return;
		}

		if (parentControl.properties.captionOrientation == 'horizontal') {
			$control.addClass('control-caption-horizontal');
		} else {
			$control.removeClass('control-caption-horizontal');
		}
	},

	setAlignment: function(control) {
		/*var $el = $jq(control.getElement());
		var result = [];

		if (control.isContainer()) {
			$el.each(function() {
				var self = this;
				$jq(this).find('.control-set-alignment')
					.each(function() {
						var directParent = $jq(this).parents('.control').get(0);
					    if (directParent == self) result.push(this);
					});
			});
		} else {
			result = $jq.makeArray($el.find('.control-set-alignment'));
		}


		var $children = $jq(result);
		var direction = this.getDirection(); // should be refactored out, but leaving it here for now.
		var alignment = control.properties.alignment;
		if (direction == 'rtl') {
			if (alignment == 'right') alignment = 'left';
			else if (alignment == 'left') alignment = 'right';
		}

		$children.css('text-align', alignment || null);*/
	},

	getFontSettings: function(control) {
		var font = control.properties.font.inherited?
			Form.properties.font : control.properties.font;

		var result;
		if (font && font.inherited) {
			var theme = Form.Theme.getTheme(Form.properties.themeName);
			if (!theme) return;
			result = {
				headerFont: $jq.extend({}, theme.headerFont),
				titleFont: $jq.extend({}, theme.titleFont),
				labelFont: $jq.extend({}, theme.labelFont),
				normalFont: $jq.extend({}, theme.font)
			};
		} else if (font){
			result = {
				headerFont: font,
				titleFont: font,
				labelFont: font,
				normalFont: font
			};
		} else {
			result = {
				headerFont: {},
				titleFont: {},
				labelFont: {},
				normalFont: {}
			};
		}
		return result;
	},

	setFont: function(control) {
		/*var font = this.getFontSettings(control);
		var $el = $jq(control.getElement());
		var result = [];
		if (control.isContainer()) {
			$el.each(function() {
				var self = this;
				$jq(this).find('.control-set-font')
					.each(function() {
						var directParent = $jq(this).parents('.control').get(0);
					    if (directParent == self) result.push(this);
					});
			});
		} else {
			result = $jq.makeArray($el.find('.control-set-font'));
		}
		$el.css('background-color', font.normalFont.bgcolor || '');

		var $children = $jq(result);
		this.setAllFont($children, font);*/
	},

	_toCSSFont: function(font) {
		return {
			'font-family': font.type || null,
			'font-size': font.size || null,
			'font-weight': font.bold ? 'bold' : 'normal',
			'font-style': font.italic ? 'italic' : 'normal',
			'color': font.color || ''
		};
	},
	
	_setElementFont: function($el, font) {
		if ($el.is('select[multiple]')) $el.find('option').css(font);
		else  $el.css(font);
	},
	
	setAllFont: function($children, font) {
		var normalFont = this._toCSSFont(font.normalFont);
		var headerFont = this._toCSSFont(font.headerFont);
		var titleFont = this._toCSSFont(font.titleFont);
		var labelFont = this._toCSSFont(font.labelFont);
		
		for (var i = 0, n = $children.length; i < n; i++) {
			var $child = $children.eq(i);
			
			if ($child.hasClass('control-font-normal')) {
				this._setElementFont($child, normalFont);
				continue;
			}

			if ($child.hasClass('control-font-header')) {
				this._setElementFont($child, headerFont);
				continue;
			}

			if ($child.hasClass('control-font-title')) {
				this._setElementFont($child, titleFont);
				continue;
			}

			if ($child.hasClass('control-font-label')) {
				this._setElementFont($child, labelFont);
				continue;
			}

		}
	},
	
	commonSetFont: function($children, className, font) {
		var props = this._toCSSFont(font);
		$children.each(function() {
			var $this = $(this);
			if (!$this.hasClass(className)) return;
			
			if ($this.is('select[multiple]')) {
				$this.find('option').css(props);
			} else {
				$this.css(props);
			}
		});
	},

	setNormalFont: function($children, font) {
		this.commonSetFont($children, 'control-font-normal', font.normalFont);
	},
	
	setHeaderFont: function($children, font) {
		this.commonSetFont($children, 'control-font-header', font.headerFont);
	},
	
	setTitleFont: function($children, font) {
		this.commonSetFont($children, 'control-font-title', font.titleFont);
	},
	
	setLabelFont: function($children, font) {
		this.commonSetFont($children, 'control-font-label', font.labelFont);
	},

	prepareControl: function(control) {
		var elem = control.getElement();

		if (!control.isPage() && !$jq.data(elem, 'draggable')) {
			$jq(elem).draggable($jq.extend( {}, Form.Design.draggableOptions,
					{
						delay: 200
					}));
		}

		if (!control.isContainer()) {
			$jq(elem).click(function(e) {
				e.stopPropagation();
				Form.Design.choose(elem);
			});
		}
	},

	renderPropertiesCommonWithTabSupport: function(placeholder, template, properties, scrollableTab) {
		scrollableTab = scrollableTab === true;

		var box = $jq(template);
		placeholder.replaceWith(box);
		var $tabs = box.find('.properties-tab:first');
		if ($tabs.length > 0) {
			$tabs.sopformTabs();
			//if (scrollableTab) $tabs.tabs('scrollable');
		}
		box.find('span.properties-placeholder').each(function() {
			var key = $jq(this).text();
			var property = properties[key];
			if (property != undefined) {
				$jq(this).replaceWith(property);
			}
		});
	},

	fontPropertiesTemplate: Form.getTemplate(FQN, 'fontProperties'),
	aclPropertiesTemplate: Form.getTemplate(FQN, 'aclProperties'),

	renderPropertiesBody: function(control, placeholder) {
		var properties = {};
		$jq.extend(properties, this.renderFontPropertiesBody(control, placeholder));
		$jq.extend(properties, this.renderAclPropertiesBody(control, placeholder));
		return properties;
	},

	renderAclPropertiesBody: function(control, placeholder) {
		var input;
		var properties = {};

		// prepare stages
		var stagesOptions = [];
		var currentForm = FormStage.getCurrentForm();
		var stage = FormStage.getStageMap()[currentForm];
		if(!stage) {
			stage = [];
		}
		stage.push(currentForm);
		$.each(stage?stage:[], function() {
			stagesOptions.push([this.toString(), this.toString()]);
		});

		// visibility
		input = Form.Common.createFromTemplate(Form.Properties.dropdown, {
			id: control.properties.id,
			name: 'acl-visibility-mode',
			value: control.properties.acl.visibility.mode,
			options: [
				['always-visible', 'Always Visible'],
				['visible-in', 'Visible in'],
				['hide-in', 'Hide in']
			]
		}, null, {
			change: function(e) {
				var mode = control.properties.acl.visibility.mode = $jq(this).val();
				var $stagesRow = $jq(this).parents('#properties-group-acl-visibility:first')
						.find('#acl-visibility-property-stages');

				if (mode == 'always-visible') $stagesRow.hide();
				else $stagesRow.show();
			}
		});

		properties['acl-visibility-mode'] = input[0];

		input = Form.Common.createFromTemplate(Form.Properties.checkbox, {
			id: control.properties.id,
			name: 'acl-visibility-stages',
			options: stagesOptions,
			selected: control.properties.acl.visibility.stages
		});

		var visibilityStages = input;
		$jq('input[type=checkbox]', input).click(function(e) {
			var stages = [];
			$(':checkbox:checked', visibilityStages).each(function() {
				stages.push($jq(this).val());
			});
			control.properties.acl.visibility.stages = stages;
		});

		properties['acl-visibility-stages'] = input[0];

		// editability
		input = Form.Common.createFromTemplate(Form.Properties.dropdown, {
			id: control.properties.id,
			name: 'acl-editability-mode',
			value: control.properties.acl.editability.mode,
			options: [
				['always-editable', 'Always Editable'],
				['editable-in', 'Editable in'],
				['not-editable-in', 'Not Editable in']
			]
		}, null, {
			change: function(e) {
				var mode = control.properties.acl.editability.mode = $jq(this).val();
				var $stagesRow = $jq(this).parents('#properties-group-acl-editability:first')
						.find('#acl-editability-property-stages');

				if (mode == 'always-editable') $stagesRow.hide();
				else $stagesRow.show();
			}
		});

		properties['acl-editability-mode'] = input[0];

		input = Form.Common.createFromTemplate(Form.Properties.checkbox, {
			id: control.properties.id,
			name: 'acl-editability-stages',
			options: stagesOptions,
			selected: control.properties.acl.editability.stages
		});

		var editabilityStages = input;
		$jq('input[type=checkbox]', input).click(function(e) {
			var stages = [];
			$jq(':checkbox:checked', editabilityStages).each(function() {
				stages.push($jq(this).val());
			});
			control.properties.acl.editability.stages = stages;
		});

		properties['acl-editability-stages'] = input[0];

		// render as HTML element
		var $tmp = $jq('#temp-placeholder')
		Form.Common.createPlaceholder($jq('#temp-placeholder'));
		var tmpl = Form.Common.mergeTemplate(this.aclPropertiesTemplate, {
			showVisibility: this.isShowVisibility(control),
			showEditability: this.isShowEditability(control)
		});
		this.renderPropertiesCommonWithTabSupport($tmp.children(':first'), tmpl, properties);

		if (control.properties.acl.visibility.mode == 'always-visible') {
			$tmp.find('#acl-visibility-property-stages').hide();
		}

		if (control.properties.acl.editability.mode == 'always-editable') {
			$tmp.find('#acl-editability-property-stages').hide();
		}

		var $tmp2 = $jq('<div>');
		$tmp2.append($jq('#temp-placeholder').children());

		return { 'acl-properties': $tmp2.children(':first').get(0) };
	},

	isShowVisibility: function(control) {
		return true;
	},
	
	isShowEditability: function(control) {
		if(control.isRepeatableRow() || control.isRepeatableSection()){
			return true
		} else{
			var isRichTextBox = control.instanceOf('sopform.controls.standard.RichTextBox');
			return !isRichTextBox && control.isInput();
		}
	},

	renderFontPropertiesBody: function(control, placeholder) {
		var input;
		var properties = {};
		var font;
		var isPage = control.isPage();
		var disableOtherSettings = false;

		if (isPage) {
			var theme = Form.Theme.getTheme(Form.properties.themeName);
			font = $jq.extend({}, theme.font, Form.properties.font);
		} else {
			font = $jq.extend({}, control.properties.font);
			if (font.inherited) {
				var theme = Form.Theme.getTheme(Form.properties.themeName);
				if (Form.properties.font.inherited) {
					font = $jq.extend({}, theme.font, font);
				} else {
					font = $jq.extend({}, theme.font, Form.properties.font, font);
				}
			}
		}

		disableOtherSettings = font.inherited;

		// font properties
		input = Form.Common.createFromTemplate(Form.Properties.checkbox, {
			id: control.properties.id,
			name: 'font-customize',
			options: [['true', '']],
			selected: [!(font.inherited) + '']
		});

		$jq('input[type=checkbox]', input).click(function(e) {
			var value = !this.checked;
			if (control.isPage()) {
				Form.properties.font.inherited = value;

				// set font settings value
				var theme = Form.Theme.getTheme(Form.properties.themeName);
				Form.properties.font = $jq.extend({}, theme.font, Form.properties.font);

				// apply the new value
				Form.traverse(function(control) {
					control.applyFont();
				});
			} else {
				control.properties.font.inherited = value;

				// set font settings value
				var theme = Form.Theme.getTheme(Form.properties.themeName);
				if (Form.properties.font.inherited) {
					control.properties.font = $jq.extend({}, theme.font, control.properties.font);
				} else {
					control.properties.font = $jq.extend({}, theme.font, Form.properties.font, control.properties.font);
				}

				// apply the new value
				control.applyFont();
			}

			var otherFontSettings = Form.Design.getProperties()
					.find('#properties-group-font .property-input')
					.not(this);

			if (value) {
				otherFontSettings.attr('disabled', 'disabled');
			} else {
				otherFontSettings.removeAttr('disabled');
			}
		});

		properties['font-customize'] = input[0];

		input = Form.Common.createFromTemplate(Form.Properties.dropdown, {
			id: control.properties.id,
			name: 'font-type',
			value: font.type,
			options: [
				['', '- select -'],
				['arial, helvetica, sans-serif', 'Arial'],
				['"Comic Sans MS", cursive', 'Comic Sans'],
				['courier, monospace', 'Courier'],
				['helvetica, sans-serif', 'Helvetica'],
				['tahoma, geneva, sans-serif', 'Tahoma'],
				['verdana, geneva, sans-serif', 'Verdana']
			]
		}, null, {
			change: function(e) {
				var value = $jq(this).val();
				if (control.isPage()) {
					Form.properties.font.type = value;
					Form.traverse(function(control) {
						control.applyFont();
					});
				} else {
					control.properties.font.type = value;
					control.applyFont();
				}
			}
		});

		if (disableOtherSettings) input.attr('disabled', 'disabled');
		properties['font-type'] = input[0];

		// START: font-color
		var $colordiv = $jq('<div>');
		input = Form.Common.createFromTemplate(Form.Properties.colorpicker, {
			id: control.properties.id,
			name: 'font-color',
			value: font.color
		});
		// hard code here. colorpicker not work.
		/*
		input.colorpicker({
			color: font.color || undefined,
			submit: function(e, ui) {
				var color = '#' + ui.hex;
				if (control.isPage()) {
					Form.properties.font.color = color;
					Form.traverse(function(control) {
						control.applyFont();
					});
				} else {
					control.properties.font.color = color;
					control.applyFont();
				}
				$jq(this).css('background-color', color);
				$jq(this).colorpicker('hide');
				
				$colordiv.children(':eq(1)').find(':checkbox').each(function() {
					this.checked = false;
				});
			}
		});
		*/

		input.css({ 'float': 'left', 'margin-top': '4px'});
		$colordiv.append(input);
		var selected = [];
		if (!font.color) {
			selected = ['default']
		}
		
		input = Form.Common.createFromTemplate(Form.Properties.checkbox, {
			id: control.properties.id,
			name: 'font-color-default',
			options: [['default', 'default']],
			selected: selected
		});

		input.find(':checkbox').click(function() {
			if (this.checked) {
				var value = '';
				if (control.isPage()) {
					Form.properties.font.color = value;
					Form.traverse(function(control) {
						control.applyFont();
					});
				} else {
					control.properties.font.color = value;
					control.applyFont();
				}
				$colordiv.children(':first').css('background-color', '');
			}
		});
		
		$colordiv.append(input);
		
		if (disableOtherSettings) $colordiv.find(':input').attr('disabled', 'disabled');
		properties['font-color'] = $colordiv[0];
		// END: font-color

		// START: font-bgcolor
		var $bgcolordiv = $jq('<div>');
		input = Form.Common.createFromTemplate(Form.Properties.colorpicker, {
			id: control.properties.id,
			name: 'font-bgcolor',
			value: font.bgcolor
		});
		// hard code here. colorpicker not work.
		/*
		input.colorpicker({
			color: font.bgcolor || undefined,
			submit: function(e, ui) {
				var color = '#' + ui.hex;
				if (control.isPage()) {
					Form.properties.font.bgcolor = color;
					Form.traverse(function(control) {
						control.applyFont();
					});
				} else {
					control.properties.font.bgcolor = color;
					control.applyFont();
				}
				$jq(this).css('background-color', color);
				$jq(this).colorpicker('hide');
				
				$bgcolordiv.children(':eq(1)').find(':checkbox').each(function() {
					this.checked = false;
				});
			}
		});
		*/

		input.css({ 'float': 'left', 'margin-top': '4px'});
		$bgcolordiv.append(input);
		selected = [];
		if (!font.bgcolor) {
			selected = ['default']
		}
		
		input = Form.Common.createFromTemplate(Form.Properties.checkbox, {
			id: control.properties.id,
			name: 'font-bgcolor-default',
			options: [['default', 'default']],
			selected: selected
		});

		input.find(':checkbox').click(function() {
			if (this.checked) {
				var value = '';
				if (control.isPage()) {
					Form.properties.font.bgcolor = value;
					Form.traverse(function(control) {
						control.applyFont();
					});
				} else {
					control.properties.font.bgcolor = value;
					control.applyFont();
				}
				$bgcolordiv.children(':first').css('background-color', '');
			}
		});
		
		$bgcolordiv.append(input);
		
		if (disableOtherSettings) $bgcolordiv.find(':input').attr('disabled', 'disabled');
		properties['font-bgcolor'] = $bgcolordiv[0];
		// END: font-bgcolor
		
		input = Form.Common.createFromTemplate(Form.Properties.textbox, {
			id: control.properties.id,
			name: 'font-size',
			size: 5,
			maxLength: 4,
			value: font.size
		}, null, {
			'keydown paste': function(e) {
				Form.Common.invokeLater(function(e, control) {
					var value = $jq(this).val();
					if (control.isPage()) {
						Form.properties.font.size = value;
						Form.traverse(function(control) {
							control.applyFont();
						});
					} else {
						control.properties.font.size = value;
						control.applyFont();
					}
				}, { context: this, uid: 'changeProperty' }, e, control);
			}
		});

		if (disableOtherSettings) input.attr('disabled', 'disabled');
		properties['font-size'] = input[0];

		input = Form.Common.createFromTemplate(Form.Properties.checkbox, {
			id: control.properties.id,
			name: 'font-bold',
			options: [ ['true', ''] ],
			selected: [(font.bold || false) + '']
		});

		$jq('input[type=checkbox]', input).click(function(e) {
			var value = this.checked;
			if (control.isPage()) {
				Form.properties.font.bold = value;
				Form.traverse(function(control) {
					control.applyFont();
				});
			} else {
				control.properties.font.bold = value;
				control.applyFont();
			}
		});

		if (disableOtherSettings) input.find('.property-input').attr('disabled', 'disabled');
		properties['font-bold'] = input[0];

		input = Form.Common.createFromTemplate(Form.Properties.checkbox, {
			id: control.properties.id,
			name: 'font-italic',
			options: [['true', '']],
			selected: [(font.italic || false) + '']
		});

		$jq('input[type=checkbox]', input).click(function(e) {
			var value = this.checked;
			if (control.isPage()) {
				Form.properties.font.italic = value;
				Form.traverse(function(control) {
					control.applyFont();
				});
			} else {
				control.properties.font.italic = value;
				control.applyFont();
			}
		});

		if (disableOtherSettings) input.find('.property-input').attr('disabled', 'disabled');
		properties['font-italic'] = input[0];

		var $tmp = $jq('#temp-placeholder');
		Form.Common.createPlaceholder($jq('#temp-placeholder'));
		this.renderPropertiesCommonWithTabSupport($tmp.children(':first'), this.fontPropertiesTemplate, properties);

		var $tmp2 = $jq('<div>');
		$tmp2.append($jq('#temp-placeholder').children());

		return { 'font-properties': $tmp2.children(':first').get(0) };
	}
})

if (Form.Runtime == undefined) Form.Runtime = {};

Form.Runtime.AbstractRenderer = Form.Design.AbstractRenderer.extend({
	getElement: function(control) {
		var id = 'control--' + control.mode + '--' + control.properties.id;
		return $jq('#'+id)[0];
	},

	prepareControl: null,

	afterRender: function(control, placeholder, index) {
		if (index === undefined) control.__elementCache = null;

		var hint = control.properties.hint;
		if (control.mode != 'printerFriendly' && $.trim(hint) != '') {
			var $elem;
			if (!control.isContainer()) $elem = $('label, input, select, textarea', control.getElement());
			else $elem = $('.section-header>label, .repeatable-section-header>label, legend', control.getElement());
			$elem.attr('title',hint);
			SOP.Common.setupTooltip($elem, {effect: "toggle",position: "center right",offset:[-15,0]});
		}

		if(control.mode != 'printerFriendly' && jQuery.trim(control.properties.help) != ""){
			var help = control.properties.help;
			var helpElem = jQuery("<div helpElem=true style='display:inline;' class='help-panel'> <image src=\"" + sopFormTheme + "images/general/icon-info.png\"> </div>");
			
			helpElem.data('helpMsg',help);
			SOP.SOPForms.setupHelpDialog(helpElem);

			if(!control.isContainer()){
				var controlElement = control.getElement();
				if (controlElement != null){
					var ce = null;
				
					if(control.FQN == 'sopform.controls.standard.Button'){
						//special handling required for button.
						ce = $("div.control-input-span", controlElement);
					}
					else{
						ce = $(controlElement).find("label:first-child");
					}
					
					ce.append(helpElem);
				}
			}
			else{
				//cannot isolate the mouseover trigger for hint from icon.
				var hp = $('.section-header>label, legend, .repeatable-section-header>label', control.getElement()).find('.help-panel');
				if(hp.length==0){
					$('.section-header>label, legend, .repeatable-section-header>label', control.getElement()).last().append(helpElem);
				}
			}
		}

		control.applyFont();
		control.applyCaptionOrientation();
		control.applyAlignment();
		var ele = control.getElement(index);
		if(ele) {
			var $input = $(':input', ele);
			$input.blur(validationManager.doNotifyBlur);
			$input.change(validationManager.doNotifyChange);
		}
	},

	isMSIE: function() {
		return $.browser.msie;
	},
	
	renderPropertiesBody: null
})

}(jQuery));