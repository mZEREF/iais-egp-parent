<%--
  Created by IntelliJ IDEA.
  User: ShiCheng_Xu
  Date: 2021/11/17
  Time: 9:47
  To change this template use File | Settings | File Templates.
--%>
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
            <c:forEach var="${arSuperDataSubmissionDto.iuiCycleStageDto.semenSources}" items="semenSource">
              <span style="font-size: 16px" class="col-xs-6 col-md-6 control-label"><iais:code code="${semenSource}"/></span><br>
            </c:forEach>
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
