<c:set var="headingSign" value="completed"/>
<div class="panel panel-default">
    <div class="panel-heading ${headingSign}">
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
        <c:set var="patientDto" value="${arSuperDataSubmissionDto.patientInfoDto.patient}" />
        <h3>
          <p><label style="font-family:'Arial Negreta', 'Arial Normal', 'Arial';font-weight:700;"><c:out value="${patientDto.name}"/>&nbsp</label><label style="font-family:'Arial Normal', 'Arial';font-weight:400;">${empty patientDto.idNumber ? "" : "("}<c:out value="${patientDto.idNumber}"/>${empty patientDto.idNumber ? "" : ")"} </label></p>
        </h3>
        <iais:row>
          <iais:field value="Premises where IUI is Performed" mandatory="false"/>
          <iais:value width="3" cssClass="col-xs-5 col-md-6 control-label">
            <span style="font-size: 16px" class="col-xs-6 col-md-6 control-label"><c:out value="${arSuperDataSubmissionDto.premisesDto.premiseLabel}"></c:out></span>
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
            <span style="font-size: 16px" class="col-xs-6 col-md-6 control-label"><c:out value="${iuiCycleStageDto.userAgeShow}"></c:out></span>
          </iais:value>
        </iais:row>
        <iais:row>
          <iais:field value="No. of Children with Current Marriage" mandatory="false"/>
          <iais:value cssClass="col-xs-5 col-md-6 control-label">
            <span style="font-size: 16px" class="col-xs-6 col-md-6 control-label"><c:out value="${iuiCycleStageDto.curMarrChildNum}"/></span>
          </iais:value>
        </iais:row>
        <iais:row>
          <iais:field value="No. of Children with Previous Marriage" mandatory="false"/>
          <iais:value cssClass="col-xs-5 col-md-6 control-label">
            <span style="font-size: 16px" class="col-xs-6 col-md-6 control-label"><c:out value="${iuiCycleStageDto.prevMarrChildNum}"/></span>
          </iais:value>
        </iais:row>
        <iais:row>
          <iais:field value="Total No. of Children Delivered under IUI" mandatory="false"/>
          <iais:value cssClass="col-xs-5 col-md-6 control-label">
            <span style="font-size: 16px" class="col-xs-6 col-md-6 control-label"><c:out value="${iuiCycleStageDto.iuiDeliverChildNum}"/></span>
          </iais:value>
        </iais:row>
        <iais:row>
          <iais:field value="Source of Semen" mandatory="false"/>
          <iais:value cssClass="col-xs-5 col-md-6 control-label">
            <c:forEach var="semenSource" items="${iuiCycleStageDto.semenSources}">
              <span style="font-size: 16px" class="col-xs-6 col-md-6 control-label"><iais:code code="${semenSource}"/></span>
              <br>
              <br>
            </c:forEach>
          </iais:value>
        </iais:row>
        <iais:row>
          <iais:field value="How many vials of sperm were extracted" mandatory="false"/>
          <iais:value cssClass="col-xs-5 col-md-6 control-label">
            <span style="font-size: 16px" class="col-xs-6 col-md-6 control-label"><c:out value="${iuiCycleStageDto.extractVialsOfSperm}"/></span>
          </iais:value>
        </iais:row>
        <iais:row>
          <iais:field value="How many vials of sperm were used in this cycle" mandatory="false"/>
          <iais:value cssClass="col-xs-5 col-md-6 control-label">
            <span style="font-size: 16px" class="col-xs-6 col-md-6 control-label"><c:out value="${iuiCycleStageDto.usedVialsOfSperm}"/></span>
          </iais:value>
        </iais:row>
      </div>
    </div>
  </div>
</div>
<c:set var="donorDtos" value="${iuiCycleStageDto.donorDtos}"/>
<%@include file="previewDonorSection.jsp"%>
