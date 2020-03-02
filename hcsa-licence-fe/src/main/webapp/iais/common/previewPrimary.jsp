<div class="panel panel-default">
    <div class="panel-heading
        <c:if test="${!FirstView}">
            <c:if test="${Msg.premiss==null}">completed </c:if> <c:if test="${Msg.premiss!=null}">incompleted </c:if>
        </c:if>" id="headingOne" role="tab">
        <h4 class="panel-title"><a role="button" data-toggle="collapse" href="#collapseOne" aria-expanded="true" aria-controls="collapseOne">Primary Documents</a></h4>
    </div>
    <div class="panel-collapse collapse " id="collapseOne" role="tabpanel" aria-labelledby="headingOne">
        <div class="panel-body">
            <c:if test="${AppSubmissionDto.appEditSelectDto==null||AppSubmissionDto.appEditSelectDto.docEdit}">
                <p class="text-right mb-0"><a href="#" id="docEdit"><em class="fa fa-pencil-square-o"></em>Edit</a></p>
            </c:if>
            <div class="elemClass-1561088919456">
                <div id="control--runtime--34" class="page section control  container-s-1" style="margin: 10px 0px">
                    <div class="control-set-font control-font-header section-header">
                        <h2 class="summary-header">Uploaded Documents
                        </h2>
                    </div>
                    <div class="pop-up">
                        <div class="pop-up-body">
                            <c:forEach var="appGrpPrimaryDocDto" items="${AppSubmissionDto.appGrpPrimaryDocDtos}" varStatus="status">
                                <div class="content-body fileUploadContainer">
                                    <div class="field col-sm-4 control-label formtext"><label>Docment1 for Premise1:</label></div>
                                    <span class="fileType" style="display:none">Docment1</span><span class="fileFilter" style="display:none">png</span><span class="fileMandatory" style="display:none">Yes</span>
                                    <div class="control col-sm-5">
                                        <div class="fileList "><span class="filename server-site" id="130"><a class="test-btn" href="${pageContext.request.contextPath}/file-repo?filerepo=fileRo${status.index}&fileRo${status.index}=<iais:mask name="fileRo${status.index}" value="${appGrpPrimaryDocDto.fileRepoId}"/>&fileRepoName=${appGrpPrimaryDocDto.docName}" title="Download" class="downloadFile">${appGrpPrimaryDocDto.docName}</a> (${appGrpPrimaryDocDto.docSize} KB)</span></div>
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
