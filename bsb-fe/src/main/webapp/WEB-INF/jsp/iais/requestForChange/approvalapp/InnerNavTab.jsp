<%@ page import="sg.gov.moh.iais.egp.bsb.common.node.NodeGroup" %>
<%@ page import="sg.gov.moh.iais.egp.bsb.action.RfcApprovalAppDelegator" %>
<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.ApprovalAppConstants.*" %>
<%
    NodeGroup approvalAppRoot = (NodeGroup) request.getSession().getAttribute("approvalAppRoot");
%>
<ul id="nav-tabs-ul" class="nav nav-tabs hidden-xs hidden-sm" role="tablist">
    <li role="presentation" class="<%=RfcApprovalAppDelegator.computeTabClassnameForJsp(approvalAppRoot, NODE_NAME_COMPANY_INFO)%>">
        <a data-step-key="compInfo" role="tab" aria-selected="<%=RfcApprovalAppDelegator.ifNodeSelectedForJsp(approvalAppRoot, NODE_NAME_COMPANY_INFO)%>">Company Info</a>
    </li>
    <li role="presentation" class="<%=RfcApprovalAppDelegator.computeTabClassnameForJsp(approvalAppRoot, NODE_NAME_ACTIVITY)%>">
        <a data-step-key="activity" role="tab" aria-controls="activityPanel" aria-selected="<%=RfcApprovalAppDelegator.ifNodeSelectedForJsp(approvalAppRoot, NODE_NAME_ACTIVITY)%>">Activity</a>
    </li>
    <li role="presentation" class="<%=RfcApprovalAppDelegator.computeTabClassnameForJsp(approvalAppRoot, NODE_NAME_APPROVAL_PROFILE)%>">
        <a data-step-key="approvalProfile" role="tab" aria-controls="approvalProfilePanel" aria-selected="<%=RfcApprovalAppDelegator.ifNodeSelectedForJsp(approvalAppRoot, NODE_NAME_APPROVAL_PROFILE)%>">Approval Profile</a>
    </li>
    <li role="presentation" class="<%=RfcApprovalAppDelegator.computeTabClassnameForJsp(approvalAppRoot, NODE_NAME_PRIMARY_DOC)%>">
        <a data-step-key="primaryDocs" role="tab" aria-controls="PrimaryDocsPanel" aria-selected="<%=RfcApprovalAppDelegator.ifNodeSelectedForJsp(approvalAppRoot, NODE_NAME_PRIMARY_DOC)%>">Primary Documents</a>
    </li>
    <li role="presentation" class="<%=RfcApprovalAppDelegator.computeTabClassnameForJsp(approvalAppRoot, NODE_NAME_PREVIEW_SUBMIT)%>">
        <a data-step-key="previewSubmit" role="tab" aria-controls="previewSubmitPanel" aria-selected="<%=RfcApprovalAppDelegator.ifNodeSelectedForJsp(approvalAppRoot, NODE_NAME_PREVIEW_SUBMIT)%>">Preview & Submit</a>
    </li>
</ul>

<div class="tab-nav-mobile visible-xs visible-sm" style="overflow:hidden">
    <div class="swiper-wrapper" role="tablist">
        <div class="swiper-slide"><a data-step-key="compInfo" role="tab" aria-selected="<%=RfcApprovalAppDelegator.ifNodeSelectedForJsp(approvalAppRoot, NODE_NAME_COMPANY_INFO)%>">Company Info</a></div>
        <div class="swiper-slide"><a data-step-key="activity" role="tab" aria-controls="activityPanel" aria-selected="<%=RfcApprovalAppDelegator.ifNodeSelectedForJsp(approvalAppRoot, NODE_NAME_ACTIVITY)%>">Activity</a></div>
        <div class="swiper-slide"><a data-step-key="approvalProfile" role="tab" aria-controls="approvalProfilePanel" aria-selected="<%=RfcApprovalAppDelegator.ifNodeSelectedForJsp(approvalAppRoot, NODE_NAME_APPROVAL_PROFILE)%>">Approval Profile</a></div>
        <div class="swiper-slide"><a data-step-key="primaryDocs" role="tab" aria-controls="PrimaryDocsPanel" aria-selected="<%=RfcApprovalAppDelegator.ifNodeSelectedForJsp(approvalAppRoot, NODE_NAME_PRIMARY_DOC)%>">Primary Documents</a></div>
        <div class="swiper-slide"><a data-step-key="previewSubmit" role="tab" aria-controls="previewSubmitPanel" aria-selected="<%=RfcApprovalAppDelegator.ifNodeSelectedForJsp(approvalAppRoot, NODE_NAME_PREVIEW_SUBMIT)%>">Preview & Submit</a></div>
    </div>
    <div class="swiper-button-prev"></div>
    <div class="swiper-button-next"></div>
</div>