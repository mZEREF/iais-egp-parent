<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="row" style="margin: 50px 0">
    <ul>
        <li>The maximum file size per document is 10 MB.</li>
        <li>Acceptable file formats: JPG, PNG, PDF, CSV, DOCX, JPEG, XLS, DOC and XLSX</li>
        <li>Please ensure that the corresponding Document Type is selected for each document uploaded under Others.</li>
    </ul>
</div>
<div id="attachmentUploadDiv" class="document-upload-gp">
    <div class="document-upload-list">
        <h3>Attachment</h3>
        <div id="upload-other-doc-gp" class="file-upload-gp">
            <c:forEach var="info" items="${newSavedDoc}">
                <c:set var="tmpId"><iais:mask name="file" value="${info.tmpId}"/></c:set>
                <div id="${tmpId}FileDiv">
                    <span id="${tmpId}Span"><a href="/bsb-web/ajax/doc/download/commentInsReport/comment/${tmpId}">${info.filename}</a>(<fmt:formatNumber value="${info.size/1024.0}" type="number" pattern="0.0"/>KB)</span><label class="other-doc-type-label">
                    (<iais:code code="${info.docType}"/>)</label><button
                        type="button" class="btn btn-secondary btn-sm" onclick="deleteNewFile('${tmpId}')">Delete</button>
                    <span data-err-ind="${info.tmpId}" class="error-msg"></span>
                </div>
            </c:forEach>
            <c:forEach var="info" items="${oldSavedDoc}">
                <c:set var="repoId"><iais:mask name="file" value="${info.repoId}"/></c:set>
                <div id="${repoId}FileDiv">
                    <span id="${repoId}Span"><a href="/bsb-web/ajax/doc/download/commentInsReport/comment/${repoId}">${info.filename}</a>(<fmt:formatNumber value="${info.size/1024.0}" type="number" pattern="0.0"/>KB)</span><label class="other-doc-type-label">
                    (<iais:code code="${info.docType}"/>)</label><button
                        type="button" class="btn btn-secondary btn-sm" onclick="deleteSavedFile('${repoId}')">Delete</button>
                    <span data-err-ind="${info.repoId}" class="error-msg"></span>
                </div>
            </c:forEach>
            <a class="btn file-upload btn-secondary" data-upload-file="others" href="javascript:void(0);">Upload</a>
            <span data-err-ind="others" class="error-msg"></span>
        </div>
    </div>
</div>