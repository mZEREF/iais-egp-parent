<div class="panel-main-content form-horizontal min-row">
    <c:forEach var="doc" items="${docSettings}">
        <c:set var="maskDocType" value="${MaskUtil.maskValue('file', doc.type)}"/>
        <c:set var="savedFileList" value="${savedFiles.get(doc.type)}" />
        <c:set var="newFileList" value="${newFiles.get(doc.type)}" />
        <c:if test="${not empty savedFileList or not empty newFileList}">
            <div class="form-group">
                <div class="col-10"><strong>${doc.typeDisplay}</strong></div>
                <div class="clear"></div>
            </div>
            <div>
                <c:forEach var="file" items="${savedFileList}">
                    <c:set var="tmpId" value="${MaskUtil.maskValue('file', file.repoId)}"/>
                    <div class="form-group">
                        <div class="col-10"><p><a href="javascript:void(0)" onclick="downloadFile('saved', '${tmpId}')">${file.filename}</a>(${String.format("%.1f", file.size/1024.0)}KB)</p></div>
                        <div class="clear"></div>
                    </div>
                </c:forEach>
                <c:forEach var="file" items="${newFileList}">
                    <c:set var="tmpId" value="${MaskUtil.maskValue('file', file.tmpId)}"/>
                    <div class="form-group">
                        <div class="col-10"><p><a href="javascript:void(0)" onclick="downloadFile('new', '${tmpId}')">${file.filename}</a>(${String.format("%.1f", file.size/1024.0)}KB)</p></div>
                        <div class="clear"></div>
                    </div>
                </c:forEach>
            </div>
        </c:if>
    </c:forEach>
</div>