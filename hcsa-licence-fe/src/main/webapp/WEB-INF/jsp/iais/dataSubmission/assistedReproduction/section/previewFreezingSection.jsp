<%--
  Created by IntelliJ IDEA.
  User: ShiCheng_Xu
  Date: 2021/11/17
  Time: 9:44
  To change this template use File | Settings | File Templates.
--%>
<c:set var="headingSign" value="completed"/>
<div class="panel panel-default">
  <div class="panel-heading ${headingSign}">
    <h4 class="panel-title">
      <a href="#freezingDetails" data-toggle="collapse">
        Freezing
      </a>
    </h4>
  </div>
  <div id="freezingDetails" class="panel-collapse collapse in">
    <div class="panel-body">
      <div class="panel-main-content form-horizontal">
        <h3>
          <label ><c:out value="${arSuperDataSubmissionDto.patientInfoDto.patient.name}"/></label>
          <span style="font-weight:normal"><c:out value="(${arSuperDataSubmissionDto.patientInfoDto.patient.idNumber})"/>
          </span>
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
