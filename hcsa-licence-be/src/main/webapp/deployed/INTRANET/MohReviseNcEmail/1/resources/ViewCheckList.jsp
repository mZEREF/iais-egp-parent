<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<c:if test="${specialServiceForChecklistDecide == '1'}">
    <%@include file="/WEB-INF/jsp/iais/inspectionncList/checkListSpecialService/checkListSpecService.jsp"%>
</c:if>
<c:if test="${specialServiceForChecklistDecide != '1'}">
    <%@include file="/WEB-INF/jsp/iais/inspectionncList/viewCheckList.jsp"%>
</c:if>