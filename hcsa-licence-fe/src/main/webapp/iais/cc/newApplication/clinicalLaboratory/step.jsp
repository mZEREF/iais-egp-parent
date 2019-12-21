

<h2 class="service-title">SERVICE 1 OF ${hcsaServiceDtoList.size()}: <strong>
  <c:forEach items="${hcsaServiceDtoList}" var="list">
    <c:if test ="${list.svcCode}==${currentSvcCode}">
      ${list.svcName}
    </c:if>
  </c:forEach>
</strong></h2>
<div class="visible-xs visible-sm servive-subtitle">
  <p>Step 1 of 5</p>
  <h3>Laboratory Disciplines</h3>
</div>
<ul class="progress-tracker">
  <c:forEach items="${hcsaServiceStepSchemeDtos}" var="steplist">
    <c:choose>
      <c:when test ="${steplist.isCurrent()}">
        <li class="tracker-item active" data-service-step="${steplist.stepCode}">${steplist.stepName}</li>
      </c:when>
     <c:otherwise>
      <li class="tracker-item disabled" data-service-step="${steplist.stepCode}">${steplist.stepName}</li>
     </c:otherwise>
    </c:choose>
  </c:forEach>
</ul>