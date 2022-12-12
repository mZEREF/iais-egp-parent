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
<script type="text/javascript" src="<%=webroot1%>js/dataSubmission/freezing.js"></script>

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
                      <div class="form-check col-xs-6" style="padding: 0;">
                        <input class="form-check-input" type="checkbox"
                               name="isFreshOocyte"
                               value="1"
                               id="isFreshOocyte"
                               <c:if test="${arSuperDataSubmissionDto.arSubFreezingStageDto.isFreshOocyte eq '1'}">checked</c:if>
                               aria-invalid="false">
                        <label class="form-check-label"
                               for="isFreshOocyte"><span
                                class="check-square"></span>Fresh Oocyte(s)</label>
                      </div>
                      <div class="form-check col-xs-6" style="padding: 0;">
                        <input class="form-check-input" type="checkbox"
                               name="isThawedOocyte"
                               value="1"
                               id="isThawedOocyte"
                               <c:if test="${arSuperDataSubmissionDto.arSubFreezingStageDto.isThawedOocyte eq '1'}">checked</c:if>
                               aria-invalid="false">
                        <label class="form-check-label"
                               for="isThawedOocyte"><span
                                class="check-square"></span>Thawed Oocyte(s)</label>
                      </div>
                      <div class="form-check col-xs-6" style="padding: 0;">
                        <input class="form-check-input" type="checkbox"
                               name="isFreshEmbryo"
                               value="1"
                               id="isFreshEmbryo"
                               <c:if test="${arSuperDataSubmissionDto.arSubFreezingStageDto.isFreshEmbryo eq '1'}">checked</c:if>
                               aria-invalid="false">
                        <label class="form-check-label"
                               for="isFreshEmbryo"><span
                                class="check-square"></span>Fresh Embryo(s)</label>
                      </div>
                      <div class="form-check col-xs-6" style="padding: 0;">
                        <input class="form-check-input" type="checkbox"
                               name="isThawedEmbryo"
                               value="1"
                               id="isThawedEmbryo"
                               <c:if test="${arSuperDataSubmissionDto.arSubFreezingStageDto.isThawedEmbryo eq '1'}">checked</c:if>
                               aria-invalid="false">
                        <label class="form-check-label"
                               for="isThawedEmbryo"><span
                                class="check-square"></span>Thawed Embryo(s)</label>
                      </div>
                      <span class="error-msg" name="iaisErrorMsg" id="error_cryopreservedType"></span>
                    </iais:value>
                  </iais:row>
                  <div id="freshOocyte" <c:if test="${arSuperDataSubmissionDto.arSubFreezingStageDto.isFreshOocyte ne 1}">style="display: none" </c:if>>
                    <iais:row>
                      <iais:field value="No. Cryopreserved (Fresh Oocyte(s))" mandatory="true"/>
                      <iais:value cssClass="col-md-7">
                        <iais:input maxLength="2" type="text" name="freshOocyteCryopNum"
                                    id="freshOocyteCryopNum"
                                    value="${arSuperDataSubmissionDto.arSubFreezingStageDto.freshOocyteCryopNum}"/>
                        <span class="error-msg" name="iaisErrorMsg" id="error_freshOocyteCryopNum"></span>
                      </iais:value>
                    </iais:row>
                  </div>
                  <div id="thawedOocyte" <c:if test="${arSuperDataSubmissionDto.arSubFreezingStageDto.isThawedOocyte ne 1}">style="display: none" </c:if>>
                    <iais:row>
                      <iais:field value="No. Cryopreserved (Thawed Oocytes(s))" mandatory="true"/>
                      <iais:value cssClass="col-md-7">
                        <iais:input maxLength="2" type="text" name="thawedOocyteCryopNum"
                                    id="thawedOocyteCryopNum"
                                    value="${arSuperDataSubmissionDto.arSubFreezingStageDto.thawedOocyteCryopNum}"/>
                        <span class="error-msg" name="iaisErrorMsg" id="error_thawedOocyteCryopNum"></span>
                      </iais:value>
                    </iais:row>
                  </div>
                  <div id="freshEmbryo" <c:if test="${arSuperDataSubmissionDto.arSubFreezingStageDto.isFreshEmbryo ne 1}">style="display: none" </c:if>>
                    <iais:row>
                      <iais:field value="No. Cryopreserved (Fresh Embryo(s))" mandatory="true"/>
                      <iais:value cssClass="col-md-7">
                        <iais:input maxLength="2" type="text" name="freshEmbryoCryopNum"
                                    id="freshEmbryoCryopNum"
                                    value="${arSuperDataSubmissionDto.arSubFreezingStageDto.freshEmbryoCryopNum}"/>
                        <span class="error-msg" name="iaisErrorMsg" id="error_freshEmbryoCryopNum"></span>
                      </iais:value>
                    </iais:row>
                  </div>
                  <div id="thawedEmbryo" <c:if test="${arSuperDataSubmissionDto.arSubFreezingStageDto.isThawedEmbryo ne 1}">style="display: none" </c:if>>
                    <iais:row>
                      <iais:field value="No. Cryopreserved (Thawed Embryo(s))" mandatory="true"/>
                      <iais:value cssClass="col-md-7">
                        <iais:input maxLength="2" type="text" name="thawedEmbryoCryopNum"
                                    id="thawedEmbryoCryopNum"
                                    value="${arSuperDataSubmissionDto.arSubFreezingStageDto.thawedEmbryoCryopNum}"/>
                        <span class="error-msg" name="iaisErrorMsg" id="error_thawedEmbryoCryopNum"></span>
                      </iais:value>
                    </iais:row>
                  </div>
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