<div class="document-upload-list">
    <h3>
        Description of organization
        <span class="mandatory"> *</span>
    </h3>
    <div class="file-upload-gp">
        <input type="hidden" name="configIndex" value="${configIndex}"/>
        <%--<span name="${configIndex}ShowId" id="${configIndex}ShowId">
            <c:forEach var="file" items="${fileList}" varStatus="fileStat">
                <div id="${configIndex}Div${file.seqNum}">
                    <c:choose>
                        <c:when test="${!file.passValidate}">
                            <c:out value="${file.docName}"/>
                        </c:when>
                        <c:otherwise>
                            <a class="<c:if test="${!isClickEdit}">disabled</c:if>" href="${pageContext.request.contextPath}/file-repo?filerepo=fileRo${v.index}&fileRo${v.index}=<iais:mask name="fileRo${v.index}" value="${file.fileRepoId}"/>&fileRepoName=${file.docName}">${file.docName}</a>
                        </c:otherwise>
                    </c:choose>
                    <button type="button" class="btn btn-secondary btn-sm delFileBtn" onclick="javascript:deleteFileFeAjax('${configIndex}',${file.seqNum});">
                        Delete
                    </button>
                    <button type="button" class="btn btn-secondary btn-sm reUploadFileBtn" onclick="javascript:reUploadFileFeAjax('${configIndex}',${file.seqNum},'mainForm');">
                        ReUpload
                    </button>
                </div>
            </c:forEach>
        </span>--%>
        <span name="iaisErrorMsg" class="error-msg" id="error_1Error"></span>
        <br/>
        <a class="btn file-upload btn-secondary" href="javascript:void(0);">Upload</a>
        <br/>
    </div>
</div>

<div class="document-upload-list">
    <h3>
        SOP for carrying out facility certification
        <span class="mandatory"> *</span>
    </h3>
    <div class="file-upload-gp">
        <input type="hidden" name="configIndex" value="${configIndex}"/>
        <span name="iaisErrorMsg" class="error-msg" id="error_2Error"></span>
        <br/>
        <a class="btn file-upload btn-secondary" href="javascript:void(0);">Upload</a>
        <br/>
    </div>
</div>

<div class="document-upload-list">

    <h3>
        CV of each certifying team member
        <span class="mandatory"> *</span>
    </h3>
    <div class="file-upload-gp">
        <input type="hidden" name="configIndex" value="${configIndex}"/>
        <span name="iaisErrorMsg" class="error-msg" id="error_3Error"></span>
        <br/>
        <a class="btn file-upload btn-secondary" href="javascript:void(0);">Upload</a>
        <br/>
    </div>
</div>

<div class="document-upload-list">
    <h3>
        Testimonials for each certifying team member
        <span class="mandatory"> *</span>
    </h3>
    <div class="file-upload-gp">
        <input type="hidden" name="configIndex" value="${configIndex}"/>
        <span name="iaisErrorMsg" class="error-msg" id="error_4Error"></span>
        <br/>
        <a class="btn file-upload btn-secondary" href="javascript:void(0);">Upload</a>
        <br/>
    </div>
</div>