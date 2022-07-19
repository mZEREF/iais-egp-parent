<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<style type="text/css">
  table {
    table-layout: fixed;
    word-break: break-all;
  }
</style>
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
                    <c:set var="stepCode" value="${hcsaServiceStepSchemeDto.stepCode}"/>
                    <c:choose>
                    <%--<c:when test="${stepCode=='SVST001'}">
                      <jsp:include page="/WEB-INF/jsp/iais/common/previewSvcDisciplines.jsp"/>
                    </c:when>--%>
                    <c:when test="${stepCode=='SVST002'}">
                      <jsp:include page="/WEB-INF/jsp/iais/view/keyPersonnel/viewSvcGovernanceOfficer.jsp"/>
                    </c:when><%--STEP_CLINICAL_GOVERNANCE_OFFICERS--%>
                    <%--<c:when test="${stepCode=='SVST003'}">
                      <jsp:include page="/WEB-INF/jsp/iais/common/previewSvcAllocation.jsp"/>
                    </c:when>--%>
                    <c:when test="${stepCode=='SVST004'}">
                      <jsp:include page="/WEB-INF/jsp/iais/view/keyPersonnel/viewSvcPrincipalOfficers.jsp"/>
                    </c:when><%--STEP_PRINCIPAL_OFFICERS--%>
                    <c:when test="${stepCode=='SVST005'}">
                      <jsp:include page="/WEB-INF/jsp/iais/view/document/viewSvcDocument.jsp"/>
                    </c:when><%--STEP_DOCUMENTS--%>
                    <c:when test="${stepCode=='SVST006'}">
                      <jsp:include page="/WEB-INF/jsp/iais/view/svcPersonnel/viewSvcPerson.jsp"/>
                    </c:when><%--STEP_SERVICE_PERSONNEL--%>
                    <c:when test="${stepCode=='SVST007'}">
                      <jsp:include page="/WEB-INF/jsp/iais/view/keyPersonnel/viewMedAlert.jsp"/>
                    </c:when>
                    <c:when test="${stepCode=='SVST008'}">
                      <jsp:include page="/WEB-INF/jsp/iais/view/others/viewSvcVehicle.jsp"/>
                    </c:when>
                    <c:when test="${stepCode=='SVST009'}">
                      <jsp:include page="/WEB-INF/jsp/iais/view/keyPersonnel/viewSvcClinicalDirector.jsp"/>
                    </c:when>
                    <c:when test="${stepCode=='SVST010'}">
                      <jsp:include page="/WEB-INF/jsp/iais/view/others/viewSvcCharges.jsp"/>
                    </c:when>
                    <c:when test="${stepCode=='SVST012'}">
                      <jsp:include page="/WEB-INF/jsp/iais/view/businessInfo/viewSvcBusiness.jsp"/>
                    </c:when>
                    <c:when test="${stepCode=='SVST013'}">
                      <jsp:include page="/WEB-INF/jsp/iais/view/svcPersonnel/viewSectionLeader.jsp"/>
                    </c:when>
                    <c:when test="${stepCode=='SVST014'}">
                      <jsp:include page="/WEB-INF/jsp/iais/view/keyPersonnel/viewKeyAppointmentHolder.jsp"/>
                    </c:when>
                    </c:choose>
                  </c:forEach>
              </div>
          </div>
      </div>
    </div>
</div>
