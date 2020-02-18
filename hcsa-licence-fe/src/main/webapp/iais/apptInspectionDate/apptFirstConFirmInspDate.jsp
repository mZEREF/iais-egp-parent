<%--
  Created by IntelliJ IDEA.
  User: ShiCheng_Xu
  Date: 2020/2/16
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

<div class="container">
  <div class="component-gp">
    <br>
    <form method="post" id="mainConfirmForm" action=<%=process.runtime.continueURL()%>>
      <%@ include file="/include/formHidden.jsp" %>
      <br>
      <br>
      <br>
      <br>
      <br>
      <input type="hidden" name="userComfireInspDateType" value="">
      <input type="hidden" id="actionValue" name="actionValue" value="">
      <iais:body >
        <div class="container">
          <div class="col-xs-12">
            <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
              <h3>
                <span>Appointment Scheduling</span>
              </h3>
              <div class="panel panel-default">
                <div class="panel-collapse collapse in" id="collapseOne" role="tabpanel" aria-labelledby="headingOne" aria-expanded="true" style="">
                  <div class="panel-body">
                    <div class="panel-main-content">
                      <iais:section title="" id = "ava_appt_date">
                        <iais:row>
                          <iais:field value="Available Appointment Dates"/>
                          <iais:value width="7">
                            <c:if test="${apptFeConfirmDateDto.inspectionDate != null}">
                              <c:forEach items="${apptFeConfirmDateDto.inspectionDate}" var="date">
                                <input class="form-check-input" type="radio" name="inspectionDate" aria-invalid="true" value="${date.value}" <c:if test="${date.value eq apptFeConfirmDateDto.checkDate}">checked</c:if>>
                                <label><c:out value = "${date.text}"/></label>
                                <br><span class="error-msg" name="iaisErrorMsg" id="error_checkDate"></span>
                              </c:forEach>
                            </c:if>
                          </iais:value>
                        </iais:row>
                        <iais:action >
                          <button class="btn btn-lg btn-login-next" style="float:right" type="button" onclick="javascript:apptFirstConFirmInspDateRej()">Request a particular date</button>
                          <span style="float:right">&nbsp;</span>
                          <button class="btn btn-lg btn-login-next" style="float:right" type="button" onclick="javascript:apptFirstConFirmInspDateRec()">Recompute other dates</button>
                          <span style="float:right">&nbsp;</span>
                          <button class="btn btn-lg btn-login-next" style="float:right" type="button" onclick="javascript:apptFirstConFirmInspDateCon()">Confirm</button>
                        </iais:action>
                      </iais:section>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </iais:body>
    </form>
  </div>
</div>
<%@ include file="/include/validation.jsp" %>
<script type="text/javascript">
    function apptFirstConFirmInspDateSubmit(action){
        $("[name='userComfireInspDateType']").val(action);
        var mainPoolForm = document.getElementById('mainConfirmForm');
        mainPoolForm.submit();
    }

    function apptFirstConFirmInspDateRej() {
        $("#actionValue").val('reject');
        apptFirstConFirmInspDateSubmit('reject');
    }

    function apptFirstConFirmInspDateRec() {
        $("#actionValue").val('reconfirm');
        apptFirstConFirmInspDateSubmit('reconfirm');
    }

    function apptFirstConFirmInspDateCon() {
        $("#actionValue").val('confirm');
        apptFirstConFirmInspDateSubmit('confirm');
    }
</script>

