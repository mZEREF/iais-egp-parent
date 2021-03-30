<c:forEach var="svcDoc" items="${fileList}" varStatus="status">
    <c:if test="${status.first}">
        <tr>
            <td>
                <div class="field col-sm-12 control-label formtext" style="font-size:16px;"><label>${svcDoc.displayTitle}</label></div>
            </td>
        </tr>
    </c:if>
    <tr class="col-xs-12">
        <td>
            <div class="fileList">
                <span class="filename server-site" id="130">
                    <c:choose>
                        <c:when test="${empty svcDoc.fileRepoId}">
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
</c:forEach>