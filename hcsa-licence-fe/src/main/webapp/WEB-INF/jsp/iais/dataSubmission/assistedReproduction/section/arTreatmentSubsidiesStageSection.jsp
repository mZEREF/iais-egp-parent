<script type="text/javascript" src="<%=webroot1%>js/dataSubmission/arTreatmentSubsidiesStage.js"></script>
<c:set var="arTreatmentSubsidiesStageDto" value="${arSuperDataSubmissionDto.arTreatmentSubsidiesStageDto}"/>
<div class="panel panel-default">
    <div class="panel-heading">
        <h4 class="panel-title">
            <strong>
                AR Treatment Subsidies
            </strong>
        </h4>
    </div>
    <div id="patientDetails" class="panel-collapse collapse in">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal">
                <input name="freshCount" type="hidden" value="${freshCount}"/>
                <input name="frozenCount" type="hidden" value="${frozenCount}"/>
                <iais:row>
                    <iais:field width="6" value="Please indicate ART Co-funding" mandatory="true" cssClass="col-md-6"/>
                    <iais:value width="6" cssClass="col-md-6">
                        <c:forEach var="artCoFundingItem" items="${artCoFundingOptions}" varStatus="defectTypeStatus">
                            <div class="form-check">
                                <input class="form-check-input"
                                       type="radio"
                                       name="coFunding"
                                       value="${artCoFundingItem.value}"
                                       id="coFunding${artCoFundingItem.value}"
                                       <c:if test="${arTreatmentSubsidiesStageDto.coFunding == artCoFundingItem.value}">checked</c:if>
                                       aria-invalid="false">
                                <label class="form-check-label"
                                       for="coFunding${artCoFundingItem.value}"><span
                                        class="check-circle"></span>${artCoFundingItem.text}</label>
                            </div>
                        </c:forEach>
                        <span id="error_coFunding" name="iaisErrorMsg" class="error-msg"></span>
                    </iais:value>
                </iais:row>
                <iais:row id="isThereAppealRow">
                    <iais:field width="6" value="Is there an Appeal?" mandatory="true" cssClass="col-md-6"/>
                    <iais:value width="3" cssClass="col-md-3">
                        <div class="form-check">
                            <input class="form-check-input"
                                   type="radio"
                                   name="isThereAppeal"
                                   value="true"
                                   id="isThereAppealYes"
                                   <c:if test="${arTreatmentSubsidiesStageDto.isThereAppeal}">checked</c:if>
                                   aria-invalid="false">
                            <label class="form-check-label"
                                   for="isThereAppealYes"><span
                                    class="check-circle"></span>Yes</label>
                        </div>
                    </iais:value>
                    <iais:value width="3" cssClass="col-md-3">
                        <div class="form-check">
                            <input class="form-check-input"
                                   type="radio"
                                   name="isThereAppeal"
                                   value="false"
                                   id="isThereAppealNo"
                                   <c:if test="${! arTreatmentSubsidiesStageDto.isThereAppeal}">checked</c:if>
                                   aria-invalid="false">
                            <label class="form-check-label"
                                   for="isThereAppealNo"><span
                                    class="check-circle"></span>No</label>
                        </div>
                    </iais:value>
                    <span id="error_isThereAppeal" name="iaisErrorMsg" class="error-msg"></span>
                </iais:row>
                <span id="error_isThereAppealShow" name="iaisErrorMsg" class="error-msg"></span>
            </div>
        </div>
    </div>
</div>