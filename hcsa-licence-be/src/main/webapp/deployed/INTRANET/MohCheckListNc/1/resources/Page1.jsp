<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<c:if test="${!commonDto.moreOneDraft}">
<%@include file="/WEB-INF/jsp/iais/inspectionncList/viewCheckList.jsp"%>
</c:if>
<c:if test="${commonDto.moreOneDraft}">
    <%@include file="/WEB-INF/jsp/iais/inspectionncList/viewCheckListMore.jsp"%>
</c:if>
