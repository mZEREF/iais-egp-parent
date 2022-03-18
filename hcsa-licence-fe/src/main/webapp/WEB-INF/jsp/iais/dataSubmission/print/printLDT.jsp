<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<c:set value="${LdtSuperDataSubmissionDto != null && LdtSuperDataSubmissionDto.getDataSubmissionDto() != null && 'DSTY_005'.equalsIgnoreCase(LdtSuperDataSubmissionDto.getDataSubmissionDto().getAppType())}"
       var="isRfc"/>
<c:if test="${isRfc}">
    <h3>Amendment</h3>
</c:if>
<c:if test="${!isRfc}">
    <h3>Laboratory Develop Test</h3>
</c:if>
<%@include file="../labDevelopedTest/section/prviewLdtSection.jsp" %>