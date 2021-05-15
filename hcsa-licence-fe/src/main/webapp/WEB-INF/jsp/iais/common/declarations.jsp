<div class="panel panel-default">
  <div class="panel-heading" id="headingOne" role="tab">
    <h4 class="panel-title"><a class="collapsed a-panel-collapse" role="button" data-toggle="collapse" href="#declarations" aria-expanded="true" aria-controls="declarations" name="printControlNameForApp">Declarations</a></h4>
  </div>
  <div class="panel-collapse collapse <c:if test="${!empty printFlag}">in</c:if>" id="declarations" role="tabpanel" aria-labelledby="headingOne">
    <div class="panel-body">
      <c:if test="${AppSubmissionDto.appType == 'APTY005' && RFC_eqHciNameChange=='RFC_eqHciNameChange'}">
        <%@include file="../newApplication/declarations/preliminaryQuestion.jsp"%>
        <%@include file="../newApplication/declarations/statements.jsp"%>
      </c:if>
      <c:if test="${renewDto.appSubmissionDtos.size()>=1 && renewDto.appSubmissionDtos[0].appType=='APTY004'}">
        <%@include file="../newApplication/declarations/preliminaryQuestion.jsp"%>
        <%@include file="../newApplication/declarations/bankruptcy.jsp"%>
        <%@include file="../newApplication/declarations/competencies.jsp"%>
        <%@include file="../newApplication/declarations/crimAndPassApp.jsp"%>
      </c:if>
    </div>
  </div>
</div>

