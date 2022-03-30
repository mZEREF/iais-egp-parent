<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT"%>
<%@ taglib prefix="webui" uri="http://www.ecquaria.com/webui" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="iais-bsb" uri="http://www.ecq.com/iais-bsb" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fac" tagdir="/WEB-INF/tags/facilityCertifier" %>

<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common.js"></script>

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
                        <div class="tab-content">
                            <div class="tab-pane fade in active">
                                <div id="previewSubmitPanel" role="tabpanel">
                                    <fac:preview companyProfile="${orgProfile}" companyCerTeam="${orgCerTeam}" companyAdmin="${orgAdmin}"
                                                 profileEditJudge="true" docEditJudge="true">
                                        <jsp:attribute name="editFrag"><c:if test="${not empty maskedEditId}"><a href="/bsb-fe/eservice/INTERNET/MohFacilityCertifierRegistration?editId=${maskedEditId}&step=REPLACE-STEP-KEY"><em class="fa fa-pencil-square-o"></em>Edit</a></c:if></jsp:attribute>
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
                                                                <div class="col-10"><p><a href="/bsb-fe/ajax/doc/download/repo/${repoId}?filename=${file.filename}">${file.filename}</a>(<fmt:formatNumber value="${file.size/1024.0}" type="number" pattern="0.0"/>KB)</p></div>
                                                                <div class="clear"></div>
                                                            </div>
                                                        </c:forEach>
                                                    </div>
                                                </c:if>
                                            </c:forEach>
                                        </jsp:attribute>
                                    </fac:preview>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-xs-12">
                    <a class="back" href="/bsb-fe/eservice/INTERNET/MohBSBInboxApp"><em class="fa fa-angle-left"></em> Previous</a>
                </div>
            </div>
        </div>
    </div>
</form>