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
<div class="dashboard" style="background-image:url('<%=webroot%>img/Masthead-banner.jpg')">
  <form method="post" id="mainEditForm" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/include/formHidden.jsp" %>
    <br>
    <br>
    <br>
    <br>
    <br>
    <input type="hidden" name="inspSupAddAvailabilityType" value="">
    <input type="hidden" name="actionValue" value="">
    <iais:body >
      <div class="container">
        <div class="col-xs-12">
          <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
            <h3>
              <span>Update Non-Availability Form</span>
            </h3>
            <div class="panel panel-default">
              <div class="panel-collapse collapse in" id="collapseOne" role="tabpanel" aria-labelledby="headingOne" aria-expanded="true" style="">
                <div class="panel-body">
                  <div class="panel-main-content">
                    <iais:section title="" id = "editAvailability">
                      <iais:row>
                        <c:if test="${curRole eq 'INSPECTOR_LEAD'}">
                          <iais:field value="Name" required="true"/>
                          <iais:value width="7">
                            <iais:select name="nonAvaUserName" options="nonAvaUserName" firstOption="Please select" value="${inspNonAvailabilityDto.userName}" ></iais:select>
                            <br><span class="error-msg" name="iaisErrorMsg" id="error_userName"></span>
                          </iais:value>
                        </c:if>
                        <c:if test="${curRole ne 'INSPECTOR_LEAD'}">
                          <iais:field value="Name"/>
                          <iais:value width="7">
                            <label><c:out value="${inspNonAvailabilityDto.userName}"/></label>
                          </iais:value>
                        </c:if>
                      </iais:row>
                      <iais:row>
                        <iais:field value="Blocked Out Date" required="true"/>
                        <iais:value width="7">
                          <label>From <iais:datePicker id = "nonAvaStartDate" name = "nonAvaStartDate" value="${inspNonAvailabilityDto.nonAvaStartDate}"></iais:datePicker> to <iais:datePicker id = "nonAvaEndDate" name = "nonAvaEndDate" value="${inspNonAvailabilityDto.nonAvaEndDate}"></iais:datePicker></label>
                          <br><span class="error-msg" name="iaisErrorMsg" id="error_nonAvaStartDate"></span>
                          <br><span class="error-msg" name="iaisErrorMsg" id="error_nonAvaEndDate"></span>
                        </iais:value>
                      </iais:row>
                      <iais:row>
                        <iais:field value="Blocked Out Date Description"/>
                        <iais:value width="7">
                          <textarea id="blockOutDesc" name="blockOutDesc" cols="70" rows="7" maxlength="255" ><c:out value="${inspNonAvailabilityDto.blockOutDesc}"></c:out></textarea>
                        </iais:value>
                      </iais:row>
                      <iais:row>
                        <iais:field value="Recurrence" required="true"/>
                        <iais:value width="7">
                          <iais:select name="recurrenceOption" options="recurrenceOption" firstOption="Please select" value="${inspNonAvailabilityDto.recurrence}" ></iais:select>
                          <br><span class="error-msg" name="iaisErrorMsg" id="error_recurrence"></span>
                        </iais:value>
                      </iais:row>
                      <iais:action >
                        <button class="btn btn-lg btn-login-back" style="float:left" type="button" onclick="javascript:doInspAvailabilityEditBack()">Back</button>
                        <button class="btn btn-lg btn-login-next" style="float:right" type="button" onclick="javascript:doInspAvailabilityEditNext()">Next</button>
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
<%@ include file="/include/validation.jsp" %>
<script type="text/javascript">
    function doInspAvailabilityEditBack() {
        $("[name='actionValue']").val('back');
        inspAvailabilityEditSubmit('back');
    }

    function doInspAvailabilityEditNext() {
        $("[name='actionValue']").val('confirm');
        inspAvailabilityEditSubmit('confirm');
    }
    function inspAvailabilityEditSubmit(action){
        $("[name='inspSupAddAvailabilityType']").val(action);
        var mainPoolForm = document.getElementById('mainEditForm');
        mainPoolForm.submit();
    }
</script>