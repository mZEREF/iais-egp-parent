<c:set var="appDeclarationMessageDto" value="${appSubmissionDto.appDeclarationMessageDto}"/>

<div class="panel-body">
    <div class="row">
        <h2>Co-Location Declaration</h2>
    </div>
    <br>
    <div class="row">
        <div>
            <div class="col-xs-12 form-group">
                <iais:message key="DECLARATION_CO_LOCATION_ITME_1" escape="false"/>
            </div>
            <div class="form-check col-xs-3">
                <input class="form-check-input" disabled type="radio" name="coLocationItem1" value = "1" aria-invalid="false"
                       <c:if test="${appDeclarationMessageDto.coLocationItem1 == '1'}">checked="checked"</c:if> />
                <label class="form-check-label"><span class="check-circle"></span>Yes</label>
            </div>
            <div class="form-check form-group col-xs-3">
                <input class="form-check-input" disabled type="radio" name="coLocationItem1" value = "0" aria-invalid="false"
                       <c:if test="${appDeclarationMessageDto.coLocationItem1 == '0'}">checked="checked"</c:if> />
                <label class="form-check-label"><span class="check-circle"></span>No</label>
            </div>
            <span class="error-msg col-xs-12 form-group" name="iaisErrorMsg" id="error_coLocationItem1"></span>
        </div>
    </div>
    <div class="row">
        <div>
            <div class="col-xs-12 form-group">
                <iais:message key="DECLARATION_CO_LOCATION_ITME_2" escape="false"/>
            </div>
            <div class="form-check col-xs-3">
                <input class="form-check-input" disabled type="radio" name="coLocationItem2" value = "1" aria-invalid="false"
                       <c:if test="${appDeclarationMessageDto.coLocationItem2 == '1'}">checked="checked"</c:if> />
                <label class="form-check-label"><span class="check-circle"></span>Yes</label>
            </div>
            <div class="form-check form-group col-xs-3">
                <input class="form-check-input" disabled type="radio" name="coLocationItem2" value = "0" aria-invalid="false"
                       <c:if test="${appDeclarationMessageDto.coLocationItem2 == '0'}">checked="checked"</c:if> />
                <label class="form-check-label"><span class="check-circle"></span>No</label>
            </div>
            <span class="error-msg col-xs-12 form-group" name="iaisErrorMsg" id="error_coLocationItem2"></span>
        </div>
    </div>
</div>