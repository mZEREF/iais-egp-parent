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
    <%@ include file="/include/formHidden.jsp" %>
    <br>
    <br>
    <br>
    <br>
    <br>
    <input type="hidden" name="inspSupAddAvailabilityType" value="">
    <input type="hidden" name="actionValue" value="">
    <input type="hidden" name="lastActionValue" id="lastActionValue" value="<c:out value="${inspNonAvailabilityDto.lastActionValue}"/>">
    <iais:body >
      <div class="container">
        <div class="col-xs-12">
          <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
            <h3>
              <span>Confirm Non-Availability Form</span>
            </h3>
            <div class="panel panel-default">
              <div class="panel-collapse collapse in" id="collapseOne" role="tabpanel" aria-labelledby="headingOne" aria-expanded="true" style="">
                <div class="panel-body">
                  <div class="panel-main-content">
                    <iais:section title="" id = "addAvailability">
                      <iais:row>
                        <iais:field value="Name" required="true"/>
                        <iais:value width="7">
                          <label><c:out value="${inspNonAvailabilityDto.userName}"/></label>
                        </iais:value>
                      </iais:row>
                      <iais:row>
                        <iais:field value="Blocked Out Date" required="true"/>
                        <iais:value width="7">
                          <label>From <fmt:formatDate value='${inspNonAvailabilityDto.nonAvaStartDate}' pattern='dd/MM/yyyy' /> to <fmt:formatDate value='${inspNonAvailabilityDto.nonAvaEndDate}' pattern='dd/MM/yyyy' /></label>
                          <br><span class="error-msg" name="iaisErrorMsg" id="error_nonAvaStartDate"></span>
                          <br><span class="error-msg" name="iaisErrorMsg" id="error_nonAvaEndDate"></span>
                        </iais:value>
                      </iais:row>
                      <iais:row>
                        <iais:field value="Blocked Out Date Description"/>
                        <iais:value width="7">
                          <textarea id="blockOutDesc" name="blockOutDesc" cols="70" rows="7" ><c:out value="${inspNonAvailabilityDto.blockOutDesc}"></c:out></textarea>
                        </iais:value>
                      </iais:row>
                      <iais:row>
                        <iais:field value="Recurrence" required="true"/>
                        <iais:value width="7">
                          <label><c:out value="${inspNonAvailabilityDto.recurrence}"/></label>
                        </iais:value>
                      </iais:row>
                      <iais:row>
                        <iais:field value="Recurrence End Date" required="true"/>
                        <iais:value width="7">
                          <fmt:formatDate value='${inspNonAvailabilityDto.recurrenceEndDate}' pattern='dd/MM/yyyy' />
                        </iais:value>
                      </iais:row>
                      <iais:action >
                        <button class="btn btn-lg btn-login-back" style="float:left" type="button" onclick="javascript:doInspAvailabilityConBack()">Back</button>
                        <button class="btn btn-lg btn-login-next" style="float:right" type="button" onclick="javascript:doInspAvailabilityConSubmit()">Submit</button>
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
    function doInspAvailabilityConBack() {
        var lastActionValue = $("[name='lastActionValue']").val();
        $("[name='actionValue']").val(lastActionValue);
        inspAvailabilityConSubmit(lastActionValue);
    }

    function doInspAvailabilityConSubmit() {
        $("[name='actionValue']").val('submit');
        inspAvailabilityConSubmit('submit');
    }
    function inspAvailabilityConSubmit(action){
        $("[name='inspSupAddAvailabilityType']").val(action);
        var mainPoolForm = document.getElementById('mainConForm');
        mainPoolForm.submit();
    }
</script>
