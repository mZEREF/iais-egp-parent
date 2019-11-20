<div class="main-content">
    <div class="container">
        <div class="row">
            <div class="col-xs-12">
                <div class="tab-gp dashboard-tab">
                    <ul class="nav nav-tabs hidden-xs hidden-sm" role="tablist">
                        <li class="active" role="presentation"><a href="#tabInbox" aria-controls="tabInbox" role="tab" data-toggle="tab" >Inbox (2)</a></li>
                        <li class="complete" role="presentation"><a href="#tabApplication" aria-controls="tabApplication" role="tab" data-toggle="tab">Applications</a></li>
                        <li class="incomplete" role="presentation"><a href="#tabLicence" aria-controls="tabLicence" role="tab" data-toggle="tab" >Licences</a></li>
                    </ul>
                    <div class="tab-nav-mobile visible-xs visible-sm">
                        <div class="swiper-wrapper" role="tablist">
                            <div class="swiper-slide"><a href="#tabInbox#tabInbox" aria-controls="tabInbox" role="tab" data-toggle="tab" onclick="javascript:getInboxList()">Inbox (2)</a></div>
                            <div class="swiper-slide"><a href="#tabApplication" aria-controls="tabApplication" role="tab" data-toggle="tab">Applications</a></div>
                            <div class="swiper-slide"><a href="#tabLicence" aria-controls="tabLicence" role="tab" data-toggle="tab">Licences</a></div>
                        </div>
                        <div class="swiper-button-prev"></div>
                        <div class="swiper-button-next"></div>
                    </div>
                    <div class="tab-content">
                        <%@ include file="./inboxList.jsp" %>
                        <%@ include file="./applicationList.jsp" %>
                        <%@ include file="./licencesList.jsp" %>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
