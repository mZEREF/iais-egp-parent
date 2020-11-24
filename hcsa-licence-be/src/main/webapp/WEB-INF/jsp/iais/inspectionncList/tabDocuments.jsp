<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>
<div class="alert alert-info" role="alert"><strong>
    <h4>Supporting Document</h4>
</strong></div>
<div id="u8522_text" class="text ">
    <p><span>These are documents uploaded by the applicant or an officer on behalf of the applicant. Listed
												documents are those defined for this digital service only.</span></p>
</div>
<div class="row">
    <div class="col-xs-12">
        <div class="table-gp">
            <table class="table">
                <thead>
                <tr>
                    <th width="20%">Document</th>
                    <th width="20%">File</th>
                    <th width="10%">Size</th>
                    <th width="10%">Version</th>
                    <th width="20%">Submitted By</th>
                    <th width="20%">Date Submitted</th>
                </tr>
                </thead>
                <tbody>
                <c:if test="${applicationViewDto.applicationDto.applicationType =='APTY008' || applicationViewDto.applicationDto.applicationType == 'APTY007'}">
                    <tr>
                        <td colspan="6" align="center">
                            <iais:message key="GENERAL_ACK018"
                                          escape="true"/>
                        </td>
                    </tr>
                </c:if>

                <c:if test="${applicationViewDto.applicationDto.applicationType !='APTY008' && applicationViewDto.applicationDto.applicationType != 'APTY007'}">
                    <c:if test="${empty applicationViewDto.appSupDocDtoList}">
                        <tr>
                            <td colspan="6" align="center">
                                <iais:message key="GENERAL_ACK018"
                                              escape="true"/>
                            </td>
                        </tr>
                    </c:if>
                    <c:forEach items="${applicationViewDto.appSupDocDtoList}"
                               var="appSupDocDto">
                        <tr>
                            <td width="20%">
                                <p><c:out value="${appSupDocDto.file}"></c:out></p>
                            </td>
                            <td width="20%">
                                <p>
                                    <a hidden href="${pageContext.request.contextPath}/file-repo?filerepo=fileRo${status.index}&fileRo${status.index}=<iais:mask name="fileRo${status.index}"  value="${appSupDocDto.fileRepoId}"/>&fileRepoName=${appSupDocDto.document}"
                                       title="Download" class="downloadFile"><span id="${appSupDocDto.fileRepoId}Down">trueDown</span> </a>
                                    <a onclick="doVerifyFileGo('${appSupDocDto.fileRepoId}')"><c:out value="${appSupDocDto.document}"></c:out></a>
                                </p>
                            </td>
                            <td width="10%">
                                <p><c:out value="${appSupDocDto.size}"></c:out></p>
                            </td>
                            <td width="10%">
                                <p><c:out value="${appSupDocDto.version}"></c:out></p>
                            </td>
                            <td width="20%">
                                <p><c:out value="${appSupDocDto.submittedBy}"></c:out></p>
                            </td>
                            <td width="20%">
                                <p><c:out value="${appSupDocDto.dateSubmitted}"></c:out></p>
                            </td>
                        </tr>
                    </c:forEach>
                </c:if>
                </tbody>

            </table>
            <div class="alert alert-info" role="alert"><strong>
                <h4>Internal Document</h4>
            </strong></div>
            <div class="text ">
                <p><span>These are documents uploaded by an agency officer to support back office processing.</span>
                </p>
            </div>
            <table class="table">
                <thead>
                <tr>
                    <th width="30%">Document</th>
                    <th width="20%">File</th>
                    <th width="10%">Size</th>
                    <th width="20%">Submitted By</th>
                    <th width="15%">Date Submitted</th>
                    <th width="5%">Action</th>
                </tr>
                </thead>
                <tbody id="tbodyFileListId">
                <c:if test="${applicationViewDto.applicationDto.applicationType =='APTY008'}">
                    <tr>
                        <td colspan="6" align="center">
                            <iais:message key="GENERAL_ACK018"
                                          escape="true"/>
                        </td>
                    </tr>
                </c:if>
                <c:if test="${applicationViewDto.applicationDto.applicationType !='APTY008'}">
                    <c:choose>
                        <c:when test="${empty applicationViewDto.appIntranetDocDtoList}">
                            <tr>
                                <td colspan="6" align="center">
                                    <iais:message key="GENERAL_ACK018"
                                                  escape="true"/>
                                </td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="interalFile" items="${applicationViewDto.appIntranetDocDtoList}"
                                       varStatus="status">
                                <tr>
                                    <td width="30%">
                                        <p><c:out value="${interalFile.docDesc}"></c:out></p>
                                    </td>
                                    <td width="20%">
                                        <p>
                                            <a hidden href="${pageContext.request.contextPath}/file-repo?filerepo=fileRo${status.index}&fileRo${status.index}=<iais:mask name="fileRo${status.index}"  value="${interalFile.fileRepoId}"/>&fileRepoName=${interalFile.docName}.${interalFile.docType}"
                                               title="Download" class="downloadFile"><span id="${interalFile.fileRepoId}Down">trueDown</span></a>
                                            <a  onclick="doVerifyFileGo('${interalFile.fileRepoId}')"><c:out
                                                    value="${interalFile.docName}.${interalFile.docType}"></c:out>
                                            </a>
                                        </p>
                                    </td>
                                    <td width="10%">
                                        <p><c:out value="${interalFile.docSize}"></c:out></p>
                                    </td>
                                    <td width="20%">
                                        <p><c:out value="${interalFile.submitByName}"></c:out></p>
                                    </td>
                                    <td width="15%">
                                        <p>${interalFile.submitDtString}</p>
                                    </td>
                                    <td width="5%">
                                        <c:if test="${interalFile.appDocType == ApplicationConsts.APP_DOC_TYPE_COM}">
                                            <button type="button" class="btn btn-danger btn-sm"
                                                    onclick="javascript:deleteFile(this,'<iais:mask name="interalFileId"
                                                                                                    value="${interalFile.id}"/>');">
                                                <em class="fa fa-times"></em></button>
                                        </c:if>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                </c:if>


                </tbody>
            </table>
            <%--upload file--%>
            <div align="right">
                <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#uploadDoc">
                    Upload Document
                </button>
            </div>
        </div>
    </div>
</div>
<iais:confirm msg="GENERAL_ACK018"  needCancel="false" callBack="tagConfirmCallbacksupport()" popupOrder="support" ></iais:confirm>
<script type="text/javascript">
    function doVerifyFileGo(verify) {
        showWaiting();
        var data = {"repoId":verify};
        $.post(
            "${pageContext.request.contextPath}/verifyFileExist",
            data,
            function (data) {
                if(data != null ){
                 if(data.verify == 'N'){
                     $('#support').modal('show');
                 }else {
                     $("#"+verify+"Down").click();
                 }
                    dismissWaiting();
                }
            }
        )
    }

    function tagConfirmCallbacksupport(){
        $('#support').modal('hide');
    }
</script>



