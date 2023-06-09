<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div class="panel-body" >
    <div class="row">
      <p >Please indicate 'Yes' or 'No' to the following statements:</p>
      <br>
      <p >
        <span>1.</span>
        <span>The new business name ("Chosen Business Name") by which the Applicant intends to provide or continue to provide the licensable healthcare service is not prohibited under section 29 of the Healthcare Services Act 2020 or any written law or other law</span>
      </p>
      <br>
      <div class="form-check col-xs-3">
        <input disabled class="form-check-input other-lic co-location" <c:if test="${appSubmissionDto.appDeclarationMessageDto.preliminaryQuestionItem1=='1'}">checked="checked"</c:if> type="radio" name="preliminaryQuestionItem1" value = "1" aria-invalid="false">
        <label class="form-check-label" ><span class="check-circle"></span>Yes</label>
      </div>
      <div class="form-check col-xs-9">
        <input disabled class="form-check-input other-lic co-location" <c:if test="${appSubmissionDto.appDeclarationMessageDto.preliminaryQuestionItem1=='0'}">checked="checked"</c:if>  type="radio" name="preliminaryQuestionItem1" value = "0" aria-invalid="false">
        <label class="form-check-label" ><span class="check-circle"></span>No</label>
      </div>
      <br>
      <p >
        <span>2.</span>
        <span>Apart from the proposed change in the business name to the Chosen Business Name, there are no amendments to any other particulars or information specified in the licence.</span>
      </p>
      <br>
      <div class="form-check col-xs-3">
        <input disabled class="form-check-input other-lic co-location" <c:if test="${appSubmissionDto.appDeclarationMessageDto.preliminaryQuestiontem2=='1'}">checked="checked"</c:if> type="radio" name="preliminaryQuestiontem2" value = "1" aria-invalid="false">
        <label class="form-check-label" ><span class="check-circle"></span>Yes</label>
      </div>
      <div class="form-check col-xs-9">
        <input disabled class="form-check-input other-lic co-location" <c:if test="${appSubmissionDto.appDeclarationMessageDto.preliminaryQuestiontem2=='0'}">checked="checked"</c:if>  type="radio" name="preliminaryQuestiontem2" value = "0" aria-invalid="false">
        <label class="form-check-label" ><span class="check-circle"></span>No</label>
      </div>
      <br>
      <p >
        <span>Please note that a successful application in respect of any business name does not confer on the Applicant any property in the business name, or in any word or expression that constitutes or is included in the business name, nor should it be taken as evidence that the Applicant owns any property or right or interest in any property held on account of, or used for the purposes of the business.</span>
      </p>
      <br>
      <hr>
      <br>
      <p class="form-check col-xs-9">Please indicate the date which you would like the changes to be effective (subject to approval). If not indicated, the effective date will be the approval date of the change.</p>
      <div  class="col-xs-3">
        <input disabled type="text" value="<fmt:formatDate value="${appSubmissionDto.appDeclarationMessageDto.effectiveDt}" pattern="dd/MM/yyyy"></fmt:formatDate>" />
      </div>
    </div>

</div>


