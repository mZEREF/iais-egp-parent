<%
    javax.servlet.http.HttpServletRequest request1 = ((org.springframework.web.context.request.ServletRequestAttributes) org.springframework.web.context.request.RequestContextHolder.getRequestAttributes()).getRequest();
    sg.gov.moh.iais.egp.bsb.common.node.NodeGroup approvalAppRoot = (sg.gov.moh.iais.egp.bsb.common.node.NodeGroup) request1.getSession().getAttribute("approvalAppRoot");
%>

<ul id="nav-tabs-ul" class="nav nav-tabs hidden-xs hidden-sm" role="tablist">
    <li role="presentation" class="<%=sg.gov.moh.iais.egp.bsb.service.ApprovalBatAndActivityService.computeTabClassnameForJsp(approvalAppRoot, sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.NODE_NAME_APP_INFO)%>">
        <a data-step-key="compInfo" role="tab" aria-selected="<%=sg.gov.moh.iais.egp.bsb.service.ApprovalBatAndActivityService.ifNodeSelectedForJsp(approvalAppRoot, sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.NODE_NAME_APP_INFO)%>">Application Information</a>
    </li>
    <li role="presentation" class="<%=sg.gov.moh.iais.egp.bsb.service.ApprovalBatAndActivityService.computeTabClassnameForJsp(approvalAppRoot, sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.NODE_NAME_PRIMARY_DOC)%>">
        <a data-step-key="compInfo" role="tab" aria-selected="<%=sg.gov.moh.iais.egp.bsb.service.ApprovalBatAndActivityService.ifNodeSelectedForJsp(approvalAppRoot, sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.NODE_NAME_PRIMARY_DOC)%>">Supporting Documents</a>
    </li>
    <li role="presentation" class="<%=sg.gov.moh.iais.egp.bsb.service.ApprovalBatAndActivityService.computeTabClassnameForJsp(approvalAppRoot, sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.NODE_NAME_PREVIEW)%>">
        <a data-step-key="compInfo" role="tab" aria-selected="<%=sg.gov.moh.iais.egp.bsb.service.ApprovalBatAndActivityService.ifNodeSelectedForJsp(approvalAppRoot, sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.NODE_NAME_PREVIEW)%>">Preview and Submit</a>
    </li>
</ul>

<div class="tab-nav-mobile visible-xs visible-sm" style="overflow:hidden">
    <div class="swiper-wrapper" role="tablist">
        <div class="swiper-slide"><a data-step-key="appInfo" role="tab" aria-selected="<%=sg.gov.moh.iais.egp.bsb.service.ApprovalBatAndActivityService.ifNodeSelectedForJsp(approvalAppRoot, sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.NODE_NAME_APP_INFO)%>">Application Information</a></div>
        <div class="swiper-slide"><a data-step-key="primaryDoc" role="tab" aria-controls="PrimaryDocsPanel" aria-selected="<%=sg.gov.moh.iais.egp.bsb.service.ApprovalBatAndActivityService.ifNodeSelectedForJsp(approvalAppRoot, sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.NODE_NAME_PRIMARY_DOC)%>">Supporting Documents</a></div>
        <div class="swiper-slide"><a data-step-key="preview" role="tab" aria-controls="previewSubmitPanel" aria-selected="<%=sg.gov.moh.iais.egp.bsb.service.ApprovalBatAndActivityService.ifNodeSelectedForJsp(approvalAppRoot, sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.NODE_NAME_PREVIEW)%>">Preview and Submit</a></div>
    </div>
    <div class="swiper-button-prev"></div>
    <div class="swiper-button-next"></div>
</div>