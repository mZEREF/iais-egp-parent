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
<div class="panel-body">
    <div class="row">
        <h2>Declaration on Criminal Records and Past Suspension/ Revocation under PHMCA/HCSA</h2>
    </div>
    <br>
    <div class="row">
        <div class="col-xs-12">Please indicate ‘Yes’ or ‘No’ to the following statements:</div>
        <br>
        <div class="col-xs-12">
            <div class="col-xs-12">
                <span>1.</span>
                <span>
                   The Applicant, PO, CGO(s), and KAHs do not have any criminal record or its equivalent in Singapore or elsewhere.
                </span>
            </div>
            <div class="form-check col-xs-3">
                <input class="form-check-input" type="radio" name="criminalRecordsItem1" value = "0" aria-invalid="false">
                <label class="form-check-label"><span class="check-circle"></span>Yes</label>
            </div>
            <div class="form-check col-xs-3">
                <input class="form-check-input" type="radio" name="criminalRecordsItem1" value = "1" aria-invalid="false">
                <label class="form-check-label"><span class="check-circle"></span>No</label>
            </div>
        </div>
        <div class="col-xs-12">
            <div class="col-xs-12">
                <span>2.</span>
                <span>
                    The Applicant, PO, CGO(s), and KAHs have not been convicted or found guilty by a disciplinary tribunal of a professional body or its equivalent in Singapore or elsewhere.
                </span>
            </div>
            <div class="form-check col-xs-3">
                <input class="form-check-input" type="radio" name="criminalRecordsItem2" value = "0" aria-invalid="false">
                <label class="form-check-label"><span class="check-circle"></span>Yes</label>
            </div>
            <div class="form-check col-xs-3">
                <input class="form-check-input" type="radio" name="criminalRecordsItem2" value = "1" aria-invalid="false">
                <label class="form-check-label" ><span class="check-circle"></span>No</label>
            </div>
        </div>
        <div class="col-xs-12">
            <div class="col-xs-12">
                <span>3.</span>
                <span>
                    The Applicant, PO, CGO(s), and KAHs are not awaiting the commencement of disciplinary proceedings before a disciplinary tribunal of a professional body or its equivalent, or the conclusion of such disciplinary proceedings, in Singapore or elsewhere.
                </span>
            </div>
            <div class="form-check col-xs-3">
                <input class="form-check-input" type="radio" name="criminalRecordsItem3" value = "0" aria-invalid="false">
                <label class="form-check-label"><span class="check-circle"></span>Yes</label>
            </div>
            <div class="form-check col-xs-3">
                <input class="form-check-input" type="radio" name="criminalRecordsItem3" value = "1" aria-invalid="false">
                <label class="form-check-label"><span class="check-circle"></span>No</label>
            </div>
        </div>
        <div class="col-xs-12">
            <div class="col-xs-12">
                <span>4.</span>
                <span>
                    The Applicant, PO, CGO(s), and KAHs have not had any license, granted to it by the Director of Medical Services under the Private Hospitals and Medical Clinics Act (Cap. 248), and the Healthcare Services Act 2020 (No. 3 of 2020), revoked or suspended.
                </span>
            </div>
            <div class="form-check col-xs-3">
                <input class="form-check-input" type="radio" name="criminalRecordsItem4" value = "0" aria-invalid="false">
                <label class="form-check-label"><span class="check-circle"></span>Yes</label>
            </div>
            <div class="form-check col-xs-3">
                <input class="form-check-input" type="radio" name="criminalRecordsItem4" value = "1" aria-invalid="false">
                <label class="form-check-label"><span class="check-circle"></span>No</label>
            </div>
        </div>
        <div class="col-xs-12 txt-area-normal">
            <p>If you have selected ‘Yes’ to any of the questions above, please provide further details below:</p>
            <textarea id=" criminalRecordsRemark" cols="85" rows="5" name="criminalRecordsRemark"></textarea>
        </div>
    </div>
</div>
