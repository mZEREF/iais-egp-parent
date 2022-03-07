<style type="text/css">
    .nav {
        background: transparent;
    }
    .dashboard-gp .dashboard-tile-item {
        width: calc((100% - 120px) / 5);
    }
    .dashboard-gp .dashboard-tile-item .dashboard-tile .dashboard-txt {
        width: calc(100%);
    }
    .dashboard-gp .dashboard-tile-item .dashboard-tile {
        height: 150px;
    }
</style>
<div class="dashboard" style="background-image:url('<%=webroot%>img/Masthead-banner.jpg')">
    <div class="container">
        <div class="navigation-gp">
            <%@ include file="menuPage.jsp" %>
        </div>
        <div class="row">
            <div class="col-xs-12">
                <div class="dashboard-gp">
                    <c:if test="${msgTab == 1}">
                    <div class="dashboard-tile-item">
                        <div class="dashboard-tile"><a data-tab="#tabInbox" href="#"  ${tabCode == 'msg' ? null : (msgContentFrom == 1 ? 'onclick=\'cotToMsg()\'':'onclick=\''.concat(tabCode).concat('ToMsgPage()\''))}>
                            <h1 class="dashboard-count">${unreadAndresponseNum}</h1>
                            <p class="dashboard-txt">New Messages</p>
                        </a></div>
                    </div>
                    </c:if>
                 <c:if test="${appTab == 1}">
                    <div class="dashboard-tile-item">
                        <div class="dashboard-tile"><a data-tab="#tabApp" href="#"  ${tabCode == 'app' ? null : (msgContentFrom == 1 ? 'onclick=\'cotToApp()\'':'onclick=\''.concat(tabCode).concat('ToAppPage()\''))}>
                            <h1 class="dashboard-count">${appDraftNum}</h1>
                            <p class="dashboard-txt">Application Drafts</p>
                        </a></div>
                    </div>
                 </c:if>
                    <c:if test="${dssTab == 1}">
                        <div class="dashboard-tile-item">
                            <div class="dashboard-tile"><a data-tab="#tabApp" href="#"  ${tabCode == 'dss' ? null : ('onclick=\'goToSubmission()\'')}>
                                <h1 class="dashboard-count">${dssDraftNum}</h1>
                                <p class="dashboard-txt">Data Submission Draft</p>
                            </a></div>
                        </div>
                    </c:if>
                  <c:if test="${licTab == 1}">
                    <div class="dashboard-tile-item">
                        <div class="dashboard-tile"><a data-tab="#tabLic" href="#"   ${tabCode == 'lic' ? null : (msgContentFrom == 1 ? 'onclick=\'cotToLic()\'' :'onclick=\''.concat(tabCode).concat('ToLicPage()\''))}>
                            <h1 class="dashboard-count">${licActiveNum}</h1>
                            <p class="dashboard-txt">Active Licences</p>
                        </a></div>
                    </div>
                  </c:if>
                    <div class="dashboard-tile-item">
                        <div class="dashboard-tile txt-only">
                            <a href="/main-web/eservice/INTERNET/MohAccessmentGuide">
                                <p class="dashboard-txt" style="line-height: 27px;">
                                    Not sure what to do? Let us guide you
                                    <em class="fa fa-angle-right"></em>
                                </p>
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-xs-12">
                <div class="dashboard-footernote">
                    <p class="dashboard-small-txt"><strong>Last Login:</strong> <fmt:formatDate value="${INTER_INBOX_USER_INFO.lastLogin}" pattern="dd/MM/yyyy HH:mm"/> |
                        <strong>Last Activity:</strong>
                        <c:choose>
                            <c:when test="${INTER_INBOX_USER_INFO.functionName != null}">
                                ${INTER_INBOX_USER_INFO.functionName}
                            </c:when>
                            <c:otherwise>
                                N/A
                            </c:otherwise>
                        </c:choose> - Licence No.
                        <c:choose>
                            <c:when test="${INTER_INBOX_USER_INFO.licenseNo != null}">
                                ${INTER_INBOX_USER_INFO.licenseNo}
                            </c:when>
                            <c:otherwise>
                                N/A
                            </c:otherwise>
                        </c:choose> , On <fmt:formatDate value="${INTER_INBOX_USER_INFO.lastLogin}" pattern="dd/MM/yyyy"/></p>
                </div>
            </div>
        </div>
    </div>
</div>