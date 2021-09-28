<%--make the nav bar determined by role logic--%>
<ul class="nav nav-tabs hidden-xs hidden-sm" role="tablist">
    <li role="presentation" id="inboxTab">
        <a href="/bsb-fe/eservice/INTERNET/MohBSBInboxMsg" aria-controls="tabInbox" role="tab">Inbox (${unreadMsgAmt})</a></li>
    <li role="presentation" id="appTab">
        <a href="/bsb-fe/eservice/INTERNET/MohBSBInboxApp" aria-controls="tabApp" role="tab">Applications</a>
    </li>
    <li role="presentation" id="approvalsTab">
        <a href="#" aria-controls="tabLic" role="tab">Approvals</a>
    </li>
    <li role="presentation" id="certTab">
        <a href="#" aria-controls="tabLic" role="tab">Certifications</a>
    </li>
    <li role="presentation" id="dataSubTab">
        <a href="#" aria-controls="tabLic" role="tab">Data Submissions</a>
    </li>
    <li role="presentation" id="reportEventTab">
        <a href="#" aria-controls="tabLic" role="tab">Report Events</a>
    </li>
</ul>
<div class="tab-nav-mobile visible-xs visible-sm">
    <div class="swiper-wrapper" role="tablist">
        <div class="swiper-slide">
            <a href="/bsb-fe/eservice/INTERNET/MohBSBInboxMsg" aria-controls="tabInbox" role="tab">Inbox (${unreadMsgAmt})</a>
        </div>
        <div class="swiper-slide">
            <a href="/bsb-fe/eservice/INTERNET/MohBSBInboxApp" aria-controls="tabApplication" role="tab">Applications</a>
        </div>
        <div class="swiper-slide">
            <a href="#" aria-controls="tabLicence" role="tab">Approvals</a>
        </div>
        <div class="swiper-slide">
            <a href="#" aria-controls="tabLicence" role="tab">Certifications</a>
        </div>
        <div class="swiper-slide">
            <a href="#" aria-controls="tabLicence" role="tab">Data Submissions</a>
        </div>
        <div class="swiper-slide">
            <a href="#" aria-controls="tabLicence" role="tab">Report Events</a>
        </div>
    </div>
    <div class="swiper-button-prev"></div>
    <div class="swiper-button-next"></div>
</div>