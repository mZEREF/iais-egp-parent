<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="embryoTransferStageDto" value="${arSuperDataSubmissionDto.embryoTransferStageDto}"/>
<c:set var="embryoTransferStageDtoVersion" value="${arSuperDataSubmissionDtoVersion.embryoTransferStageDto}"/>

<div class="panel panel-default">
    <div class="panel-heading ">
        <h4 class="panel-title">
            <a href="#cycleDetails" data-toggle="collapse">
                Embryo Transfer
            </a>
        </h4>
    </div>
    <div id="cycleDetails" class="panel-collapse collapse in">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal ">
                <h3>
                    <label ><c:out value="${arSuperDataSubmissionDto.patientInfoDto.patient.name}"/></label>
                    <span style="font-weight:normal"><c:out value="(${arSuperDataSubmissionDto.patientInfoDto.patient.idNumber})"/>
                    </span>
                </h3>
                <iais:row>
                    <iais:field width="4" cssClass="col-md-4"  value="" />
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <c:out value="Current Version"/>
                    </iais:value>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <iais:select  id="oldDsSelect" name="oldDsSelect" options="versionOptions" value="${arSuperDataSubmissionDtoVersion.dataSubmissionDto.id}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="4" value="No. Transferred" cssClass="col-md-4"/>
                    <iais:value width="4" display="true" cssClass="col-md-4">
                        <c:out value="${embryoTransferStageDto.transferNum}"/>
                    </iais:value>
                    <iais:value width="4" display="true" cssClass="col-md-4">
                        <c:out value="${embryoTransferStageDtoVersion.transferNum}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="4" value="Age of 1st Embryo Transferred" cssClass="col-md-4"/>
                    <iais:value width="4" display="true" cssClass="col-md-4">
                        <iais:code code="${embryoTransferStageDto.firstEmbryoAge}"/>
                    </iais:value>
                    <iais:value width="4" display="true" cssClass="col-md-4">
                        <iais:code code="${embryoTransferStageDtoVersion.firstEmbryoAge}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="4" value="Was the 1st Embryo Transferred a fresh or thawed embryo?"
                                cssClass="col-md-4"/>
                    <iais:value width="4" display="true" cssClass="col-md-4">
                        <c:if test="${ embryoTransferStageDto.firstEmbryoType =='fresh' }">Fresh Embryo</c:if>
                        <c:if test="${ embryoTransferStageDto.firstEmbryoType =='thawed' }">Thawed Embryo</c:if>
                    </iais:value>
                    <iais:value width="4" display="true" cssClass="col-md-4">
                        <c:if test="${ embryoTransferStageDtoVersion.firstEmbryoType =='fresh' }">Fresh Embryo</c:if>
                        <c:if test="${ embryoTransferStageDtoVersion.firstEmbryoType =='thawed' }">Thawed Embryo</c:if>
                    </iais:value>
                </iais:row>
                <div id="section2nd"
                     <c:if test="${embryoTransferStageDto.transferNum < 2 || embryoTransferStageDtoVersion.transferNum < 2}">style="display: none;"</c:if>>
                    <iais:row>
                        <iais:field width="4" value="Age of 2nd Embryo Transferred" cssClass="col-md-4"/>
                        <iais:value width="4" display="true" cssClass="col-md-4">
                            <iais:code code="${embryoTransferStageDto.secondEmbryoAge}"/>
                        </iais:value>
                        <iais:value width="4" display="true" cssClass="col-md-4">
                            <iais:code code="${embryoTransferStageDtoVersion.secondEmbryoAge}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="4" value="Was the 2nd Embryo Transferred a fresh or thawed embryo?"
                                    cssClass="col-md-4"/>
                        <iais:value width="4" display="true" cssClass="col-md-4">
                            <c:if test="${ embryoTransferStageDto.secondEmbryoType =='fresh' }">Fresh Embryo</c:if>
                            <c:if test="${ embryoTransferStageDto.secondEmbryoType =='thawed' }">Thawed Embryo</c:if>
                        </iais:value>
                        <iais:value width="4" display="true" cssClass="col-md-4">
                            <c:if test="${ embryoTransferStageDtoVersion.secondEmbryoType =='fresh' }">Fresh Embryo</c:if>
                            <c:if test="${ embryoTransferStageDtoVersion.secondEmbryoType =='thawed' }">Thawed Embryo</c:if>
                        </iais:value>
                    </iais:row>
                </div>
                <div id="section3nd"
                     <c:if test="${embryoTransferStageDto.transferNum < 3 || embryoTransferStageDtoVersion.transferNum < 3}">style="display: none;"</c:if>>
                    <iais:row>
                        <iais:field width="4" value="Age of 3rd Embryo Transferred" cssClass="col-md-4"/>
                        <iais:value width="4" display="true" cssClass="col-md-4">
                            <iais:code code="${embryoTransferStageDto.thirdEmbryoAge}"/>
                        </iais:value>
                        <iais:value width="4" display="true" cssClass="col-md-4">
                            <iais:code code="${embryoTransferStageDtoVersion.thirdEmbryoAge}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="4" value="Was the 3rd Embryo Transferred a fresh or thawed embryo?"
                                    cssClass="col-md-4"/>
                        <iais:value width="4" display="true" cssClass="col-md-4">
                            <c:if test="${ embryoTransferStageDto.thirdEmbryoType =='fresh' }">Fresh Embryo</c:if>
                            <c:if test="${ embryoTransferStageDto.thirdEmbryoType =='thawed' }">Thawed Embryo</c:if>
                        </iais:value>
                        <iais:value width="4" display="true" cssClass="col-md-4">
                            <c:if test="${ embryoTransferStageDtoVersion.thirdEmbryoType =='fresh' }">Fresh Embryo</c:if>
                            <c:if test="${ embryoTransferStageDtoVersion.thirdEmbryoType =='thawed' }">Thawed Embryo</c:if>
                        </iais:value>
                    </iais:row>
                </div>
                <iais:row>
                    <iais:field width="4" value="1st Date of Transfer" cssClass="col-md-4"/>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <fmt:formatDate value="${embryoTransferStageDto.firstTransferDate}" pattern="dd/MM/yyyy"/>
                    </iais:value>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <fmt:formatDate value="${embryoTransferStageDtoVersion.firstTransferDate}" pattern="dd/MM/yyyy"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="4" value="2nd Date of Transfer (if applicable)" cssClass="col-md-4"/>
                    <iais:value width="4" display="true" cssClass="col-md-4">
                        <c:out value="${secondTransferDate}"/>
                        <fmt:formatDate value="${embryoTransferStageDto.secondTransferDate}" pattern="dd/MM/yyyy"/>
                    </iais:value>
                    <iais:value width="4" display="true" cssClass="col-md-4">
                        <c:out value="${secondTransferDate}"/>
                        <fmt:formatDate value="${embryoTransferStageDtoVersion.secondTransferDate}" pattern="dd/MM/yyyy"/>
                    </iais:value>
                </iais:row>

            </div>
        </div>
    </div>
</div>