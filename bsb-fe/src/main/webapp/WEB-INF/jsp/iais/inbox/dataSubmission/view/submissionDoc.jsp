<div class="panel panel-default">
    <div class="panel-heading completed">
        <h4 class="panel-title">
            <a class="collapsed" data-toggle="collapse" href="#previewDocs">Primary Documents</a>
        </h4>
    </div>
    <div id="previewDocs" class="panel-collapse collapse">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal min-row">
                <c:forEach var="doc" items="${docSettings}">
                    <c:set var="savedFileList" value="${savedFiles.get(doc.type)}" />
                    <c:if test="${not empty savedFileList}">
                        <div class="form-group">
                            <div class="col-10"><strong>${doc.typeDisplay}</strong></div>
                            <div class="clear"></div>
                        </div>
                        <div>
                            <c:forEach var="file" items="${savedFileList}">
                                <div class="form-group">
                                    <div class="col-10"><p><a href="javascript:void(0)" onclick="downloadSavedFile('<iais:mask name="file" value="${file.repoId}"/>')">${file.filename}</a>(${String.format("%.1f", file.size/1024.0)}KB)</p></div>
                                    <div class="clear"></div>
                                </div>
                            </c:forEach>
                        </div>
                    </c:if>
                </c:forEach>
            </div>
        </div>
    </div>
</div>