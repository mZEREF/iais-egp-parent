<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.*" %>
<%
  NodeGroup appInfoGroup = (NodeGroup) approvalAppRoot.getNode(NODE_NAME_APP_INFO);
%>

<ul class="progress-tracker">
  <li class="tracker-item <%=ApprovalBatAndActivityService.computeStepClassnameForJsp(appInfoGroup, NODE_NAME_FAC_PROFILE)%>" data-step-key="appInfo_facProfile">Facility Profile</li>
  <c:if test="${processType eq 'PROTYPE002'}">
    <li class="tracker-item <%=ApprovalBatAndActivityService.computeStepClassnameForJsp(appInfoGroup, NODE_NAME_POSSESS_BAT)%>" data-step-key="appInfo_possessBat">Details of Biological Agent</li>
  </c:if>
  <c:if test="${processType eq 'PROTYPE003'}">
    <li class="tracker-item <%=ApprovalBatAndActivityService.computeStepClassnameForJsp(appInfoGroup, NODE_NAME_LARGE_BAT)%>" data-step-key="appInfo_largeBat">Details of Biological Agent</li>
  </c:if>
  <c:if test="${processType eq 'PROTYPE004'}">
    <li class="tracker-item <%=ApprovalBatAndActivityService.computeStepClassnameForJsp(appInfoGroup, NODE_NAME_SPECIAL_BAT)%>" data-step-key="appInfo_specialBat">Details of Biological Agent</li>
    <li class="tracker-item <%=ApprovalBatAndActivityService.computeStepClassnameForJsp(appInfoGroup, NODE_NAME_FAC_AUTHORISED)%>" data-step-key="appInfo_facAuthorised">Authorised Personnel</li>
  </c:if>
  <c:if test="${processType eq 'PROTYPE012'}">
    <li class="tracker-item <%=ApprovalBatAndActivityService.computeStepClassnameForJsp(appInfoGroup, NODE_NAME_FAC_ACTIVITY)%>" data-step-key="appInfo_facActivity">Facility Activity Type</li>
  </c:if>
</ul>