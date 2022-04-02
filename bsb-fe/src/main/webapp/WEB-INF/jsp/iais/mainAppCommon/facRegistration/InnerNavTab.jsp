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
    <li role="presentation" class="<%=FacilityRegistrationService.computeTabClassnameForJsp(facRegRoot, NODE_NAME_FAC_INFO)%>">
        <a data-step-key="facInfo" role="tab" aria-controls="facInfoPanel" aria-selected="<%=FacilityRegistrationService.ifNodeSelectedForJsp(facRegRoot, NODE_NAME_FAC_INFO)%>">Facility Information</a>
    </li>
    <%if (facRegRoot.contains(NODE_NAME_FAC_BAT_INFO)) {%>
    <li role="presentation" class="<%=FacilityRegistrationService.computeTabClassnameForJsp(facRegRoot, NODE_NAME_FAC_BAT_INFO)%>">
        <a data-step-key="batInfo" role="tab" aria-controls="batInfoPanel" aria-selected="<%=FacilityRegistrationService.ifNodeSelectedForJsp(facRegRoot, NODE_NAME_FAC_BAT_INFO)%>">Biological Agents & Toxins</a>
    </li>
    <%}%>
    <li role="presentation" class="<%=FacilityRegistrationService.computeTabClassnameForJsp(facRegRoot, NODE_NAME_OTHER_INFO)%>">
        <a data-step-key="otherInfo" role="tab" aria-controls="otherAppInfoPanel" aria-selected="<%=FacilityRegistrationService.ifNodeSelectedForJsp(facRegRoot, NODE_NAME_OTHER_INFO)%>">Other Application Information</a>
    </li>
    <li role="presentation" class="<%=FacilityRegistrationService.computeTabClassnameForJsp(facRegRoot, NODE_NAME_PRIMARY_DOC)%>">
        <a data-step-key="primaryDocs" role="tab" aria-controls="PrimaryDocsPanel" aria-selected="<%=FacilityRegistrationService.ifNodeSelectedForJsp(facRegRoot, NODE_NAME_PRIMARY_DOC)%>">Supporting Documents</a>
    </li>
    <%if (facRegRoot.contains(NODE_NAME_AFC)) {%>
    <li role="presentation" class="<%=FacilityRegistrationService.computeTabClassnameForJsp(facRegRoot, NODE_NAME_AFC)%>">
        <a data-step-key="afc" role="tab" aria-controls="afcPanel" aria-selected="<%=FacilityRegistrationService.ifNodeSelectedForJsp(facRegRoot, NODE_NAME_AFC)%>">Approved Facility Certifier</a>
    </li>
    <%}%>
    <li role="presentation" class="<%=FacilityRegistrationService.computeTabClassnameForJsp(facRegRoot, NODE_NAME_PREVIEW_SUBMIT)%>">
        <a data-step-key="previewSubmit" role="tab" aria-controls="previewSubmitPanel" aria-selected="<%=FacilityRegistrationService.ifNodeSelectedForJsp(facRegRoot, NODE_NAME_PREVIEW_SUBMIT)%>">Preview & Submit</a>
    </li>
</ul>

<div class="tab-nav-mobile visible-xs visible-sm" style="overflow:hidden">
    <div class="swiper-wrapper" role="tablist">
        <div class="swiper-slide"><a data-step-key="compInfo" role="tab" aria-selected="<%=FacilityRegistrationService.ifNodeSelectedForJsp(facRegRoot, NODE_NAME_COMPANY_INFO)%>">Company Info</a></div>
        <div class="swiper-slide"><a data-step-key="facInfo" role="tab" aria-controls="facInfoPanel" aria-selected="<%=FacilityRegistrationService.ifNodeSelectedForJsp(facRegRoot, NODE_NAME_FAC_INFO)%>">Facility Information</a></div>
        <%if (facRegRoot.contains(NODE_NAME_FAC_BAT_INFO)) {%>
        <div class="swiper-slide"><a data-step-key="batInfo" role="tab" aria-controls="batInfoPanel" aria-selected="<%=FacilityRegistrationService.ifNodeSelectedForJsp(facRegRoot, NODE_NAME_FAC_BAT_INFO)%>">Biological Agents & Toxins</a></div>
        <%}%>
        <div class="swiper-slide"><a data-step-key="otherInfo" role="tab" aria-controls="otherInfoPanel" aria-selected="<%=FacilityRegistrationService.ifNodeSelectedForJsp(facRegRoot, NODE_NAME_OTHER_INFO)%>">Other Application Information</a></div>
        <div class="swiper-slide"><a data-step-key="primaryDocs" role="tab" aria-controls="PrimaryDocsPanel" aria-selected="<%=FacilityRegistrationService.ifNodeSelectedForJsp(facRegRoot, NODE_NAME_PRIMARY_DOC)%>">Supporting Documents</a></div>
        <%if (facRegRoot.contains(NODE_NAME_AFC)) {%>
        <div class="swiper-slide"><a data-step-key="afc" role="tab" aria-controls="afcPanel" aria-selected="<%=FacilityRegistrationService.ifNodeSelectedForJsp(facRegRoot, NODE_NAME_AFC)%>">Approved Facility Certifier</a></div>
        <%}%>
        <div class="swiper-slide"><a data-step-key="previewSubmit" role="tab" aria-controls="previewSubmitPanel" aria-selected="<%=FacilityRegistrationService.ifNodeSelectedForJsp(facRegRoot, NODE_NAME_PREVIEW_SUBMIT)%>">Preview & Submit</a></div>
    </div>
    <div class="swiper-button-prev"></div>
    <div class="swiper-button-next"></div>
</div>