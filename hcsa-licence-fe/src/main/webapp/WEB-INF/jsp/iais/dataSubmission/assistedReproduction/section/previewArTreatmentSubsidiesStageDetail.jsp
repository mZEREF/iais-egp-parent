<c:set var="arTreatmentSubsidiesStageDto" value="${arSuperDataSubmissionDto.arTreatmentSubsidiesStageDto}"/>
<c:set var="headingSign" value="completed"/>
<div class="panel panel-default">
    <div class="panel-heading ${headingSign} ">
        <h4 class="panel-title">
            <a href="#cycleDetails" data-toggle="collapse">
                AR Treatment Co-funding
            </a>
        </h4>
    </div>
    <div id="cycleDetails" class="panel-collapse collapse in">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal">
                <c:set var="patientDto" value="${arSuperDataSubmissionDto.patientInfoDto.patient}"/>
                <p>
                    <label style="font-family:'Arial Negreta', 'Arial Normal', 'Arial';font-weight:700;font-size: 2.2rem;">
                        <c:out value="${patientDto.name}"/>&nbsp
                    </label>
                    <label style="font-family:'Arial Normal', 'Arial';font-weight:400;">${empty patientDto.idNumber ? "" : "("}
                        <c:out value="${patientDto.idNumber}"/>
                        ${empty patientDto.idNumber ? "" : ")"}
                    </label>
                </p>
                <hr/>
                <iais:row>
                    <iais:field width="6" value="Please indicate ART Co-funding" cssClass="col-md-6"/>
                    <iais:value width="6" cssClass="col-md-6" display="true">
                        <iais:code code="${arTreatmentSubsidiesStageDto.coFunding}"/>
                    </iais:value>
                </iais:row>
                <c:if test="${isDisplayAppeal}">
                    <iais:row>
                        <iais:field width="6" value="Is there an Appeal?" cssClass="col-md-6"/>
                        <iais:value width="6" cssClass="col-md-6" display="true">
                            <c:out value="${arTreatmentSubsidiesStageDto.isThereAppeal?'Yes':'No'}"/>
                        </iais:value>
                    </iais:row>
                </c:if>
                <%@include file="../common/patientInventoryTable.jsp" %>
            </div>
        </div>
    </div>
</div>