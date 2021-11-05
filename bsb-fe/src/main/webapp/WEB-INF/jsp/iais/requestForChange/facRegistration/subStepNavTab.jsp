<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.*" %>
<%
  NodeGroup facInfoGroup = (NodeGroup) facRegRoot.getNode(NODE_NAME_FAC_INFO);
%>

<ul class="progress-tracker">
  <li class="tracker-item <%=RfcFacilityRegistrationDelegator.computeStepClassnameForJsp(facInfoGroup, NODE_NAME_FAC_PROFILE)%>" data-step-key="facInfo_facProfile">Facility Profile</li>
  <li class="tracker-item <%=RfcFacilityRegistrationDelegator.computeStepClassnameForJsp(facInfoGroup, NODE_NAME_FAC_OPERATOR)%>" data-step-key="facInfo_facOperator">Facility Operator</li>
  <li class="tracker-item <%=RfcFacilityRegistrationDelegator.computeStepClassnameForJsp(facInfoGroup, NODE_NAME_FAC_AUTH)%>" data-step-key="facInfo_facAuth">Authorised Personnel</li>
  <li class="tracker-item <%=RfcFacilityRegistrationDelegator.computeStepClassnameForJsp(facInfoGroup, NODE_NAME_FAC_ADMIN)%>" data-step-key="facInfo_facAdmin">Facility Administrator</li>
  <li class="tracker-item <%=RfcFacilityRegistrationDelegator.computeStepClassnameForJsp(facInfoGroup, NODE_NAME_FAC_OFFICER)%>" data-step-key="facInfo_facOfficer">Facility Officer</li>
  <li class="tracker-item <%=RfcFacilityRegistrationDelegator.computeStepClassnameForJsp(facInfoGroup, NODE_NAME_FAC_COMMITTEE)%>" data-step-key="facInfo_facCommittee">Biosafety Committee</li>
</ul>