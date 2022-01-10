<%@ page import="sg.gov.moh.iais.egp.bsb.common.node.NodeGroup" %>
<%@ page import="sg.gov.moh.iais.egp.bsb.service.FacilityCertifierRegistrationService" %>
<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.FacCertifierRegisterConstants.*" %>
<%
    NodeGroup facRegRoot = (NodeGroup) request.getSession().getAttribute("facCertifierRegRoot");
%>
<ul id="nav-tabs-ul" class="nav nav-tabs hidden-xs hidden-sm" role="tablist">
    <li role="presentation" class="<%=FacilityCertifierRegistrationService.computeTabClassnameForJsp(facRegRoot, NODE_NAME_ORGANISATION_INFO)%>">
        <a data-step-key="orgInfo" role="tab" aria-controls="orgInfoPanel" aria-selected="<%=FacilityCertifierRegistrationService.ifNodeSelectedForJsp(facRegRoot, NODE_NAME_ORGANISATION_INFO)%>">Organization Info</a>
    </li>
    <li role="presentation" class="<%=FacilityCertifierRegistrationService.computeTabClassnameForJsp(facRegRoot, NODE_NAME_FAC_PRIMARY_DOCUMENT)%>">
        <a data-step-key="primaryDoc" role="tab" aria-controls="PrimaryDocPanel" aria-selected="<%=FacilityCertifierRegistrationService.ifNodeSelectedForJsp(facRegRoot, NODE_NAME_FAC_PRIMARY_DOCUMENT)%>">Primary Documents</a>
    </li>
</ul>

<div class="tab-nav-mobile visible-xs visible-sm" style="overflow:hidden">
    <div class="swiper-wrapper" role="tablist">
        <div class="swiper-slide"><a data-step-key="orgInfo" role="tab" aria-controls="orgInfoPanel" aria-selected="<%=FacilityCertifierRegistrationService.ifNodeSelectedForJsp(facRegRoot, NODE_NAME_ORGANISATION_INFO)%>">Organization Info</a></div>
        <div class="swiper-slide"><a data-step-key="primaryDocument" role="tab" aria-controls="PrimaryDocPanel" aria-selected="<%=FacilityCertifierRegistrationService.ifNodeSelectedForJsp(facRegRoot, NODE_NAME_FAC_PRIMARY_DOCUMENT)%>">Primary Documents</a></div>
    </div>
    <div class="swiper-button-prev"></div>
    <div class="swiper-button-next"></div>
</div>