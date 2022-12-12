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
          <iais:field width="4" value="What was cryopreserved?" cssClass="col-md-4"/>
          <iais:value width="4" cssClass="col-xs-4 col-md-4 control-label" display="true">
            <c:if test="${arSuperDataSubmissionDto.arSubFreezingStageDto.isFreshOocyte eq '1'}">Fresh Oocyte(s)<br></c:if>
            <c:if test="${arSuperDataSubmissionDto.arSubFreezingStageDto.isThawedOocyte eq '1'}">Thawed Oocyte(s)<br></c:if>
            <c:if test="${arSuperDataSubmissionDto.arSubFreezingStageDto.isFreshEmbryo eq '1'}">Fresh Embryo(s)<br></c:if>
            <c:if test="${arSuperDataSubmissionDto.arSubFreezingStageDto.isThawedEmbryo eq '1'}">Thawed Embryo(s)</c:if>
          </iais:value>
          <iais:value width="4" cssClass="col-xs-4 col-md-4 control-label" display="true">
            <c:if test="${arSuperDataSubmissionDtoVersion.arSubFreezingStageDto.isFreshOocyte eq '1'}">Fresh Oocyte(s)<br></c:if>
            <c:if test="${arSuperDataSubmissionDtoVersion.arSubFreezingStageDto.isThawedOocyte eq '1'}">Thawed Oocyte(s)<br></c:if>
            <c:if test="${arSuperDataSubmissionDtoVersion.arSubFreezingStageDto.isFreshEmbryo eq '1'}">Fresh Embryo(s)<br></c:if>
            <c:if test="${arSuperDataSubmissionDtoVersion.arSubFreezingStageDto.isThawedEmbryo eq '1'}">Thawed Embryo(s)</c:if>
          </iais:value>
        </iais:row>

        <div id="freshOocyte" <c:if test="${arSuperDataSubmissionDto.arSubFreezingStageDto.isFreshOocyte ne 1 and arSuperDataSubmissionDtoVersion.arSubFreezingStageDto.isFreshOocyte ne 1}">style="display: none" </c:if>>
          <iais:row>
            <iais:field width="4" value="No. Cryopreserved (Fresh Oocyte(s))" cssClass="col-md-4"/>
            <iais:value width="4" cssClass="col-xs-4 col-md-4 control-label" display="true">
              <c:out value="${arSuperDataSubmissionDto.arSubFreezingStageDto.freshOocyteCryopNum}"/>
            </iais:value>
            <iais:value width="4" cssClass="col-xs-4 col-md-4 control-label" display="true">
              <c:out value="${arSuperDataSubmissionDtoVersion.arSubFreezingStageDto.freshOocyteCryopNum}"/>
            </iais:value>
          </iais:row>
        </div>
        <div id="thawedOocyte" <c:if test="${arSuperDataSubmissionDto.arSubFreezingStageDto.isThawedOocyte ne 1 and arSuperDataSubmissionDtoVersion.arSubFreezingStageDto.isThawedOocyte ne 1}">style="display: none" </c:if>>
          <iais:row>
            <iais:field width="4" value="No. Cryopreserved (Thawed Oocyte(s))" cssClass="col-md-4"/>
            <iais:value width="4" cssClass="col-xs-4 col-md-4 control-label" display="true">
              <c:out value="${arSuperDataSubmissionDto.arSubFreezingStageDto.thawedOocyteCryopNum}"/>
            </iais:value>
            <iais:value width="4" cssClass="col-xs-4 col-md-4 control-label" display="true">
              <c:out value="${arSuperDataSubmissionDtoVersion.arSubFreezingStageDto.thawedOocyteCryopNum}"/>
            </iais:value>
          </iais:row>
        </div>
        <div id="freshEmbryo" <c:if test="${arSuperDataSubmissionDto.arSubFreezingStageDto.isFreshEmbryo ne 1 and arSuperDataSubmissionDtoVersion.arSubFreezingStageDto.isFreshEmbryo ne 1}">style="display: none" </c:if>>
          <iais:row>
            <iais:field width="4" value="No. Cryopreserved (Thawed Embryo(s))" cssClass="col-md-4"/>
            <iais:value width="4" cssClass="col-xs-4 col-md-4 control-label" display="true">
              <c:out value="${arSuperDataSubmissionDto.arSubFreezingStageDto.freshEmbryoCryopNum}"/>
            </iais:value>
            <iais:value width="4" cssClass="col-xs-4 col-md-4 control-label" display="true">
              <c:out value="${arSuperDataSubmissionDtoVersion.arSubFreezingStageDto.freshEmbryoCryopNum}"/>
            </iais:value>
          </iais:row>
        </div>
        <div id="thawedEmbryo" <c:if test="${arSuperDataSubmissionDto.arSubFreezingStageDto.isThawedEmbryo ne 1 and arSuperDataSubmissionDtoVersion.arSubFreezingStageDto.isThawedEmbryo ne 1}">style="display: none" </c:if>>
          <iais:row>
            <iais:field width="4" value="No. Cryopreserved (Thawed Embryo(s))" cssClass="col-md-4"/>
            <iais:value width="4" cssClass="col-xs-4 col-md-4 control-label" display="true">
              <c:out value="${arSuperDataSubmissionDto.arSubFreezingStageDto.thawedEmbryoCryopNum}"/>
            </iais:value>
            <iais:value width="4" cssClass="col-xs-4 col-md-4 control-label" display="true">
              <c:out value="${arSuperDataSubmissionDtoVersion.arSubFreezingStageDto.thawedEmbryoCryopNum}"/>
            </iais:value>
          </iais:row>
        </div>

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
