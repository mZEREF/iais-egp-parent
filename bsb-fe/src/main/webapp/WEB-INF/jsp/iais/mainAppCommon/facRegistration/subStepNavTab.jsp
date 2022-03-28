<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.*" %>
<%
  String currentNode = (String) request.getSession().getAttribute(KEY_JUMP_DEST_NODE);
  int order;
  switch (currentNode) {
    case NODE_PATH_FAC_PROFILE:
      order = 1;
      break;
    case NODE_PATH_FAC_OPERATOR:
      order = 2;
      break;
    case NODE_PATH_FAC_ADMIN_OFFICER:
      order = 3;
      break;
    case NODE_PATH_FAC_COMMITTEE:
      order = 4;
      break;
    case NODE_PATH_FAC_AUTH:
      order = 5;
      break;
    default:
      order = -1;
      break;
  }
%>

<ul class="progress-tracker">
  <li class="tracker-item <%=order >= 1 ? "active" : "disabled"%>" data-step-key="facInfo_facProfile">Facility Profile</li>
  <li class="tracker-item <%=order >= 2 ? "active" : "disabled"%>" data-step-key="facInfo_facOperator">Facility Operator</li>
  <li class="tracker-item <%=order >= 3 ? "active" : "disabled"%>" data-step-key="facInfo_facAdminOfficer">Facility Administrator/Officer</li>
  <li class="tracker-item <%=order >= 4 ? "active" : "disabled"%>" data-step-key="facInfo_facCommittee">Biosafety Committee</li>
  <li class="tracker-item <%=order >= 5 ? "active" : "disabled"%>" data-step-key="facInfo_facAuth">Authorised Personnel</li>
</ul>