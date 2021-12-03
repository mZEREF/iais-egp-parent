<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page import="java.nio.charset.StandardCharsets" %>
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
                    <th scope="col" width="20%">Document</th>
                    <th scope="col" width="20%">File</th>
                    <th scope="col" width="15%">Size</th>
                    <%--                    <th width="20%">Version</th>--%>
                    <th scope="col" width="20%">Submitted By</th>
                    <th scope="col" width="25%">Date Submitted</th>
                </tr>
                </thead>
                <tbody>
                <%--                <c:if test="${applicationViewDto.applicationDto.applicationType =='APTY008' || applicationViewDto.applicationDto.applicationType == 'APTY007'}">--%>
                <%--                    <tr>--%>
                <%--                        <td colspan="6" align="left">--%>
                <%--                            <iais:message key="GENERAL_ACK018"--%>
                <%--                                          escape="true"/>--%>
                <%--                        </td>--%>
                <%--                    </tr>--%>
                <%--                </c:if>--%>

                <%--                <c:if test="${applicationViewDto.applicationDto.applicationType !='APTY008' && applicationViewDto.applicationDto.applicationType != 'APTY007'}">--%>
                <%--                    <c:if test="${empty applicationViewDto.appSupDocDtoList}">--%>
                <%--                        <tr>--%>
                <%--                            <td colspan="6" align="left">--%>
                <%--                                <iais:message key="GENERAL_ACK018"--%>
                <%--                                              escape="true"/>--%>
                <%--                            </td>--%>
                <%--                        </tr>--%>
                <%--                    </c:if>--%>
                <%--                <c:forEach items="${auditDocDto.facilityDocs}" var="appSupDocDto">--%>
                <%--                    <tr>--%>
                <%--                        <td width="30%">--%>
                <%--                            <p><c:out value="${appSupDocDto.name}"></c:out></p>--%>
                <%--                        </td>--%>
                <%--                        <td width="20%">--%>
                <%--                            <p>--%>
                <%--                                    &lt;%&ndash;                                    <a hidden href="${pageContext.request.contextPath}/file-repo?filerepo=fileRo${status.index}&fileRo${status.index}=<iais:mask name="fileRo${status.index}"  value="${appSupDocDto.fileRepoId}"/>&fileRepoName=${URLEncoder.encode(appSupDocDto.document, StandardCharsets.UTF_8.toString())}"&ndash;%&gt;--%>
                <%--                                    &lt;%&ndash;                                       title="Download" class="downloadFile"><span id="${appSupDocDto.fileRepoId}Down">trueDown</span> </a>&ndash;%&gt;--%>
                <%--                                    &lt;%&ndash;                                    <a onclick="doVerifyFileGo('${appSupDocDto.fileRepoId}')">&ndash;%&gt;--%>
                <%--                                <c:out value="${appSupDocDto.name}"></c:out>--%>
                <%--                                    &lt;%&ndash;                                    </a>&ndash;%&gt;--%>
                <%--                            </p>--%>
                <%--                        </td>--%>
                <%--                        <td width="10%">--%>
                <%--                            <p><c:out value="${appSupDocDto.size}"></c:out></p>--%>
                <%--                        </td>--%>
                <%--                            &lt;%&ndash;                            <td width="20%">&ndash;%&gt;--%>
                <%--                            &lt;%&ndash;                                <p><c:out value="${appSupDocDto.version}"></c:out></p>&ndash;%&gt;--%>
                <%--                            &lt;%&ndash;                            </td>&ndash;%&gt;--%>
                <%--                        <td width="10%">--%>
                <%--                            <p><c:out value="${appSupDocDto.submitByName}"></c:out></p>--%>
                <%--                        </td>--%>
                <%--                        <td width="10%">--%>
                <%--                            <p><fmt:formatDate value='${appSupDocDto.submitAt}' pattern='dd/MM/yyyy'/></p>--%>
                <%--                        </td>--%>
                <%--                    </tr>--%>
                <%--                </c:forEach>--%>
                <%--                </c:if>--%>
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
                    <th scope="col" width="20%">Document</th>
                    <th scope="col" width="20%">File</th>
                    <th scope="col" width="10%">Size</th>
                    <th scope="col" width="20%">Submitted By</th>
                    <th scope="col" width="20%">Date Submitted</th>
                    <th scope="col" width="10%">Action</th>
                </tr>
                </thead>
                <tbody id="tbodyFileListId">
                <%--                    <c:choose>--%>
                <%--                        <c:when test="${empty applicationViewDto.appIntranetDocDtoList}">--%>
                <%--                            <tr>--%>
                <%--                                <td colspan="6" align="left">--%>
                <%--                                    <iais:message key="GENERAL_ACK018"--%>
                <%--                                                  escape="true"/>--%>
                <%--                                </td>--%>
                <%--                            </tr>--%>
                <%--                        </c:when>--%>
                <%--                        <c:otherwise>--%>
                <c:if test="${auditDocDto.facilityDocs ne null}">
                <c:forEach items="${auditDocDto.facilityDocs}" var="appSupDocDto" varStatus="status">
                    <%--                                                <c:if test="${applicationViewDto.applicationDto.applicationType != ApplicationConsts.APPLICATION_TYPE_CREATE_AUDIT_TASK || (applicationViewDto.applicationDto.applicationType == ApplicationConsts.APPLICATION_TYPE_CREATE_AUDIT_TASK && interalFile.appDocType != ApplicationConsts.APP_DOC_TYPE_SELF_DEC_FORM)}">--%>
                    <tr>
                        <input name="have" id="have" value="haveFile" hidden>
                        <td width="20%">
                            <p><c:out value="${appSupDocDto.name}"></c:out></p>
                        </td>
                        <td width="20%">
                            <p>
                                <a hidden
                                   href="${pageContext.request.contextPath}/file-repo?filerepo=fileRo${status.index}&fileRo${status.index}=<iais:mask name="fileRo${status.index}" value="${appSupDocDto.fileRepoId}"/>&fileRepoName=${URLEncoder.encode(appSupDocDto.name, StandardCharsets.UTF_8.toString())}"
                                   title="Download" class="downloadFile"><span id="${appSupDocDto.fileRepoId}Down">trueDown</span>
                                </a>
                                <a onclick="doVerifyFileGo('${appSupDocDto.fileRepoId}')">
                                    <c:out value="${appSupDocDto.name}.${appSupDocDto.docType}"></c:out>
                                </a>
                            </p>
                        </td>
                        <td width="10%">
                            <p><c:out value="${appSupDocDto.size}"></c:out></p>
                        </td>
                            <%--                            <td width="20%">--%>
                            <%--                                <p><c:out value="${appSupDocDto.version}"></c:out></p>--%>
                            <%--                            </td>--%>
                        <td width="20%">
                            <p><c:out value="${appSupDocDto.submitByName}"></c:out></p>
                        </td>
                        <td width="20%">
                            <p><fmt:formatDate value='${appSupDocDto.submitAt}' pattern='dd/MM/yyyy'/></p>
                        </td>
                        <td width="10%">
                                <%--                                                        <c:if test="${interalFile.appDocType == ApplicationConsts.APP_DOC_TYPE_COM || interalFile.appDocType == ApplicationConsts.APP_DOC_TYPE_CHECK_LIST_MOBILE}">--%>
                            <button type="button" class="btn btn-secondary-del btn-sm"
                                    onclick="javascript:deleteFile(this,'<iais:mask name="fileRepoId" value="${appSupDocDto.fileRepoId}"/>');">
                                Delete
                            </button>
                                <%--                                                        </c:if>--%>
                        </td>
                    </tr>
                    <%--                                                </c:if>--%>
                </c:forEach>
                </c:if>
                <%--                        </c:otherwise>--%>
                <%--                    </c:choose>--%>

                </tbody>
            </table>
            <%--upload file--%>
            <iais:action>
                <a style="float:left;padding-top: 1.1%;" class="back" id="backFromDoc" href="#"><em
                        class="fa fa-angle-left"></em> Back</a>
                <button type="button" style="float:right" class="btn btn-primary" data-toggle="modal"
                        data-target="#uploadDoc">
                    Upload Document
                </button>
            </iais:action>
        </div>
    </div>
</div>
<script type="text/javascript">
    function doVerifyFileGo(verify) {
        showWaiting();
        var data = {"repoId": verify};
        $.post(
            "${pageContext.request.contextPath}/verifyFileExist",
            data,
            function (data) {
                if (data != null) {
                    if (data.verify == 'N') {
                        $('#support').modal('show');
                    } else {
                        $("#" + verify + "Down").click();
                    }
                    dismissWaiting();
                }
            }
        )
    }

    function tagConfirmCallbacksupport() {
        $('#support').modal('hide');
    }
</script>



