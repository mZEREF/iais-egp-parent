var Utils = {
    getFileName: function(o){
        var pos = o.lastIndexOf("\\");
        return o.substring(pos + 1);
    }
}