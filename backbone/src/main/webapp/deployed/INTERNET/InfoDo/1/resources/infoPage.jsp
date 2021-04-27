<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<c:if test="${section == 'PrivacyStatement'}">
    <%@include file="/WEB-INF/jsp/common/privacystate.jsp"%>
</c:if>
<c:if test="${section == 'TermsOfUse'}">
    <%@include file="/WEB-INF/jsp/common/termsofuse.jsp"%>
</c:if>