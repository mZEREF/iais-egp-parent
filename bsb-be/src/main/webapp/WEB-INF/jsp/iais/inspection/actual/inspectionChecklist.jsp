<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<c:if test="${!commonDto.moreOneDraft || inspectionNcCheckListDelegator_before_finish_check_list == '1'}">
    <c:if test="${specialServiceForChecklistDecide == '1'}">
        <%@include file="/WEB-INF/jsp/iais/inspection/nc/checkListSpecialService/checkListSpecService.jsp"%>
    </c:if>
    <c:if test="${specialServiceForChecklistDecide != '1'}">
        <%@include file="/WEB-INF/jsp/iais/inspection/nc/viewCheckList.jsp"%>
    </c:if>
</c:if>
<c:if test="${commonDto.moreOneDraft && inspectionNcCheckListDelegator_before_finish_check_list != '1'}">
    <c:if test="${specialServiceForChecklistDecide == '1'}">
        <%@include file="/WEB-INF/jsp/iais/inspection/nc/checkListSpecialService/checkListSpecServiceMore.jsp"%>
    </c:if>
    <c:if test="${specialServiceForChecklistDecide != '1'}">
        <%@include file="/WEB-INF/jsp/iais/inspection/nc/viewCheckListMore.jsp"%>
    </c:if>
</c:if>