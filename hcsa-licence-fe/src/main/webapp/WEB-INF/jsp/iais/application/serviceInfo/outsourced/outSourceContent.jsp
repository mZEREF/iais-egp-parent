<%--<c:set var="cL" value="${currSvcInfoDto.appPremOutSourceProvidersList}"/>--%>
<%--<c:if test="${!empty ids}">--%>
<%--    <c:set var="len" value="${ids.size()}"/>--%>
<%--    <input name="idLenght" value="${len}" type="hidden">--%>
<%--    <c:forEach end="${len-1}" begin="0" step="1" varStatus="s">--%>
<%--        <c:set var="index" value="${s.index}" />--%>
<%--        <c:set var="ids" value="${ids[index]}"/>--%>
<%--        <input value="${ids}" name="ids${index}" type="hidden">--%>
<%--    </c:forEach>--%>
<%--</c:if>--%>
<c:if test="${!empty cL.clinicalLaboratoryList}">
    <c:set var="clen" value="${cL.clinicalLaboratoryList.size()}"/>
    <input name="clenght" value="${clen}" type="hidden">
    <c:forEach end="${clen-1}" begin="0" step="1" varStatus="c">
        <c:set var="index" value="${c.index}" />
        <c:set var="clinicalLaboratoryList" value="${cL.clinicalLaboratoryList[index]}"/>
        <input value="${clinicalLaboratoryList.licenceNo}" name="licNo${index}" type="hidden">
        <input value="${clinicalLaboratoryList.agreementStartDate}" name="agreementStartDate${index}" type="hidden">
        <input value="${clinicalLaboratoryList.outstandingScope}" name="outstandingScope${index}" type="hidden">
        <input value="${clinicalLaboratoryList.agreementEndDate}" name="agreementEndDate${index}" type="hidden">
        <input value="${clinicalLaboratoryList.businessName}" name="bName${index}" type="hidden">
        <input value="${clinicalLaboratoryList.address}" name="addr${index}" type="hidden">
        <input value="${clinicalLaboratoryList.expiryDate}" name="expriyDate${index}" type="hidden">
        <input value="${clinicalLaboratoryList.status}" name="status${index}" type="hidden">
    </c:forEach>
</c:if>
<c:if test="${!empty cL.radiologicalServiceList}">
    <c:set var="rlen" value="${cL.radiologicalServiceList.size()}"/>
    <input name="rlenght" value="${rlen}" type="hidden">
    <c:forEach end="${rlen-1}" begin="0" step="1" varStatus="r">
        <c:set var="index" value="${r.index}" />
        <c:set var="radiologicalServiceList" value="${cL.radiologicalServiceList[index]}"/>
        <input value="${radiologicalServiceList.licenceNo}" name="licNo${index}" type="hidden">
        <input value="${radiologicalServiceList.agreementStartDate}" name="agreementStartDate${index}" type="hidden">
        <input value="${radiologicalServiceList.outstandingScope}" name="outstandingScope${index}" type="hidden">
        <input value="${radiologicalServiceList.agreementEndDate}" name="agreementEndDate${index}" type="hidden">
        <input value="${radiologicalServiceList.businessName}" name="bName${index}" type="hidden">
        <input value="${radiologicalServiceList.address}" name="addr${index}" type="hidden">
        <input value="${radiologicalServiceList.expiryDate}" name="expriyDate${index}" type="hidden">
        <input value="${radiologicalServiceList.status}" name="status${index}" type="hidden">
    </c:forEach>
</c:if>