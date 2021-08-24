<iais:input cssClass="not-clear" type="hidden" name="currentStep" value="${currentStep}"/>

<c:if test="${hcsaServiceDtoList.size()>1}">
<h2 class="service-title">SERVICE ${serviceStepDto.serviceNumber+1} OF ${hcsaServiceDtoList.size()}: <strong>
  <c:forEach items="${hcsaServiceDtoList}" var="list">
    <c:if test ="${list.svcCode==currentSvcCode}">
      ${list.svcName}
    </c:if>
  </c:forEach>
</strong></h2>
</c:if>

<div class="visible-xs visible-sm servive-subtitle">
  <p>Step ${serviceStepDto.currentNumber+1} of ${serviceStepDto.hcsaServiceStepSchemeDtos.size()}</p>
  <h3>${serviceStepDto.currentStep.stepName}</h3>
</div>
<ul class="progress-tracker">
  <c:forEach items="${serviceStepDto.hcsaServiceStepSchemeDtos}" var="steplist" varStatus="status">
    <c:choose>
      <c:when test ="${serviceStepDto.currentNumber>=status.index}">
        <li class="tracker-item active" data-service-step="${steplist.stepCode}">${steplist.stepName}</li>
      </c:when>
     <c:otherwise>
      <li class="tracker-item disabled" data-service-step="${steplist.stepCode}">${steplist.stepName}</li>
     </c:otherwise>
    </c:choose>
  </c:forEach>
</ul>