<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.FacCertifierRegisterConstants.*" %>
<%@ page import="sg.gov.moh.iais.egp.bsb.service.FacilityCertifierRegistrationService" %>
<%
  NodeGroup facInfoGroup = (NodeGroup) facRegRoot.getNode(NODE_NAME_ORGANISATION_INFO);
%>

<ul class="progress-tracker">
  <li class="tracker-item <%=FacilityCertifierRegistrationService.computeStepClassnameForJsp(facInfoGroup, NODE_NAME_COMPANY_PROFILE)%>" data-step-key="orgInfo_companyProfile">Company Profile</li>
  <li class="tracker-item <%=FacilityCertifierRegistrationService.computeStepClassnameForJsp(facInfoGroup, NODE_NAME_COMPANY_CERTIFYING_TEAM)%>" data-step-key="orgInfo_companyCerTeam">Certifying Team</li>
  <li class="tracker-item <%=FacilityCertifierRegistrationService.computeStepClassnameForJsp(facInfoGroup, NODE_NAME_COMPANY_FAC_ADMINISTRATOR)%>" data-step-key="orgInfo_companyAdmin">Administrator</li>
</ul>