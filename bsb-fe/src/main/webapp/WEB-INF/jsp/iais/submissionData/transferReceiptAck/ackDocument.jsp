<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.MaskUtil" %>
<div class = "row">
    <div class="col-xs-12">
        <div id="PrimaryDocsPanel" role="tabpanel">
            <div class="document-content" style="margin: 0 auto;background-color: #F2F2F2;height: 100%;padding: 10px">
                <div class="document-upload-list--v--${status.index}">
                    <c:set var="newFiles" value="${keyNewMap.get(status.index)}"/>
                    <c:set var="savedFiles" value="${keySavedMap.get(status.index)}"/>
                    <h3>
                        <c:choose>
                            <c:when test="${item.scheduleType eq 'SCHTYPE050'}">
                                Inventory: Toxins <span class="mandatory otherQualificationSpan">*</span>
                            </c:when>
                            <c:otherwise>
                                Inventory: Biological Agents <span class="mandatory otherQualificationSpan">*</span>
                            </c:otherwise>
                        </c:choose>
                    </h3>
                    <div class="file-upload-gp" style="margin-left: 40px">
                        <c:if test="${savedFiles ne null}">
                            <c:forEach var="info" items="${savedFiles}">
                                <c:set var="tmpId" value="${MaskUtil.maskValue('file', info.repoId)}"/>
                                <div id="${tmpId}FileDiv">
                                    <span id="${tmpId}Span">${info.filename}(${String.format("%.1f", info.size/1024.0)}KB)</span><button
                                        type="button" class="btn btn-secondary btn-sm" onclick="deleteFile('${tmpId}')">Delete</button><button
                                        type="button" class="btn btn-secondary btn-sm" onclick="reloadFile('${tmpId}','${status.index}')">Reload</button><button
                                        type="button" class="btn btn-secondary btn-sm" onclick="downloadFile('saved', '${tmpId}')">Download</button>
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
                                    <button
                                            type="button" class="btn btn-secondary btn-sm"
                                            onclick="reloadNewFile('${tmpId}')">Reload
                                    </button>

                                    <button type="button" class="btn btn-secondary btn-sm"
                                            onclick="downloadFile('new', '${tmpId}')">
                                        Download
                                    </button>
                                    <span data-err-ind="${info.tmpId}" class="error-msg"></span>
                                </div>
                            </c:forEach>
                        </c:if>
                        <a class="btn file-upload btn-secondary" data-upload-file="upload--v--${status.index}" href="javascript:void(0);">Upload</a>
                        <span data-err-ind="upload--v--${status.index}" class="error-msg"></span>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
