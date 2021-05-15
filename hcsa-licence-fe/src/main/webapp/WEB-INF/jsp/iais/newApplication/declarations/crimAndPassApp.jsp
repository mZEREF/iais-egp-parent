<div class="panel-body">
  <div class="row">
    <h2>Declaration on Criminal Records and Past Suspension/ Revocation under PHMCA/HCSA</h2>
  </div>
  <br>
  <div class="row">
    <P>Please indicate 'Yes' or 'No' to the following statements:</P>
    <br>
    <p>The Applicant, PO, CGO(s), and KAHs do not have any criminal record or its equivalent in Singapore or elsewhere.</p>
    <br>
    <div class="form-check col-xs-12">
      <input  class="form-check-input other-lic co-location" <c:if test="${AppSubmissionDto.appDeclarationMessageDto.criminalRecordsItem1=='1'}">checked="checked"</c:if> type="radio" name="criminalRecordsItem1" value = "1" aria-invalid="false">
      <label class="form-check-label" ><span class="check-circle"></span>Yes</label>
    </div>
    <div class="form-check col-xs-12">
      <input  class="form-check-input other-lic co-location" <c:if test="${AppSubmissionDto.appDeclarationMessageDto.criminalRecordsItem1=='0'}">checked="checked"</c:if> type="radio" name="criminalRecordsItem1" value = "0" aria-invalid="false">
      <label class="form-check-label" ><span class="check-circle"></span>NO</label>
    </div>
    <span  class="error-msg" name="iaisErrorMsg" id="error_criminalRecordsItem1"></span>
    <br>
    <p>The Applicant, PO, CGO(s), and KAHs have not been convicted or found guilty by a disciplinary tribunal of a professional body or its equivalent in Singapore or elsewhere.</p>
    <br>
    <div class="form-check col-xs-12">
      <input  class="form-check-input other-lic co-location" <c:if test="${AppSubmissionDto.appDeclarationMessageDto.criminalRecordsItem2=='1'}">checked="checked"</c:if> type="radio" name="criminalRecordsItem2" value = "1" aria-invalid="false">
      <label class="form-check-label" ><span class="check-circle"></span>Yes</label>
    </div>
    <div class="form-check col-xs-12">
      <input  class="form-check-input other-lic co-location" <c:if test="${AppSubmissionDto.appDeclarationMessageDto.criminalRecordsItem2=='0'}">checked="checked"</c:if> type="radio" name="criminalRecordsItem2" value = "0" aria-invalid="false">
      <label class="form-check-label" ><span class="check-circle"></span>NO</label>
    </div>
    <span  class="error-msg" name="iaisErrorMsg" id="error_criminalRecordsItem2"></span>
    <br>
    <p>The Applicant, PO, CGO(s), and KAHs are not awaiting the commencement of disciplinary proceedings before a disciplinary tribunal of a professional body or its equivalent, or the conclusion of such disciplinary proceedings, in Singapore or elsewhere.</p>
    <br>
    <div class="form-check col-xs-12">
      <input  class="form-check-input other-lic co-location" <c:if test="${AppSubmissionDto.appDeclarationMessageDto.criminalRecordsItem3=='1'}">checked="checked"</c:if> type="radio" name="criminalRecordsItem3" value = "1" aria-invalid="false">
      <label class="form-check-label" ><span class="check-circle"></span>Yes</label>
    </div>
    <div class="form-check col-xs-12">
      <input  class="form-check-input other-lic co-location" <c:if test="${AppSubmissionDto.appDeclarationMessageDto.criminalRecordsItem3=='0'}">checked="checked"</c:if> type="radio" name="criminalRecordsItem3" value = "0" aria-invalid="false">
      <label class="form-check-label" ><span class="check-circle"></span>NO</label>
    </div>
    <span  class="error-msg" name="iaisErrorMsg" id="error_criminalRecordsItem3"></span>
    <br>
    <p>The Applicant, PO, CGO(s), and KAHs are not awaiting the commencement of disciplinary proceedings before a disciplinary tribunal of a professional body or its equivalent, or the conclusion of such disciplinary proceedings, in Singapore or elsewhere.</p>
    <br>
    <div class="form-check col-xs-12">
      <input  class="form-check-input other-lic co-location" <c:if test="${AppSubmissionDto.appDeclarationMessageDto.criminalRecordsItem4=='1'}">checked="checked"</c:if> type="radio" name="criminalRecordsItem4" value = "1" aria-invalid="false">
      <label class="form-check-label" ><span class="check-circle"></span>Yes</label>
    </div>
    <div class="form-check col-xs-12">
      <input  class="form-check-input other-lic co-location" <c:if test="${AppSubmissionDto.appDeclarationMessageDto.criminalRecordsItem4=='0'}">checked="checked"</c:if> type="radio" name="criminalRecordsItem4" value = "0" aria-invalid="false">
      <label class="form-check-label" ><span class="check-circle"></span>NO</label>
    </div>
    <span  class="error-msg" name="iaisErrorMsg" id="error_criminalRecordsItem4"></span>
    <br>
    <p>If you have selected 'Yes' to any of the questions above, please provide further details below:</p>
    <br>
    <div class="form-check col-xs-12">
      <input   type="text" name="criminalRecordsRemark" >
      <span  class="error-msg" name="iaisErrorMsg" id="error_criminalRecordsRemark"></span>
    </div>

  </div>
</div>
