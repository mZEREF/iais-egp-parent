<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT"%>
<%@ taglib prefix="webui" uri="http://www.ecquaria.com/webui" %>
<%@ taglib prefix="ias" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
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
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common-facility-register.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-rfc-common.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-rfc-facility-register.js"></script>

<%@include file="/WEB-INF/jsp/iais/include/showErrorMsg.jsp"%>

<%@include file="../common/dashboard.jsp"%>

<%--@elvariable id="savedFiles" type="java.util.Map<java.lang.String, java.util.List<sg.gov.moh.iais.egp.bsb.dto.register.facility.PrimaryDocDto$DocRecordInfo>>"--%>
<%--@elvariable id="newFiles" type="java.util.Map<java.lang.String, java.util.List<sg.gov.moh.iais.egp.bsb.dto.register.facility.PrimaryDocDto$NewDocInfo>>"--%>
<%--@elvariable id="docSettings" type="java.util.List<sg.gov.moh.iais.egp.bsb.entity.DocSetting>"--%>
<form method="post" id="mainForm" enctype="multipart/form-data" action="<%=process.runtime.continueURL()%>">
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="action_type" value="">
    <input type="hidden" name="action_value" value="">
    <input type="hidden" name="action_additional" value="">
    <input type="hidden" id="deleteExistFiles" name="deleteExistFiles" value="">
    <input type="hidden" id="deleteNewFiles" name="deleteNewFiles" value="">
    <div id="fileUploadInputDiv" style="display: none"></div>
    <div class="main-content">
        <div class="container">
            <div class="row">
                <div class="col-xs-12">
                    <div class="tab-gp steps-tab">
                        <%@ include file="InnerNavTab.jsp" %>
                        <div class="tab-content">
                            <div class="tab-pane fade in active">
                                <div id="PrimaryDocsPanel" role="tabpanel">
                                    <div class="document-content">
                                        <div class="document-info-list">
                                            <ul>
                                                <li><p>The maximum file size for each upload is 5MB</p></li>
                                                <li><p>Acceptable file formats are PDF, word, JPG, Excel and PNG</p></li>
                                            </ul>
                                        </div>
                                        <div class="text-right"><a id="edit" href="javascript:void(0)"><em class="fa fa-pencil-square-o"></em>Edit</a></div>
                                        <div class="document-upload-gp">
                                            <h2>PRIMARY DOCUMENTS</h2>
                                            <c:forEach var="doc" items="${docSettings}">
                                                <c:set var="maskDocType" value="${MaskUtil.maskValue('file', doc.type)}"/>
                                                <div class="document-upload-list">
                                                    <h3>${doc.typeDisplay}<c:if test="${doc.mandatory}"> <span class="mandatory otherQualificationSpan">*</span></c:if></h3>
                                                    <div class="file-upload-gp">
                                                        <c:if test="${savedFiles.get(doc.type) ne null}">
                                                            <c:forEach var="info" items="${savedFiles.get(doc.type)}">
                                                                <c:set var="tmpId" value="${MaskUtil.maskValue('file', info.repoId)}"/>
                                                                <div id="${tmpId}FileDiv">
                                                                    <span id="${tmpId}Span">${info.filename}(${String.format("%.1f", info.size/1024.0)}KB)</span><button
                                                                        type="button" class="btn btn-secondary btn-sm" onclick="deleteSavedFile('${tmpId}')">Delete</button><button
                                                                        type="button" class="btn btn-secondary btn-sm" onclick="reloadSavedFile('${tmpId}', '${maskDocType}')">Reload</button><button
                                                                        type="button" class="btn btn-secondary btn-sm" onclick="downloadFile('saved', '${tmpId}')">Download</button>
                                                                    <span data-err-ind="${info.repoId}" class="error-msg"></span>
                                                                </div>
                                                            </c:forEach>
                                                        </c:if>
                                                        <c:if test="${newFiles.get(doc.type) ne null}">
                                                            <c:forEach var="info" items="${newFiles.get(doc.type)}">
                                                                <c:set var="tmpId" value="${MaskUtil.maskValue('file', info.tmpId)}"/>
                                                                <div id="${tmpId}FileDiv">
                                                                    <span id="${tmpId}Span">${info.filename}(${String.format("%.1f", info.size/1024.0)}KB)</span><button
                                                                        type="button" class="btn btn-secondary btn-sm" onclick="deleteNewFile('${tmpId}')">Delete</button><button
                                                                        type="button" class="btn btn-secondary btn-sm" onclick="reloadNewFile('${tmpId}', '${maskDocType}')">Reload</button><button
                                                                        type="button" class="btn btn-secondary btn-sm" onclick="downloadFile('new', '${tmpId}')">Download</button>
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

                                <%@ include file="../common/InnerFooter.jsp" %>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>