(function($jq) {

Form.Controls = {};

/**
 * A factory method to create a control instance
 * from the given class name. The constructor will
 * automatically call <code>init()</code> method
 * after the instance is being created, unless the
 * caller choose not to by passing in <code>false</code>
 * to callInit argument.
 */
Form.createControl = function(name, options, position, callInit) {
	var clazz = Form.getClass(name);
	var control = clazz ? new clazz(options, position, callInit) : null;
	//Form.addControl(control);
	return control;
}

/**
 * Get class object for the given class name.
 */
Form.getClass = function(name) {
	var clazz = Form.CLASSES[name];
	return (clazz && typeof clazz == 'function') ? clazz : null;
}

/**
 * Register a class object with a name.
 */
Form.registerClass = function(className, clazz) {
	Form.CLASSES[className] = clazz;
}

/**
 * Register a renderer for the specified class name and mode.
 */
Form.registerRenderer = function(className, mode, renderer) {
	if (!Form.RENDERERS[className]) {
		Form.RENDERERS[className] = [];
	}
	Form.RENDERERS[className][mode] = renderer;
}

/**
 * Get a renderer instance for the specified class name and mode.
 */
Form.getRenderer = function(className, mode) {
	var obj = Form.RENDERERS[className]
	if(!obj){return null;}

	if(!Form.isView){
		return obj[mode];
	}else if(Form.createControl(className, null, null, false) instanceof Form.Controls.Input){
		if(className == 'sopform.controls.standard.CheckBox'){
			return Form.RENDERERS['sopform.controls.standard.CheckBox']['view'];
		}else if(className == 'sopform.controls.standard.Radio'){
			return Form.RENDERERS['sopform.controls.standard.Radio']['view'];
		}{
			return Form.RENDERERS['sopform.controls.base.Input']['view'];
		}
	}else
		return obj[mode];
}

/**
 * Register a serializer for the specified class name.
 */
Form.registerSerializer = function(className, serializer) {
	Form.SERIALIZERS[className] = serializer;
}

/**
 * Get a serializer instance for the specified class name.
 */
Form.getSerializer = function(className) {
	return Form.SERIALIZERS[className];
}

var FQN = 'sopform.controls.base.Control';

/**
 * This is the base class for all control. Contains all common methods
 * and attributes that are required for a control.
 */
Form.Controls.Control = Base.extend({
	__elementCache: null,
		
	constructor: function(options, position, callInit) {
		callInit = callInit === false ? false : true;
		// comments for this method call at the function.
		//this.id = this.getUniqueControlIDToUse(options);

		this.mode = Form.currentMode;
		this.position = position;

		this.properties = {
			font: { inherited: true },
			acl: {
				visibility: {
					mode: 'always-visible'
				},
				editability: {
					mode: 'always-editable'
				}
			},
			alignment: 'left',
			hint: '',
			help: ''
		};

		if (this.getDefaults && this.getDefaults.constructor == Function) {
			this.properties = $jq.extend(this.properties, this.getDefaults());
		}

		if (options) {
			this.properties = $jq.extend(this.properties, options);
		}

		if (this.properties.id === undefined) {
			this.properties.id = '' + Form.nextUID();
		}

		if (this.properties.name === undefined) {
			this.properties.name = 'control_' + this.properties.id;
		}

		if (callInit && this.init && (typeof this.init == 'function')) {
			this.init();
		}
	},

	/**
	 * A convenient method to test whether this control is a subclass
	 * of <code>Container</code>.
	 */
	isContainer: function() {
		return this.instanceOf(Form.Controls.Container);
	},

	/**
	 * A convenient method to test whether this control is a subclass
	 * of <code>SingleSelect</code>.
	 */
	isSingleSelect: function() {
		return this.instanceOf(Form.Controls.SingleSelect);
	},

	/**
	 * A convenient method to test whether this control is a subclass
	 * of <code>MultiSelect</code>.
	 */
	isMultiSelect: function() {
		return this.instanceOf(Form.Controls.MultiSelect);
	},

	/**
	 * A convenient method to test whether this control is a subclass
	 * of <code>SingleInput</code>.
	 */
	isSingleInput: function() {
		return this.instanceOf(Form.Controls.SingleInput);
	},

	/**
	 * A convenient method to test whether this control is a subclass
	 * of <code>Input</code>.
	 */
	isInput: function() {
		return this.instanceOf(Form.Controls.Input);
	},

	/**
	 * A convenient method to test whether this control is a subclass
	 * of <code>Section</code>.
	 */
	isSection: function() {
		return this.instanceOf(Form.Controls.Section);
	},

	/**
	 * A convenient method to test whether this control is a subclass
	 * of <code>Page</code>.
	 */
	isPage: function() {
		return this.instanceOf(Form.Controls.Page);
	},

	/**
	 * A convenient method to test whether this control is a subclass
	 * of <code>RepeatableRow</code>.
	 */
	isRepeatableRow: function() {
		return this.instanceOf(Form.Controls.RepeatableRow);
	},

	/**
	 * A convenient method to test whether this control is a subclass
	 * of <code>RepeatableSection</code>.
	 */
	isRepeatableSection: function() {
		return this.instanceOf(Form.Controls.RepeatableSection);
	},

	/**
	 * A convenient method to test whether this control is
	 * a direct child of a Section.
	 */
	isInSection: function() {
		return this.parent && this.parent.isSection();
	},

	getContainingRepeatableRow: function(){
		var par = this.parent;
		if(par != null && par.FQN == 'sopform.controls.base.RepeatableRow'){
			return par;
		}
	},

	getContainingRepeatableSection: function(){
		var par = this.parent;
		if(par != null && par.FQN == 'sopform.controls.base.RepeatableSection'){
			return par;
		}
	},
	
	/**
	 * Return the Section instance which contain this control
	 * if any. Otherwise return <code>undefined</code>.
	 */
	getContainingSection: function(){
		if(this.isInSection()){
			return this.parent;
		}
		return null;
	},

	/**
	 * Return the Page instance which contain this control.
	 * When invoked on a page it will return the page
	 * control itself.
	 */
	getContainingPage: function() {
		var root = this;
		while (root.parent) root = root.parent;
		return root;
	},

	getElement: function() {
		if (!this.__elementCache) {
			var renderer = this.getRenderer();
			this.__elementCache = renderer.getElement(this);
		}
		return this.__elementCache;
	},

	/**
	 * Refresh the control element display.
	 */
	refresh: function(placeholder) {
		var args = [];
		var rr = this.getContainingRepeatableRow();
		if (!rr) rr = this.getContainingRepeatableSection();
		var index = null;
		if(rr) index = rr['eventIndex'];
		args.push.apply(args, arguments);
		if (!placeholder) {
			if (args.length > 1 && typeof args[1] == 'number') {
				args[0] = this.getElement(args[1]);
			} else {
				args[0] = this.getElement();
			}
		}
		if (!placeholder && args[0] && args[0].length) {
			var elements = args[0];
			for (var i = 0, n = elements.length; i < n; i++) {
				var _idx = parseInt(elements[i].id.match(/[0-9]+$/)[0]);
				if(rr && index != "undefined" && index != null) {
					if(_idx == index.actual){
						args[0] = this.getElement(_idx);
						args[1] = _idx;
						this.render.apply(this, args);
						this.bind(_idx);
					}
				}else{
					args[0] = this.getElement(_idx);
					args[1] = _idx;
					this.render.apply(this, args);
					this.bind(_idx);
				}
			}
			if(rr) rr['eventIndex'] = null;
		} else {
			this.render.apply(this, args);
			if(validationManager.map.containsKey(this.properties.id)){
				validationManager.doRequiredIndicator(this);
			}
			this.bind();
		}
		if (this.isPage()
				|| (this.parent && !this.parent.instanceOf('sopform.controls.base.RepeatableRow'))) {

			this.prepareControl();
		}
	},

	/**
	 * Bind the control to its HTML element.
	 * This way we can get this control instance from
	 * its HTML element.
	 */
	bind: function(idx) {
		Form.setControl(this.getElement(idx), this);
	},

	id: null,
	properties: null,
	parent: null,

	/**
	 * Render the control's HTML element.
	 * This method will delegate to a specific
	 * renderer for this control type.
	 *
	 * It will call these three methods:
	 * beforeRender, renderBody, afterRender
	 * respectively.
	 */
	render: function(placeholder) {
		var renderer = this.getRenderer();
		if (!renderer) return;

		// insert control as first parameter
		var args = [this];
		args.push.apply(args, arguments);
		
		if (renderer.beforeRender) {
			renderer.beforeRender.apply(renderer, args);
		}
		if (renderer.renderBody) {
			renderer.renderBody.apply(renderer, args);
		}
		if (renderer.afterRender) {
			renderer.afterRender.apply(renderer, args);
		}
	},

	/**
	 * Render the control's properties box.
	 * A properties box allow user to set control
	 * properties value in design time.
	 * This method will delegate to a specific
	 * renderer for this control type.
	 *
	 * It will call these three methods:
	 * beforeRenderProperties, renderPropertiesBody,
	 * afterRenderProperties respectively.
	 */
	renderProperties: function(placeholder) {
		var renderer = this.getRenderer();
		if (!renderer) return;

		// insert control as first parameter
		var args = [this];
		args.push.apply(args, arguments);

		if (renderer.beforeRenderProperties) {
			renderer.beforeRenderProperties.apply(renderer, args);
		}
		if (renderer.renderPropertiesBody) {
			renderer.renderPropertiesBody.apply(renderer, args);
		}
		if (renderer.afterRenderProperties) {
			renderer.afterRenderProperties.apply(renderer, args);
		}
	},

	/**
	 * Prepare control.
	 */
	prepareControl: function() {
		var renderer = this.getRenderer();
		if (!renderer) return;

		// insert control as first parameter
		var args = [this];
		args.push.apply(args, arguments);

		if (renderer.prepareControl) {
			renderer.prepareControl.apply(renderer, args);
		}
	},

	/**
	 * Clone this object.
	 */
	clone: function(parent) {
		var obj = Form.createControl(this.FQN, { id: this.properties.id }, 0, false);
		//Form.addControl(this); 
		// because the right control is changed when createControl, and we should repair it.
		var result = $jq.extend(obj, this, {
			properties: $jq.extend(true, {}, this.properties),
			parent: parent,
			children: null,
			__elementCache: null,
			mode: Form.currentMode
		});

		if (!this.children) return result;

		var self = result;
		result.children = [];

		$jq.each(this.children, function() {
			var c = this.clone(self);
			result.children.push(c);
		});

		return result;
	},

	mode: null,

	/**
	 * Get a renderer instance for this type.
	 */
	getRenderer: function() {
		return Form.getRenderer(this.FQN, this.mode);
	},

	/**
	 * Test whether this instance is an instance
	 * of the given class name.
	 */
	instanceOf: function(nameOrClass) {
		var clazz;
		if (typeof nameOrClass == 'string') clazz = Form.getClass(nameOrClass);
		else if (typeof nameOrClass == 'function') clazz = nameOrClass;
		return clazz ? this instanceof clazz : false;
	},

	FQN: FQN,

	/**
	 * Get simple short name of this instance's class.
	 */
	getSimpleName: function() {
		return this.FQN.match(/[^\.]*$/)[0];
	},

	/**
	 * Get the value of this control.
	 */
	getValue: function() {
		return null;
	},

	/**
	 * Set the value of this control.
	 */
	setValue: function(value) {
	},

	/**
	 * Traverse this control. Call the provided function,
	 * passing in the instance as its first parameter, and
	 * also the rest arguments.
	 */
	traverse: function(fn) {
		var args = [];
		args.push.apply(args, arguments);
		args[0] = this;
		return fn.apply(null, args);
	},

	/**
	 * Traverse this control. Call the provided function,
	 * passing in the instance as its first parameter, and
	 * also the rest arguments.
	 *
	 * The provided function can choose to include of exclude
	 * the instance by returning true or false respectively.
	 */
	filter: function(fn) {
		var args = [];
		args.push.apply(args, arguments);
		args[0] = this;
		return fn.apply(null, args) ? [this] : [];
	},

	/**
	 * A convenient method to test whether this control is
	 * a direct child of a RepeatableRow.
	 */
	isInRepeatable: function() {
		return this.parent && this.parent.instanceOf('sopform.controls.base.RepeatableRow');
	},

	/**
	 * A convenient method to test whether this control is
	 * a direct child of a RepeatableSection.
	 */
	isInRepeatableSection: function() {
		return this.parent && this.parent.instanceOf('sopform.controls.base.RepeatableSection');
	},

	/**
	 * Apply font settings value to this control's element.
	 */
	applyFont: function() {
		this.getRenderer().setFont(this);
	},

	/**
	 * Apply alignment to this control's element.
	 */
	applyAlignment: function() {
		this.getRenderer().setAlignment(this);
	},

	/**
	 * Apply caption orientation to this control's element.
	 */
	applyCaptionOrientation: function() {
		this.getRenderer().setCaptionOrientation(this);
	},

	/**
	 * Get generated HTML element id.
	 */
	getElementId: function() {
		return ['control--', this.mode, '--', this.properties.id].join('');
	},
	
	__getAllChildPreid_IdMap: function(){
		var m = new Map();
		var children = this.children;
		if(children){
			for(var i=0; i<children.length; i++){
				if(children[i].properties.__preid)
					m.put(children[i].properties.__preid,children[i].properties.id);
				if(children[i].children){
					var c = children[i].__getAllChildPreid_IdMap();
					if(c)
						m.putAll(c);
				}
			}
		}
		return m;
	}
});

Form.registerClass(FQN, Form.Controls.Control);

}(jQuery));