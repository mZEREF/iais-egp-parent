<div class="amended-service-info-gp">
    <h2>SERVICE SPECIFIC DOCUMENTS</h2>
    <div class="amend-preview-info">
        <p></p>
        <div class="form-check-gp">
            <div class="row">
                <div class="col-xs-12">
                    <table class="col-xs-12">
                        <c:forEach var="svcDoc" items="${currentPreviewSvcInfo.appSvcDocDtoLit}" varStatus="status">
                            <tr>
                                <td>
                                    <div class="field col-sm-6 control-label formtext"><label>${svcDoc.upFileName}:</label></div>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <span class="fileType" style="display:none">Docment1</span><span class="fileFilter" style="display:none">png</span><span class="fileMandatory" style="display:none">Yes</span>
                                </td>
                            </tr>
                            <tr class="col-xs-12">
                                <td>
                                        <%-- <a href="${pageContext.request.contextPath}/file-repo?filerepo=svcFileRoId${currentSvcCode}${status.index}&fileRo${status.index}=<iais:mask name="svcFileRoId${currentSvcCode}${status.index}" value="${svcDoc.fileRepoId}"/>&fileRepoName=${svcDoc.docName}" title="Download" class="downloadFile">${svcDoc.docName}</a>--%>
                                    <div class="fileList"><span class="filename server-site" id="130"><a href="${pageContext.request.contextPath}/file-repo?filerepo=fileRo${status.index}&fileRo${status.index}=<iais:mask name="fileRo${status.index}" value="${svcDoc.fileRepoId}"/>&fileRepoName=${svcDoc.docName}" title="Download" class="downloadFile">${svcDoc.docName}</a> (${svcDoc.docSize} KB)</span></div>
                                </td>
                            </tr>
                        </c:forEach>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>

