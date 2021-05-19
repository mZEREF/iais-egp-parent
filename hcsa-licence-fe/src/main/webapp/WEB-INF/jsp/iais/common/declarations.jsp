<div class="panel panel-default">
  <div class="panel-heading" id="headingOne" role="tab">
    <h4 class="panel-title"><a class="collapsed a-panel-collapse" role="button" data-toggle="collapse" href="#declarations" aria-expanded="true" aria-controls="declarations" name="printControlNameForApp">Declarations</a></h4>
  </div>
  <div class="panel-collapse collapse <c:if test="${!empty printFlag}">in</c:if>" id="declarations" role="tabpanel" aria-labelledby="headingOne">
    <div class="panel-body">
      <%-- New Application --%>
      <c:if test="${AppSubmissionDto.appType == 'APTY002'}">
        <c:set var="pageShowFileDtos" value="${selectedNewFileDocShowPageDto.pageShowFileDtos}" scope="request"/>
        <c:set var="sec" value="New" scope="request"/>
        <%@include file="../newApplication/declarations/preliminaryQuestion.jsp"%>
        <%@include file="../newApplication/declarations/proofOfAuthorisationDocument.jsp"%>
        <%@include file="../newApplication/declarations/bankruptcy.jsp"%>
        <%@include file="../newApplication/declarations/competencies.jsp"%>
        <%@include file="../newApplication/declarations/crimAndPassApp.jsp"%>
        <%@include file="../newApplication/declarations/generalAccuracy.jsp"%>
      </c:if>
      <c:if test="${AppSubmissionDto.appType == 'APTY005' && RFC_eqHciNameChange=='RFC_eqHciNameChange'}">
        <c:set var="pageShowFileDtos" value="${selectedRFCFileDocShowPageDto.pageShowFileDtos}" scope="request"/>
        <c:set var="sec" value="RFC" scope="request"/>
        <%@include file="../newApplication/declarations/preliminaryQuestion.jsp"%>
        <%@include file="../newApplication/declarations/proofOfAuthorisationDocument.jsp"%>
        <%@include file="../newApplication/declarations/statements.jsp"%>
      </c:if>
      <c:if test="${renewDto.appSubmissionDtos.size()>=1 && renewDto.appSubmissionDtos[0].appType=='APTY004'}">
        <c:set value="${renewDto.appSubmissionDtos[0]}" var="AppSubmissionDto"></c:set>
        <c:set var="pageShowFileDtos" value="${selectedRENEWFileDocShowPageDto.pageShowFileDtos}" scope="request"/>
        <c:set var="sec" value="RENEW" scope="request"/>
        <%@include file="../newApplication/declarations/preliminaryQuestion.jsp"%>
        <%@include file="../newApplication/declarations/proofOfAuthorisationDocument.jsp"%>
        <%@include file="../newApplication/declarations/bankruptcy.jsp"%>
        <%@include file="../newApplication/declarations/competencies.jsp"%>
        <%@include file="../newApplication/declarations/crimAndPassApp.jsp"%>
        <%@include file="../newApplication/declarations/generalAccuracy.jsp"%>
      </c:if>
      <c:if test="${declaration_page_is == 'cessation' }">
        <c:set value="${appCessationDtos[0]}" var="AppSubmissionDto"></c:set>
        <%@include file="../newApplication/declarations/preliminaryQuestion.jsp"%>
        <%@include file="../newApplication/declarations/proofOfAuthorisationDocument.jsp"%>
        <%@include file="../newApplication/declarations/cessationQuestion.jsp"%>
      </c:if>
    </div>
  </div>
</div>
<script type="text/javascript">
  $(document).ready(function(){
    $('#declarations').find('.error-msg').on('DOMNodeInserted', function(){
      if ($(this).not(':empty')) {
        $('#declarations').collapse('show');
      }
    });
  });
</script>
