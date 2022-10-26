<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>

<%--@elvariable id="AFTER_SAVE_REPORT" type="java.lang.Boolean"--%>
<input type="hidden" id="afterSaveAsReport" name="afterSaveAsReport" value="${AFTER_SAVE_REPORT}" readonly disabled/>
<c:if test="${AFTER_SAVE_REPORT}">
    <iais:confirm msg="BISINSACK001" popupOrder="afterSaveReport" callBack="cancelJumpAfterReport()" yesBtnDesc="continue" yesBtnCls="btn btn-secondary" cancelBtnDesc="exit to task" cancelBtnCls="btn btn-primary" cancelFunc="jumpAfterReport()"/>
</c:if>