<div class="form-group col-xs-12 document-upload-list ">
    <h3>
        <c:out value="${doc.displayTitle}"/><c:if test="${doc.mandatory}"><span class="mandatory"> *</span></c:if>
    </h3>
    <div class="file-upload-gp">
        <input type="hidden" name="configIndex" value="${configIndex}"/>
        <span name="${configIndex}ShowId" id="${configIndex}ShowId">
            <c:forEach var="file" items="${doc.appSvcDocDtoList}" varStatus="fileStat">
                <div id="${configIndex}Div${file.seqNum}">
                    <c:choose>
                        <c:when test="${!file.passValidate}">
                            <c:out value="${file.docName}"/>
                        </c:when>
                        <c:otherwise>
                            <iais:downloadLink fileRepoIdName="fileRo${v.index}" fileRepoId="${file.fileRepoId}" docName="${file.docName}"/>
                        </c:otherwise>
                    </c:choose>
                    <button type="button" class="btn btn-secondary btn-sm delFileBtn" onclick="javascript:deleteFileFeAjax('${configIndex}',${file.seqNum});">
                        Delete
                    </button>
                    <button type="button" class="btn btn-secondary btn-sm reUploadFileBtn" onclick="javascript:reUploadFileFeAjax('${configIndex}',${file.seqNum},'mainForm');">
                        ReUpload
                    </button>
                </div>
                <span name="iaisErrorMsg" class="error-msg" id="error_${configIndex}${file.seqNum}Error"></span>
            </c:forEach>
        </span>
        <span name="iaisErrorMsg" class="error-msg" id="error_${configIndex}Error"></span>
        <br/>
        <a class="btn file-upload btn-secondary" href="javascript:void(0);">Upload</a>
        <br/>
    </div>
</div>