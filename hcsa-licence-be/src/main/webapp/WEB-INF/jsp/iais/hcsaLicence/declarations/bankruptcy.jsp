<c:set var="appDeclarationMessageDto" value="${appSubmissionDto.appDeclarationMessageDto}"/>

<div class="panel-body">
    <div class="row">
        <h2>Declaration on bankruptcy</h2>
    </div>
    <br>
    <div class="row">
        <div class="col-xs-12 form-group">Please indicate 'Yes' or 'No' to the following statements:</div>
        <br>
        <div>
            <div class="col-xs-12 form-group">
                <span>1.</span>
                <span>
                   The Applicant is not an undischarged bankrupt or has gone, or is likely to go, into compulsory or voluntary liquidation other than for the purpose of amalgamation or reconstruction.
                </span>
            </div>
            <div class="form-check col-xs-3">
                <input class="form-check-input" disabled type="radio" name="bankruptcyItem1" value = "1" aria-invalid="false"
                       <c:if test="${appDeclarationMessageDto.bankruptcyItem1=='1'}">checked="checked"</c:if> >
                <label class="form-check-label"><span class="check-circle"></span>Yes</label>
            </div>
            <div class="form-check col-xs-3">
                <input class="form-check-input" disabled type="radio" name="bankruptcyItem1" value = "0" aria-invalid="false"
                       <c:if test="${appDeclarationMessageDto.bankruptcyItem1=='0'}">checked="checked"</c:if> >
                <label class="form-check-label"><span class="check-circle"></span>No</label>
            </div>
            <span class="error-msg col-xs-12 form-group" name="iaisErrorMsg" id="error_bankruptcyItem1"></span>
        </div>
        <div>
            <div class="col-xs-12 form-group">
                <span>2.</span>
                <span>
                    The individual who is the principal officer for the Applicant and is to be the principal officer for the licensee (if the application is granted) ("PO") is not an undischarged bankrupt.
                </span>
            </div>
            <div class="form-check col-xs-3">
                <input class="form-check-input" disabled type="radio" name="bankruptcyItem2" value = "1" aria-invalid="false"
                       <c:if test="${appDeclarationMessageDto.bankruptcyItem2=='1'}">checked="checked"</c:if> >
                <label class="form-check-label"><span class="check-circle"></span>Yes</label>
            </div>
            <div class="form-check col-xs-3">
                <input class="form-check-input" disabled type="radio" name="bankruptcyItem2" value = "0" aria-invalid="false"
                       <c:if test="${appDeclarationMessageDto.bankruptcyItem2=='0'}">checked="checked"</c:if> >
                <label class="form-check-label" ><span class="check-circle"></span>No</label>
            </div>
            <span class="error-msg col-xs-12 form-group" name="iaisErrorMsg" id="error_bankruptcyItem2"></span>
        </div>
        <div>
            <div class="col-xs-12 form-group">
                <span>3.</span>
                <span>
                    The individual or individuals who is or are the Clinical Governance Officer or Clinical Governance Officers for the Applicant and is or are to be the Clinical Governance Officer or Clinical Governance Officers for the licensee (if the application is granted) ("CGO(s)") is not an or are not undischarged  bankruptcy(s).
                </span>
            </div>
            <div class="form-check col-xs-3">
                <input class="form-check-input" disabled type="radio" name="bankruptcyItem3" value = "1" aria-invalid="false"
                       <c:if test="${appDeclarationMessageDto.bankruptcyItem3=='1'}">checked="checked"</c:if> >
                <label class="form-check-label"><span class="check-circle"></span>Yes</label>
            </div>
            <div class="form-check col-xs-3">
                <input class="form-check-input" disabled type="radio" name="bankruptcyItem3" value = "0" aria-invalid="false"
                       <c:if test="${appDeclarationMessageDto.bankruptcyItem3=='0'}">checked="checked"</c:if>>
                <label class="form-check-label"><span class="check-circle"></span>No</label>
            </div>
            <span class="error-msg col-xs-12 form-group" name="iaisErrorMsg" id="error_bankruptcyItem3"></span>
        </div>
        <div>
            <div class="col-xs-12 form-group">
                <span>4.</span>
                <span>
                    The Applicant's key appointment holders ("<b>KAHs</b>") are not undischarged bankrupt(s).
                </span>
            </div>
            <div class="form-check col-xs-3">
                <input class="form-check-input" disabled type="radio" name="bankruptcyItem4" value = "1" aria-invalid="false"
                       <c:if test="${appDeclarationMessageDto.bankruptcyItem4=='1'}">checked="checked"</c:if>>
                <label class="form-check-label"><span class="check-circle"></span>Yes</label>
            </div>
            <div class="form-check col-xs-3">
                <input class="form-check-input" disabled type="radio" name="bankruptcyItem4" value = "0" aria-invalid="false"
                       <c:if test="${appDeclarationMessageDto.bankruptcyItem4=='0'}">checked="checked"</c:if>>
                <label class="form-check-label"><span class="check-circle"></span>No</label>
            </div>
            <span class="error-msg col-xs-12 form-group" name="iaisErrorMsg" id="error_bankruptcyItem4"></span>
        </div>
        <div class="col-xs-12 txt-area-normal">
            <p>If you have selected 'Yes' to any of the questions above, please provide further details below:</p>
            <textarea disabled id="bankruptcyRemark" cols="85" rows="5" name="bankruptcyRemark">${appDeclarationMessageDto.bankruptcyRemark}</textarea>
            <span class="error-msg col-xs-12 form-group" name="iaisErrorMsg" id="error_bankruptcyRemark"></span>
        </div>
    </div>
</div>
