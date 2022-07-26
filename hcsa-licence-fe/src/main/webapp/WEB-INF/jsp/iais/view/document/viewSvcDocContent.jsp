<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<c:forEach var="svcDoc" items="${doc.appSvcDocDtoList}" varStatus="status">
    <c:if test="${status.first}">
        <iais:row>
            <div class="field col-sm-12 control-label formtext" style="font-size:16px;">
                <label><c:out value="${doc.displayTitle}"/></label>
            </div>
        </iais:row>
    </c:if>
    <div class="col-xs-12 fileList">
        <iais:value display="true">
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
        </iais:value>
    </div>
</c:forEach>