<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="iais" uri="http://www.ecq.com/iais" %>

<%--@elvariable id="haveSuitableDraftData" type="java.lang.Boolean"--%>
<input type="hidden" id="haveSuitableDraftData" name="haveSuitableDraftData" value="${haveSuitableDraftData}" readonly disabled/>
<c:if test="${haveSuitableDraftData}">
<iais:confirm msg="There is an existing draft for the chosen service; if you choose to continue with a new application, the draft application will be discarded."
              callBack="cancelLoadDraftData()" popupOrder="haveData" yesBtnDesc="CONTINUE" cancelBtnDesc="RESUME FROM DRAFT" cancelBtnCls="btn btn-primary" yesBtnCls="btn btn-secondary" cancelFunc="loadDraftData()"/>
</c:if>