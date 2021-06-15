<div class="panel-body">
    <div class="row">
      <h2>Preliminary Question</h2>
    </div>
    <br>
    <div class="row">
      <div class="form-check col-xs-12">
        <P>Any person ("<strong>Applicant</strong>") that wishes to be granted a licence or to renew an existing licence under the Healthcare Services Act 2020 must apply to the Director of Medical Services. If the application is granted, the Applicant will hold the licence or renewed licence, as the case may be, as the licensee.</P>
      </div>
      <br>
      <div class="form-check col-xs-12">
        <p> Kindly select one of the following:</p>
      </div>
      <br>
      <div class="form-check col-xs-12">
        <input disabled class="form-check-input other-lic co-location" <c:if test="${appSubmissionDto.appDeclarationMessageDto.preliminaryQuestionKindly=='1'}">checked="checked"</c:if> type="radio" name="preliminaryQuestionKindly" value = "1" aria-invalid="false">
        <label class="form-check-label" ><span class="check-circle"></span>I am the Applicant and I will be the licensee if the application is granted; or</label>
      </div>
      <div class="form-check col-xs-12">
        <input disabled class="form-check-input other-lic co-location" <c:if test="${appSubmissionDto.appDeclarationMessageDto.preliminaryQuestionKindly=='0'}">checked="checked"</c:if> type="radio" name="preliminaryQuestionKindly" value = "0" aria-invalid="false">
        <label class="form-check-label" ><span class="check-circle"></span>I am duly authorised by the Applicant to make this application on its behalf and the Applicant will be the licensee if the application is granted. </label>
      </div>
    </div>
</div>

