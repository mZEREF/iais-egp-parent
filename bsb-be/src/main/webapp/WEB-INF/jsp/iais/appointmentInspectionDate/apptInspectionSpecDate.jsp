<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>

<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>

<%
    String webroot = IaisEGPConstant.CSS_ROOT + IaisEGPConstant.BE_CSS_ROOT;
%>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-appointment.js"></script>
<div class="dashboard" style="background-image:url('<%=webroot%>img/Masthead-banner.jpg')">
    <form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
        <input type="hidden" name="apptInspectionDateType" value="">
        <input type="hidden" id="actionValue" name="actionValue" value="">
        <div class="main-content">
            <div class="row">
                <div class="col-lg-12 col-xs-12">
                    <div class="center-content">
                        <div class="intranet-content">
                            <div class="bg-title">
                                <h2>
                                    <span>Assign Specific Date</span>
                                </h2>
                            </div>
                            <%--@elvariable id="apptReviewData" type="sg.gov.moh.iais.egp.bsb.dto.appointment.AppointmentReviewDataDto"--%>
                            <iais:body>
                                <iais:section title="" id="inspection_date">
                                    <div class="row">
                                        <div class="col-md-1">
                                            <label style="font-size: 16px">Date<span style="color: red"> *</span></label>
                                        </div>
                                        <div class="col-md-6">
                                            <div class="col-xs-12 col-md-4">
                                                <input type="text" autocomplete="off" name="specifyStartDate" id="specifyStartDate" data-date-start-date="01/01/1900"  placeholder="dd/mm/yyyy" maxlength="10" class="date_picker form-control" value="${apptReviewData.specifyStartDate}"/>
                                                <span data-err-ind="specifyStartDate" class="error-msg" ></span>
                                            </div>
                                            <div class="col-xs-12 col-md-3">
                                                <iais:select name="specifyStartHour" options="startHoursOption" firstOption="--:--" value="${apptReviewData.specifyStartHour}"/>
                                                <span data-err-ind="specifyStartHour" class="error-msg" ></span>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-md-1">
                                            <label style="font-size: 16px"> </label>
                                        </div>
                                        <div class="col-md-6">
                                            <div class="col-xs-12 col-md-4">
                                                <span style="font-size: 16px">To</span>
                                                <p></p>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-md-1">
                                            <label style="font-size: 16px"></label>
                                        </div>
                                        <div class="col-md-6">
                                            <div class="col-xs-12 col-md-4">
                                                <input type="text" autocomplete="off" name="specifyEndDate" id="specifyEndDate" data-date-start-date="01/01/1900"  placeholder="dd/mm/yyyy" maxlength="10" class="date_picker form-control" value="${apptReviewData.specifyEndDate}"/>
                                                <span data-err-ind="specifyEndDate" class="error-msg" ></span>
                                            </div>
                                            <div class="col-xs-12 col-md-3">
                                                <iais:select name="specifyEndHour" options="endHoursOption" firstOption="--:--" value="${apptReviewData.specifyEndHour}"/>
                                                <span data-err-ind="specifyEndHour" class="error-msg" ></span>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-md-1">
                                            <label style="font-size: 16px"> </label>
                                        </div>
                                        <div class="col-md-6">
                                            <div class="col-xs-12 col-md-5">
                                                <span data-err-ind="errorSpecificDate" class="error-msg" ></span>
<%--                                                <span class="error-msg" name="iaisErrorMsg" id="error_specificDate"></span>--%>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row" style="margin-bottom: 200px;"></div>
                                    <iais:action>
                                        <button class="btn btn-primary" style="float:right" type="button" onclick="javascript:apptInspectionSpecDateConfirm()">Confirm</button>
                                        <a href="#" class="back" id="Back" onclick="javascript:apptInspectionSpecDateBack()" style="float:left"><em class="fa fa-angle-left"></em> Back</a>
                                    </iais:action>
                                </iais:section>
                            </iais:body>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </form>
</div>
<%@include file="/WEB-INF/jsp/iais/include/showErrorMsg.jsp" %>
