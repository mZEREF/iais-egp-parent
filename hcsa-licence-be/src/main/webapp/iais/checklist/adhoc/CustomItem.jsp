<%--
  Created by IntelliJ IDEA.
  User: yichen
  Date: 12/11/2019
  Time: 2:45 PM
  To change this template use File | Settings | File Templates.
--%>

<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais"   prefix="iais"%>
<webui:setLayout name="iais-intranet"/>


<%
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<style>
  .form-check-gp{
    width: 50%;
    float:left;
  }

  .col-md-3 {
    width: 50%;
  }

 .alert {
   padding: 15px;
   border: 1px solid #d6e9c6;
   border-radius: 4px;
   color: #3c763d;
   background-color: #dff0d8;
 }


</style>


<form id = "mainForm" method = "post" action=<%=process.runtime.continueURL()%>>
  <%@ include file="/include/formHidden.jsp" %>
  <input type="hidden" name="crud_action_type" value="">
  <input type="hidden" name="crud_action_value" value="">
  <input type="hidden" name="crud_action_additional" value="">
  <br><br><br>
  <div class="main-content">
    <div class="container">
      <div class="tab-pane active" id="tabInbox" role="tabpanel">
        <div class="form-horizontal">
          <div class="form-group">

            <div class="col-xs-5 col-md-3">
              <iais:field value="Checklist Item" required="true"></iais:field>
              <div class="col-xs-5 col-md-3">
                <input type="text" name="checklistItem" value="" />
                <span id="error_checklistItem" name="iaisErrorMsg" class="error-msg"></span>
              </div>

            </div>
          </div>

          <div class="form-group">
            <div class="col-xs-5 col-md-3">
              <iais:field value="Risk Level" required="true"></iais:field>
              <div class="col-xs-5 col-md-3">
                <iais:select name="riskLevel" id="riskLevel" codeCategory="CATE_ID_RISK_LEVEL" firstOption="Select Risk Level" value=""></iais:select>
                <span id="error_riskLevel" name="iaisErrorMsg" class="error-msg"></span>
              </div>

            </div>
          </div>

          <div class="form-group">
            <div class="col-xs-5 col-md-3">
              <iais:field value="Answer Type" required="true"></iais:field>
              <div class="col-xs-5 col-md-3">
                <iais:select name="answerType" id="answerType" codeCategory="CATE_ID_ANSWER_TYPE" firstOption="Select Answer Type" value=""></iais:select>
                <span id="error_answerType" name="iaisErrorMsg" class="error-msg"></span>
              </div>

            </div>
          </div>
        </div>
      </div>
      <div class="col-xs-12 col-sm-6">
        <p><a class="back" onclick="doBack();"><em class="fa fa-angle-left" ></em> Back</a></p>
      </div>
      <div class="text-right text-center-mobile">
            <a class="btn btn-primary appendClass " id = "customItemId">Custom</a>
      </div>
    </div>


  </div>


</>

<%@include file="/include/validation.jsp"%>
<script>
    "use strict";
    customItemId.onclick = function(){
        doValidation();
        if (getErrorMsg()) {
            dismissWaiting();
        } else {
            SOP.Crud.cfxSubmit("mainForm", "customItem");
        }
    }
  
    function doBack() {
        SOP.Crud.cfxSubmit("mainForm", "doBack");
    }

</script>