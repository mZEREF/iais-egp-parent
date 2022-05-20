<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page import="java.nio.charset.StandardCharsets" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts" %>
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
            <table aria-describedby="" class="table">
                <thead>
                <tr>
                    <th scope="col" width="30%">Document</th>
                    <th scope="col" width="20%">File</th>
                    <th scope="col" width="10%">Size</th>
                    <th scope="col"
                        <c:if test="${iais_Audit_Trail_dto_Attr.functionName != AuditTrailConsts.FUNCTION_ONLINE_ENQUIRY}">width="20%"</c:if>
                        <c:if test="${iais_Audit_Trail_dto_Attr.functionName == AuditTrailConsts.FUNCTION_ONLINE_ENQUIRY}">width="10%"</c:if>
                    >Version</th>
                    <th scope="col"
                        <c:if test="${iais_Audit_Trail_dto_Attr.functionName != AuditTrailConsts.FUNCTION_ONLINE_ENQUIRY}">width="10%"</c:if>
                        <c:if test="${iais_Audit_Trail_dto_Attr.functionName == AuditTrailConsts.FUNCTION_ONLINE_ENQUIRY}">width="20%"</c:if>
                    >Submitted By</th>
                    <th scope="col" width="10%">Date Submitted</th>
                </tr>
                </thead>
                <tbody>
                <c:if test="${applicationViewDto.applicationDto.applicationType =='APTY008' || applicationViewDto.applicationDto.applicationType == 'APTY007'}">
                    <tr>
                        <td colspan="6" align="left">
                            <iais:message key="GENERAL_ACK018"
                                          escape="true"/>
                        </td>
                    </tr>
                </c:if>

                <c:if test="${applicationViewDto.applicationDto.applicationType !='APTY008' && applicationViewDto.applicationDto.applicationType != 'APTY007'}">
                    <c:if test="${empty applicationViewDto.appSupDocDtoList}">
                        <tr>
                            <td colspan="6" align="left">
                                <iais:message key="GENERAL_ACK018"
                                              escape="true"/>
                            </td>
                        </tr>
                    </c:if>
                    <c:forEach items="${applicationViewDto.appSupDocDtoList}"
                               var="appSupDocDto">
                        <tr>
                            <td >
                                <p><c:out value="${appSupDocDto.file}"></c:out></p>
                            </td>
                            <td >
                                <p>
                                    <a hidden href="${pageContext.request.contextPath}/file-repo?filerepo=fileRo${status.index}&fileRo${status.index}=<iais:mask name="fileRo${status.index}"  value="${appSupDocDto.fileRepoId}"/>&fileRepoName=${URLEncoder.encode(appSupDocDto.document, StandardCharsets.UTF_8.toString())}"
                                       title="Download" class="downloadFile"><span id="${appSupDocDto.fileRepoId}Down">trueDown</span> </a>
                                    <a href="javascript:void(0);" onclick="doVerifyFileGo('${appSupDocDto.fileRepoId}')"><c:out value="${appSupDocDto.document}"></c:out></a>
                                </p>
                            </td>
                            <td >
                                <p><c:out value="${appSupDocDto.size}"></c:out></p>
                            </td>
                            <td >
                                <p><c:out value="${appSupDocDto.version}"></c:out></p>
                            </td>
                            <td >
                                <p><c:out value="${appSupDocDto.submittedBy}"></c:out></p>
                            </td>
                            <td >
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
            <table aria-describedby="" class="table">
                <thead>
                <tr>
                    <th scope="col" width="30%">Document</th>
                    <th scope="col" width="20%">File</th>
                    <th scope="col"
                        <c:if test="${iais_Audit_Trail_dto_Attr.functionName != AuditTrailConsts.FUNCTION_ONLINE_ENQUIRY}">width="10%"</c:if>
                        <c:if test="${iais_Audit_Trail_dto_Attr.functionName == AuditTrailConsts.FUNCTION_ONLINE_ENQUIRY}">width="20%"</c:if>
                    >Size</th>
                    <th scope="col" width="20%">Submitted By</th>
                    <th scope="col" width="10%">Date Submitted</th>
                    <c:if test="${iais_Audit_Trail_dto_Attr.functionName != AuditTrailConsts.FUNCTION_ONLINE_ENQUIRY}">
                        <th scope="col" width="10%">Action</th>
                    </c:if>
                </tr>
                </thead>
                <tbody id="tbodyFileListId">
                    <c:choose>
                        <c:when test="${empty applicationViewDto.appIntranetDocDtoList}">
                            <tr>
                                <td colspan="6" align="left">
                                    <iais:message key="GENERAL_ACK018"
                                                  escape="true"/>
                                </td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="interalFile" items="${applicationViewDto.appIntranetDocDtoList}"
                                       varStatus="status">
                                <c:if test="${applicationViewDto.applicationDto.applicationType != ApplicationConsts.APPLICATION_TYPE_CREATE_AUDIT_TASK || (applicationViewDto.applicationDto.applicationType == ApplicationConsts.APPLICATION_TYPE_CREATE_AUDIT_TASK && interalFile.appDocType != ApplicationConsts.APP_DOC_TYPE_SELF_DEC_FORM)}">
                                <tr>
                                    <td >
                                        <p>
                                            <c:if test="${interalFile.appDocType == ApplicationConsts.APP_DOC_TYPE_CHECK_LIST}">
                                                Letter Written to Licensee
                                            </c:if>
                                            <c:if test="${interalFile.appDocType != ApplicationConsts.APP_DOC_TYPE_CHECK_LIST}">
                                             <c:out value="${interalFile.docDesc}"></c:out>
                                            </c:if>
                                        </p>
                                    </td>
                                    <td >
                                        <p>
                                            <a hidden href="${pageContext.request.contextPath}/file-repo?filerepo=fileRo${status.index}&fileRo${status.index}=<iais:mask name="fileRo${status.index}"  value="${interalFile.fileRepoId}"/>&fileRepoName=${URLEncoder.encode(interalFile.docName, StandardCharsets.UTF_8.toString())}.${interalFile.docType}"
                                               title="Download" class="downloadFile"><span id="${interalFile.fileRepoId}Down">trueDown</span></a>
                                            <a href="javascript:void(0);" onclick="doVerifyFileGo('${interalFile.fileRepoId}')"><c:out
                                                    value="${interalFile.docName}.${interalFile.docType}"></c:out>
                                            </a>
                                        </p>
                                    </td>
                                    <td >
                                        <p><c:out value="${interalFile.docSize}"></c:out></p>
                                    </td>
                                    <td >
                                        <p><c:out value="${interalFile.submitByName}"></c:out></p>
                                    </td>
                                    <td >
                                        <p>${interalFile.submitDtString}</p>
                                    </td>
                                <c:if test="${iais_Audit_Trail_dto_Attr.functionName != AuditTrailConsts.FUNCTION_ONLINE_ENQUIRY}">
                                    <td >
                                        <c:if test="${interalFile.appDocType == ApplicationConsts.APP_DOC_TYPE_COM || interalFile.appDocType == ApplicationConsts.APP_DOC_TYPE_CHECK_LIST_MOBILE}">
                                            <button type="button" class="btn btn-secondary-del btn-sm"
                                                    onclick="javascript:deleteFile(this,'<iais:mask name="interalFileId"
                                                                                                    value="${interalFile.id}"/>');">
                                                Delete</button>
                                        </c:if>
                                    </td>
                                </c:if>
                                </tr>
                                </c:if>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>

                </tbody>
            </table>
            <%--upload file--%>
            <c:if test="${iais_Audit_Trail_dto_Attr.functionName != AuditTrailConsts.FUNCTION_ONLINE_ENQUIRY}">
                <iais:action>
                    <a style="float:left;padding-top: 1.1%;" class="back" href="/main-web/eservice/INTRANET/MohHcsaBeDashboard?dashProcessBack=1"><em class="fa fa-angle-left"></em> Back</a>
                    <button type="button" style="float:right" class="btn btn-primary" data-toggle="modal" data-target="#uploadDoc">
                        Upload Document
                    </button>
                </iais:action>
            </c:if>

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



