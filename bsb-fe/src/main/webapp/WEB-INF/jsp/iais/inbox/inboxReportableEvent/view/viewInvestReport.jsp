<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%@ taglib prefix="webui" uri="http://www.ecquaria.com/webui" %>
<%@ taglib prefix="ias" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="iais-bsb" uri="http://www.ecq.com/iais-bsb" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-report-event.js"></script>

<%@include file="/WEB-INF/jsp/iais/include/showErrorMsg.jsp" %>

<form method="post" id="mainForm" action="<%=process.runtime.continueURL()%>">
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="action_type" value="">
    <input type="hidden" name="action_value" value="">
    <input type="hidden" name="action_additional" value="">
    <div class="main-content">
        <div class="form-horizontal">
            <div class="container">
                <div class="row" style="margin-top: 30px">
                    <div class="col-xs-12">
                        <div class="tab-gp steps-tab">
                            <div class="tab-content">
                                <div class="tab-pane fade in active">
                                    <div id="previewSubmitPanel" role="tabpanel">
                                        <div class="preview-gp">
                                            <div class="row">
                                                <div class="col-xs-12">
                                                    <div class="panel-group" role="tablist" aria-multiselectable="true">
                                                        <div class="panel panel-default">
                                                            <div class="panel-heading completed">
                                                                <h4 class="panel-title">
                                                                    <a class="collapsed" data-toggle="collapse"
                                                                       href="#previewIncidentInfo">Incident Info</a>
                                                                </h4>
                                                            </div>
                                                            <div id="previewIncidentInfo" class="panel-collapse collapse">
                                                                <div class="panel-body">
                                                                    <div class="panel-main-content form-horizontal min-row">
                                                                        <c:set var="info" value="${view.incidentInfoInvestDto}"/>
                                                                        <div class="form-group">
                                                                            <div class="col-10"><h3>Investigation Report</h3></div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Incident
                                                                                Reference No.</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7">
                                                                                <p><c:out value="${info.referenceNo}"/></p>
                                                                            </div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Is
                                                                                the facility a Protected Place?</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7">
                                                                                <p><c:out value="${info.incidentType}"/></p>
                                                                            </div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <div class="col-10"><h3>Incident Information</h3></div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Facility Name</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7">
                                                                                <p><c:out value="${info.facName}"/></p>
                                                                            </div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Facility Type(s)</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7">
                                                                                <p><c:out value="${info.facType}"/></p>
                                                                            </div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Date of Incident</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7">
                                                                                <p><c:out value="${info.incidentEntityDate}"/></p>
                                                                            </div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Location</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7">
                                                                                <p><c:out value="${info.location}"/></p>
                                                                            </div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Name of Agent or Toxin involved</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7">
                                                                                <p><c:out value="${info.involvedBat}"/></p>
                                                                            </div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <div class="col-10"><h3>Investigation Team</h3></div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Lead Investigator</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7">
                                                                                <p><c:out value="${info.investigatorLead}"/></p>
                                                                            </div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Other Investigation(s)</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7">
                                                                                <p><c:out value="${info.otherInvestigator}"/></p>
                                                                            </div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                        <div class="panel panel-default">
                                                            <div class="panel-heading completed">
                                                                <h4 class="panel-title">
                                                                    <a class="collapsed" data-toggle="collapse"
                                                                       href="#previewIncidentInvest">Incident Investigation</a>
                                                                </h4>
                                                            </div>
                                                            <div id="previewIncidentInvest" class="panel-collapse collapse">
                                                                <div class="panel-body">
                                                                    <c:set var="invest" value="${view.incidentInvestDto}"/>
                                                                    <div class="form-group">
                                                                        <label class="col-xs-5 col-md-4 control-label">Background information leading up to the incident </label>
                                                                        <div class="col-sm-7 col-md-5 col-xs-7">
                                                                            <p><c:out value="${invest.backgroundInfo}"/></p>
                                                                        </div>
                                                                        <div class="clear"></div>
                                                                    </div>

                                                                    <div class="form-group">
                                                                        <label class="col-xs-5 col-md-4 control-label">Description of incident </label>
                                                                        <div class="col-sm-7 col-md-5 col-xs-7">
                                                                            <p><c:out value="${invest.incidentDesc}"/></p>
                                                                        </div>
                                                                        <div class="clear"></div>
                                                                    </div>

                                                                    <c:forEach var="item" items="${invest.incidentCauses}">
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Cause(s) of the incident (including probable cause)</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7">
                                                                                <p><c:out value="${item.incidentCause}"/></p>
                                                                            </div>
                                                                            <div class="clear"></div>
                                                                        </div>

                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Cause(s) of the Incident, Others</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7">
                                                                                <p><c:out
                                                                                        value="${item.otherCause}"/></p>
                                                                            </div>
                                                                            <div class="clear"></div>
                                                                        </div>

                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Explain the Cause</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7">
                                                                                <p><c:out
                                                                                        value="${item.explainCause}"/></p>
                                                                            </div>
                                                                            <div class="clear"></div>
                                                                        </div>

                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Corrective measures and/or preventive measures to address the probable</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7">
                                                                                <p><c:out
                                                                                        value="${item.measure}"/></p>
                                                                            </div>
                                                                            <div class="clear"></div>
                                                                        </div>

                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Due date for implementation of corrective and/or preventive measures</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7">
                                                                                <p><c:out
                                                                                        value="${item.implementDate}"/></p>
                                                                            </div>
                                                                            <div class="clear"></div>
                                                                        </div>

                                                                    </c:forEach>
                                                                </div>
                                                            </div>
                                                        </div>
                                                        <div class="panel panel-default">
                                                            <div class="panel-heading completed">
                                                                <h4 class="panel-title">
                                                                    <a class="collapsed" data-toggle="collapse"
                                                                       href="#previewInvolvedInfo">Medical Investigation</a>
                                                                </h4>
                                                            </div>
                                                            <div id="previewInvolvedInfo" class="panel-collapse collapse">
                                                                <div class="panel-body">
                                                                    <c:set var="medical" value="${view.medicalInvestDto}"/>
                                                                    <div class="form-group">
                                                                        <div class="col-10"><h3>Details of Medical Investigation</h3></div>
                                                                        <div class="clear"></div>
                                                                    </div>
                                                                    <div class="form-group">
                                                                        <label class="col-xs-5 col-md-4 control-label">Name of Personnel</label>
                                                                        <div class="col-sm-7 col-md-5 col-xs-7">
                                                                            <p><c:out value="${medical.personnelName}"/></p>
                                                                        </div>
                                                                        <div class="clear"></div>
                                                                    </div>

                                                                    <div class="form-group">
                                                                        <label class="col-xs-5 col-md-4 control-label">Updates on medical follow-up</label>
                                                                        <div class="col-sm-7 col-md-5 col-xs-7">
                                                                            <p><c:out value="${medical.medicalUpdate}"/></p>
                                                                        </div>
                                                                        <div class="clear"></div>
                                                                    </div>

                                                                    <div class="form-group">
                                                                        <label class="col-xs-5 col-md-4 control-label">Interpretation of test results</label>
                                                                        <div class="col-sm-7 col-md-5 col-xs-7">
                                                                            <p><c:out value="${medical.testResult}"/></p>
                                                                        </div>
                                                                        <div class="clear"></div>
                                                                    </div>

                                                                    <div class="form-group">
                                                                        <label class="col-xs-5 col-md-4 control-label">Is further medical follow-up advised/expected</label>
                                                                        <div class="col-sm-7 col-md-5 col-xs-7">
                                                                            <p><c:out
                                                                                    value="${medical.medicalFollowup}"/></p>
                                                                        </div>
                                                                        <div class="clear"></div>
                                                                    </div>

                                                                    <div class="form-group">
                                                                        <label class="col-xs-5 col-md-4 control-label">Estimated duration and frequency of follow-up</label>
                                                                        <div class="col-sm-7 col-md-5 col-xs-7">
                                                                            <p><c:out
                                                                                    value="${medical.fpDuration}"/></p>
                                                                        </div>
                                                                        <div class="clear"></div>
                                                                    </div>

                                                                    <div class="form-group">
                                                                        <div class="col-10"><h3>Additional Personnel Identified for Medical Investigation/Follow-up</h3></div>
                                                                        <div class="clear"></div>
                                                                    </div>
                                                                    <div class="form-group">
                                                                        <label class="col-xs-5 col-md-4 control-label">Were there persons who were not identified during Notification of Incident but were subsequently identified during the course of investigation? </label>
                                                                        <div class="col-sm-7 col-md-5 col-xs-7">
                                                                            <p><c:out value="${medical.isIdentified}"/></p>
                                                                        </div>
                                                                        <div class="clear"></div>
                                                                    </div>

                                                                    <div class="form-group">
                                                                        <label class="col-xs-5 col-md-4 control-label">Name of Personnel</label>
                                                                        <div class="col-sm-7 col-md-5 col-xs-7">
                                                                            <p><c:out
                                                                                    value="${medical.addPersonnelName}"/></p>
                                                                        </div>
                                                                        <div class="clear"></div>
                                                                    </div>

                                                                    <div class="form-group">
                                                                        <label class="col-xs-5 col-md-4 control-label">Description of involvement</label>
                                                                        <div class="col-sm-7 col-md-5 col-xs-7">
                                                                            <p><c:out value="${medical.involvementDesc}"/></p>
                                                                        </div>
                                                                        <div class="clear"></div>
                                                                    </div>

                                                                    <div class="form-group">
                                                                        <label class="col-xs-5 col-md-4 control-label">Description</label>
                                                                        <div class="col-sm-7 col-md-5 col-xs-7">
                                                                            <p><c:out value="${medical.description}"/></p>
                                                                        </div>
                                                                        <div class="clear"></div>
                                                                    </div>

                                                                    <div class="form-group">
                                                                        <label class="col-xs-5 col-md-4 control-label">Is further medical follow-up advised/expected</label>
                                                                        <div class="col-sm-7 col-md-5 col-xs-7">
                                                                            <p><c:out value="${medical.addTestResult}"/></p>
                                                                        </div>
                                                                        <div class="clear"></div>
                                                                    </div>

                                                                    <div class="form-group">
                                                                        <label class="col-xs-5 col-md-4 control-label">Estimated duration and frequency of follow-up</label>
                                                                        <div class="col-sm-7 col-md-5 col-xs-7">
                                                                            <p><c:out
                                                                                    value="${medical.addMedicalFollowup}"/></p>
                                                                        </div>
                                                                        <div class="clear"></div>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                        <div class="panel panel-default">
                                                            <div class="panel-heading completed">
                                                                <h4 class="panel-title">
                                                                    <a class="collapsed" data-toggle="collapse"
                                                                       href="#previewDocuments">Documents</a>
                                                                </h4>
                                                            </div>
                                                            <div id="previewDocuments" class="panel-collapse collapse">
                                                                <div class="panel-body">
                                                                    <div class="panel-main-content form-horizontal min-row">
                                                                        <div class="form-group">
                                                                            <div class="col-10"><h3>Uploaded Documents</h3></div>
                                                                            <div class="clear"></div>
                                                                        </div>

                                                                        <c:forEach var="doc" items="${docSettings}">
                                                                            <c:set var="maskDocType"><iais:mask name="file" value="${doc.type}"/></c:set>
                                                                            <c:set var="savedFileList" value="${docMap.get(doc.type)}" />
                                                                            <c:if test="${not empty savedFileList}">
                                                                                <div class="form-group">
                                                                                    <div class="col-10"><strong>${doc.typeDisplay}</strong></div>
                                                                                    <div class="clear"></div>
                                                                                </div>
                                                                                <div>
                                                                                    <c:forEach var="file" items="${savedFileList}">
                                                                                        <c:set var="tmpId"><iais:mask name="file" value="${file.repoId}"/></c:set>
                                                                                        <div class="form-group">
                                                                                            <a href="/bsb-web/ajax/doc/download/reportableEvent/view/${tmpId}" style="text-decoration: underline"><span id="${tmpId}Span">${file.filename}</span></a>(<fmt:formatNumber value="${file.size/1024.0}" type="number" pattern="0.0"/>KB)
                                                                                            <div class="clear"></div>
                                                                                        </div>
                                                                                    </c:forEach>
                                                                                </div>
                                                                            </c:if>
                                                                        </c:forEach>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-xs-12">
                        <a class="back" href="/bsb-web/eservice/INTERNET/MohBSBInboxApp"><em class="fa fa-angle-left"></em>
                            Previous</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>