<div class="main-content">
    <div class="container">
        <div class="row">
            <div class="col-xs-12">
                <div class="tab-gp dashboard-tab">
                    <ul class="nav nav-tabs hidden-xs hidden-sm" role="tablist">
                        <li class="complete" role="presentation"><a href="#" aria-controls="tabInbox" role="tab" data-toggle="tab" onclick="appToMsgPage()">Inbox (2)</a></li>
                        <li class="active" role="presentation"><a href="#" aria-controls="tabApp" role="tab" data-toggle="tab">Applications</a></li>
                        <li class="incomplete" role="presentation"><a href="#" aria-controls="tabLic" role="tab" data-toggle="tab" onclick="appToLicPage()">Licences (${licActiveNum})</a></li>
                    </ul>
                    <div class="tab-nav-mobile visible-xs visible-sm">
                        <div class="swiper-wrapper" role="tablist">
                            <div class="swiper-slide"><a href="#tabInbox" aria-controls="tabInbox" role="tab" data-toggle="tab">Inbox (2)</a></div>
                            <div class="swiper-slide"><a href="#tabApp" aria-controls="tabApplication" role="tab" data-toggle="tab">Applications</a></div>
                            <div class="swiper-slide"><a href="#tabLic" aria-controls="tabLicence" role="tab" data-toggle="tab">Licences (${licActiveNum})</a></div>
                        </div>
                        <div class="swiper-button-prev"></div>
                        <div class="swiper-button-next"></div>
                    </div>
                    <div class="tab-content">
                        <%@ include file="applicationList.jsp" %>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>