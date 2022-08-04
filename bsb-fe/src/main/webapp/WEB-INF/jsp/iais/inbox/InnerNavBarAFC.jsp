<%--make the nav bar determined by role logic--%>
<ul class="nav nav-tabs hidden-xs hidden-sm" role="tablist">
    <li role="presentation" id="inboxTab">
        <a href="/bsb-web/eservice/INTERNET/MohBSBInboxMsg" aria-controls="tabInbox" role="tab">Inbox (${unreadMsgAmt})  <span class="btn-tooltip styleguide-tooltip" data-toggle="tooltip" data-html="true" title="" data-original-title="<p>Contains messages to the user.The archive function is available for housekeeping of inbox messages</p>" style="border-radius: 50%">i</span></a>
    </li>
    <li role="presentation" id="appTab">
        <a href="/bsb-web/eservice/INTERNET/MohBSBInboxAppAFC" aria-controls="tabApp" role="tab">Applications  <span class="btn-tooltip styleguide-tooltip" data-toggle="tooltip" data-html="true" title="" data-original-title="<p>Contains past applications that were submitted to MOH and saved draft applications.</p>" style="border-radius: 50%">i</span></a>
    </li>
    <li role="presentation" id="approvalFacTab">
        <a href="/bsb-web/eservice/INTERNET/MohBsbInboxApprovalFacAdmin" aria-controls="tabLic" role="tab">Approvals  <span class="btn-tooltip styleguide-tooltip" data-toggle="tooltip" data-html="true" title="" data-original-title="<p>Records of facility approvals</p>" style="border-radius: 50%">i</span></a>
    </li>
    <li role="presentation" id="certTab">
        <a href="#" aria-controls="tabLic" role="tab">Certifications  <span class="btn-tooltip styleguide-tooltip" data-toggle="tooltip" data-html="true" title="" data-original-title="<p>For retrieval of final report uploaded by MOH and/or MOH-AFC.</p>" style="border-radius: 50%">i</span></a>
    </li>
    <li role="presentation" id="dataSubTab">
        <a href="/bsb-web/eservice/INTERNET/DataSubInbox" aria-controls="tabLic" role="tab">Data Submissions  <span class="btn-tooltip styleguide-tooltip" data-toggle="tooltip" data-html="true" title="" data-original-title="<p>Includes records of notification of inventory movement, reports and other data submissions by the facility.</p>" style="border-radius: 50%">i</span></a>
    </li>
</ul>
<div class="tab-nav-mobile visible-xs visible-sm">
    <div class="swiper-wrapper" role="tablist">
        <div class="swiper-slide">
            <a href="/bsb-web/eservice/INTERNET/MohBSBInboxMsg" aria-controls="tabInbox" role="tab">Inbox (${unreadMsgAmt})</a>
        </div>
        <div class="swiper-slide">
            <a href="/bsb-web/eservice/INTERNET/MohBSBInboxAppAFC" aria-controls="tabApplication" role="tab">Applications</a>
        </div>
        <div class="swiper-slide">
            <a href="/bsb-web/eservice/INTERNET/MohBsbInboxApprovalFacAdmin" aria-controls="tabLicence" role="tab">Approvals</a>
        </div>
        <div class="swiper-slide">
            <a href="#" aria-controls="tabLicence" role="tab">Certifications</a>
        </div>
        <div class="swiper-slide">
            <a href="#" aria-controls="tabLicence" role="tab">Data Submissions</a>
        </div>
    </div>
    <div class="swiper-button-prev"></div>
    <div class="swiper-button-next"></div>
</div>