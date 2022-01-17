<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.MaskUtil" %>
<div id="fileUploadInputDiv" style="display: none"></div>
<div class = "row">
    <div class="col-xs-12">
        <div id="PrimaryDocsPanel" role="tabpanel">
            <div class="document-content" style="margin: 0 auto;background-color: #F2F2F2;height: 100%">
                <div class="document-upload-list">
                    <c:set var="newFiles" value="${otherDocs}"/>
                    <c:set var="savedFiles" value="${savedOthersDoc}"/>
                    <h3>Others</h3>
                    <div class="file-upload-gp" style="margin-left: 40px">
                        <c:if test="${newFiles ne null}">
                            <c:forEach var="info" items="${newFiles}">
                                <c:set var="tmpId" value="${MaskUtil.maskValue('file', info.id)}"/>
                                <div id="${tmpId}FileDiv">
                                    <span id="${tmpId}Span">${info.filename}(${String.format("%.1f", info.size/1024.0)}KB)</span>
                                    <button type="button" class="btn btn-secondary btn-sm"
                                            onclick="deleteNewFile('${tmpId}')">Delete
                                    </button>
                                    <button
                                            type="button" class="btn btn-secondary btn-sm"
                                            onclick="reloadNewFile('${tmpId}')">Reload
                                    </button>

                                    <button type="button" class="btn btn-secondary btn-sm"
                                            onclick="downloadFile('new', '${tmpId}')">
                                        Download
                                    </button>
                                    <span data-err-ind="${info.id}" class="error-msg"></span>
                                </div>
                            </c:forEach>
                        </c:if>

                        <c:if test="${savedFiles ne null}">
                            <c:forEach var="savedFile" items="${savedFiles}">
                                <c:set var="tmpId" value="${MaskUtil.maskValue('file', savedFile.repoId)}"/>
                                <div id="${tmpId}FileDiv">
                                    <span id="${tmpId}Span">${savedFile.filename}(${String.format("%.1f", savedFile.size/1024.0)}KB)</span>
                                    <button type="button" class="btn btn-secondary btn-sm"
                                            onclick="deleteFile('${tmpId}')">Delete
                                    </button>
                                    <button
                                            type="button" class="btn btn-secondary btn-sm"
                                            onclick="reloadFile('${tmpId}')">Reload
                                    </button>

                                    <button type="button" class="btn btn-secondary btn-sm"
                                            onclick="downloadFile('saved', '${tmpId}')">
                                        Download
                                    </button>
                                    <span data-err-ind="${tmpId}" class="error-msg"></span>
                                </div>
                            </c:forEach>
                        </c:if>
                        <a class="btn file-upload btn-secondary" data-upload-file="others" href="javascript:void(0);">Upload</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
