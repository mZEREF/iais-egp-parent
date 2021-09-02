<div class="panel-body">
  <%-- New Application --%>
  <c:if test="${appSubmissionDto.appType == 'APTY002'}">
      <%@include file="../declarations/preliminaryQuestion.jsp"%>
      <%@include file="../declarations/document.jsp"%>
      <%@include file="../declarations/bankruptcy.jsp"%>
      <%@include file="../declarations/competencies.jsp"%>
      <%@include file="../declarations/crimAndPassApp.jsp"%>
      <%@include file="../declarations/generalAccuracy.jsp"%>
  </c:if>
  <c:if test="${appSubmissionDto.appType == 'APTY005'&&RFC_HCAI_NAME_CHNAGE=='false'}">
    <%@include file="../declarations/preliminaryQuestion.jsp"%>
    <%@include file="../declarations/document.jsp"%>
    <%@include file="../declarations/statements.jsp"%>
  </c:if>

  <c:if test="${appSubmissionDto.appType == 'APTY005'&&renew_rfc_show=='Y'}">
    <%@include file="../declarations/preliminaryQuestion.jsp"%>
    <%@include file="../declarations/document.jsp"%>
    <%@include file="../declarations/bankruptcy.jsp"%>
    <%@include file="../declarations/competencies.jsp"%>
    <%@include file="../declarations/crimAndPassApp.jsp"%>
    <%@include file="../declarations/generalAccuracy.jsp"%>
  </c:if>
  <c:if test="${appSubmissionDto.appType == 'APTY004'}">
    <%@include file="../declarations/preliminaryQuestion.jsp"%>
    <%@include file="../declarations/document.jsp"%>
    <%@include file="../declarations/bankruptcy.jsp"%>
    <%@include file="../declarations/competencies.jsp"%>
    <%@include file="../declarations/crimAndPassApp.jsp"%>
    <%@include file="../declarations/generalAccuracy.jsp"%>
  </c:if>
</div>
<script type="text/javascript">
  $(document).ready(function(){
    $('#declarations').find('.error-msg').on('DOMNodeInserted', function(){
      console.log($(this).html() + " - " + $(this).text());
      if ($(this).not(':empty')) {
        $('#declarations').collapse('show');
      }
    });
  });
</script>
<style>
  span ,p {
      font-size: 16px;
  }
</style>
