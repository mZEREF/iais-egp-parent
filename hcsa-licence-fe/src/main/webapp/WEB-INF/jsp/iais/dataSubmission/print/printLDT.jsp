<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<c:if test="${empty title}">
    <h3>Data Submission</h3>
</c:if>
<c:if test="${not empty title}">
    <h3>${title}</h3>
</c:if>
<%@include file="../labDevelopedTest/section/prviewLdtSection.jsp" %>