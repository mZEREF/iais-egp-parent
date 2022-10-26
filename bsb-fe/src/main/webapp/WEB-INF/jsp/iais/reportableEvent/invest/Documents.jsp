<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT"%>
<%@ taglib prefix="webui" uri="http://www.ecquaria.com/webui" %>
<%@ taglib prefix="ias" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="iais-bsb" uri="http://www.ecq.com/iais-bsb" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<link href="<%=WEB_ROOT%>/css/bsb/bsb-common.css" rel="stylesheet"/>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-incident.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common-file.js"></script>


<%@include file="/WEB-INF/jsp/iais/include/showErrorMsg.jsp"%>

<%@include file="common/dashboard.jsp"%>

<form method="post" id="mainForm" enctype="multipart/form-data" action="<%=process.runtime.continueURL()%>">
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="action_type" value="">
    <input type="hidden" name="action_value" value="">
    <input type="hidden" name="action_additional" value="">
    <input id="multiUploadTrigger" type="file" multiple="multiple" style="display: none"/>
    <input id="echoReloadTrigger" type="file" style="display: none"/>
    <input type="hidden" id="deleteExistFiles" name="deleteExistFiles" value="">
    <input type="hidden" id="deleteNewFiles" name="deleteNewFiles" value="">
    <div id="fileUploadInputDiv" style="display: none"></div>
    <div class="main-content">
        <div class="container">
            <div class="row">
                <div class="col-xs-12">
                    <div class="tab-gp dashboard-tab">
                        <%@ include file="common/InnerNavTab.jsp" %>
                        <div class="tab-content">
                            <div class="tab-pane fade in active" id="tabDocuments" role="tabpanel" style="background-color: rgba(255, 255, 255, 1);border-radius: 15px;box-shadow: 0 0 15px #00000059;">
                                <div class="panel panel-default">
                                    <div class="form-horizontal">
                                        <div class="container">
                                            <div class="component-gp col-xs-12 col-sm-11 col-md-12 col-lg-10">
                                                <div class="row">
                                                    <div class="document-content" style="margin-top: 20px">
                                                        <div class="document-upload-gp">
                                                            <c:forEach var="doc" items="${docSettings}">
                                                                <c:set var="maskDocType"><iais:mask name="file" value="${doc.type}"/></c:set>                                                                <div class="document-upload-list">
                                                                    <h3>${doc.typeDisplay}<c:if test="${doc.mandatory}"> <span class="mandatory otherQualificationSpan">*</span></c:if></h3>
                                                                    <div class="file-upload-gp">
                                                                        <c:if test="${savedFiles.get(doc.type) ne null}">
                                                                            <c:forEach var="info" items="${savedFiles.get(doc.type)}">
                                                                                <c:set var="tmpId"><iais:mask name="file" value="${info.repoId}"/></c:set>
                                                                                <div id="${tmpId}FileDiv">
                                                                                    <a href="/bsb-web/ajax/doc/download/investigationReport/repo/${tmpId}" style="text-decoration: underline"><span id="${tmpId}Span">${info.filename}</span></a>(<fmt:formatNumber value="${info.size/1024.0}" type="number" pattern="0.0"/>KB)<button
                                                                                        type="button" class="btn btn-secondary btn-sm" onclick="deleteSavedFile('${tmpId}')">Delete</button><button
                                                                                        type="button" class="btn btn-secondary btn-sm" onclick="reloadSavedFile('${tmpId}', '${maskDocType}')">Reload</button>
                                                                                    <span data-err-ind="${info.repoId}" class="error-msg"></span>
                                                                                </div>
                                                                            </c:forEach>
                                                                        </c:if>
                                                                        <c:if test="${newFiles.get(doc.type) ne null}">
                                                                            <c:forEach var="info" items="${newFiles.get(doc.type)}">
                                                                                <c:set var="tmpId"><iais:mask name="file" value="${info.tmpId}"/></c:set>
                                                                                <div id="${tmpId}FileDiv">
                                                                                    <a href="/bsb-web/ajax/doc/download/investigationReport/new/${tmpId}" style="text-decoration: underline"><span id="${tmpId}Span">${info.filename}</span></a>(<fmt:formatNumber value="${info.size/1024.0}" type="number" pattern="0.0"/>KB)<button
                                                                                        type="button" class="btn btn-secondary btn-sm" onclick="deleteNewFile('${tmpId}')">Delete</button><button
                                                                                        type="button" class="btn btn-secondary btn-sm" onclick="reloadNewFile('${tmpId}', '${maskDocType}')">Reload</button>
                                                                                    <span data-err-ind="${info.tmpId}" class="error-msg"></span>
                                                                                </div>
                                                                            </c:forEach>
                                                                        </c:if>
                                                                        <a class="btn file-upload btn-secondary" data-upload-file="${maskDocType}" href="javascript:void(0);">Upload</a><span data-err-ind="${doc.type}" class="error-msg"></span>
                                                                    </div>
                                                                </div>
                                                            </c:forEach>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <%@ include file="common/InnerFooter.jsp" %>

                            <%@include file="/WEB-INF/jsp/iais/include/jumpAfterDraft.jsp"%>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>