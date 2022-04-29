<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%
    String webroot = IaisEGPConstant.CSS_ROOT + IaisEGPConstant.FE_CSS_ROOT;
%>
<style>
    .nav {
        background: transparent;
    }

    .dashboard-gp .dashboard-tile-item {
        width: calc((100% - 90px) / 4);
        float: left;
    }
    .dashboard-gp .dashboard-tile-item .dashboard-tile .dashboard-txt {
        font-size: 1.9rem;
        width: calc(100% - 70px);
    }
    .dashboard-gp .dashboard-tile-item .dashboard-tile a {
        padding: 35px 35px 32px 20px;
    }
    .dashboard-gp .dashboard-tile-item:not(:last-child) {
        margin-right: 30px;
    }
</style>
<div class="dashboard" style="background-image:url('<%=webroot%>img/Masthead-banner.jpg')">
    <div class="container">
        <div class="navigation-gp">
            <%@include file="menuPage.jsp" %>
        </div>
        <div class="row">
            <div class="col-xs-12">
                <div class="dashboard-gp">
                    <div class="dashboard-tile-item">
                        <div class="dashboard-tile"><a data-tab="#tabInbox" href="/bsb-fe/eservice/INTERNET/MohBSBInboxMsg?msgStatus=MSGRS001" onclick="licToMsgPage()">
                            <p class="dashboard-txt">New Messages</p>
                            <h1 class="dashboard-count">${unreadMsgAmt}</h1>
                        </a>
                        </div>
                    </div>
                    <div class="dashboard-tile-item">
                        <div class="dashboard-tile"><a data-tab="#tabApp" href="/bsb-fe/eservice/INTERNET/MohBSBInboxApp?searchStatus=BSBAPST011" onclick="licToAppPage()">
                            <p class="dashboard-txt">Application Drafts</p>
                            <h1 class="dashboard-count">${draftAppAmt}</h1>
                        </a></div>
                    </div>
                    <div class="dashboard-tile-item">
                        <div class="dashboard-tile"><a data-tab="#tabLic" href="/bsb-fe/eservice/INTERNET/MohBSBInboxFac?facilityStatus=APPRSTA001">
                            <p class="dashboard-txt">Active Facilities</p>
                            <h1 class="dashboard-count">${activeFacilityAmt}</h1>
                        </a></div>
                    </div>
                    <div class="dashboard-tile-item">
                        <div class="dashboard-tile"><a data-tab="#tabLic" href="/bsb-fe/eservice/INTERNET/MohBsbInboxApprovalFacAdmin?searchStatus=APPRSTA001">
                            <p class="dashboard-txt">Active Approvals</p>
                            <h1 class="dashboard-count">${activeApprovalAmt}</h1>
                        </a></div>
                    </div>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-xs-12">
                <div class="dashboard-footernote">

                    <p class="dashboard-small-txt"><strong>Last Login:</strong> 13/07/2021 | Last Activity: Inbox, On 13/07/2021
<%--  todo: recover in future, now use above hard code
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
                        </c:choose> , On <fmt:formatDate value="${INTER_INBOX_USER_INFO.lastLogin}" pattern="dd/MM/yyyy"/>
                    </p>
--%>
                </div>
            </div>
        </div>
    </div>
</div>