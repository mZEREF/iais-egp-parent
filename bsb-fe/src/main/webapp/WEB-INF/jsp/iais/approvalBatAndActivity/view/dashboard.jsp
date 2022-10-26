<%@taglib prefix="common" tagdir="/WEB-INF/tags/common" %>
<common:dashboard>
    <jsp:attribute name="titleFrag">
        <c:choose>
            <c:when test="${!editJudge}">
                <h1>View Application for Approval</h1>
            </c:when>
            <c:otherwise>
                <h1>Update Approval Information</h1>
                <c:if test="${processType ne null}">
                    <h3>You are amending for the <strong><iais:code code="${processType}"/></strong></h3>
                </c:if>
            </c:otherwise>
        </c:choose>
    </jsp:attribute>
</common:dashboard>