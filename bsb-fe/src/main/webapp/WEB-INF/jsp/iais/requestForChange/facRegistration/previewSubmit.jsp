<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT"%>
<%@ taglib prefix="webui" uri="http://www.ecquaria.com/webui" %>
<%@ taglib prefix="ias" uri="http://www.ecq.com/iais" %>
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
    <div class="main-content">
        <div class="container">
            <div class="row">
                <div class="col-xs-12">
                    <div class="tab-gp steps-tab">
                        <%@ include file="../../mainAppCommon/facRegistration/InnerNavTab.jsp" %>
                        <div class="tab-content">
                            <div class="tab-pane fade in active">
                                <div id="previewSubmitPanel" role="tabpanel">
                                    <fac:preview facProfile="${facProfile}" facOperator="${facOperator}" facAuth="${facAuth}" facAdmin="${facAdmin}" facOfficer="${facOfficer}" facCommittee="${facCommittee}" batList="${batList}"
                                                 profileEditJudge="true" batListEditJudge="true" docEditJudge="true">
                                        <jsp:attribute name="editFrag"><a href="#" data-step-key="REPLACE-STEP-KEY"><em class="fa fa-pencil-square-o"></em>Edit</a></jsp:attribute>
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
                                                <textarea maxLength="1000" class="col-xs-12" name="remarks" id="remarks" rows="5"><c:out value="${previewSubmit.remarks}"/></textarea>
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
                                                        <option value="${certifier.value}" <c:if test="${previewSubmit.approvedFacCertifier eq certifier.value}">selected="selected"</c:if>>${certifier.text}</option>
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
                                                <textarea maxLength="500" class="col-xs-12" name="reason" id="reason" rows="2"><c:out value="${previewSubmit.reason}"/></textarea>
                                                <span data-err-ind="reason" class="error-msg"></span>
                                            </div>
                                        </div>
                                        <div class="form-group " style="z-index: 10">
                                            <div class="col-xs-1" style="padding: 30px 0 20px 30px;">
                                                <input type="checkbox" name="declare" id="declare" value="Y" <c:if test="${previewSubmit.declare eq 'Y'}">checked="checked"</c:if> />
                                            </div>
                                            <div class="col-xs-10 control-label">
                                                <label for="declare">I, hereby declare that all the information I have provided here is true and accurate. If any of the information given herein changes or becomes inaccurate in any way, I shall immediately notify MOH Biosafety Branch of such change or inaccuracy.</label>
                                                <span data-err-ind="declare" class="error-msg"></span>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <%@ include file="../common/InnerFooter.jsp" %>
                            </div>
                        </div>
                        <%@include file="/WEB-INF/jsp/iais/include/jumpAfterDraft.jsp"%>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>