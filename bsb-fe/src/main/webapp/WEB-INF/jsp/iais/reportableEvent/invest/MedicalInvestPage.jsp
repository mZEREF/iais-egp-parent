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
                                                                <h3 style="margin: 20px 0">Details of Medical Investigation</h3>

                                                                <div class="form-group">
                                                                    <div class="col-sm-5 control-label">
                                                                        <label for="personnelName">Name of Personnel</label>
                                                                    </div>
                                                                    <div class="col-sm-6 col-md-7">
                                                                        <input type="text" autocomplete="off" name="personnelName" id="personnelName" value='<c:out value="${medicalInvest.personnelName}"/>'/>
                                                                        <span data-err-ind="personnelName" class="error-msg"></span>
                                                                    </div>
                                                                </div>
                                                                <div class="form-group">
                                                                    <div class="col-sm-5 control-label">
                                                                        <label for="medicalUpdate">Updates on medical follow-up</label>
                                                                    </div>
                                                                    <div class="col-sm-6 col-md-7">
                                                                        <input type="text" autocomplete="off" name="medicalUpdate" id="medicalUpdate" value='<c:out value="${medicalInvest.medicalUpdate}"/>'/>
                                                                        <span data-err-ind="medicalUpdate" class="error-msg"></span>
                                                                    </div>
                                                                </div>
                                                                <div class="form-group">
                                                                    <div class="col-sm-5 control-label">
                                                                        <label for="testResult">Interpretation of test results</label>
                                                                    </div>
                                                                    <div class="col-sm-6 col-md-7">
                                                                        <input type="text" autocomplete="off" name="testResult" id="testResult" value='<c:out value="${medicalInvest.testResult}"/>'/>
                                                                        <span data-err-ind="testResult" class="error-msg"></span>
                                                                    </div>
                                                                </div>
                                                                <div class="form-group">
                                                                    <div class="col-sm-5 control-label">
                                                                        <label>Is further medical follow-up advised/expected</label>
                                                                    </div>
                                                                    <div class="col-sm-6 col-md-7">
                                                                        <div class="col-sm-5" style="margin-top: 8px">
                                                                            <input type="radio" name="medicalFollowup" id="medicalFollowupY" value="Y" <c:if test="${medicalInvest.medicalFollowup eq 'Y'}">checked="checked"</c:if> />
                                                                            <label for="medicalFollowupY">Yes</label>
                                                                        </div>
                                                                        <div class="col-sm-5" style="margin-top: 8px">
                                                                            <input type="radio" name="medicalFollowup" id="medicalFollowupN" value="N" <c:if test="${medicalInvest.medicalFollowup eq 'N'}">checked="checked"</c:if> />
                                                                            <label for="medicalFollowupN">No</label>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                                <div class="form-group">
                                                                    <div class="col-sm-5 control-label">
                                                                        <label for="fpDuration">Estimated duration and frequency of follow-up</label>
                                                                    </div>
                                                                    <div class="col-sm-6 col-md-7">
                                                                        <input type="text" autocomplete="off" name="fpDuration" id="fpDuration" value='<c:out value="${medicalInvest.fpDuration}"/>'/>
                                                                        <span data-err-ind="fpDuration" class="error-msg"></span>
                                                                    </div>
                                                                </div>

                                                                <h3 style="margin: 20px 0">Additional Personnel Identified for Medical Investigation/Follow-up</h3>
                                                                <div class="form-group ">
                                                                    <div class="col-sm-5 control-label">
                                                                        <label>Were there persons who were not identified during Notification of Incident but were subsequently identified during the course of investigation?</label>
                                                                        <span class="mandatory otherQualificationSpan">*</span>
                                                                    </div>
                                                                    <div class="col-sm-6 col-md-7">
                                                                        <div class="col-sm-5" style="margin-top: 8px">
                                                                            <input type="radio" name="isIdentified" id="isIdentifiedY" value="Y" <c:if test="${medicalInvest.isIdentified eq 'Y'}">checked="checked"</c:if> />
                                                                            <label for="isIdentifiedY">Yes</label>
                                                                        </div>
                                                                        <div class="col-sm-5" style="margin-top: 8px">
                                                                            <input type="radio" name="isIdentified" id="isIdentifiedN" value="N" <c:if test="${medicalInvest.isIdentified eq 'N'}">checked="checked"</c:if> />
                                                                            <label for="isIdentifiedN">No</label>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                                <div class="form-group">
                                                                    <div class="col-sm-5 control-label">
                                                                        <label for="addPersonnelName">Name of Personnel</label>
                                                                        <span class="mandatory otherQualificationSpan">*</span>
                                                                    </div>
                                                                    <div class="col-sm-6 col-md-7">
                                                                        <input type="text" autocomplete="off" name="addPersonnelName" id="addPersonnelName" value='<c:out value="${medicalInvest.addPersonnelName}"/>'/>
                                                                        <span data-err-ind="addPersonnelName" class="error-msg"></span>
                                                                    </div>
                                                                </div>
                                                                <div class="form-group">
                                                                    <div class="col-sm-5 control-label">
                                                                        <label for="involvementDesc">Description of involvement</label>
                                                                    </div>
                                                                    <div class="col-sm-6 col-md-7">
                                                                        <input type="text" autocomplete="off" name="involvementDesc" id="involvementDesc" value='<c:out value="${medicalInvest.involvementDesc}"/>'/>
                                                                        <span data-err-ind="involvementDesc" class="error-msg"></span>
                                                                    </div>
                                                                </div>
                                                                <div class="form-group">
                                                                    <div class="col-sm-5 control-label">
                                                                        <label for="description">Description</label>
                                                                    </div>
                                                                    <div class="col-sm-6 col-md-7">
                                                                        <input type="text" autocomplete="off" name="description" id="description" value='<c:out value="${medicalInvest.description}"/>'/>
                                                                        <span data-err-ind="description" class="error-msg"></span>
                                                                    </div>
                                                                </div>
                                                                <div class="form-group">
                                                                    <div class="col-sm-5 control-label">
                                                                        <label for="addTestResult">Interpretation of test results</label>
                                                                    </div>
                                                                    <div class="col-sm-6 col-md-7">
                                                                        <input type="text" autocomplete="off" name="addTestResult" id="addTestResult" value='<c:out value="${medicalInvest.addTestResult}"/>'/>
                                                                        <span data-err-ind="addTestResult" class="error-msg"></span>
                                                                    </div>
                                                                </div>
                                                                <div class="form-group ">
                                                                    <div class="col-sm-5 control-label">
                                                                        <label>Is further medical follow-up advised/expected</label>
                                                                    </div>
                                                                    <div class="col-sm-6 col-md-7">
                                                                        <div class="col-sm-5" style="margin-top: 8px">
                                                                            <input type="radio" name="addMedicalFollowup" id="addMedicalFollowupY" value="Y" <c:if test="${medicalInvest.addMedicalFollowup eq 'Y'}">checked="checked"</c:if> />
                                                                            <label for="addMedicalFollowupY">Yes</label>
                                                                        </div>
                                                                        <div class="col-sm-5" style="margin-top: 8px">
                                                                            <input type="radio" name="addMedicalFollowup" id="addMedicalFollowupN" value="N" <c:if test="${medicalInvest.addMedicalFollowup eq 'N'}">checked="checked"</c:if> />
                                                                            <label for="addMedicalFollowupN">No</label>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                                <div class="form-group">
                                                                    <div class="col-sm-5 control-label">
                                                                        <label for="addFpDuration">Estimated duration and frequency of follow-up</label>
                                                                    </div>
                                                                    <div class="col-sm-6 col-md-7">
                                                                        <input type="text" autocomplete="off" name="addFpDuration" id="addFpDuration" value='<c:out value="${medicalInvest.addFpDuration}"/>'/>
                                                                        <span data-err-ind="addFpDuration" class="error-msg"></span>
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

