<%@tag description="Facility supporting documents tag of facility registration" %>
<%@taglib prefix="webui" uri="http://www.ecquaria.com/webui" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="iais-bsb" uri="http://www.ecq.com/iais-bsb" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@attribute name="newFiles" required="true" type="java.util.Map<java.lang.String, java.util.List<sg.gov.moh.iais.egp.bsb.dto.file.NewDocInfo>>" %>
<%@attribute name="savedFiles" required="true" type="java.util.Map<java.lang.String, java.util.List<sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo>>" %>
<%@attribute name="newFileDLUrl" required="true" type="java.lang.String" %>
<%@attribute name="savedFileDLUrl" required="true" type="java.lang.String" %>
<%@attribute name="docTypeOps" required="true" type="java.util.List<com.ecquaria.cloud.moh.iais.common.dto.SelectOption>" %>

<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<link href="<%=sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT%>/css/bsb/bsb-common.css" rel="stylesheet"/>
<script type="text/javascript" src="<%=sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<script type="text/javascript" src="<%=sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT%>/js/bsb/bsb-common-file.js"></script>

<script>
    <% String jsonStr = (String) request.getAttribute("docTypeOpsJson");
    if (jsonStr == null || "".equals(jsonStr)) {
       jsonStr = "undefined";
    }
    %>
    var docTypeOps = <%=jsonStr%>;
</script>

<%@include file="/WEB-INF/jsp/iais/include/showErrorMsg.jsp" %>
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
    <div class="row">
        <div class="col-xs-12">
            <div class="tab-gp steps-tab">
                <div class="document-content">
                    <div class="document-upload-gp">
                        <div class="document-upload-list">
                            <h2>Supporting Documents</h2>
                            <div id="upload-other-doc-gp" class="file-upload-gp">
                                <c:if test="${savedFiles.values() ne null}">
                                    <c:forEach var="info" items="${savedFiles.values()}">
                                        <c:set var="tmpId"><iais:mask name="file" value="${info.repoId}"/></c:set>
                                        <div id="${tmpId}FileDiv">
                                            <a href="${savedFileDLUrl}${tmpId}" style="text-decoration: underline"><span id="${tmpId}Span"><c:out value="${info.filename}"/></span></a>(<fmt:formatNumber value="${info.size/1024.0}" type="number" pattern="0.0"/>KB)
                                            <label class="other-doc-type-label">(<iais:code code="${info.docType}"/>)</label>
                                            <button type="button" class="btn btn-secondary btn-sm" onclick="deleteSavedFile('${tmpId}')">Delete</button>
                                            <span data-err-ind="${info.repoId}" class="error-msg"></span>
                                        </div>
                                    </c:forEach>
                                </c:if>
                                <c:if test="${newFiles.values() ne null}">
                                    <c:forEach var="info" items="${newFiles.values()}">
                                        <c:set var="tmpId"><iais:mask name="file" value="${info.tmpId}"/></c:set>
                                        <div id="${tmpId}FileDiv">
                                            <a href="${newFileDLUrl}${tmpId}" style="text-decoration: underline"><span id="${tmpId}Span"><c:out value="${info.filename}"/></span></a>(<fmt:formatNumber value="${info.size/1024.0}" type="number" pattern="0.0"/>KB)
                                            <label class="other-doc-type-label">(<iais:code code="${info.docType}"/>)</label>
                                            <button type="button" class="btn btn-secondary btn-sm" onclick="deleteNewFile('${tmpId}')">Delete</button>
                                            <span data-err-ind="${info.tmpId}" class="error-msg"></span>
                                        </div>
                                    </c:forEach>
                                </c:if>
                                <a class="btn file-upload btn-secondary" data-upload-file="others" href="javascript:void(0);">Upload</a><span data-err-ind="others" class="error-msg"></span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>