<%--
  Created by IntelliJ IDEA.
  User: ShiCheng_Xu
  Date: 2020/2/16
  Time: 9:48
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais"%>

<%
  //handle to the Engine APIs
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>
<%@include file="../common/dashboard.jsp"%>
<div class="container" style="margin-left: 320px;">
  <div class="component-gp">
    <br>
    <form method="post" id="mainReConfirmForm" action=<%=process.runtime.continueURL()%>>
      <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
      <input type="hidden" name="userComfireInspDateType" value="">
      <input type="hidden" id="actionValue" name="actionValue" value="">
      <div class="main-content">
        <div class="row">
          <div class="col-lg-12 col-xs-12">
            <div class="intranet-content">
              <iais:section title="" id = "ava_apptrej_date">
                <div class="row">
                  <div class="col-md-4" style="padding-right: 0px;">
                    <label style="font-size: 16px">Available Appointment Dates<span style="color: red"> *</span></label>
                  </div>
                  <div class="col-md-6" style="padding-left: 0px;">
                    <div class="col-xs-12 col-md-4">
                      <iais:datePicker id = "rejectDate" name = "rejectDate" value="${apptFeConfirmDateDto.rejectDate}"></iais:datePicker>
                      <br><span class="error-msg" name="iaisErrorMsg" id="error_rejectDate"></span>
                    </div>
                    <div class="col-xs-12 col-md-3">
                      <iais:select name="apptAmPm" options="amPmFeOption" firstOption="N/A" value="${apptFeConfirmDateDto.amPm}"></iais:select>
                      <br><span class="error-msg" name="iaisErrorMsg" id="error_amPm"></span>
                    </div>
                  </div>
                </div>
                <iais:row>
                  <iais:field value="Enter Reason" required="true"/>
                  <iais:value width="7">
                    <textarea maxlength="300" id="apptRejectReason" name="apptRejectReason" cols="70" rows="7" ><c:out value="${apptFeConfirmDateDto.reason}"></c:out></textarea>
                    <br><span class="error-msg" name="iaisErrorMsg" id="error_reason"></span>
                  </iais:value>
                </iais:row>
                <iais:action >
                  <button class="btn btn-primary" style="float:right" type="button" onclick="javascript:apptRejectInspDateCon()">Confirm</button>
                  <a class="back" id="Back" onclick="javascript:apptRejectInspDateBack()" style="float:left"><em class="fa fa-angle-left"></em> Back</a>
                </iais:action>
              </iais:section>
            </div>
          </div>
        </div>
      </div>
    </form>
  </div>
</div>
<%@ include file="/WEB-INF/jsp/include/validation.jsp" %>
<script type="text/javascript">
    function apptRejectInspDateSubmit(action){
        $("[name='userComfireInspDateType']").val(action);
        var mainPoolForm = document.getElementById('mainReConfirmForm');
        mainPoolForm.submit();
    }

    function apptRejectInspDateCon() {
        showWaiting();
        $("#actionValue").val('ack');
        apptRejectInspDateSubmit('ack');
    }

    function apptRejectInspDateBack() {
        showWaiting();
        $("#actionValue").val('back');
        apptRejectInspDateSubmit('back');
    }
</script>
