<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="http://www.ecquaria.com/menu" prefix="menu" %>
<%@ taglib uri="ecquaria/sop/egov-smc" prefix="egov-smc" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.ecq.com/iais-bsb" prefix="iais-bsb" %>
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<link href="<%=WEB_ROOT%>/css/bsb/bsb-common.css" rel="stylesheet"/>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-inbox.js"></script>


<%@include file="../dashboard/dashboard.jsp"%>


<div class="main-content">
    <div class="container">
        <div class="row">
            <div class="col-xs-12">
                <div class="tab-gp dashboard-tab" style="margin-left: 6px;margin-right: -8px;">

                    <%--todo: make the nav bar determined by role logic--%>
                    <ul class="nav nav-tabs hidden-xs hidden-sm" role="tablist">
                        <li class="active" role="presentation" id="inboxTab">
                            <a href="#" aria-controls="tabInbox" role="tab" data-toggle="tab">Inbox (${unreadMsgAmt})</a></li>
                        <li class="complete" role="presentation" id="appTab">
                            <a href="#" aria-controls="tabApp" role="tab" data-toggle="tab">Applications</a>
                        </li>
                        <li class="incomplete" role="presentation" id="approvalsTab">
                            <a href="#" aria-controls="tabLic" role="tab" data-toggle="tab">Approvals</a>
                        </li>
                        <li class="incomplete" role="presentation" id="certTab">
                            <a href="#" aria-controls="tabLic" role="tab" data-toggle="tab">Certifications</a>
                        </li>
                        <li class="incomplete" role="presentation" id="dataSubTab">
                            <a href="#" aria-controls="tabLic" role="tab" data-toggle="tab">Data Submissions</a>
                        </li>
                        <li class="incomplete" role="presentation" id="reportEventTab">
                            <a href="#" aria-controls="tabLic" role="tab" data-toggle="tab">Report Events</a>
                        </li>
                    </ul>
                    <div class="tab-nav-mobile visible-xs visible-sm">
                        <div class="swiper-wrapper" role="tablist">
                            <div class="swiper-slide">
                                <a href="#" aria-controls="tabInbox" role="tab" data-toggle="tab">Inbox (${unreadMsgAmt})</a>
                            </div>
                            <div class="swiper-slide">
                                <a href="#tabApp" aria-controls="tabApplication" role="tab" data-toggle="tab">Applications</a>
                            </div>
                            <div class="swiper-slide">
                                <a href="#tabApprovals" aria-controls="tabLicence" role="tab" data-toggle="tab">Approvals</a>
                            </div>
                            <div class="swiper-slide">
                                <a href="#tabCert" aria-controls="tabLicence" role="tab" data-toggle="tab">Certifications</a>
                            </div>
                            <div class="swiper-slide">
                                <a href="#tabDSub" aria-controls="tabLicence" role="tab" data-toggle="tab">Data Submissions</a>
                            </div>
                            <div class="swiper-slide">
                                <a href="#tabRE" aria-controls="tabLicence" role="tab" data-toggle="tab">Report Events</a>
                            </div>
                        </div>
                        <div class="swiper-button-prev"></div>
                        <div class="swiper-button-next"></div>
                    </div>



                    <div class="tab-content">
                        <%@ include file="InboxMsgListPage.jsp" %>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>