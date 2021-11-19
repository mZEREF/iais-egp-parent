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
                            <iais:code code="${arTreatmentSubsidiesStageDto.coFunding}"/>
                        </p>
                    </div>
                </iais:row>
                <iais:row>
                    <label class="col-xs-6 col-md-6 ">Is there an Appeal?</label>
                    <div class="col-sm-7 col-xs-6 col-md-6">
                        <p>
                            ${arTreatmentSubsidiesStageDto.isThereAppeal?"Yes":"No"}
                        </p>
                    </div>
                </iais:row>
                <%@include file="../common/patientInventoryTable.jsp" %>
            </div>
        </div>
    </div>
</div>