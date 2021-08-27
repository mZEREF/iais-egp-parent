<%--
  Created by IntelliJ IDEA.
  User: ShiCheng_Xu
  Date: 2020/1/13
  Time: 13:42
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%
  //handle to the Engine APIs
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>

<%
  String webroot=IaisEGPConstant.CSS_ROOT + IaisEGPConstant.BE_CSS_ROOT;
%>
<div class="dashboard" style="background-image:url('<%=webroot%>img/Masthead-banner.jpg')">
  <form method="post" id="mainConForm" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
    <input type="hidden" name="inspSupAddAvailabilityType" value="">
    <input type="hidden" name="nonActionValue" value="">
    <input type="hidden" name="lastActionValue" id="lastActionValue" value="<c:out value="${lastActionValue}"/>">
    <div class="main-content">
      <div class="row">
        <div class="col-lg-12 col-xs-12">
          <div class="center-content">
            <div class="intranet-content">
              <div class="container">
                <div class="col-xs-12">
                  <iais:body >
                    <iais:section title="" id = "addAvailability">
                      <h2>
                        <span>Confirm Non-Availability Form</span>
                      </h2>
                      <div class="row">
                        <div class="col-md-2">
                          <label style="font-size: 16px">Inspector ID</label>
                        </div>
                        <div class="col-md-6">
                          <span style="font-size: 16px"><c:out value="${userName}"/></span>
                        </div>
                      </div>
                      <div class="row">
                        <div class="col-md-2">
                          <label style="font-size: 16px">Non-Available Date Start</label>
                        </div>
                        <div class="col-md-6">
                          <span style="font-size: 16px"><fmt:formatDate value='${inspNonAvailabilityDto.blockOutStart}' pattern='dd/MM/yyyy'/></span>
                        </div>
                      </div>
                      <p></p>
                      <div class="row">
                        <div class="col-md-2">
                          <label style="font-size: 16px">Non-Available Date End</label>
                        </div>
                        <div class="col-md-6">
                          <span style="font-size: 16px"><fmt:formatDate value='${inspNonAvailabilityDto.blockOutEnd}' pattern='dd/MM/yyyy' /></span>
                          <c:if test="${'true' eq containDate}">
                            <br><span class="error-msg"><iais:message key="OAPPT_ACK009" escape="false"></iais:message></span>
                          </c:if>
                        </div>
                      </div>
                      <p></p>
                      <div class="row form-group" style="margin-bottom: 15px;">
                        <div class="col-md-2">
                          <label style="font-size: 16px">Non-Available Date Description</label>
                        </div>
                        <div class="col-md-6">
                          <c:if test="${empty inspNonAvailabilityDto.nonAvaDescription}">
                            <span style="font-size: 16px"><c:out value="-"></c:out></span>
                          </c:if>
                          <c:if test="${not empty inspNonAvailabilityDto.nonAvaDescription}">
                            <span style="font-size: 16px"><c:out value="${inspNonAvailabilityDto.nonAvaDescription}"></c:out></span>
                          </c:if>
                        </div>
                      </div>
                      <div class="row">
                        <div class="col-md-2">
                          <label style="font-size: 16px">Recurrence</label>
                        </div>
                        <div class="col-md-6">
                          <span style="font-size: 16px"><iais:code code ="${inspNonAvailabilityDto.recurrence}"/></span>
                        </div>
                      </div>
                      <iais:action >
                        <button class="btn btn-primary" style="float:right" type="button" onclick="javascript:doInspAvailabilityConSubmit()">Submit</button>
                        <a href="#" class="back" id="Back" onclick="javascript:doInspAvailabilityConBack()" style="float:left"><em class="fa fa-angle-left"></em> Back</a>
                      </iais:action>
                    </iais:section>
                  </iais:body>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </form>
</div>
<%@ include file="/WEB-INF/jsp/include/validation.jsp" %>
<script type="text/javascript">
    function doInspAvailabilityConBack() {
        var lastActionValue = $("[name='lastActionValue']").val();
        $("[name='nonActionValue']").val(lastActionValue);
        inspAvailabilityConSubmit(lastActionValue);
    }

    function doInspAvailabilityConSubmit() {
        showWaiting();
        $("[name='nonActionValue']").val('back');
        inspAvailabilityConSubmit('back');
    }
    function inspAvailabilityConSubmit(action){
        $("[name='inspSupAddAvailabilityType']").val(action);
        var mainPoolForm = document.getElementById('mainConForm');
        mainPoolForm.submit();
    }
</script>
