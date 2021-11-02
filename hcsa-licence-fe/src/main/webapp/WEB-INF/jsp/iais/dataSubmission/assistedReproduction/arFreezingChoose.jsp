<%--
  Created by IntelliJ IDEA.
  User: ShiCheng_Xu
  Date: 2021/10/29
  Time: 15:06
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%
  //handle to the Engine APIs
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
  String continueURL = "";
  if (process != null && process.runtime != null && process.runtime.getBaseProcessClass() != null) {
    continueURL = process.runtime.continueURL();
  }
%>
<webui:setLayout name="iais-internet"/>

<%@ include file="common/arHeader.jsp" %>

<script type="text/javascript" src="<%=webroot1%>js/dataSubmission/patientInformation.js"></script>

<form method="post" id="mainForm" action=<%=continueURL%>>
  <div class="main-content">
    <div class="container center-content">
      <div class="col-xs-12">
        <h3>Please key in the cycle information below.</h3>
        <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
          <div class="panel panel-default">
            <div class="panel-heading">
              <h4 class="panel-title">
                <strong>
                  Freezing
                </strong>
              </h4>
            </div>
            <div id="patientDetails" class="panel-collapse collapse in">
              <div class="panel-body">
                <div class="panel-main-content form-horizontal">
                  <iais:row>
                    <iais:field value="What was cryopreserved?" mandatory="true"/>
                    <iais:value cssClass="col-md-3">
                      <c:forEach items="${freeCryoOptions}" var="freeCryo">
                        <input class="form-check-input" <c:if test="${arSuperDataSubmissionDto.arSubFreezingStageDto.cryopreservedType eq freeCryo.value}">checked="checked"</c:if>
                               type="radio" name="freeCryoRadio" value = "<c:out value="${freeCryo.value}"/>" aria-invalid="false">
                        <label class="form-check-label" ><span class="check-circle"></span><c:out value="${freeCryo.text}"/></label>
                        <br>
                      </c:forEach>
                    </iais:value>
                    <span class="error-msg" name="iaisErrorMsg" id="error_cryopreservedType"></span>
                  </iais:row>
                  <iais:row>
                    <iais:field value="No. Cryopreserved" mandatory="true"/>
                    <iais:value cssClass="col-md-3">
                      <input type="number" maxlength="2" style="margin-bottom: 0px;" name="cryopreservedNum" value="${arSuperDataSubmissionDto.arSubFreezingStageDto.cryopreservedNum}"/>
                      <br>
                    </iais:value>
                    <span class="error-msg" name="iaisErrorMsg" id="error_cryopreservedNum"></span>
                  </iais:row>
                  <iais:row>
                    <iais:field value="Cryopreservation Date" mandatory="true"/>
                    <iais:value cssClass="col-md-3">
                      <iais:datePicker id = "cryopreservationDate" name = "cryopreservationDate" dateVal="${arSuperDataSubmissionDto.arSubFreezingStageDto.cryopreservedDate}"></iais:datePicker>
                    </iais:value>
                    <span class="error-msg" name="iaisErrorMsg" id="error_cryopreservedDate"></span>
                  </iais:row>
                </div>
              </div>
            </div>
          </div>
          <%@include file="common/arDeclaration.jsp" %>
        </div>
        <%@include file="common/arFooter.jsp" %>
      </div>
    </div>
  </div>
</form>
<%@ include file="/WEB-INF/jsp/include/validation.jsp" %>
