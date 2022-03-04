<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%@ taglib prefix="webui" uri="http://www.ecquaria.com/webui" %>
<%@ taglib prefix="ias" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="iais-bsb" uri="http://www.ecq.com/iais-bsb" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants" %>

<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<link href="<%=WEB_ROOT%>/css/bsb/bsb-common.css" rel="stylesheet"/>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common-facility-register.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-display-or-not.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-rfc-facility-register.js"></script>

<%@include file="/WEB-INF/jsp/iais/include/showErrorMsg.jsp"%>

<%@include file="../common/dashboard.jsp"%>

<form method="post" id="mainForm" action="<%=process.runtime.continueURL()%>">
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="action_type" value="">
    <input type="hidden" name="action_value" value="">
    <input type="hidden" name="action_additional" value="">

    <div class="container">
        <div class="component-gp col-xs-12 col-sm-11 col-md-10 col-lg-8" style="margin-top: -45px">
            <div class="row">
                <div class="col-xs-12 col-md-1">
                </div>
                <div class="col-xs-12 col-md-10">
                    <h3>
                        Select the Facility & Activity Type(s) for which you wish to make this application
                    </h3>
                </div>
            </div>
            <div class="row">
                <div class="col-xs-12 col-md-1">
                </div>
                <div class="col-xs-12 col-md-10">
                    <span data-err-ind="facClassification" class="error-msg"></span>
                    <br/>
                    <span data-err-ind="activityTypes" class="error-msg"></span>
                </div>
            </div>
            <div class="row">
                <div class="col-xs-12 col-md-1">
                </div>
                <div class="col-xs-12 col-md-10">
                    <%--@elvariable id="serviceSelection" type="sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilitySelectionDto"--%>
                    <div class="self-assessment-checkbox-gp gradient-light-grey">
                        <div class="text-right app-font-size-16"><a id="edit" href="javascript:void(0)"><em class="fa fa-pencil-square-o"></em>Edit</a></div>
                        <%@include file="../../mainAppCommon/facRegistration/facilitySelection.jsp"%>
                    </div>
                </div>
            </div>
            <div class="application-tab-footer" style="margin-left:0px;margin-right:0px">
                <div class="row">
                    <div class="col-xs-12 col-sm-6 ">
                        <a class="back" id="back" href="#"><em class="fa fa-angle-left"></em> Previous</a>
                    </div>
                    <div class="col-xs-12 col-sm-6">
                        <div class="button-group">
                            <a class="btn btn-primary next" id="next" >Next</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>