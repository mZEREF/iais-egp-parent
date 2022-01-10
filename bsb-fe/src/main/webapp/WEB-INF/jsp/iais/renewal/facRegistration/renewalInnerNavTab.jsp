<%@ page import="sg.gov.moh.iais.egp.bsb.common.node.NodeGroup" %>
<%@ page import="sg.gov.moh.iais.egp.bsb.service.FacilityRegistrationService" %>
<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.*" %>
<%
    NodeGroup viewApprovalRoot = (NodeGroup) request.getSession().getAttribute("viewApprovalRoot");
%>
<ul id="nav-tabs-ul" class="nav nav-tabs hidden-xs hidden-sm" role="tablist">
    <li role="presentation" class="<%=FacilityRegistrationService.computeTabClassnameForJsp(viewApprovalRoot, NODE_NAME_INSTRUCTION)%>">
        <a data-step-key="instruction" role="tab" aria-selected="<%=FacilityRegistrationService.ifNodeSelectedForJsp(viewApprovalRoot, NODE_NAME_INSTRUCTION)%>">Instructions</a>
    </li>
    <li role="presentation" class="<%=FacilityRegistrationService.computeTabClassnameForJsp(viewApprovalRoot, NODE_NAME_REVIEW)%>">
        <a data-step-key="review" role="tab" aria-selected="<%=FacilityRegistrationService.ifNodeSelectedForJsp(viewApprovalRoot, NODE_NAME_REVIEW)%>">Review</a>
    </li>
</ul>

<div class="tab-nav-mobile visible-xs visible-sm" style="overflow:hidden">
    <div class="swiper-wrapper" role="tablist">
        <div class="swiper-slide"><a data-step-key="instruction" role="tab" aria-selected="<%=FacilityRegistrationService.ifNodeSelectedForJsp(viewApprovalRoot, NODE_NAME_INSTRUCTION)%>">Instructions</a></div>
        <div class="swiper-slide"><a data-step-key="review" role="tab" aria-selected="<%=FacilityRegistrationService.ifNodeSelectedForJsp(viewApprovalRoot, NODE_NAME_REVIEW)%>">Review</a></div>
    </div>
    <div class="swiper-button-prev"></div>
    <div class="swiper-button-next"></div>
</div>