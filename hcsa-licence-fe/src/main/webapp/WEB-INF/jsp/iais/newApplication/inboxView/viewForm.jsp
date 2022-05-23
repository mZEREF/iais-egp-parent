<div class="panel panel-default">
  <div class="panel-heading" id="headingOne" role="tab">
    <h4 class="panel-title"><a class="collapsed" role="button" data-toggle="collapse" href="#collapseServer" aria-expanded="true" aria-controls="collapseOne">Service Related Information - ${hcsaServiceDto.svcName}</a></h4>
  </div>
    <div class="panel-collapse collapse " id="collapseServer" role="tabpanel" aria-labelledby="headingOne">
      <div class="panel-body">
            <div class="pop-up">
              <div class="pop-up-body">
                  <c:forEach items="${currentPreviewSvcInfo.hcsaServiceStepSchemeDtos}" var="hcsaServiceStepSchemeDto">
                    <c:set var="currStepName" value="${hcsaServiceStepSchemeDto.stepName}" scope="request"/>
                    <c:if test="${hcsaServiceStepSchemeDto.stepCode=='SVST001'}">
                      <jsp:include page="/WEB-INF/jsp/iais/common/previewSvcDisciplines.jsp"/>
                    </c:if><%--STEP_LABORATORY_DISCIPLINES--%>
                    <c:if test="${hcsaServiceStepSchemeDto.stepCode=='SVST002'}">
                      <jsp:include page="/WEB-INF/jsp/iais/common/previewSvcGovernanceOfficer.jsp"/>
                    </c:if><%--STEP_CLINICAL_GOVERNANCE_OFFICERS--%>
                    <c:if test="${hcsaServiceStepSchemeDto.stepCode=='SVST003'}">
                      <jsp:include page="/WEB-INF/jsp/iais/common/previewSvcAllocation.jsp"/>
                    </c:if><%--STEP_DISCIPLINE_ALLOCATION--%>
                    <c:if test="${hcsaServiceStepSchemeDto.stepCode=='SVST004'}">
                      <jsp:include page="/WEB-INF/jsp/iais/common/previewSvcPrincipalOfficers.jsp"/>
                    </c:if><%--STEP_PRINCIPAL_OFFICERS--%>
                    <c:if test="${hcsaServiceStepSchemeDto.stepCode=='SVST005'}">
                      <jsp:include page="/WEB-INF/jsp/iais/common/previewSvcDocument.jsp"/>
                    </c:if><%--STEP_DOCUMENTS--%>
                    <c:if test="${hcsaServiceStepSchemeDto.stepCode=='SVST006'}">
                      <jsp:include page="/WEB-INF/jsp/iais/common/previewSvcPerson.jsp"/>
                    </c:if><%--STEP_SERVICE_PERSONNEL--%>
                    <c:if test="${hcsaServiceStepSchemeDto.stepCode=='SVST007'}">
                      <jsp:include page="/WEB-INF/jsp/iais/common/previewMedAlert.jsp"/>
                    </c:if>
                    <c:if test="${hcsaServiceStepSchemeDto.stepCode=='SVST008'}">
                      <jsp:include page="/WEB-INF/jsp/iais/common/previewSvcVehicle.jsp"/>
                    </c:if>
                    <c:if test="${hcsaServiceStepSchemeDto.stepCode=='SVST009'}">
                      <jsp:include page="/WEB-INF/jsp/iais/common/previewSvcClinicalDirector.jsp"/>
                    </c:if>
                    <c:if test="${hcsaServiceStepSchemeDto.stepCode=='SVST010'}">
                      <jsp:include page="/WEB-INF/jsp/iais/common/previewSvcCharges.jsp"/>
                    </c:if>
                    <c:if test="${hcsaServiceStepSchemeDto.stepCode=='SVST012'}">
                      <jsp:include page="/WEB-INF/jsp/iais/common/previewSvcBusiness.jsp"/>
                    </c:if>
                    <c:if test="${hcsaServiceStepSchemeDto.stepCode=='SVST013'}">
                      <jsp:include page="/WEB-INF/jsp/iais/common/previewSectionLeader.jsp"/>
                    </c:if>
                    <c:if test="${hcsaServiceStepSchemeDto.stepCode=='SVST014'}">
                      <jsp:include page="/WEB-INF/jsp/iais/common/previewKeyAppointmentHolder.jsp"/>
                    </c:if>
                  </c:forEach>
              </div>
          </div>
      </div>
    </div>
</div>
