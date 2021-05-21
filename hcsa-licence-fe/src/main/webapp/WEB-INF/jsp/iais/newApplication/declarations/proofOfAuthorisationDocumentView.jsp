<div class="panel-body">
    <div class="row">
        <P>If you have selected that <span style="font-style: italic">'I am duly authorised by the Applicant to make this application on its behalf and
          the Applicant will be the licensee if the application is granted'</span>, please attach proof of your authorisation
            below:</P>
        <br>
        <div class="document-upload-gp">
            <div class="document-upload-list Proof-Authorisation">
                <h3>Proof of Authorisation</h3>
                <div class="file-upload-gp">
                    <span name="selected${sec}FileShowId" id="selected${sec}FileShowId">
                    <c:forEach items="${pageShowFileDtos}" var="pageShowFileDto" varStatus="ind">
                        <div id="${pageShowFileDto.fileMapId}">
                          <span name="fileName" style="font-size: 14px;color: #2199E8;text-align: center">
                          <a href="${pageContext.request.contextPath}/file-repo?filerepo=fileRo0&fileRo0=<iais:mask name="fileRo0" value="${pageShowFileDto.fileUploadUrl}"/>&fileRepoName=${pageShowFileDto.fileName}"
                             title="Download" class="downloadFile">${pageShowFileDto.fileName}</a>
                          </span>
                        </div>
                    </c:forEach>
                    </span>
                </div>
            </div>
        </div>
    </div>
</div>

