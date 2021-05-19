<c:set var="appDeclarationMessageDto" value="${appSubmissionDto.appDeclarationMessageDto}"/>

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
                    The PO possesses the skills and competencies that are prescribed for the performance of the functions and duties as the licensee’s PO (if the application is granted)
                </span>
            </div>
            <div class="form-check col-xs-3">
                <input class="form-check-input" disabled type="radio" name="competenciesItem1" value = "0" aria-invalid="false"
                       <c:if test="${appDeclarationMessageDto.competenciesItem1=='0'}">checked="checked"</c:if> >
                <label class="form-check-label"><span class="check-circle"></span>Yes</label>
            </div>
            <div class="form-check col-xs-3">
                <input class="form-check-input" disabled type="radio" name="competenciesItem1" value = "1" aria-invalid="false"
                       <c:if test="${appDeclarationMessageDto.competenciesItem1=='1'}">checked="checked"</c:if> >
                <label class="form-check-label"><span class="check-circle"></span>No</label>
            </div>
        </div>
        <div>
            <div class="col-xs-12 form-group">
                <span>2.</span>
                <span>
                    The CGO(s) possesses or possess the skills and competencies that are prescribed for the performance of the functions and duties as the licensee’s CGO(s) (if the application is granted).
                </span>
            </div>
            <div class="form-check col-xs-3">
                <input class="form-check-input" disabled type="radio" name="competenciesItem2" value = "0" aria-invalid="false"
                       <c:if test="${appDeclarationMessageDto.competenciesItem2=='0'}">checked="checked"</c:if> >
                <label class="form-check-label"><span class="check-circle"></span>Yes</label>
            </div>
            <div class="form-check col-xs-3">
                <input class="form-check-input" disabled type="radio" name="competenciesItem2" value = "1" aria-invalid="false"
                       <c:if test="${appDeclarationMessageDto.competenciesItem2=='1'}">checked="checked"</c:if> >
                <label class="form-check-label"><span class="check-circle"></span>No</label>
            </div>
        </div>
        <div>
            <div class="col-xs-12 form-group">
                <span>3.</span>
                <span>
                    The composition of the KAHs satisfies the skills and competencies requirements prescribed or as specified in any code of practice if not prescribed (if the application is granted).
                </span>
            </div>
            <div class="form-check col-xs-3">
                <input class="form-check-input" disabled type="radio" name="competenciesItem3" value = "0" aria-invalid="false"
                       <c:if test="${appDeclarationMessageDto.competenciesItem3=='0'}">checked="checked"</c:if> >
                <label class="form-check-label"><span class="check-circle"></span>Yes</label>
            </div>
            <div class="form-check col-xs-3">
                <input class="form-check-input" disabled type="radio" name="competenciesItem3" value = "1" aria-invalid="false"
                       <c:if test="${appDeclarationMessageDto.competenciesItem3=='1'}">checked="checked"</c:if> >
                <label class="form-check-label"><span class="check-circle"></span>No</label>
            </div>
        </div>
        <div class="col-xs-12 form-group txt-area-normal">
            <p>If you have selected 'Yes' to any of the questions above, please provide further details below:</p>
            <textarea disabled id="competencyRemarks" cols="85"  rows="5" name="competenciesRemark">${appDeclarationMessageDto.competenciesRemark}</textarea>
        </div>
    </div>
</div>
