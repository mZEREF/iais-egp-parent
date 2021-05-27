<div class="panel-body">
    <div class="row">
        <P>If you have selected that <span style="font-style: italic">'I am duly authorised by the Applicant to make this application on its behalf and
            the Applicant will be the licensee if the application is granted'</span>, please attach proof of your authorisation
            below:</P>
        <br>
        <div class="document-upload-gp">
            <div class="document-upload-list">
                <h3>Proof of Authorisation </h3>
                <div class="file-upload-gp">
                            <span name="selectedWdFileShowId" id="selectedWdFileShowId">
                            <c:forEach items="${pageShowFileDtos}"
                                       var="pageShowFileDto"
                                       varStatus="ind">
                              <div id="${pageShowFileDto.fileMapId}">
                                  <span name="fileName"
                                        style="font-size: 14px;color: #2199E8;text-align: center">
                                  <a href="${pageContext.request.contextPath}/file-repo?filerepo=fileRo0&fileRo0=<iais:mask name="fileRo0" value="${pageShowFileDto.fileUploadUrl}"/>&fileRepoName=${withdrawPageShowFile.fileName}"
                                     title="Download"
                                     class="downloadFile">${pageShowFileDto.fileName}</a></span>
                                  <span class="error-msg" name="iaisErrorMsg"
                                        id="file${ind.index}"></span>
                                  <span class="error-msg" name="iaisErrorMsg"
                                        id="error_${configIndex}error"></span>
                                <button type="button" class="btn btn-secondary btn-sm"
                                        onclick="javascript:deleteFileFeAjax('selectedFile',${pageShowFileDto.index});">
                                Delete</button>  <button type="button"
                                                         class="btn btn-secondary btn-sm"
                                                         onclick="javascript:reUploadFileFeAjax('selectedFile',${pageShowFileDto.index},'mainForm');">
                              ReUpload</button>
                              </div>
                            </c:forEach>
                            </span>
                    <input id="selectedFile" name="selectedFile"
                           class="selectedFile commDoc"
                           type="file" style="display: none;"
                           aria-label="selectedFile1"
                           onclick="fileClicked(event)"
                           onchange="doUserRecUploadConfirmFile(event)"/><a
                        class="btn btn-file-upload btn-secondary"
                        onclick="doFileAddEvent()">Upload</a>
                </div>
                <span class="error-msg" id="error_litterFile_Show" name="error_litterFile_Show"
                      style="color: #D22727; font-size: 1.6rem"></span>
                <span id="error_selectedFileError" name="iaisErrorMsg" class="error-msg"></span>
            </div>
        </div>
    </div>
</div>
