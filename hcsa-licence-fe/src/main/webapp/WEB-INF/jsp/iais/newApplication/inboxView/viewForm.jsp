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
                      <%@include file="../../common/previewSvcDisciplines.jsp"%>
                    </c:if><%--STEP_LABORATORY_DISCIPLINES--%>
                    <c:if test="${hcsaServiceStepSchemeDto.stepCode=='SVST002'}">
                      <%@include file="../../common/previewSvcGovernanceOfficer.jsp"%>
                    </c:if><%--STEP_CLINICAL_GOVERNANCE_OFFICERS--%>
                    <c:if test="${hcsaServiceStepSchemeDto.stepCode=='SVST003'}">
                      <%@include file="../../common/previewSvcAllocation.jsp"%>
                    </c:if><%--STEP_DISCIPLINE_ALLOCATION--%>
                    <c:if test="${hcsaServiceStepSchemeDto.stepCode=='SVST004'}">
                      <%@include file="../../common/previewSvcPrincipalOfficers.jsp"%>
                    </c:if><%--STEP_PRINCIPAL_OFFICERS--%>
                    <c:if test="${hcsaServiceStepSchemeDto.stepCode=='SVST005'}">
                      <%@include file="../../common/previewSvcDocument.jsp"%>
                    </c:if><%--STEP_DOCUMENTS--%>
                    <c:if test="${hcsaServiceStepSchemeDto.stepCode=='SVST006'}">
                      <%@include file="../../common/previewSvcPerson.jsp"%>
                    </c:if><%--STEP_SERVICE_PERSONNEL--%>
                    <c:if test="${hcsaServiceStepSchemeDto.stepCode=='SVST007'}">
                      <%@include file="../../common/previewMedAlert.jsp"%>
                    </c:if>
                    <c:if test="${hcsaServiceStepSchemeDto.stepCode=='SVST008'}">
                      <%@include file="../../common/previewSvcVehicle.jsp"%>
                    </c:if>
                    <c:if test="${hcsaServiceStepSchemeDto.stepCode=='SVST009'}">
                      <%@include file="../../common/previewSvcClinicalDirector.jsp"%>
                    </c:if>
                    <c:if test="${hcsaServiceStepSchemeDto.stepCode=='SVST010'}">
                      <%@include file="../../common/previewSvcCharges.jsp"%>
                    </c:if>
                    <c:if test="${hcsaServiceStepSchemeDto.stepCode=='SVST012'}">
                      <%@include file="../../common/previewSvcBusiness.jsp"%>
                    </c:if>
                    <c:if test="${hcsaServiceStepSchemeDto.stepCode=='SVST013'}">
                      <%@include file="../../common/previewSectionLeader.jsp"%>
                    </c:if>
                    <c:if test="${hcsaServiceStepSchemeDto.stepCode=='SVST014'}">
                      <%@include file="../../common/previewKeyAppointmentHolder.jsp"%>
                    </c:if>
                  </c:forEach>
              </div>
          </div>
      </div>
    </div>
</div>
