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
  <form method="post" id="mainAddForm" action=<%=process.runtime.continueURL()%>>
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
                  <iais:body >
                    <iais:section title="" id = "addAvailability">
                      <h2>
                        <span>Add Non-Availability Form</span>
                      </h2>
                      <iais:row>
                        <c:if test="${curRole eq 'INSPECTOR_LEAD'}">
                          <iais:field value="Name" required="true"/>
                          <iais:value width="7">
                            <iais:select name="nonAvaUserNameId" options="nonAvaUserName" firstOption="Please Select" value="${inspNonAvailabilityDto.userCorrId}" ></iais:select>
                            <br><span class="error-msg" name="iaisErrorMsg" id="error_userName"></span>
                          </iais:value>
                        </c:if>
                        <c:if test="${curRole ne 'INSPECTOR_LEAD'}">
                          <iais:field value="Name"/>
                          <iais:value width="7">
                            <span style="font-size: 16px"><c:out value="${userName}"/></span>
                          </iais:value>
                        </c:if>
                      </iais:row>
                      <div class="form-group" style="margin-bottom: 0px;">
                        <iais:field value="Blocked Out Date" required="true"/>
                        <iais:value width="7">
                          <iais:datePicker id = "nonAvaStartDate" name = "nonAvaStartDate" value="${inspNonAvailabilityDto.blockOutStart}"></iais:datePicker>
                          <span style="font-size: 16px">To</span><p></p>
                          <iais:datePicker id = "nonAvaEndDate" name = "nonAvaEndDate" value="${inspNonAvailabilityDto.blockOutEnd}"></iais:datePicker>
                          <br><span class="error-msg" name="iaisErrorMsg" id="error_blockOutStart"></span>
                          <br><span class="error-msg" name="iaisErrorMsg" id="error_blockOutEnd"></span>
                        </iais:value>
                      </div>
                      <div class="form-group">
                        <iais:field value="Blocked Out Date Description"/>
                        <iais:value width="7">
                          <textarea id="blockOutDesc" name="blockOutDesc" cols="70" rows="7" maxlength="255" ><c:out value="${inspNonAvailabilityDto.nonAvaDescription}"></c:out></textarea>
                        </iais:value>
                      </div>
                      <div class="form-group" style="margin-bottom: 100px;">
                        <iais:field value="Recurrence" required="true"/>
                        <iais:value width="7">
                          <iais:select name="recurrence" options="recurrenceOption" firstOption="Please Select" value="${inspNonAvailabilityDto.recurrence}" ></iais:select>
                          <br><span class="error-msg" name="iaisErrorMsg" id="error_recurrence"></span>
                        </iais:value>
                      </div>
                      <iais:action >
                        <button class="btn btn-primary" style="float:right" type="button" onclick="javascript:doInspAvailabilityAddNext()">Confirm</button>
                        <a class="back" id="Back" onclick="javascript:doInspAvailabilityAddBack()" style="float:left"><em class="fa fa-angle-left"></em> Back</a>
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
    function doInspAvailabilityAddBack() {
        showWaiting();
        $("[name='nonActionValue']").val('back');
        inspAvailabilityAddSubmit('back');
    }

    function doInspAvailabilityAddNext() {
        showWaiting();
        $("[name='nonActionValue']").val('confirm');
        inspAvailabilityAddSubmit('confirm');
    }
    function inspAvailabilityAddSubmit(action){
        $("[name='inspSupAddAvailabilityType']").val(action);
        var mainPoolForm = document.getElementById('mainAddForm');
        mainPoolForm.submit();
    }
</script>

