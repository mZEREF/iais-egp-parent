<c:set var="appDeclarationMessageDto" value="${AppSubmissionDto.appDeclarationMessageDto}"/>

<div class="panel-body">
    <div class="row">
        <h2>General Accuracy Declaration</h2>
    </div>
    <br>
    <div class="row">
        <div>
            <div class="col-xs-12 form-group">
                <iais:message key="DECLARATION_GENERAL_ACCURACY_ITME_1" escape="false"/>
            </div>
            <div class="form-check col-xs-3">
                <input class="form-check-input" type="radio" name="generalAccuracyItem1" value = "1" aria-invalid="false"
                    <c:if test="${appDeclarationMessageDto.generalAccuracyItem1 == '1'}">checked="checked"</c:if> />
                <label class="form-check-label"><span class="check-circle"></span>Yes</label>
            </div>
            <div class="form-check form-group col-xs-3">
                <input class="form-check-input" type="radio" name="generalAccuracyItem1" value = "0" aria-invalid="false"
                       <c:if test="${appDeclarationMessageDto.generalAccuracyItem1 == '0'}">checked="checked"</c:if> />
                <label class="form-check-label"><span class="check-circle"></span>No</label>
            </div>
            <span class="error-msg col-xs-12 form-group" name="iaisErrorMsg" id="error_generalAccuracyItem1"></span>
        </div>
    </div>
</div>
