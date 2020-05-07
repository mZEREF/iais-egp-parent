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
  String webroot=IaisEGPConstant.BE_CSS_ROOT;
%>
<div class="dashboard" style="background-image:url('<%=webroot%>img/Masthead-banner.jpg')">
  <form method="post" id="mainConForm" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
    <input type="hidden" name="inspSupAddAvailabilityType" value="">
    <input type="hidden" name="nonActionValue" value="">
    <input type="hidden" name="lastActionValue" id="lastActionValue" value="<c:out value="${actionValue}"/>">
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
                          <label style="font-size: 16px">Name</label>
                        </div>
                        <div class="col-md-6">
                          <span style="font-size: 16px"><iais:code code="${inspNonAvailabilityDto.checkUserName}"/></span>
                        </div>
                      </div>
                      <div class="row">
                        <div class="col-md-2">
                          <label style="font-size: 16px">Blocked Out Date</label>
                        </div>
                        <div class="col-md-6">
                          <span style="font-size: 16px"><fmt:formatDate value='${inspNonAvailabilityDto.blockOutStart}' pattern='dd/MM/yyyy'/> To <fmt:formatDate value='${inspNonAvailabilityDto.blockOutEnd}' pattern='dd/MM/yyyy' /></span>
                        </div>
                      </div>
                      <div class="row">
                        <div class="col-md-2">
                          <label style="font-size: 16px">Blocked Out Date Description</label>
                        </div>
                        <div class="col-md-6">
                          <textarea id="blockOutDesc" name="blockOutDesc" cols="70" rows="7" maxlength="255" disabled><c:out value="${inspNonAvailabilityDto.blockOutDesc}"></c:out></textarea>
                        </div>
                      </div>
                      <div class="row">
                        <div class="col-md-2">
                          <label style="font-size: 16px">Recurrence</label>
                        </div>
                        <div class="col-md-6">
                          <span style="font-size: 16px"><c:out value="${inspNonAvailabilityDto.recurrence}"/></span>
                        </div>
                      </div>
                      <iais:action >
                        <button class="btn btn-primary" style="float:right" type="button" onclick="javascript:doInspAvailabilityConSubmit()">Submit</button>
                        <a class="back" id="Back" onclick="javascript:doInspAvailabilityConBack()" style="float:left"><em class="fa fa-angle-left"></em> Back</a>
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
