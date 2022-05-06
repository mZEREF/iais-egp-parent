<%@tag description="Facility supporting documents tag of facility registration" %>
<%@taglib prefix="webui" uri="http://www.ecquaria.com/webui" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="iais-bsb" uri="http://www.ecq.com/iais-bsb" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@attribute name="savedFiles" required="true" type="java.util.Map<java.lang.String, java.util.List<sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo>>" %>
<%@attribute name="newFiles" required="true" type="java.util.Map<java.lang.String, java.util.List<sg.gov.moh.iais.egp.bsb.dto.file.NewDocInfo>>" %>
<%@attribute name="docSettings" required="true" type="java.util.List<sg.gov.moh.iais.egp.bsb.entity.DocSetting>" %>
<%@attribute name="otherDocTypes" required="true" type="java.util.Collection<java.lang.String>" %>
<%@attribute name="docTypeOps" required="true" type="java.util.List<com.ecquaria.cloud.moh.iais.common.dto.SelectOption>" %>
<%@attribute name="specialJsFrag" fragment="true" %>
<%@attribute name="dashboardFrag" fragment="true" %>
<%@attribute name="innerFooterFrag" fragment="true" %>
<%@attribute name="editJudge" type="java.lang.Boolean" %>

<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<link href="<%=sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT%>/css/bsb/bsb-common.css" rel="stylesheet"/>
<script type="text/javascript" src="<%=sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<script type="text/javascript" src="<%=sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT%>/js/bsb/bsb-common-node-group.js"></script>
<script type="text/javascript" src="<%=sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT%>/js/bsb/bsb-common-file.js"></script>
<jsp:invoke fragment="specialJsFrag"/>

<script>
    <% String jsonStr = (String) request.getAttribute("docTypeOpsJson");
    if (jsonStr == null || "".equals(jsonStr)) {
       jsonStr = "undefined";
    }
    %>
    var docTypeOps = <%=jsonStr%>;
</script>

<%@include file="/WEB-INF/jsp/iais/include/showErrorMsg.jsp" %>
<jsp:invoke fragment="dashboardFrag"/>
<form method="post" id="mainForm" enctype="multipart/form-data" action="<%=process.runtime.continueURL()%>" onsubmit="return validateOtherDocType();">
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="action_type" value="">
    <input type="hidden" name="action_value" value="">
    <input type="hidden" name="action_additional" value="">
    <input type="hidden" id="deleteExistFiles" name="deleteExistFiles" value="">
    <input type="hidden" id="deleteNewFiles" name="deleteNewFiles" value="">
    <input id="multiUploadTrigger" type="file" multiple="multiple" style="display: none"/>
    <input id="echoReloadTrigger" type="file" style="display: none"/>
    <div id="fileUploadInputDiv" style="display: none"></div>
    <div class="main-content">
        <div class="container">
            <div class="row">
                <div class="col-xs-12">
                    <div class="tab-gp steps-tab">
                        <%@include file="/WEB-INF/jsp/iais/mainAppCommon/facRegistration/InnerNavTab.jsp" %>
                        <div class="tab-content">
                            <div class="tab-pane fade in active">
                                <div id="PrimaryDocsPanel" role="tabpanel">
                                    <div class="document-content">
                                        <div class="document-info-list">
                                            <ul>
                                                <li><p>The maximum file size per document is 10 MB.</p></li>
                                                <li><p>Acceptable file formats: JPG, PNG, PDF, CSV, DOCX, JPEG, XLS, DOC and XLSX.</p></li>
                                                <li><p>Please ensure that the corresponding Document Type is selected for each document uploaded under Others.</p></li>
                                            </ul>
                                        </div>
                                        <div class="document-upload-gp">
                                            <c:if test="${editJudge}"><div class="text-right app-font-size-16"><a id="edit" href="javascript:void(0)"><em class="fa fa-pencil-square-o"></em>Edit</a></div></c:if>
                                            <h2>Supporting Documents</h2>
                                            <c:forEach var="doc" items="${docSettings}">
                                                <c:set var="maskDocType"><iais:mask name="file" value="${doc.type}"/></c:set>
                                                <div class="document-upload-list">
                                                    <h3>${doc.typeDisplay}<c:if test="${doc.mandatory}"> <span class="mandatory otherQualificationSpan">*</span></c:if></h3>
                                                    <div class="file-upload-gp">
                                                        <c:if test="${savedFiles.get(doc.type) ne null}">
                                                            <c:forEach var="info" items="${savedFiles.get(doc.type)}">
                                                                <c:set var="tmpId"><iais:mask name="file" value="${info.repoId}"/></c:set>
                                                                <div id="${tmpId}FileDiv">
                                                                    <a href="/bsb-fe/ajax/doc/download/facReg/repo/${tmpId}" style="text-decoration: underline"><span id="${tmpId}Span">${info.filename}</span></a>(<fmt:formatNumber value="${info.size/1024.0}" type="number" pattern="0.0"/>KB)<button
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
                                                                    <a href="/bsb-fe/ajax/doc/download/facReg/new/${tmpId}" style="text-decoration: underline"><span id="${tmpId}Span">${info.filename}</span></a>(<fmt:formatNumber value="${info.size/1024.0}" type="number" pattern="0.0"/>KB)<button
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
                                            <div class="document-upload-list">
                                                <h3>Others</h3>
                                                <div id="upload-other-doc-gp" class="file-upload-gp">
                                                    <c:forEach var="type" items="${otherDocTypes}">
                                                        <c:set var="maskDocType"><iais:mask name="file" value="${type}"/></c:set>
                                                        <c:if test="${savedFiles.get(type) ne null}">
                                                            <c:forEach var="info" items="${savedFiles.get(type)}">
                                                                <c:set var="tmpId"><iais:mask name="file" value="${info.repoId}"/></c:set>
                                                                <div id="${tmpId}FileDiv">
                                                                    <a href="/bsb-fe/ajax/doc/download/facReg/repo/${tmpId}" style="text-decoration: underline"><span id="${tmpId}Span">${info.filename}</span></a>(<fmt:formatNumber value="${info.size/1024.0}" type="number" pattern="0.0"/>KB)<label class="other-doc-type-label">
                                                                    (<iais:code code="${info.docType}"/>)</label><button
                                                                        type="button" class="btn btn-secondary btn-sm" onclick="deleteSavedFile('${tmpId}')">Delete</button>
                                                                    <span data-err-ind="${info.repoId}" class="error-msg"></span>
                                                                </div>
                                                            </c:forEach>
                                                        </c:if>
                                                        <c:if test="${newFiles.get(type) ne null}">
                                                            <c:forEach var="info" items="${newFiles.get(type)}">
                                                                <c:set var="tmpId"><iais:mask name="file" value="${info.tmpId}"/></c:set>
                                                                <div id="${tmpId}FileDiv">
                                                                    <a href="/bsb-fe/ajax/doc/download/facReg/new/${tmpId}" style="text-decoration: underline"><span id="${tmpId}Span">${info.filename}</span></a>(<fmt:formatNumber value="${info.size/1024.0}" type="number" pattern="0.0"/>KB)<label class="other-doc-type-label">
                                                                    (<iais:code code="${info.docType}"/>)</label><button
                                                                        type="button" class="btn btn-secondary btn-sm" onclick="deleteNewFile('${tmpId}')">Delete</button>
                                                                    <span data-err-ind="${info.tmpId}" class="error-msg"></span>
                                                                </div>
                                                            </c:forEach>
                                                        </c:if>
                                                    </c:forEach>
                                                    <a class="btn file-upload btn-secondary" data-upload-file="others" href="javascript:void(0);">Upload</a><span data-err-ind="others" class="error-msg"></span>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <jsp:invoke fragment="innerFooterFrag"/>
                            </div>
                        </div>
                        <%@include file="/WEB-INF/jsp/iais/include/jumpAfterDraft.jsp" %>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>