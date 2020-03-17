<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%
    String webroot = IaisEGPConstant.CSS_ROOT + IaisEGPConstant.FE_CSS_ROOT;
%>
<div class="dashboard" style="background-image:url('<%=webroot%>img/Masthead-banner.jpg')">
    <div class="container">
        <div class="navigation-gp">
            <%@include file="licMenuPage.jsp" %>
            <div class="row">
                <div class="col-xs-12">
                    <div class="dashboard-gp">
                        <div class="dashboard-tile-item">
                            <div class="dashboard-tile"><a data-tab="#tabInbox" href="#" onclick="licToMsgPage()">
                                <p class="dashboard-txt">New Messages</p>
                                <h1 class="dashboard-count">${unreadAndresponseNum}</h1>
                            </a>
                            </div>
                        </div>
                        <div class="dashboard-tile-item">
                            <div class="dashboard-tile"><a data-tab="#tabApp" href="#" onclick="licToAppPage()">
                                <p class="dashboard-txt">Application Drafts</p>
                                <h1 class="dashboard-count">${appDraftNum}</h1>
                            </a></div>
                        </div>
                        <div class="dashboard-tile-item">
                            <div class="dashboard-tile"><a data-tab="#tabLic" href="#">
                                <p class="dashboard-txt">Active Licences</p>
                                <h1 class="dashboard-count">${licActiveNum}</h1>
                            </a></div>
                        </div>
                        <div class="dashboard-tile-item">
                            <div class="dashboard-tile txt-only">
                                <a href="#">
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
                        <p class="dashboard-small-txt"><strong>Last Login:</strong> 07 February 2020, 14: 39 | <strong>Last
                            Activity:</strong> Application For Renewal - Licence No. EP000-2020-2, On 01 Feb 2020</p>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
