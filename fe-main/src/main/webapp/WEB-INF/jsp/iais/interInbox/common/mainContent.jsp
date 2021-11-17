<div class="main-content">
    <div class="container">
        <div class="row">
            <div class="col-xs-12">
                <div class="tab-gp dashboard-tab" style="margin-left: 6px;margin-right: -8px;">
                    <ul class="nav nav-tabs hidden-xs hidden-sm" role="tablist">
                        <c:if test="${msgTab == 1}">
                        <li class="${tabCode == 'msg' ? 'active' : 'complete'}" role="presentation" id="inboxTab"><a aria-controls="tabInbox"
                                                                                    role="tab" data-toggle="tab"
                                                                                  ${tabCode == 'msg' ? null : 'onclick=\''.concat(tabCode).concat('ToMsgPage()\'')}>Inbox
                            (${unreadAndresponseNum})</a></li>
                        </c:if>
                        <c:if test="${appTab == 1}">
                        <li class="${tabCode == 'app' ? 'active' : 'complete'}" role="presentation" id="appTab"><a aria-controls="tabApp"
                                                                                role="tab" data-toggle="tab"
                                                                             ${tabCode == 'app' ? null : 'onclick=\''.concat(tabCode).concat('ToAppPage()\'')}>Applications</a>
                        </li>
                        </c:if>
                        <c:if test="${licTab == 1}">
                        <li class="${tabCode == 'lic' ? 'active' : 'complete'}" role="presentation" id="licTab"><a aria-controls="tabLic" role="tab"
                                                                              data-toggle="tab"    ${tabCode == 'lic' ? null : 'onclick=\''.concat(tabCode).concat('ToLicPage()\'')}>Licences</a></li>
                        </c:if>
                        <c:if test="${dssTab == 1}">
                        <li class="${tabCode == 'dss' ? 'active' : 'complete'}" role="presentation" id="dataSubTab">
                            <a aria-controls="dataSubTab" role="tab" data-toggle="tab"  ${tabCode == 'dss' ? null : ('onclick=\'goToSubmission()\'')}>Data Submissions</a></li>
                        </c:if>
                    </ul>
                    <div class="tab-nav-mobile visible-xs visible-sm">
                        <div class="swiper-wrapper" role="tablist">
                            <c:if test="${msgTab == 1}">
                            <div class="swiper-slide"><a href="#tabInbox" aria-controls="tabInbox" role="tab"
                                                         data-toggle="tab"   ${tabCode == 'msg' ? null : 'onclick=\''.concat(tabCode).concat('ToMsgPage()\'')}>Inbox (${unreadAndresponseNum})</a></div>
                            </c:if>
                            <c:if test="${appTab == 1}">
                            <div class="swiper-slide"><a href="#tabApp" aria-controls="tabApplication" role="tab"
                                                         data-toggle="tab"     ${tabCode == 'app' ? null : 'onclick=\''.concat(tabCode).concat('ToAppPage()\'')}>Applications</a></div>
                            </c:if>
                            <c:if test="${licTab == 1}">
                            <div class="swiper-slide"><a href="#tabLic" aria-controls="tabLicence" role="tab"
                                                         data-toggle="tab"   ${tabCode == 'lic' ? null : 'onclick=\''.concat(tabCode).concat('ToLicPage()\'')}>Licences</a></div>
                            </c:if>
                            <c:if test="${dssTab == 1}">
                            <div class="swiper-slide"><a href="#dataSubTab" aria-controls="dataSubTab" role="tab"
                                                         data-toggle="tab" ${tabCode == 'dss' ? null : ('onclick=\'goToSubmission()\'')}>Data Submissions</a></div>
                            </c:if>
                        </div>
                        <div class="swiper-button-prev"></div>
                        <div class="swiper-button-next"></div>
                    </div>
                    <div class="tab-content">
                        <c:if test="${tabCode == 'msg'}">
                            <%@ include file="../msg/messageList.jsp" %>
                        </c:if>
                        <c:if test="${tabCode == 'app'}">
                            <%@ include file="../app/applicationList.jsp" %>
                        </c:if>
                        <c:if test="${tabCode == 'lic'}">
                            <%@ include file="../lic/licencesList.jsp" %>
                        </c:if>
                        <c:if test="${tabCode == 'dss'}">
                            <%@ include file="../dataSubmission/dataList.jsp" %>
                        </c:if>

                    </div>
                </div>
            </div>
        </div>
    </div>
</div>