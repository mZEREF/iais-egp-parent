<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT"%>
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
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-incident.js"></script>
<%@include file="/WEB-INF/jsp/iais/include/showErrorMsg.jsp"%>

<%@include file="common/dashboard.jsp"%>
<form method="post" id="mainForm" action="<%=process.runtime.continueURL()%>">
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="action_type" value="">
    <input type="hidden" name="action_value" value="">
    <input type="hidden" name="action_additional" value="">
    <div class="main-content">
        <div class="container">
            <div class="row">
                <div class="col-lg-12 col-xs-12">
                    <div class="internet-content">
                        <div class="col-xs-12">
                            <div class="tab-gp dashboard-tab">
                                <%@include file="common/InnerNavTab.jsp"%>
                                <div class="tab-content">
                                    <div class="tab-pane fade in active" id="tabReportPerson" role="tabpanel" style="background-color: rgba(255, 255, 255, 1);border-radius: 15px;box-shadow: 0 0 15px #00000059;">
                                        <div class="panel panel-default">
                                            <div class="form-horizontal">
                                                <div class="container">
                                                    <div class="col-xs-12 col-md-10" style="margin: 20px 0"><h3>Person Reporting The Adverse Incident</h3></div>
                                                    <div class="col-xs-12 col-sm-11 col-md-12 col-lg-10">
                                                        <div class="row">
                                                            <div class="col-xs-12 col-md-10" style="margin-top: 20px">
                                                                <div class="form-group">
                                                                    <div class="col-sm-5 control-label">
                                                                        <label for="name">Name (as per NRIC/FIN)</label>
                                                                    </div>
                                                                    <div class="col-sm-6 col-md-7">
                                                                        <input type="text" maxlength="10" name="name" id="name" value="">
                                                                        <span data-err-ind="name" class="error-msg"></span>
                                                                    </div>
                                                                </div>
                                                                <div class="form-group">
                                                                    <div class="col-sm-5 control-label">
                                                                        <label for="orgName">Name of Organisation</label>
                                                                    </div>
                                                                    <div class="col-sm-6 col-md-7">
                                                                        <input type="text" maxlength="10" name="orgName" id="orgName" value="">
                                                                        <span data-err-ind="orgName" class="error-msg"></span>
                                                                    </div>
                                                                </div>
                                                                <div class="form-group">
                                                                    <div class="col-sm-5 control-label">
                                                                        <label for="address">Address</label>
                                                                    </div>
                                                                    <div class="col-sm-6 col-md-7">
                                                                        <input type="text" maxlength="10" name="address" id="address" value="">
                                                                        <span data-err-ind="address" class="error-msg"></span>
                                                                    </div>
                                                                </div>
                                                                <div class="form-group">
                                                                    <div class="col-sm-5 control-label">
                                                                        <label for="officeTelNo">Tel No. (office)</label>
                                                                    </div>
                                                                    <div class="col-sm-6 col-md-7">
                                                                        <input type="text" maxlength="10" name="officeTelNo" id="officeTelNo" value="">
                                                                        <span data-err-ind="officeTelNo" class="error-msg"></span>
                                                                    </div>
                                                                </div>
                                                                <div class="form-group">
                                                                    <div class="col-sm-5 control-label">
                                                                        <label for="mobileTelNo">Tel No. (mobile)</label>
                                                                        <span class="mandatory otherQualificationSpan">*</span>
                                                                    </div>
                                                                    <div class="col-sm-6 col-md-7">
                                                                        <input type="text" maxlength="10" name="mobileTelNo" id="mobileTelNo" value="">
                                                                        <span data-err-ind="mobileTelNo" class="error-msg"></span>
                                                                    </div>
                                                                </div>
                                                                <div class="form-group">
                                                                    <div class="col-sm-5 control-label">
                                                                        <label for="email">Email address</label>
                                                                    </div>
                                                                    <div class="col-sm-6 col-md-7">
                                                                        <input type="text" maxlength="10" name="email" id="email" value="">
                                                                        <span data-err-ind="email" class="error-msg"></span>
                                                                    </div>
                                                                </div>
                                                                <div class="form-group">
                                                                    <div class="col-sm-5 control-label">
                                                                        <label for="roleDesignation">Role & Designation</label>
                                                                    </div>
                                                                    <div class="col-sm-6 col-md-7">
                                                                        <input type="text" maxlength="10" name="roleDesignation" id="roleDesignation" value="">
                                                                        <span data-err-ind="roleDesignation" class="error-msg"></span>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="col-xs-12 col-md-10" style="margin: 20px 0"><h3>Incident Information</h3></div>
                                                    <div class="component-gp col-xs-12 col-sm-11 col-md-12 col-lg-10">
                                                        <div class="row">
                                                            <div class="col-xs-12 col-md-10" style="margin-top: 20px">
                                                                <div class="form-group">
                                                                    <div class="col-sm-5 control-label">
                                                                        <label for="facName">Facility name</label>
                                                                    </div>
                                                                    <div class="col-sm-6 col-md-7">
                                                                        <select name="facName" id="facName">
                                                                            <option value="">Please Select</option>
                                                                        </select>
                                                                        <span data-err-ind="facName" class="error-msg"></span>
                                                                    </div>
                                                                </div>
                                                                <div class="form-group">
                                                                    <div class="col-sm-5 control-label">
                                                                        <label for="facType">Facility Type</label>
                                                                    </div>
                                                                    <div class="col-sm-6 col-md-7">
                                                                        <input type="text" name="facType" id="facType" value="">
                                                                        <span data-err-ind="facType" class="error-msg"></span>
                                                                    </div>
                                                                </div>
                                                                <div class="form-group">
                                                                    <div class="col-sm-5 control-label">
                                                                        <label for="activityType">Activity Type(s)</label>
                                                                    </div>
                                                                    <div class="col-sm-6 col-md-7">
                                                                        <select name="activityType" id="activityType">
                                                                            <option value="">Please Select</option>
                                                                        </select>
                                                                        <span data-err-ind="activityType" class="error-msg"></span>
                                                                    </div>
                                                                </div>
                                                                <div class="form-group">
                                                                    <div class="col-sm-5 control-label">
                                                                        <label for="incidentDate">Date of Incident</label>
                                                                    </div>
                                                                    <div class="col-sm-6 col-md-7">
                                                                        <input type="text" autocomplete="off" name="incidentDate" id="incidentDate" data-date-start-date="01/01/1900"  placeholder="dd/mm/yyyy" maxlength="10" class="date_picker form-control"/>
                                                                        <span data-err-ind="incidentDate" class="error-msg"></span><span data-err-ind="officeTelNo" class="error-msg"></span>
                                                                    </div>
                                                                </div>
                                                                <div class="form-group">
                                                                    <div class="col-sm-5 control-label">
                                                                        <label>Time of Occurrence</label>
                                                                    </div>
                                                                    <div class="col-sm-6 col-md-7">
                                                                        <div class="col-md-10 col-lg-5 col-9">
                                                                            <select id="occurrenceTimeH" name="occurrenceTimeH">
                                                                                <option value="">--</option>
                                                                                <c:forEach var="item" items="${occurHHOps}">
                                                                                    <option value="${item.value}">${item.text}</option>
                                                                                </c:forEach>
                                                                            </select>
                                                                        </div>
                                                                        <div class="col-md-2 col-lg-1 col-3 label-padding">
                                                                            <label for="occurrenceTimeH">(HH)</label>
                                                                        </div>
                                                                        <div class="col-md-10 col-lg-5 col-9">
                                                                            <select id="occurrenceTimeM" name="occurrenceTimeH">
                                                                                <option value="">--</option>
                                                                                <c:forEach var="item" items="${occurMMOps}">
                                                                                    <option value="${item.value}">${item.text}</option>
                                                                                </c:forEach>
                                                                            </select>
                                                                        </div>
                                                                        <div class="col-md-2 col-lg-1 col-3 label-padding">
                                                                            <label for="occurrenceTimeM">(MM)</label>
                                                                        </div>
                                                                        <span data-err-ind="occurrenceTime" class="error-msg"></span>
                                                                    </div>
                                                                </div>

                                                                <div class="form-group">
                                                                    <div class="col-sm-5 control-label">
                                                                        <label for="location">Location</label>
                                                                    </div>
                                                                    <div class="col-sm-6 col-md-7">
                                                                        <input type="text" name="location" id="location" value="">
                                                                        <span data-err-ind="location" class="error-msg"></span>
                                                                    </div>
                                                                </div>

                                                                <div class="form-group">
                                                                    <div class="col-sm-5 control-label">
                                                                        <label for="batName">Name of Agent or Toxin Involved</label>
                                                                    </div>
                                                                    <div class="col-sm-6 col-md-7">
                                                                        <select name = "batName" id = "batName" multiple>
                                                                            <option>Please select</option>
                                                                        </select>
                                                                        <span data-err-ind="batName" class="error-msg"></span>
                                                                    </div>
                                                                </div>

                                                                <div class="form-group">
                                                                    <div class="col-sm-5 control-label">
                                                                        <label for="incidentDesc">Description of Incident</label>
                                                                        <a class="btn-tooltip styleguide-tooltip" href="javascript:void(0);" data-toggle="tooltip" data-html="true" title="" data-original-title="<p>Note:The driver of hte conveyance must have a valid Hazardous Materials Transport Driver Permit</p>">i</a>
                                                                    </div>
                                                                    <div class="col-sm-6 col-md-7">
                                                                        <textarea id="incidentDesc" rows="6" name="incidentDesc" maxlength="500"></textarea>
                                                                        <span data-err-ind="incidentDesc" class="error-msg"></span>
                                                                    </div>
                                                                </div>

                                                                <div class="form-group">
                                                                    <div class="col-sm-5 control-label">
                                                                        <label>Any possibility of BA/Toxin released beyond the containment facility?</label>
                                                                    </div>
                                                                    <div class="col-sm-6 col-md-7">
                                                                        <div class="col-sm-4" style="margin-top: 8px">
                                                                            <input type="radio" name="batReleasePossibility" id="possibleY" value="Y"/>
                                                                            <label for="possibleY">yes</label>
                                                                        </div>
                                                                        <div class="col-sm-4" style="margin-top: 8px">
                                                                            <input type="radio" name="batReleasePossibility" id="possibleN" value="N"/>
                                                                            <label for="possibleN">no</label>
                                                                        </div>
                                                                        <span data-err-ind="batReleasePossibility" class="error-msg"></span>
                                                                    </div>
                                                                </div>

                                                                <div id="releasePossibleY" style="display: none">
                                                                    <div class="form-group">
                                                                        <div class="col-sm-5 control-label">
                                                                            <label for="releaseExtent">Extent of release</label>
                                                                        </div>
                                                                        <div class="col-sm-6 col-md-7">
                                                                            <input type="text" name="releaseExtent" id="releaseExtent" value="" maxlength="120">
                                                                            <span data-err-ind="releaseExtent" class="error-msg"></span>
                                                                        </div>
                                                                    </div>
                                                                    <div class="form-group">
                                                                        <div class="col-sm-5 control-label">
                                                                            <label for="releaseMode">Mode of Release</label>
                                                                        </div>
                                                                        <div class="col-sm-6 col-md-7">
                                                                            <input type="text" name="releaseMode" id="releaseMode" value="" maxlength="120">
                                                                            <span data-err-ind="releaseMode" class="error-msg"></span>
                                                                        </div>
                                                                    </div>
                                                                </div>

                                                                <div class="form-group">
                                                                    <div class="col-sm-5 control-label">
                                                                        <label>Was any personnel involved in the incident?</label>
                                                                    </div>
                                                                    <div class="col-sm-6 col-md-7">
                                                                        <div class="col-sm-4" style="margin-top: 8px">
                                                                            <input type="radio" name="incidentPersonInvolved" id="involvedY" value="Y"/>
                                                                            <label for="involvedY">yes</label>
                                                                        </div>
                                                                        <div class="col-sm-4" style="margin-top: 8px">
                                                                            <input type="radio" name="incidentPersonInvolved" id="involvedN" value="N"/>
                                                                            <label for="involvedN">no</label>
                                                                        </div>
                                                                        <span data-err-ind="incidentPersonInvolved" class="error-msg"></span>
                                                                    </div>
                                                                </div>
                                                                <div class="form-group" id="involved" style="display: none">
                                                                    <div class="col-sm-5 control-label">
                                                                        <label for="incidentPersonInvolvedCount">Number of personnel involved or affected (including persons who were directly involved and indirectly affected e.g. persons who were present in the vicinity during a biological spill)</label>
                                                                    </div>
                                                                    <div class="col-sm-6 col-md-7">
                                                                        <label id="incidentPersonInvolvedCount">0</label>
                                                                    </div>
                                                                </div>
                                                                <div class="form-group">
                                                                    <div class="col-sm-5 control-label">
                                                                        <label for="emergencyResponse">Immediate emergency response that was taken</label>
                                                                    </div>
                                                                    <div class="col-sm-6 col-md-7">
                                                                        <input type="text" name="emergencyResponse" id="emergencyResponse" maxlength="200" value="">
                                                                        <span data-err-ind="emergencyResponse" class="error-msg"></span>
                                                                    </div>
                                                                </div>
                                                                <div class="form-group">
                                                                    <div class="col-sm-5 control-label">
                                                                        <label for="immCorrectiveAction">Immediate corrective action that was taken (if any)</label>
                                                                    </div>
                                                                    <div class="col-sm-6 col-md-7">
                                                                        <input type="text" name="immCorrectiveAction" id="immCorrectiveAction" maxlength="200" value="">
                                                                        <span data-err-ind="immCorrectiveAction" class="error-msg"></span>
                                                                    </div>
                                                                </div>

                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <%@include file="common/InnerFooter.jsp"%>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>


