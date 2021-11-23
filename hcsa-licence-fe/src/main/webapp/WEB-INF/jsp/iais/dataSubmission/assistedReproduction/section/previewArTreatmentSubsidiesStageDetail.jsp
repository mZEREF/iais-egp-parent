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
                    <iais:field width="6" value="Please indicate ART Co-funding" cssClass="col-md-6"/>
                    <iais:value width="6" cssClass="col-md-6">
                        <label class="col-xs-6 control-label col-md-6">
                            <iais:code code="${arTreatmentSubsidiesStageDto.coFunding}"/>
                        </label>
                    </iais:value>
                </iais:row>
                <c:if test="${isDisplayAppeal}">
                    <iais:row>
                        <iais:field width="6" value="Is there an Appeal?" cssClass="col-md-6"/>
                        <iais:value width="6" cssClass="col-md-6">
                            <c:out value="${arTreatmentSubsidiesStageDto.isThereAppeal?'Yes':'No'}"/>
                        </iais:value>
                    </iais:row>
                </c:if>
                <%@include file="../common/patientInventoryTable.jsp" %>
            </div>
        </div>
    </div>
</div>