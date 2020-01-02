
var Utils = {
    getFileName: function(o){
        var pos = o.lastIndexOf("\\");
        return o.substring(pos + 1);
    },

    submit: function (form, action, value) {
        SOP.Crud.cfxSubmit(form, action, value);
    }
}