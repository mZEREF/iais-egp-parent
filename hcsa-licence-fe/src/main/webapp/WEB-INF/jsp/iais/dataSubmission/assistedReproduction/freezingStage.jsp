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
        <%@include file="common/headStepNavTab.jsp" %>
        <h3>Please key in the cycle information below.</h3>
        <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
          <div class="panel panel-default">
            <div class="panel-heading" style="padding-left: 90px;">
              <h4 class="panel-title">
                <strong>
                  Freezing
                </strong>
              </h4>
            </div>
            <div id="patientDetails" class="panel-collapse collapse in">
              <div class="panel-body">
                <div class="panel-main-content form-horizontal">
                  <h3>
                    <label ><c:out value="${arSuperDataSubmissionDto.patientInfoDto.patient.name}"/></label>
                    <span style="font-weight:normal"><c:out value="(${arSuperDataSubmissionDto.patientInfoDto.patient.idNumber})"/>
                    </span>
                  </h3>
                  <iais:row>
                    <iais:field value="What was cryopreserved?" mandatory="true"/>
                    <iais:value width="3" cssClass="col-md-7">
                      <c:forEach items="${arFreeCryoOptions}" var="freeCryo" varStatus="index">
                        <div class="form-check col-md-6" style="padding-left: 0px;">
                          <input class="form-check-input"
                                 <c:if test="${arSuperDataSubmissionDto.arSubFreezingStageDto.cryopreservedType eq freeCryo.value}">checked="checked"</c:if>
                                 type="radio" name="freeCryoRadio" value="<c:out value="${freeCryo.value}"/>"
                                 aria-invalid="false"
                                 id="freeCryopreserved${index.index}"
                          >
                          <label class="form-check-label" for="freeCryopreserved${index.index}">
                            <span class="check-circle"></span><c:out value="${freeCryo.text}"/>
                          </label>
                        </div>
                      </c:forEach>
                      <span class="error-msg" name="iaisErrorMsg" id="error_cryopreservedType"></span>
                    </iais:value>
                  </iais:row>
                  <iais:row>
                    <iais:field value="No. Cryopreserved" mandatory="true"/>
                    <iais:value cssClass="col-md-7">
                      <iais:input maxLength="2" type="text" name="cryopreservedNum"
                                  id="cryopreservedNum"
                                  value="${arSuperDataSubmissionDto.arSubFreezingStageDto.cryopreservedNum}"/>
                    </iais:value>
                  </iais:row>
                  <iais:row>
                    <iais:field value="Cryopreservation Date" mandatory="true"/>
                    <iais:value cssClass="col-md-7">
                      <iais:datePicker id = "cryopreservationDate" name = "cryopreservationDate" dateVal="${arSuperDataSubmissionDto.arSubFreezingStageDto.cryopreservedDate}"/>
                      <span class="error-msg" name="iaisErrorMsg" id="error_cryopreservedDate"></span>
                    </iais:value>
                  </iais:row>
                  <%@include file="section/hasDisposalRow.jsp"%>
                </div>
              </div>
              <%@include file="section/disposalStageDetailSection.jsp" %>
              <%@include file="common/dsAmendment.jsp" %>
            </div>
          </div>
        </div>
        <%@include file="common/arFooter.jsp" %>
      </div>
    </div>
  </div>
</form>