<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%@ taglib prefix="webui" uri="http://www.ecquaria.com/webui" %>
<%@ taglib prefix="ias" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="iais-bsb" uri="http://www.ecq.com/iais-bsb" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.lang.String" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.MaskUtil" %>

<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<link href="<%=WEB_ROOT%>/css/bsb/bsb-common.css" rel="stylesheet"/>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-incident.js"></script>

<%@include file="/WEB-INF/jsp/iais/include/showErrorMsg.jsp"%>

<%@include file="common/dashboard.jsp" %>
<form method="post" id="mainForm" action="<%=process.runtime.continueURL()%>">
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="action_type" value="">
    <input type="hidden" name="action_value" value="">
    <input type="hidden" name="action_additional" value="">
    <div class="main-content">
        <div class="form-horizontal">
            <div class="container">
                <div class="row">
                    <div class="col-xs-12">
                        <h3 style="border-bottom: 1px solid #BABABA;margin-top: 20px">Preview & Submit</h3>
                        <div class="preview-gp">
                            <div class="row">
                                <div class="col-xs-12">
                                    <div class="form-horizontal">
                                       <h3 style="margin: 10px 0;border-bottom: 0px">Preview Investigation Report</h3>
                                        <div class="form-group">
                                            <div class="col-sm-5 control-label">
                                                <label for="referNo">Incident Reference No.</label>
                                            </div>
                                            <div class="col-sm-6 col-md-7">
                                                <label id="referNo">${incidentDto.referenceNo}</label>
                                            </div>
                                        </div>

                                        <div class="form-group ">
                                            <div class="col-sm-5 control-label">
                                                <label>Type of Incident(s)</label>
                                            </div>
                                            <div class="col-sm-6 col-md-7">
                                                <label>${incidentDto.incidentType}</label>
                                            </div>
                                        </div>
                                        <h3 style="margin: 10px 0;border-bottom: 0px">Incident Information</h3>
                                        <div class="form-group ">
                                            <div class="col-sm-5 control-label">
                                                <label>Facility Name</label>
                                            </div>
                                            <div class="col-sm-6 col-md-7">
                                                <label>${incidentDto.facName}</label>
                                            </div>
                                        </div>

                                        <div class="form-group ">
                                            <div class="col-sm-5 control-label">
                                                <label>Facility Type(s)</label>
                                            </div>
                                            <div class="col-sm-6 col-md-7">
                                                <label>${incidentDto.facType}</label>
                                            </div>
                                        </div>

                                        <div class="form-group ">
                                            <div class="col-sm-5 control-label">
                                                <label>Date of Incident</label>
                                            </div>
                                            <div class="col-sm-6 col-md-7">
                                                <label>${incidentDto.incidentDate}</label>
                                            </div>
                                        </div>

                                        <div class="form-group ">
                                            <div class="col-sm-5 control-label">
                                                <label>Location</label>
                                            </div>
                                            <div class="col-sm-6 col-md-7">
                                                <label>${incidentDto.location}</label>
                                            </div>
                                        </div>

                                        <div class="form-group ">
                                            <div class="col-sm-5 control-label">
                                                <label>Name of Agent or Toxin Involved</label>
                                            </div>
                                            <div class="col-sm-6 col-md-7">
                                                <label>${incidentDto.batNames}</label>
                                            </div>
                                        </div>

                                        <h3 style="margin: 10px 0;border-bottom: 0px">Investigation Team</h3>
                                        <div class="form-group ">
                                            <div class="col-sm-5 control-label">
                                                <label>Lead Investigator</label>
                                            </div>
                                            <div class="col-sm-6 col-md-7">
                                                <label>${incidentInfo.investLeader}</label>
                                            </div>
                                        </div>
                                        <div class="form-group ">
                                            <div class="col-sm-5 control-label">
                                                <label>Other Investigator(s)</label>
                                            </div>
                                            <div class="col-sm-6 col-md-7">
                                                <label>${incidentInfo.otherInvest}</label>
                                            </div>
                                        </div>
                                        <h3 style="margin: 10px 0;border-bottom: 0">Details of Incident Investigation</h3>
                                        <div class="form-group ">
                                            <div class="col-sm-5 control-label">
                                                <label>Background information leading up to the incident</label>
                                            </div>
                                            <div class="col-sm-6 col-md-7">
                                                <label>${incidentInvest.backgroundInfo}</label>
                                            </div>
                                        </div>

                                        <div class="form-group ">
                                            <div class="col-sm-5 control-label">
                                                <label>Description of incident</label>
                                            </div>
                                            <div class="col-sm-6 col-md-7">
                                                <label>${incidentInvest.incidentDesc}</label>
                                            </div>
                                        </div>

                                        <c:forEach var="item" items="${incidentInvest.incidentCauses}">
                                            <div class="form-group ">
                                                <div class="col-sm-5 control-label">
                                                    <label>Cause(s) of the incident (including probable cause)</label>
                                                </div>
                                                <div class="col-sm-6 col-md-7">
                                                    <label>${item.incidentCause}</label>
                                                </div>
                                            </div>

                                            <c:if test="${item.otherCause ne null}">
                                                <div class="form-group ">
                                                    <div class="col-sm-5 control-label">
                                                        <label>Cause(s) of the Incident, Others</label>
                                                    </div>
                                                    <div class="col-sm-6 col-md-7">
                                                        <label>${item.otherCause}</label>
                                                    </div>
                                                </div>
                                            </c:if>

                                            <div class="form-group ">
                                                <div class="col-sm-5 control-label">
                                                    <label>Explain the Cause</label>
                                                </div>
                                                <div class="col-sm-6 col-md-7">
                                                    <label>${item.explainCause}</label>
                                                </div>
                                            </div>

                                            <div class="form-group ">
                                                <div class="col-sm-5 control-label">
                                                    <label>Corrective measures and/or preventive measures to address the probable</label>
                                                </div>
                                                <div class="col-sm-6 col-md-7">
                                                    <label>${item.measure}</label>
                                                </div>
                                            </div>

                                            <div class="form-group ">
                                                <div class="col-sm-5 control-label">
                                                    <label>Due date for implementation of corrective and/or preventive measures</label>
                                                </div>
                                                <div class="col-sm-6 col-md-7">
                                                    <label>${item.implementDate}</label>
                                                </div>
                                            </div>

                                        </c:forEach>
                                        <h3 style="margin: 10px 0;border-bottom: 0px">Details of Medical Investigation</h3>
                                        <div class="form-group ">
                                            <div class="col-sm-5 control-label">
                                                <label>Name of Personnel</label>
                                            </div>
                                            <div class="col-sm-6 col-md-7">
                                                <label>${medicalInvest.personnelName}</label>
                                            </div>
                                        </div>

                                        <div class="form-group ">
                                            <div class="col-sm-5 control-label">
                                                <label>Updates on medical follow-up</label>
                                            </div>
                                            <div class="col-sm-6 col-md-7">
                                                <label>${medicalInvest.medicalUpdate}</label>
                                            </div>
                                        </div>

                                        <div class="form-group ">
                                            <div class="col-sm-5 control-label">
                                                <label>Interpretation of test results</label>
                                            </div>
                                            <div class="col-sm-6 col-md-7">
                                                <label>${medicalInvest.testResult}</label>
                                            </div>
                                        </div>

                                        <div class="form-group ">
                                            <div class="col-sm-5 control-label">
                                                <label>Is further medical follow-up advised/expected</label>
                                            </div>
                                            <div class="col-sm-6 col-md-7">
                                                <c:if test="${medicalInvest.medicalFollowup eq 'Y'}"><label>Yes</label></c:if>
                                                <c:if test="${medicalInvest.medicalFollowup eq 'N'}"><label>No</label></c:if>
                                            </div>
                                        </div>

                                        <div class="form-group ">
                                            <div class="col-sm-5 control-label">
                                                <label>Estimated duration and frequency of follow-up</label>
                                            </div>
                                            <div class="col-sm-6 col-md-7">
                                                <label>${medicalInvest.fpDuration}</label>
                                            </div>
                                        </div>
                                        <h3 style="margin: 10px 0;border-bottom: 0px">Additional Personnel Identified for Medical Investigation/Follow-up</h3>
                                        <div class="form-group ">
                                            <div class="col-sm-5 control-label">
                                                <label>Were there persons who were not identified during Notification of Incident but were subsequently identified during the course of investigation?</label>
                                            </div>
                                            <div class="col-sm-6 col-md-7">
                                                <c:if test="${medicalInvest.isIdentified eq 'Y'}"><label>Yes</label></c:if>
                                                <c:if test="${medicalInvest.isIdentified eq 'N'}"><label>No</label></c:if>
                                            </div>
                                        </div>

                                        <div class="form-group ">
                                            <div class="col-sm-5 control-label">
                                                <label>Name of Personnel</label>
                                            </div>
                                            <div class="col-sm-6 col-md-7">
                                                <label>${medicalInvest.addPersonnelName}</label>
                                            </div>
                                        </div>

                                        <div class="form-group ">
                                            <div class="col-sm-5 control-label">
                                                <label>Description of involvement</label>
                                            </div>
                                            <div class="col-sm-6 col-md-7">
                                                <label>${medicalInvest.involvementDesc}</label>
                                            </div>
                                        </div>

                                        <div class="form-group ">
                                            <div class="col-sm-5 control-label">
                                                <label>Description</label>
                                            </div>
                                            <div class="col-sm-6 col-md-7">
                                                <label>${medicalInvest.description}</label>
                                            </div>
                                        </div>

                                        <div class="form-group ">
                                            <div class="col-sm-5 control-label">
                                                <label>Interpretation of test results</label>
                                            </div>
                                            <div class="col-sm-6 col-md-7">
                                                <label>${medicalInvest.addTestResult}</label>
                                            </div>
                                        </div>

                                        <div class="form-group ">
                                            <div class="col-sm-5 control-label">
                                                <label>Is further medical follow-up advised/expected</label>
                                            </div>
                                            <div class="col-sm-6 col-md-7">
                                                <c:if test="${medicalInvest.addMedicalFollowup eq 'Y'}"><label>Yes</label></c:if>
                                                <c:if test="${medicalInvest.addMedicalFollowup eq 'N'}"><label>No</label></c:if>
                                            </div>
                                        </div>

                                        <div class="form-group ">
                                            <div class="col-sm-5 control-label">
                                                <label>Estimated duration and frequency of follow-up</label>
                                            </div>
                                            <div class="col-sm-6 col-md-7">
                                                <label>${medicalInvest.addFpDuration}</label>
                                            </div>
                                        </div>

                                        <h3 style="margin: 10px 0;border-bottom: 0px">Attachments</h3>
                                        <c:forEach var="doc" items="${docSettings}">
                                            <c:set var="maskDocType" value="${MaskUtil.maskValue('file', doc.type)}"/>
                                            <c:set var="savedFileList" value="${savedFiles.get(doc.type)}" />
                                            <c:set var="newFileList" value="${newFiles.get(doc.type)}" />
                                            <c:if test="${not empty savedFileList or not empty newFileList}">
                                                <div class="form-group">
                                                    <div class="col-10"><strong>${doc.typeDisplay}</strong></div>
                                                    <div class="clear"></div>
                                                </div>
                                                <div>
                                                    <c:forEach var="file" items="${savedFileList}">
                                                        <c:set var="tmpId" value="${MaskUtil.maskValue('file', file.repoId)}"/>
                                                        <div class="form-group">
                                                            <div class="col-10"><p><a href="javascript:void(0)" onclick="downloadFile('saved', '${tmpId}')">${file.filename}</a>(${String.format("%.1f", file.size/1024.0)}KB)</p></div>
                                                            <div class="clear"></div>
                                                        </div>
                                                    </c:forEach>
                                                    <c:forEach var="file" items="${newFileList}">
                                                        <c:set var="tmpId" value="${MaskUtil.maskValue('file', file.tmpId)}"/>
                                                        <div class="form-group">
                                                            <div class="col-10"><p><a href="javascript:void(0)" onclick="downloadFile('new', '${tmpId}')">${file.filename}</a>(${String.format("%.1f", file.size/1024.0)}KB)</p></div>
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
                        <div class="application-tab-footer">
                            <div class="row">
                                <div class="col-xs-12 col-sm-6 ">
                                    <a class="back" id="back" href="#"><em class="fa fa-angle-left"></em> Back</a>
                                </div>
                                <div class="col-xs-12 col-sm-6">
                                    <div class="button-group">
                                        <a class="btn btn-secondary" id="saveDraft" >Save as Draft</a>
                                        <a class="btn btn-primary next" id="next" >Save</a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>