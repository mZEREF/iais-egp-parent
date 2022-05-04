<c:set var="arTreatmentSubsidiesStageDto" value="${arSuperDataSubmissionDto.arTreatmentSubsidiesStageDto}"/>
<c:set var="arTreatmentSubsidiesStageDtoVersion" value="${arSuperDataSubmissionDtoVersion.arTreatmentSubsidiesStageDto}"/>

<div class="panel panel-default">
    <div class="panel-heading  ">
        <h4 class="panel-title">
            <a href="#cycleDetails" data-toggle="collapse">
                AR Treatment Co-funding
            </a>
        </h4>
    </div>
    <div id="cycleDetails" class="panel-collapse collapse in">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal">
                <%@include file="comPart.jsp" %>
                <iais:row>
                    <iais:field width="4" value="Is the ART cycle being co-funded" cssClass="col-md-4"/>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <iais:code code="${arTreatmentSubsidiesStageDto.coFunding}"/>
                    </iais:value>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <iais:code code="${arTreatmentSubsidiesStageDtoVersion.coFunding}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="4" value="Is there an approved appeal?" cssClass="col-md-4"/>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <c:out value="${arTreatmentSubsidiesStageDto.isThereAppeal?'Yes':'No'}"/>
                    </iais:value>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <c:out value="${arTreatmentSubsidiesStageDtoVersion.isThereAppeal?'Yes':'No'}"/>
                    </iais:value>
                </iais:row>
            </div>
        </div>
    </div>
</div>