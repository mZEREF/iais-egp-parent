<%@ page import="sg.gov.moh.iais.egp.bsb.common.node.NodeGroup" %>
<%@ page import="sg.gov.moh.iais.egp.bsb.service.FacilityCertifierRegistrationService" %>
<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.FacCertifierRegisterConstants.*" %>
<%
    NodeGroup facRegRoot = (NodeGroup) request.getSession().getAttribute("facCertifierRegRoot");
%>
<ul id="nav-tabs-ul" class="nav nav-tabs hidden-xs hidden-sm" role="tablist">
    <li role="presentation" class="<%=FacilityCertifierRegistrationService.computeTabClassnameForJsp(facRegRoot, NODE_NAME_APPLICATION_INFO)%>">
        <a data-step-key="appInfo" role="tab" aria-controls="appInfoPanel" aria-selected="<%=FacilityCertifierRegistrationService.ifNodeSelectedForJsp(facRegRoot, NODE_NAME_APPLICATION_INFO)%>">Application Information</a>
    </li>
    <li role="presentation" class="<%=FacilityCertifierRegistrationService.computeTabClassnameForJsp(facRegRoot, NODE_NAME_SUPPORTING_DOCUMENT)%>">
        <a data-step-key="supportingDoc" role="tab" aria-controls="supportingDocPanel" aria-selected="<%=FacilityCertifierRegistrationService.ifNodeSelectedForJsp(facRegRoot, NODE_NAME_SUPPORTING_DOCUMENT)%>">Supporting Documents</a>
    </li>
    <li role="presentation" class="<%=FacilityCertifierRegistrationService.computeTabClassnameForJsp(facRegRoot, NODE_NAME_FACILITY_CERTIFIER_PREVIEW_SUBMIT)%>">
        <a data-step-key="previewSubmit" role="tab" aria-controls="previewSubmitPanel" aria-selected="<%=FacilityCertifierRegistrationService.ifNodeSelectedForJsp(facRegRoot, NODE_NAME_FACILITY_CERTIFIER_PREVIEW_SUBMIT)%>">Preview and Submit</a>
    </li>
</ul>

<div class="tab-nav-mobile visible-xs visible-sm" style="overflow:hidden">
    <div class="swiper-wrapper" role="tablist">
        <div class="swiper-slide"><a data-step-key="appInfo" role="tab" aria-controls="appInfoPanel" aria-selected="<%=FacilityCertifierRegistrationService.ifNodeSelectedForJsp(facRegRoot, NODE_NAME_APPLICATION_INFO)%>">Application Information</a></div>
        <div class="swiper-slide"><a data-step-key="supportingDoc" role="tab" aria-controls="supportingDocPanel" aria-selected="<%=FacilityCertifierRegistrationService.ifNodeSelectedForJsp(facRegRoot, NODE_NAME_SUPPORTING_DOCUMENT)%>">Supporting Documents</a></div>
        <div class="swiper-slide"><a data-step-key="previewSubmit" role="tab" aria-controls="previewSubmitPanel" aria-selected="<%=FacilityCertifierRegistrationService.ifNodeSelectedForJsp(facRegRoot, NODE_NAME_FACILITY_CERTIFIER_PREVIEW_SUBMIT)%>">Preview and Submit</a></div>
    </div>
    <div class="swiper-button-prev"></div>
    <div class="swiper-button-next"></div>
</div>