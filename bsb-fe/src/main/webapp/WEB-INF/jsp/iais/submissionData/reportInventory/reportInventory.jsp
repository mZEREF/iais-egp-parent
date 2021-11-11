<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="http://www.ecquaria.com/menu" prefix="menu" %>
<%@ taglib uri="ecquaria/sop/egov-smc" prefix="egov-smc" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
    String webroot1 = IaisEGPConstant.CSS_ROOT + IaisEGPConstant.FE_CSS_ROOT;
%>
<webui:setLayout name="iais-internet"/>

<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-report-inventory.js"></script>

<%@include file="/WEB-INF/jsp/iais/include/showErrorMsg.jsp"%>

<form method="post" id="mainForm" enctype="multipart/form-data" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="action_type" value="">
    <input type="hidden" name="action_value" value="">
    <input type="hidden" name="action_additional" value="">
    <div class="main-content">
    <div class="row">
        <div class="col-lg-12 col-xs-12">
            <div class="center-content">
                <div class="internet-content">
                    <iais:body>
                        <div class="col-xs-12">
                            <div class="tab-gp dashboard-tab">
                                <ul class="nav nav-tabs hidden-xs hidden-sm" role="tablist">
                                    <li class="active" id="info" role="presentation">
                                        <a href="#tabFacInfo"
                                           id="infoa"
                                           aria-controls="tabFacInfo"
                                           role="tab"
                                           data-toggle="tab">Facility Info</a></li>
                                    <li class="complete" id="bat" role="presentation">
                                        <a href="#tabNotBat"
                                           id="bata"
                                           aria-controls="tabNotBat" role="tab"
                                           data-toggle="tab">Biological Agent/Toxin</a></li>
                                </ul>
                                <div class="tab-nav-mobile visible-xs visible-sm">
                                    <div class="swiper-wrapper" role="tablist">
                                        <div class="swiper-slide"><a href="#tabFacInfo" aria-controls="tabFacInfo"
                                                                     role="tab"
                                                                     data-toggle="tab">Facility Info</a>
                                        </div>
                                        <div class="swiper-slide"><a href="#tabNotBat" aria-controls="tabNotBat"
                                                                     role="tab" data-toggle="tab">Biological Agent/Toxin</a>
                                        </div>
                                    </div>
                                    <div class="swiper-button-prev"></div>
                                    <div class="swiper-button-next"></div>
                                </div>
                                <div class="tab-content">
                                    <div class="tab-pane active" id="tabFacInfo" role="tabpanel">
                                        <%@include file="../common/facilityInfo.jsp" %>
                                    </div>
                                    <div class="tab-pane" id="tabNotBat" role="tabpanel">
                                        <%@include file="reportDoc.jsp" %>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </iais:body>
                </div>
            </div>
        </div>
    </div>
</div>
    <div class="row" style="margin-bottom: 20px">
        <div class="container">
            <div class="col-xs-12 col-md-6 text-left">
                <a class="back" href="#"><em class="fa fa-angle-left"></em> Back</a>
            </div>
            <div class="col-xs-12 col-md-6 text-right">
                <button class="btn btn-secondary save" id="saveDraft">Save as Draft</button>
                <button class="btn btn-primary save" id="doConfirm">NEXT</button>
            </div>
        </div>
    </div>
</form>

