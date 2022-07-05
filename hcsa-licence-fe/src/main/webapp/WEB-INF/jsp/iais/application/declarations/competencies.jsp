<c:set var="appDeclarationMessageDto" value="${AppSubmissionDto.appDeclarationMessageDto}"/>

<div class="panel-body">
    <div class="row">
        <h2>Declaration on Competencies</h2>
    </div>
    <br>
    <div class="row">
        <div class="col-xs-12 form-group">Please indicate 'Yes' or 'No' to the following statements:</div>
        <br>
        <div>
            <div class="col-xs-12 form-group">
                <span>1.</span>
                <span>
                    <iais:message key="DECLARATION_COMPETENCIES_ITME_1" escape="false"/>
                </span>
            </div>
            <div class="form-check col-xs-3">
                <input class="form-check-input" type="radio" name="competenciesItem1" value = "1" aria-invalid="false"
                       <c:if test="${appDeclarationMessageDto.competenciesItem1=='1'}">checked="checked"</c:if> >
                <label class="form-check-label"><span class="check-circle"></span>Yes</label>
            </div>
            <div class="form-check col-xs-3">
                <input class="form-check-input" type="radio" name="competenciesItem1" value = "0" aria-invalid="false"
                       <c:if test="${appDeclarationMessageDto.competenciesItem1=='0'}">checked="checked"</c:if> >
                <label class="form-check-label"><span class="check-circle"></span>No</label>
            </div>
            <span class="error-msg col-xs-12 form-group" name="iaisErrorMsg" id="error_competenciesItem1"></span>
        </div>
        <div>
            <div class="col-xs-12 form-group">
                <span>2.</span>
                <span>
                    <iais:message key="DECLARATION_COMPETENCIES_ITME_2" escape="false"/>
                </span>
            </div>
            <div class="form-check col-xs-3">
                <input class="form-check-input" type="radio" name="competenciesItem2" value = "1" aria-invalid="false"
                       <c:if test="${appDeclarationMessageDto.competenciesItem2=='1'}">checked="checked"</c:if> >
                <label class="form-check-label"><span class="check-circle"></span>Yes</label>
            </div>
            <div class="form-check col-xs-3">
                <input class="form-check-input" type="radio" name="competenciesItem2" value = "0" aria-invalid="false"
                       <c:if test="${appDeclarationMessageDto.competenciesItem2=='0'}">checked="checked"</c:if> >
                <label class="form-check-label"><span class="check-circle"></span>No</label>
            </div>
            <span class="error-msg col-xs-12 form-group" name="iaisErrorMsg" id="error_competenciesItem2"></span>
        </div>
        <div>
            <div class="col-xs-12 form-group">
                <span>3.</span>
                <span>
                    <iais:message key="DECLARATION_COMPETENCIES_ITME_3" escape="false"/>
                </span>
            </div>
            <div class="form-check col-xs-3">
                <input class="form-check-input" type="radio" name="competenciesItem3" value = "1" aria-invalid="false"
                       <c:if test="${appDeclarationMessageDto.competenciesItem3=='1'}">checked="checked"</c:if> >
                <label class="form-check-label"><span class="check-circle"></span>Yes</label>
            </div>
            <div class="form-check col-xs-3">
                <input class="form-check-input" type="radio" name="competenciesItem3" value = "0" aria-invalid="false"
                       <c:if test="${appDeclarationMessageDto.competenciesItem3=='0'}">checked="checked"</c:if> >
                <label class="form-check-label"><span class="check-circle"></span>No</label>
            </div>
            <span class="error-msg col-xs-12 form-group" name="iaisErrorMsg" id="error_competenciesItem3"></span>
        </div>
        <div class="col-xs-12 form-group txt-area-normal">
            <p>If you have selected 'No' to any of the questions above, please provide further details below:</p>
            <textarea id="competencyRemarks" maxlength="1000" class="form-control" name="competenciesRemark"><c:out value="${appDeclarationMessageDto.competenciesRemark}" /></textarea>
            <span class="error-msg col-xs-12 form-group" name="iaisErrorMsg" id="error_competenciesRemark"></span>
        </div>
    </div>
</div>
