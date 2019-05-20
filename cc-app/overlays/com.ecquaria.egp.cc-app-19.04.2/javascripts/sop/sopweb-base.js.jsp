<%@ page contentType="text/javascript" %>
(function() {
	window.sopAppContext = '/sample';
    if(window.sopAppContext.indexOf(';') != -1) {
        window.sopAppContext = window.sopAppContext.substring(0, window.sopAppContext.indexOf(';'));
    }
	window.sopStaticFolder = '/sample/_statics';
	window.sopThemeFolder = '/sample/_themes/sop6';
})();