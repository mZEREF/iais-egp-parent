(function($jq) {

var FQN = 'sopform.controls.standard.Time';

var TimeRendererCommon = {
    tmpl: Form.getTemplate(FQN)
}

var TimeRenderer = Form.Design.AbstractRenderer.extend($jq.extend({
    propertiesTemplate: Form.getTemplate(FQN, 'properties'),

    // runtime implementation exists - do changes there too.
    renderBody: function(control, placeholder, index) {
        var input = this.renderElementCommon(placeholder, this.tmpl,
                { control: control, index: index });

        $jq('input[type=text]', input)
            .timeEntry({ spinnerImage: '' })
            .timeEntry('disable');
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
            name: 'caption',
            value: control.properties.caption
        }, null, {
            'keydown paste': function(e) {
                if (control.isInRepeatable()) return;

                Form.Common.invokeLater(function(e, control) {
                    var caption = control.properties.caption = $jq(this).val();
                    var $label = $jq('label', control.getElement());
                    $label.text(caption);
                }, { context: this, uid: 'changeProperty' }, e, control);
            },
            blur: function(e) {
                if (control.isInRepeatable()) {
                    control.properties.caption = $jq(this).val();
                    Form.Design.refresh(control);
                }
            }
        });

        properties['caption'] = input[0];

        // 3
        input = Form.Common.createFromTemplate(Form.Properties.textbox, {
            id: control.properties.id,
            name: 'text',
            size: 10,
            value: control.properties.text
        }, null, {
            'keydown paste': function(e) {
                Form.Common.invokeLater(function(e, control) {
                    $jq(this).change();
                }, { context: this, uid: 'changeProperty' }, e, control);
            },
            change: function(e) {
                var text = control.properties.text = $jq(this).val();
                var $text = $jq('input[type=text]', control.getElement());
                $text.val(text);
            }
        });

        var text = input;
        properties['text'] = input[0];

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
        
        // for developer properties
        input = Form.Common.createFromTemplate(Form.Properties.developer,
                developerManager.constructArguments(control), null, null);
        developerManager.setupTriggers(input, control);
        properties['developer-properties'] = input[0];
        
        // for condition properties
        input = Form.Common.createFromTemplate(Form.Properties.conditions,
                conditionManager.constructArguments(control), null, null);
        conditionManager.setupTriggers(input);

        properties['conditions-properties'] = input[0];

        // for validation properties
        input = Form.Common.createFromTemplate(Form.Properties.validations,
                validationManager.constructArguments(control), null, null);
        validationManager.setupTriggers(input);

        properties['validation-properties'] = input[0];

        this.renderPropertiesCommonWithTabSupport(placeholder, this.propertiesTemplate, properties, true);

        componentLockManager.setupAll(control);
        conditionManager.setupAll(control);
        validationManager.setupAll(control);

        $jq(text).timeEntry({ spinnerImage: '' });
    }
}, TimeRendererCommon));

Form.registerRenderer(FQN, 'design', new TimeRenderer());

// RUNTIME
var TimeRuntimeRenderer = Form.Runtime.AbstractRenderer.extend($jq.extend({
    renderBody: function(control, placeholder, index) {
        var input = this.renderElementCommon(placeholder, this.tmpl,
                { control: control, index: index });

        $jq('input[type=text]', input)
            .timeEntry({ spinnerImage: '' });
    },

    afterRender: function(control, placeholder){
        this.base(control, placeholder);
        var $input = $(':input', control.getElement());
        $input.change(conditionManager.doNotifyTrigger);
        
        var onchangeScript = control.properties.onchangescript;
        var _script = null;
        var $TimePanel = $('#ui-Time-div');
        if(onchangeScript != null && $.trim(onchangeScript) != ''){
            _script = new Function("e", onchangeScript);
            $input.change(_script);
        }   
    }
}, TimeRendererCommon));

Form.registerRenderer(FQN, 'runtime', new TimeRuntimeRenderer());

// PRINTER FRIENDLY
var TimePrinterFriendlyRenderer = Form.Runtime.AbstractRenderer.extend({
    tmpl: Form.getTemplate(FQN, 'printerFriendly'),
    
    renderBody: function(control, placeholder, index) {
        var val = sopformsApi.getJSONDataValue(control.properties.id, index, control);
        var _hasValue = false;
        var _value = null;
        if(val != null){
            _value = val.value;
            if(_value != null && $.trim(_value).length > 0){
                _hasValue = true;
            }
        }
        
        this.renderElementCommon(placeholder, this.tmpl, { 
            control: control, 
            index: index,
            hasValue: _hasValue,
            text: _value,
            instructions: sopformsApi.getInstruction(control.properties.id)
        });
    }
});

Form.registerRenderer(FQN, 'printerFriendly', new TimePrinterFriendlyRenderer());

}(jQuery));