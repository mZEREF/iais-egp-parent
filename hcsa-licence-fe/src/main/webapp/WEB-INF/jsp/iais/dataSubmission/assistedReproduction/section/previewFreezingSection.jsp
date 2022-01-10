<%--
  Created by IntelliJ IDEA.
  User: ShiCheng_Xu
  Date: 2021/11/17
  Time: 9:44
  To change this template use File | Settings | File Templates.
--%>
<div class="panel panel-default">
  <div class="panel-heading ${headingSign}">
    <h4 class="panel-title">
      <a class="collapsed" href="#freezingDetails" data-toggle="collapse">
        Freezing
      </a>
    </h4>
  </div>
  <div id="freezingDetails" class="panel-collapse collapse">
    <div class="panel-body">
      <div class="panel-main-content form-horizontal">

        <h3>
          <label><c:out value="${arSuperDataSubmissionDto.patientInfoDto.patient.name}"/></label>
          <span style="font-weight:normal"><c:out
                  value="(${arSuperDataSubmissionDto.patientInfoDto.patient.idNumber})"/>
          </span>
        </h3>

        <iais:row>
          <iais:field width="6" value="What was cryopreserved?" cssClass="col-md-6"/>
          <iais:value width="6" cssClass="col-xs-5 col-md-6 control-label" display="true">
            <iais:code code="${arSuperDataSubmissionDto.arSubFreezingStageDto.cryopreservedType}"/>
          </iais:value>
        </iais:row>

        <iais:row>
          <iais:field width="6" value="No. Cryopreserved" cssClass="col-md-6"/>
          <iais:value width="6" cssClass="col-xs-5 col-md-6 control-label" display="true">
            <c:out value="${arSuperDataSubmissionDto.arSubFreezingStageDto.cryopreservedNum}"/>
          </iais:value>
        </iais:row>

        <iais:row>
          <iais:field width="6" value="Cryopreservation Date" cssClass="col-md-6"/>
          <iais:value width="6" cssClass="col-xs-5 col-md-6 control-label" display="true">
            <fmt:formatDate value='${arSuperDataSubmissionDto.arSubFreezingStageDto.cryopreservedDate}'
                            pattern='dd/MM/yyyy'/>
          </iais:value>
        </iais:row>

        <%@include file="../common/patientInventoryTable.jsp" %>
      </div>
    </div>
  </div>
</div>
