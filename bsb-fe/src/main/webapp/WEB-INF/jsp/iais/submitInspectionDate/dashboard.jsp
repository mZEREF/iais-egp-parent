<%@taglib prefix="common" tagdir="/WEB-INF/tags/common" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<common:dashboard>
    <jsp:attribute name="titleFrag">
        <%--@elvariable id="title" type="java.lang.String"--%>
        <h1><c:out value="${title}"/></h1>
    </jsp:attribute>
</common:dashboard>