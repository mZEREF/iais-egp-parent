<input type="hidden" name="action_type" value="">
<input type="hidden" name="action_value" value="">
<input type="hidden" name="action_additional" value="">
<input type="hidden" id="deleteExistFiles" name="deleteExistFiles" value="">
<input type="hidden" id="deleteNewFiles" name="deleteNewFiles" value="">
<div id="fileUploadInputDiv" style="display: none"></div>
<div class = "row">
    <div class="col-xs-12">
        <div id="PrimaryDocsPanel" role="tabpanel">
            <div class="document-content">
                <div class="file-upload-gp">
                    <c:if test="${savedFiles ne null}">
                        <c:forEach var="info" items="${savedFiles}">
                            <c:set var="tmpId"
                                   value="${MaskUtil.maskValue('file', info.repoId)}"/>
                            <div id="${tmpId}FileDiv">
                                <span id="${tmpId}Span">${info.filename}(${String.format("%.1f", info.size/1024.0)}KB)</span>
                                <button
                                        type="button" class="btn btn-secondary btn-sm"
                                        onclick="deleteSavedFile('${tmpId}')">Delete
                                </button>
                                <button
                                        type="button" class="btn btn-secondary btn-sm"
                                        onclick="reloadSavedFile('${tmpId}')">
                                    Reload
                                </button>
                                <button
                                        type="button" class="btn btn-secondary btn-sm"
                                        onclick="downloadFile('saved', '${tmpId}', '${info.filename}')">
                                    Download
                                </button>
                                <span data-err-ind="${info.repoId}" class="error-msg"></span>
                            </div>
                        </c:forEach>
                    </c:if>
                    <c:if test="${newFiles ne null}">
                        <c:forEach var="info" items="${newFiles}">
                            <c:set var="tmpId" value="${MaskUtil.maskValue('file', info.tmpId)}"/>
                            <div id="${tmpId}FileDiv">
                                <span id="${tmpId}Span">${info.filename}(${String.format("%.1f", info.size/1024.0)}KB)</span>
                                <button type="button" class="btn btn-secondary btn-sm"
                                        onclick="deleteNewFile('${tmpId}')">Delete
                                </button>
                                <button type="button" class="btn btn-secondary btn-sm"
                                        onclick="reloadNewFile('${tmpId}')">
                                    Reload
                                </button>
                                <button type="button" class="btn btn-secondary btn-sm"
                                        onclick="downloadFile('new', '${tmpId}', '${info.filename}')">
                                    Download
                                </button>
                                <span data-err-ind="${info.tmpId}" class="error-msg"></span>
                            </div>
                        </c:forEach>
                    </c:if>
                    <a class="btn file-upload btn-secondary" data-upload-file="upload--v--${status.index}" href="javascript:void(0);">Upload</a>
                </div>
            </div>
        </div>
    </div>
</div>
