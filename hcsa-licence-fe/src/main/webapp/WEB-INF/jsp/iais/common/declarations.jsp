<div class="panel panel-default">
  <div class="panel-heading" id="headingOne" role="tab">
    <h4 class="panel-title"><a class="collapsed a-panel-collapse" role="button" data-toggle="collapse" href="#declarations" aria-expanded="true" aria-controls="declarations" name="printControlNameForApp">Declarations</a></h4>
  </div>
  <div class="panel-collapse collapse <c:if test="${!empty printFlag}">in</c:if>" id="declarations" role="tabpanel" aria-labelledby="headingOne">
    <div class="panel-body">
      <%@include file="../newApplication/declarations/preliminaryQuestion.jsp"%>
      <%@include file="../newApplication/declarations/statements.jsp"%>
    </div>
  </div>
</div>

