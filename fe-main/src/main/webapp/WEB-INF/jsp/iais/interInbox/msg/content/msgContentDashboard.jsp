<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%
    String webroot = IaisEGPConstant.CSS_ROOT + IaisEGPConstant.FE_CSS_ROOT;
%>
<div class="dashboard" style="background-image:url('<%=webroot%>img/Masthead-banner.jpg')">
    <div class="container">
        <div class="navigation-gp">
            <%@include file="msgMenuPage.jsp" %>
            <div class="row">
                <div class="col-xs-12">
                    <div class="dashboard-gp">
                        <div class="dashboard-tile-item">
                            <div class="dashboard-tile"><a data-tab="#tabInbox" href="#" onclick="cotToMsg()">
                                <p class="dashboard-txt">New Messages</p>
                                <h1 class="dashboard-count">${unreadAndresponseNum}</h1>
                            </a></div>
                        </div>
                        <div class="dashboard-tile-item">
                            <div class="dashboard-tile"><a data-tab="#tabApp" href="#" onclick="cotToApp()">
                                <p class="dashboard-txt">Application Drafts</p>
                                <h1 class="dashboard-count">&nbsp;${appDraftNum}</h1>
                            </a></div>
                        </div>
                        <div class="dashboard-tile-item">
                            <div class="dashboard-tile"><a data-tab="#tabLic" href="#" onclick="cotToLic()">
                                <p class="dashboard-txt">Active Licences</p>
                                <h1 class="dashboard-count">${licActiveNum}</h1>
                            </a></div>
                        </div>
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
        </div>
    </div>
</div>
