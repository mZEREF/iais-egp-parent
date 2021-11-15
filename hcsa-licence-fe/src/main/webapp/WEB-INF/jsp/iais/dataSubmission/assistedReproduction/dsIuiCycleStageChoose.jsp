<%--
  Created by IntelliJ IDEA.
  User: ShiCheng_Xu
  Date: 2021/11/12
  Time: 16:03
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
                  Intrauterine Insemination Cycle
                </strong>
              </h4>
            </div>
            <div id="patientDetails" class="panel-collapse collapse in">
              <div class="panel-body">
                <div class="panel-main-content form-horizontal">
                  <h4 class="panel-title">
                    <strong>
                      Intrauterine Insemination Cycle
                    </strong>
                    <c:out value=""></c:out>
                  </h4>
                  <iais:row>
                    <iais:field value="Premises where IUI is Performed" mandatory="false"/>
                    <iais:value width="3" cssClass="col-md-7">
                      <c:out value="${arSuperDataSubmissionDto.appGrpPremisesDto.premiseLabel}"></c:out>
                    </iais:value>
                  </iais:row>
                  <iais:row>
                    <iais:field value="Date Started" mandatory="true"/>
                    <iais:value cssClass="col-md-3">
                      <iais:datePicker id = "iuiCycleStartDate" name = "iuiCycleStartDate" dateVal="${arSuperDataSubmissionDto.iuiCycleStageDto.startDate}"></iais:datePicker>
                      <br><span class="error-msg" name="iaisErrorMsg" id="error_startDate"></span>
                    </iais:value>
                  </iais:row>
                  <iais:row>
                    <iais:field value="Patient's Age as of This Treatment" mandatory="false"/>
                    <iais:value cssClass="col-md-3">
                      <c:out value=""></c:out>
                    </iais:value>
                  </iais:row>
                  <iais:row>
                    <iais:field value="No. of Children with Current Marriage" mandatory="true"/>
                    <iais:value cssClass="col-md-3">
                      <iais:select name="curMarrChildNum" options="" value="${arSuperDataSubmissionDto.iuiCycleStageDto.curMarrChildNum}"></iais:select>
                    </iais:value>
                  </iais:row>
                  <iais:row>
                    <iais:field value="No. of Children with Previous Marriage" mandatory="true"/>
                    <iais:value cssClass="col-md-3">
                      <iais:select name="prevMarrChildNum" options="" value="${arSuperDataSubmissionDto.iuiCycleStageDto.prevMarrChildNum}"></iais:select>
                    </iais:value>
                  </iais:row>
                  <iais:row>
                    <iais:field value="Total No. of Children Delivered under IUI" mandatory="true"/>
                    <iais:value cssClass="col-md-3">
                      <input type="number" oninput="if(value.length>2)value=value.slice(0,2)" style="margin-bottom: 0px;" name="iuiDeliverChildNum" value="${arSuperDataSubmissionDto.iuiCycleStageDto.iuiDeliverChildNum}"/>
                    </iais:value>
                  </iais:row>
                  <iais:row>
                    <iais:field value="Source of Semen" mandatory="true"/>
                    <iais:value cssClass="col-md-3">
                      <c:forEach var="${sourceOfSemenOption}" items="sourceOfSemen" varStatus="index">
                        <input class="form-check-input" <c:if test="${arSuperDataSubmissionDto.iuiCycleStageDto.semenSource eq sourceOfSemen.value}">checked="checked"</c:if>
                               type="radio" name="sourceOfSemenOp" value = "<c:out value="${sourceOfSemen.value}"/>" aria-invalid="false"
                               id="sourceOfSemenOp${index.index}"
                        >
                        <label class="form-check-label" for="sourceOfSemenOp${index.index}">
                          <span class="check-circle"></span><c:out value="${sourceOfSemen.text}"/>
                        </label>
                      </c:forEach>
                      <span class="error-msg" name="iaisErrorMsg" id="error_semenSource"></span>
                    </iais:value>
                  </iais:row>
                  <iais:row>
                    <iais:field value="How many vials of sperm were extracted" mandatory="true"/>
                    <iais:value cssClass="col-md-3">
                      <input type="number" oninput="if(value.length>2)value=value.slice(0,2)" style="margin-bottom: 0px;" name="extractVialsOfSperm" value="${arSuperDataSubmissionDto.iuiCycleStageDto.extractVialsOfSperm}"/>
                    </iais:value>
                  </iais:row>
                  <iais:row>
                    <iais:field value="How many vials of sperm were used in this cycle" mandatory="true"/>
                    <iais:value cssClass="col-md-3">
                      <input type="number" oninput="if(value.length>2)value=value.slice(0,2)" style="margin-bottom: 0px;" name="usedVialsOfSperm" value="${arSuperDataSubmissionDto.iuiCycleStageDto.usedVialsOfSperm}"/>
                    </iais:value>
                  </iais:row>
                </div>
              </div>
            </div>
          </div>
        </div>
        <%@include file="common/arFooter.jsp" %>
      </div>
    </div>
  </div>
</form>