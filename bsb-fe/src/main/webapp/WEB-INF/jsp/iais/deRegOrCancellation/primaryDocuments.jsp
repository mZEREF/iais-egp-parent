<input type="hidden" id="deleteExistFiles" name="deleteExistFiles" value="">
<input type="hidden" id="deleteNewFiles" name="deleteNewFiles" value="">
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.MaskUtil" %>
<div id="fileUploadInputDiv" style="display: none"></div>
<div class="panel-body">
    <div class="row">
        <div class="document-upload-gp">
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