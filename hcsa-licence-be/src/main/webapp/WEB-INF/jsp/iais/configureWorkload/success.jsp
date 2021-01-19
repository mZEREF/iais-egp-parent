<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais"%>
<%
  //handle to the Engine APIs
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>
<form id = "mainForm" method = "post" action=<%=process.runtime.continueURL()%>>
  <div class="main-content">
    <div class="container">
      <div class="form-horizontal">
        <div class="form-group">
          <h1>succcess</h1>
        </div>
      </div>
    </div>
  </div>
</form>