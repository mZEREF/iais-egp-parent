<%--
  Created by IntelliJ IDEA.
  User: ShiCheng_Xu
  Date: 2021/7/27
  Time: 9:45
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
    <form method="post" id="tcuAuditApptForm" action=<%=process.runtime.continueURL()%>>
      <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
      <input type="hidden" name="feTcuAuditApptPreDateType" value="">
      <input type="hidden" id="actionValue" name="actionValue" value="">
      <div class="main-content">
        <div class="row">
          <div class="col-lg-12 col-xs-12">
            <div class="intranet-content">
              <c:if test="${'SUCCESS' eq tcuAuditApptPreDateFlag}">
                <iais:section title="" id = "tcu_audit_appt">
                  <div class="bg-title">
                    <h3 style="border-bottom: 0px solid">Acknowledgement</h3>
                  </div>
                  <iais:row>
                    <iais:value width="7">
                      <p><label><iais:message key="LOLEV_ACK045" escape="true"></iais:message></label></p>
                    </iais:value>
                  </iais:row>
                  <iais:action >
                    <a class="btn btn-primary" style="float:right" href="/main-web/eservice/INTERNET/MohInternetInbox" >Go to Dashboard</a>
                  </iais:action>
                </iais:section>
              </c:if>
              <c:if test="${'SUCCESS' ne tcuAuditApptPreDateFlag}">
                <span id="error_dateError" name="iaisErrorMsg" class="error-msg"></span>
                <br><br>
                <iais:section title="" id = "tcu_audit_appt">
                  <iais:row>
                    <iais:field value="Preferred date for inspection (Start)"  required="true"/>
                    <iais:value width="18">
                      <iais:datePicker id = "inspStartDate" name = "inspStartDate" value="${inspStartDate}"></iais:datePicker>
                      <span id="error_inspStartDate" name="iaisErrorMsg" class="error-msg"></span>
                    </iais:value>
                  </iais:row>
                  <iais:row>
                    <iais:field value="Preferred date for inspection (End)" required="true"/>
                    <iais:value width="18">
                      <iais:datePicker id = "inspEndDate" name = "inspEndDate" value="${inspEndDate}"></iais:datePicker>
                      <span id="error_inspEndDate" name="iaisErrorMsg" class="error-msg"></span>
                    </iais:value>
                  </iais:row>
                  <br>
                  <iais:action>
                    <button class="btn btn-primary" style="float:right" type="button" onclick="javascript:tcuAuditApptPreDateVali()">Submit</button>
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

    function tcuAuditApptPreDateSubmit(action){
        $("[name='feTcuAuditApptPreDateType']").val(action);
        var mainPoolForm = document.getElementById('tcuAuditApptForm');
        mainPoolForm.submit();
    }

    function tcuAuditApptPreDateVali() {
        showWaiting();
        $("#actionValue").val('validate');
        tcuAuditApptPreDateSubmit('validate');
    }
</script>