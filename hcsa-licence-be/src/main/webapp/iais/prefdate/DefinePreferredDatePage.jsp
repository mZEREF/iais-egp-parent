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

<form id = "mainForm" method = "post" action=<%=process.runtime.continueURL()%>>
  <input type="hidden" name="crud_action_type" value="">
  <input type="hidden" name="crud_action_value" value="">
  <input type="hidden" name="crud_action_additional" value="">
  <%@ include file="/include/formHidden.jsp" %>


  <div class="main-content">
    <div class="container">
      <br>
      <span id="error_dataError" name="iaisErrorMsg" class="error-msg"></span>
      <span id="error_numberError" name="iaisErrorMsg" class="error-msg"></span>
      <br><br><br>
      <div class="tab-pane active" id="tabInbox" role="tabpanel">
        <div class="form-horizontal">
          <div class="form-group">
            <div class="col-xs-10 col-md-10">
              <iais:field value="Service Name" required="true"></iais:field>
              <div class="col-xs-10 col-md-10">
                ${requestPperiodAttr.svcCode}
                <span id="error_svcCode" name="iaisErrorMsg" class="error-msg"></span>
              </div>
            </div>
          </div>

          <div class="form-group">
            <div class="col-xs-10 col-md-10">
              <iais:field value="Block-out Period after Application" required="true"></iais:field> wks
              <div class="col-xs-10 col-md-10">
                <input type="text" name="periodAfterApp"  value="${requestPperiodAttr.periodAfterApp}" />
                <span id="error_periodAfterApp" name="iaisErrorMsg" class="error-msg"></span>
              </div>
            </div>
          </div>


          <div class="form-group">
            <div class="col-xs-10 col-md-10">
              <iais:field value="Block-out Period before Expiry" required="true"></iais:field> wks
              <div class="col-xs-10 col-md-10">
                <input type="text" name="periodBeforeExp" value="${requestPperiodAttr.periodBeforeExp}" />
                <span id="error_periodBeforeExp" name="iaisErrorMsg" class="error-msg"></span>
              </div>
            </div>
          </div>

          <div class="form-group">
            <div class="col-xs-10 col-md-10">
              <iais:field value="Non-reply Notification Window" required="true"></iais:field> working day
              <div class="col-xs-10 col-md-10">
                <input type="text" name="nonReplyWindow" value="${requestPperiodAttr.nonReplyWindow}" />
                <span id="error_nonReplyWindow" name="iaisErrorMsg" class="error-msg"></span>
              </div>
            </div>
          </div>

        </div>
      </div>
      <div class="col-xs-12 col-sm-6">
        <p><a class="back" onclick="doCancel();"><em class="fa fa-angle-left" ></em> Back</a></p>
      </div>

      <div class="application-tab-footer">
        <div class="row">
          <div class="col-xs-12 col-sm-12">
            <div class="text-right text-center-mobile"><a class="btn btn-primary" href="#">Update</a></div>
          </div>
        </div>
      </div>

    </div>


  </div>

</form>
<%@include file="/include/validation.jsp"%>
<script>
    $(".back").click(function () {
        SOP.Crud.cfxSubmit("mainForm", "doBack");
    })

    $(".text-center-mobile").click(function () {
        SOP.Crud.cfxSubmit("mainForm", "submitPrefDate");
    })

</script>

