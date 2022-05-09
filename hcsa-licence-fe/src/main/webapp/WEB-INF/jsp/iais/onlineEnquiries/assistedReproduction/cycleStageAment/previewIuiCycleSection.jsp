<div class="panel panel-default">
    <div class="panel-heading ">
      <h4 class="panel-title" >
        <a href="#viewIuiCycleStage" data-toggle="collapse" >
          Intrauterine Insemination Cycle
        </a>
      </h4>
    </div>
  <div id="viewIuiCycleStage" class="panel-collapse collapse in">
    <div class="panel-body">
      <div class="panel-main-content form-horizontal">
        <c:set var="iuiCycleStageDto" value="${arSuperDataSubmissionDto.iuiCycleStageDto}" />
        <c:set var="iuiCycleStageDtoVersion" value="${arSuperDataSubmissionDtoVersion.iuiCycleStageDto}" />
        <c:set var="patientDto" value="${arSuperDataSubmissionDto.patientInfoDto.patient}" />
        <%@include file="comPart.jsp" %>
        <iais:row>
          <iais:field value="Premises where IUI is Performed" mandatory="false"/>
          <iais:value width="4" cssClass="col-md-4" display="true" >
            <c:out value="${arSuperDataSubmissionDto.premisesDto.premiseLabel}"></c:out>
          </iais:value>
          <iais:value width="4" cssClass="col-md-4" display="true" >
            <c:out value="${arSuperDataSubmissionDto.premisesDto.premiseLabel}"></c:out>
          </iais:value>
        </iais:row>
        <iais:row>
          <iais:field value="Date Started" mandatory="false"/>
          <iais:value  width="4" cssClass="col-md-4" display="true" >
            <fmt:formatDate value='${iuiCycleStageDto.startDate}' pattern='dd/MM/yyyy' />
          </iais:value>
          <iais:value  width="4" cssClass="col-md-4" display="true" >
            <fmt:formatDate value='${iuiCycleStageDtoVersion.startDate}' pattern='dd/MM/yyyy' />
          </iais:value>
        </iais:row>
        <iais:row>
          <iais:field value="Patient's Age as of This Treatment" />
          <iais:value  width="4" cssClass="col-md-4" display="true">
            <c:out value="${iuiCycleStageDto.userAgeShow}"/>
          </iais:value>
          <iais:value  width="4" cssClass="col-md-4" display="true">
            <c:out value="${iuiCycleStageDtoVersion.userAgeShow}"/>
          </iais:value>
        </iais:row>
        <iais:row>
          <iais:field value="No. of Children from Current Marriage" mandatory="false"/>
          <iais:value width="4" cssClass="col-md-4"  display="true">
            <c:out value="${iuiCycleStageDto.curMarrChildNum}"/>
          </iais:value>
          <iais:value width="4" cssClass="col-md-4"  display="true">
            <c:out value="${iuiCycleStageDtoVersion.curMarrChildNum}"/>
          </iais:value>
        </iais:row>
        <iais:row>
          <iais:field value="No. of Children from Previous Marriage" mandatory="false"/>
          <iais:value width="4" cssClass="col-md-4"  display="true">
            <c:out value="${iuiCycleStageDto.prevMarrChildNum}"/>
          </iais:value>
          <iais:value width="4" cssClass="col-md-4"  display="true">
            <c:out value="${iuiCycleStageDtoVersion.prevMarrChildNum}"/>
          </iais:value>
        </iais:row>
        <iais:row>
          <iais:field value="Total No. of Children Delivered under IUI" mandatory="false"/>
          <iais:value width="4" cssClass="col-md-4"  display="true">
            <c:out value="${iuiCycleStageDto.iuiDeliverChildNum}"/>
          </iais:value>
          <iais:value width="4" cssClass="col-md-4"  display="true">
            <c:out value="${iuiCycleStageDtoVersion.iuiDeliverChildNum}"/>
          </iais:value>
        </iais:row>
        <iais:row>
          <iais:field value="Source of Semen" mandatory="false"/>
          <iais:value width="4" cssClass="col-md-4"  display="true">
            <c:forEach items="${iuiCycleStageDto.semenSources}" var="semenSources" varStatus="status">
              <c:if test="${status.index != 0}"><br></c:if> <iais:code code="${semenSources}"/>
            </c:forEach>
          </iais:value>
          <iais:value width="4" cssClass="col-md-4"  display="true">
            <c:forEach items="${iuiCycleStageDtoVersion.semenSources}" var="semenSources" varStatus="status">
              <c:if test="${status.index != 0}"><br></c:if> <iais:code code="${semenSources}"/>
            </c:forEach>
          </iais:value>
        </iais:row>
        <iais:row>
          <iais:field value="How many vials of sperm were extracted" mandatory="false"/>
          <iais:value width="4" cssClass="col-md-4"  display="true">
            <c:out value="${iuiCycleStageDto.extractVialsOfSperm}"/>
          </iais:value>
          <iais:value width="4" cssClass="col-md-4"  display="true">
            <c:out value="${iuiCycleStageDtoVersion.extractVialsOfSperm}"/>
          </iais:value>
        </iais:row>
        <iais:row>
          <iais:field value="How many vials of sperm were used in this cycle" mandatory="false"/>
          <iais:value width="4" cssClass="col-md-4"  display="true" >
            <c:out value="${iuiCycleStageDto.usedVialsOfSperm}"/>
          </iais:value>
          <iais:value width="4" cssClass="col-md-4"  display="true" >
            <c:out value="${iuiCycleStageDtoVersion.usedVialsOfSperm}"/>
          </iais:value>
        </iais:row>
      </div>
    </div>
  </div>
</div>
<c:set var="donorDtos" value="${iuiCycleStageDto.donorDtos}"/>
<%@include file="../cycleStage/previewDonorSection.jsp"%>
<c:set var="donorDtosVersion" value="${iuiCycleStageDtoVersion.donorDtos}"/>
<%@include file="previewDonorSection.jsp"%>
