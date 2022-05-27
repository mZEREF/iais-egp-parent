<c:set var="appDeclarationMessageDto" value="${appDeclarationMessageDto}"/>

<div class="panel-body">
  <div class="row">
    <h2>Declaration on Criminal Records and Past Suspension/ Revocation under PHMCA/HCSA</h2>
  </div>
  <br>
  <div class="row">
    <P class="col-xs-12">Please indicate 'Yes' or 'No' to the following statements:</P>
    <br>
    <p class="col-xs-12">
      <span>1.</span>
      <span><iais:message key="DECLARATION_CRIM_AND_PASS_APP_ITME_1" escape="false"/></span>
    </p>
    <br>
    <div class="form-check col-xs-3">
      <input  class="form-check-input other-lic co-location" <c:if test="${appDeclarationMessageDto.criminalRecordsItem1=='1'}">checked="checked"</c:if> type="radio" name="criminalRecordsItem1" value = "1" aria-invalid="false">
      <label class="form-check-label" ><span class="check-circle"></span>Yes</label>
    </div>
    <div class="form-check col-xs-3">
      <input  class="form-check-input other-lic co-location" <c:if test="${appDeclarationMessageDto.criminalRecordsItem1=='0'}">checked="checked"</c:if> type="radio" name="criminalRecordsItem1" value = "0" aria-invalid="false">
      <label class="form-check-label" ><span class="check-circle"></span>No</label>
    </div>
    <span  class="error-msg col-xs-12" name="iaisErrorMsg" id="error_criminalRecordsItem1"></span>
    <br>
    <p class="col-xs-12">
      <span>2.</span>
      <span><iais:message key="DECLARATION_CRIM_AND_PASS_APP_ITME_2" escape="false"/></span>
    </p>
    <br>
    <div class="form-check col-xs-3">
      <input  class="form-check-input other-lic co-location" <c:if test="${appDeclarationMessageDto.criminalRecordsItem2=='1'}">checked="checked"</c:if> type="radio" name="criminalRecordsItem2" value = "1" aria-invalid="false">
      <label class="form-check-label" ><span class="check-circle"></span>Yes</label>
    </div>
    <div class="form-check col-xs-3">
      <input  class="form-check-input other-lic co-location" <c:if test="${appDeclarationMessageDto.criminalRecordsItem2=='0'}">checked="checked"</c:if> type="radio" name="criminalRecordsItem2" value = "0" aria-invalid="false">
      <label class="form-check-label" ><span class="check-circle"></span>No</label>
    </div>
    <span  class="error-msg col-xs-12" name="iaisErrorMsg" id="error_criminalRecordsItem2"></span>
    <br>
    <p class="col-xs-12">
      <span>3.</span>
      <span><iais:message key="DECLARATION_CRIM_AND_PASS_APP_ITME_3" escape="false"/></span>
    </p>
    <br>
    <div class="form-check col-xs-3">
      <input  class="form-check-input other-lic co-location" <c:if test="${appDeclarationMessageDto.criminalRecordsItem3=='1'}">checked="checked"</c:if> type="radio" name="criminalRecordsItem3" value = "1" aria-invalid="false">
      <label class="form-check-label" ><span class="check-circle"></span>Yes</label>
    </div>
    <div class="form-check col-xs-3">
      <input  class="form-check-input other-lic co-location" <c:if test="${appDeclarationMessageDto.criminalRecordsItem3=='0'}">checked="checked"</c:if> type="radio" name="criminalRecordsItem3" value = "0" aria-invalid="false">
      <label class="form-check-label" ><span class="check-circle"></span>No</label>
    </div>
    <span  class="error-msg col-xs-12" name="iaisErrorMsg" id="error_criminalRecordsItem3"></span>
    <br>
    <p class="col-xs-12">
      <span>4.</span>
      <span><iais:message key="DECLARATION_CRIM_AND_PASS_APP_ITME_4" escape="false"/></span>
    </p>
    <br>
    <div class="form-check col-xs-3">
      <input  class="form-check-input other-lic co-location" <c:if test="${appDeclarationMessageDto.criminalRecordsItem4=='1'}">checked="checked"</c:if> type="radio" name="criminalRecordsItem4" value = "1" aria-invalid="false">
      <label class="form-check-label" ><span class="check-circle"></span>Yes</label>
    </div>
    <div class="form-check col-xs-3">
      <input  class="form-check-input other-lic co-location" <c:if test="${appDeclarationMessageDto.criminalRecordsItem4=='0'}">checked="checked"</c:if> type="radio" name="criminalRecordsItem4" value = "0" aria-invalid="false">
      <label class="form-check-label" ><span class="check-circle"></span>No</label>
    </div>
    <span  class="error-msg col-xs-12" name="iaisErrorMsg" id="error_criminalRecordsItem4"></span>
    <br>
    <div class="col-xs-12 txt-area-normal">
      <p>If you have selected 'No' to any of the questions above, please provide further details below:</p>
      <textarea id="criminalRecordsRemark" maxlength="1000" class="form-control" name="criminalRecordsRemark" ><c:out value="${appDeclarationMessageDto.criminalRecordsRemark}" /></textarea>
      <span class="error-msg col-xs-12" name="iaisErrorMsg" id="error_criminalRecordsRemark"></span>
    </div>

  </div>
</div>
