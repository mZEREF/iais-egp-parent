<%@taglib prefix="common" tagdir="/WEB-INF/tags/common" %>
<%@taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%--@elvariable id="processType" type="java.lang.String"--%>
<%--@elvariable id="editJudge" type="java.lang.Boolean"--%>
<common:dashboard>
    <jsp:attribute name="titleFrag">
        <c:choose>
            <c:when test="${!editJudge}">
                <h1>Application for Approval</h1>
                <c:if test="${processType ne null}">
                    <h3>You are applying for <strong><iais:code code="${processType}"/></strong></h3>
                </c:if>
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