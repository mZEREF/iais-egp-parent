<%--
  Created by IntelliJ IDEA.
  User: ShiCheng_Xu
  Date: 2021/11/12
  Time: 16:05
  To change this template use File | Settings | File Templates.
  --%>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
        <h3>Preview and Submit</h3>
        <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
          <div class="panel panel-default">
            <div class="panel-heading">
              <h4 class="panel-title">
                <strong>
                  Intrauterine Insemination Cycle
                </strong>
              </h4>
            </div>
            <div id="patientDetails" class="panel-collapse collapse in">
              <div class="panel-body">
                <div class="panel-main-content form-horizontal">
                  <h4 class="panel-title">
                    <strong>
                      <c:out value="${arSuperDataSubmissionDto.patientInfoDto.patient.name}"></c:out>
                    </strong>
                    &nbsp;<c:out value="(${arSuperDataSubmissionDto.patientInfoDto.patient.idNumber})"></c:out>
                  </h4>
                  <iais:row>
                    <iais:field value="Premises where IUI is Performed" mandatory="false"/>
                    <iais:value width="3" cssClass="col-xs-5 col-md-6 control-label">
                      <span style="font-size: 16px" class="col-xs-6 col-md-6 control-label"><c:out value="${arSuperDataSubmissionDto.appGrpPremisesDto.premiseLabel}"></c:out></span>
                    </iais:value>
                  </iais:row>
                  <iais:row>
                    <iais:field value="Date Started" mandatory="false"/>
                    <iais:value cssClass="col-xs-5 col-md-6 control-label">
                      <span style="font-size: 16px" class="col-xs-6 col-md-6 control-label"><fmt:formatDate value='${arSuperDataSubmissionDto.iuiCycleStageDto.startDate}' pattern='dd/MM/yyyy' /></span>
                    </iais:value>
                  </iais:row>
                  <iais:row>
                    <iais:field value="Patient's Age as of This Treatment" mandatory="false"/>
                    <iais:value cssClass="col-xs-5 col-md-6 control-label">
                      <span style="font-size: 16px" class="col-xs-6 col-md-6 control-label"><c:out value="${arSuperDataSubmissionDto.iuiCycleStageDto.userAgeShow}"></c:out></span>
                    </iais:value>
                  </iais:row>
                  <iais:row>
                    <iais:field value="No. of Children with Current Marriage" mandatory="false"/>
                    <iais:value cssClass="col-xs-5 col-md-6 control-label">
                      <span style="font-size: 16px" class="col-xs-6 col-md-6 control-label"><c:out value="${arSuperDataSubmissionDto.iuiCycleStageDto.curMarrChildNum}"/></span>
                    </iais:value>
                  </iais:row>
                  <iais:row>
                    <iais:field value="No. of Children with Previous Marriage" mandatory="false"/>
                    <iais:value cssClass="col-xs-5 col-md-6 control-label">
                      <span style="font-size: 16px" class="col-xs-6 col-md-6 control-label"><c:out value="${arSuperDataSubmissionDto.iuiCycleStageDto.prevMarrChildNum}"/></span>
                    </iais:value>
                  </iais:row>
                  <iais:row>
                    <iais:field value="Total No. of Children Delivered under IUI" mandatory="false"/>
                    <iais:value cssClass="col-xs-5 col-md-6 control-label">
                      <span style="font-size: 16px" class="col-xs-6 col-md-6 control-label"><c:out value="${arSuperDataSubmissionDto.iuiCycleStageDto.iuiDeliverChildNum}"/></span>
                    </iais:value>
                  </iais:row>
                  <iais:row>
                    <iais:field value="Source of Semen" mandatory="false"/>
                    <iais:value cssClass="col-xs-5 col-md-6 control-label">
                        <span style="font-size: 16px" class="col-xs-6 col-md-6 control-label"><iais:code code="${arSuperDataSubmissionDto.iuiCycleStageDto.semenSource}"/></span>
                    </iais:value>
                  </iais:row>
                  <iais:row>
                    <iais:field value="How many vials of sperm were extracted" mandatory="false"/>
                    <iais:value cssClass="col-xs-5 col-md-6 control-label">
                      <span style="font-size: 16px" class="col-xs-6 col-md-6 control-label"><c:out value="${arSuperDataSubmissionDto.iuiCycleStageDto.extractVialsOfSperm}"/></span>
                    </iais:value>
                  </iais:row>
                  <iais:row>
                    <iais:field value="How many vials of sperm were used in this cycle" mandatory="false"/>
                    <iais:value cssClass="col-xs-5 col-md-6 control-label">
                      <span style="font-size: 16px" class="col-xs-6 col-md-6 control-label"><c:out value="${arSuperDataSubmissionDto.iuiCycleStageDto.usedVialsOfSperm}"/></span>
                    </iais:value>
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