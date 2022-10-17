<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>

<c:forEach var="svcDoc" items="${doc.appSvcDocDtoList}" varStatus="status">
    <c:if test="${status.first}">
        <iais:row>
            <p class="col-sm-12">
                <strong><c:out value="${doc.displayTitle}"/></strong>
            </p>
        </iais:row>
    </c:if>
    <iais:row>
        <iais:value width="12" display="true" cssClass="col-md-10" style="font-size: 14px">
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
    </iais:row>
</c:forEach>