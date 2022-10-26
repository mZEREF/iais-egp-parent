<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>

<%--@elvariable id="finalRound" type="java.lang.Boolean"--%>
<input type="hidden" id="haveConfirm" name="haveConfirm" value=""/>
<input type="hidden" id="finalRound" name="finalRound" value="${finalRound}" readonly disabled/>
<%--<c:if test="${FINAL_ROUND}">--%>
    <iais:confirm msg="BISACKINS007" popupOrder="beforeSubmit" callBack="cancelBeforeSubmitModule()" yesBtnCls="btn btn-primary" needCancel="false"/>
<%--</c:if>--%>