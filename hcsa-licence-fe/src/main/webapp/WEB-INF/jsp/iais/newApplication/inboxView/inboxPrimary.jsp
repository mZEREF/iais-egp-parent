<div class="panel panel-default">
    <div class="panel-heading" id="headingOne" role="tab">
        <h4 class="panel-title"><a class="collapsed" role="button" data-toggle="collapse" href="#collapseOne" aria-expanded="true" aria-controls="collapseOne">Primary Documents</a></h4>
    </div>
    <div class="panel-collapse collapse " id="collapseOne" role="tabpanel" aria-labelledby="headingOne">
        <div class="panel-body">
            <div class="elemClass-1561088919456">
                <div id="control--runtime--34" class="page section control  container-s-1" style="margin: 10px 0px">
                    <div class="control-set-font control-font-header section-header">
                        <label style="font-size: 2.2rem">Uploaded Documents</label>
                    </div>
                    <div class="pop-up">
                        <div class="pop-up-body">
                            <c:forEach var="appGrpPrimaryDoc" items="${AppSubmissionDto.appGrpPrimaryDocDtos}" varStatus="status">
                                <div class="content-body fileUploadContainer">
                                    <div class="field col-sm-4 control-label formtext"><label>${appGrpPrimaryDoc.svcComDocName}</label></div>
                                    <span class="fileType" style="display:none">Docment1</span><span class="fileFilter" style="display:none">png</span><span class="fileMandatory" style="display:none">Yes</span>
                                    <div class="control col-sm-5">
                                        <div class="fileList">
                                            <span class="filename server-site">
                                                <a class="test-btn" href="${pageContext.request.contextPath}/file-repo?filerepo=fileRo${status.index}&fileRo${status.index}=<iais:mask name="fileRo${status.index}" value="${appGrpPrimaryDocDto.fileRepoId}"/>&fileRepoName=${appGrpPrimaryDocDto.docName}" title="Download" class="downloadFile">
                                                    ${appGrpPrimaryDoc.docName}
                                                </a>
                                                (${appGrpPrimaryDoc.docSize} KB)
                                            </span>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
