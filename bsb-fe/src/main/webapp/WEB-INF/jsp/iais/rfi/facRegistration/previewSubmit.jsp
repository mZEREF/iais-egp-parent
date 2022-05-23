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
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common-node-group.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common-data-file.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-rfi-facility-register.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-display-or-not.js"></script>

<%@include file="/WEB-INF/jsp/iais/include/showErrorMsg.jsp"%>

<%@include file="dashboard.jsp"%>


<%--@elvariable id="organizationAddress" type="sg.gov.moh.iais.egp.bsb.dto.info.common.OrgAddressInfo"--%>
<%--@elvariable id="facProfile" type="sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityProfileDto"--%>
<%--@elvariable id="facOperator" type="sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityOperatorDto"--%>
<%--@elvariable id="facAuth" type="sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityAuthoriserDto"--%>
<%--@elvariable id="facAdminOfficer" type="sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityAdminAndOfficerDto"--%>
<%--@elvariable id="facCommittee" type="sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityCommitteeDto"--%>
<%--@elvariable id="batList" type="java.util.List<sg.gov.moh.iais.egp.bsb.dto.register.bat.BiologicalAgentToxinDto>"--%>
<%--@elvariable id="configList" type="java.util.List<sg.gov.moh.iais.egp.bsb.dto.declaration.DeclarationItemMainInfo>"--%>
<%--@elvariable id="answerMap" type="java.util.Map<java.lang.String, java.lang.String>"--%>
<%--@elvariable id="afc" type="sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityAfcDto"--%>
<%--@elvariable id="docSettings" type="java.util.List<sg.gov.moh.iais.egp.bsb.entity.DocSetting>"--%>
<%--@elvariable id="savedFiles" type="java.util.Map<java.lang.String, java.util.List<sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo>>"--%>
<%--@elvariable id="newFiles" type="java.util.Map<java.lang.String, java.util.List<sg.gov.moh.iais.egp.bsb.dto.file.NewDocInfo>>"--%>
<%--@elvariable id="previewSubmit" type="sg.gov.moh.iais.egp.bsb.dto.register.facility.PreviewSubmitDto"--%>
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
                        <%@ include file="../mainAppCommon/facRegistration/InnerNavTab.jsp" %>
                        <div class="tab-content">
                            <div class="tab-pane fade in active">
                                <div id="previewSubmitPanel" role="tabpanel">
                                    <fac:preview compProfile="${organizationAddress}" facProfile="${facProfile}" facOperator="${facOperator}" facAuth="${facAuth}" facAdminOfficer="${facAdminOfficer}" facCommittee="${facCommittee}" batList="${batList}"
                                                 afc="${afc}" declarationConfigList="${configList}" declarationAnswerMap="${answerMap}"
                                                 profileEditJudge="true" batListEditJudge="true" docEditJudge="true" otherAppInfoEditJudge="true" afcEditJudge="true" containsAfcJudge="${isCertifiedFacility}" containsBatListJudge="${isUncertifiedFacility}">
                                        <jsp:attribute name="editFrag"><a href="#" data-step-key="REPLACE-STEP-KEY"><em class="fa fa-pencil-square-o"></em>Edit</a></jsp:attribute>
                                        <jsp:attribute name="docFrag">
                                            <fac:doc-preview docSettings="${docSettings}" savedFiles="${savedFiles}" newFiles="${newFiles}"/>
                                        </jsp:attribute>
                                    </fac:preview>
                                    <div class="form-horizontal" style="padding: 30px 20px 10px;">
                                        <div class="form-group ">
                                            <div class="col-xs-4 control-label">
                                                <label for="remarks">Remarks</label>
                                            </div>
                                            <div class="col-xs-7 col-md-8">
                                                <textarea maxLength="1000" class="col-xs-12" name="remarks" id="remarks" rows="5"><c:out value="${previewSubmit.remarks}"/></textarea>
                                                <span data-err-ind="remarks" class="error-msg"></span>
                                            </div>
                                        </div>
                                        <div class="form-group " style="z-index: 10">
                                            <div class="col-xs-1" style="padding: 20px 0 20px 30px;">
                                                <input type="checkbox" name="regulationDeclare" id="regulationDeclare" value="Y" <c:if test="${previewSubmit.regulationDeclare eq 'Y'}">checked="checked"</c:if> />
                                            </div>
                                            <div class="col-xs-10 control-label">
                                                <label for="regulationDeclare">I will ensure that the packaging of the materials and the transfer are carried out in accordance with the requirements stipulated under the BATA Transportation Regulations, the BATA and any other related regulations.</label>
                                                <span data-err-ind="regulationDeclare" class="error-msg"></span>
                                            </div>
                                        </div>
                                        <div class="form-group " style="z-index: 10">
                                            <div class="col-xs-1" style="padding: 30px 0 20px 30px;">
                                                <input type="checkbox" name="accuracyDeclare" id="accuracyDeclare" value="Y" <c:if test="${previewSubmit.accuracyDeclare eq 'Y'}">checked="checked"</c:if> />
                                            </div>
                                            <div class="col-xs-10 control-label">
                                                <label for="accuracyDeclare">I, hereby declare that all the information I have provided here is true and accurate. If any of the information given herein changes or becomes inaccurate in any way, I shall immediately notify MOH Biosafety Branch of such change or inaccuracy.</label>
                                                <span data-err-ind="accuracyDeclare" class="error-msg"></span>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <div class="application-tab-footer">
                                    <div class="row">
                                        <div class="col-xs-12 col-sm-6 ">
                                            <a class="back" id="previous" href="#"><em class="fa fa-angle-left"></em> Previous</a>
                                        </div>
                                        <div class="col-xs-12 col-sm-6">
                                            <div class="button-group">
                                                <a class="btn btn-secondary" id="saveDraft" >Save as Draft</a>
                                                <a class="btn btn-primary next" id="submit" >Next</a>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="modal fade" id="submitDeclareModal" role="dialog" aria-labelledby="myModalLabel">
                            <div class="modal-dialog modal-dialog-centered" role="document">
                                <div class="modal-content">
                                    <div class="modal-body">
                                        <div class="row">
                                            <div class="col-md-12"><span style="font-size: 2rem">Please check the declaration box</span></div>
                                        </div>
                                    </div>
                                    <div class="modal-footer">
                                        <button type="button" class="btn btn-secondary btn-md" data-dismiss="modal">Close</button>
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