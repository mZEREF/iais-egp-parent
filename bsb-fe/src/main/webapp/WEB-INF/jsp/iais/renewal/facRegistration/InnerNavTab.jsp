<%@ page import="sg.gov.moh.iais.egp.bsb.common.node.NodeGroup" %>
<%@ page import="sg.gov.moh.iais.egp.bsb.service.FacilityRegistrationService" %>
<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.*" %>
<%
    NodeGroup facRegRoot = (NodeGroup) request.getSession().getAttribute("facRegRoot");
%>
<ul id="nav-tabs-ul" class="nav nav-tabs hidden-xs hidden-sm" role="tablist">
    <li role="presentation" class="<%=FacilityRegistrationService.computeTabClassnameForJsp(facRegRoot, NODE_NAME_COMPANY_INFO)%>">
        <a data-step-key="compInfo" role="tab" aria-selected="<%=FacilityRegistrationService.ifNodeSelectedForJsp(facRegRoot, NODE_NAME_COMPANY_INFO)%>">Company Info</a>
    </li>
    <li role="presentation" class="<%=FacilityRegistrationService.computeTabClassnameForJsp(facRegRoot, NODE_NAME_FAC_SELECTION)%>">
        <a data-step-key="serviceSelection" role="tab" aria-controls="facSelectionPanel" aria-selected="<%=FacilityRegistrationService.ifNodeSelectedForJsp(facRegRoot, NODE_NAME_FAC_SELECTION)%>">Service Selection</a>
    </li>
    <li role="presentation" class="<%=FacilityRegistrationService.computeTabClassnameForJsp(facRegRoot, NODE_NAME_FAC_INFO)%>">
        <a data-step-key="facInfo" role="tab" aria-controls="facInfoPanel" aria-selected="<%=FacilityRegistrationService.ifNodeSelectedForJsp(facRegRoot, NODE_NAME_FAC_INFO)%>">Facility</a>
    </li>
    <li role="presentation" class="<%=FacilityRegistrationService.computeTabClassnameForJsp(facRegRoot, NODE_NAME_FAC_BAT_INFO)%>">
        <a data-step-key="batInfo" role="tab" aria-controls="batInfoPanel" aria-selected="<%=FacilityRegistrationService.ifNodeSelectedForJsp(facRegRoot, NODE_NAME_FAC_BAT_INFO)%>">Biological Agents & Toxin</a>
    </li>
    <li role="presentation" class="<%=FacilityRegistrationService.computeTabClassnameForJsp(facRegRoot, NODE_NAME_OTHER_INFO)%>">
        <a data-step-key="otherInfo" role="tab" aria-controls="otherInfoPanel" aria-selected="<%=FacilityRegistrationService.ifNodeSelectedForJsp(facRegRoot, NODE_NAME_OTHER_INFO)%>">Other Application Information</a>
    </li>
    <li role="presentation" class="<%=FacilityRegistrationService.computeTabClassnameForJsp(facRegRoot, NODE_NAME_PRIMARY_DOC)%>">
        <a data-step-key="primaryDocs" role="tab" aria-controls="PrimaryDocsPanel" aria-selected="<%=FacilityRegistrationService.ifNodeSelectedForJsp(facRegRoot, NODE_NAME_PRIMARY_DOC)%>">Primary Documents</a>
    </li>
</ul>

<div class="tab-nav-mobile visible-xs visible-sm" style="overflow:hidden">
    <div class="swiper-wrapper" role="tablist">
        <div class="swiper-slide"><a data-step-key="compInfo" role="tab" aria-selected="<%=FacilityRegistrationService.ifNodeSelectedForJsp(facRegRoot, NODE_NAME_COMPANY_INFO)%>">Company Info</a></div>
        <div class="swiper-slide"><a data-step-key="serviceSelection" role="tab" aria-controls="facSelectionPanel" aria-selected="<%=FacilityRegistrationService.ifNodeSelectedForJsp(facRegRoot, NODE_NAME_FAC_SELECTION)%>">Service Selection</a></div>
        <div class="swiper-slide"><a data-step-key="facInfo" role="tab" aria-controls="facInfoPanel" aria-selected="<%=FacilityRegistrationService.ifNodeSelectedForJsp(facRegRoot, NODE_NAME_FAC_INFO)%>">Facility</a></div>
        <div class="swiper-slide"><a data-step-key="batInfo" role="tab" aria-controls="batInfoPanel" aria-selected="<%=FacilityRegistrationService.ifNodeSelectedForJsp(facRegRoot, NODE_NAME_FAC_BAT_INFO)%>">Biological Agents & Toxin</a></div>
        <div class="swiper-slide"><a data-step-key="otherInfo" role="tab" aria-controls="otherInfoPanel" aria-selected="<%=FacilityRegistrationService.ifNodeSelectedForJsp(facRegRoot, NODE_NAME_OTHER_INFO)%>">Other Application Information</a></div>
        <div class="swiper-slide"><a data-step-key="primaryDocs" role="tab" aria-controls="PrimaryDocsPanel" aria-selected="<%=FacilityRegistrationService.ifNodeSelectedForJsp(facRegRoot, NODE_NAME_PRIMARY_DOC)%>">Primary Documents</a></div>
    </div>
    <div class="swiper-button-prev"></div>
    <div class="swiper-button-next"></div>
</div>