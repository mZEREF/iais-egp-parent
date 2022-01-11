<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>

<%--@elvariable id="AFTER_SAVE_AS_DRAFT" type="java.lang.Boolean"--%>
<input type="hidden" id="afterSaveAsDraft" name="afterSaveAsDraft" value="${AFTER_SAVE_AS_DRAFT}" readonly disabled/>
<c:if test="${AFTER_SAVE_AS_DRAFT}">
<iais:confirm msg="This application has been saved successfully" callBack="cancelJumpAfterDraft()" popupOrder="afterSaveDraft" yesBtnDesc="continue" cancelBtnDesc="exit to inbox" cancelBtnCls="btn btn-primary" yesBtnCls="btn btn-secondary" cancelFunc="jumpAfterDraft()"/>
</c:if>