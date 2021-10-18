<div class="amended-service-info-gp">
    <label style="font-size: 2.2rem">${currStepName}</label>
    <div class="amend-preview-info">
        <p></p>
        <div class="form-check-gp">
            <div class="row">
                <div class="col-xs-12">
                    <table aria-describedby="" class="col-xs-12">
                        <thead style="display: none">
                        <tr><th scope="col"></th> </tr>
                        </thead>
                        <%--<c:forEach var="svcDoc" items="${currentPreviewSvcInfo.appSvcDocDtoLit}" varStatus="status">
                            <tr>
                                <td>
                                    <div class="field col-sm-6 control-label formtext" style="font-size:16px;"><label>${svcDoc.upFileName}:</label></div>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <span class="fileType" style="display:none">Docment1</span><span class="fileFilter" style="display:none">png</span><span class="fileMandatory" style="display:none">Yes</span>
                                </td>
                            </tr>
                            <tr class="col-xs-12">
                                <td>
                                    <div class="fileList">
                                        <span class="filename server-site" id="130">
                                            <c:choose>
                                                <c:when test="${!svcDoc.passValidate}">
                                                    ${svcDoc.docName}
                                                </c:when>
                                                <c:otherwise>
                                                    <a href="${pageContext.request.contextPath}/file-repo?filerepo=fileRo${status.index}&fileRo${status.index}=<iais:mask name="fileRo${status.index}" value="${svcDoc.fileRepoId}"/>&fileRepoName=${svcDoc.docName}" title="Download" class="downloadFile">${svcDoc.docName}</a>
                                                </c:otherwise>
                                            </c:choose> (${svcDoc.docSize} KB)
                                        </span>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>--%>
                            <c:set var="reloadMap" value="${currentPreviewSvcInfo.multipleSvcDoc}"/>
                            <c:forEach var="config" items="${svcDocConfig}" varStatus="configStat">
                                <c:choose>
                                    <c:when test="${'0' == config.dupForPrem}">
                                        <c:choose>
                                            <c:when test="${empty config.dupForPerson}">
                                                <c:set var="fileList" value="${reloadMap[config.id]}"/>
                                                <%@include file="previewSvcDocContent.jsp"%>
                                            </c:when>
                                            <c:otherwise>
                                                <c:set var="premIndexNo" value=""/>
                                                <%@include file="previewDupForPerson.jsp"%>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:when>
                                    <c:when test="${'1' == config.dupForPrem}">
                                        <c:forEach var="prem" items="${AppSubmissionDto.appGrpPremisesDtoList}" varStatus="premStat">
                                            <c:set var="premIndexNo" value="${prem.premisesIndexNo}"/>
                                            <c:choose>
                                                <c:when test="${empty config.dupForPerson}">
                                                    <c:set var="mapKey" value="${premIndexNo}${config.id}"/>
                                                    <c:set var="fileList" value="${reloadMap[mapKey]}"/>
                                                    <%@include file="previewSvcDocContent.jsp"%>
                                                </c:when>
                                                <c:otherwise>
                                                    <%@include file="previewDupForPerson.jsp"%>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:forEach>
                                    </c:when>
                                </c:choose>
                            </c:forEach>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>

