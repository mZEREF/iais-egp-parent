<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
    String webroot1 = IaisEGPConstant.CSS_ROOT + IaisEGPConstant.FE_CSS_ROOT;
%>
<webui:setLayout name="iais-internet"/>
<script type="text/javascript" src="<%=webroot1%>js/dataSubmission/ar_common.js"></script>
<script type="text/javascript" src="<%=webroot1%>js/dataSubmission/transferInOutStageReceiveSection.js"></script>
<div class="center-content">
    <div class="intranet-content">
        <div class="bg-title">
            <c:if test="${not empty bindStageIsRfc}">
                <h2>${bindStageIsRfc}</h2>
            </c:if>
            <c:if test="${not empty hasConfirmationStage}">
                <h2>${hasConfirmationStage}</h2>
            </c:if>
        </div>
    </div>
</div>
<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <%@ include file="../common/formHidden.jsp" %>
    <c:if test="${not empty bindStageIsRfc}">
        <iais:confirm
                msg="${bindStageIsRfc}"
                callBack="$('#inactionModal').modal('hide');mySubmit('return');" popupOrder="inactionModal"
                yesBtnDesc="Ok"
                yesBtnCls="btn btn-secondary" needFungDuoJi="false"
                needCancel="false"/>
    </c:if>
    <c:if test="${not empty hasConfirmationStage}">
        <iais:confirm msg="${hasConfirmationStage}"
                      callBack="$('#hasConfirmationModal').modal('hide');mySubmit('return');"
                      popupOrder="hasConfirmationModal" yesBtnDesc="Ok"
                      yesBtnCls="btn btn-secondary" needFungDuoJi="true" needCancel="false"/>
    </c:if>
</form>
