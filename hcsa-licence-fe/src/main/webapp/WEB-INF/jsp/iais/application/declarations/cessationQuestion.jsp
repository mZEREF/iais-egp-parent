<div class="panel-body">
    <div class="row">
    </div>
    <br>
    <div class="row">
        <p>This notice is submitted by the licensee at least one calendar month before the Effective Date stated above.</p>
        <div class="form-check col-xs-6">
            <input  class="form-check-input other-lic co-location"  type="radio" <c:if test="${!empty declaration_page_confirm}">disabled</c:if> <c:if test="${AppSubmissionDto.appDeclarationMessageDto.preliminaryQuestionItem1=='1'}">checked="checked"</c:if> name="isBefore" value = "1" aria-invalid="false">
            <label class="form-check-label" ><span class="check-circle"></span>Yes</label>
        </div>
        <div class="form-check col-xs-6">
            <input  class="form-check-input other-lic co-location"  type="radio" <c:if test="${!empty declaration_page_confirm}">disabled</c:if> <c:if test="${AppSubmissionDto.appDeclarationMessageDto.preliminaryQuestionItem1=='0'}">checked="checked"</c:if> name="isBefore" value = "0" aria-invalid="false">
            <label class="form-check-label" ><span class="check-circle"></span>No</label>
        </div>
        <span  class="error-msg" name="iaisErrorMsg" id="error_isBefore"></span>
        <p>The licensee has taken all reasonable measures to ensure the continuity of care of every patient that is affected by it stopping the provision of the licensable healthcare service to which the licence relates; stopping the usage of the premises or conveyance specified in the licence; or surrendering the licence.</p>
        <div class="form-check col-xs-6">
            <input  class="form-check-input other-lic co-location"  type="radio" <c:if test="${!empty declaration_page_confirm}">disabled</c:if> <c:if test="${AppSubmissionDto.appDeclarationMessageDto.preliminaryQuestiontem2=='1'}">checked="checked"</c:if> name="isSurrendering" value = "1" aria-invalid="false">
            <label class="form-check-label" ><span class="check-circle"></span>Yes</label>
        </div>
        <div class="form-check col-xs-6">
            <input  class="form-check-input other-lic co-location"  type="radio" <c:if test="${!empty declaration_page_confirm}">disabled</c:if> <c:if test="${AppSubmissionDto.appDeclarationMessageDto.preliminaryQuestiontem2=='0'}">checked="checked"</c:if> name="isSurrendering" value = "0" aria-invalid="false">
            <label class="form-check-label" ><span class="check-circle"></span>No</label>
        </div>
        <span  class="error-msg" name="iaisErrorMsg" id="error_isSurrendering"></span>
    </div>
</div>

