<c:set var="appDeclarationMessageDto" value="${AppSubmissionDto.appDeclarationMessageDto}"/>

<div class="panel-body">
    <div class="row">
        <h2>Declaration on Bankruptcy</h2>
    </div>
    <br>
    <div class="row">
        <div class="col-xs-12 form-group">Please indicate ''Yes'' or ''No'' to the following statements:</div>
        <br>
        <div>
            <div class="col-xs-12 form-group">
                <span>1.</span>
                <span>
                    <iais:message key="DECLARATION_BANKRUPTCY_ITME_1" escape="false"/>
                </span>
            </div>
            <div class="form-check col-xs-3">
                <input class="form-check-input" type="radio" name="bankruptcyItem1" value = "1" aria-invalid="false"
                       <c:if test="${appDeclarationMessageDto.bankruptcyItem1=='1'}">checked="checked"</c:if> >
                <label class="form-check-label"><span class="check-circle"></span>Yes</label>
            </div>
            <div class="form-check col-xs-3">
                <input class="form-check-input" type="radio" name="bankruptcyItem1" value = "0" aria-invalid="false"
                       <c:if test="${appDeclarationMessageDto.bankruptcyItem1=='0'}">checked="checked"</c:if> >
                <label class="form-check-label"><span class="check-circle"></span>No</label>
            </div>
            <span class="error-msg col-xs-12 form-group" name="iaisErrorMsg" id="error_bankruptcyItem1"></span>
        </div>
        <div>
            <div class="col-xs-12 form-group">
                <span>2.</span>
                <span>
                    <iais:message key="DECLARATION_BANKRUPTCY_ITME_2" escape="false"/>
                </span>
            </div>
            <div class="form-check col-xs-3">
                <input class="form-check-input" type="radio" name="bankruptcyItem2" value = "1" aria-invalid="false"
                       <c:if test="${appDeclarationMessageDto.bankruptcyItem2=='1'}">checked="checked"</c:if> >
                <label class="form-check-label"><span class="check-circle"></span>Yes</label>
            </div>
            <div class="form-check col-xs-3">
                <input class="form-check-input" type="radio" name="bankruptcyItem2" value = "0" aria-invalid="false"
                       <c:if test="${appDeclarationMessageDto.bankruptcyItem2=='0'}">checked="checked"</c:if> >
                <label class="form-check-label" ><span class="check-circle"></span>No</label>
            </div>
            <span class="error-msg col-xs-12 form-group" name="iaisErrorMsg" id="error_bankruptcyItem2"></span>
        </div>
        <div>
            <div class="col-xs-12 form-group">
                <span>3.</span>
                <span>
                    <iais:message key="DECLARATION_BANKRUPTCY_ITME_3" escape="false"/>
                </span>
            </div>
            <div class="form-check col-xs-3">
                <input class="form-check-input" type="radio" name="bankruptcyItem3" value = "1" aria-invalid="false"
                       <c:if test="${appDeclarationMessageDto.bankruptcyItem3=='1'}">checked="checked"</c:if> >
                <label class="form-check-label"><span class="check-circle"></span>Yes</label>
            </div>
            <div class="form-check col-xs-3">
                <input class="form-check-input" type="radio" name="bankruptcyItem3" value = "0" aria-invalid="false"
                       <c:if test="${appDeclarationMessageDto.bankruptcyItem3=='0'}">checked="checked"</c:if>>
                <label class="form-check-label"><span class="check-circle"></span>No</label>
            </div>
            <span class="error-msg col-xs-12 form-group" name="iaisErrorMsg" id="error_bankruptcyItem3"></span>
        </div>
        <div>
            <div class="col-xs-12 form-group">
                <span>4.</span>
                <span>
                    <iais:message key="DECLARATION_BANKRUPTCY_ITME_4" escape="false"/>
                </span>
            </div>
            <div class="form-check col-xs-3">
                <input class="form-check-input" type="radio" name="bankruptcyItem4" value = "1" aria-invalid="false"
                       <c:if test="${appDeclarationMessageDto.bankruptcyItem4=='1'}">checked="checked"</c:if>>
                <label class="form-check-label"><span class="check-circle"></span>Yes</label>
            </div>
            <div class="form-check col-xs-3">
                <input class="form-check-input" type="radio" name="bankruptcyItem4" value = "0" aria-invalid="false"
                       <c:if test="${appDeclarationMessageDto.bankruptcyItem4=='0'}">checked="checked"</c:if>>
                <label class="form-check-label"><span class="check-circle"></span>No</label>
            </div>
            <span class="error-msg col-xs-12 form-group" name="iaisErrorMsg" id="error_bankruptcyItem4"></span>
        </div>
        <div class="col-xs-12 txt-area-normal">
            <p>If you have selected 'No' to any of the questions above, please provide further details below:</p>
            <textarea id="bankruptcyRemark" class="form-control" name="bankruptcyRemark" maxlength="1000"><c:out value="${appDeclarationMessageDto.bankruptcyRemark}" /></textarea>
            <span class="error-msg col-xs-12 form-group" name="iaisErrorMsg" id="error_bankruptcyRemark"></span>
        </div>
    </div>
</div>
