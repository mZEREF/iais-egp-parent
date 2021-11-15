<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.FacCertifierRegisterConstants.*" %>
<%
  NodeGroup facInfoGroup = (NodeGroup) facRegRoot.getNode(NODE_NAME_ORGANISATION_INFO);
%>

<ul class="progress-tracker">
  <li class="tracker-item <%=RfcFacCertifierRegistrationDelegator.computeStepClassnameForJsp(facInfoGroup, NODE_NAME_ORG_PROFILE)%>" data-step-key="orgInfo_orgProfile">Organisation Profile</li>
  <li class="tracker-item <%=RfcFacCertifierRegistrationDelegator.computeStepClassnameForJsp(facInfoGroup, NODE_NAME_ORG_CERTIFYING_TEAM)%>" data-step-key="orgInfo_orgCerTeam">Certifying Team</li>
  <li class="tracker-item <%=RfcFacCertifierRegistrationDelegator.computeStepClassnameForJsp(facInfoGroup, NODE_NAME_ORG_FAC_ADMINISTRATOR)%>" data-step-key="orgInfo_orgAdmin">Administrator</li>
</ul>