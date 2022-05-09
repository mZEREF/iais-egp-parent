<%--
  Created by IntelliJ IDEA.
  User: ShiCheng_Xu
  Date: 2021/11/17
  Time: 9:44
  To change this template use File | Settings | File Templates.
--%>

<div class="panel panel-default">
  <div class="panel-heading ">
    <h4 class="panel-title">
      <a href="#freezingDetails" data-toggle="collapse">
        Freezing
      </a>
    </h4>
  </div>
  <div id="freezingDetails" class="panel-collapse collapse in">
    <div class="panel-body">
      <div class="panel-main-content form-horizontal">

        <%@include file="comPart.jsp" %>
        <iais:row>
          <iais:field width="4" value="What was cryopreserved?" cssClass="col-md-4"/>
          <iais:value width="4" cssClass="col-xs-4 col-md-4 control-label" display="true">
            <iais:code code="${arSuperDataSubmissionDto.arSubFreezingStageDto.cryopreservedType}"/>
          </iais:value>
          <iais:value width="4" cssClass="col-xs-4 col-md-4 control-label" display="true">
            <iais:code code="${arSuperDataSubmissionDtoVersion.arSubFreezingStageDto.cryopreservedType}"/>
          </iais:value>
        </iais:row>

        <iais:row>
          <iais:field width="4" value="No. Cryopreserved" cssClass="col-md-4"/>
          <iais:value width="4" cssClass="col-xs-4 col-md-4 control-label" display="true">
            <c:out value="${arSuperDataSubmissionDto.arSubFreezingStageDto.cryopreservedNum}"/>
          </iais:value>
          <iais:value width="4" cssClass="col-xs-4 col-md-4 control-label" display="true">
            <c:out value="${arSuperDataSubmissionDtoVersion.arSubFreezingStageDto.cryopreservedNum}"/>
          </iais:value>
        </iais:row>

        <iais:row>
          <iais:field width="4" value="Cryopreservation Date" cssClass="col-md-4"/>
          <iais:value width="4" cssClass="col-xs-4 col-md-4 control-label" display="true">
            <fmt:formatDate value='${arSuperDataSubmissionDto.arSubFreezingStageDto.cryopreservedDate}'
                            pattern='dd/MM/yyyy'/>
          </iais:value>
          <iais:value width="4" cssClass="col-xs-4 col-md-4 control-label" display="true">
            <fmt:formatDate value='${arSuperDataSubmissionDtoVersion.arSubFreezingStageDto.cryopreservedDate}'
                            pattern='dd/MM/yyyy'/>
          </iais:value>
        </iais:row>


      </div>
    </div>
  </div>
</div>
