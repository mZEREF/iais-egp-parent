<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<div class="row normal-label">
    <%@include file="outsourceService.jsp"%>
    <c:if test="${!empty outSourceParam}">
        <%@include file="clinicalLaboratory.jsp"%>
        <%@include file="radiologicalService.jsp"%>
    </c:if>
</div>