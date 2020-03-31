<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%
  //handle to the Engine APIs
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<%--<webui:setLayout name="egp-blank"/>--%>
<webui:setLayout name="iais-blank"/>
<%--<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>--%>

<div class="panel-main-content" style="margin: 2%">
  <c:forEach items="${currentPreviewSvcInfo.hcsaServiceStepSchemeDtos}" var="hcsaServiceStepSchemeDto">
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
  </c:forEach>

</div>

<script type="text/javascript">
    $(document).ready(function(){


    });

</script>