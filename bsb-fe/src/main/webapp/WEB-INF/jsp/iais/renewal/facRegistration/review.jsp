<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT"%>
<%@ taglib prefix="webui" uri="http://www.ecquaria.com/webui" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="iais-bsb" uri="http://www.ecq.com/iais-bsb" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fac" tagdir="/WEB-INF/tags/facility" %>

<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<link href="<%=WEB_ROOT%>/css/bsb/bsb-common.css" rel="stylesheet"/>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-display-or-not.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common-facility-register.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-renewal-facility-register.js"></script>

<%@include file="/WEB-INF/jsp/iais/include/showErrorMsg.jsp"%>

<%@include file="dashboard.jsp"%>

<form method="post" id="mainForm" action="<%=process.runtime.continueURL()%>">
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="action_type" value="">
    <input type="hidden" name="action_value" value="">
    <input type="hidden" name="action_additional" value="">
    <div class="main-content">
        <div class="container">
            <div class="row">
                <div class="col-xs-12">
                    <div class="tab-gp steps-tab">
                        <%@ include file="renewalInnerNavTab.jsp" %>
                        <div class="tab-content">
                            <div class="tab-pane fade in active">
                                <div id="reviewPanel" role="tabpanel">
                                    <p>Please review your Facility details.</p>
                                    <p>Click Edit to make necessary changes before renewal where applicable.</p>
                                    <fac:preview facProfile="${facProfile}" facOperator="${facOperator}" facAuth="${facAuth}" facAdmin="${facAdmin}" facOfficer="${facOfficer}" facCommittee="${facCommittee}" batList="${batList}"
                                                 authorisedEditJudge="true" adminEditJudge="true" officerEditJudge="true" committeeEditJudge="true" batListEditJudge="true" docEditJudge="true">
                                        <jsp:attribute name="editFrag"><a href="#" review-edit-data-step-key="REPLACE-STEP-KEY"><em class="fa fa-pencil-square-o"></em>Edit</a></jsp:attribute>
                                        <jsp:attribute name="docFrag">
                                            <fac:doc-preview docSettings="${docSettings}" savedFiles="${savedFiles}" newFiles="${newFiles}"/>
                                        </jsp:attribute>
                                    </fac:preview>
                                    <div class="text-right"><a id="edit" href="javascript:void(0)"><em class="fa fa-pencil-square-o"></em>Edit</a></div>
                                    <div class="form-horizontal" style="padding: 30px 20px 10px;">
                                        <div class="form-group ">
                                            <div class="col-sm-5 control-label">
                                                <label for="remarks">Remarks</label>
                                            </div>
                                            <div class="col-sm-6 col-md-7">
                                                <textarea maxLength="1000" class="col-xs-12" name="remarks" id="remarks" rows="5"><c:out value="${review.remarks}"/></textarea>
                                                <span data-err-ind="remarks" class="error-msg"></span>
                                            </div>
                                        </div>
                                        <div class="form-group ">
                                            <div class="col-sm-5 control-label">
                                                <label for="approvedFacCertifier">Select Approved Facility Certifier</label>
                                                <span class="mandatory otherQualificationSpan">*</span>
                                            </div>
                                            <div class="col-sm-6 col-md-7" style="z-index: 20">
                                                <select name="approvedFacCertifier" id="approvedFacCertifier">
                                                    <c:forEach items="${approvedFacCertifierOps}" var="certifier">
                                                        <option value="${certifier.value}" <c:if test="${review.approvedFacCertifier eq certifier.value}">selected="selected"</c:if>>${certifier.text}</option>
                                                    </c:forEach>
                                                </select>
                                                <span data-err-ind="approvedFacCertifier" class="error-msg"></span>
                                            </div>
                                        </div>
                                        <div class="form-group ">
                                            <div class="col-sm-5 control-label">
                                                <label for="reason">Reasons to Choose This AFC</label>
                                            </div>
                                            <div class="col-sm-6 col-md-7">
                                                <textarea maxLength="500" class="col-xs-12" name="reason" id="reason" rows="2"><c:out value="${review.reason}"/></textarea>
                                                <span data-err-ind="reason" class="error-msg"></span>
                                            </div>
                                        </div>

                                        <div class="form-group ">
                                            <div class="col-sm-5 control-label">
                                                <label for="effectiveDateOfChange">Effective Date of Change</label>
                                            </div>
                                            <div class="col-sm-6 col-md-7">
                                                <input type="text" autocomplete="off" name="effectiveDateOfChange" id="effectiveDateOfChange" data-date-start-date="01/01/1900" value="<c:out value="${review.effectiveDateOfChange}"/>" placeholder="dd/mm/yyyy" maxlength="10" class="date_picker form-control"/>
                                                <span data-err-ind="effectiveDateOfChange" class="error-msg"></span>
                                            </div>
                                        </div>
                                        <div class="form-group " style="z-index: 10">
                                            <div class="col-xs-1" style="padding: 30px 0 20px 30px;">
                                                <input type="checkbox" name="declare" id="declare" value="Y" <c:if test="${review.declare eq 'Y'}">checked="checked"</c:if> />
                                            </div>
                                            <div class="col-xs-10 control-label">
                                                <label for="declare">I, hereby declare that all the information I have provided here is true and accurate. If any of the information given herein changes or becomes inaccurate in any way, I shall immediately notify MOH Biosafety Branch of such change or inaccuracy.</label>
                                                <span data-err-ind="declare" class="error-msg"></span>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="application-tab-footer">
                                    <div class="row">
                                        <div class="col-xs-12 col-sm-6 ">
                                            <a class="back" id="back" href="#"><em class="fa fa-angle-left"></em> Previous</a>
                                        </div>
                                        <div class="col-xs-12 col-sm-6">
                                            <div class="button-group">
                                                <a class="btn btn-secondary" href="javascript:void(0);">PRINT</a>
                                                <a class="btn btn-secondary" id="saveDraft" >Save as Draft</a>
                                                <a class="btn btn-primary next" id="submit" >SUBMIT</a>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <%@include file="/WEB-INF/jsp/iais/include/jumpAfterDraft.jsp"%>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>