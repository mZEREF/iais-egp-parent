<div style="display: none">
    <input class="inputtext-required" id="selectedFileInput" name="selectedFileInput" type="file">
    <input type="hidden" name="docType" value="">
</div>
<%--@elvariable id="commonDocDto" type="sg.gov.moh.iais.egp.bsb.dto.inspection.afc.AFCCommonDocDto"--%>
<div id="attachmentUploadDiv" class="document-upload-gp">
    <div class="document-upload-list">
        <h3>Attachment</h3>
        <div class="file-upload-gp">
            <c:forEach var="info" items="${commonDocDto.newDocMap.values()}">
                <div id="${info.disPlayDto.maskedRepoId}FileDiv">
                    <span id="${info.disPlayDto.maskedRepoId}Span"><a href="javascript:void(0)" onclick="downloadFile('new','${info.disPlayDto.maskedRepoId}')">${info.disPlayDto.docName}</a>(${String.format("%.1f", info.disPlayDto.size/1024.0)}KB)</span>
                    <button type="button" class="btn btn-secondary btn-sm" onclick="deleteNewFile('${info.disPlayDto.maskedRepoId}')">Delete</button>
                    <span data-err-ind="${info.disPlayDto.maskedRepoId}" class="error-msg"></span>
                </div>
            </c:forEach>
<%--            <c:forEach var="info" items="${oldSavedDoc}">--%>
<%--                <c:set var="repoId" value="${MaskUtil.maskValue('file', info.repoId)}"/>--%>
<%--                <div id="${repoId}FileDiv">--%>
<%--                    <span id="${repoId}Span"><a href="/bsb-fe/ajax/doc/download/commentInsReport/comment/${repoId}">${info.filename}</a>(${String.format("%.1f", info.size/1024.0)}KB)</span>--%>
<%--                    <button type="button" class="btn btn-secondary btn-sm" onclick="deleteSavedFile('${repoId}')">Delete</button>--%>
<%--                    <span data-err-ind="${info.repoId}" class="error-msg"></span>--%>
<%--                </div>--%>
<%--            </c:forEach>--%>
            <%--@elvariable id="canNotUploadInternalDoc" type="java.lang.Boolean"--%>
            <c:if test="${canNotUploadInternalDoc eq null or !canNotUploadInternalDoc}">
                <button type="button" id="uploadDocModalBtn" class="btn btn-secondary" data-toggle="modal" data-target="#uploadDoc">
                    Upload
                </button>
            </c:if>
        </div>
        <span data-err-ind="attachment" class="error-msg"></span>
        <span data-err-ind="chooseOneAction" class="error-msg"></span>
    </div>
</div>