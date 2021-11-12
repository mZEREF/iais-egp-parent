<c:set var="arTreatmentSubsidiesStageDto" value="${arSuperDataSubmissionDto.arTreatmentSubsidiesStageDto}"/>
<div class="panel panel-default">
    <div class="panel-heading completed ">
        <h4 class="panel-title">
            <a class="collapsed" data-toggle="collapse" href="#">
                AR Treatment Subsidies
            </a>
        </h4>
    </div>
    <div id="atsDetails" class="panel-collapse collapse in">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal">
                <iais:row>
                    <label class="col-xs-6 col-md-6 ">Please indicate ART Co-funding</label>
                    <div class="col-sm-7 col-xs-6 col-md-6">
                        <p>
                            <c:if test="${arTreatmentSubsidiesStageDto.coFunding == 'No'}">No Co-funding for this cycle</c:if>
                            <c:if test="${arTreatmentSubsidiesStageDto.coFunding == 'Fresh'}">Fresh Cycle Subsidy</c:if>
                            <c:if test="${arTreatmentSubsidiesStageDto.coFunding == 'Frozen'}">Frozen Cycle Subsidy</c:if>
                        </p>
                    </div>
                </iais:row>
                <iais:row>
                    <label class="col-xs-6 col-md-6 ">Is there an Appeal?</label>
                    <div class="col-sm-7 col-xs-6 col-md-6">
                        <p>
                            <c:if test="${arTreatmentSubsidiesStageDto.isThereAppeal == 'true'}">Yes</c:if>
                            <c:if test="${arTreatmentSubsidiesStageDto.isThereAppeal == 'false'}">No</c:if>
                        </p>
                    </div>
                </iais:row>
                <%@include file="../common/patientInventoryTable.jsp" %>
            </div>
        </div>
    </div>
</div>