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
                        <c:when test="${!empty printFlag}">
                            ${svcDoc.docName}
                        </c:when>
                        <c:when test="${empty svcDoc.fileRepoId}">
                            ${svcDoc.docName}
                        </c:when>
                        <c:otherwise>
                            <iais:downloadLink fileRepoIdName="fileRo${status.index}" fileRepoId="${svcDoc.fileRepoId}" docName="${svcDoc.docName}"/>
                        </c:otherwise>
                    </c:choose> (${svcDoc.docSize} KB)
                </span>
            </div>
        </td>
    </tr>
</c:forEach>