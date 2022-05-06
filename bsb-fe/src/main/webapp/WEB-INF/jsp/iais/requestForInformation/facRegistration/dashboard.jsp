<%@taglib prefix="common" tagdir="/WEB-INF/tags/common" %>
<%@taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--@elvariable id="SELECTED_CLASSIFICATION" type="java.lang.String"--%>
<%--@elvariable id="SELECTED_ACTIVITIES" type="java.util.List"--%>
<common:dashboard>
    <jsp:attribute name="titleFrag">
        <h1>New Facility Registration</h1>
        <p>You are applying for <strong><iais:code code="${SELECTED_CLASSIFICATION}"/></strong> with activity type:</p>
        <p>
            <c:forEach var="activity" items="${SELECTED_ACTIVITIES}" varStatus="status">
                <c:if test="${status.index > 0}"> | </c:if>
                <strong><iais:code code="${activity}"/></strong>
            </c:forEach>
        </p>
    </jsp:attribute>
</common:dashboard>