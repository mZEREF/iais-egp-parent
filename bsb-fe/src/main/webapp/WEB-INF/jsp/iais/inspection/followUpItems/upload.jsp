<div id="attachmentUploadDiv" class="document-upload-gp">
    <div class="document-upload-list">
        <h3>Attachment</h3>
        <div class="file-upload-gp">
            <c:forEach var="info" items="${newSavedDoc}">
                <c:set var="tmpId" value="${MaskUtil.maskValue('file', info.tmpId)}"/>
                <div id="${tmpId}FileDiv">
                    <span id="${tmpId}Span"><a href="/bsb-fe/ajax/doc/download/commentInsReport/comment/${tmpId}">${info.filename}</a>(${String.format("%.1f", info.size/1024.0)}KB)</span><button
                        type="button" class="btn btn-secondary btn-sm" onclick="deleteNewFile('${tmpId}')">Delete</button>
                    <span data-err-ind="${info.tmpId}" class="error-msg"></span>
                </div>
            </c:forEach>
            <c:forEach var="info" items="${oldSavedDoc}">
                <c:set var="repoId" value="${MaskUtil.maskValue('file', info.repoId)}"/>
                <div id="${repoId}FileDiv">
                    <span id="${repoId}Span"><a href="/bsb-fe/ajax/doc/download/commentInsReport/comment/${repoId}">${info.filename}</a>(${String.format("%.1f", info.size/1024.0)}KB)</span><button
                        type="button" class="btn btn-secondary btn-sm" onclick="deleteSavedFile('${repoId}')">Delete</button>
                    <span data-err-ind="${info.repoId}" class="error-msg"></span>
                </div>
            </c:forEach>
            <a class="btn file-upload btn-secondary" data-upload-file="attachment" href="javascript:void(0);">Upload</a>
            <span data-err-ind="attachment" class="error-msg"></span>
        </div>
    </div>
</div>