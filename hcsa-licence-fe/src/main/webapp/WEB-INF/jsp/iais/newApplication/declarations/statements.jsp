<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div class="panel-body" >
    <div class="row">
      <p>Please indicate 'Yes' or 'No' to the following statements:</p>
      <br>
      <p>1.  The new business name ("Chosen Business Name") by which the Applicant intends to provide or continue to provide the licensable healthcare service is not prohibited under section 29 of the Healthcare Services Act 2020 or any written law or other law.<p></p>
      <br>
      <div class="form-check col-xs-3">
        <input  class="form-check-input other-lic co-location" <c:if test="${AppSubmissionDto.appDeclarationMessageDto.preliminaryQuestionItem1=='1'}">checked="checked"</c:if> type="radio" name="preliminaryQuestionItem1" value = "1" aria-invalid="false">
        <label class="form-check-label" ><span class="check-circle"></span>Yes</label>
      </div>
      <div class="form-check col-xs-9">
        <input  class="form-check-input other-lic co-location" <c:if test="${AppSubmissionDto.appDeclarationMessageDto.preliminaryQuestionItem1=='0'}">checked="checked"</c:if>  type="radio" name="preliminaryQuestionItem1" value = "0" aria-invalid="false">
        <label class="form-check-label" ><span class="check-circle"></span>No</label>
      </div>
      <span  class="error-msg" name="iaisErrorMsg" id="error_preliminaryQuestionItem1"></span>
      <br>
      <p>2.  Apart from the proposed change in the business name to the Chosen Business Name, there are no amendments to any other particulars or information specified in the licence.</p>
      <br>
      <div class="form-check col-xs-3">
        <input  class="form-check-input other-lic co-location" <c:if test="${AppSubmissionDto.appDeclarationMessageDto.preliminaryQuestiontem2=='1'}">checked="checked"</c:if> type="radio" name="preliminaryQuestiontem2" value = "1" aria-invalid="false">
        <label class="form-check-label" ><span class="check-circle"></span>Yes</label>
      </div>
      <div class="form-check col-xs-9">
        <input  class="form-check-input other-lic co-location" <c:if test="${AppSubmissionDto.appDeclarationMessageDto.preliminaryQuestiontem2=='0'}">checked="checked"</c:if>  type="radio" name="preliminaryQuestiontem2" value = "0" aria-invalid="false">
        <label class="form-check-label" ><span class="check-circle"></span>No</label>
      </div>
      <span  class="error-msg" name="iaisErrorMsg" id="error_preliminaryQuestiontem2"></span>
      <br>
      <p>Please note that a successful application in respect of any business name does not confer on the Applicant any property in the business name, or in any word or expression that constitutes or is included in the business name, nor should it be taken as evidence that the Applicant owns any property or right or interest in any property held on account of, or used for the purposes of the business.</p>
      <br>
      <hr>
      <br>
      <p class="form-check col-md-8 col-lg-9 col-xs-12">Please indicate the date which you would like the changes to be effective
        (subject to approval). If not indicated, the effective date will be the approval date of the change.</p>
      <div class="col-md-4 col-lg-3 col-xs-12">
        <input type="text" autocomplete="off" value="<fmt:formatDate value="${AppSubmissionDto.appDeclarationMessageDto.effectiveDt}" pattern="dd/MM/yyyy"/>"  class="date_picker form-control form_datetime" name="effectiveDt" id="-20247433206800" data-date-start-date="01/01/1900" placeholder="dd/mm/yyyy" maxlength="10">
        <span class="error-msg" name="iaisErrorMsg" id="error_effectiveDt"></span>
      </div>
    </div>

</div>


