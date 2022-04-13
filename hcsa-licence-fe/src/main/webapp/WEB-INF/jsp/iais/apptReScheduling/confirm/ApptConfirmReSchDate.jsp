<%--
  Created by IntelliJ IDEA.
  User: ShiCheng_Xu
  Date: 2020/6/18
  Time: 13:37
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
<%@include file="../../common/dashboard.jsp"%>
<div class="container" style="margin-left: 320px;">
  <div class="component-gp">
    <br>
    <form method="post" id="mainConfirmForm" action=<%=process.runtime.continueURL()%>>
      <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
      <input type="hidden" name="apptUserChooseDateType" value="">
      <input type="hidden" id="actionValue" name="actionValue" value="">
      <div class="main-content">
        <div class="row">
          <div class="col-lg-12 col-xs-12">
            <div class="intranet-content">
              <c:if test="${'SUCCESS' eq apptInspFlag}">
                <iais:section title="" id = "rec_ack_page">
                  <div class="bg-title">
                    <h3 style="border-bottom: 0px solid">Submission Successful</h3>
                  </div>
                  <iais:row>
                    <iais:value width="7">
                      <p><label>Congratulations, you have successfully submitted to MOH.</label></p>
                    </iais:value>
                  </iais:row>
                  <iais:action >
                    <p class="print">
                      <a class="btn btn-primary" style="float:right" href="/main-web/eservice/INTERNET/MohInternetInbox" >Go to Dashboard</a>
                    </p>
                  </iais:action>
                </iais:section>
              </c:if>
              <c:if test="${'SUCCESS' ne apptInspFlag}">
                <iais:section title="" id = "ava_appt_date">
                  <div class="row">
                    <div class="col-md-4">
                      <label style="font-size: 16px">Available Appointment Dates</label>
                    </div>
                  </div>
                  <div class="row">
                    <div class="col-md-6">
                      <c:forEach items="${processReSchedulingDto.inspectionDate}" var="date">
                        <br><input class="form-check-input" type="radio" name="apptCheckDate" aria-invalid="true" value="${date.value}" <c:if test="${date.value eq processReSchedulingDto.checkDate}">checked</c:if>>
                        <span><c:out value = "${date.text}"/></span>
                      </c:forEach>
                      <br><span class="error-msg" name="iaisErrorMsg" id="error_checkDate"></span>
                    </div>
                  </div>
                  <br>
                  <iais:action>
                    <button class="btn btn-primary" style="float:right" data-toggle= "modal" data-target= "#rejectDate" type="button">Reject All</button>
                    <iais:confirm yesBtnCls="btn btn-primary" msg="OAPPT_ACK008" callBack="apptConfirmReSchDateRej()" popupOrder="rejectDate" title="Message from webpage" needCancel="false"></iais:confirm>
                    <span style="float:right">&nbsp;</span>
                    <button class="btn btn-primary" style="float:right" type="button" onclick="javascript:apptConfirmReSchDateCon()">Accept</button>
                  </iais:action>
                </iais:section>
              </c:if>
            </div>
          </div>
        </div>
      </div>
    </form>
  </div>
</div>
<%@ include file="/WEB-INF/jsp/include/validation.jsp" %>
<script type="text/javascript">
    $("#print-ack").click(function () {
        showWaiting();
        window.print();
    })

    function apptConfirmReSchDateSubmit(action){
        $("[name='apptUserChooseDateType']").val(action);
        var mainPoolForm = document.getElementById('mainConfirmForm');
        mainPoolForm.submit();
    }

    function apptConfirmReSchDateRej() {
        showWaiting();
        $("#actionValue").val('reject');
        apptConfirmReSchDateSubmit('reject');
    }

    function apptConfirmReSchDateCon() {
        showWaiting();
        $("#actionValue").val('success');
        apptConfirmReSchDateSubmit('success');
    }
</script>

