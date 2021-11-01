<%@ page import="com.ecquaria.cloud.helper.EngineHelper" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.Formatter" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.ecquaria.cloud.ServerConfig" %>
<%--
  Created by IntelliJ IDEA.
  User: MI
  Date: 2020/10/19
  Time: 9:57
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
  String resourcePathExp = EngineHelper.getResourcePath();
%>
<!DOCTYPE html>
<html lang="en">
<head>
  <title>Error</title>
</head>
<body>
<link href="<%=resourcePathExp%>/_statics/css/jquery-ui/smoothness/jquery-ui-custom.css" rel="stylesheet">
<link href="<%=resourcePathExp%>/_statics/css/jquery-ui/form-viewer/jquery-ui-all.css" rel="stylesheet">
<link href="<%=resourcePathExp%>/_statics/css/core/core.css" rel="stylesheet">
<link href="<%=resourcePathExp%>/_themes/egov/css/template.css" rel="stylesheet">
<link href="<%=resourcePathExp%>/_statics/css/error-page.css" rel="stylesheet">
<script type="text/javascript" src="<%=resourcePathExp%>/javascripts/egov/jquery-3.5.1.min.js"></script>

  <%
    Map<String, Object> errors = (Map<String, Object>) request.getAttribute("errors");
    String dateStr = Formatter.formatDateTime(new Date());
    Integer status = (Integer) errors.get("status");
    String error = (String) errors.get("error");
    String path = (String) errors.get("path");

    String sURL = ServerConfig.getInstance().getSopSiteURL();
    String sUserDomain = sop.iwe.SessionManager.getInstance(request)
            .getCurrentUserDomain();

    pageContext.setAttribute("sURL", sURL);
  %>
  <meta http-equiv="refresh" content="0;url=${sURL}">
<!--
  <div>
    <div>
      <div class="fb-rounded-box-content-error-Appexception fb-summary-info-error-Appexception">
        <div>
          <div id="title_head">
            <strong>An unexpected error has been encountered</strong>
          </div><br/>
          <div id="exception_content">
            <%=status%>-<%=error%><br/><%=path%><br/>at <%=dateStr%><br/>
          </div><br/><hr/>
          <div class="fb-summary-info-error-Appexception-action">
            We are sorry for the error you have encountered.<br/>Please report it to your administrator and we will investigate it.
          </div>
        </div>
      </div>
    </div>
  </div>
  -->
</body>
</html>


