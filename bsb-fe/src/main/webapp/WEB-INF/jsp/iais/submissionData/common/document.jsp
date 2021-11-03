    <input type="hidden" name="action_type" value="">
    <input type="hidden" name="action_value" value="">
    <input type="hidden" name="action_additional" value="">
    <input type="hidden" id="deleteExistFiles" name="deleteExistFiles" value="">
    <input type="hidden" id="deleteNewFiles" name="deleteNewFiles" value="">
    <div id="fileUploadInputDiv" style="display: none"></div>
            <div class="row">
                <div class="col-xs-12">
                    <div class="tab-content">
                        <div class="tab-pane fade in active">
                            <div id="PrimaryDocsPanel" role="tabpanel">
                                <div class="document-content">
                                    <div class="document-info-list">
                                        <ul>
                                            <li><p>The maximum file size for each upload is 5MB</p></li>
                                            <li><p>Acceptable file formats are PDF, .docx, or .csv, .xlsx</p></li>
                                        </ul>
                                    </div>
                                    <c:set var="maskDocType" value="${MaskUtil.maskValue('file', doc.type)}"/>
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
                                                                onclick="reloadSavedFile('${tmpId}', '${maskDocType}')">
                                                            Reload
                                                        </button>
                                                        <button
                                                                type="button" class="btn btn-secondary btn-sm"
                                                                onclick="downloadFile('saved', '${tmpId}', '${maskDocType}', '${info.filename}')">
                                                            Download
                                                        </button>
                                                        <span data-err-ind="${info.repoId}" class="error-msg"></span>
                                                    </div>
                                                </c:forEach>
                                            </c:if>
                                            <c:if test="${newFiles ne null}">
                                                <c:forEach var="info" items="${newFiles}">
                                                    <c:set var="tmpId"
                                                           value="${MaskUtil.maskValue('file', info.tmpId)}"/>
                                                    <div id="${tmpId}FileDiv">
                                                        <span id="${tmpId}Span">${info.filename}(${String.format("%.1f", info.size/1024.0)}KB)</span>
                                                        <button type="button" class="btn btn-secondary btn-sm"
                                                                onclick="deleteNewFile('${tmpId}')">Delete
                                                        </button>
                                                        <button type="button" class="btn btn-secondary btn-sm"
                                                                onclick="reloadNewFile('${tmpId}', '${maskDocType}')">
                                                            Reload
                                                        </button>
                                                        <button type="button" class="btn btn-secondary btn-sm"
                                                                onclick="downloadFile('new', '${tmpId}', '${maskDocType}', '${info.filename}')">
                                                            Download
                                                        </button>
                                                        <span data-err-ind="${info.tmpId}" class="error-msg"></span>
                                                    </div>
                                                </c:forEach>
                                            </c:if>
                                            <a class="btn file-upload btn-secondary" data-upload-file="${maskDocType}"
                                               href="javascript:void(0);">Upload</a><span data-err-ind="${doc.type}"
                                                                                          class="error-msg"></span>
                                        </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

