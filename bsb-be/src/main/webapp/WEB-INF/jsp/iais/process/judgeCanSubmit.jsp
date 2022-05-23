<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>

<%--@elvariable id="canSubmit" type="java.lang.String"--%>
<input type="hidden" id="canSubmit" name="canSubmit" value="${canSubmit}" readonly disabled/>
<c:if test="${canSubmit eq 'N'}">
<iais:confirm msg="The AFC Certification task has not been completed and cannot be submitted for the time being" callBack="cancelJump()" popupOrder="afterCanNotSubmit" yesBtnDesc="continue" cancelBtnDesc="exit to task list" cancelBtnCls="btn btn-primary" yesBtnCls="btn btn-secondary" cancelFunc="jumpToTaskList()"/>
</c:if>