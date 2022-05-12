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
                    <c:forEach items="${AppSubmissionDto.appDeclarationDocDtos}" var="appDeclarationDocDto" varStatus="ind">
                        <div >
                          <span name="fileName" style="font-size: 14px;color: #2199E8;text-align: center">
                              <iais:downloadLink fileRepoIdName="fileRo0" fileRepoId="${appDeclarationDocDto.fileRepoId}" docName="${appDeclarationDocDto.docName}"/>
                          </span>
                        </div>
                    </c:forEach>
                    </span>
                </div>
            </div>
        </div>
    </div>
</div>

