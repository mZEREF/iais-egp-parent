<%--
  Created by IntelliJ IDEA.
  User: ShiCheng_Xu
  Date: 2020/2/16
  Time: 9:46
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
<div class="container">
  <div class="component-gp">
    <br>
    <form method="post" id="mainReConfirmForm" action=<%=process.runtime.continueURL()%>>
      <%@ include file="/include/formHidden.jsp" %>
      <input type="hidden" name="userComfireInspDateType" value="">
      <input type="hidden" id="actionValue" name="actionValue" value="">
      <div class="main-content">
        <div class="row">
          <div class="col-lg-12 col-xs-12">
            <div class="center-content">
              <div class="intranet-content">
                <iais:body >
                  <iais:section title="" id = "ava_apptnew_date">
                    <div class="row">
                      <div class="col-md-4">
                        <label style="font-size: 16px">Available Appointment Dates</label>
                      </div>
                    </div>
                    <div class="row">
                      <div class="col-md-6">
                        <c:if test="${apptFeConfirmDateDto.inspectionNewDate != null}">
                          <c:forEach items="${apptFeConfirmDateDto.inspectionNewDate}" var="newDate">
                            <br><input class="form-check-input" type="radio" name="apptCheckNewDate" aria-invalid="true" value="${newDate.value}" <c:if test="${newDate.value eq apptFeConfirmDateDto.checkNewDate}">checked</c:if>>
                            <span><c:out value = "${newDate.text}"/></span>
                          </c:forEach>
                          <br><span class="error-msg" name="iaisErrorMsg" id="error_checkNewDate"></span>
                        </c:if>
                        <c:if test="${apptFeConfirmDateDto.inspectionNewDate == null}">
                          <span><c:out value = "-"/></span>
                        </c:if>
                      </div>
                    </div>
                    <br>
                    <iais:action >
                      <c:if test="${apptFeConfirmDateDto.inspectionNewDate != null}">
                        <button class="btn btn-primary" style="float:right" type="button" onclick="javascript:apptTwoConfirmInspDateCon()">Confirm</button>
                      </c:if>
                      <a class="back" id="Back" onclick="javascript:apptTwoConfirmInspDateBack()" style="float:left"><em class="fa fa-angle-left"></em> Back</a>
                    </iais:action>
                  </iais:section>
                </iais:body>
              </div>
            </div>
          </div>
        </div>
      </div>
    </form>
  </div>
</div>
<%@ include file="/include/validation.jsp" %>
<script type="text/javascript">
    function apptTwoConfirmInspDateSubmit(action){
        $("[name='userComfireInspDateType']").val(action);
        var mainPoolForm = document.getElementById('mainReConfirmForm');
        mainPoolForm.submit();
    }

    function apptTwoConfirmInspDateCon() {
        showWaiting();
        $("#actionValue").val('success');
        apptTwoConfirmInspDateSubmit('success');
    }

    function apptTwoConfirmInspDateBack() {
        showWaiting();
        $("#actionValue").val('back');
        apptTwoConfirmInspDateSubmit('back');
    }
</script>
