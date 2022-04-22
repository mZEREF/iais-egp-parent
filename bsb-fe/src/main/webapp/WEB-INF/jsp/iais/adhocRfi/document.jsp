<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.MaskUtil" %>
<div id="fileUploadInputDiv" style="display: none"></div>
<div class = "row">
    <div class="col-xs-12">
        <div id="PrimaryDocsPanel" role="tabpanel">
            <div class="document-content" style="margin: 0 auto;background-color: #F2F2F2;height: 100%">
                <div class="document-upload-gp">
                    <c:set var="newFiles" value="${adhocReqForInfoDto.applicationDocDtos}"/>
                    <h3>Attachment</h3>
                    <div class="file-upload-list" style="margin-left: 40px">
                        <div class="file-upload-gp">
                            <c:if test="${newFiles ne null}">
                                <c:forEach var="info" items="${newFiles}">
                                    <c:set var="tmpId" value="${MaskUtil.maskValue('file', info.repoId)}"/>
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
                                                onclick="downloadFile('${tmpId}','${info.filename}')">
                                            Download
                                        </button>
                                        <span data-err-ind="${info.repoId}" class="error-msg"></span>
                                    </div>
                                </c:forEach>
                            </c:if>
                            <a class="btn file-upload btn-secondary" data-upload-file="upload" href="javascript:void(0);">Upload</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
