<%--
  Created by IntelliJ IDEA.
  User: ShiCheng_Xu
  Date: 2021/11/17
  Time: 9:44
  To change this template use File | Settings | File Templates.
--%>
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
        <c:set var="patientDto" value="${arSuperDataSubmissionDto.patientInfoDto.patient}" />
        <h3>
          <p><label style="font-family:'Arial Negreta', 'Arial Normal', 'Arial';font-weight:700;"><c:out value="${patientDto.name}"/>&nbsp</label><label style="font-family:'Arial Normal', 'Arial';font-weight:400;">${empty patientDto.idNumber ? "" : "("}<c:out value="${patientDto.idNumber}"/>${empty patientDto.idNumber ? "" : ")"} </label></p>
        </h3>
        <iais:row>
          <iais:field value="What was cryopreserved?"/>
          <iais:value cssClass="col-xs-5 col-md-6 control-label">
            <span style="font-size: 16px" class="col-xs-6 col-md-6 control-label"><iais:code code="${arSuperDataSubmissionDto.arSubFreezingStageDto.cryopreservedType}"/></span>
          </iais:value>
        </iais:row>
        <iais:row>
          <iais:field value="No. Cryopreserved"/>
          <iais:value cssClass="col-xs-5 col-md-6 control-label">
            <span style="font-size: 16px" class="col-xs-6 col-md-6 control-label"><c:out value="${arSuperDataSubmissionDto.arSubFreezingStageDto.cryopreservedNum}"/></span>
          </iais:value>
        </iais:row>
        <iais:row>
          <iais:field value="Cryopreservation Date"/>
          <iais:value cssClass="col-xs-5 col-md-6 control-label">
            <span style="font-size: 16px" class="col-xs-6 col-md-6 control-label"><fmt:formatDate value='${arSuperDataSubmissionDto.arSubFreezingStageDto.cryopreservedDate}' pattern='dd/MM/yyyy' /></span>
          </iais:value>
        </iais:row>
        <%@include file="../common/patientInventoryTable.jsp" %>
      </div>
    </div>
  </div>
</div>
