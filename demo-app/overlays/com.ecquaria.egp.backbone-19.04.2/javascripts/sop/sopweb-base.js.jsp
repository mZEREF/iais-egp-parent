<%@ page import="com.ecquaria.cloud.helper.EngineHelper"%><%@ page contentType="text/javascript" %>
(function() {
	window.sopAppContext = '<%=EngineHelper.getContextPath()%>';
    if(window.sopAppContext.indexOf(';') != -1) {
        window.sopAppContext = window.sopAppContext.substring(0, window.sopAppContext.indexOf(';'));
    }
	window.sopStaticFolder = '<%=EngineHelper.getResourcePath()%>/_statics';
	window.sopThemeFolder = '<%=EngineHelper.getResourcePath()%>/_themes/sop6';
})();