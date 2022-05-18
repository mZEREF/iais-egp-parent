<input type="hidden" id="deleteExistFiles" name="deleteExistFiles" value="">
<input type="hidden" id="deleteNewFiles" name="deleteNewFiles" value="">
<input id="multiUploadTrigger" type="file" multiple="multiple" style="display: none"/>
<input id="echoReloadTrigger" type="file" style="display: none"/>
<div id="fileUploadInputDiv" style="display: none"></div>
<div class="document-upload-gp">
    <c:forEach var="doc" items="${docSettings}">
        <c:set var="maskDocType"><iais:mask name="file" value="${doc.type}"/></c:set>
        <div class="document-upload-list">
            <h3>${doc.typeDisplay}<c:if test="${doc.mandatory}"> <span class="mandatory otherQualificationSpan">*</span></c:if></h3>
            <div class="file-upload-gp">
                <c:if test="${savedFiles.get(doc.type) ne null}">
                    <c:forEach var="info" items="${savedFiles.get(doc.type)}">
                        <c:set var="tmpId"><iais:mask name="file" value="${info.repoId}"/></c:set>
                        <div id="${tmpId}FileDiv">
                            <a href="/bsb-web/ajax/doc/download/reportableEvent/followup/repo/${tmpId}" style="text-decoration: underline"><span id="${tmpId}Span">${info.filename}</span></a>(<fmt:formatNumber value="${info.size/1024.0}" type="number" pattern="0.0"/>KB)<button
                                type="button" class="btn btn-secondary btn-sm" onclick="deleteNewFile('${tmpId}')">Delete</button><button
                                type="button" class="btn btn-secondary btn-sm" onclick="reloadNewFile('${tmpId}', '${maskDocType}')">Reload</button>
                            <span data-err-ind="${info.repoId}" class="error-msg"></span>
                        </div>
                    </c:forEach>
                </c:if>
                <c:if test="${newFiles.get(doc.type) ne null}">
                    <c:forEach var="info" items="${newFiles.get(doc.type)}">
                        <c:set var="tmpId"><iais:mask name="file" value="${info.tmpId}"/></c:set>
                        <div id="${tmpId}FileDiv">
                            <a href="/bsb-web/ajax/doc/download/reportableEvent/followup/new/${tmpId}" style="text-decoration: underline"><span id="${tmpId}Span">${info.filename}</span></a>(<fmt:formatNumber value="${info.size/1024.0}" type="number" pattern="0.0"/>KB)<button
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