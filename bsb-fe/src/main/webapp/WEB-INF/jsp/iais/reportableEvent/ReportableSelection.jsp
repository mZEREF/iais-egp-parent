<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%@ taglib prefix="webui" uri="http://www.ecquaria.com/webui" %>
<%@ taglib prefix="ias" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="iais-bsb" uri="http://www.ecq.com/iais-bsb" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<link href="<%=WEB_ROOT%>/css/bsb/bsb-common.css" rel="stylesheet"/>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-reportable-event.js"></script>

<%@include file="/WEB-INF/jsp/iais/include/showErrorMsg.jsp"%>

<%@include file="common/dashboard.jsp"%>

<form method="post" id="mainForm" action="<%=process.runtime.continueURL()%>">
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="action_type" value="">
    <input type="hidden" name="action_value" value="">
    <input type="hidden" name="action_additional" value="">

    <div class="container">
        <div class="component-gp col-xs-12 col-sm-11 col-md-10 col-lg-8" style="margin-top: 60px">
            <div class="row">
                <div class="col-xs-12 col-md-1">
                </div>
                <div class="col-xs-12 col-md-10">
                    <h2>
                        Reportable Events
                    </h2>
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
                    <div class="self-assessment-checkbox-gp gradient-light-grey">
                        <p class="assessment-title">Select the Report Type(s) for which you wish to make this application</p>
                        <div class="form-check-gp">
                            <div class="form-check">
                                <input type="radio" class="form-check-input" name="reportType" id="incidentRadio" value="EVTYPE001"/>
                                <label for="incidentRadio" class="form-check-label"><span class="check-circle"></span>Notification of Incident Form</label>
                            </div>
                            <div class="form-check">
                                <input type="radio" class="form-check-input" name="reportType" id="investRadio" value="EVTYPE002"/>
                                <label for="investRadio" class="form-check-label"><span class="check-circle"></span>Investigation Report</label>
                            </div>
                            <div class="form-check">
                                <input type="radio" class="form-check-input" name="reportType" id="fp1ARadio" value="EVTYPE003"/>
                                <label for="fp1ARadio" class="form-check-label"><span class="check-circle"></span>Follow-up Report 1A</label>
                            </div>
                            <div class="form-check">
                                <input type="radio" class="form-check-input" name="reportType" id="fp1BRadio" value="EVTYPE004"/>
                                <label for="fp1BRadio" class="form-check-label"><span class="check-circle"></span>Follow-up Report 1B</label>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="application-tab-footer" style="margin-left:0px;margin-right:0px">
                <div class="row">
                    <div class="col-xs-12 col-sm-6 ">
                        <a class="back" id="back" href="/bsb-fe/eservice/INTERNET/MohBSBInboxMsg"><em class="fa fa-angle-left"></em> Back</a>
                    </div>
                    <div class="col-xs-12 col-sm-6">
                        <div class="button-group">
                            <a class="btn btn-primary next" id="next" >CONTINUE</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>