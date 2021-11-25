<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="embryoTransferStageDto" value="${arSuperDataSubmissionDto.embryoTransferStageDto}"/>
<div class="panel panel-default">
    <div class="panel-heading completed">
        <h4 class="panel-title">
            <a class="collapsed" data-toggle="collapse" href="#">
                Embryo Transfer
            </a>
        </h4>
    </div>
    <div id="patientDetails" class="panel-collapse collapse in">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal ">
                <c:set var="patientDto" value="${arSuperDataSubmissionDto.patientInfoDto.patient}" />
                <h3>
                    <p><label style="font-family:'Arial Negreta', 'Arial Normal', 'Arial';font-weight:700;"><c:out value="${patientDto.name}"/>&nbsp</label><label style="font-family:'Arial Normal', 'Arial';font-weight:400;">${empty patientDto.idNumber ? "" : "("}<c:out value="${patientDto.idNumber}"/>${empty patientDto.idNumber ? "" : ")"} </label></p>
                </h3>
                <iais:row>
                    <iais:field width="6" value="No. Transferred" cssClass="col-md-6"/>
                    <iais:value width="6" display="true" cssClass="col-md-6">
                        <c:out value="${embryoTransferStageDto.transferNum}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="6" value="Age of 1st Embryo Transferred" cssClass="col-md-6"/>
                    <iais:value width="6" display="true" cssClass="col-md-6">
                        <iais:code code="${embryoTransferStageDto.firstEmbryoAge}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="6" value="Was the 1st Embryo Transferred a fresh or thawed embryo?"
                                cssClass="col-md-6"/>
                    <iais:value width="6" display="true" cssClass="col-md-6">
                        <c:out value="${embryoTransferStageDto.firstEmbryoType == 'fresh'?  'Fresh Embryo' : 'Thawed Embryo'}"/>
                    </iais:value>
                </iais:row>
                <div id="section2nd"
                     <c:if test="${embryoTransferStageDto.transferNum < 2}">style="display: none;"</c:if>>
                    <iais:row>
                        <iais:field width="6" value="Age of 2nd Embryo Transferred" cssClass="col-md-6"/>
                        <iais:value width="6" display="true" cssClass="col-md-6">
                            <iais:code code="${embryoTransferStageDto.secondEmbryoAge}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="6" value="Was the 2nd Embryo Transferred a fresh or thawed embryo?"
                                    cssClass="col-md-6"/>
                        <iais:value width="6" display="true" cssClass="col-md-6">
                            <c:out value="${embryoTransferStageDto.secondEmbryoType == 'fresh'?  'Fresh Embryo' : 'Thawed Embryo'}"/>
                        </iais:value>
                    </iais:row>
                </div>
                <div id="section2nd"
                     <c:if test="${embryoTransferStageDto.transferNum < 2}">style="display: none;"</c:if>>
                    <iais:row>
                        <iais:field width="6" value="Age of 3rd Embryo Transferred" cssClass="col-md-6"/>
                        <iais:value width="6" display="true" cssClass="col-md-6">
                            <iais:code code="${embryoTransferStageDto.thirdEmbryoAge}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="6" value="Was the 3rd Embryo Transferred a fresh or thawed embryo?"
                                    cssClass="col-md-6"/>
                        <iais:value width="6" display="true" cssClass="col-md-6">
                            <c:out value="${embryoTransferStageDto.thirdEmbryoType == 'fresh'?  'Fresh Embryo' : 'Thawed Embryo'}"/>
                        </iais:value>
                    </iais:row>
                </div>
                <iais:row>
                    <iais:field width="6" value="1st Date of Transfer" cssClass="col-md-6"/>
                    <iais:value width="6" display="true">
                        <fmt:formatDate value="${embryoTransferStageDto.firstTransferDate}" pattern="dd/MM/yyyy"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="6" value="2nd Date of Transfer (if applicable)" cssClass="col-md-6"/>
                    <iais:value width="6" display="true" cssClass="col-md-6">
                        <c:out value="${secondTransferDate}"/>
                        <fmt:formatDate value="${embryoTransferStageDto.secondTransferDate}" pattern="dd/MM/yyyy"/>
                    </iais:value>
                </iais:row>
            </div>
        </div>
    </div>
</div>