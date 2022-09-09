<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<c:choose>
    <c:when test="${crud_action_type_value == 'licensee'}">
        <jsp:include page="/WEB-INF/jsp/iais/application/licensee.jsp" />
    </c:when>
    <c:when test="${crud_action_type_value == 'premises'}">
        <jsp:include page="/WEB-INF/jsp/iais/application/premises.jsp" />
    </c:when>
    <c:when test="${crud_action_type_value == 'specialised'}">
        <jsp:include page="/WEB-INF/jsp/iais/application/specialised.jsp" />
    </c:when>
</c:choose>