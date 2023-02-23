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
                <%@include file="comPart.jsp" %>
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
                    <div class="col-md-6">
                        <%@include file="previewEmbryoTransferDetailSectionCurr.jsp" %>
                    </div>
                    <div class="col-md-6">
                        <%@include file="previewEmbryoTransferDetailSection.jsp" %>
                    </div>
                </iais:row>

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