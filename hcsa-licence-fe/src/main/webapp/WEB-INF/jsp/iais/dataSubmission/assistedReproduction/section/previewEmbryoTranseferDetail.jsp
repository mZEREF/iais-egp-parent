<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="headingSign" value="completed"/>
<c:set var="embryoTransferStageDto" value="${arSuperDataSubmissionDto.embryoTransferStageDto}"/>
<div class="panel panel-default">
    <div class="panel-heading ${headingSign}">
        <h4 class="panel-title">
            <a class="" data-toggle="collapse" href="#patientDetails">
                Details of Patient
            </a>
        </h4>
    </div>
    <div id="patientDetails" class="panel-collapse collapse in">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal "><%--min-row--%>
                <iais:row>
                    <iais:field width="5" value="No. Transferred" cssClass="col-md-7"/>
                    <iais:value width="7" display="true">
                        <c:out value="${embryoTransferStageDto.transferNum}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Age of 1st Embryo Transferred" cssClass="col-md-7"/>
                    <iais:value width="7" display="true">
                        <c:out value="${embryoTransferStageDto.firstEmbryoAge}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Was the 1st Embryo Transferred a fresh or thawed embryo?" cssClass="col-md-7"/>
                    <iais:value width="7" display="true">
                        <c:out value="${embryoTransferStageDto.firstEmbryoType == 'fresh'?  'Fresh Embryo' : 'Thawed Embryo'}"/>
                    </iais:value>
                </iais:row>
                <div id="section2nd" <c:if test="${embryoTransferStageDto.transferNum < 2}">style="display: none;"</c:if>>
                    <iais:row>
                        <iais:field width="5" value="Age of 2nd Embryo Transferred" cssClass="col-md-7"/>
                        <iais:value width="7" display="true">
                            <c:out value="${secondEmbryoAge}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="5" value="Was the 2nd Embryo Transferred a fresh or thawed embryo?" cssClass="col-md-7"/>
                        <iais:value width="7" display="true">
                            <c:out value="${embryoTransferStageDto.secondEmbryoType == 'fresh'?  'Fresh Embryo' : 'Thawed Embryo'}"/>
                        </iais:value>
                    </iais:row>
                </div>
                <div id="section2nd" <c:if test="${embryoTransferStageDto.transferNum < 2}">style="display: none;"</c:if>>
                    <iais:row>
                        <iais:field width="5" value="Age of 3rd Embryo Transferred" cssClass="col-md-7"/>
                        <iais:value width="7" display="true">
                            <c:out value="${embryoTransferStageDto.thirdEmbryoAge}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="5" value="Was the 3rd Embryo Transferred a fresh or thawed embryo?" cssClass="col-md-7"/>
                        <iais:value width="7" display="true">
                            <c:out value="${embryoTransferStageDto.thirdEmbryoType == 'fresh'?  'Fresh Embryo' : 'Thawed Embryo'}"/>
                        </iais:value>
                    </iais:row>
                </div>
                <iais:row>
                    <iais:field width="5" value="1st Date of Transfer" cssClass="col-md-7"/>
                    <iais:value width="7" display="true">
                        <fmt:formatDate value="${embryoTransferStageDto.firstTransferDate}" pattern="dd/MM/yyyy"></fmt:formatDate>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="2nd Date of Transfer (if applicable)" cssClass="col-md-7"/>
                    <iais:value width="7" display="true">
                        <c:out value="${secondTransferDate}"/>
                        <fmt:formatDate value="${embryoTransferStageDto.secondTransferDate}" pattern="dd/MM/yyyy"></fmt:formatDate>
                    </iais:value>
                </iais:row>
            </div>
        </div>
    </div>
</div>