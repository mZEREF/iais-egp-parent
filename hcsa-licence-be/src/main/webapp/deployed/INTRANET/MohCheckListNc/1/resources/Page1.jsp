<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<c:if test="${!commonDto.moreOneDraft || inspectionNcCheckListDelegator_before_finish_check_list == '1'}">
<%@include file="/WEB-INF/jsp/iais/inspectionncList/viewCheckList.jsp"%>
</c:if>
<c:if test="${commonDto.moreOneDraft && inspectionNcCheckListDelegator_before_finish_check_list != '1'}">
    <%@include file="/WEB-INF/jsp/iais/inspectionncList/viewCheckListMore.jsp"%>
</c:if>
