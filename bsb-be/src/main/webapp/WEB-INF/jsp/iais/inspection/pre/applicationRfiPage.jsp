<%@ taglib prefix="webui" uri="http://www.ecquaria.com/webui" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fac" tagdir="/WEB-INF/tags/facility" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-file.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common-data-file.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-pre-inspection.js"></script>
<form method="post" id="mainForm" action="<%=process.runtime.continueURL()%>">
    <input type="hidden" name="closePage" value="${closePage}">
    <div class="main-content">
        <div class="container">
            <div class="row">
                <div class="col-xs-12">
                    <div class="tab-gp steps-tab">
                        <div class="tab-content">
                            <div class="tab-pane fade in active">
                                <div id="previewSubmitPanel" role="tabpanel">
                                    <div class="preview-gp">
                                        <div class="row">
                                            <div class="col-xs-12">
                                                <div class="panel-group" role="tablist" aria-multiselectable="true">
                                                    <%--@elvariable id="organizationAddress" type="sg.gov.moh.iais.egp.bsb.dto.info.common.OrgAddressInfo"--%>
                                                    <%--@elvariable id="facProfile" type="sg.gov.moh.iais.egp.bsb.dto.appview.facility.FacilityProfileDto"--%>
                                                    <%--@elvariable id="facOperator" type="sg.gov.moh.iais.egp.bsb.dto.appview.facility.FacilityOperatorDto"--%>
                                                    <%--@elvariable id="facAuth" type="sg.gov.moh.iais.egp.bsb.dto.appview.facility.FacilityAuthoriserDto"--%>
                                                    <%--@elvariable id="facAdminOfficer" type="sg.gov.moh.iais.egp.bsb.dto.appview.facility.FacilityAdminAndOfficerDto"--%>
                                                    <%--@elvariable id="facCommittee" type="sg.gov.moh.iais.egp.bsb.dto.appview.facility.FacilityCommitteeDto"--%>
                                                    <%--@elvariable id="batList" type="java.util.List<sg.gov.moh.iais.egp.bsb.dto.appview.facility.BiologicalAgentToxinDto>"--%>
                                                    <%--@elvariable id="configList" type="java.util.List<sg.gov.moh.iais.egp.bsb.dto.declaration.DeclarationItemMainInfo>"--%>
                                                    <%--@elvariable id="answerMap" type="java.util.Map<java.lang.String, java.lang.String>"--%>
                                                    <%--@elvariable id="afc" type="sg.gov.moh.iais.egp.bsb.dto.appview.facility.FacilityAfcDto"--%>
                                                    <%--@elvariable id="previewSubmit" type="sg.gov.moh.iais.egp.bsb.dto.appview.facility.PreviewSubmitDto"--%>
                                                    <%--@elvariable id="docSettings" type="java.util.List<sg.gov.moh.iais.egp.bsb.entity.DocSetting>"--%>
                                                    <%--@elvariable id="savedFiles" type="java.util.Map<java.lang.String, java.util.List<sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo>>"--%>
                                                    <fac:preview compProfile="${organizationAddress}" facProfile="${facProfile}" facOperator="${facOperator}" facAuth="${facAuth}" facAdminOfficer="${facAdminOfficer}" facCommittee="${facCommittee}" batList="${batList}"
                                                                 afc="${afc}" declarationConfigList="${configList}" declarationAnswerMap="${answerMap}"
                                                                 profileEditJudge="false" batListEditJudge="false" docEditJudge="false" otherAppInfoEditJudge="false" afcEditJudge="false" containsAfcJudge="${isCertifiedFacility}" containsBatListJudge="${!isCertifiedFacility}"
                                                                 facilityInfoSelectJudge="true" batSelectJudge="true" docSelectJudge="true">
                                                        <jsp:attribute name="docFrag">
                                                            <c:forEach var="doc" items="${docSettings}">
                                                                <c:set var="savedFileList" value="${savedFiles.get(doc.type)}" />
                                                                <c:if test="${not empty savedFileList}">
                                                                    <div class="form-group">
                                                                        <div class="col-10"><strong>${doc.typeDisplay}</strong></div>
                                                                        <div class="clear"></div>
                                                                    </div>
                                                                    <div>
                                                                        <c:forEach var="file" items="${savedFileList}">
                                                                            <c:set var="repoId"><iais:mask name="file" value="${file.repoId}"/></c:set>
                                                                            <div class="form-group">
                                                                                <div class="col-10"><p><a href="/bsb-be/ajax/doc/download/repo/${repoId}?filename=${file.filename}">${file.filename}</a>(<fmt:formatNumber value="${file.size/1024.0}" type="number" pattern="0.0"/>KB)</p></div>
                                                                                <div class="clear"></div>
                                                                            </div>
                                                                        </c:forEach>
                                                                    </div>
                                                                </c:if>
                                                            </c:forEach>
                                                        </jsp:attribute>
                                                    </fac:preview>
                                                        <div style="text-align: right"><button name="submitAppRfiBtn" id="submitAppRfiBtn" type="button" class="btn btn-primary">Submit</button></div>
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
</form>