<c:set var="cL" value="${currSvcInfoDto.appPremOutSourceLicenceDto}"/>
<c:if test="${!empty cL.clinicalLaboratoryList}">
    <c:set var="clen" value="${cL.clinicalLaboratoryList.size()}"/>
    <input name="clenght" value="${clen}" type="hidden">
    <c:forEach end="${clen-1}" begin="0" step="1" varStatus="c">
        <c:set var="index" value="${c.index}" />
        <c:set var="clinicalLaboratoryList" value="${cL.clinicalLaboratoryList[index]}"/>
        <c:set var="appPremOutSourceLicenceDto" value="${msgTemplateResult.appPremOutSourceLicenceDto}"/>
        <input value="${appPremOutSourceLicenceDto.licenceNo}" name="licNo${index}" type="hidden">
        <input value="${appPremOutSourceLicenceDto.agreementStartDate}" name="agreementStartDate${index}" type="hidden">
        <input value="${appPremOutSourceLicenceDto.outstandingScope}" name="outstandingScope${index}" type="hidden">
        <input value="${appPremOutSourceLicenceDto.agreementEndDate}" name="agreementEndDate${index}" type="hidden">
        <input value="${clinicalLaboratoryList.businessName}" name="bName${index}" type="hidden">
        <input value="${clinicalLaboratoryList.address}" name="addr${index}" type="hidden">
        <input value="${clinicalLaboratoryList.expiryDate}" name="expriyDate${index}" type="hidden">
    </c:forEach>
</c:if>
<c:if test="${!empty cL.radiologicalServiceList}">
    <c:set var="rlen" value="${cL.radiologicalServiceList.size()}"/>
    <input name="rlenght" value="${rlen}" type="hidden">
    <c:forEach end="${rlen-1}" begin="0" step="1" varStatus="r">
        <c:set var="index" value="${r.index}" />
        <c:set var="radiologicalServiceList" value="${cL.radiologicalServiceList[index]}"/>
        <c:set var="appPremOutSourceLicenceDto" value="${msgTemplateResult.appPremOutSourceLicenceDto}"/>
        <input value="${appPremOutSourceLicenceDto.licenceNo}" name="licNo${index}" type="hidden">
        <input value="${appPremOutSourceLicenceDto.agreementStartDate}" name="agreementStartDate${index}" type="hidden">
        <input value="${appPremOutSourceLicenceDto.outstandingScope}" name="outstandingScope${index}" type="hidden">
        <input value="${appPremOutSourceLicenceDto.agreementEndDate}" name="agreementEndDate${index}" type="hidden">
        <input value="${radiologicalServiceList.businessName}" name="bName${index}" type="hidden">
        <input value="${radiologicalServiceList.address}" name="addr${index}" type="hidden">
        <input value="${radiologicalServiceList.expiryDate}" name="expriyDate${index}" type="hidden">
    </c:forEach>
</c:if>