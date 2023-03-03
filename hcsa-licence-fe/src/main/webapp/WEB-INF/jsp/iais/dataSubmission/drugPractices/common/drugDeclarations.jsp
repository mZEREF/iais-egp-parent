<c:if test="${empty declaration}">
    <c:set var="dataSubmissionDto" value="${dpSuperDataSubmissionDto.dataSubmissionDto}" />
</c:if>
<c:set var="drugPrescribedDispensedDto" value="${dpSuperDataSubmissionDto.drugPrescribedDispensedDto}" />
<c:set var="drugMedication" value="${drugPrescribedDispensedDto.drugMedication}" />
<c:set var="drugSubmission" value="${drugPrescribedDispensedDto.drugSubmission}" />
<div class="panel panel-default">
    <div class="panel-heading">
        <h4 class="panel-title">
            <a class="collapsed" data-toggle="collapse" href="#ar-declaration">
                Declarations
            </a>
        </h4>
    </div>
    <div id="ar-declaration" class="panel-collapse collapse ">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal">
                <c:if test="${drugSubmission.drugType eq 'DPD002' and drugSubmission.getReasonMandatory() ==true and drugSubmission.medication eq 'MED001'}">
                    <iais:row>
                        <div class="form-check">
                            <c:if test="${DeclarationsCheckBox != 'hide'}">
                                <input class="form-check-input" id="dpLateReasonRadio" type="checkbox" name="dpLateReasonRadio" aria-invalid="false" value="1"
                                <c:if test="${!empty dataSubmissionDto.dpLateReasonRadio}">
                                       checked="checked"
                                </c:if>
                                       <c:if test="${formPriview == 1}">disabled="disabled"</c:if>
                                >
                            </c:if>
                            <label class="form-check-label" for="declaration">
                                <c:if test="${DeclarationsCheckBox != 'hide'}">
                                    <span class="check-square"></span>
                                </c:if>
                                <iais:message key="DS_DEC003" escape="false"/></label>
                            <span class="error-msg col-md-7" name="iaisErrorMsg" id="error_dpLateReasonRadio"></span>
                        </div>

                    </iais:row>
                    <iais:row>
                        <div class="col-xs-12">Reason for Late Submission
                            <c:if test="${DeclarationsCheckBox != 'hide' && dpSuperDataSubmissionDto.drugPrescribedDispensedDto.drugSubmission.getReasonMandatory()}">
                                <span class="mandatory">*</span>
                            </c:if>
                        </div>
                    </iais:row>
                    <iais:row>
                        <iais:value cssClass="col-sm-12 col-md-12 col-xs-12">
                            <textarea id="remarks" rows="6" maxlength="500" name="remarks" class="form-control">${dataSubmissionDto.remarks}</textarea>
                            <br><span class="error-msg" name="errorMsg" id="error_remarks"></span>
                        </iais:value>
                    </iais:row>
                </c:if>

                <iais:row>
                    <div class="form-check">
                        <c:if test="${DeclarationsCheckBox != 'hide'}">
                            <input class="form-check-input" id="declaration" type="checkbox" name="declaration" aria-invalid="false" value="1"
                            <c:if test="${!empty dataSubmissionDto.declaration}">
                                   checked="checked"
                            </c:if>
                                   <c:if test="${formPriview == 1}">disabled="disabled"</c:if>
                            >
                        </c:if>
                        <label class="form-check-label" for="declaration">
                            <c:if test="${DeclarationsCheckBox != 'hide'}">
                                <span class="check-square"></span>
                            </c:if>
                            <iais:message key="DS_DEC004" escape="false"/></label>
                        <span class="error-msg col-md-7" name="iaisErrorMsg" id="error_declaration"></span>
                    </div>
                </iais:row>
            </div>
        </div>
    </div>
</div>