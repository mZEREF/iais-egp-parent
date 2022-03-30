<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.FacCertifierRegisterConstants.*" %>
<%@ page import="sg.gov.moh.iais.egp.bsb.service.FacilityCertifierRegistrationService" %>
<%
  NodeGroup facInfoGroup = (NodeGroup) facRegRoot.getNode(NODE_NAME_APPLICATION_INFO);
%>

<ul class="progress-tracker">
  <li class="tracker-item <%=FacilityCertifierRegistrationService.computeStepClassnameForJsp(facInfoGroup, NODE_NAME_COMPANY_PROFILE)%>" data-step-key="appInfo_companyProfile">Company Profile</li>
  <li class="tracker-item <%=FacilityCertifierRegistrationService.computeStepClassnameForJsp(facInfoGroup, NODE_NAME_ADMINISTRATOR)%>" data-step-key="appInfo_companyAdmin">Administrator</li>
  <li class="tracker-item <%=FacilityCertifierRegistrationService.computeStepClassnameForJsp(facInfoGroup, NODE_NAME_CERTIFYING_TEAM_DETAIL)%>" data-step-key="appInfo_cerTeam">Details of Certifying Team</li>
</ul>