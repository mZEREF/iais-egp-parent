<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>

<c:forEach var="svcDoc" items="${doc.appSvcDocDtoList}" varStatus="status">
    <c:set var="oldSvcDoc" value="${oldDoc.appSvcDocDtoList[status.index]}" />
    <c:if test="${status.first}">
        <iais:row>
            <p class="col-sm-12">
                <strong><c:out value="${doc.displayTitle}"/></strong>
            </p>
        </iais:row>
    </c:if>
    <iais:row>
        <iais:value width="12" display="true" cssClass="col-md-10" style="font-size: 14px">
            <div class="col-xs-6">
                <div class="newVal" attr="${svcDoc.md5Code}<c:out value="${svcDoc.docName}"/>">
                    <c:choose>
                        <c:when test="${empty svcDoc.fileRepoId}">
                            <c:out value="${svcDoc.docName}"/>
                        </c:when>
                        <c:otherwise>
                            <iais:downloadLink fileRepoIdName="fileRo${status.index}" fileRepoId="${svcDoc.fileRepoId}" docName="${svcDoc.docName}"/>
                        </c:otherwise>
                    </c:choose> (${svcDoc.docSize} KB)
                </div>
            </div>
            <div class="col-xs-6">
                <div class="oldVal" attr="${oldSvcDoc.md5Code}<c:out value="${oldSvcDoc.docName}"/>">
                    <c:choose>
                        <c:when test="${empty oldSvcDoc.fileRepoId}">
                            <c:out value="${oldSvcDoc.docName}"/>
                        </c:when>
                        <c:otherwise>
                            <iais:downloadLink fileRepoIdName="fileRo${status.index}" fileRepoId="${oldSvcDoc.fileRepoId}" docName="${oldSvcDoc.docName}"/>
                        </c:otherwise>
                    </c:choose> (${oldSvcDoc.docSize} KB)
                </div>
            </div>
        </iais:value>
    </iais:row>
</c:forEach>