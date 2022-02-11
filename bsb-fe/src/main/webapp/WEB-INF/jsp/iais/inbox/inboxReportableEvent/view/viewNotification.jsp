<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%@ taglib prefix="webui" uri="http://www.ecquaria.com/webui" %>
<%@ taglib prefix="ias" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="iais-bsb" uri="http://www.ecq.com/iais-bsb" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.MaskUtil" %>


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
                                                                        <c:set var="info" value="${view.incidentInfoDto}"/>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Incident
                                                                                Reference No.</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7">
                                                                                <p><c:out value="${info.referenceNo}"/></p>
                                                                            </div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Reporting
                                                                                of Incident</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7">
                                                                                <p><iais:code code="${info.incidentReporting}"/></p>
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
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                        <div class="panel panel-default">
                                                            <div class="panel-heading completed">
                                                                <h4 class="panel-title">
                                                                    <a class="collapsed" data-toggle="collapse"
                                                                       href="#previewReportInfo">Person Reporting Info</a>
                                                                </h4>
                                                            </div>
                                                            <div id="previewReportInfo" class="panel-collapse collapse">
                                                                <div class="panel-body">
                                                                    <c:set var="report" value="${view.reportingInfoDto}"/>
                                                                    <div class="form-group">
                                                                        <div class="col-10"><strong>Person Reporting The Adverse
                                                                            Incident</strong></div>
                                                                        <div class="clear"></div>
                                                                    </div>
                                                                    <div class="form-group">
                                                                        <label class="col-xs-5 col-md-4 control-label">Name
                                                                            (as per NRIC/FIN)</label>
                                                                        <div class="col-sm-7 col-md-5 col-xs-7">
                                                                            <p><c:out value="${report.name}"/></p>
                                                                        </div>
                                                                        <div class="clear"></div>
                                                                    </div>

                                                                    <div class="form-group">
                                                                        <label class="col-xs-5 col-md-4 control-label">Name
                                                                            of Organization</label>
                                                                        <div class="col-sm-7 col-md-5 col-xs-7">
                                                                            <p><c:out value="${report.orgName}"/></p>
                                                                        </div>
                                                                        <div class="clear"></div>
                                                                    </div>

                                                                    <div class="form-group">
                                                                        <label class="col-xs-5 col-md-4 control-label">Address</label>
                                                                        <div class="col-sm-7 col-md-5 col-xs-7">
                                                                            <p><c:out value="${report.address}"/></p>
                                                                        </div>
                                                                        <div class="clear"></div>
                                                                    </div>

                                                                    <div class="form-group">
                                                                        <label class="col-xs-5 col-md-4 control-label">Tel
                                                                            No. (Office)</label>
                                                                        <div class="col-sm-7 col-md-5 col-xs-7">
                                                                            <p><c:out
                                                                                    value="${report.officeTelNo}"/></p>
                                                                        </div>
                                                                        <div class="clear"></div>
                                                                    </div>

                                                                    <div class="form-group">
                                                                        <label class="col-xs-5 col-md-4 control-label">Tel
                                                                            No. (Mobile)</label>
                                                                        <div class="col-sm-7 col-md-5 col-xs-7">
                                                                            <p><c:out
                                                                                    value="${report.mobileTelNo}"/></p>
                                                                        </div>
                                                                        <div class="clear"></div>
                                                                    </div>

                                                                    <div class="form-group">
                                                                        <label class="col-xs-5 col-md-4 control-label">Email
                                                                            Address</label>
                                                                        <div class="col-sm-7 col-md-5 col-xs-7">
                                                                            <p><c:out value="${report.email}"/></p>
                                                                        </div>
                                                                        <div class="clear"></div>
                                                                    </div>

                                                                    <div class="form-group">
                                                                        <label class="col-xs-5 col-md-4 control-label">Role
                                                                            & Designation</label>
                                                                        <div class="col-sm-7 col-md-5 col-xs-7">
                                                                            <p><c:out
                                                                                    value="${report.roleDesignation}"/></p>
                                                                        </div>
                                                                        <div class="clear"></div>
                                                                    </div>
                                                                    <div class="form-group" style="margin-top: 20px">
                                                                        <div class="col-10"><strong>Incident
                                                                            Information</strong></div>
                                                                        <div class="clear"></div>
                                                                    </div>
                                                                    <div class="form-group">
                                                                        <label class="col-xs-5 col-md-4 control-label">Facility
                                                                            Name</label>
                                                                        <div class="col-sm-7 col-md-5 col-xs-7">
                                                                            <p><c:out value="${report.facName}"/></p>
                                                                        </div>
                                                                        <div class="clear"></div>
                                                                    </div>
                                                                    <div class="form-group">
                                                                        <label class="col-xs-5 col-md-4 control-label">Facility
                                                                            Type(s)</label>
                                                                        <div class="col-sm-7 col-md-5 col-xs-7">
                                                                            <p><c:out value="${report.facType}"/></p>
                                                                        </div>
                                                                        <div class="clear"></div>
                                                                    </div>
                                                                    <div class="form-group">
                                                                        <label class="col-xs-5 col-md-4 control-label">Activity
                                                                            Type(s)</label>
                                                                        <div class="col-sm-7 col-md-5 col-xs-7">
                                                                            <p><iais:code code="${report.activityType}"/></p>
                                                                        </div>
                                                                        <div class="clear"></div>
                                                                    </div>
                                                                    <div class="form-group">
                                                                        <label class="col-xs-5 col-md-4 control-label">Date
                                                                            of Incident</label>
                                                                        <div class="col-sm-7 col-md-5 col-xs-7">
                                                                            <p><c:out
                                                                                    value="${report.entityIncidentEDate}"/></p>
                                                                        </div>
                                                                        <div class="clear"></div>
                                                                    </div>
                                                                    <div class="form-group">
                                                                        <label class="col-xs-5 col-md-4 control-label">Time
                                                                            of Occurence</label>
                                                                        <div class="col-sm-7 col-md-5 col-xs-7">
                                                                            <p><c:out
                                                                                    value="${report.entityIncidentEDate}"/></p>
                                                                        </div>
                                                                        <div class="clear"></div>
                                                                    </div>
                                                                    <div class="form-group">
                                                                        <label class="col-xs-5 col-md-4 control-label">Location</label>
                                                                        <div class="col-sm-7 col-md-5 col-xs-7">
                                                                            <p><c:out value="${report.location}"/></p>
                                                                        </div>
                                                                        <div class="clear"></div>
                                                                    </div>
                                                                    <div class="form-group">
                                                                        <label class="col-xs-5 col-md-4 control-label">Name
                                                                            of Agent or Toxin involved</label>
                                                                        <div class="col-sm-7 col-md-5 col-xs-7">
                                                                            <p><c:out value="${report.batNames}"/></p>
                                                                        </div>
                                                                        <div class="clear"></div>
                                                                    </div>
                                                                    <div class="form-group">
                                                                        <label class="col-xs-5 col-md-4 control-label">Description
                                                                            of Incident</label>
                                                                        <div class="col-sm-7 col-md-5 col-xs-7">
                                                                            <p><c:out
                                                                                    value="${report.incidentDesc}"/></p>
                                                                        </div>
                                                                        <div class="clear"></div>
                                                                    </div>
                                                                    <div class="form-group">
                                                                        <label class="col-xs-5 col-md-4 control-label">Any
                                                                            possibility of BA/Toxin released beyond the
                                                                            containment facility?</label>
                                                                        <div class="col-sm-7 col-md-5 col-xs-7">
                                                                            <p><c:out
                                                                                    value="${report.batReleasePossibility}"/></p>
                                                                        </div>
                                                                        <div class="clear"></div>
                                                                    </div>
                                                                    <div class="form-group">
                                                                        <label class="col-xs-5 col-md-4 control-label">Was
                                                                            any personnel involved in the
                                                                            incident?</label>
                                                                        <div class="col-sm-7 col-md-5 col-xs-7">
                                                                            <p><c:out
                                                                                    value="${report.incidentPersonInvolved}"/></p>
                                                                        </div>
                                                                        <div class="clear"></div>
                                                                    </div>
                                                                    <div class="form-group">
                                                                        <label class="col-xs-5 col-md-4 control-label">Immediate
                                                                            emergency response that was taken</label>
                                                                        <div class="col-sm-7 col-md-5 col-xs-7">
                                                                            <p><c:out
                                                                                    value="${report.emergencyResponse}"/></p>
                                                                        </div>
                                                                        <div class="clear"></div>
                                                                    </div>
                                                                    <div class="form-group">
                                                                        <label class="col-xs-5 col-md-4 control-label">Immediate
                                                                            corrective action that was taken (if
                                                                            any)</label>
                                                                        <div class="col-sm-7 col-md-5 col-xs-7">
                                                                            <p><c:out
                                                                                    value="${report.immCorrectiveAction}"/></p>
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
                                                                       href="#previewInvolvedInfo">Person(s) Involved Info</a>
                                                                </h4>
                                                            </div>
                                                            <div id="previewInvolvedInfo" class="panel-collapse collapse">
                                                                <div class="panel-body">
                                                                    <c:set var="persons" value="${view.personInvolvedInfos}"/>
                                                                    <c:forEach var="item" items="${persons}" varStatus="status">
                                                                        <div class="form-group">
                                                                            <div class="col-10"><h3>Person ${status.index+1}</h3></div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <div class="col-10"><strong>Person(s) Involved in the Adverse Incident</strong></div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Name</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7">
                                                                                <p><c:out value="${item.name}"/></p>
                                                                            </div>
                                                                            <div class="clear"></div>
                                                                        </div>

                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Gender</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7">
                                                                                <p><c:out value="${item.gender}"/></p>
                                                                            </div>
                                                                            <div class="clear"></div>
                                                                        </div>

                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Tel No</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7">
                                                                                <p><c:out value="${item.telNo}"/></p>
                                                                            </div>
                                                                            <div class="clear"></div>
                                                                        </div>

                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Designation</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7">
                                                                                <p><c:out
                                                                                        value="${item.designation}"/></p>
                                                                            </div>
                                                                            <div class="clear"></div>
                                                                        </div>

                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Was the personnel injured</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7">
                                                                                <p><c:out
                                                                                        value="${item.personnelInjured}"/></p>
                                                                            </div>
                                                                            <div class="clear"></div>
                                                                        </div>

                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Personnel Involvement</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7">
                                                                                <p><c:out value="${item.personnelInvolvement}"/></p>
                                                                            </div>
                                                                            <div class="clear"></div>
                                                                        </div>

                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Description of involvement e.g. type of injury, exposure to biological agent</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7">
                                                                                <p><c:out
                                                                                        value="${item.involvementDesc}"/></p>
                                                                            </div>
                                                                            <div class="clear"></div>
                                                                        </div>

                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Was the person involved sent for medical consultation/treatment</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7">
                                                                                <p><c:out value="${item.medicalPerson}"/></p>
                                                                            </div>
                                                                            <div class="clear"></div>
                                                                        </div>

                                                                        <div class="form-group" style="margin-top: 20px">
                                                                            <div class="col-10"><strong>Details Related to Medical Consultation/Treatment</strong></div>
                                                                            <div class="clear"></div>
                                                                        </div>

                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Name of Medical Practitioner</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7">
                                                                                <p><c:out value="${item.practitionerName}"/></p>
                                                                            </div>
                                                                            <div class="clear"></div>
                                                                        </div>

                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Name of Hospital/Clinic where medical consultation was sought</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7">
                                                                                <p><c:out value="${item.hospitalName}"/></p>
                                                                            </div>
                                                                            <div class="clear"></div>
                                                                        </div>

                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Description</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7">
                                                                                <p><c:out
                                                                                        value="${item.medicalDesc}"/></p>
                                                                            </div>
                                                                            <div class="clear"></div>
                                                                        </div>

                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Is subsequent medical follow-up required/advised?</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7">
                                                                                <p><c:out
                                                                                        value="${item.medicalFollowup}"/></p>
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
                                                                            <c:set var="maskDocType" value="${MaskUtil.maskValue('file', doc.type)}"/>
                                                                            <c:set var="savedFileList" value="${docMap.get(doc.type)}" />
                                                                            <c:if test="${not empty savedFileList}">
                                                                                <div class="form-group">
                                                                                    <div class="col-10"><strong>${doc.typeDisplay}</strong></div>
                                                                                    <div class="clear"></div>
                                                                                </div>
                                                                                <div>
                                                                                    <c:forEach var="file" items="${savedFileList}">
                                                                                        <c:set var="tmpId" value="${MaskUtil.maskValue('file', file.repoId)}"/>
                                                                                        <div class="form-group">
                                                                                            <div class="col-10"><p><a href="javascript:void(0)" onclick="downloadFile('view', '${tmpId}')">${file.filename}</a>(${String.format("%.1f", file.size/1024.0)}KB)</p></div>
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
                        <a class="back" href="/bsb-fe/eservice/INTERNET/MohBSBInboxApp"><em class="fa fa-angle-left"></em>
                            Back</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>