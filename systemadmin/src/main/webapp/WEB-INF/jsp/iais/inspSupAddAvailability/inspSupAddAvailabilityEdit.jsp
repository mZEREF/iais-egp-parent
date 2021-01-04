<%--
  Created by IntelliJ IDEA.
  User: ShiCheng_Xu
  Date: 2020/1/13
  Time: 15:56
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
<style type="text/css">
  #blockOutStart {
    margin-bottom: 0px;
  }
  #blockOutEnd {
    margin-bottom: 0px;
  }
  div.nice-select.input-large {
    margin-bottom: 0px;
  }
</style>
<div class="dashboard" style="background-image:url('<%=webroot%>img/Masthead-banner.jpg')">
  <form method="post" id="mainEditForm" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
    <input type="hidden" name="inspSupAddAvailabilityType" value="">
    <input type="hidden" name="nonActionValue" value="">
    <div class="main-content">
      <div class="row">
        <div class="col-lg-12 col-xs-12">
          <div class="center-content">
            <div class="intranet-content">
              <div class="container">
                <div class="col-xs-12">
                  <iais:body>
                    <iais:section title="" id = "editAvailability">
                      <h2>
                        <span>Update Non-Availability Form</span>
                      </h2>
                      <div class="form-group" style="margin-bottom: 0px;">
                        <iais:field value="Inspector ID"/>
                        <iais:value width="7">
                          <p><span style="font-size: 16px"><c:out value="${userName}"/></span></p>
                        </iais:value>
                      </div>
                      <div class="form-group">
                        <iais:field value="Non-Available Date Start" required="true"/>
                        <iais:value width="7">
                          <iais:datePicker id = "blockOutStart" name = "blockOutStart" dateVal="${inspNonAvailabilityDto.blockOutStart}"></iais:datePicker>
                          <span class="error-msg" name="iaisErrorMsg" id="error_nonAvaStartDate"></span>
                        </iais:value>
                      </div>
                      <div class="form-group">
                        <iais:field value="Non-Available Date End" required="true"/>
                        <iais:value width="7">
                          <iais:datePicker id = "blockOutEnd" name = "blockOutEnd" dateVal="${inspNonAvailabilityDto.blockOutEnd}"></iais:datePicker>
                          <span class="error-msg" name="iaisErrorMsg" id="error_nonAvaDate"></span>
                        </iais:value>
                      </div>
                      <div class="form-group">
                        <iais:field value="Non-Available Date Description"/>
                        <iais:value width="7">
                          <textarea id="blockOutDesc" name="blockOutDesc" cols="70" rows="7" maxlength="255" ><c:out value="${inspNonAvailabilityDto.nonAvaDescription}"></c:out></textarea>
                        </iais:value>
                      </div>
                      <div class="form-group" style="margin-bottom: 150px;">
                        <iais:field value="Recurrence" required="true"/>
                        <iais:value width="7">
                          <iais:select name="recurrence" options="recurrenceOption" firstOption="Please Select" value="${inspNonAvailabilityDto.recurrence}"></iais:select>
                          <br><span class="error-msg" name="iaisErrorMsg" id="error_recurrence"></span>
                        </iais:value>
                      </div>
                      <iais:action >
                        <button class="btn btn-primary" style="float:right" type="button" onclick="javascript:doInspAvailabilityEditNext()">Confirm</button>
                        <a class="back" id="Back" onclick="javascript:doInspAvailabilityEditBack()" style="float:left"><em class="fa fa-angle-left"></em> Back</a>
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
    function doInspAvailabilityEditBack() {
        showWaiting();
        $("[name='nonActionValue']").val('back');
        inspAvailabilityEditSubmit('back');
    }

    function doInspAvailabilityEditNext() {
        showWaiting();
        $("[name='nonActionValue']").val('confirm');
        inspAvailabilityEditSubmit('confirm');
    }
    function inspAvailabilityEditSubmit(action){
        $("[name='inspSupAddAvailabilityType']").val(action);
        var mainPoolForm = document.getElementById('mainEditForm');
        mainPoolForm.submit();
    }
</script>