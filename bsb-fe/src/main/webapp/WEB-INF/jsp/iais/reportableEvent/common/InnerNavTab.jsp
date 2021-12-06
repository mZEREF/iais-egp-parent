<%@ page import="sg.gov.moh.iais.egp.bsb.common.node.NodeGroup" %>
<%@ page import="sg.gov.moh.iais.egp.bsb.action.IncidentNotificationDelegator" %>
<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.IncidentNotificationConstants.*" %>
<%
    NodeGroup incidentNotRoot = (NodeGroup) request.getSession().getAttribute("incidentNotRoot");
%>
<ul class="nav nav-tabs hidden-xs hidden-sm" role="tablist">
    <li role="presentation" class="<%=IncidentNotificationDelegator.computeTabClassnameForJsp(incidentNotRoot, NODE_NAME_INCIDENT_INFO)%>">
        <a data-step-key = "incidentInfo"
           aria-controls="tabIncidentInfo"
           role="tab"
           aria-selected="<%=IncidentNotificationDelegator.ifNodeSelectedForJsp(incidentNotRoot, NODE_NAME_INCIDENT_INFO)%>">Incident Info</a>
    </li>
    <li  role="presentation" class="<%=IncidentNotificationDelegator.computeTabClassnameForJsp(incidentNotRoot, NODE_NAME_PERSON_REPORTING_INFO)%>">
        <a data-step-key = "reportingPerson"
           aria-controls="tabReportPerson"
           role="tab"
           aria-selected="<%=IncidentNotificationDelegator.ifNodeSelectedForJsp(incidentNotRoot, NODE_NAME_PERSON_REPORTING_INFO)%>">Person Reporting Info</a>
    </li>
    <li  role="presentation" class="<%=IncidentNotificationDelegator.computeTabClassnameForJsp(incidentNotRoot, NODE_NAME_PERSON_INVOLVED_INFO)%>">
        <a  data-step-key = "involvedPerson"
            aria-controls="tabInvolvedPerson"
            role="tab"
            aria-selected="<%=IncidentNotificationDelegator.ifNodeSelectedForJsp(incidentNotRoot, NODE_NAME_PERSON_INVOLVED_INFO)%>">Person(s) Involved Info</a>
    </li>
    <li role="presentation" class="<%=IncidentNotificationDelegator.computeTabClassnameForJsp(incidentNotRoot, NODE_NAME_DOCUMENTS)%>">
        <a  data-step-key = "documents"
            aria-controls="tabDocuments"
            role="tab"
            aria-selected="<%=IncidentNotificationDelegator.ifNodeSelectedForJsp(incidentNotRoot, NODE_NAME_DOCUMENTS)%>">Incident Investigation</a>
    </li>
</ul>
<div class="tab-nav-mobile visible-xs visible-sm">
    <div class="swiper-wrapper" role="tablist">
        <div class="swiper-slide"><a  aria-controls="tabIncidentInfo"
                                     role="tab"
                                     aria-selected="<%=IncidentNotificationDelegator.ifNodeSelectedForJsp(incidentNotRoot, NODE_NAME_INCIDENT_INFO)%>">Incident Info</a>
        </div>
        <div class="swiper-slide"><a  aria-controls="tabReportPerson"
                                     role="tab"
                                     aria-selected="<%=IncidentNotificationDelegator.ifNodeSelectedForJsp(incidentNotRoot, NODE_NAME_PERSON_REPORTING_INFO)%>">Person Reporting Info</a>
        </div>
        <div class="swiper-slide"><a  aria-controls="tabInvolvedPerson"
                                     role="tab"
                                     aria-selected="<%=IncidentNotificationDelegator.ifNodeSelectedForJsp(incidentNotRoot, NODE_NAME_PERSON_INVOLVED_INFO)%>">Person(s) Involved Info</a>
        </div>
        <div class="swiper-slide"><a  aria-controls="tabDocuments"
                                     role="tab"
                                     aria-selected="<%=IncidentNotificationDelegator.ifNodeSelectedForJsp(incidentNotRoot, NODE_NAME_DOCUMENTS)%>">Incident Investigation</a>
        </div>
    </div>
    <div class="swiper-button-prev"></div>
    <div class="swiper-button-next"></div>
</div>
