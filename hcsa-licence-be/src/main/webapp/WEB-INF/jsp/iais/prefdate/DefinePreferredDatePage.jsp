<%--
  Created by IntelliJ IDEA.
  User: yichen
  Date: 12/24/2019
  Time: 6:56 PM
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
<div class="main-content">
<form id = "mainForm" method = "post" action=<%=process.runtime.continueURL()%>>
  <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
      <br>
      <span id="error_dataError" name="iaisErrorMsg" class="error-msg"></span>
      <span id="error_numberError" name="iaisErrorMsg" class="error-msg"></span>
      <br><br><br>


      <div class="form-horizontal">
        <div class="form-group">
            <iais:field value="Service Name" required="true"></iais:field>
            <div class="col-xs-5 col-md-3" >
                <iais:service value="${requestPeriodAttr.svcCode}" isSvcCode="true"></iais:service>
            </div>
            <span id="error_svcCode" name="iaisErrorMsg" class="error-msg"></span>
        </div>


        <div class="form-group">
          <iais:field value="Block-out Period after Application" required="true"></iais:field>
          <div class="col-xs-5 col-md-3" >
            <input type="text" name="periodAfterApp" maxlength="2" value="<c:out value="${sessionScope.requestPeriodAttr.periodAfterApp}"/>" />
            <span id="error_periodAfterApp" name="iaisErrorMsg" class="error-msg"></span>
          </div>
            <div style="padding-top: 12px;">Weeks</div>
        </div>

        <div class="form-group">
          <iais:field value="Block-out Period before Expiry" required="true"></iais:field>
          <div class="col-xs-5 col-md-3" >
            <input type="text" name="periodBeforeExp" maxlength="2" value="<c:out value="${sessionScope.requestPeriodAttr.periodBeforeExp}"/>" />
            <span id="error_periodBeforeExp" name="iaisErrorMsg" class="error-msg"></span>
          </div>
          <div style="padding-top: 12px;">Weeks</div>
        </div>


        <div class="form-group">
          <iais:field value="Non-reply Notification Window" required="true"></iais:field>
          <div class="col-xs-5 col-md-3" >
            <input type="text" name="nonReplyWindow" maxlength="2" value="<c:out value="${sessionScope.requestPeriodAttr.nonReplyWindow}"/>" />
            <span id="error_nonReplyWindow" name="iaisErrorMsg" class="error-msg"></span>
          </div>
            <div style="padding-top: 12px;">Working days</div>
        </div>

      </div>

      <div class="col-xs-12 col-sm-6">
        <a class="back" onclick="doCancel();"><em class="fa fa-angle-left" ></em> Back</a>
      </div>


        <div class="row">
          <div class="col-xs-12 col-sm-12">
            <div class="text-right text-center-mobile"><a id="submitPrefDate" class="btn btn-primary" href="#">Update</a></div>
          </div>
        </div>

</form>
</div>
<%@include file="/WEB-INF/jsp/include/validation.jsp"%>
<script>
    $(".back").click(function () {
        SOP.Crud.cfxSubmit("mainForm", "doBack");
    })

    $("#submitPrefDate").click(function () {
        SOP.Crud.cfxSubmit("mainForm", "submitPrefDate");
    })

</script>

