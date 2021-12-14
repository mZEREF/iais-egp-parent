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
          <label><c:out value="${patientDto.name}"/></label><span style="font-weight:normal;">${empty patientDto.idNumber ? "" : "("}<c:out value="${patientDto.idNumber}"/>${empty patientDto.idNumber ? "" : ")"} </span>
        </h3>
        <iais:row>
          <iais:field value="Premises where IUI is Performed" mandatory="false"/>
          <iais:value width="7" cssClass="col-md-7" display="true" >
         <c:out value="${iuiCycleStageDto.ownPremises ? 'Own premises' : 'Others'}"></c:out>
          </iais:value>
        </iais:row>
        <c:if test="${!iuiCycleStageDto.ownPremises}">
        <iais:row>
          <iais:field value="IUI Treatment performed in Other Premises" mandatory="false"/>
          <iais:value width="7" cssClass="col-md-7" display="true" >
            <c:out value="${iuiCycleStageDto.otherPremises}"/>
          </iais:value>
        </iais:row>
        </c:if>
        <iais:row>
          <iais:field value="Date Started" mandatory="false"/>
          <iais:value  width="7" cssClass="col-md-7" display="true" >
          <fmt:formatDate value='${arSuperDataSubmissionDto.iuiCycleStageDto.startDate}' pattern='dd/MM/yyyy' />
          </iais:value>
        </iais:row>
        <iais:row>
          <iais:field value="Patient's Age as of This Treatment" />
          <iais:value  width="7" cssClass="col-md-7" display="true">
           <c:out value="${iuiCycleStageDto.userAgeShow}"></c:out>
          </iais:value>
        </iais:row>
        <iais:row>
          <iais:field value="No. of Children with Current Marriage" mandatory="false"/>
          <iais:value width="7" cssClass="col-md-7"  display="true">
           <c:out value="${iuiCycleStageDto.curMarrChildNum}"/>
          </iais:value>
        </iais:row>
        <iais:row>
          <iais:field value="No. of Children with Previous Marriage" mandatory="false"/>
          <iais:value width="7" cssClass="col-md-7"  display="true">
         <c:out value="${iuiCycleStageDto.prevMarrChildNum}"/>
          </iais:value>
        </iais:row>
        <iais:row>
          <iais:field value="Total No. of Children Delivered under IUI" mandatory="false"/>
          <iais:value width="7" cssClass="col-md-7"  display="true">
           <c:out value="${iuiCycleStageDto.iuiDeliverChildNum}"/>
          </iais:value>
        </iais:row>
        <iais:row>
          <iais:field value="Source of Semen" mandatory="false"/>
          <iais:value width="7" cssClass="col-md-7"  display="true">
            <c:forEach items="${iuiCycleStageDto.semenSources}" var="semenSources" varStatus="status">
              <c:if test="${status.index != 0}"><br></c:if> <iais:code code="${semenSources}"/>
            </c:forEach>
          </iais:value>
        </iais:row>
        <iais:row>
          <iais:field value="How many vials of sperm were extracted" mandatory="false"/>
          <iais:value width="7" cssClass="col-md-7"  display="true">
          <c:out value="${iuiCycleStageDto.extractVialsOfSperm}"/>
          </iais:value>
        </iais:row>
        <iais:row>
          <iais:field value="How many vials of sperm were used in this cycle" mandatory="false"/>
          <iais:value width="7" cssClass="col-md-7"  display="true" >
          <c:out value="${iuiCycleStageDto.usedVialsOfSperm}"/>
          </iais:value>
        </iais:row>
      </div>
    </div>
  </div>
</div>
<c:set var="donorDtos" value="${iuiCycleStageDto.donorDtos}"/>
<%@include file="previewDonorSection.jsp"%>
