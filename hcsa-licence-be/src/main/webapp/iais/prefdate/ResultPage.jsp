<%--
  Created by IntelliJ IDEA.
  User: yichen
  Date: 12/25/2019
  Time: 3:27 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais"   prefix="iais"%>
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<webui:setLayout name="iais-intranet"/>


<%
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>

<style>

</style>

<form id = "mainForm" method = "post" action=<%=process.runtime.continueURL()%>>
  <input type="hidden" name="crud_action_type" value="">
  <input type="hidden" name="crud_action_value" value="">
  <input type="hidden" name="crud_action_additional" value="">

  <div class="center-content">
    <div class="intranet-content">
      <div class="bg-title">
        <h2>Moh Define Preferred Date Range Period Ack Page</h2>
      </div>
      <br>
      <span id="error_numberError" name="iaisErrorMsg" class="error-msg"></span>
      <br><br><br>

  </div>

</form>
