<%@ page import="sg.gov.moh.iais.egp.bsb.common.node.NodeGroup" %>
<%@ page import="sg.gov.moh.iais.egp.bsb.action.InvestigationReportDelegator" %>
<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.ReportableEventConstants.*" %>
<%
    NodeGroup investRepoRoot = (NodeGroup) request.getSession().getAttribute("investRepoRoot");
%>
<ul class="nav nav-tabs hidden-xs hidden-sm" role="tablist">
    <li role="presentation" class="<%=InvestigationReportDelegator.computeTabClassnameForJsp(investRepoRoot, NODE_NAME_INCIDENT_INFO)%>">
        <a data-step-key = "incidentInfo"
           aria-controls="tabIncidentInfo"
           role="tab"
           aria-selected="<%=InvestigationReportDelegator.ifNodeSelectedForJsp(investRepoRoot, NODE_NAME_INCIDENT_INFO)%>">Incident Info</a>
    </li>

    <li  role="presentation" class="<%=InvestigationReportDelegator.computeTabClassnameForJsp(investRepoRoot, NODE_NAME_INCIDENT_INVESTIGATION)%>">
        <a data-step-key = "incidentInvest"
           aria-controls="tabIncidentInvest"
           role="tab"
           aria-selected="<%=InvestigationReportDelegator.ifNodeSelectedForJsp(investRepoRoot, NODE_NAME_INCIDENT_INVESTIGATION)%>">Incident Investigation</a>
    </li>

    <li  role="presentation" class="<%=InvestigationReportDelegator.computeTabClassnameForJsp(investRepoRoot, NODE_NAME_MEDICAL_INVESTIGATION)%>">
        <a  data-step-key = "medicalInvest"
            aria-controls="tabMedicalInvest"
            role="tab"
            aria-selected="<%=InvestigationReportDelegator.ifNodeSelectedForJsp(investRepoRoot, NODE_NAME_MEDICAL_INVESTIGATION)%>">Medical Investigation</a>
    </li>

    <li role="presentation" class="<%=InvestigationReportDelegator.computeTabClassnameForJsp(investRepoRoot, NODE_NAME_DOCUMENTS)%>">
        <a  data-step-key = "documents"
            aria-controls="tabDocuments"
            role="tab"
            aria-selected="<%=InvestigationReportDelegator.ifNodeSelectedForJsp(investRepoRoot, NODE_NAME_DOCUMENTS)%>">Documents</a>
    </li>
</ul>
<div class="tab-nav-mobile visible-xs visible-sm">
    <div class="swiper-wrapper" role="tablist">
        <div class="swiper-slide"><a  aria-controls="tabIncidentInfo"
                                     role="tab"
                                     aria-selected="<%=InvestigationReportDelegator.ifNodeSelectedForJsp(investRepoRoot, NODE_NAME_INCIDENT_INFO)%>">Incident Info</a>
        </div>
        <div class="swiper-slide"><a  aria-controls="tabIncidentInvest"
                                     role="tab"
                                     aria-selected="<%=InvestigationReportDelegator.ifNodeSelectedForJsp(investRepoRoot, NODE_NAME_PERSON_REPORTING_INFO)%>">Incident Investigation</a>
        </div>
        <div class="swiper-slide"><a  aria-controls="tabMedicalInvest"
                                     role="tab"
                                     aria-selected="<%=InvestigationReportDelegator.ifNodeSelectedForJsp(investRepoRoot, NODE_NAME_PERSON_INVOLVED_INFO)%>">Medical Investigation</a>
        </div>
        <div class="swiper-slide"><a  aria-controls="tabDocuments"
                                     role="tab"
                                     aria-selected="<%=InvestigationReportDelegator.ifNodeSelectedForJsp(investRepoRoot, NODE_NAME_DOCUMENTS)%>">Documents</a>
        </div>
    </div>
    <div class="swiper-button-prev"></div>
    <div class="swiper-button-next"></div>
</div>
