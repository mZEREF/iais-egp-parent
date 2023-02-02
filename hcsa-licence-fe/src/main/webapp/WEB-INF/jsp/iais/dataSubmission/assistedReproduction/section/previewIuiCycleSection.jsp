<c:set var="headingSign" value="${currentPageStage eq 'preview' ?'completed':''}"/>
<div class="panel panel-default">
    <div class="panel-heading ${headingSign}">
      <h4 class="panel-title" >
        <a class="collapsed" href="#viewIuiCycleStage" data-toggle="collapse" >
          Intrauterine Insemination Cycle
        </a>
      </h4>
    </div>
  <div id="viewIuiCycleStage" class="panel-collapse collapse">
    <div class="panel-body">
      <div class="panel-main-content form-horizontal">
        <c:set var="iuiCycleStageDto" value="${arSuperDataSubmissionDto.iuiCycleStageDto}" />
        <c:set var="donorDtos" value="${iuiCycleStageDto.donorDtos}"/>
        <%@include file="patientCommon.jsp"%>
        <iais:row>
          <iais:field width="5" value="Is the IUI Performed in the current Institution" mandatory="false"/>
          <iais:value width="7" cssClass="col-md-7" display="true" >
         <c:out value="${iuiCycleStageDto.ownPremises ? 'Yes' : 'No'}"></c:out>
          </iais:value>
        </iais:row>
        <c:if test="${iuiCycleStageDto.ownPremises}">
          <iais:row>
            <iais:field width="5" value="IUI Treatment Performed in Own Premise" mandatory="false"/>
            <iais:value width="7" cssClass="col-md-7" display="true" >
              <iais:optionText value="${arSuperDataSubmissionDto.premisesDto.premiseLabel}"/>
            </iais:value>
          </iais:row>
        </c:if>
        <c:if test="${!iuiCycleStageDto.ownPremises}">
        <iais:row>
          <iais:field width="5" value="Name of Premise Where IUI Treatment Is Performed" mandatory="false"/>
          <iais:value width="7" cssClass="col-md-7" display="true" >
            <c:out value="${iuiCycleStageDto.otherPremises}"/>
          </iais:value>
        </iais:row>
        </c:if>
        <iais:row>
          <iais:field width="5" value="Date Started" info="${MessageUtil.getMessageDesc('DS_MSG038')}" mandatory="false"/>
          <iais:value  width="7" cssClass="col-md-7" display="true" >
          <fmt:formatDate value='${arSuperDataSubmissionDto.iuiCycleStageDto.startDate}' pattern='dd/MM/yyyy' />
          </iais:value>
        </iais:row>
        <iais:row>
          <iais:field width="5" value="Patient's Age as of This Treatment" />
          <iais:value  width="7" cssClass="col-md-7" display="true">
           <c:out value="${iuiCycleStageDto.userAgeShow}"></c:out>
          </iais:value>
        </iais:row>
        <iais:row>
          <iais:field width="5" value="No. of Children from Current Marriage" mandatory="false"/>
          <iais:value width="7" cssClass="col-md-7"  display="true">
           <c:out value="${iuiCycleStageDto.curMarrChildNum}"/>
          </iais:value>
        </iais:row>
        <iais:row>
          <iais:field width="5" value="No. of Children from Previous Marriage" mandatory="false"/>
          <iais:value width="7" cssClass="col-md-7"  display="true">
         <c:out value="${iuiCycleStageDto.prevMarrChildNum}"/>
          </iais:value>
        </iais:row>
        <iais:row>
          <label class="col-xs-4 col-md-4 control-label">No. of Children Delivered under IUI
            <a class="btn-tooltip styleguide-tooltip" data-toggle="tooltip" data-html="true" href="javascript:void(0);"
               title="${DSACK003Message}"
               style="z-index: 10"
               data-original-title="">i</a>
          </label>
          <iais:value width="7" cssClass="col-md-7"  display="true">
           <c:out value="${iuiCycleStageDto.iuiDeliverChildNum}"/>
          </iais:value>
        </iais:row>
        <iais:row>
          <iais:field  width="5" value="Source of Semen" mandatory="false"/>
          <iais:value width="7" cssClass="col-md-7"  display="true">
            <c:forEach items="${iuiCycleStageDto.semenSources}" var="semenSources" varStatus="status">
              <c:if test="${status.index != 0}"><br></c:if> <iais:code code="${semenSources}"/>
            </c:forEach>
          </iais:value>
        </iais:row>
        <iais:row>
          <iais:field  width="5" value="How many vials of sperm were extracted?" mandatory="false"/>
          <iais:value width="7" cssClass="col-md-7"  display="true">
          <c:out value="${iuiCycleStageDto.extractVialsOfSperm}"/>
          </iais:value>
        </iais:row>
        <iais:row>
          <iais:field  width="5" value="How many vials of sperm were used in this cycle?" mandatory="false"/>
          <iais:value width="7" cssClass="col-md-7"  display="true" >
          <c:out value="${iuiCycleStageDto.usedVialsOfSperm}"/>
          </iais:value>
        </iais:row>
      </div>
      <c:if test="${empty donorDtos}">
        <%@include file="../common/patientInventoryTable.jsp" %>
      </c:if>
    </div>
  </div>
</div>
<c:set var="donorFrom" value="iui"/>
<%@include file="previewDonorSection.jsp"%>
