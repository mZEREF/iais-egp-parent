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
                                    <div class="tab-pane fade in active" id="tabIncidentInfo" role="tabpanel" style="background-color: rgba(255, 255, 255, 1);border-radius: 15px;box-shadow: 0 0 15px #00000059;">
                                        <div class="panel panel-default">
                                            <div class="form-horizontal">
                                                <div class="container">
                                                    <div class="component-gp col-xs-12 col-sm-11 col-md-12 col-lg-10">
                                                        <div class="row">
                                                            <div class="col-xs-12 col-md-10" style="margin-top: 20px">
                                                                <div class="col-xs-12 col-md-10" style="margin: 20px 0"><h3>Investigation Report</h3></div>
                                                                <div class="form-group">
                                                                    <div class="col-sm-5 control-label">
                                                                        <label for="referenceNo">Incident Reference No.</label>
                                                                    </div>
                                                                    <div class="col-sm-6 col-md-7">
                                                                        <label id="referenceNo">
                                                                            <a href="#" onclick="openIncident('${maskedEditId}')">${incidentDto.referenceNo}</a></label>
                                                                    </div>
                                                                </div>
                                                                <div class="form-group ">
                                                                    <div class="col-sm-5 control-label">
                                                                        <label for="incidentType">Type of Incident</label>
                                                                    </div>
                                                                    <div class="col-sm-6 col-md-7">
                                                                        <label id="incidentType">${incidentDto.incidentType}</label>
                                                                    </div>
                                                                </div>
                                                                <div class="col-xs-12 col-md-10" style="margin: 20px 0"><h3>Incident Information</h3></div>
                                                                <div class="form-group ">
                                                                    <div class="col-sm-5 control-label">
                                                                        <label for="facName">Facility Name</label>
                                                                    </div>
                                                                    <div class="col-sm-6 col-md-7">
                                                                        <label id="facName">${incidentDto.facName}</label>
                                                                    </div>
                                                                </div>
                                                                <div class="form-group ">
                                                                    <div class="col-sm-5 control-label">
                                                                        <label for="facTypes">Facility Type(s)</label>
                                                                    </div>
                                                                    <div class="col-sm-6 col-md-7">
                                                                        <label id="facTypes">${incidentDto.facType}</label>
                                                                    </div>
                                                                </div>
                                                                <div class="form-group ">
                                                                    <div class="col-sm-5 control-label">
                                                                        <label for="incidentDate">Date of Incident</label>
                                                                    </div>
                                                                    <div class="col-sm-6 col-md-7">
                                                                        <label id="incidentDate">${incidentDto.incidentDate}</label>
                                                                    </div>
                                                                </div>
                                                                <div class="form-group ">
                                                                    <div class="col-sm-5 control-label">
                                                                        <label for="location">Location</label>
                                                                    </div>
                                                                    <div class="col-sm-6 col-md-7">
                                                                        <label id="location">${incidentDto.location}</label>
                                                                    </div>
                                                                </div>
                                                                <div class="form-group ">
                                                                    <div class="col-sm-5 control-label">
                                                                        <label for="batName">Name of Agent or Toxin Involved</label>
                                                                    </div>
                                                                    <div class="col-sm-6 col-md-7">
                                                                        <label id="batName">${incidentDto.batNames}</label>
                                                                    </div>
                                                                </div>
                                                                <div class="col-xs-12 col-md-10" style="margin: 20px 0"><h3>Investigation Team</h3></div>
                                                                <div class="form-group ">
                                                                    <div class="col-sm-5 control-label">
                                                                        <label for="investLeader">Lead Investigator</label>
                                                                        <span class="mandatory otherQualificationSpan">*</span>
                                                                    </div>
                                                                    <div class="col-sm-6 col-md-7">
                                                                        <input type="text" autocomplete="off" name="investLeader" id="investLeader" value='<c:out value="${incidentInfo.investLeader}"/>'/>
                                                                        <span data-err-ind="investLeader" class="error-msg"></span>
                                                                    </div>
                                                                </div>
                                                                <div class="form-group ">
                                                                    <div class="col-sm-5 control-label">
                                                                        <label for="otherInvest">Other Investigator(s)</label>
                                                                        <span class="mandatory otherQualificationSpan">*</span>
                                                                    </div>
                                                                    <div class="col-sm-6 col-md-7">
                                                                        <input type="text" autocomplete="off" name="otherInvest" id="otherInvest" value='<c:out value="${incidentInfo.otherInvest}"/>'/>
                                                                        <span data-err-ind="otherInvest" class="error-msg"></span>
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

                                    <%@include file="/WEB-INF/jsp/iais/include/jumpAfterDraft.jsp"%>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>

