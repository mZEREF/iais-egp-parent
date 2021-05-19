<c:set var="appDeclarationMessageDto" value="${appSubmissionDto.appDeclarationMessageDto}"/>

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
    <div class="form-check col-xs-3">
      <input disabled  class="form-check-input other-lic co-location" <c:if test="${appDeclarationMessageDto.criminalRecordsItem1=='1'}">checked="checked"</c:if> type="radio" name="criminalRecordsItem1" value = "1" aria-invalid="false">
      <label class="form-check-label" ><span class="check-circle"></span>Yes</label>
    </div>
    <div class="form-check col-xs-3">
      <input disabled class="form-check-input other-lic co-location" <c:if test="${appDeclarationMessageDto.criminalRecordsItem1=='0'}">checked="checked"</c:if> type="radio" name="criminalRecordsItem1" value = "0" aria-invalid="false">
      <label class="form-check-label" ><span class="check-circle"></span>NO</label>
    </div>
    <br>
    <p>The Applicant, PO, CGO(s), and KAHs have not been convicted or found guilty by a disciplinary tribunal of a professional body or its equivalent in Singapore or elsewhere.</p>
    <br>
    <div class="form-check col-xs-3">
      <input disabled class="form-check-input other-lic co-location" <c:if test="${appDeclarationMessageDto.criminalRecordsItem2=='1'}">checked="checked"</c:if> type="radio" name="criminalRecordsItem2" value = "1" aria-invalid="false">
      <label class="form-check-label" ><span class="check-circle"></span>Yes</label>
    </div>
    <div class="form-check col-xs-3">
      <input disabled class="form-check-input other-lic co-location" <c:if test="${appDeclarationMessageDto.criminalRecordsItem2=='0'}">checked="checked"</c:if> type="radio" name="criminalRecordsItem2" value = "0" aria-invalid="false">
      <label class="form-check-label" ><span class="check-circle"></span>NO</label>
    </div>
    <br>
    <p>The Applicant, PO, CGO(s), and KAHs are not awaiting the commencement of disciplinary proceedings before a disciplinary tribunal of a professional body or its equivalent, or the conclusion of such disciplinary proceedings, in Singapore or elsewhere.</p>
    <br>
    <div class="form-check col-xs-3">
      <input disabled class="form-check-input other-lic co-location" <c:if test="${appDeclarationMessageDto.criminalRecordsItem3=='1'}">checked="checked"</c:if> type="radio" name="criminalRecordsItem3" value = "1" aria-invalid="false">
      <label class="form-check-label" ><span class="check-circle"></span>Yes</label>
    </div>
    <div class="form-check col-xs-3">
      <input disabled class="form-check-input other-lic co-location" <c:if test="${appDeclarationMessageDto.criminalRecordsItem3=='0'}">checked="checked"</c:if> type="radio" name="criminalRecordsItem3" value = "0" aria-invalid="false">
      <label class="form-check-label" ><span class="check-circle"></span>NO</label>
    </div>
    <br>
    <p>The Applicant, PO, CGO(s), and KAHs are not awaiting the commencement of disciplinary proceedings before a disciplinary tribunal of a professional body or its equivalent, or the conclusion of such disciplinary proceedings, in Singapore or elsewhere.</p>
    <br>
    <div class="form-check col-xs-3">
      <input disabled class="form-check-input other-lic co-location" <c:if test="${appDeclarationMessageDto.criminalRecordsItem4=='1'}">checked="checked"</c:if> type="radio" name="criminalRecordsItem4" value = "1" aria-invalid="false">
      <label class="form-check-label" ><span class="check-circle"></span>Yes</label>
    </div>
    <div class="form-check col-xs-3">
      <input disabled class="form-check-input other-lic co-location" <c:if test="${appDeclarationMessageDto.criminalRecordsItem4=='0'}">checked="checked"</c:if> type="radio" name="criminalRecordsItem4" value = "0" aria-invalid="false">
      <label class="form-check-label" ><span class="check-circle"></span>NO</label>
    </div>
    <br>
    <div class="col-xs-12 txt-area-normal">
      <p>If you have selected 'Yes' to any of the questions above, please provide further details below:</p>
      <textarea disabled cols="85"  rows="5" name="criminalRecordsRemark" >${appDeclarationMessageDto.criminalRecordsRemark}</textarea>
    </div>

  </div>
</div>
