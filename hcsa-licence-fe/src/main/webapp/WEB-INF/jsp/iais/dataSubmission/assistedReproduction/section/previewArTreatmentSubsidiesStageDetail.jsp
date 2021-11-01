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
                    <label class="col-xs-6 col-md-6 ">
                        <c:if test="${arTreatmentSubsidiesStageDto.coFunding == 'No'}">No Co-funding for this cycle</c:if>
                        <c:if test="${arTreatmentSubsidiesStageDto.coFunding == 'Fresh'}">Fresh Cycle Subsidy</c:if>
                        <c:if test="${arTreatmentSubsidiesStageDto.coFunding == 'Frozen'}">Frozen Cycle Subsidy</c:if>
                    </label>
                </iais:row>
                <iais:row>
                    <label class="col-xs-6 col-md-6 ">Is there an Appeal?</label>
                    <label class="col-xs-6 col-md-6 ">
                        <c:if test="${arTreatmentSubsidiesStageDto.isThereAppeal == 'true'}">Yes</c:if>
                        <c:if test="${arTreatmentSubsidiesStageDto.isThereAppeal == 'false'}">No</c:if>
                    </label>
                </iais:row>
                <h3>Patient's Inventory</h3>
                <table aria-describedby="" class="table discipline-table">
                    <thead>
                    <tr>
                        <th scope="col"></th>
                        <th scope="col">Frozen Oocytes</th>
                        <th scope="col">Thawed Oocytes</th>
                        <th scope="col">Fresh Oocytes</th>
                        <th scope="col">Frozen Embryos</th>
                        <th scope="col">Thawed Embryos</th>
                        <th scope="col">Fresh Embryos</th>
                        <th scope="col">Frozen Sperms</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <th scope="col">Changes</th>
                        <th scope="col">0</th>
                        <th scope="col">0</th>
                        <th scope="col">0</th>
                        <th scope="col">0</th>
                        <th scope="col">0</th>
                        <th scope="col">0</th>
                        <th scope="col">0</th>
                    </tr>
                    <tr>
                        <th scope="col">Current</th>
                        <th scope="col">0</th>
                        <th scope="col">0</th>
                        <th scope="col">0</th>
                        <th scope="col">0</th>
                        <th scope="col">0</th>
                        <th scope="col">0</th>
                        <th scope="col">0</th>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>