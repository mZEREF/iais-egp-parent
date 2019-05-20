(function($jq) {

var FQN = 'sopform.controls.standard.RichTextBox';

var RichTextBox = Form.Controls.BlobInput.extend({
	init: function(){
		conditionManager.doControlInit(this);
		validationManager.doControlInit(this);
	},

	getDefaults: function() {
		return {
			caption: 'Label',
			text: '',
			_id: this.id
		}
	},

	FQN: FQN,

	getValue: function() {
		return this.properties.text;
	},

	setValue: function(value) {
		this.properties.text = value;
	},

	getRuntimeData: function() {
		var el = this.getElement.apply(this, arguments);
		var value = null;
		if (el && !Form.isView) {
			var tid = $('textarea', el).attr('id');
			var richtext = CKEDITOR.instances[tid];
			value = richtext.getData();
		} else if (data_json) {
			var p = data_json[this.properties.id];
			if (p === undefined) value = this.properties.text;
			else if (p) value = p.value;
		}
	
		return { type: Form.Constants.KEY_BLOB_VALUE, value: value };
	},
	
	setRuntimeData: function(){
		var p = data_json[this.properties.id];
		if (p == null) return;

		var el = this.getElement.apply(this, arguments);
		if (!el) return;

		var value = p.value;

		if (typeof value !== 'string') return;
		
		var tid = $('textarea', el).attr('id');
		var richtext = CKEDITOR.instances[tid];
		if (richtext == null) return;

		richtext.setData(value);
	}
});

Form.registerClass(FQN, RichTextBox);

Form.Loader.load(FQN, 'renderer');

Form.Loader.load(FQN, 'serializer');

// this is a hack to initialize CKEDITOR properly for design mode
$jq(function() {
	if ($('#temp-placeholder').length) {
		CKEDITOR.appendTo('temp-placeholder');
		setTimeout(function() {
			$jq('#temp-placeholder').empty();
		}, 1000);
	}
});

// Periodically check for changes to the rich text
// this is required to emulate onchange event
setInterval(function() {
	if (!CKEDITOR || !CKEDITOR.instances) return;
		
	var instances = CKEDITOR.instances;
	for (var p in instances) {
		var instance = instances[p];
		if (instance.focusManager.hasFocus) {
			if (instance['__status'] != 'in') {
				instance['__status'] = 'in';
				//instance.resetDirty();
			}
		} else if (instance['__status'] == 'in') {
			instance['__status'] = '';
			if (instance.checkDirty()) {
				instance.resetDirty();
				$(instance).change();
			}
		}
	}
}, 1000);

}(jQuery));